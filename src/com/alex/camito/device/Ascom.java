package com.alex.camito.device;

import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.cliProtocol;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.AscomType;
import com.alex.camito.utils.Variables.ItmType;

/**
 * Dedicated to Ascom device
 *
 * @author Alexandre RATEL
 */
public class Ascom extends Device
	{
	/**
	 * Variables
	 */
	private AscomType deviceType;

	public Ascom(ItmType type, String id, String name, String ip, String mask, String gateway, String officeid, ActionType action, String user, String password,
			CliProfile cliProfile, cliProtocol connexionProtocol, AscomType deviceType) throws Exception
		{
		super(type, id, name, ip, mask, gateway, officeid, action, user, password,
				cliProfile, connexionProtocol);
		this.deviceType = deviceType;
		}

	public Ascom(BasicAscom ba, ActionType action) throws Exception
		{
		super(ba, action);
		this.deviceType = ba.getAscomType();
		}
	
	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		
		s.append(LanguageManagement.getString(type.name())+" ");
		s.append(ip+" ");
		s.append(deviceType+" ");
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
	
	public AscomType getAscomType()
		{
		return deviceType;
		}
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
