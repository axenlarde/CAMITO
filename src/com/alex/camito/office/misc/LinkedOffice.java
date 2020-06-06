package com.alex.camito.office.misc;

import com.alex.camito.utils.Variables.OfficeType;

/**
 * @author Alexandre RATEL
 *
 * Sometime there is an office with the same devicepool but a different coda
 * Instead of creating a new basic office, we create a linked office and add it to the main one
 */
public class LinkedOffice
	{
	/**
	 * Variables
	 */
	private String coda,
	name,
	pole;
	
	private OfficeType officeType;
	
	public LinkedOffice(String coda, String name, String pole, OfficeType officeType)
		{
		super();
		this.coda = coda;
		this.name = name;
		this.pole = pole;
		this.officeType = officeType;
		}

	public String getCoda()
		{
		return coda;
		}

	public void setCoda(String coda)
		{
		this.coda = coda;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getPole()
		{
		return pole;
		}

	public void setPole(String pole)
		{
		this.pole = pole;
		}

	public OfficeType getOfficeType()
		{
		return officeType;
		}

	public void setOfficeType(OfficeType officeType)
		{
		this.officeType = officeType;
		}
	
	
	
	
	
	/*2020*//*Alexandre RATEL 8)*/
	}
