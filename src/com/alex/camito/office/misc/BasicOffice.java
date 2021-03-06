package com.alex.camito.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.camito.device.BasicDevice;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.SimpleItem;
import com.alex.camito.utils.Variables;
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
	
	private OfficeType officeType;
	private CMG cmg;
	private Lot lot;
	private ArrayList<BasicDevice> deviceList;
	private ArrayList<Did> didList;
	private ArrayList<LinkedOffice> linkedOffice;
	
	/**
	 * Constructor
	 */
	public BasicOffice(String coda, String name, String pole, String dxi, String devicepool,
			OfficeType officeType, CMG cmg, Lot lot, ArrayList<Did> didList, ArrayList<BasicDevice> deviceList)
		{
		super(name+coda+devicepool);
		this.coda = coda;
		this.name = name;
		this.pole = pole;
		this.dxi = dxi;
		this.devicepool = devicepool;
		this.officeType = officeType;
		this.cmg = cmg;
		this.lot = lot;
		this.deviceList = deviceList;
		this.didList = didList;
		this.linkedOffice = new ArrayList<LinkedOffice>();
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
		
		throw new Exception("String not found");
		}
	
	public void addLinkedOffice(LinkedOffice linkedOffice)
		{
		this.linkedOffice.add(linkedOffice);
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


	public ArrayList<LinkedOffice> getLinkedOffice()
		{
		return linkedOffice;
		}

	

	/*2020*//*RATEL Alexandre 8)*/
	}
