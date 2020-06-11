package com.alex.camito.webserver;

import java.util.ArrayList;

import com.alex.camito.action.Task;
import com.alex.camito.device.BasicDevice;
import com.alex.camito.office.misc.BasicOffice;
import com.alex.camito.office.misc.LinkedOffice;
import com.alex.camito.office.misc.Office;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.webserver.ManageWebRequest.webRequestType;



/**
 * Used to build web request
 *
 * @author Alexandre RATEL
 */
public class WebRequestBuilder
	{
	
	public static WebRequest buildWebRequest(webRequestType type, Object obj)
		{
		switch(type)
			{
			case search:return buildSearchReply((String)obj);
			case getOfficeList:return buildGetOfficeListReply();
			case getDeviceList:return buildGetDeviceListReply();
			case getTaskList:return buildGetTaskListReply();
			case getOffice:return buildGetOfficeReply((String)obj);
			case getDevice:return buildGetDeviceReply((String)obj);
			case getTask:return buildGetTaskReply((String)obj);
			case newTask:return buildNewTaskReply((String)obj);
			case success:return buildSuccess();
			case error:return buildError((String)obj);
			default:return null;
			}
		}
	
	/**
	 * To build the requested request
	 * getOfficeList
	 */
	private static WebRequest buildSearchReply(String search)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.search;
		
		ArrayList<BasicOffice> ol = new ArrayList<BasicOffice>();
		
		try
			{
			/**
			 * First we look for offices
			 */
			Variables.getLogger().debug("We look for office matching : "+search);
			for(BasicOffice o : Variables.getOfficeList())
				{
				if((o.getName().toLowerCase().contains(search.toLowerCase())) ||
						(o.getCoda().toLowerCase().contains(search.toLowerCase())) ||
						(o.getLot().name().toLowerCase().equals(UsefulMethod.searchForLot(search.toLowerCase()).toLowerCase())) ||
						(o.getPole().toLowerCase().contains(search.toLowerCase())))
					{
					Variables.getLogger().debug("Office found : "+o.getInfo());
					ol.add(o);
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while building the search reply for '"+search+"' : "+e.getMessage());
			}
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<offices>\r\n");
		
		//offices
		if(ol.size() != 0)
			{
			for(BasicOffice o : ol)
				{
				content.append("				<office>\r\n");
				content.append(getOffice("				", o));
				content.append("				</office>\r\n");
				}
			}
		content.append("			</offices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getOfficeList
	 */
	private static WebRequest buildGetOfficeListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getOfficeList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<offices>\r\n");
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				content.append("				<office>\r\n");
				content.append(getOffice("				", o));
				content.append("				</office>\r\n");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the office list : "+e.getMessage());
			content.append("				<office></office>\r\n");
			}
		
		content.append("			</offices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getDeviceList
	 */
	private static WebRequest buildGetDeviceListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getDeviceList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<devices>\r\n");
		
		try
			{
			for(BasicDevice d : Variables.getDeviceList())
				{
				StringBuffer temp = new StringBuffer();
				temp.append("				<device>\r\n");
				temp.append(getDevice("				", d));
				temp.append("				</device>\r\n");
				content.append(temp);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the device list : "+e.getMessage());
			content.append("				<device></device>\r\n");
			}
		
		content.append("			</devices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getTaskList
	 */
	private static WebRequest buildGetTaskListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getTaskList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			if(Variables.getTaskList().size()==0)throw new Exception("The tasklist is empty");
			
			for(Task t : Variables.getTaskList())
				{
				StringBuffer temp = new StringBuffer();
				temp.append("				<task>\r\n");
				temp.append(getTask("				", t));
				temp.append("				</task>\r\n");
				content.append(temp);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the task status : "+e.getMessage());
			content.append("				<task></task>\r\n");
			}
		
		content.append("			</tasks>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getOffice
	 */
	private static WebRequest buildGetOfficeReply(String officeID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getOffice;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getId().equals(officeID))
					{
					content.append("			<office>\r\n");
					content.append(getOffice("			", o));
					content.append("			</office>\r\n");
					found = true;
					break;
					}
				}
			if(!found)throw new Exception("The following office was not found : "+officeID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the office list : "+e.getMessage());
			content.append("			<office></office>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getDevice
	 */
	private static WebRequest buildGetDeviceReply(String deviceID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getDevice;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			for(BasicDevice d : Variables.getDeviceList())
				{
				if(d.getId().equals(deviceID))
					{
					StringBuffer temp = new StringBuffer();
					temp.append("			<device>\r\n");
					temp.append(getDevice("			", d));
					temp.append("			</device>\r\n");
					found = true;
					content.append(temp);
					break;
					}
				}
			
			if(!found)throw new Exception("The following office was not found : "+deviceID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the device list : "+e.getMessage());
			content.append("			<device></device>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getTask
	 */
	private static WebRequest buildGetTaskReply(String taskID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getTask;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			if(taskID.equals(""))
				{
				//We return the current task
				if(Variables.getTaskList().size() != 0)
					{
					content.append("			<task>\r\n");
					content.append(getTask("			", Variables.getTaskList().get(0)));
					content.append("			</task>\r\n");
					}
				else
					{
					throw new Exception("No task in progress to return");
					}
				}
			else
				{
				for(Task t : Variables.getTaskList())
					{
					if(t.getTaskId().equals(taskID))
						{
						StringBuffer temp = new StringBuffer();
						temp.append("			<task>\r\n");
						temp.append(getTask("			", t));
						temp.append("			</task>\r\n");
						found = true;
						content.append(temp);
						break;
						}
					}
				if(!found)throw new Exception("The following task was not found : "+taskID);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the task status : "+e.getMessage());
			content.append("			<task></task>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * newTask
	 */
	private static WebRequest buildNewTaskReply(String taskID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.newTask;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<taskid>"+taskID+"</taskid>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To create one office
	 */
	private static String getOffice(String tabs, BasicOffice o)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+o.getId()+"</id>\r\n");
		content.append(tabs+"	<coda>"+o.getCoda()+"</coda>\r\n");
		content.append(tabs+"	<name>"+o.getName()+"</name>\r\n");
		content.append(tabs+"	<type>"+o.getOfficeType()+"</type>\r\n");
		content.append(tabs+"	<pole>"+o.getPole()+"</pole>\r\n");
		content.append(tabs+"	<devicepool>"+o.getDevicepool()+"</devicepool>\r\n");
		content.append(tabs+"	<lot>"+UsefulMethod.convertLot(o.getLot())+"</lot>\r\n");
		content.append(tabs+"	<officetype>"+o.getOfficeType()+"</officetype>\r\n");
		content.append(tabs+"	<cmg>"+o.getCmg().getName()+"</cmg>\r\n");
		content.append(tabs+"	<status>"+o.getStatus()+"</status>\r\n");
		content.append(tabs+"	<linkedoffices>\r\n");
		for(LinkedOffice lo : o.getLinkedOffice())
			{
			content.append(tabs+"		<linkedoffice>\r\n");
			content.append(tabs+"			<coda>"+lo.getCoda()+"</coda>\r\n");
			content.append(tabs+"			<name>"+lo.getName()+"</name>\r\n");
			content.append(tabs+"			<type>"+lo.getOfficeType()+"</type>\r\n");
			content.append(tabs+"			<pole>"+lo.getPole()+"</pole>\r\n");
			content.append(tabs+"		</linkedoffice>\r\n");
			}
		content.append(tabs+"	</linkedoffices>\r\n");
		return content.toString();
		}
	
	
	/**
	 * To create one device
	 */
	private static String getDevice(String tabs, BasicDevice d)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+d.getId()+"</id>\r\n");
		content.append(tabs+"	<name>"+d.getName()+"</name>\r\n");
		content.append(tabs+"	<type>"+d.getDeviceType().getName()+"</type>\r\n");
		content.append(tabs+"	<ip>"+d.getIp()+"</ip>\r\n");
		content.append(tabs+"	<mask>"+d.getMask()+"</mask>\r\n");
		content.append(tabs+"	<gateway>"+d.getGateway()+"</gateway>\r\n");
		content.append(tabs+"	<officeid>"+d.getOfficeid()+"</officeid>\r\n");
		
		return content.toString();
		}
	
	/**
	 * To create one task
	 */
	private static String getTask(String tabs, Task t)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+t.getTaskId()+"</id>\r\n");
		content.append(tabs+"	<overallstatus>"+t.getStatus()+"</overallstatus>\r\n");
		content.append(tabs+"	<itemlist>\r\n");
		
		for(Office o : t.getOfficeList())
			{
			content.append(tabs+"	<item>\r\n");
			content.append(tabs+"		<id>"+o.getId()+"</id>\r\n");
			content.append(tabs+"		<type>"+o.getOfficeType()+"</type>\r\n");
			content.append(tabs+"		<info>"+o.getInfo()+"</info>\r\n");
			content.append(tabs+"		<status>"+o.getStatus()+"</status>\r\n");
			content.append(tabs+"		<desc>"+o.getDetailedStatus()+"</desc>\r\n");
			content.append(tabs+"	</item>\r\n");
			}
		
		content.append(tabs+"	</itemlist>\r\n");
		
		return content.toString();
		}
	
	/**
	 * To build the requested request
	 * success
	 */
	private static WebRequest buildSuccess()
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>success</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<success></success>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.success);
		}
	
	/**
	 * To build the requested request
	 * error
	 */
	private static WebRequest buildError(String message)
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>error</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<error>"+message+"</error>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.error);
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
