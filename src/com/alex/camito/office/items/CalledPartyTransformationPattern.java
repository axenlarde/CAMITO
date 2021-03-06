package com.alex.camito.office.items;

import com.alex.camito.axl.linkers.CalledPartyTransformationPatternLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.CollectionTools;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;
import com.alex.camito.utils.Variables.StatusType;

/**********************************
 * Class used to define an item of type "Called Party Transformation Pattern"
 * 
 * @author RATEL Alexandre
 **********************************/

public class CalledPartyTransformationPattern extends ItemToInject
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
	

	public CalledPartyTransformationPattern(String name, String description, String routePartitionName,
			String calledPartyTransformationMask, String digitDiscardInstructionName, String calledPartyPrefixDigits,
			String calledPartyNumberingPlan, String calledPartyNumberType) throws Exception
		{
		super(ItemType.calledpartytransformationpattern, name, new CalledPartyTransformationPatternLinker(name, routePartitionName));
		this.description = description;
		this.routePartitionName = routePartitionName;
		this.calledPartyTransformationMask = calledPartyTransformationMask;
		this.digitDiscardInstructionName = digitDiscardInstructionName;
		this.calledPartyPrefixDigits = calledPartyPrefixDigits;
		this.calledPartyNumberingPlan = calledPartyNumberingPlan;
		this.calledPartyNumberType = calledPartyNumberType;
		}

	public CalledPartyTransformationPattern(String name, String routePartitionName) throws Exception
		{
		super(ItemType.calledpartytransformationpattern, name, new CalledPartyTransformationPatternLinker(name, routePartitionName));
		this.routePartitionName = routePartitionName;
		}

	/***********
	 * Method used to prepare the item for the injection
	 * by gathering the needed UUID from the CUCM 
	 */
	public void doBuild(CUCM cucm) throws Exception
		{
		this.errorList.addAll(linker.init(cucm));
		}
	
	
	/**
	 * Method used to inject data in the CUCM using
	 * the Cisco API
	 * 
	 * It also return the item's UUID once injected
	 */
	public String doInject(CUCM cucm) throws Exception
		{
		return linker.inject(cucm);//Return UUID
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doDelete(CUCM cucm) throws Exception
		{
		linker.delete(cucm);
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doUpdate(CUCM cucm) throws Exception
		{
		linker.update(tuList, cucm);
		}
	
	/**
	 * Method used to check if the element exist in the CUCM
	 */
	public boolean isExisting(CUCM cucm) throws Exception
		{
		CalledPartyTransformationPattern myCPTP = (CalledPartyTransformationPattern) linker.get(cucm);
		this.UUID = myCPTP.getUUID();
		this.calledPartyTransformationMask = myCPTP.getCalledPartyTransformationMask();
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		this.status = StatusType.waiting;
		return true;
		}
	
	
	/**
	 * Method used to resolve pattern into real value
	 * @throws Exception 
	 */
	public void resolve() throws Exception
		{
		/*
		this.name = CollectionTools.getRawValue(this.name, this, true);
		this.description = CollectionTools.getRawValue(this.description, this, true);
		this.routePartitionName = CollectionTools.getRawValue(this.routePartitionName, this, true);
		this.calledPartyTransformationMask = CollectionTools.getRawValue(this.calledPartyTransformationMask, this, false);
		this.digitDiscardInstructionName = CollectionTools.getRawValue(this.digitDiscardInstructionName, this, false);
		this.calledPartyPrefixDigits = CollectionTools.getRawValue(this.calledPartyPrefixDigits, this, false);
		this.calledPartyNumberingPlan = CollectionTools.getRawValue(this.calledPartyNumberingPlan, this, false);
		this.calledPartyNumberType = CollectionTools.getRawValue(this.calledPartyNumberType, this, false);
		*/
		
		/**
		 * We set the item parameters
		 */
		CalledPartyTransformationPatternLinker myCalledPartyTransformationPattern = (CalledPartyTransformationPatternLinker) linker;
		myCalledPartyTransformationPattern.setName(this.getName());
		myCalledPartyTransformationPattern.setDescription(description);
		myCalledPartyTransformationPattern.setRoutePartitionName(routePartitionName);
		myCalledPartyTransformationPattern.setCalledPartyTransformationMask(calledPartyTransformationMask);
		myCalledPartyTransformationPattern.setDigitDiscardInstructionName(digitDiscardInstructionName);
		myCalledPartyTransformationPattern.setCalledPartyPrefixDigits(calledPartyPrefixDigits);
		myCalledPartyTransformationPattern.setCalledPartyNumberingPlan(calledPartyNumberingPlan);
		myCalledPartyTransformationPattern.setCalledPartyNumberType(calledPartyNumberType);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(description))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.description);
		if(UsefulMethod.isNotEmpty(calledPartyTransformationMask))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.calledPartyTransformationMask);
		if(UsefulMethod.isNotEmpty(digitDiscardInstructionName))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.digitDiscardInstructionName);
		if(UsefulMethod.isNotEmpty(calledPartyPrefixDigits))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.calledPartyPrefixDigits);
		if(UsefulMethod.isNotEmpty(calledPartyNumberingPlan))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.calledPartyNumberingPlan);
		if(UsefulMethod.isNotEmpty(calledPartyNumberType))tuList.add(CalledPartyTransformationPatternLinker.toUpdate.calledPartyNumberType);
		}

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

	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

