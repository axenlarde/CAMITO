package com.alex.camito.cli;

import java.util.ArrayList;

import com.alex.camito.device.Device;



/**
 * Used to store a data fetch using the "get" keyword
 *
 * @author Alexandre RATEL
 */
public class CliGetOutput
	{
	/**
	 * Variables
	 */
	private Device device;
	private ArrayList<CliGetOutputEntry> entryList;
	
	public CliGetOutput(Device device)
		{
		super();
		this.device = device;
		entryList = new ArrayList<CliGetOutputEntry>();
		}

	public void add(CliGetOutputEntry entry)
		{
		this.entryList.add(entry);
		}
	
	public Device getDevice()
		{
		return device;
		}

	public void setDevice(Device device)
		{
		this.device = device;
		}

	public ArrayList<CliGetOutputEntry> getEntryList()
		{
		return entryList;
		}

	public void setEntryList(ArrayList<CliGetOutputEntry> entryList)
		{
		this.entryList = entryList;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
