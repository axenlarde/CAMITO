package com.alex.camito.user.items;

import com.alex.camito.axl.linkers.UdpLoginLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.CollectionTools;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.ItemType;

/**********************************
 * Class used to define an item of type "UDP Login"
 * 
 * @author RATEL Alexandre
 **********************************/

public class UdpLogin extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String deviceName,
	deviceProfile;//UserID is the name
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public UdpLogin(String name,
			String deviceName, String deviceProfile) throws Exception
		{
		super(ItemType.udplogin, name, new UdpLoginLinker());
		this.deviceName = deviceName;
		this.deviceProfile = deviceProfile;
		this.action = ActionType.inject;
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
		UdpLogin myUDP = (UdpLogin) linker.get(cucm);
		return false;
		}
	
	public String getInfo()
		{
		return name+" "
		+deviceName+" "
		+deviceProfile;
		}
	
	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		/*
		name = CollectionTools.getValueFromCollectionFile(index, name, this, true);
		deviceName = CollectionTools.getValueFromCollectionFile(index, deviceName, this, true);
		deviceProfile = CollectionTools.getValueFromCollectionFile(index, deviceProfile, this, true);
		*/
		/**
		 * We set the item parameters
		 */
		((UdpLoginLinker)linker).setName(name);//It is the userID
		((UdpLoginLinker)linker).setDeviceName(deviceName);
		((UdpLoginLinker)linker).setDeviceProfile(deviceProfile);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(name))tuList.add(UdpLoginLinker.toUpdate.userID);
		if(UsefulMethod.isNotEmpty(deviceName))tuList.add(UdpLoginLinker.toUpdate.deviceName);
		if(UsefulMethod.isNotEmpty(deviceProfile))tuList.add(UdpLoginLinker.toUpdate.deviceProfile);
		}

	public String getDeviceName()
		{
		return deviceName;
		}

	public void setDeviceName(String deviceName)
		{
		this.deviceName = deviceName;
		}

	public String getDeviceProfile()
		{
		return deviceProfile;
		}

	public void setDeviceProfile(String deviceProfile)
		{
		this.deviceProfile = deviceProfile;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

