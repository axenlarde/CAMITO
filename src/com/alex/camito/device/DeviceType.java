package com.alex.camito.device;

import java.util.ArrayList;

import com.alex.camito.cli.OneLine;
import com.alex.camito.misc.ItmType;


/**
 * Device Type
 *
 * @author Alexandre RATEL
 */
public class DeviceType extends ItmType
	{
	/**
	 * Variables
	 */
	private String vendor;
	private ArrayList<OneLine> howToConnect;
	private ArrayList<OneLine> howToSave;
	private ArrayList<OneLine> howToReboot;
	
	public DeviceType(String name, String vendor, ArrayList<OneLine> howToConnect, ArrayList<OneLine> howToSave,
			ArrayList<OneLine> howToReboot) throws Exception
		{
		super(TypeName.valueOf(name.toLowerCase()));
		this.vendor = vendor;
		this.howToConnect = howToConnect;
		this.howToSave = howToSave;
		this.howToReboot = howToReboot;
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
	
	/*2020*//*RATEL Alexandre 8)*/
	}
