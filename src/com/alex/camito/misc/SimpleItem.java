package com.alex.camito.misc;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.camito.utils.Variables;

public class SimpleItem
	{
	/**
	 * Variables
	 */
	public enum basicItemStatus
		{
		tomigrate,
		migrated
		};	
	
	protected String id;
	protected basicItemStatus status; 
	protected ItmType type;
	
	public SimpleItem(String patternID, ItmType type)
		{
		this.id = DigestUtils.md5Hex(patternID);
		if(Variables.getMigratedItemList().contains(id))status = basicItemStatus.migrated;
		else status = basicItemStatus.tomigrate;
		this.type = type;
		}
	
	public basicItemStatus getStatus()
		{
		return status;
		}

	public void setStatus(basicItemStatus status)
		{
		this.status = status;
		}

	public String getId()
		{
		return id;
		}

	public ItmType getType()
		{
		return type;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
