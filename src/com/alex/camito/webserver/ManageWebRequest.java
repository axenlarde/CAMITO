package com.alex.camito.webserver;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.alex.camito.action.Task;
import com.alex.camito.action.Task.taskActionType;
import com.alex.camito.action.TaskManager;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.xMLGear;


/**
 * Used to Manage web requests
 *
 * @author Alexandre RATEL
 */
public class ManageWebRequest
	{
	/**
	 * Variables
	 */
	public enum webRequestType
		{
		doAuthenticate,
		search,
		getOfficeList,
		getDeviceList,
		getTaskList,
		getDevice,
		getOffice,
		getTask,
		newTask,
		setTask,
		copyLogFile,
		newDevice,
		newOffice,
		success,
		error
		}
	
	/**
	 * Parse a web request
	 * @throws Exception 
	 */
	public synchronized static WebRequest parseWebRequest(String content) throws Exception
		{
		ArrayList<String> params = new ArrayList<String>();
		
		//We parse the request type
		params.add("request");
		ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
		webRequestType type = webRequestType.valueOf(UsefulMethod.getItemByName("type", parsed.get(0)));
		Variables.getLogger().debug("Web request type found : "+type.name());
		
		return new WebRequest(content, type);
		}
	
	/**
	 * doAuthenticate
	 */
	public synchronized static WebRequest doAuthenticate(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String userID = UsefulMethod.getItemByName("userid", t);
			String password = UsefulMethod.getItemByName("userpassword", t);
			
			if(UsefulMethod.doAuthenticate(Variables.getDstcucm(), userID, password))return WebRequestBuilder.buildWebRequest(webRequestType.success, null);
			else return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing doAuthenticate web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getOfficeList
	 */
	public synchronized static WebRequest search(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String search = UsefulMethod.getItemByName("search", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.search, search);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing search web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getOfficeList
	 */
	public synchronized static WebRequest getOfficeList()
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getOfficeList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getOfficeList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getDeviceList
	 */
	public synchronized static WebRequest getDeviceList()	
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getDeviceList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getDeviceList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getTaskList
	 */
	public synchronized static WebRequest getTaskList()	
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getTaskList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getTaskList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getOffice
	 */
	public synchronized static WebRequest getOffice(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String officeID = UsefulMethod.getItemByName("officeid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getOffice, officeID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getOffice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getDevice
	 */
	public synchronized static WebRequest getDevice(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String deviceID = UsefulMethod.getItemByName("deviceid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getDevice, deviceID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getDevice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getTask
	 */
	public synchronized static WebRequest getTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String taskID = UsefulMethod.getItemByName("taskid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getTask, taskID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getTask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * newTask
	 */
	public synchronized static WebRequest newTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] tab = parsed.get(0);
			
			ActionType action = ActionType.valueOf(UsefulMethod.getItemByName("action", tab));
			String ownerid = UsefulMethod.getItemByName("ownerid", tab);
			
			params.add("itemlist");
			parsed = xMLGear.getResultListTab(content, params);
			tab = parsed.get(0);
			ArrayList<String> idList = new ArrayList<String>();
			
			for(String[] t : tab)
				{
				idList.add(t[1]);
				}
			
			String taskID = TaskManager.addNewTask(idList, action, ownerid);
			
			return WebRequestBuilder.buildWebRequest(webRequestType.newTask, taskID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing newTask web request : "+e.getMessage(),e);
			return WebRequestBuilder.buildWebRequest(webRequestType.error, e.getMessage());
			}
		}
	
	/**
	 * setTask
	 */
	public synchronized static WebRequest setTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("task");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] tab = parsed.get(0);
			
			String taskID = UsefulMethod.getItemByName("taskid", tab);
			taskActionType action = taskActionType.valueOf(UsefulMethod.getItemByName("action", tab));
			
			boolean found = false;
			for(Task t : Variables.getTaskList())
				{
				if(t.getTaskId().equals(taskID))
					{
					t.act(action);
					found = true;
					break;
					}
				}
			
			if(found)
				{
				return WebRequestBuilder.buildWebRequest(webRequestType.success, taskID);
				}
			else
				{
				Variables.getLogger().debug("The following task ID was not found : "+taskID);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing setask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * copyLogFile
	 */
	public synchronized static WebRequest copyLogFile()	
		{
		try
			{
			/**
			 * First we copy all the log files
			 */
			File srcDirectory = new File(Variables.getMainDirectory());
			
			Variables.getLogger().debug("##Copying logs##");
			for(File f : srcDirectory.listFiles())
				{
				if(f.getName().toLowerCase().contains(Variables.getLogFileName().toLowerCase()))
					{
					Variables.getLogger().debug("Copying log file : "+f.getName());
					File dstFile = new File(UsefulMethod.getTargetOption("targetdirectory")+"/"+f.getName());
					FileUtils.copyFile(f, dstFile);
					}
				}
			
			/**
			 * Then we copy the phone survey file
			 */
			Variables.getLogger().debug("##Copying phone survey##");
			for(File f : srcDirectory.listFiles())
				{
				if(f.getName().toLowerCase().contains(Variables.getPhoneSurveyFileName().toLowerCase()))
					{
					Variables.getLogger().debug("Copying phone survey file : "+f.getName());
					File dstFile = new File(UsefulMethod.getTargetOption("targetdirectory")+"/"+f.getName());
					FileUtils.copyFile(f, dstFile);
					}
				}
			
			/**
			 * Then we copy the Get cli output file
			 */
			Variables.getLogger().debug("##Copying cli get output##");
			for(File f : srcDirectory.listFiles())
				{
				if(f.getName().toLowerCase().contains(Variables.getCliGetOutputFileName().toLowerCase()))
					{
					Variables.getLogger().debug("Copying cli get output file : "+f.getName());
					File dstFile = new File(UsefulMethod.getTargetOption("targetdirectory")+"/"+f.getName());
					FileUtils.copyFile(f, dstFile);
					}
				}
			
			/**
			 * Finally we copy the overall result file
			 */
			Variables.getLogger().debug("##Copying the overall result##");
			for(File f : srcDirectory.listFiles())
				{
				if(f.getName().toLowerCase().contains(Variables.getOverallResultFileName().toLowerCase()))
					{
					Variables.getLogger().debug("Copying overall result file : "+f.getName());
					File dstFile = new File(UsefulMethod.getTargetOption("targetdirectory")+"/"+f.getName());
					FileUtils.copyFile(f, dstFile);
					}
				}
			
			/**
			 * Finally we copy the mismatch file
			 */
			Variables.getLogger().debug("##Copying mismatch##");
			for(File f : srcDirectory.listFiles())
				{
				if(f.getName().toLowerCase().contains(Variables.getMismatcheListFileName().toLowerCase()))
					{
					Variables.getLogger().debug("Copying mismatch file : "+f.getName());
					File dstFile = new File(UsefulMethod.getTargetOption("targetdirectory")+"/"+f.getName());
					FileUtils.copyFile(f, dstFile);
					}
				}
			
			return WebRequestBuilder.buildWebRequest(webRequestType.success, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getTask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * newDevice
	 */
	public synchronized static WebRequest newDevice(String content)	
		{
		try
			{
			//To be written
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing newDevice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * newOffice
	 */
	public synchronized static WebRequest newOffice(String content)	
		{
		try
			{
			//To be written
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing newOffice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
