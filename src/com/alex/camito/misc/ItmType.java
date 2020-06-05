package com.alex.camito.misc;

/**
 * @author Alexandre RATEL
 *
 * 
 */
public class ItmType
	{
	/**
	 * Variables
	 */
	
	public enum TypeName
		{
		office,
		isr,
		vg,
		dect,
		phone,
		sip
		};
		
	protected TypeName name;

	public ItmType(TypeName name)
		{
		super();
		this.name = name;
		}

	public String getName()
		{
		return name.name();
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
