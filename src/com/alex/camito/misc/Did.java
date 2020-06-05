package com.alex.camito.misc;

/**
 * @author Alexandre RATEL
 *
 * Used to depict one did number
 */
public class Did
	{
	/**
	 * Variables
	 */
	public enum DidType
		{
		lg,
		phone,
		analog,
		none
		}
	
	private String officeid, did, internalNumber, description;
	private boolean toTest;
	private DidType type;
	
	public Did(String officeid, String did, String internalNumber, String description, boolean toTest, DidType type) throws Exception
		{
		super();
		this.officeid = officeid;
		this.did = did;
		this.internalNumber = internalNumber;
		this.description = description;
		this.toTest = toTest;
		this.type = type;
		
		if(this.did.isEmpty())
			{
			throw new Exception("A mandatory field was either incorrect or empty");
			}
		}

	public String getOfficeid()
		{
		return officeid;
		}

	public String getDid()
		{
		return did;
		}

	public String getInternalNumber()
		{
		return internalNumber;
		}

	public String getDescription()
		{
		return description;
		}

	public boolean isToTest()
		{
		return toTest;
		}

	public DidType getType()
		{
		return type;
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
