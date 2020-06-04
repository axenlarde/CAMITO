package com.alex.camito.device;

/**
 * Used to represent a phone
 *
 * @author Alexandre RATEL
 */
public class BasicPhone
	{
	/**
	 * Variables
	 */
	public enum PhoneStatus
		{
		any,
		registered,
		unregistered,
		rejected,
		partiallyregistered,
		unknown
		};
	
	private String name,
	description,
	model,
	ip;
	
	private PhoneStatus firstStatus;
	private PhoneStatus srcStatus;
	private PhoneStatus dstStatus;
	
	
	public BasicPhone(String name, String description, String model, String ip, PhoneStatus status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.firstStatus = status;
		}

	public BasicPhone(String name, String description, String model, String ip, String status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.firstStatus = PhoneStatus.valueOf(status);
		}

	public BasicPhone(String name, String description, String model)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		}
	
	public void setSrcStatus(PhoneStatus status)
		{
		if(this.firstStatus == null)this.firstStatus = status;
		else this.srcStatus = status;
		}
	
	public void setDstStatus(PhoneStatus status)
		{
		this.dstStatus = status;
		}
	
	public boolean isOK()
		{
		if(dstStatus != null)
			{
			if((firstStatus.equals(PhoneStatus.registered)) && (!dstStatus.equals(PhoneStatus.registered)))
				{
				return false;
				}
			}
		return true;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

	public String getModel()
		{
		return model;
		}

	public void setModel(String model)
		{
		this.model = model;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public PhoneStatus getFirstStatus()
		{
		return firstStatus;
		}

	public PhoneStatus getSrcStatus()
		{
		return srcStatus;
		}

	public PhoneStatus getDstStatus()
		{
		return dstStatus;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
