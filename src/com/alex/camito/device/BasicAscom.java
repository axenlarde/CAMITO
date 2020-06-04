package com.alex.camito.device;

import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.cliProtocol;
import com.alex.camito.utils.Variables.AscomType;
import com.alex.camito.utils.Variables.ItmType;

/**
 * Dedicated to ascom devices
 *
 * @author Alexandre RATEL
 */
public class BasicAscom extends BasicDevice
	{
	/**
	 * Variables
	 */
	private AscomType deviceType;

	public BasicAscom(ItmType type, String name, String ip, String mask, String gateway, String officeid, String user, String password, CliProfile cliProfile,
			cliProtocol connexionProtocol, AscomType deviceType) throws Exception
		{
		super(type, name, ip, mask, gateway, officeid, user, password, cliProfile,
				connexionProtocol);
		this.deviceType = deviceType;
		}

	public AscomType getAscomType()
		{
		return deviceType;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
