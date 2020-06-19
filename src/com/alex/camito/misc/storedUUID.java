package com.alex.camito.misc;

import com.alex.camito.utils.Variables.ItemType;

/**********************************
 * Class used to store a UUID
 * 
 * @author RATEL Alexandre
 **********************************/
public class storedUUID
	{
	/**
	 * Variables
	 */
	private String UUID, name, comparison;
	private ItemType type;
	private CUCM cucm;
	
	/***************
	 * Constructor
	 ***************/
	public storedUUID(String uUID, String name, ItemType type, CUCM cucm)
		{
		UUID = uUID;
		this.name = name;
		this.type = type;
		this.cucm = cucm;
		this.comparison = type.name()+name+cucm.getIp();
		}

	public String getUUID()
		{
		return UUID;
		}

	public void setUUID(String uUID)
		{
		UUID = uUID;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public ItemType getType()
		{
		return type;
		}

	public void setType(ItemType type)
		{
		this.type = type;
		}

	public String getComparison()
		{
		return comparison;
		}

	public void setComparison(String comparison)
		{
		this.comparison = comparison;
		}
	
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

