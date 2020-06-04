package com.alex.camito.device;

import java.lang.reflect.Field;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.cliProtocol;
import com.alex.camito.misc.SimpleItem;
import com.alex.camito.utils.Variables.ItmType;



/**
 * Used to store a device data
 *
 * @author Alexandre RATEL
 */
public class BasicDevice extends SimpleItem
	{
	/**
	 * Variables
	 */
	protected ItmType type;
	protected String id,
	name,
	ip,
	mask,
	gateway,
	officeid,
	officename,
	user,
	password;
	protected CliProfile cliProfile;
	protected cliProtocol connexionProtocol;
	protected basicItemStatus status;
	
	
	public BasicDevice(ItmType type, String name, String ip, String mask, String gateway, String officeid,
			String user, String password, CliProfile cliProfile, cliProtocol connexionProtocol) throws Exception
		{
		super(name+ip+officeid);
		this.type = type;
		this.name = name;
		this.officeid = officeid;
		this.user = user;
		this.password = password;
		this.cliProfile = cliProfile;
		this.connexionProtocol = connexionProtocol;
		
		this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
		this.mask = (InetAddressValidator.getInstance().isValidInet4Address(mask))?mask:"";
		this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
		
		if(this.ip.isEmpty() || this.mask.isEmpty() || this.gateway.isEmpty())
			{
			throw new Exception(getInfo()+" : A mandatory field was either incorrect or empty");
			}
		}

	public String getInfo()
		{
		return type+" "+
		ip+" "+
		name;
		}
	
	/******
	 * Used to return a value based on the string provided
	 * @throws Exception 
	 */
	public String getString(String s) throws Exception
		{
		String tab[] = s.split("\\.");
		
		if(tab.length == 2)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			}
		
		return null;
		}

	public ItmType getType()
		{
		return type;
		}

	public void setType(ItmType type)
		{
		this.type = type;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public String getMask()
		{
		return mask;
		}

	public void setMask(String mask)
		{
		this.mask = mask;
		}

	public String getGateway()
		{
		return gateway;
		}

	public void setGateway(String gateway)
		{
		this.gateway = gateway;
		}

	public String getOfficeid()
		{
		return officeid;
		}

	public void setOfficeid(String officeid)
		{
		this.officeid = officeid;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public CliProfile getCliProfile()
		{
		return cliProfile;
		}

	public void setCliProfile(CliProfile cliProfile)
		{
		this.cliProfile = cliProfile;
		}

	public cliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(cliProtocol connexionProtocol)
		{
		this.connexionProtocol = connexionProtocol;
		}

	public String getUser()
		{
		return user;
		}

	public void setUser(String user)
		{
		this.user = user;
		}

	public String getPassword()
		{
		return password;
		}

	public void setPassword(String password)
		{
		this.password = password;
		}

	public String getOfficename()
		{
		return officename;
		}

	public void setOfficename(String officename)
		{
		this.officename = officename;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}
