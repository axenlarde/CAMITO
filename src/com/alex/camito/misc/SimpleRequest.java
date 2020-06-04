package com.alex.camito.misc;

import java.util.List;

import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.CucmVersion;
import com.alex.camito.utils.Variables.ItemType;



/**********************************
 * Class used to contain static method for
 * simple common AXL request to the CUCM
 * 
 * @author RATEL Alexandre
 **********************************/
public class SimpleRequest
	{
	
	
	
	/**************
	 * Method aims to return the Version of the CUCM of the asked item
	 *************/
	public static String getCUCMVersion(CUCM cucm) throws Exception
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			com.cisco.axl.api._10.GetCCMVersionReq req = new com.cisco.axl.api._10.GetCCMVersionReq();
			com.cisco.axl.api._10.GetCCMVersionRes resp = cucm.getAXLConnectionV105().getCCMVersion(req);//We send the request to the CUCM
			
			return resp.getReturn().getComponentVersion().getVersion();
			}
		else
			{
			throw new Exception("AXL unsupported version");
			}
		}
	
	/*****
	 * Used to get the string value of an UUID item
	 */
	public static String getUUID(ItemType type, String itemName, CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			return getUUIDV105(type, itemName, cucm).getUuid();
			}
		else
			{
			throw new Exception("AXL unsupported version");
			}
		}
	
	/**
	 * Method used to find a UUID from the CUCM
	 * 
	 * In addition it stores all the UUID found to avoid to
	 * Interrogate the CUCM twice
	 * @throws Exception 
	 */
	public static com.cisco.axl.api._10.XFkType getUUIDV105(ItemType type, String itemName, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("Get UUID from CUCM : "+type+" "+itemName);
		
		if((itemName == null) || (itemName.equals("")))
			{
			return getXFKV105("", itemName, type);
			}
		
		String id = type.name()+itemName;
		
		for(storedUUID s : Variables.getUuidList())
			{
			if(s.getComparison().equals(id))
				{
				Variables.getLogger().debug("UUID known");
				return getXFKWithoutStoringItV105(s.getUUID(), itemName, type);
				}
			}
		
		if(type.equals(ItemType.location))
			{
			com.cisco.axl.api._10.GetLocationReq req = new com.cisco.axl.api._10.GetLocationReq();
			com.cisco.axl.api._10.RLocation returnedTags = new com.cisco.axl.api._10.RLocation();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetLocationRes resp = cucm.getAXLConnectionV105().getLocation(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getLocation().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.region))
			{
			com.cisco.axl.api._10.GetRegionReq req = new com.cisco.axl.api._10.GetRegionReq();
			com.cisco.axl.api._10.RRegion returnedTags = new com.cisco.axl.api._10.RRegion();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetRegionRes resp = cucm.getAXLConnectionV105().getRegion(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getRegion().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.partition))
			{
			com.cisco.axl.api._10.GetRoutePartitionReq req = new com.cisco.axl.api._10.GetRoutePartitionReq();
			com.cisco.axl.api._10.RRoutePartition returnedTags = new com.cisco.axl.api._10.RRoutePartition();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetRoutePartitionRes resp = cucm.getAXLConnectionV105().getRoutePartition(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getRoutePartition().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.callingsearchspace))
			{
			com.cisco.axl.api._10.GetCssReq req = new com.cisco.axl.api._10.GetCssReq();
			com.cisco.axl.api._10.RCss returnedTags = new com.cisco.axl.api._10.RCss();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetCssRes resp = cucm.getAXLConnectionV105().getCss(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getCss().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.conferencebridge))
			{
			com.cisco.axl.api._10.GetConferenceBridgeReq req = new com.cisco.axl.api._10.GetConferenceBridgeReq();
			com.cisco.axl.api._10.RConferenceBridge returnedTags = new com.cisco.axl.api._10.RConferenceBridge();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetConferenceBridgeRes resp = cucm.getAXLConnectionV105().getConferenceBridge(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getConferenceBridge().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.devicepool))
			{
			com.cisco.axl.api._10.GetDevicePoolReq req = new com.cisco.axl.api._10.GetDevicePoolReq();
			com.cisco.axl.api._10.RDevicePool returnedTags = new com.cisco.axl.api._10.RDevicePool();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetDevicePoolRes resp = cucm.getAXLConnectionV105().getDevicePool(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getDevicePool().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.mediaresourcegroup))
			{
			com.cisco.axl.api._10.GetMediaResourceGroupReq req = new com.cisco.axl.api._10.GetMediaResourceGroupReq();
			com.cisco.axl.api._10.RMediaResourceGroup returnedTags = new com.cisco.axl.api._10.RMediaResourceGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetMediaResourceGroupRes resp = cucm.getAXLConnectionV105().getMediaResourceGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getMediaResourceGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.mediaresourcegrouplist))
			{
			com.cisco.axl.api._10.GetMediaResourceListReq req = new com.cisco.axl.api._10.GetMediaResourceListReq();
			com.cisco.axl.api._10.RMediaResourceList returnedTags = new com.cisco.axl.api._10.RMediaResourceList();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetMediaResourceListRes resp = cucm.getAXLConnectionV105().getMediaResourceList(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getMediaResourceList().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.physicallocation))
			{
			com.cisco.axl.api._10.GetPhysicalLocationReq req = new com.cisco.axl.api._10.GetPhysicalLocationReq();
			com.cisco.axl.api._10.RPhysicalLocation returnedTags = new com.cisco.axl.api._10.RPhysicalLocation();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetPhysicalLocationRes resp = cucm.getAXLConnectionV105().getPhysicalLocation(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getPhysicalLocation().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.routegroup))
			{
			com.cisco.axl.api._10.GetRouteGroupReq req = new com.cisco.axl.api._10.GetRouteGroupReq();
			com.cisco.axl.api._10.RRouteGroup returnedTags = new com.cisco.axl.api._10.RRouteGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetRouteGroupRes resp = cucm.getAXLConnectionV105().getRouteGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getRouteGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.srstreference))
			{
			com.cisco.axl.api._10.GetSrstReq req = new com.cisco.axl.api._10.GetSrstReq();
			com.cisco.axl.api._10.RSrst returnedTags = new com.cisco.axl.api._10.RSrst();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetSrstRes resp = cucm.getAXLConnectionV105().getSrst(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getSrst().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.trunksip))
			{
			com.cisco.axl.api._10.GetSipTrunkReq req = new com.cisco.axl.api._10.GetSipTrunkReq();
			com.cisco.axl.api._10.RSipTrunk returnedTags = new com.cisco.axl.api._10.RSipTrunk();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetSipTrunkRes resp = cucm.getAXLConnectionV105().getSipTrunk(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getSipTrunk().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.vg))
			{
			com.cisco.axl.api._10.GetGatewayReq req = new com.cisco.axl.api._10.GetGatewayReq();
			com.cisco.axl.api._10.RGateway returnedTags = new com.cisco.axl.api._10.RGateway();
			req.setDomainName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetGatewayRes resp = cucm.getAXLConnectionV105().getGateway(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getGateway().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.datetimesetting))
			{
			com.cisco.axl.api._10.GetDateTimeGroupReq req = new com.cisco.axl.api._10.GetDateTimeGroupReq();
			com.cisco.axl.api._10.RDateTimeGroup returnedTags = new com.cisco.axl.api._10.RDateTimeGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetDateTimeGroupRes resp = cucm.getAXLConnectionV105().getDateTimeGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getDateTimeGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.phone))
			{
			com.cisco.axl.api._10.GetPhoneReq req = new com.cisco.axl.api._10.GetPhoneReq();
			com.cisco.axl.api._10.RPhone returnedTags = new com.cisco.axl.api._10.RPhone();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetPhoneRes resp = cucm.getAXLConnectionV105().getPhone(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getPhone().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.udp))
			{
			com.cisco.axl.api._10.GetDeviceProfileReq req = new com.cisco.axl.api._10.GetDeviceProfileReq();
			com.cisco.axl.api._10.RDeviceProfile returnedTags = new com.cisco.axl.api._10.RDeviceProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetDeviceProfileRes resp = cucm.getAXLConnectionV105().getDeviceProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getDeviceProfile().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.user))
			{
			com.cisco.axl.api._10.GetUserReq req = new com.cisco.axl.api._10.GetUserReq();
			com.cisco.axl.api._10.RUser returnedTags = new com.cisco.axl.api._10.RUser();
			req.setUserid(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetUserRes resp = cucm.getAXLConnectionV105().getUser(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getUser().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.phonetemplatename))
			{
			com.cisco.axl.api._10.GetPhoneButtonTemplateReq req = new com.cisco.axl.api._10.GetPhoneButtonTemplateReq();
			com.cisco.axl.api._10.RPhoneButtonTemplate returnedTags = new com.cisco.axl.api._10.RPhoneButtonTemplate();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetPhoneButtonTemplateRes resp = cucm.getAXLConnectionV105().getPhoneButtonTemplate(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getPhoneButtonTemplate().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.callmanagergroup))
			{
			com.cisco.axl.api._10.GetCallManagerGroupReq req = new com.cisco.axl.api._10.GetCallManagerGroupReq();
			com.cisco.axl.api._10.RCallManagerGroup returnedTags = new com.cisco.axl.api._10.RCallManagerGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetCallManagerGroupRes resp = cucm.getAXLConnectionV105().getCallManagerGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getCallManagerGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.devicemobilitygroup))
			{
			com.cisco.axl.api._10.GetDeviceMobilityGroupReq req = new com.cisco.axl.api._10.GetDeviceMobilityGroupReq();
			com.cisco.axl.api._10.RDeviceMobilityGroup returnedTags = new com.cisco.axl.api._10.RDeviceMobilityGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetDeviceMobilityGroupRes resp = cucm.getAXLConnectionV105().getDeviceMobilityGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getDeviceMobilityGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.telecasterservice))
			{
			com.cisco.axl.api._10.GetIpPhoneServicesReq req = new com.cisco.axl.api._10.GetIpPhoneServicesReq();
			com.cisco.axl.api._10.RIpPhoneServices returnedTags = new com.cisco.axl.api._10.RIpPhoneServices();
			req.setServiceName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetIpPhoneServicesRes resp = cucm.getAXLConnectionV105().getIpPhoneServices(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getIpPhoneServices().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.commondeviceconfig))
			{
			com.cisco.axl.api._10.GetCommonDeviceConfigReq req = new com.cisco.axl.api._10.GetCommonDeviceConfigReq();
			com.cisco.axl.api._10.RCommonDeviceConfig returnedTags = new com.cisco.axl.api._10.RCommonDeviceConfig();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetCommonDeviceConfigRes resp = cucm.getAXLConnectionV105().getCommonDeviceConfig(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getCommonDeviceConfig().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.siptrunksecurityprofile))
			{
			com.cisco.axl.api._10.GetSipTrunkSecurityProfileReq req = new com.cisco.axl.api._10.GetSipTrunkSecurityProfileReq();
			com.cisco.axl.api._10.RSipTrunkSecurityProfile returnedTags = new com.cisco.axl.api._10.RSipTrunkSecurityProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetSipTrunkSecurityProfileRes resp = cucm.getAXLConnectionV105().getSipTrunkSecurityProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getSipTrunkSecurityProfile().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.sipprofile))
			{
			com.cisco.axl.api._10.GetSipProfileReq req = new com.cisco.axl.api._10.GetSipProfileReq();
			com.cisco.axl.api._10.RSipProfile returnedTags = new com.cisco.axl.api._10.RSipProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetSipProfileRes resp = cucm.getAXLConnectionV105().getSipProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getSipProfile().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.linegroup))
			{
			com.cisco.axl.api._10.GetLineGroupReq req = new com.cisco.axl.api._10.GetLineGroupReq();
			com.cisco.axl.api._10.RLineGroup returnedTags = new com.cisco.axl.api._10.RLineGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetLineGroupRes resp = cucm.getAXLConnectionV105().getLineGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getLineGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.huntlist))
			{
			com.cisco.axl.api._10.GetHuntListReq req = new com.cisco.axl.api._10.GetHuntListReq();
			com.cisco.axl.api._10.RHuntList returnedTags = new com.cisco.axl.api._10.RHuntList();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetHuntListRes resp = cucm.getAXLConnectionV105().getHuntList(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getHuntList().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.callpickupgroup))
			{
			com.cisco.axl.api._10.GetCallPickupGroupReq req = new com.cisco.axl.api._10.GetCallPickupGroupReq();
			com.cisco.axl.api._10.RCallPickupGroup returnedTags = new com.cisco.axl.api._10.RCallPickupGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetCallPickupGroupRes resp = cucm.getAXLConnectionV105().getCallPickupGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getCallPickupGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.voicemail))
			{
			com.cisco.axl.api._10.GetVoiceMailProfileReq req = new com.cisco.axl.api._10.GetVoiceMailProfileReq();
			com.cisco.axl.api._10.RVoiceMailProfile returnedTags = new com.cisco.axl.api._10.RVoiceMailProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetVoiceMailProfileRes resp = cucm.getAXLConnectionV105().getVoiceMailProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getVoiceMailProfile().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.aargroup))
			{
			com.cisco.axl.api._10.GetAarGroupReq req = new com.cisco.axl.api._10.GetAarGroupReq();
			com.cisco.axl.api._10.RAarGroup returnedTags = new com.cisco.axl.api._10.RAarGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetAarGroupRes resp = cucm.getAXLConnectionV105().getAarGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getAarGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.usercontrolgroup))
			{
			com.cisco.axl.api._10.GetUserGroupReq req = new com.cisco.axl.api._10.GetUserGroupReq();
			com.cisco.axl.api._10.RUserGroup returnedTags = new com.cisco.axl.api._10.RUserGroup();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetUserGroupRes resp = cucm.getAXLConnectionV105().getUserGroup(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getUserGroup().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.gateway))
			{
			com.cisco.axl.api._10.GetGatewayReq req = new com.cisco.axl.api._10.GetGatewayReq();
			com.cisco.axl.api._10.RGateway returnedTags = new com.cisco.axl.api._10.RGateway();
			req.setDomainName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetGatewayRes resp = cucm.getAXLConnectionV105().getGateway(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getGateway().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.commonPhoneConfig))
			{
			com.cisco.axl.api._10.GetCommonPhoneConfigReq req = new com.cisco.axl.api._10.GetCommonPhoneConfigReq();
			com.cisco.axl.api._10.RCommonPhoneConfig returnedTags = new com.cisco.axl.api._10.RCommonPhoneConfig();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetCommonPhoneConfigRes resp = cucm.getAXLConnectionV105().getCommonPhoneConfig(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getCommonPhoneConfig().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.securityProfile))
			{
			com.cisco.axl.api._10.GetPhoneSecurityProfileReq req = new com.cisco.axl.api._10.GetPhoneSecurityProfileReq();
			com.cisco.axl.api._10.RPhoneSecurityProfile returnedTags = new com.cisco.axl.api._10.RPhoneSecurityProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetPhoneSecurityProfileRes resp = cucm.getAXLConnectionV105().getPhoneSecurityProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getPhoneSecurityProfile().getUuid(), itemName, type);
			}
		else if(type.equals(ItemType.softkeytemplate))
			{
			com.cisco.axl.api._10.GetSoftKeyTemplateReq req = new com.cisco.axl.api._10.GetSoftKeyTemplateReq();
			com.cisco.axl.api._10.RSoftKeyTemplate returnedTags = new com.cisco.axl.api._10.RSoftKeyTemplate();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetSoftKeyTemplateRes resp = cucm.getAXLConnectionV105().getSoftKeyTemplate(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getSoftKeyTemplate().getUuid(), itemName, type);
			}
		
		throw new Exception("ItemType \""+type+"\" not found");
		}
	
	/***************
	 * Store and return an XFXType item from an UUID
	 */
	private static com.cisco.axl.api._10.XFkType getXFKV105(String UUID, String itemName, ItemType type)
		{
		com.cisco.axl.api._10.XFkType xfk = new com.cisco.axl.api._10.XFkType();
		//UUID = UUID.toLowerCase();//Temp
		Variables.getUuidList().add(new storedUUID(UUID, itemName, type));//We add the item to the uuid stored slist
		xfk.setUuid(UUID);
		Variables.getLogger().debug("Returned UUID from CUCM : "+xfk.getUuid());
		return xfk;
		}
	
	/***************
	 * return an XFXType item
	 */
	private static com.cisco.axl.api._10.XFkType getXFKWithoutStoringItV105(String UUID, String itemName, ItemType type)
		{
		com.cisco.axl.api._10.XFkType xfk = new com.cisco.axl.api._10.XFkType();
		xfk.setUuid(UUID);
		Variables.getLogger().debug("Returned UUID from CUCM : "+xfk.getUuid());
		return xfk;
		}
	
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static List<Object> doSQLQuery(String request, CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			return doSQLQueryV105(request, cucm);
			}
		
		throw new Exception("Unsupported AXL Version");
		}
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static void doSQLUpdate(String request, CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			doSQLUpdateV105(request, cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static List<Object> doSQLQueryV105(String request, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLQueryReq req = new com.cisco.axl.api._10.ExecuteSQLQueryReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLQueryRes resp = cucm.getAXLConnectionV105().executeSQLQuery(req);//We send the request to the CUCM
		
		List<Object> myList = resp.getReturn().getRow();
		
		return myList;
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static void doSQLUpdateV105(String request, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLUpdateReq req = new com.cisco.axl.api._10.ExecuteSQLUpdateReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLUpdateRes resp = cucm.getAXLConnectionV105().executeSQLUpdate(req);//We send the request to the CUCM
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}

