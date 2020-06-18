package com.alex.camito.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.Device;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.office.items.DevicePool;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.Lot;
import com.alex.camito.utils.Variables.OfficeType;
import com.alex.camito.utils.Variables.StatusType;



/**
 * Represent an office
 *
 * @author Alexandre RATEL
 */
public class Office extends SourceOffice
	{
	/**
	 * Variables
	 */
	private StatusType status;
	private ActionType action;
	
	private String id,
	dxi;
	
	private CMG cmg;
	private Lot lot;
	
	private boolean exists;
	
	private DevicePool devicePool;
	private ArrayList<BasicPhone> phoneList;
	private ArrayList<BasicPhone> dstPhoneList;
	private ArrayList<BasicPhone> missingPhone;
	private ArrayList<Did> didList;
	private ArrayList<Device> deviceList;
	private ArrayList<LinkedOffice> linkedOffice;

	public Office(BasicOffice bo, ActionType action) throws Exception
		{
		super(bo.getCoda(), bo.getName(), bo.getPole(), bo.getOfficeType());
		this.action = action;
		this.id = bo.getId();
		this.dxi = bo.getDxi();
		this.devicePool = new DevicePool(bo.getDevicepool());
		this.cmg = bo.getCmg();
		this.lot = bo.getLot();
		this.didList = bo.getDidList();
		deviceList = new ArrayList<Device>();
		for(BasicDevice bd : bo.getDeviceList())deviceList.add(new Device(bd, action));
		phoneList = new ArrayList<BasicPhone>();
		dstPhoneList = new ArrayList<BasicPhone>();
		missingPhone = new ArrayList<BasicPhone>();
		errorList = new ArrayList<ErrorTemplate>();
		this.status = StatusType.init;
		errorList = new ArrayList<ErrorTemplate>();
		this.linkedOffice = bo.getLinkedOffice();
		}

	public void build(CUCM srccucm, CUCM dstcucm) throws Exception
		{
		/**
		 * We build the associated device pool
		 */
		//We check for the office device pool in both cluster
		try
			{
			devicePool.isExisting(srccucm);//Will raise an exception if not
			exists = true;
			}
		catch(Exception e)
			{
			addError(new ErrorTemplate(getInfo()+" : ERROR : the associated device pool was not found in the source cluster : "+devicePool.getName()));
			this.status = StatusType.error;
			exists = false;
			return;
			}
		
		try
			{
			devicePool.isExisting(dstcucm);//Will raise an exception if not
			exists = true;
			}
		catch(Exception e)
			{
			addError(new ErrorTemplate(getInfo()+" : ERROR : the associated device pool was not found in the destination cluster : "+devicePool.getName()));
			this.status = StatusType.error;
			exists = false;
			return;
			}
		
		/**
		 * We now build the associated phone lists
		 */
		phoneList = OfficeTools.getDevicePoolPhoneList(devicePool.getName(), srccucm);
		dstPhoneList = OfficeTools.getDevicePoolPhoneList(devicePool.getName(), dstcucm);
		
		/**
		 * We check that no phone are missing in the destination cluster
		 */
		for(BasicPhone bp : phoneList)
			{
			boolean missing = true;
			for(BasicPhone dstBP : dstPhoneList)
				{
				if(dstBP.getName().equals(bp.getName()))
					{
					missing = false;
					break;
					}
				}
			if(missing)
				{
				missingPhone.add(bp);
				Variables.getLogger().debug("Warning : the following phone is missing in the destination cluster : "+bp.getName());
				}
			}
		}
	
	public void reset(CUCM cucm)
		{
		try
			{
			if(exists)
				{
				devicePool.reset(cucm);
				Variables.getLogger().debug(getInfo()+" Reset done");
				}
			else Variables.getLogger().debug(getInfo()+" : Reset could not be performed because the device pool was not found");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(getInfo()+" : ERROR while reseting devices "+e.getMessage(), e);
			addError(new ErrorTemplate(getInfo()+" : Failed to reset the device pool"));
			}
		}
	
	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String getDetailedStatus()
		{
		StringBuffer result = new StringBuffer("");
		
		StringBuffer temp = new StringBuffer("");
		int lostPhone = 0;
		for(BasicPhone p : phoneList)
			{
			if(!p.isOK())
				{
				lostPhone++;
				temp.append("+ "+p.getName()+" "+p.getModel()+" "+p.getDescription()+" : "+p.getSrcStatus()+"\r\n");
				}
			}
		
		String phone = LanguageManagement.getString("phone");
		String found = LanguageManagement.getString("found");
		String lost = LanguageManagement.getString("lost");
		
		result.append(phone+" : "+found+" "+phoneList.size()+", "+lost+" "+lostPhone);
		Variables.getLogger().debug(this.name+" : Lost phone found : "+temp);
		//result.append(temp);
		
		return result.toString();
		}
	
	/******
	 * Used to return a value based on the string provided
	 * @throws Exception 
	 */
	public String getString(String s) throws Exception
		{
		String tab[] = s.split("\\.");
		
		if(tab.length == 2)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			//We try also in the super class
			for(Field f : this.getClass().getSuperclass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			}
		else if(tab.length == 3)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					if(f.get(this) instanceof CMG)
						{
						return ((CMG) f.get(this)).getString(tab[2]);
						}
					}
				}
			}
		
		return null;
		}

	public StatusType getStatus()
		{
		return status;
		}

	public ActionType getAction()
		{
		return action;
		}

	public String getId()
		{
		return id;
		}

	public String getDxi()
		{
		return dxi;
		}

	public CMG getCmg()
		{
		return cmg;
		}

	public Lot getLot()
		{
		return lot;
		}

	public boolean isExists()
		{
		return exists;
		}

	public DevicePool getDevicePool()
		{
		return devicePool;
		}

	public ArrayList<BasicPhone> getPhoneList()
		{
		return phoneList;
		}

	public ArrayList<Did> getDidList()
		{
		return didList;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public ArrayList<Device> getDeviceList()
		{
		return deviceList;
		}

	public void setStatus(StatusType status)
		{
		this.status = status;
		}

	public ArrayList<BasicPhone> getDstPhoneList()
		{
		return dstPhoneList;
		}

	public ArrayList<BasicPhone> getMissingPhone()
		{
		return missingPhone;
		}

	public ArrayList<LinkedOffice> getLinkedOffice()
		{
		return linkedOffice;
		}


	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
