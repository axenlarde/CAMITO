package com.alex.camito.axl.linkers;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.alex.camito.axl.misc.AXLItemLinker;
import com.alex.camito.axl.misc.ToUpdate;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ErrorTemplate.errorType;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.user.items.Line;
import com.alex.camito.user.misc.UserError;
import com.alex.camito.utils.Variables.ItemType;


/**********************************
 * Is the AXLItem design to link the item "Line"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class LineLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String description,
	routePartitionName,
	usage,
	alertingName,
	asciiAlertingName,
	shareLineAppearanceCssName,
	callPickupGroupName,
	fwCallingSearchSpaceName,
	fwAllCallingSearchSpaceName,
	fwAllDestination,
	fwNoanDestination,
	fwBusyDestination,
	fwUnrDestination,
	voiceMailProfileName;
	
	private boolean fwAllVoicemailEnable,
	fwNoanVoicemailEnable,
	fwBusyVoicemailEnable,
	fwUnrVoicemailEnable;
	
	public enum toUpdate implements ToUpdate
		{
		description,
		alertingName,
		asciiAlertingName,
		shareLineAppearanceCssName,
		callPickupGroupName,
		fwCallingSearchSpaceName,
		fwAllCallingSearchSpaceName,
		fwAllDestination,
		fwNoanDestination,
		fwBusyDestination,
		fwUnrDestination,
		voiceMailProfileName,
		fwAllVoicemailEnable,
		fwNoanVoicemailEnable,
		fwBusyVoicemailEnable,
		fwUnrVoicemailEnable
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public LineLinker(String name, String routePartitionName) throws Exception
		{
		super(name);
		this.routePartitionName = routePartitionName;
		}
	
	/***************
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> doInitVersion85(CUCM cucm) throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		//To be written
		
		return errorList;
		}
	
	public ArrayList<ErrorTemplate> doInitVersion105(CUCM cucm) throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		try
			{
			SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.routePartitionName, "Not found during init : "+e.getMessage(), ItemType.line, ItemType.partition, errorType.notFound));
			}
		
		try
			{
			SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.shareLineAppearanceCssName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.shareLineAppearanceCssName, "Not found during init : "+e.getMessage(), ItemType.line, ItemType.callingsearchspace, errorType.notFound));
			}
		
		try
			{
			SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.fwCallingSearchSpaceName, "Not found during init : "+e.getMessage(), ItemType.line, ItemType.callingsearchspace, errorType.notFound));
			}
		
		try
			{
			SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwAllCallingSearchSpaceName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.fwAllCallingSearchSpaceName, "Not found during init : "+e.getMessage(), ItemType.line, ItemType.callingsearchspace, errorType.notFound));
			}
		
		try
			{
			SimpleRequest.getUUIDV105(ItemType.voicemail, this.voiceMailProfileName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.voiceMailProfileName, "Not found during init : "+e.getMessage(), ItemType.line, ItemType.callingsearchspace, errorType.notFound));
			}
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105(CUCM cucm) throws Exception
		{
		//First we get the UUID of the line
		com.cisco.axl.api._10.GetLineReq req = new com.cisco.axl.api._10.GetLineReq();
		req.setPattern(this.name);
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		com.cisco.axl.api._10.GetLineRes resp = cucm.getAXLConnectionV105().getLine(req);//We send the request to the CUCM
		
		//Then we delete the line
		com.cisco.axl.api._10.RemoveLineReq deleteReq = new com.cisco.axl.api._10.RemoveLineReq();
		deleteReq.setUuid(resp.getReturn().getLine().getUuid());//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse response = cucm.getAXLConnectionV105().removeLine(deleteReq);//We send the request to the CUCM
		}

	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.AddLineReq req = new com.cisco.axl.api._10.AddLineReq();
		com.cisco.axl.api._10.XLine params = new com.cisco.axl.api._10.XLine();
		
		/******************************
		 * We set the item parameters
		 * 
		 */
		params.setPattern(this.name);
		params.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		params.setDescription(this.description);
		params.setAlertingName(this.alertingName);
		params.setAsciiAlertingName(this.asciiAlertingName);
		params.setShareLineAppearanceCssName(new JAXBElement(new QName("shareLineAppearanceCssName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.shareLineAppearanceCssName, cucm)));
		params.setCallPickupGroupName(SimpleRequest.getUUIDV105(ItemType.callpickupgroup, this.callPickupGroupName, cucm));
		params.setVoiceMailProfileName(new JAXBElement(new QName("voiceMailProfileName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.voicemail, this.voiceMailProfileName, cucm)));
		
		/****
		 * Forward
		 */
		//All
		com.cisco.axl.api._10.XCallForwardAll myFwAll = new com.cisco.axl.api._10.XCallForwardAll();
		if(fwAllCallingSearchSpaceName.isEmpty())myFwAll.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		else myFwAll.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwAllCallingSearchSpaceName, cucm)));
		myFwAll.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwAllDestination));
		myFwAll.setForwardToVoiceMail((this.fwAllVoicemailEnable)?"true":"false");
		params.setCallForwardAll(myFwAll);
		
		//Noan
		com.cisco.axl.api._10.XCallForwardNoAnswer myFwNoan = new com.cisco.axl.api._10.XCallForwardNoAnswer();
		myFwNoan.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwNoan.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwNoanDestination));
		myFwNoan.setForwardToVoiceMail((this.fwNoanVoicemailEnable)?"true":"false");
		params.setCallForwardNoAnswer(myFwNoan);
		com.cisco.axl.api._10.XCallForwardNoAnswerInt myFwNoanInt = new com.cisco.axl.api._10.XCallForwardNoAnswerInt();
		myFwNoanInt.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwNoanInt.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwNoanDestination));
		myFwNoanInt.setForwardToVoiceMail((this.fwNoanVoicemailEnable)?"true":"false");
		params.setCallForwardNoAnswerInt(myFwNoanInt);
		
		//Busy
		com.cisco.axl.api._10.XCallForwardBusy myFwBusy = new com.cisco.axl.api._10.XCallForwardBusy();
		myFwBusy.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwBusy.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwBusyDestination));
		myFwBusy.setForwardToVoiceMail((this.fwBusyVoicemailEnable)?"true":"false");
		params.setCallForwardBusy(myFwBusy);
		com.cisco.axl.api._10.XCallForwardBusyInt myFwBusyInt = new com.cisco.axl.api._10.XCallForwardBusyInt();
		myFwBusyInt.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwBusyInt.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwBusyDestination));
		myFwBusyInt.setForwardToVoiceMail((this.fwBusyVoicemailEnable)?"true":"false");
		params.setCallForwardBusyInt(myFwBusyInt);
		
		//Unregistered
		com.cisco.axl.api._10.XCallForwardNotRegistered myFwUnr = new com.cisco.axl.api._10.XCallForwardNotRegistered();
		myFwUnr.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwUnr.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwUnrDestination));
		myFwUnr.setForwardToVoiceMail((this.fwUnrVoicemailEnable)?"true":"false");
		params.setCallForwardNotRegistered(myFwUnr);
		com.cisco.axl.api._10.XCallForwardNotRegisteredInt myFwUnrInt = new com.cisco.axl.api._10.XCallForwardNotRegisteredInt();
		myFwUnrInt.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
		myFwUnrInt.setDestination(new JAXBElement(new QName("destination"), String.class , this.fwUnrDestination));
		myFwUnrInt.setForwardToVoiceMail((this.fwUnrVoicemailEnable)?"true":"false");
		params.setCallForwardNotRegisteredInt(myFwUnrInt);
		/************/
		
		req.setLine(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().addLine(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList, CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.UpdateLineReq req = new com.cisco.axl.api._10.UpdateLineReq();
		com.cisco.axl.api._10.XCallForwardAll CallFA = new com.cisco.axl.api._10.XCallForwardAll();
		
		/***********
		 * We set the item parameters
		 */
		req.setPattern(this.name);
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		
		if(tuList.contains(toUpdate.description))req.setDescription(this.description);
		if(tuList.contains(toUpdate.alertingName))req.setAlertingName(this.alertingName);
		if(tuList.contains(toUpdate.asciiAlertingName))req.setAsciiAlertingName(this.asciiAlertingName);
		if(tuList.contains(toUpdate.shareLineAppearanceCssName))req.setShareLineAppearanceCssName(new JAXBElement(new QName("shareLineAppearanceCssName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.shareLineAppearanceCssName, cucm)));
		if(tuList.contains(toUpdate.callPickupGroupName))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(ItemType.callpickupgroup, this.callPickupGroupName, cucm));
		if(tuList.contains(toUpdate.fwAllCallingSearchSpaceName))
			{
			CallFA.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwAllCallingSearchSpaceName, cucm)));
			req.setCallForwardAll(CallFA);
			}
		else if(tuList.contains(toUpdate.fwCallingSearchSpaceName))
			{
			CallFA.setCallingSearchSpaceName(new JAXBElement(new QName("callingSearchSpaceName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.callingsearchspace, this.fwCallingSearchSpaceName, cucm)));
			req.setCallForwardAll(CallFA);
			}
		if(tuList.contains(toUpdate.fwAllDestination))
			{
			CallFA.setDestination(new JAXBElement(new QName("destination"), String.class, this.fwAllDestination));
			req.setCallForwardAll(CallFA);
			}
		if(tuList.contains(toUpdate.fwAllVoicemailEnable))
			{
			CallFA.setForwardToVoiceMail(Boolean.toString(this.fwAllVoicemailEnable));
			req.setCallForwardAll(CallFA);
			}
		/*To do
		if(tuList.contains(toUpdate.fwNoanDestination))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwBusyDestination))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwUnrDestination))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.voiceMailProfileName))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwAllVoicemailEnable))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwNoanVoicemailEnable))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwBusyVoicemailEnable))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		if(tuList.contains(toUpdate.fwUnrVoicemailEnable))req.setCallPickupGroupName(SimpleRequest.getUUIDV105(itemType.callpickupgroup, this.callPickupGroupName));
		*/
		
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().updateLine(req);//We send the request to the CUCM
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.GetLineReq req = new com.cisco.axl.api._10.GetLineReq();
		
		/**
		 * We set the item parameters
		 */
		req.setPattern(this.getName());
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		/************/
		
		com.cisco.axl.api._10.GetLineRes resp = cucm.getAXLConnectionV105().getLine(req);//We send the request to the CUCM
		
		Line myLine = new Line(this.getName(),this.getRoutePartitionName());
		myLine.setUUID(resp.getReturn().getLine().getUuid());
		myLine.setFwAllDestination(resp.getReturn().getLine().getCallForwardAll().getDestination());
		myLine.setFwAllCallingSearchSpaceName(resp.getReturn().getLine().getCallForwardAll().getCallingSearchSpaceName().getValue());
		myLine.setFwAllVoicemailEnable(resp.getReturn().getLine().getCallForwardAll().getForwardToVoiceMail());
		//etc..
		//Has to be written
		
		return myLine;//Return a location
		}
	/****************/

	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

	public String getRoutePartitionName()
		{
		return routePartitionName;
		}

	public void setRoutePartitionName(String routePartitionName)
		{
		this.routePartitionName = routePartitionName;
		}

	public String getUsage()
		{
		return usage;
		}

	public void setUsage(String usage)
		{
		this.usage = usage;
		}

	public String getAlertingName()
		{
		return alertingName;
		}

	public void setAlertingName(String alertingName)
		{
		this.alertingName = alertingName;
		}

	public String getAsciiAlertingName()
		{
		return asciiAlertingName;
		}

	public void setAsciiAlertingName(String asciiAlertingName)
		{
		this.asciiAlertingName = asciiAlertingName;
		}

	public String getShareLineAppearanceCssName()
		{
		return shareLineAppearanceCssName;
		}

	public void setShareLineAppearanceCssName(String shareLineAppearanceCssName)
		{
		this.shareLineAppearanceCssName = shareLineAppearanceCssName;
		}

	public String getCallPickupGroupName()
		{
		return callPickupGroupName;
		}

	public void setCallPickupGroupName(String callPickupGroupName)
		{
		this.callPickupGroupName = callPickupGroupName;
		}

	public String getFwCallingSearchSpaceName()
		{
		return fwCallingSearchSpaceName;
		}

	public void setFwCallingSearchSpaceName(String fwCallingSearchSpaceName)
		{
		this.fwCallingSearchSpaceName = fwCallingSearchSpaceName;
		}

	public String getFwAllDestination()
		{
		return fwAllDestination;
		}

	public void setFwAllDestination(String fwAllDestination)
		{
		this.fwAllDestination = fwAllDestination;
		}

	public String getFwNoanDestination()
		{
		return fwNoanDestination;
		}

	public void setFwNoanDestination(String fwNoanDestination)
		{
		this.fwNoanDestination = fwNoanDestination;
		}

	public String getFwBusyDestination()
		{
		return fwBusyDestination;
		}

	public void setFwBusyDestination(String fwBusyDestination)
		{
		this.fwBusyDestination = fwBusyDestination;
		}

	public String getFwUnrDestination()
		{
		return fwUnrDestination;
		}

	public void setFwUnrDestination(String fwUnrDestination)
		{
		this.fwUnrDestination = fwUnrDestination;
		}

	public String getVoiceMailProfileName()
		{
		return voiceMailProfileName;
		}

	public void setVoiceMailProfileName(String voiceMailProfileName)
		{
		this.voiceMailProfileName = voiceMailProfileName;
		}

	public boolean isFwAllVoicemailEnable()
		{
		return fwAllVoicemailEnable;
		}

	public void setFwAllVoicemailEnable(boolean fwAllVoicemailEnable)
		{
		this.fwAllVoicemailEnable = fwAllVoicemailEnable;
		}

	public boolean isFwNoanVoicemailEnable()
		{
		return fwNoanVoicemailEnable;
		}

	public void setFwNoanVoicemailEnable(boolean fwNoanVoicemailEnable)
		{
		this.fwNoanVoicemailEnable = fwNoanVoicemailEnable;
		}

	public boolean isFwBusyVoicemailEnable()
		{
		return fwBusyVoicemailEnable;
		}

	public void setFwBusyVoicemailEnable(boolean fwBusyVoicemailEnable)
		{
		this.fwBusyVoicemailEnable = fwBusyVoicemailEnable;
		}

	public boolean isFwUnrVoicemailEnable()
		{
		return fwUnrVoicemailEnable;
		}

	public void setFwUnrVoicemailEnable(boolean fwUnrVoicemailEnable)
		{
		this.fwUnrVoicemailEnable = fwUnrVoicemailEnable;
		}

	public String getFwAllCallingSearchSpaceName()
		{
		return fwAllCallingSearchSpaceName;
		}

	public void setFwAllCallingSearchSpaceName(String fwAllCallingSearchSpaceName)
		{
		this.fwAllCallingSearchSpaceName = fwAllCallingSearchSpaceName;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}

