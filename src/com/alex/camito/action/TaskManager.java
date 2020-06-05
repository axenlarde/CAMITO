package com.alex.camito.action;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.Device;
import com.alex.camito.misc.ItemToMigrate;
import com.alex.camito.office.misc.BasicOffice;
import com.alex.camito.office.misc.Office;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;



public class TaskManager
	{
	
	/**
	 * To start new tasks
	 * @throws Exception 
	 */
	public static String addNewTask(ArrayList<String> itemList, ActionType action, String ownerID) throws Exception
		{
		try
			{
			//First we clear finished tasks and run GC
			Variables.getLogger().debug("Clearing task list");
			if(Variables.getTaskList().size()>0)
				{
				clearStaleTask();
				System.gc();
				}
			Variables.getLogger().debug("Task list cleared");
			
			int sum = 0;
			for(Task t : Variables.getTaskList())
				{
				if(t.isAlive())sum++;
				}
			
			if(sum < Integer.parseInt(UsefulMethod.getTargetOption("maxtaskthread")))
				{
				ArrayList<ItemToMigrate> todoList = new ArrayList<ItemToMigrate>();
				
				for(String s : itemList)
					{
					boolean found = false;
					//Devices
					for(BasicDevice d : Variables.getDeviceList())
						{
						if(d.getId().equals(s))
							{
							todoList.add(new Device(d, action));
							found = true;
							break;
							}
						}
					if(!found)
						{
						//Offices
						for(BasicOffice o : Variables.getOfficeList())
							{
							if(o.getId().equals(s))
								{
								todoList.add(new Office(o, action));
								found = true;
								break;
								}
							}
						}
					if(!found)Variables.getLogger().debug("Warning : The following item was not found in the database which is not normal : "+s);
					}
				
				//We generate a new unique ID
				if(todoList.size() != 0)
					{
					String id = DigestUtils.md5Hex(System.currentTimeMillis()+Math.random()+"8)");
					Task t = new Task(todoList, id, ownerID, action);
					Variables.getTaskList().add(t);
					t.start();
					return id;
					}
				else
					{
					throw new Exception("The item list was empty. No task could be started");
					}
				}
			else
				{
				throw new Exception("Max concurent task reached. You cannot start more task");
				}
			}
		catch (Exception e)
			{
			throw new Exception("ERROR while adding a new migration task : "+e.getMessage());
			}
		}
	
	/**
	 * To clear stale tasks
	 */
	private static void clearStaleTask()
		{
		for(Task t : Variables.getTaskList())
			{
			if(!t.isAlive())
				{
				Variables.getTaskList().remove(t);
				clearStaleTask();
				break;
				}
			}
		}
	
	/**
	 * Give the current processing task ID
	 * useful only in mono thread cases
	 */
	public static String getCurrentTask()
		{
		return Variables.getTaskList().get(0).getTaskId();
		}
	
	/**
	 * To get a task using its ID
	 */
	public static Task getTask(String ID)
		{
		for(Task t : Variables.getTaskList())
			{
			if(t.getTaskId().equals(ID))return t;
			}
		return null;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
