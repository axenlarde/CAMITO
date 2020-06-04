package com.alex.camito.action;

import java.net.InetAddress;

import com.alex.camito.device.Device;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ReachableStatus;


/**
 * used to ping a device
 *
 * @author Alexandre RATEL
 */
public class PingProcess extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private int timeout;

	public PingProcess(Device device, int timeout)
		{
		super();
		this.device = device;
		this.timeout = timeout;
		}
	
	public void run()
		{
		device.setReachable(ping(device.getIp())?ReachableStatus.reachable:ReachableStatus.unreachable);
		}
	
	private boolean ping(String ip)
		{
		try
			{
			InetAddress inet = InetAddress.getByName(ip);
			return inet.isReachable(timeout);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while pinging "+device.getInfo());
			}
		return false;
		}

	public void setTimeout(int timeout)
		{
		this.timeout = timeout;
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
