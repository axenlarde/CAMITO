package com.alex.camito.user.items;

import com.alex.camito.axl.linkers.LineLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;
import com.alex.camito.utils.Variables.StatusType;

/**********************************
 * Class used to define an item of type "Line"
 * 
 * @author RATEL Alexandre
 **********************************/

public class Line extends ItemToInject
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
	voiceMailProfileName,
	fwAllVoicemailEnable,
	fwNoanVoicemailEnable,
	fwBusyVoicemailEnable,
	fwUnrVoicemailEnable;
	
	

	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public Line(String name, String description,
			String routePartitionName, String alertingName,
			String asciiAlertingName, String shareLineAppearanceCssName,
			String fwCallingSearchSpaceName, String fwAllCallingSearchSpaceName,
			String fwAllDestination, String fwNoanDestination,
			String fwBusyDestination, String fwUnrDestination,
			String voiceMailProfileName, String fwAllVoicemailEnable,
			String fwNoanVoicemailEnable, String fwBusyVoicemailEnable,
			String fwUnrVoicemailEnable, int index) throws Exception
		{
		super(ItemType.line, name, new LineLinker(name, routePartitionName));
		this.description = description;
		this.routePartitionName = routePartitionName;
		this.usage = "Device";
		this.alertingName = alertingName;
		this.asciiAlertingName = asciiAlertingName;
		this.shareLineAppearanceCssName = shareLineAppearanceCssName;
		this.fwCallingSearchSpaceName = fwCallingSearchSpaceName;
		this.fwAllCallingSearchSpaceName = fwAllCallingSearchSpaceName;
		this.fwAllDestination = fwAllDestination;
		this.fwNoanDestination = fwNoanDestination;
		this.fwBusyDestination = fwBusyDestination;
		this.fwUnrDestination = fwUnrDestination;
		this.voiceMailProfileName = voiceMailProfileName;
		this.fwAllVoicemailEnable = fwAllVoicemailEnable;
		this.fwNoanVoicemailEnable = fwNoanVoicemailEnable;
		this.fwBusyVoicemailEnable = fwBusyVoicemailEnable;
		this.fwUnrVoicemailEnable = fwUnrVoicemailEnable;
		}

	public Line(String name, String routePartitionName) throws Exception
		{
		super(ItemType.line, name, new LineLinker(name, routePartitionName));
		this.routePartitionName = routePartitionName;
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
		Line myL = (Line) linker.get(cucm);
		this.UUID = myL.getUUID();
		this.fwAllDestination = myL.getFwAllDestination();
		this.fwAllVoicemailEnable = myL.getFwAllVoicemailEnable();
		this.fwAllCallingSearchSpaceName = myL.getFwAllCallingSearchSpaceName();
		
		//Etc...
		//Has to be written
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		this.status = StatusType.waiting;
		return true;
		}
	
	public String getInfo()
		{
		return name+" "
		+routePartitionName+" "
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
		shareLineAppearanceCssName = CollectionTools.getValueFromCollectionFile(index, shareLineAppearanceCssName, this, false);
		fwCallingSearchSpaceName = CollectionTools.getValueFromCollectionFile(index, fwCallingSearchSpaceName, this, false);
		fwAllDestination = CollectionTools.getValueFromCollectionFile(index, fwAllDestination, this, false);
		fwNoanDestination = CollectionTools.getValueFromCollectionFile(index, fwNoanDestination, this, false);
		fwBusyDestination = CollectionTools.getValueFromCollectionFile(index, fwBusyDestination, this, false);
		fwUnrDestination = CollectionTools.getValueFromCollectionFile(index, fwUnrDestination, this, false);
		voiceMailProfileName = CollectionTools.getValueFromCollectionFile(index, voiceMailProfileName, this, false);
		fwAllVoicemailEnable = CollectionTools.getValueFromCollectionFile(index, fwAllVoicemailEnable, this, false);
		fwNoanVoicemailEnable = CollectionTools.getValueFromCollectionFile(index, fwNoanVoicemailEnable, this, false);
		fwBusyVoicemailEnable = CollectionTools.getValueFromCollectionFile(index, fwBusyVoicemailEnable, this, false);
		fwUnrVoicemailEnable = CollectionTools.getValueFromCollectionFile(index, fwUnrVoicemailEnable, this, false);
		callPickupGroupName = CollectionTools.getValueFromCollectionFile(index, callPickupGroupName, this, false);
		*/
		/**
		 * We set the item parameters
		 */
		LineLinker myLine = (LineLinker) linker;
		myLine.setName(name);//It is the line number
		myLine.setDescription(description);
		myLine.setRoutePartitionName(routePartitionName);
		myLine.setAlertingName(alertingName);
		myLine.setAsciiAlertingName(asciiAlertingName);
		myLine.setShareLineAppearanceCssName(shareLineAppearanceCssName);
		myLine.setUsage(usage);
		myLine.setCallPickupGroupName(callPickupGroupName);
		myLine.setFwCallingSearchSpaceName(fwCallingSearchSpaceName);
		myLine.setFwAllCallingSearchSpaceName(fwAllCallingSearchSpaceName);
		myLine.setFwAllDestination(fwAllDestination);
		myLine.setFwNoanDestination(fwNoanDestination);
		myLine.setFwBusyDestination(fwBusyDestination);
		myLine.setFwUnrDestination(fwUnrDestination);
		myLine.setVoiceMailProfileName(voiceMailProfileName);
		if(UsefulMethod.isNotEmpty(fwAllVoicemailEnable))myLine.setFwAllVoicemailEnable(fwAllVoicemailEnable.equals("true")?true:false);
		if(UsefulMethod.isNotEmpty(fwNoanVoicemailEnable))myLine.setFwNoanVoicemailEnable(fwNoanVoicemailEnable.equals("true")?true:false);
		if(UsefulMethod.isNotEmpty(fwBusyVoicemailEnable))myLine.setFwBusyVoicemailEnable(fwBusyVoicemailEnable.equals("true")?true:false);
		if(UsefulMethod.isNotEmpty(fwUnrVoicemailEnable))myLine.setFwUnrVoicemailEnable(fwUnrVoicemailEnable.equals("true")?true:false);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(description))tuList.add(LineLinker.toUpdate.description);
		if(UsefulMethod.isNotEmpty(alertingName))tuList.add(LineLinker.toUpdate.alertingName);
		if(UsefulMethod.isNotEmpty(asciiAlertingName))tuList.add(LineLinker.toUpdate.asciiAlertingName);
		if(UsefulMethod.isNotEmpty(shareLineAppearanceCssName))tuList.add(LineLinker.toUpdate.shareLineAppearanceCssName);
		if(UsefulMethod.isNotEmpty(callPickupGroupName))tuList.add(LineLinker.toUpdate.callPickupGroupName);
		if(UsefulMethod.isNotEmpty(fwCallingSearchSpaceName))tuList.add(LineLinker.toUpdate.fwCallingSearchSpaceName);
		if(UsefulMethod.isNotEmpty(fwAllCallingSearchSpaceName))tuList.add(LineLinker.toUpdate.fwAllCallingSearchSpaceName);
		if(UsefulMethod.isNotEmpty(fwAllDestination))tuList.add(LineLinker.toUpdate.fwAllDestination);
		if(UsefulMethod.isNotEmpty(fwNoanDestination))tuList.add(LineLinker.toUpdate.fwNoanDestination);
		if(UsefulMethod.isNotEmpty(fwBusyDestination))tuList.add(LineLinker.toUpdate.fwBusyDestination);
		if(UsefulMethod.isNotEmpty(fwUnrDestination))tuList.add(LineLinker.toUpdate.fwUnrDestination);
		if(UsefulMethod.isNotEmpty(voiceMailProfileName))tuList.add(LineLinker.toUpdate.voiceMailProfileName);
		if(UsefulMethod.isNotEmpty(fwAllVoicemailEnable))tuList.add(LineLinker.toUpdate.fwAllVoicemailEnable);
		if(UsefulMethod.isNotEmpty(fwNoanVoicemailEnable))tuList.add(LineLinker.toUpdate.fwNoanVoicemailEnable);
		if(UsefulMethod.isNotEmpty(fwBusyVoicemailEnable))tuList.add(LineLinker.toUpdate.fwBusyVoicemailEnable);
		if(UsefulMethod.isNotEmpty(fwUnrVoicemailEnable))tuList.add(LineLinker.toUpdate.fwUnrVoicemailEnable);
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

	public String getFwAllVoicemailEnable()
		{
		return fwAllVoicemailEnable;
		}

	public void setFwAllVoicemailEnable(String fwAllVoicemailEnable)
		{
		this.fwAllVoicemailEnable = fwAllVoicemailEnable;
		}

	public String getFwNoanVoicemailEnable()
		{
		return fwNoanVoicemailEnable;
		}

	public void setFwNoanVoicemailEnable(String fwNoanVoicemailEnable)
		{
		this.fwNoanVoicemailEnable = fwNoanVoicemailEnable;
		}

	public String getFwBusyVoicemailEnable()
		{
		return fwBusyVoicemailEnable;
		}

	public void setFwBusyVoicemailEnable(String fwBusyVoicemailEnable)
		{
		this.fwBusyVoicemailEnable = fwBusyVoicemailEnable;
		}

	public String getFwUnrVoicemailEnable()
		{
		return fwUnrVoicemailEnable;
		}

	public void setFwUnrVoicemailEnable(String fwUnrVoicemailEnable)
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

