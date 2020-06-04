package com.alex.camito.office.items;

import java.util.ArrayList;

import com.alex.camito.axl.items.LocalRouteGroup;
import com.alex.camito.axl.linkers.DevicePoolLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;




/**********************************
 * Class used to define an item of type "Device Pool"
 * 
 * @author RATEL Alexandre
 **********************************/

public class DevicePool extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String callManagerGroupName,
	regionName,
	locationName,
	networkLocale,
	dateTimeSettingName,
	srstreference,
	mediaressourcegrouplist,
	physicallocation,
	devicemobilitygroup,
	devicemobilitycss,
	cgpnTransformationCssName,
	cdpnTransformationCssName,
	callingPartyNationalTransformationCssName,
	callingPartyInternationalTransformationCssName,
	callingPartyUnknownTransformationCssName,
	callingPartySubscriberTransformationCssName,
	cntdPnTransformationCssName,
	redirectingPartyTransformationCSS,
	callingPartyTransformationCSS;

	private ArrayList<LocalRouteGroup> localRouteGroupList;
	
	
	public DevicePool(String name, String callManagerGroupName, String regionName, String locationName,
			String networkLocale, String dateTimeSettingName, String srstreference, String mediaressourcegrouplist,
			String physicallocation, String devicemobilitygroup, String devicemobilitycss,
			String cgpnTransformationCssName, String cdpnTransformationCssName,
			String callingPartyNationalTransformationCssName, String callingPartyInternationalTransformationCssName,
			String callingPartyUnknownTransformationCssName, String callingPartySubscriberTransformationCssName,
			String cntdPnTransformationCssName, String redirectingPartyTransformationCSS,
			String callingPartyTransformationCSS, ArrayList<LocalRouteGroup> localRouteGroupList) throws Exception
		{
		super(ItemType.devicepool, name, new DevicePoolLinker(name));
		this.callManagerGroupName = callManagerGroupName;
		this.regionName = regionName;
		this.locationName = locationName;
		this.networkLocale = networkLocale;
		this.dateTimeSettingName = dateTimeSettingName;
		this.srstreference = srstreference;
		this.mediaressourcegrouplist = mediaressourcegrouplist;
		this.physicallocation = physicallocation;
		this.devicemobilitygroup = devicemobilitygroup;
		this.devicemobilitycss = devicemobilitycss;
		this.cgpnTransformationCssName = cgpnTransformationCssName;
		this.cdpnTransformationCssName = cdpnTransformationCssName;
		this.callingPartyNationalTransformationCssName = callingPartyNationalTransformationCssName;
		this.callingPartyInternationalTransformationCssName = callingPartyInternationalTransformationCssName;
		this.callingPartyUnknownTransformationCssName = callingPartyUnknownTransformationCssName;
		this.callingPartySubscriberTransformationCssName = callingPartySubscriberTransformationCssName;
		this.cntdPnTransformationCssName = cntdPnTransformationCssName;
		this.redirectingPartyTransformationCSS = redirectingPartyTransformationCSS;
		this.callingPartyTransformationCSS = callingPartyTransformationCSS;
		this.localRouteGroupList = localRouteGroupList;
		}

	public DevicePool(String name) throws Exception
		{
		super(ItemType.devicepool, name, new DevicePoolLinker(name));
		}

	/***********
	 * Method used to prepare the item for the injection
	 * by gathering the needed UUID from the CUCM 
	 */
	public void doBuild(CUCM cucm) throws Exception
		{
		this.errorList = linker.init();
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
		DevicePool myDP = (DevicePool) linker.get(cucm);
		this.UUID = myDP.getUUID();
		
		/*
		to be written
		*/
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	public String getInfo()
		{
		return name+" "
		+UUID+" "
		+regionName+" "
		+locationName+" "
		+networkLocale+" "
		+dateTimeSettingName+" "
		+srstreference+" "
		+mediaressourcegrouplist+" "
		+localRouteGroupList+" "
		+physicallocation+" "
		+devicemobilitygroup+" "
		+devicemobilitycss;
		}
	
	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		/*
		name = CollectionTools.getRawValue(name, this, true);
		callManagerGroupName = CollectionTools.getRawValue(callManagerGroupName, this, true);
		regionName = CollectionTools.getRawValue(regionName, this, true);
		locationName = CollectionTools.getRawValue(locationName, this, true);
		networkLocale = CollectionTools.getRawValue(networkLocale, this, false);
		dateTimeSettingName = CollectionTools.getRawValue(dateTimeSettingName, this, true);
		srstreference = CollectionTools.getRawValue(srstreference, this, false);
		mediaressourcegrouplist = CollectionTools.getRawValue(mediaressourcegrouplist, this, false);
		physicallocation = CollectionTools.getRawValue(physicallocation, this, false);
		devicemobilitygroup = CollectionTools.getRawValue(devicemobilitygroup, this, false);
		devicemobilitycss = CollectionTools.getRawValue(devicemobilitycss, this, false);
		cgpnTransformationCssName = CollectionTools.getRawValue(cgpnTransformationCssName, this, false);
		cdpnTransformationCssName = CollectionTools.getRawValue(cdpnTransformationCssName, this, false);
		callingPartyNationalTransformationCssName = CollectionTools.getRawValue(callingPartyNationalTransformationCssName, this, false);
		callingPartyInternationalTransformationCssName = CollectionTools.getRawValue(callingPartyInternationalTransformationCssName, this, false);
		callingPartyUnknownTransformationCssName = CollectionTools.getRawValue(callingPartyUnknownTransformationCssName, this, false);
		callingPartySubscriberTransformationCssName = CollectionTools.getRawValue(callingPartySubscriberTransformationCssName, this, false);
		cntdPnTransformationCssName = CollectionTools.getRawValue(cntdPnTransformationCssName, this, false);
		redirectingPartyTransformationCSS = CollectionTools.getRawValue(redirectingPartyTransformationCSS, this, false);
		callingPartyTransformationCSS = CollectionTools.getRawValue(callingPartyTransformationCSS, this, false);
		*/
		for(LocalRouteGroup lrg : localRouteGroupList)
			{
			lrg.resolve();
			}
		
		/**
		 * We set the item parameters
		 */
		DevicePoolLinker dpl = ((DevicePoolLinker)linker);
		dpl.setName(this.getName());
		dpl.setCallManagerGroupName(this.callManagerGroupName);
		dpl.setDateTimeSettingName(this.dateTimeSettingName);
		dpl.setDevicemobilitycss(this.devicemobilitycss);
		dpl.setDevicemobilitygroup(this.devicemobilitygroup);
		dpl.setLocalRouteGroupList(this.localRouteGroupList);
		dpl.setLocationName(this.locationName);
		dpl.setMediaressourcegrouplist(this.mediaressourcegrouplist);
		dpl.setNetworkLocale(this.networkLocale);
		dpl.setPhysicallocation(this.physicallocation);
		dpl.setRegionName(this.regionName);
		dpl.setSrstreference(this.srstreference);
		dpl.setCgpnTransformationCssName(cgpnTransformationCssName);
		dpl.setCdpnTransformationCssName(cdpnTransformationCssName);
		dpl.setCallingPartyNationalTransformationCssName(callingPartyNationalTransformationCssName);
		dpl.setCallingPartyInternationalTransformationCssName(callingPartyInternationalTransformationCssName);
		dpl.setCallingPartyUnknownTransformationCssName(callingPartyUnknownTransformationCssName);
		dpl.setCallingPartySubscriberTransformationCssName(callingPartySubscriberTransformationCssName);
		dpl.setCntdPnTransformationCssName(cntdPnTransformationCssName);
		dpl.setRedirectingPartyTransformationCSS(redirectingPartyTransformationCSS);
		dpl.setCallingPartyTransformationCSS(callingPartyTransformationCSS);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(callManagerGroupName))tuList.add(DevicePoolLinker.toUpdate.callManagerGroupName);
		if(UsefulMethod.isNotEmpty(regionName))tuList.add(DevicePoolLinker.toUpdate.regionName);
		if(UsefulMethod.isNotEmpty(locationName))tuList.add(DevicePoolLinker.toUpdate.locationName);
		if(UsefulMethod.isNotEmpty(networkLocale))tuList.add(DevicePoolLinker.toUpdate.networkLocale);
		if(UsefulMethod.isNotEmpty(dateTimeSettingName))tuList.add(DevicePoolLinker.toUpdate.dateTimeSettingName);
		if(UsefulMethod.isNotEmpty(srstreference))tuList.add(DevicePoolLinker.toUpdate.srstreference);
		if(UsefulMethod.isNotEmpty(mediaressourcegrouplist))tuList.add(DevicePoolLinker.toUpdate.mediaressourcegrouplist);
		if(UsefulMethod.isNotEmpty(physicallocation))tuList.add(DevicePoolLinker.toUpdate.physicallocation);
		if(UsefulMethod.isNotEmpty(devicemobilitygroup))tuList.add(DevicePoolLinker.toUpdate.devicemobilitygroup);
		if(UsefulMethod.isNotEmpty(devicemobilitycss))tuList.add(DevicePoolLinker.toUpdate.devicemobilitycss);
		if(UsefulMethod.isNotEmpty(cgpnTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.cgpnTransformationCssName);
		if(UsefulMethod.isNotEmpty(cdpnTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.cdpnTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingPartyNationalTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.callingPartyNationalTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingPartyInternationalTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.callingPartyInternationalTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingPartyUnknownTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.callingPartyUnknownTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingPartySubscriberTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.callingPartySubscriberTransformationCssName);
		if(UsefulMethod.isNotEmpty(cntdPnTransformationCssName))tuList.add(DevicePoolLinker.toUpdate.cntdPnTransformationCssName);
		if(UsefulMethod.isNotEmpty(redirectingPartyTransformationCSS))tuList.add(DevicePoolLinker.toUpdate.redirectingPartyTransformationCSS);
		if(UsefulMethod.isNotEmpty(callingPartyTransformationCSS))tuList.add(DevicePoolLinker.toUpdate.callingPartyTransformationCSS);
		
		
		if((localRouteGroupList == null) || (localRouteGroupList.size() == 0))
			{
			//Nothing to do
			}
		else
			{
			tuList.add(DevicePoolLinker.toUpdate.localroutegroup);
			}
		}
	
	/**
	 * Used to reset the device pool and therefore the associated devices
	 * @throws Exception 
	 */
	public void reset(CUCM cucm) throws Exception
		{
		if(this.UUID == null)
			{
			linker.get(cucm);
			}	
		((DevicePoolLinker)linker).reset(cucm);
		}

	public String getCallManagerGroupName()
		{
		return callManagerGroupName;
		}

	public void setCallManagerGroupName(String callManagerGroupName)
		{
		this.callManagerGroupName = callManagerGroupName;
		((DevicePoolLinker)linker).setCallManagerGroupName(callManagerGroupName);
		}

	public String getRegionName()
		{
		return regionName;
		}

	public void setRegionName(String regionName)
		{
		this.regionName = regionName;
		((DevicePoolLinker)linker).setRegionName(regionName);
		}

	public String getLocationName()
		{
		return locationName;
		}

	public void setLocationName(String locationName)
		{
		this.locationName = locationName;
		((DevicePoolLinker)linker).setLocationName(locationName);
		}

	public String getNetworkLocale()
		{
		return networkLocale;
		}

	public void setNetworkLocale(String networkLocale)
		{
		this.networkLocale = networkLocale;
		((DevicePoolLinker)linker).setNetworkLocale(networkLocale);
		}

	public String getDateTimeSettingName()
		{
		return dateTimeSettingName;
		}

	public void setDateTimeSettingName(String dateTimeSettingName)
		{
		this.dateTimeSettingName = dateTimeSettingName;
		((DevicePoolLinker)linker).setDateTimeSettingName(dateTimeSettingName);
		}

	public String getSrstreference()
		{
		return srstreference;
		}

	public void setSrstreference(String srstreference)
		{
		this.srstreference = srstreference;
		((DevicePoolLinker)linker).setSrstreference(srstreference);
		}

	public String getMediaressourcegrouplist()
		{
		return mediaressourcegrouplist;
		}

	public void setMediaressourcegrouplist(String mediaressourcegrouplist)
		{
		this.mediaressourcegrouplist = mediaressourcegrouplist;
		((DevicePoolLinker)linker).setMediaressourcegrouplist(mediaressourcegrouplist);
		}

	public String getPhysicallocation()
		{
		return physicallocation;
		}

	public void setPhysicallocation(String physicallocation)
		{
		this.physicallocation = physicallocation;
		((DevicePoolLinker)linker).setPhysicallocation(physicallocation);
		}

	public String getDevicemobilitygroup()
		{
		return devicemobilitygroup;
		}

	public void setDevicemobilitygroup(String devicemobilitygroup)
		{
		this.devicemobilitygroup = devicemobilitygroup;
		((DevicePoolLinker)linker).setDevicemobilitygroup(devicemobilitygroup);
		}

	public String getDevicemobilitycss()
		{
		return devicemobilitycss;
		}

	public void setDevicemobilitycss(String devicemobilitycss)
		{
		this.devicemobilitycss = devicemobilitycss;
		((DevicePoolLinker)linker).setDevicemobilitycss(devicemobilitycss);
		}

	public String getCgpnTransformationCssName()
		{
		return cgpnTransformationCssName;
		}

	public void setCgpnTransformationCssName(String cgpnTransformationCssName)
		{
		this.cgpnTransformationCssName = cgpnTransformationCssName;
		((DevicePoolLinker)linker).setCgpnTransformationCssName(cgpnTransformationCssName);
		}

	public String getCdpnTransformationCssName()
		{
		return cdpnTransformationCssName;
		}

	public void setCdpnTransformationCssName(String cdpnTransformationCssName)
		{
		this.cdpnTransformationCssName = cdpnTransformationCssName;
		((DevicePoolLinker)linker).setCdpnTransformationCssName(cdpnTransformationCssName);
		}

	public String getCallingPartyNationalTransformationCssName()
		{
		return callingPartyNationalTransformationCssName;
		}

	public void setCallingPartyNationalTransformationCssName(String callingPartyNationalTransformationCssName)
		{
		this.callingPartyNationalTransformationCssName = callingPartyNationalTransformationCssName;
		((DevicePoolLinker)linker).setCallingPartyNationalTransformationCssName(callingPartyNationalTransformationCssName);
		}

	public String getCallingPartyInternationalTransformationCssName()
		{
		return callingPartyInternationalTransformationCssName;
		}

	public void setCallingPartyInternationalTransformationCssName(String callingPartyInternationalTransformationCssName)
		{
		this.callingPartyInternationalTransformationCssName = callingPartyInternationalTransformationCssName;
		((DevicePoolLinker)linker).setCallingPartyInternationalTransformationCssName(callingPartyInternationalTransformationCssName);
		}

	public String getCallingPartyUnknownTransformationCssName()
		{
		return callingPartyUnknownTransformationCssName;
		}

	public void setCallingPartyUnknownTransformationCssName(String callingPartyUnknownTransformationCssName)
		{
		this.callingPartyUnknownTransformationCssName = callingPartyUnknownTransformationCssName;
		((DevicePoolLinker)linker).setCallingPartyUnknownTransformationCssName(callingPartyUnknownTransformationCssName);
		}

	public String getCallingPartySubscriberTransformationCssName()
		{
		return callingPartySubscriberTransformationCssName;
		}

	public void setCallingPartySubscriberTransformationCssName(String callingPartySubscriberTransformationCssName)
		{
		this.callingPartySubscriberTransformationCssName = callingPartySubscriberTransformationCssName;
		((DevicePoolLinker)linker).setCallingPartySubscriberTransformationCssName(callingPartySubscriberTransformationCssName);
		}

	public String getCntdPnTransformationCssName()
		{
		return cntdPnTransformationCssName;
		}

	public void setCntdPnTransformationCssName(String cntdPnTransformationCssName)
		{
		this.cntdPnTransformationCssName = cntdPnTransformationCssName;
		((DevicePoolLinker)linker).setCntdPnTransformationCssName(cntdPnTransformationCssName);
		}

	public String getRedirectingPartyTransformationCSS()
		{
		return redirectingPartyTransformationCSS;
		}

	public void setRedirectingPartyTransformationCSS(String redirectingPartyTransformationCSS)
		{
		this.redirectingPartyTransformationCSS = redirectingPartyTransformationCSS;
		((DevicePoolLinker)linker).setRedirectingPartyTransformationCSS(redirectingPartyTransformationCSS);
		}

	public String getCallingPartyTransformationCSS()
		{
		return callingPartyTransformationCSS;
		}

	public void setCallingPartyTransformationCSS(String callingPartyTransformationCSS)
		{
		this.callingPartyTransformationCSS = callingPartyTransformationCSS;
		((DevicePoolLinker)linker).setCallingPartyTransformationCSS(callingPartyTransformationCSS);
		}

	public ArrayList<LocalRouteGroup> getLocalRouteGroupList()
		{
		return localRouteGroupList;
		}

	public void setLocalRouteGroupList(ArrayList<LocalRouteGroup> localRouteGroupList)
		{
		this.localRouteGroupList = localRouteGroupList;
		((DevicePoolLinker)linker).setLocalRouteGroupList(localRouteGroupList);
		}
	
	
	/*2018*//*RATEL Alexandre 8)*/
	}

