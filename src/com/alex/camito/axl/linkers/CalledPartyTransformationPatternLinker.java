package com.alex.camito.axl.linkers;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.alex.camito.axl.misc.AXLItemLinker;
import com.alex.camito.axl.misc.ToUpdate;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.office.items.CalledPartyTransformationPattern;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;





/**********************************
 * Is the AXLItem design to link the item "Calling Party Transformation pattern"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class CalledPartyTransformationPatternLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String description,
	routePartitionName,
	calledPartyTransformationMask,
	digitDiscardInstructionName,
	calledPartyPrefixDigits,
	calledPartyNumberingPlan,
	calledPartyNumberType;
	
	public enum toUpdate implements ToUpdate
		{
		description,
		calledPartyTransformationMask,
		digitDiscardInstructionName,
		calledPartyPrefixDigits,
		calledPartyNumberingPlan,
		calledPartyNumberType
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public CalledPartyTransformationPatternLinker(String name, String routePartitionName) throws Exception
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
		//To be written
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.RemoveCalledPartyTransformationPatternReq deleteReq = new com.cisco.axl.api._10.RemoveCalledPartyTransformationPatternReq();
		
		deleteReq.setPattern(this.getName());//We add the parameters to the request
		deleteReq.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().removeCalledPartyTransformationPattern(deleteReq);//We send the request to the CUCM
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.AddCalledPartyTransformationPatternReq req = new com.cisco.axl.api._10.AddCalledPartyTransformationPatternReq();
		com.cisco.axl.api._10.XCalledPartyTransformationPattern params = new com.cisco.axl.api._10.XCalledPartyTransformationPattern();
		
		/**
		 * We set the item parameters
		 */
		params.setPattern(this.getName());//Pattern
		params.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class,SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		params.setDescription(this.description);
		params.setCalledPartyTransformationMask(new JAXBElement(new QName("calledPartyTransformationMask"), String.class, this.calledPartyTransformationMask));
		
		//We get the UUID of the digitDiscardInstructionName from the CUCM
		String digitDiscardInstructionUUID = SimpleRequest.getDigitDiscardUUID(this.digitDiscardInstructionName, cucm);
		if(digitDiscardInstructionUUID != null)
			{
			com.cisco.axl.api._10.XFkType xfkDigit = new com.cisco.axl.api._10.XFkType();
			xfkDigit.setUuid(digitDiscardInstructionUUID);
			params.setDigitDiscardInstructionName(new JAXBElement(new QName("digitDiscardInstructionName"), com.cisco.axl.api._10.XFkType.class, xfkDigit));
			}
		
		params.setCalledPartyPrefixDigits(new JAXBElement(new QName("calledPartyPrefixDigits"), String.class, this.calledPartyPrefixDigits));
		params.setCalledPartyNumberingPlan(this.calledPartyNumberingPlan);
		params.setCalledPartyNumberType(this.calledPartyNumberType);
		/************/
		
		req.setCalledPartyTransformationPattern(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().addCalledPartyTransformationPattern(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList, CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.UpdateCalledPartyTransformationPatternReq req = new com.cisco.axl.api._10.UpdateCalledPartyTransformationPatternReq();
		
		/***********
		 * We set the item parameters
		 */
		req.setPattern(this.getName());
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		
		if(tuList.contains(toUpdate.description))req.setDescription(this.description);
		if(tuList.contains(toUpdate.calledPartyTransformationMask))req.setCalledPartyTransformationMask(new JAXBElement(new QName("calledPartyTransformationMask"), String.class, this.calledPartyTransformationMask));
		
		if(tuList.contains(toUpdate.digitDiscardInstructionName))
			{
			//We get the UUID of the digitDiscardInstructionName from the CUCM
			String digitDiscardInstructionUUID = SimpleRequest.getDigitDiscardUUID(this.digitDiscardInstructionName, cucm);
			if(digitDiscardInstructionUUID != null)
				{
				com.cisco.axl.api._10.XFkType xfkDigit = new com.cisco.axl.api._10.XFkType();
				xfkDigit.setUuid(digitDiscardInstructionUUID);
				req.setDigitDiscardInstructionName(new JAXBElement(new QName("digitDiscardInstructionName"), com.cisco.axl.api._10.XFkType.class, xfkDigit));
				}
			}
		
		if(tuList.contains(toUpdate.calledPartyPrefixDigits))req.setCalledPartyPrefixDigits(new JAXBElement(new QName("calledPartyPrefixDigits"), String.class, this.calledPartyPrefixDigits));
		if(tuList.contains(toUpdate.calledPartyNumberingPlan))req.setCalledPartyNumberingPlan(this.calledPartyNumberingPlan);
		if(tuList.contains(toUpdate.calledPartyNumberType))req.setCalledPartyNumberType(this.calledPartyNumberType);
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().updateCalledPartyTransformationPattern(req);//We send the request to the CUCM
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.GetCalledPartyTransformationPatternReq req = new com.cisco.axl.api._10.GetCalledPartyTransformationPatternReq();
		
		/**
		 * We set the item parameters
		 */
		req.setPattern(this.getName());
		req.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, this.routePartitionName, cucm)));
		/************/
		
		com.cisco.axl.api._10.GetCalledPartyTransformationPatternRes resp = cucm.getAXLConnectionV105().getCalledPartyTransformationPattern(req);//We send the request to the CUCM
		
		CalledPartyTransformationPattern myCPTP = new CalledPartyTransformationPattern(this.getName(), this.getRoutePartitionName());
		myCPTP.setUUID(resp.getReturn().getCalledPartyTransformationPattern().getUuid());
		
		return myCPTP;//Return a Translation Pattern
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

	public String getCalledPartyTransformationMask()
		{
		return calledPartyTransformationMask;
		}

	public void setCalledPartyTransformationMask(String calledPartyTransformationMask)
		{
		this.calledPartyTransformationMask = calledPartyTransformationMask;
		}

	public String getDigitDiscardInstructionName()
		{
		return digitDiscardInstructionName;
		}

	public void setDigitDiscardInstructionName(String digitDiscardInstructionName)
		{
		this.digitDiscardInstructionName = digitDiscardInstructionName;
		}

	public String getCalledPartyPrefixDigits()
		{
		return calledPartyPrefixDigits;
		}

	public void setCalledPartyPrefixDigits(String calledPartyPrefixDigits)
		{
		this.calledPartyPrefixDigits = calledPartyPrefixDigits;
		}

	public String getCalledPartyNumberingPlan()
		{
		return calledPartyNumberingPlan;
		}

	public void setCalledPartyNumberingPlan(String calledPartyNumberingPlan)
		{
		this.calledPartyNumberingPlan = calledPartyNumberingPlan;
		}

	public String getCalledPartyNumberType()
		{
		return calledPartyNumberType;
		}

	public void setCalledPartyNumberType(String calledPartyNumberType)
		{
		this.calledPartyNumberType = calledPartyNumberType;
		}

	
	
	/*2018*//*RATEL Alexandre 8)*/
	}

