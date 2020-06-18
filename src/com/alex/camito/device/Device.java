package com.alex.camito.device;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.CliProtocol;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.ReachableStatus;
import com.alex.camito.utils.Variables.StatusType;



/**
 * Represent a device
 *
 * @author Alexandre RATEL
 */
public class Device
	{
	/**
	 * Variables
	 */
	private StatusType status;
	private ActionType action;
	
	private String id,
	name,
	ip,
	mask,
	shortmask,
	gateway,
	officeid,
	user,
	password;
	
	private DeviceType deviceType;
	private ReachableStatus reachable;
	private CliProfile cliProfile;
	private CliProfile rollbackCliProfile;
	private CliProtocol connexionProtocol;
	private ArrayList<ErrorTemplate> errorList;
	
	public Device(BasicDevice bd, ActionType action) throws Exception
		{
		super();
		this.action = action;
		this.id = bd.getId();
		this.name = bd.getName();
		this.deviceType = bd.getDeviceType();
		this.ip = bd.getIp();
		this.mask = bd.getMask();
		this.gateway = bd.getGateway();
		this.officeid = bd.getOfficeid();
		this.user = bd.getUser();
		this.password = bd.getPassword();
		this.cliProfile = bd.getCliProfile();
		this.connexionProtocol = bd.getConnexionProtocol();
		this.reachable = ReachableStatus.unknown;
		shortmask = UsefulMethod.convertlongMaskToShortOne(this.mask);
		this.status = StatusType.init;
		errorList = new ArrayList<ErrorTemplate>();
		}
	
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		
		s.append(deviceType.getName()+" ");
		s.append(ip+" ");
		s.append(name);
		
		int maxchar = 60;
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
		}
	
	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String getDetailedStatus()
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
		
		
		if((errorList != null) && (errorList.size() > 0))s.append(", Error found");
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
	
	/**
	 * Add an error to the error list and check for duplicate
	 */
	public void addError(ErrorTemplate error)
		{
		boolean duplicate = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)errorList.add(error);
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

	public CliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(CliProtocol connexionProtocol)
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

	public String getShortmask()
		{
		return shortmask;
		}

	public StatusType getStatus()
		{
		return status;
		}

	public ActionType getAction()
		{
		return action;
		}

	public String getId()
		{
		return id;
		}

	public String getName()
		{
		return name;
		}

	public DeviceType getDeviceType()
		{
		return deviceType;
		}

	public ReachableStatus getReachable()
		{
		return reachable;
		}

	public CliProfile getCliProfile()
		{
		if(action.equals(ActionType.rollback))return rollbackCliProfile;
		else return cliProfile;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public void setStatus(StatusType status)
		{
		this.status = status;
		}

		
	/*2020*//*RATEL Alexandre 8)*/
	}
