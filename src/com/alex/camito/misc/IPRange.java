package com.alex.camito.misc;

import java.lang.reflect.Field;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.camito.utils.UsefulMethod;

/**
 * IPRange
 *
 * @author Alexandre RATEL
 */
public class IPRange
	{
	/**
	 * Variables
	 */
	private String subnet,longmask,shortmask;

	public IPRange(String subnet, String shortmask) throws Exception
		{
		super();
		if(InetAddressValidator.getInstance().isValidInet4Address(subnet))
			{
			this.subnet = subnet;
			}
		else
			{
			throw new Exception("Invalid subnet");
			}
		
		if(!shortmask.equals(""))
			{
			this.shortmask = shortmask;
			this.longmask = UsefulMethod.convertShortMaskToLongOne(this.shortmask);
			}
		else
			{
			throw new Exception("Invalid mask");
			}
		}

	public IPRange(String ipmask) throws Exception
		{
		if(ipmask.contains("/"))
			{
			String[] temp = ipmask.split("/");
			if(InetAddressValidator.getInstance().isValidInet4Address(temp[0]))
				{
				subnet = temp[0];
				shortmask = temp[1];
				longmask = UsefulMethod.convertShortMaskToLongOne(shortmask);
				}
			else
				{
				throw new Exception("Invalid CIDR format");
				}
			}
		else
			{
			throw new Exception("Invalid CIDR format");
			}
		}
	
	public String getInfo()
		{
		return subnet+"/"+shortmask;
		}
	
	public boolean compareTo(IPRange range)
		{
		if((subnet.equals(range.getSubnet())) && (shortmask.equals(range.getShortmask())))return true;
		return false;
		}
	
	public String getCIDRFormat()
		{
		return subnet+"/"+shortmask;
		}
	
	public String getString(String s) throws Exception
		{
		for(Field f : this.getClass().getDeclaredFields())
			{
			if(f.getName().toLowerCase().equals(s.toLowerCase()))
				{
				return (String) f.get(this);
				}
			}
		
		return null;
		}

	public String getSubnet()
		{
		return subnet;
		}

	public String getLongmask()
		{
		return longmask;
		}

	public String getShortmask()
		{
		return shortmask;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
