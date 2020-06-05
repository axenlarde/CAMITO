package com.alex.camito.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.camito.device.BasicPhone;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToMigrate;
import com.alex.camito.misc.ItmType;
import com.alex.camito.misc.ItmType.TypeName;
import com.alex.camito.office.items.DevicePool;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.Lot;
import com.alex.camito.utils.Variables.OfficeType;



/**
 * Represent an office
 *
 * @author Alexandre RATEL
 */
public class Office extends ItemToMigrate  
	{
	/**
	 * Variables
	 */
	private String coda,
	pole,
	dxi,
	devicepool;
	
	private boolean unknownOffice;
	private OfficeType officeType;
	private CMG cmg;
	private Lot lot;
	
	private boolean exists;
	
	private DevicePool dp;
	private ArrayList<BasicPhone> phoneList;
	private ArrayList<Did> didList;
	
	public Office(String name, String id, ActionType action, String coda, String pole, String dxi,
			OfficeType officeType, CMG cmg, Lot lot, ArrayList<Did> didList)
		{
		super(new ItmType(TypeName.office), name, id, action);
		this.coda = coda;
		this.pole = pole;
		this.dxi = dxi;
		this.officeType = officeType;
		this.cmg = cmg;
		this.lot = lot;
		phoneList = new ArrayList<BasicPhone>();
		this.didList = didList;
		unknownOffice = false;
		exists = false;
		}
	
	public Office(BasicOffice bo, ActionType action)
		{
		super(bo.getType(), bo.getName(), bo.getId(), action);
		this.coda = bo.getCoda();
		this.unknownOffice = bo.isUnknownOffice();
		phoneList = new ArrayList<BasicPhone>();
		exists = false;
		
		if(unknownOffice)
			{
			Variables.getLogger().debug("Reminder : "+coda+" is an unknown office. Should just reset associated phones");
			}
		else
			{
			this.name = bo.getName();
			this.pole = bo.getPole();
			this.dxi = bo.getDxi();
			this.devicepool = bo.getDevicepool();
			this.cmg = bo.getCmg();
			this.lot = bo.getLot();
			this.officeType = bo.getOfficeType();
			this.didList = bo.getDidList();
			}
		}

	

	@Override
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
	
	@Override
	public void doInit() throws Exception
		{
		//Write something if needed
		}

	@Override
	public void doBuild() throws Exception
		{
		/**
		 * We build the associated device pool
		 */
		dp = new DevicePool(devicepool);
		//We check for the office device pool
		try
			{
			dp.isExisting(Variables.getSrccucm());//Will raise an exception if not
			dp.isExisting(Variables.getDstcucm());//Will raise an exception if not
			exists = true;
			}
		catch(Exception e)
			{
			Variables.getLogger().error(name+" warning : The associated device pool was not found : "+dp.getName());
			addError(new ErrorTemplate(name+" warning the associated device pool was not found : "+dp.getName()));
			}
		
		/**
		 * We now build the associated phone list
		 */
		phoneList = OfficeTools.getDevicePoolPhoneList(dp.getName(), action.equals(ActionType.rollback)?Variables.getDstcucm():Variables.getSrccucm());
		
		/**
		 * We check that no phone are missing in the destination cluster
		 */
		ArrayList<BasicPhone> dstPhoneList = OfficeTools.getDevicePoolPhoneList(dp.getName(), action.equals(ActionType.rollback)?Variables.getSrccucm():Variables.getDstcucm());
		
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
	
	/**
	 * Will check items and return the error list
	 */
	@Override
	public void doStartSurvey() throws Exception
		{
		//Write something if needed
		}

	@Override
	public void doUpdate() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doResolve() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doReset()
		{
		try
			{
			if(exists)
				{
				dp.reset(Variables.getSrccucm());
				}
			else Variables.getLogger().debug(getIndex()+" Reset could not be performed because the device pool was not found");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while reseting devices for "+type+" "+name+" "+e.getMessage(), e);
			addError(new ErrorTemplate("Failed to reset the device pool for "+type+" "+name));
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

	public String getDevicepool()
		{
		return devicepool;
		}

	public boolean isUnknownOffice()
		{
		return unknownOffice;
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

	public DevicePool getDp()
		{
		return dp;
		}

	public ArrayList<BasicPhone> getPhoneList()
		{
		return phoneList;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
