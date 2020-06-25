package com.alex.camito.device;

import java.util.ArrayList;

import com.alex.camito.cli.OneLine;


/**
 * Device Type
 *
 * @author Alexandre RATEL
 */
public class DeviceType
	{
	/**
	 * Variables
	 */
	private String name, vendor, checkTelnet;
	private ArrayList<OneLine> howToConnect;
	private ArrayList<OneLine> secondaryHowToConnect;
	private ArrayList<OneLine> howToSave;
	private ArrayList<OneLine> howToReboot;
	
	public DeviceType(String name, String vendor, String checkTelnet, ArrayList<OneLine> howToConnect, ArrayList<OneLine> secondaryHowToConnect,
			ArrayList<OneLine> howToSave, ArrayList<OneLine> howToReboot) throws Exception
		{
		super();
		this.name = name;
		this.vendor = vendor;
		this.checkTelnet = checkTelnet;
		this.howToConnect = howToConnect;
		this.secondaryHowToConnect = secondaryHowToConnect;
		this.howToSave = howToSave;
		this.howToReboot = howToReboot;
		}
	
	public void resolve(Device d) throws Exception
		{
		for(OneLine ol : howToConnect)
			{
			ol.resolve(d);
			}
		
		for(OneLine ol : secondaryHowToConnect)
			{
			ol.resolve(d);
			}
		
		for(OneLine ol : howToReboot)
			{
			ol.resolve(d);
			}
		
		for(OneLine ol : howToSave)
			{
			ol.resolve(d);
			}
		}

	public String getName()
		{
		return name;
		}

	public String getVendor()
		{
		return vendor;
		}

	public ArrayList<OneLine> getHowToConnect()
		{
		return howToConnect;
		}

	public ArrayList<OneLine> getHowToSave()
		{
		return howToSave;
		}

	public ArrayList<OneLine> getHowToReboot()
		{
		return howToReboot;
		}

	public String getCheckTelnet()
		{
		return checkTelnet;
		}

	public ArrayList<OneLine> getSecondaryHowToConnect()
		{
		return secondaryHowToConnect;
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
