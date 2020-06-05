package com.alex.camito.device;

import java.lang.reflect.Field;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.camito.cli.CliInjector;
import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.cliProtocol;
import com.alex.camito.misc.ItemToMigrate;
import com.alex.camito.misc.ItmType;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.ReachableStatus;



/**
 * Represent a device
 *
 * @author Alexandre RATEL
 */
public class Device extends ItemToMigrate
	{
	/**
	 * Variables
	 */
	protected String ip,
	mask,
	shortmask,
	gateway,
	officeid,
	user,
	password;
	
	protected ReachableStatus reachable;
	protected CliInjector cliInjector;
	protected cliProtocol connexionProtocol;

	public Device(ItmType type, String id, String name, String ip, String mask, String gateway, String officeid, ActionType action, String user, String password,
			CliProfile cliProfile, cliProtocol connexionProtocol) throws Exception
		{
		super(type, name, id, action);
		this.officeid = officeid;
		this.user = user;
		this.password = password;
		this.connexionProtocol = connexionProtocol;
		if(cliProfile != null)this.cliInjector = new CliInjector(this, cliProfile);
		this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
		this.mask = (InetAddressValidator.getInstance().isValidInet4Address(mask))?mask:"";
		this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
		this.reachable = ReachableStatus.unknown;
		
		/*
		if(ip.isEmpty() || mask.isEmpty() || gateway.isEmpty())
			{
			throw new Exception("A mandatory field was empty");
			}*/
		
		if(ip.isEmpty())
			{
			throw new Exception("A mandatory field was empty");
			}
		
		if(!this.mask.isEmpty())shortmask = UsefulMethod.convertlongMaskToShortOne(this.mask);
		}
	
	public Device(BasicDevice bd, ActionType action) throws Exception
		{
		super(bd.getType(), bd.getName(), bd.getId(), action);
		this.officeid = bd.getOfficeid();
		this.user = bd.getUser();
		this.password = bd.getPassword();
		this.connexionProtocol = bd.getConnexionProtocol();
		if(bd.getCliProfile() != null)this.cliInjector = new CliInjector(this, bd.getCliProfile());
		this.ip = bd.getIp();
		this.mask = bd.getMask();
		this.gateway = bd.getGateway();
		this.reachable = ReachableStatus.unknown;
		shortmask = UsefulMethod.convertlongMaskToShortOne(this.mask);
		}
	
	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		
		s.append(LanguageManagement.getString(type.getName())+" ");
		s.append(ip+" ");
		s.append(name);
		
		int maxchar = 60;
		
		try
			{
			maxchar = Integer.parseInt(UsefulMethod.getTargetOption("maxinfochar"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Unable to retrieve maxinfochar");
			}
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
		}
	
	@Override
	public void doInit() throws Exception
		{
		//Write something if needed
		}
	
	//To init the item
	@Override
	public void doBuild() throws Exception
		{
		/**
		 * We initialize the CLI list
		 */
		if(cliInjector != null)cliInjector.build();
		}

	@Override
	public void doStartSurvey() throws Exception
		{
		if(reachable.equals(ReachableStatus.reachable))Variables.getLogger().debug(name+" "+type+" : The device is reachable (ping)");
		else if(reachable.equals(ReachableStatus.unknown))Variables.getLogger().debug(name+" "+type+" : The device reachability is currently unknown (ping)");
		else Variables.getLogger().debug(name+" "+type+" : The device could not been reach (ping failed)");
		}

	@Override
	public void doUpdate() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doResolve() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doReset()
		{
		//Write something if needed
		}
	
	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String doGetDetailedStatus()
		{
		StringBuffer s = new StringBuffer("");
		
		switch(reachable)
			{
			case reachable:
				{
				s.append("Reachable : true");
				break;
				}
			case unreachable:
				{
				s.append("Reachable : false");
				break;
				}
			default:
				{
				s.append("Reachable : unknown");
				break;
				}
			}
		
		/*
		if(cliInjector.getErrorList().size() > 0)
			{
			s.append("\r\n");
			s.append("Cli error list : \r\n");
			
			for(ErrorTemplate e : cliInjector.getErrorList())
				{
				s.append(e.getErrorDesc()+"\r\n");
				}
			}*/
		
		if((cliInjector != null) && (cliInjector.getErrorList().size() > 0))
			{
			s.append(", Error found");
			this.status = ItmStatus.error;
			}
		else if((errorList != null) && (errorList.size() > 0))s.append(", Error found");
		
		return s.toString();
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
			//We try also in the super class
			for(Field f : this.getClass().getSuperclass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			}
		
		return null;
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
		shortmask = UsefulMethod.convertlongMaskToShortOne(mask);
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

	public boolean isReachable()
		{
		if(this.reachable.equals(ReachableStatus.reachable))return true;
		else return false;
		}

	public void setReachable(ReachableStatus reachable)
		{
		this.reachable = reachable;
		}

	public cliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(cliProtocol connexionProtocol)
		{
		this.connexionProtocol = connexionProtocol;
		}
	
	public CliInjector getCliInjector()
		{
		return cliInjector;
		}

	public void setCliInjector(CliInjector cliInjector)
		{
		this.cliInjector = cliInjector;
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

	public String getShortmask()
		{
		return shortmask;
		}

		
	/*2020*//*RATEL Alexandre 8)*/
	}
