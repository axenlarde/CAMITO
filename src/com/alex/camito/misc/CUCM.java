package com.alex.camito.misc;

import com.alex.camito.utils.Variables.CucmVersion;
import com.alex.camito.utils.Variables.ReachableStatus;
import com.cisco.schemas.ast.soap.RisPortType;

/**
 * @author Alexandre RATEL
 *
 * 
 */
public class CUCM
	{
	/**
	 * Variables
	 */
	private String ip, port, user, password;
	private CucmVersion version;
	private com.cisco.axlapiservice10.AXLPort AXLConnectionV105;
	private RisPortType RISConnection;
	private boolean reachable;
	
	public CUCM(String ip, String port, String user, String password, CucmVersion version)
		{
		super();
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.password = password;
		this.version = version;
		this.reachable = false;
		}
	
	public String getInfo()
		{
		return ip+" "+version.name();
		}

	public String getIp()
		{
		return ip;
		}

	public String getPort()
		{
		return port;
		}

	public String getUser()
		{
		return user;
		}

	public String getPassword()
		{
		return password;
		}

	public CucmVersion getVersion()
		{
		return version;
		}

	public RisPortType getRISConnection()
		{
		return RISConnection;
		}

	public void setRISConnection(RisPortType rISConnection)
		{
		RISConnection = rISConnection;
		}

	public com.cisco.axlapiservice10.AXLPort getAXLConnectionV105()
		{
		return AXLConnectionV105;
		}

	public void setAXLConnectionV105(com.cisco.axlapiservice10.AXLPort aXLConnectionV105)
		{
		AXLConnectionV105 = aXLConnectionV105;
		}

	public boolean isReachable()
		{
		return reachable;
		}

	public void setReachable(boolean reachable)
		{
		this.reachable = reachable;
		}

	
	
	
	/*2020*//*Alexandre RATEL 8)*/
	}
