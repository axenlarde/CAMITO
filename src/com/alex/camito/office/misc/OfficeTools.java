package com.alex.camito.office.misc;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.BasicPhone.PhoneStatus;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.risport.RisportTools;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.StatusType;


/**
 * Toolbox of static method about offices
 *
 * @author Alexandre RATEL
 */
public class OfficeTools
	{
	
	/**
	 * Used to get a devicepool associated phones
	 */
	public static ArrayList<BasicPhone> getDevicePoolPhoneList(String DevicePoolName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for devicepool's phone "+DevicePoolName);
		
		ArrayList<BasicPhone> l = new ArrayList<BasicPhone>();
		
		String request = "select d.name, d.description, tm.name as model from device d, devicepool dp, typemodel tm where dp.pkid=d.fkdevicepool and tm.enum=d.tkmodel and d.tkClass='1' and dp.name='"+DevicePoolName+"'";
		
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				BasicPhone bp = new BasicPhone("TBD", "", "");
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						bp.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("description"))
						{
						bp.setDescription(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("model"))
						{
						bp.setModel(list.item(i).getTextContent());
						}
					}
				Variables.getLogger().debug("Phone found : "+bp.getName());
				l.add(bp);
				}
			Variables.getLogger().debug("Found "+l.size()+" phones for "+DevicePoolName);
			return l;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the devicepool's phones for "+DevicePoolName+" "+e.getMessage(),e);
			}
		
		return new ArrayList<BasicPhone>();
		}
	
	/**
	 * Return the device pool name of the given phone
	 */
	public static String getDevicePoolFromPhoneName(String phoneName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for phone's devicePool : "+phoneName);
		
		String request = "select dp.name from device d, devicepool dp where dp.pkid=d.fkdevicepool and d.name='"+phoneName+"'";
		
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						Variables.getLogger().debug("Found devicePool "+list.item(i).getTextContent()+" for phone "+phoneName);
						return list.item(i).getTextContent();
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the phone's devicePool for phone "+phoneName+" : "+e.getMessage(),e);
			}
		return null;
		}
	
	/**
	 * Will execute a streamlined office phone survey
	 * We concatenate all the phone in one big request
	 * to lower the total RIS request per minute and the avoid 
	 * to reach the system limit (15 request per minute max)
	 */
	public static void phoneSurvey(ArrayList<Office> officeList, CUCM srccucm, CUCM dstcucm)
		{
		int officeCount = 0;
		ArrayList<BasicPhone> srcPL = new ArrayList<BasicPhone>();
		ArrayList<BasicPhone> dstPL = new ArrayList<BasicPhone>();
		ArrayList<Office> selectedO = new ArrayList<Office>();
		
		for(Office o : officeList)
			{
			srcPL.addAll(o.getPhoneList());
			dstPL.addAll(o.getPhoneList());
			selectedO.add(o);
			officeCount++;
			}
		
		Variables.getLogger().debug(srccucm.getInfo()+" : Phone survey starts, sending RISRequest for "+officeCount+" offices and "+srcPL.size()+" phones");
		srcPL = RisportTools.doPhoneSurvey(srccucm, srcPL);
		
		Variables.getLogger().debug(dstcucm.getInfo()+" : Phone survey starts, sending RISRequest for "+officeCount+" offices and "+dstPL.size()+" phones");
		dstPL = RisportTools.doPhoneSurvey(dstcucm, dstPL);
		
		//We now put the result back to each office
		for(Office o : selectedO)
			{
			for(BasicPhone bp : o.getPhoneList())
				{
				boolean srcFound = false;
				boolean dstFound = false;
				for(BasicPhone risBP : srcPL)
					{
					if(bp.getName().equals(risBP.getName()))
						{
						srcFound = true;
						break;
						}
					}
				bp.setSrcStatus(srcFound?PhoneStatus.registered:PhoneStatus.unregistered);
				
				for(BasicPhone risBP : dstPL)
					{
					if(bp.getName().equals(risBP.getName()))
						{
						dstFound = true;
						break;
						}
					}
				bp.setDstStatus(dstFound?PhoneStatus.registered:PhoneStatus.unregistered);
				}
			}
		Variables.getLogger().debug("Phone survey ends");
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
