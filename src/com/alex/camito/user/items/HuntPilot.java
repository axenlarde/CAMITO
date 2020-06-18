package com.alex.camito.user.items;

import com.alex.camito.axl.linkers.HuntPilotLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;

/**********************************
 * Class used to define an item of type "Hunt Pilot"
 * 
 * @author RATEL Alexandre
 **********************************/

public class HuntPilot extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String description,//Name is the HP Number
	routePartitionName,
	alertingName,
	asciiAlertingName,
	huntListName,
	forwardHuntNoAnswerDestination;
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public HuntPilot(String name,
			String description, String routePartitionName, String alertingName,
			String asciiAlertingName, String huntListName, String forwardHuntNoAnswerDestination) throws Exception
		{
		super(ItemType.huntpilot, name, new HuntPilotLinker(huntListName, routePartitionName));
		this.description = description;
		this.routePartitionName = routePartitionName;
		this.alertingName = alertingName;
		this.asciiAlertingName = asciiAlertingName;
		this.huntListName = huntListName;
		this.forwardHuntNoAnswerDestination = forwardHuntNoAnswerDestination;
		}

	public HuntPilot(String name, String routePartitionName) throws Exception
		{
		super(ItemType.huntpilot, name, new HuntPilotLinker(name, routePartitionName));
		}

	/***********
	 * Method used to prepare the item for the injection
	 * by gathering the needed UUID from the CUCM 
	 */
	public void doBuild(CUCM cucm) throws Exception
		{
		errorList.addAll(linker.init(cucm));
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
		HuntPilot myHP = (HuntPilot) linker.get(cucm);
		this.UUID = myHP.getUUID();
		this.forwardHuntNoAnswerDestination = myHP.getForwardHuntNoAnswerDestination();
		//Has to be enhanced
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	public String getInfo()
		{
		return name+" "
		+UUID;
		}
	
	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		/*
		name = CollectionTools.getValueFromCollectionFile(index, name, this, true);
		description = CollectionTools.getValueFromCollectionFile(index, description, this, false);
		routePartitionName = CollectionTools.getValueFromCollectionFile(index, routePartitionName, this, true);
		alertingName = CollectionTools.getValueFromCollectionFile(index, alertingName, this, false);
		asciiAlertingName = CollectionTools.getValueFromCollectionFile(index, asciiAlertingName, this, false);
		huntListName = CollectionTools.getValueFromCollectionFile(index, huntListName, this, true);
		*/
		
		HuntPilotLinker myHuntPilot = (HuntPilotLinker) linker;
		myHuntPilot.setAlertingName(alertingName);
		myHuntPilot.setAsciiAlertingName(asciiAlertingName);
		myHuntPilot.setDescription(description);
		myHuntPilot.setHuntListName(huntListName);
		myHuntPilot.setName(name);
		myHuntPilot.setRoutePartitionName(routePartitionName);
		myHuntPilot.setForwardHuntNoAnswerDestination(forwardHuntNoAnswerDestination);
		}
	
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(description))tuList.add(HuntPilotLinker.toUpdate.description);
		if(UsefulMethod.isNotEmpty(alertingName))tuList.add(HuntPilotLinker.toUpdate.alertingName);
		if(UsefulMethod.isNotEmpty(asciiAlertingName))tuList.add(HuntPilotLinker.toUpdate.alertingName);
		if(UsefulMethod.isNotEmpty(huntListName))tuList.add(HuntPilotLinker.toUpdate.huntListName);
		if(UsefulMethod.isNotEmpty(forwardHuntNoAnswerDestination))tuList.add(HuntPilotLinker.toUpdate.forwardHuntNoAnswerDestination);
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

