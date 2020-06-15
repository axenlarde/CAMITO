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
import com.alex.camito.user.items.HuntPilot;
import com.alex.camito.user.misc.UserError;
import com.alex.camito.utils.Variables.ItemType;




/**********************************
 * Is the AXLItem design to link the item "Hunt Pilot"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class HuntPilotLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String description,
	routePartitionName,
	alertingName,
	asciiAlertingName,
	huntListName,
	forwardHuntNoAnswerDestination;

	public enum toUpdate implements ToUpdate
		{
		description,
		alertingName,
		huntListName,
		forwardHuntNoAnswerDestination
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public HuntPilotLinker(String name, String routePartitionName) throws Exception
		{
		super(name);
		this.routePartitionName = routePartitionName;
		}
	
	/***************
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> doInitVersion105(CUCM cucm) throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		
		try
			{
			SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm);
			}
		catch (Exception e)
			{
			errorList.add(new UserError(this.name, this.routePartitionName, "Not found during init : "+e.getMessage(), ItemType.huntpilot, ItemType.partition, errorType.notFound));
			}
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.RemoveHuntPilotReq deleteReq = new com.cisco.axl.api._10.RemoveHuntPilotReq();
		
		deleteReq.setPattern(this.getName());//We add the parameters to the request
		deleteReq.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, routePartitionName, cucm)));
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().removeHuntPilot(deleteReq);//We send the request to the CUCM
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.AddHuntPilotReq req = new com.cisco.axl.api._10.AddHuntPilotReq();
		com.cisco.axl.api._10.XHuntPilot params = new com.cisco.axl.api._10.XHuntPilot();
		com.cisco.axl.api._10.XHuntPilot.ForwardHuntNoAnswer fwnoan = new com.cisco.axl.api._10.XHuntPilot.ForwardHuntNoAnswer();
		
		/**
		 * We set the item parameters
		 */
		params.setPattern(this.getName());//Name
		params.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, routePartitionName, cucm)));
		params.setDescription(description);
		
		params.setAlertingName(alertingName);
		params.setAsciiAlertingName(asciiAlertingName);
		params.setHuntListName(SimpleRequest.getUUIDV105(ItemType.huntlist, huntListName, cucm));
		fwnoan.setDestination(new JAXBElement(new QName("destination"), String.class, forwardHuntNoAnswerDestination));
		params.setForwardHuntNoAnswer(fwnoan);
		/************/
		
		req.setHuntPilot(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().addHuntPilot(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList, CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.UpdateHuntPilotReq req = new com.cisco.axl.api._10.UpdateHuntPilotReq();
		com.cisco.axl.api._10.UpdateHuntPilotReq.ForwardHuntNoAnswer fwnoan = new com.cisco.axl.api._10.UpdateHuntPilotReq.ForwardHuntNoAnswer();
		
		/***********
		 * We set the item parameters
		 */
		req.setPattern(this.getName());
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, routePartitionName, cucm)));
		
		if(tuList.contains(toUpdate.description))req.setDescription(description);
		if(tuList.contains(toUpdate.alertingName))
			{
			req.setAlertingName(alertingName);
			req.setAsciiAlertingName(asciiAlertingName);
			}
		if(tuList.contains(toUpdate.huntListName))req.setHuntListName(SimpleRequest.getUUIDV105(ItemType.huntlist, huntListName, cucm));
		if(tuList.contains(toUpdate.forwardHuntNoAnswerDestination))
			{
			fwnoan.setDestination(new JAXBElement(new QName("destination"), String.class, forwardHuntNoAnswerDestination));
			req.setForwardHuntNoAnswer(fwnoan);
			}
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().updateHuntPilot(req);//We send the request to the CUCM
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.GetHuntPilotReq req = new com.cisco.axl.api._10.GetHuntPilotReq();
		
		/**
		 * We set the item parameters
		 */
		req.setPattern(this.getName());
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, routePartitionName, cucm)));
		/************/
		
		com.cisco.axl.api._10.GetHuntPilotRes resp = cucm.getAXLConnectionV105().getHuntPilot(req);//We send the request to the CUCM
		
		HuntPilot myHP = new HuntPilot(this.getName(), this.routePartitionName);
		myHP.setUUID(resp.getReturn().getHuntPilot().getUuid());
		myHP.setForwardHuntNoAnswerDestination(resp.getReturn().getHuntPilot().getForwardHuntNoAnswer().getDestination());
		
		//Has to be written
		
		return myHP;
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

	public String getHuntListName()
		{
		return huntListName;
		}

	public void setHuntListName(String huntListName)
		{
		this.huntListName = huntListName;
		}

	public String getForwardHuntNoAnswerDestination()
		{
		return forwardHuntNoAnswerDestination;
		}

	public void setForwardHuntNoAnswerDestination(String forwardHuntNoAnswerDestination)
		{
		this.forwardHuntNoAnswerDestination = forwardHuntNoAnswerDestination;
		}

	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

