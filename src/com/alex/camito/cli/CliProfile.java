package com.alex.camito.cli;

import java.util.ArrayList;

import com.alex.camito.device.DeviceType;



/**
 * Class used to describe a cli injection profile
 *
 * @author Alexandre RATEL
 */
public class CliProfile
	{
	/**
	 * Variables
	 */
	public enum CliProtocol
		{
		ssh,
		telnet,
		auto
		};
	
	private String name;
	private int defaultInterCommandTimer;
	private DeviceType type;
	private ArrayList<OneLine> cliList;
	
	public CliProfile(String name, DeviceType type, ArrayList<OneLine> cliList, int defaultInterCommandTimer)
		{
		super();
		this.name = name;
		this.type = type;
		this.cliList = cliList;
		this.defaultInterCommandTimer = defaultInterCommandTimer;
		}

	public ArrayList<OneLine> getCliList()
		{
		return cliList;
		}

	public void setCliList(ArrayList<OneLine> cliList)
		{
		this.cliList = cliList;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public int getDefaultInterCommandTimer()
		{
		return defaultInterCommandTimer;
		}

	public void setDefaultInterCommandTimer(int defaultInterCommandTimer)
		{
		this.defaultInterCommandTimer = defaultInterCommandTimer;
		}

	public DeviceType getType()
		{
		return type;
		}

	public void setType(DeviceType type)
		{
		this.type = type;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
