package com.alex.camito.action;

import java.util.ArrayList;

import com.alex.camito.cli.CliInjector;
import com.alex.camito.cli.CliTools;
import com.alex.camito.device.Device;
import com.alex.camito.misc.EmailManager;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.storedUUID;
import com.alex.camito.office.misc.Office;
import com.alex.camito.office.misc.OfficeTools;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.StatusType;



/**********************************
 * Class used to store a list of todo
 * 
 * It also allowed to launch the task
 * 
 * @author RATEL Alexandre
 **********************************/
public class Task extends Thread
	{
	/**
	 * Variables
	 */
	public enum taskActionType
		{
		start,
		stop,
		pause
		};
		
	public enum TaskStatus
		{
		init,
		preaudit,
		processing,
		postaudit,
		error,
		done
		};
	
	private ArrayList<Office> officeList;
	private TaskStatus status;
	private boolean pause, stop;
	private String taskID, ownerID;
	private ActionType action;
	private ThreadManager cliManager;
	private ThreadManager pingManager;
	private String statusDescription;
	
	/***************
	 * Constructor
	 ***************/
	public Task(ArrayList<Office> officeList, String id, String ownerID, ActionType action)
		{
		this.officeList = officeList;		
		this.status = TaskStatus.init;
		stop = false;
		pause = true;
		this.taskID = id;
		this.ownerID = ownerID;
		this.action = action;
		}
	
	public void run()
		{
		try
			{
			Variables.getLogger().info(action+" task "+taskID+" begins");
			
			if(!stop)build();
			
			if(action.equals(ActionType.reset))
				{
				/**
				 * Here we reset only the phones by reseting the device pool
				 * There is no action on devices
				 */
				pause = false;
				status = TaskStatus.preaudit;
				if(!stop)officeSurvey();
				status = TaskStatus.processing;
				if(!stop)reset();
				status = TaskStatus.postaudit;
				if(!stop)officeSurvey();
				}
			else if((action.equals(ActionType.migrate)) ||
					(action.equals(ActionType.rollback)))
				{
				/**
				 * Migration process will execute the following task to complete :
				 * - Search and correct the configuration differences between source and destination CUCM cluster
				 * - Ping the selected devices
				 * - Send cli command to the selected devices
				 * - Clear the bad phone configuration on the source cluster
				 * - Execute a first phone survey
				 * - Reset the selected offices's phones
				 * - Connect the device profile on the destination cluster
				 * - Configure line forward from source cluster to destination cluster
				 * - Configure specific user service profile for PCCE agents
				 * - Try to fix phone with migration issue
				 * - Test the selected office's did line
				 */
				status = TaskStatus.preaudit;
				if(!stop)officeSurvey();
				if(!stop)deviceSurvey();
				
				//We then wait for the user to accept the migration
				int counter = 0;
				while(pause && (!stop))
					{
					this.sleep(500);
					counter++;
					if(counter > 240)//240 = 2 minutes
						{
						Variables.getLogger().debug("Max time reached, we end the task");
						stop = true;
						}
					}
				
				if((pause == false) && (stop == false))Variables.getLogger().info(action+" task "+taskID+" starts");
				
				if(!stop)status = TaskStatus.processing;
				if(!stop)sendDeviceCli();
				if(!stop)reset();
				setOfficeMigrationStatus();
				if(!stop)status = TaskStatus.postaudit;
				officeSurvey();
				deviceSurvey();
				
				counter = 0;
				while((!stop) && (counter<12))
					{
					/**
					 * We proceed with the survey as long as the user want or max 2 minutes
					 * 
					 * This is necessary to allow phone or device to register after a reset
					 * Indeed, there is no point at raising a warning about a phone which is just taking a little time to register
					 * Instead it is better to try several times to reach it
					 */
					officeSurvey();
					deviceSurvey();
					counter++;
					this.sleep(10000);
					}
				}
			else
				{
				throw new Exception("Unsupported action : "+action);
				}
			
			if(status.equals(TaskStatus.postaudit) && (UsefulMethod.getTargetOption("smtpemailenable").equals("true")))new EmailManager(officeList);
			
			status = TaskStatus.done;
			Variables.getLogger().info(action+" task "+taskID+" ends");
			
			Variables.setUuidList(new ArrayList<storedUUID>());//We clean the UUID list
			Variables.getLogger().info("UUID list cleared");
			
			/**
			 * We write the phone status
			 */
			ArrayList<Office> ol = new ArrayList<Office>();
			for(Office o : officeList)
				{
				if(!o.getStatus().equals(StatusType.error))ol.add(o);
				}
			if(ol.size() > 0)OfficeTools.writePhoneSurveyToCSV(ol);
			
			/**
			 * We write the Cli get outputs
			 */
			if(Variables.getCliGetOutputList().size() > 0)
				{
				CliTools.writeCliGetOutputToCSV();
				}
			
			/**
			 * We write the overall result file
			 */
			OfficeTools.writeOverallResultToCSV(officeList);
			}
		catch (Exception e)
			{
			Variables.getLogger().debug("ERROR : "+e.getMessage(),e);
			status = TaskStatus.error;
			}
		}
	
	private void setOfficeStatus(StatusType status)
		{
		for(Office o : officeList)
			{
			if(o.getStatus().equals(StatusType.error))
				{
				//Nothing
				}
			else o.setStatus(status);
			}
		}
	
	/******
	 * Used to start the build process
	 * @throws Exception 
	 */
	private void build() throws Exception
		{
		Variables.getLogger().info("Starting build process");
		
		for(Office o : officeList)
			{
			if(stop)break;
			o.build();
			}
		
		Variables.getLogger().info("Build process ends");
		}
	
	private void setOfficeMigrationStatus()
		{
		ArrayList<String> idList = new ArrayList<String>();
		for(Office o : officeList)
			{
			if(!o.getStatus().equals(StatusType.error))
				{
				idList.add(o.getId());
				}
			}
		
		if(action.equals(ActionType.rollback))UsefulMethod.removeEntryToTheMigratedList(idList);
		else UsefulMethod.addEntryToTheMigratedList(idList);
		}
	
	private void officeSurvey()
		{
		Variables.getLogger().info("Starting office survey");
		ArrayList<Office> ol = new ArrayList<Office>();
		
		for(Office o : officeList)
			{
			if(o.getStatus().equals(StatusType.error))
				{
				Variables.getLogger().debug(o.getInfo()+" : Raised an error so we do not start the survey");
				}
			else
				{
				if(o.isExists())
					{
					ol.add(o);
					}
				}
			}
		
		if(ol.size() > 0)
			{
			OfficeTools.phoneSurvey(ol, action.equals(ActionType.rollback)?Variables.getDstcucm():Variables.getSrccucm(),
					action.equals(ActionType.rollback)?Variables.getSrccucm():Variables.getDstcucm());
			}
		
		Variables.getLogger().info("Office survey ends");
		}
	
	
	/**
	 * Start survey process
	 */
	private void deviceSurvey() throws Exception
		{
		Variables.getLogger().info("Starting device survey");
		
		/**
		 * First we ping the devices and disable the not reachable ones
		 */
		
		int pingTimeout = Integer.parseInt(UsefulMethod.getTargetOption("pingtimeout"));
		int maxThread = Integer.parseInt(UsefulMethod.getTargetOption("maxpingthread"));
		ArrayList<Device> deviceList = new ArrayList<Device>();
		
		for(Office o : officeList)
			{
			if(o.getStatus().equals(StatusType.error))
				{
				Variables.getLogger().debug(o.getInfo()+" : Raised an error so we do not start the survey");
				}
			else
				{
				deviceList.addAll(o.getDeviceList());
				}
			}
		
		Variables.getLogger().info("Pinging devices");
		
		if((deviceList == null) || (deviceList.size() == 0))
			{
			Variables.getLogger().debug("No device to ping");
			return;
			}
		
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		
		for(Device d : deviceList)
			{
			if(d.getStatus() == StatusType.error)
				{
				Variables.getLogger().debug(d.getInfo()+" : Raised an error so we do not start the survey");
				}
			else
				{
				threadList.add(new PingProcess(d, pingTimeout));
				}
			}
		
		pingManager = new ThreadManager(maxThread,
				100,
				threadList);
		
		pingManager.start();
		
		/**
		 * It is better to wait for the ping manager to end before continue
		 */
		Variables.getLogger().debug("We wait for the ping manager to end");
		while(pingManager.isAlive() && (!stop))
			{
			this.sleep(200);
			}
		Variables.getLogger().debug("Ping manager ends");
		
		Variables.getLogger().info("Device survey ends");
		}
	
	/**
	 * Send Device cli
	 * @throws Exception 
	 */
	private void sendDeviceCli() throws Exception
		{
		Variables.getLogger().info("Starting sending device cli");
		
		/**
		 * We take all the devices and start the cli update process
		 * Because each cliInjector is a different thread we can start them all
		 * simultaneously to save time
		 */
		
		ArrayList<Device> deviceList = new ArrayList<Device>();
		ArrayList<Thread> cliList = new ArrayList<Thread>();
		
		for(Office o : officeList)
			{
			if(o.getStatus().equals(StatusType.error))
				{
				Variables.getLogger().debug(o.getInfo()+" : Raised an error so we do not process its devices");
				}
			else
				{
				deviceList.addAll(o.getDeviceList());
				}
			}
		
		for(Device d : deviceList)
			{
			if(d.getStatus() == StatusType.error)
				{
				Variables.getLogger().debug(d.getInfo()+" : Raised an error so we do not start to send cli for it");
				}
			else
				{
				if(d.getCliProfile() != null)
					{
					CliInjector clii = new CliInjector(d);
					clii.resolve();
					cliList.add(clii);
					}
				}
			}
		
		cliManager = new ThreadManager(Integer.parseInt(UsefulMethod.getTargetOption("maxclithread")),
				100,
				cliList);
		
		if(cliList.size() == 0)
			{
			Variables.getLogger().debug("No device to update");
			}
		else if(!stop)
			{
			cliManager.start();
			
			/**
			 * It is better to wait for the cli task to end before starting the AXL ones
			 * For instance, it is pointless to reset a sip trunk before changing the ISR ip
			 */
			Variables.getLogger().debug("We wait for the cli manager to end");
			while(cliManager.isAlive() && (!stop))
				{
				this.sleep(500);
				}
			Variables.getLogger().debug("Cli manager ends");
			
			if(UsefulMethod.getTargetOption("smtperroremailenable").equals("true"))
				{
				/**
				 * If an error occurred we send an email to warn the main admin
				 */
				for(Thread t : cliManager.getThreadList())
					{
					CliInjector clii = (CliInjector)t;
					Device d = clii.getDevice();
					if(d.getStatus().equals(StatusType.error))
						{
						Variables.getLogger().debug("Error occurred with device "+d.getInfo()+" : sending email to the main admin");
						String adminEmail = UsefulMethod.getTargetOption("smtperroremail");
						
						try
							{
							StringBuffer content = new StringBuffer();
							content.append("An error occured with the following device : "+d.getInfo()+" , office "+d.getOfficeid()+"\t\r\n");
							for(ErrorTemplate e : d.getErrorList())
								{
								content.append("- "+e.getErrorDesc()+"\t\r\n");
								}
							Variables.geteMSender().send(adminEmail,
									Variables.getSoftwareName()+" : an error occurred with a device",
									content.toString(),
									"Error email");
							Variables.getLogger().debug("Error email sent for "+d.getInfo());
							}
						catch (Exception e)
							{
							Variables.getLogger().debug("Failed to send an error email to "+adminEmail+" : "+e.getMessage());
							}
						}
					}
				}
			}
		
		Variables.getLogger().info("Sending device cli ends");
		}
	
	/**
	 * Start reset process
	 */
	private void reset()
		{
		Variables.getLogger().info("Beginning the reset process");
		for(Office o : officeList)
			{
			try
				{
				if(stop)break;
				while(pause)
					{
					this.sleep(500);
					}
				if(o.getStatus().equals(StatusType.error))
					{
					Variables.getLogger().debug(o.getInfo()+" : Raised an error so we do not reset it");
					}
				else
					{
					/**
					 * The cluster to reset depends of the office status
					 * If migrated we reset the destination cluster
					 * If not we reset the source cluster
					 */
					o.reset(Variables.getMigratedItemList().contains(o.getId())?Variables.getDstcucm():Variables.getSrccucm());
					}
				}
			catch (InterruptedException e)
				{
				Variables.getLogger().error(o.getInfo()+" : ERROR while reseting : "+e.getMessage(), e);
				o.setStatus(StatusType.error);
				}
			}
		Variables.getLogger().info("End of the reset process");
		}
	
	public void act(taskActionType action) throws Exception
		{
		switch(action)
			{
			case pause:setPause(true);break;
			case start:setPause(false);break;
			case stop:setStop(true);break;
			default:break;
			}
		}

	public void Stop()
		{
		this.stop = true;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause) throws Exception
		{
		this.pause = pause;
		
		if(this.isAlive())
			{
			if(this.pause)
				{
				Variables.getLogger().debug("The user asked to pause the task : "+taskID);
				if(cliManager != null)cliManager.setPause(pause);
				if(pingManager != null)pingManager.setPause(pause);
				}
			else
				{
				Variables.getLogger().debug("The user asked to resume the task : "+taskID);
				if(cliManager != null)cliManager.setPause(pause);
				if(pingManager != null)pingManager.setPause(pause);
				}
			}
		else
			{
			throw new Exception("The task is finished and therefore cannot be paused");
			}
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop) throws Exception
		{
		if(this.isAlive())
			{
			Variables.getLogger().debug("The user asked to stop the task : "+taskID);
			this.stop = stop;
			if(cliManager != null)cliManager.setStop(stop);
			if(pingManager != null)pingManager.setStop(stop);
			}
		else
			{
			throw new Exception("The task is finished and therefore cannot be stopped again");
			}
		}

	public String getTaskId()
		{
		return taskID;
		}

	public void setTaskId(String id)
		{
		this.taskID = id;
		}

	public String getOwnerID()
		{
		return ownerID;
		}

	public ActionType getAction()
		{
		return action;
		}

	public ArrayList<Office> getOfficeList()
		{
		return officeList;
		}

	public TaskStatus getStatus()
		{
		return status;
		}

	public String getStatusDescription()
		{
		return statusDescription;
		}

	public void setStatusDescription(String statusDescription)
		{
		this.statusDescription = statusDescription;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

