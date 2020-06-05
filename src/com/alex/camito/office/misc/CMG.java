package com.alex.camito.office.misc;

import java.lang.reflect.Field;

/**
 * @author Alexandre RATEL
 *
 * Used to depict a CMG group
 */
public class CMG
	{
	/**
	 * Variables
	 */
	public enum CMGName
		{
		SUB1_SUB4_CMG,
		SUB2_SUB3_CMG
		}
	
	private CMGName name;
	private String cucm1, cucm2;
	
	public CMG(CMGName name, String cucm1, String cucm2)
		{
		super();
		this.name = name;
		this.cucm1 = cucm1;
		this.cucm2 = cucm2;
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

	public CMGName getName()
		{
		return name;
		}

	public String getCucm1()
		{
		return cucm1;
		}

	public String getCucm2()
		{
		return cucm2;
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
