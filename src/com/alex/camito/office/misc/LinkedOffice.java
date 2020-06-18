package com.alex.camito.office.misc;

import com.alex.camito.utils.Variables.OfficeType;

/**
 * @author Alexandre RATEL
 *
 * Sometime there is an office with the same devicepool but a different coda
 * Instead of creating a new basic office, we create a linked office and add it to the main one
 */
public class LinkedOffice extends SourceOffice
	{
	/**
	 * Variables
	 */
	
	public LinkedOffice(String coda, String name, String pole, OfficeType officeType)
		{
		super(coda, name, pole, officeType);
		}	
	
	/*2020*//*Alexandre RATEL 8)*/
	}
