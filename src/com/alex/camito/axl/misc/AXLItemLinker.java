package com.alex.camito.axl.misc;

import java.util.ArrayList;

import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.CucmVersion;



/**********************************
 * Class used to define an AXLItem
 * 
 * An AXLItem is intent to link the CUCM AXL API with the
 * software. Indeed the AXL API is evaluating version after version
 * and the AXLItem should be modified as less as possible
 * 
 * @author RATEL Alexandre
 **********************************/
public abstract class AXLItemLinker implements AXLItemLinkerImpl
	{
	/**
	 * Variables
	 */
	protected String name;
	
	
	/**
	 * Constructor
	 * @throws Exception 
	 */
	public AXLItemLinker(String name) throws Exception
		{
		this.name = name;
		//init();
		}
	
	/****
	 * We initialize here what is needed
	 */
	public ArrayList<ErrorTemplate> init() throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			return doInitVersion105();
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	
	/**
	 * Used to define the injection process for this item
	 */
	public String inject(CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			return doInjectVersion105(cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}

	/**
	 * Used to define the deletion process for this item
	 */
	public void delete(CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			doDeleteVersion105(cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");	
			}
		}
	
	/**
	 * Used to define the update process for this item
	 */
	public void update(ArrayList<ToUpdate> tulist, CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			doUpdateVersion105(tulist, cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	public ItemToInject get(CUCM cucm) throws Exception
		{
		if(Variables.getCUCMVersion().equals(CucmVersion.version105))
			{
			return doGetVersion105(cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}

