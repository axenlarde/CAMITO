package com.alex.camito.office.misc;

import java.util.ArrayList;

import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.utils.Variables.OfficeType;

/**
 * @author Alexandre RATEL 
 */
public abstract class SourceOffice
	{
	/**
	 * Variables
	 */
	protected String coda,
	name,
	pole;
	
	protected OfficeType officeType;
	protected ArrayList<ErrorTemplate> errorList;
	
	public SourceOffice(String coda, String name, String pole, OfficeType officeType)
		{
		super();
		this.coda = coda;
		this.name = name;
		this.pole = pole;
		this.officeType = officeType;
		}
	
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		s.append(officeType.name()+" ");
		s.append(coda+" ");
		s.append(name);
		
		int maxchar = 50;
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
		}
	
	public void addError(ErrorTemplate error)
		{
		boolean duplicate = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)errorList.add(error);
		}

	public String getCoda()
		{
		return coda;
		}

	public String getName()
		{
		return name;
		}

	public String getPole()
		{
		return pole;
		}

	public OfficeType getOfficeType()
		{
		return officeType;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}
	
	
	
	/*2020*//*Alexandre RATEL 8)*/
	}
