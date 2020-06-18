package com.alex.camito.cli;

import java.util.ArrayList;

import com.alex.camito.device.Device;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.StatusType;



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
	private ArrayList<String> responses;
	private ArrayList<OneLine> todo;
	private ArrayList<ErrorTemplate> errorList;
	
	public CliInjector(Device device)
		{
		super();
		this.device = device;
		responses = new ArrayList<String>();
		todo = new ArrayList<OneLine>();
		errorList = new ArrayList<ErrorTemplate>();
		}
	
	public void resolve() throws Exception
		{
		/**
		 * We get our own version of the cliprofile commands and resolve them to match device values
		 */
		for(OneLine ol : device.getCliProfile().getCliList())
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
			clil.connect();//First we initialize the connection
			Variables.getLogger().debug(device.getInfo()+" : Connected successfully, ready for cli injection");
			
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection starts");
			for(OneLine l : todo)
				{
				try
					{
					clil.execute(l);
					this.sleep(device.getCliProfile().getDefaultInterCommandTimer());
					}
				catch (ConnectionException ce)
					{
					throw new ConnectionException(ce);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(device.getInfo()+" : CLI : ERROR whith the command : "+l.getInfo());
					device.addError(new ErrorTemplate("CLI : ERROR whith the command : "+l.getInfo()));
					device.setStatus(StatusType.error);
					}
				}
			Variables.getLogger().debug(device.getInfo()+" : CLI : command injection ends");
			
			clil.disconnect();//Last we disconnect
			Variables.getLogger().debug(device.getInfo()+" : Disconnected successfully");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : CLI : Critical ERROR : "+e.getMessage());
			device.addError(new ErrorTemplate("CLI : Critical ERROR : "+e.getMessage()));
			device.setStatus(StatusType.error);
			}
		}

	
	public Device getDevice()
		{
		return device;
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
	
	/*2020*//*RATEL Alexandre 8)*/
	}
