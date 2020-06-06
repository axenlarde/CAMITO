package com.alex.camito.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.Device;
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
public class Office  
	{
	/**
	 * Variables
	 */
	private StatusType status;
	private ActionType action;
	
	private String id,
	name,
	coda,
	pole,
	dxi;
	
	private OfficeType officeType;
	private CMG cmg;
	private Lot lot;
	
	private boolean exists;
	
	private DevicePool devicePool;
	private ArrayList<BasicPhone> phoneList;
	private ArrayList<Did> didList;
	private ArrayList<ErrorTemplate> errorList;
	private ArrayList<Device> deviceList;

	public Office(BasicOffice bo) throws Exception
		{
		super();
		this.id = bo.getId();
		this.name = bo.getName();
		this.coda = bo.getCoda();
		this.pole = bo.getPole();
		this.dxi = bo.getDxi();
		this.devicePool = new DevicePool(bo.getDevicepool());
		this.officeType = bo.getOfficeType();
		this.cmg = bo.getCmg();
		this.lot = bo.getLot();
		this.didList = bo.getDidList();
		for(BasicDevice bd : bo.getDeviceList())deviceList.add(new Device(bd, action));
		phoneList = new ArrayList<BasicPhone>();
		}

	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		s.append(LanguageManagement.getString("office")+" ");
		s.append(coda+" ");
		s.append(name);
		
		int maxchar = 50;
		
		try
			{
			maxchar = Integer.parseInt(UsefulMethod.getTargetOption("maxinfochar"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Unable to retrieve maxinfochar");
			}
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
		}
	
	public void init() throws Exception
		{
		for(Device d : deviceList)d.init();
		}

	public void build() throws Exception
		{
		/**
		 * We build the associated device pool
		 */
		//We check for the office device pool in both cluster
		try
			{
			devicePool.isExisting(Variables.getSrccucm());//Will raise an exception if not
			exists = true;
			}
		catch(Exception e)
			{
			addError(new ErrorTemplate(getInfo()+" ERROR : the associated device pool was not found in the source cluster : "+devicePool.getName()));
			this.status = StatusType.error;
			exists = false;
			throw new Exception(getInfo()+" ERROR : The associated device pool was not found in the source cluster : "+devicePool.getName());
			}
		
		try
			{
			devicePool.isExisting(Variables.getDstcucm());//Will raise an exception if not
			exists = true;
			}
		catch(Exception e)
			{
			addError(new ErrorTemplate(getInfo()+" ERROR : the associated device pool was not found in the destination cluster : "+devicePool.getName()));
			this.status = StatusType.error;
			exists = false;
			throw new Exception(getInfo()+" ERROR : The associated device pool was not found in the destination cluster : "+devicePool.getName());
			}
		
		/**
		 * We now build the associated phone list
		 */
		phoneList = OfficeTools.getDevicePoolPhoneList(devicePool.getName(), action.equals(ActionType.rollback)?Variables.getDstcucm():Variables.getSrccucm());
		
		/**
		 * We check that no phone are missing in the destination cluster
		 */
		ArrayList<BasicPhone> dstPhoneList = OfficeTools.getDevicePoolPhoneList(devicePool.getName(), action.equals(ActionType.rollback)?Variables.getSrccucm():Variables.getDstcucm());
		
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
			if(missing)Variables.getLogger().debug("Warning : the following phone is missing in the destination cluster : "+bp.getName());
			}
		}

	public void process() throws Exception
		{
		//Write something if needed
		}
	
	public void Resolve() throws Exception
		{
		for(Device d : deviceList)d.resolve();
		}
	
	public void Reset()
		{
		try
			{
			if(exists)
				{
				devicePool.reset(Variables.getSrccucm());
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
	public String doGetDetailedStatus()
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
	
	/**
	 * Add an error to the error list and check for duplicate
	 */
	public void addError(ErrorTemplate error)
		{
		boolean duplicate = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)errorList.add(error);
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

	public String getName()
		{
		return name;
		}

	public String getCoda()
		{
		return coda;
		}

	public String getPole()
		{
		return pole;
		}

	public String getDxi()
		{
		return dxi;
		}

	public OfficeType getOfficeType()
		{
		return officeType;
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


	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
