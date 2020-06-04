package com.alex.camito.cli;

import java.util.ArrayList;

import com.alex.camito.device.Device;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToMigrate.ItmStatus;
import com.alex.camito.utils.Variables;



/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public class CliInjector extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private CliProfile cliProfile;
	private ArrayList<String> responses;
	private ArrayList<OneLine> todo;
	private ArrayList<OneLine> howToAuthenticate;
	private ArrayList<ErrorTemplate> errorList;
	
	public CliInjector(Device device, CliProfile cliProfile)
		{
		super();
		this.device = device;
		this.cliProfile = cliProfile;
		responses = new ArrayList<String>();
		todo = new ArrayList<OneLine>();
		howToAuthenticate = new ArrayList<OneLine>();
		errorList = new ArrayList<ErrorTemplate>();
		}
	
	public void build() throws Exception
		{
		/**
		 * First we get our own version of the cliprofile commands and resolve them to match device values
		 */
		for(OneLine ol : cliProfile.getHowToAuthenticate())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			howToAuthenticate.add(l);
			}
		for(OneLine ol : cliProfile.getCliList())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			todo.add(l);
			}
		}
	
	public void run()
		{
		try
			{
			/**
			 * Here we send the cli command
			 */
			CliLinker clil = new CliLinker(this);
			
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection starts");
			for(OneLine l : todo)
				{
				try
					{
					clil.execute(l);
					this.sleep(cliProfile.getDefaultInterCommandTimer());
					}
				catch (ConnectionException ce)
					{
					throw new ConnectionException(ce);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(device.getInfo()+" : CLI : ERROR whith the command : "+l.getInfo());
					device.addError(new ErrorTemplate("CLI : ERROR whith the command : "+l.getInfo()));
					device.setStatus(ItmStatus.error);
					}
				}
			clil.disconnect();
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection ends");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : CLI : Critical ERROR : "+e.getMessage());
			device.addError(new ErrorTemplate("CLI : Critical ERROR : "+e.getMessage()));
			device.setStatus(ItmStatus.error);
			}
		}

	
	public Device getDevice()
		{
		return device;
		}

	public CliProfile getCliProfile()
		{
		return cliProfile;
		}

	public ArrayList<String> getResponses()
		{
		return responses;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}

	public ArrayList<OneLine> getTodo()
		{
		return todo;
		}

	public ArrayList<OneLine> getHowToAuthenticate()
		{
		return howToAuthenticate;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
