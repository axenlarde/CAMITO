package com.alex.camito.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.camito.device.BasicDevice;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.SimpleItem;
import com.alex.camito.utils.Variables.CMG;
import com.alex.camito.utils.Variables.Lot;
import com.alex.camito.utils.Variables.OfficeType;


/**
 * Used to store office data
 *
 * @author Alexandre RATEL
 */
public class BasicOffice extends SimpleItem
	{
	/**
	 * Variables
	 */
	private String coda,
	name,
	pole,
	dxi,
	devicepool;
	
	private boolean unknownOffice;
	private OfficeType officeType;
	private CMG cmg;
	private Lot lot;
	private ArrayList<BasicDevice> deviceList;
	private ArrayList<Did> didList;
	
	/**
	 * Constructor
	 */
	public BasicOffice(String coda, String name, String pole, String dxi, String devicepool,
			OfficeType officeType, CMG cmg, Lot lot, ArrayList<Did> didList)
		{
		super(name+coda);
		this.coda = coda;
		this.name = name;
		this.pole = pole;
		this.dxi = dxi;
		this.devicepool = devicepool;
		this.officeType = officeType;
		this.cmg = cmg;
		this.lot = lot;
		deviceList = new ArrayList<BasicDevice>();
		this.didList = didList;
		unknownOffice = false;
		}
	
	/**
	 * Used to create an office just for phone reset purpose
	 * @throws Exception 
	 */
	public BasicOffice(String coda) throws Exception
		{
		super("Unknown"+coda);
		this.name = "Unknown office "+coda;
		this.coda = coda;
		this.pole = "Unknown";
		this.dxi = "0";
		this.devicepool = "Unknown";
		this.cmg = CMG.SUB1_SUB4_CMG;
		this.lot = Lot.UNKNOWN;
		this.officeType = OfficeType.AGENCE;
		deviceList = new ArrayList<BasicDevice>();
		didList = new ArrayList<Did>();
		unknownOffice = true;
		}
	

	public String getInfo()
		{
		return coda+" "+
			name;
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
			}
		
		throw new Exception("String not found");
		}

	public String getCoda()
		{
		return coda;
		}

	public String getName()
		{
		return name;
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

	public ArrayList<BasicDevice> getDeviceList()
		{
		return deviceList;
		}

	public ArrayList<Did> getDidList()
		{
		return didList;
		}

	

	/*2020*//*RATEL Alexandre 8)*/
	}
