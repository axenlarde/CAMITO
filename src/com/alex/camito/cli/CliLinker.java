package com.alex.camito.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import com.alex.camito.cli.CliConnection.connectedTech;
import com.alex.camito.cli.CliProfile.CliProtocol;
import com.alex.camito.device.Device;
import com.alex.camito.device.DeviceType;
import com.alex.camito.misc.CollectionTools;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;


/**
 * This class will connect to the given device and send the given command
 *
 * @author Alexandre RATEL
 */
public class CliLinker
	{
	/**
	 * Variables
	 */
	private Device device;
	private CliProtocol protocol;
	private CliConnection connection;
	private CliInjector clii;
	private BufferedWriter out;
	private AnswerReceiver receiver;
	private int timeout;
	private String carrierReturn;
	
	public CliLinker(CliInjector clii)
		{
		super();
		this.clii = clii;
		this.device = clii.getDevice();
		this.protocol = device.getConnexionProtocol();
		
		if(protocol.equals(CliProtocol.ssh))
			{
			this.carrierReturn = "\n";
			}
		else
			{
			this.carrierReturn = "\r\n";
			}
		
		try
			{
			this.timeout = Integer.parseInt(UsefulMethod.getTargetOption("cliconnectiontimeout"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error(device.getInfo()+" : CLI : Unable to find timeout so we apply the default value");
			this.timeout = 10000;
			}
		}
	
	/**
	 * here we connect using the given ip
	 * @throws ConnectionException 
	 */
	public void connect(String ip) throws ConnectionException, Exception
		{
		try
			{
			if((connection == null) || (!connection.isConnected()))
				{
				connection = new CliConnection(device, timeout);
				connection.connect();
				out = connection.getOut();
				receiver = connection.getReceiver();
				
				/**
				 * Using telnet credentials cannot be sent during the connection process
				 * Instead, we have to send them once connected when prompted
				 * So we add an extra step
				 */
				if(connection.getcTech().equals(connectedTech.telnet))
					{
					telnetAuth();
					}
				}
			}
		catch (Exception e)
			{
			throw new ConnectionException("Failed to connect : "+e.getMessage());
			}
		}
	
	/**
	 * Here we connect using the default device ip
	 * @throws ConnectionException 
	 */
	public void connect() throws ConnectionException, Exception
		{
		connect(device.getIp());
		}
	
	public void disconnect()
		{
		if((connection != null) && (connection.isConnected()))connection.close();
		}
	
	public String waitFor(String s) throws ConnectionException, Exception
		{
		int timer = 0;
		
		Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for the word :"+s);
		
		boolean onlyOnce = true;
		
		while(true)
			{
			for(int i=0; i<receiver.getExchange().size(); i++)
				{
				//(?i) : Case insensitive
				if(Pattern.matches("(?i).*"+s+".*",receiver.getExchange().get(i)))
					{
					String SToReturn = receiver.getExchange().get(i);
					receiver.getExchange().clear();
					return SToReturn;
					}
				else if(onlyOnce)
					{
					/**
					 * We send a carriage return just to activate the connection
					 * and only once
					 */
					out.write(carrierReturn);
					out.flush();
					onlyOnce = false;
					}
				}
			
			clii.sleep(100);
			if(timer>100)
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : We have been waiting too longfor '"+s+"' so we keep going");
				break;
				}
			timer++;
			}
		return null;
		}
	
	/**
	 * Simply wait for a return from the gateway
	 * @throws ConnectionException, Exception 
	 */
	private String waitForAReturn() throws ConnectionException, Exception
		{
		int timer = 0;
		
		//Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for a reply");
		
		while(true)
			{
			if(receiver.getExchange().size() > 0)return receiver.getExchange().get(0);
			
			clii.sleep(100);
			if(timer>100)
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : We have been waiting too long so we keep going");
				break;
				}
			timer++;
			}
		return null;
		}
	
	
	public void write(String s) throws ConnectionException, Exception
		{
		receiver.getExchange().clear();
		out.write(s+carrierReturn);
		out.flush();
		waitForAReturn();
		}
	
	/**
	 * Write a command and then analyze the result
	 * if the result contains the given string we will write the given command
	 * otherwise will write the other command instead
	 * 
	 * The command pattern is the following :
	 * command to send:::string to compare:::write if the result contains the string to compare:::write if not
	 * The last parameters is optional. You can write only a:::b:::c
	 * @throws Exception 
	 * @throws ConnectionException 
	 */
	public void writeIf(String s) throws ConnectionException, Exception 
		{
		String[] cmdTab = s.split(":::");
		receiver.getExchange().clear();
		out.write(cmdTab[0]+carrierReturn);
		out.flush();
		
		if(waitForAReturn().toLowerCase().equals(cmdTab[1].toLowerCase()))
			{
			write(cmdTab[2]);
			}
		else
			{
			if(cmdTab.length == 4)write(cmdTab[3]);
			}
		}
	
	/**
	 * Write a command and then store the result in an output file
	 * 
	 * The command pattern is the following :
	 * Column name:::Command to send:::How many line to store:::Regex to apply to the collected output
	 * The last two parameters are optional
	 * If nothing is mentioned we will just collect the first line returned
	 * 
	 * Regex will come in a later release
	 * @throws IOException 
	 */
	public void get(String s) throws IOException, Exception
		{
		String[] cmdTab = s.split(":::");
		int howManyToReturn = 2;
		String regex = null;
		
		clii.sleep(100);//Just to be sure we don't get something from the previous command
		receiver.getExchange().clear();
		out.write(cmdTab[1]+carrierReturn);
		out.flush();
		
		if(cmdTab.length>2 && Integer.parseInt(cmdTab[2])>2)howManyToReturn = Integer.parseInt(cmdTab[2])+1;
		if(cmdTab.length>3)regex = cmdTab[3];
		waitForAReturn();
		clii.sleep(100);//We've seen some latency, so better to wait a bit to get the data
		
		//We get just what we need
		StringBuffer replyWanted = new StringBuffer("");
		for(int i=1;(i<receiver.getExchange().size()) && (i<howManyToReturn); i++)//We start from 1 because the line 0 is just what we've just sent
			{
			replyWanted.append(receiver.getExchange().get(i));
			}
		
		//Once we get a return we add it in a CliGetOutput
		CliGetOutput cgo = UsefulMethod.getCliGetOutput(device);
		if(cgo == null)
			{
			cgo = new CliGetOutput(device);
			Variables.getCliGetOutputList().add(cgo);
			}
		
		String result = replyWanted.toString();
		if(regex != null)result = CollectionTools.resolveRegex(result, regex);
		
		cgo.add(new CliGetOutputEntry(cmdTab[0], result));
		
		Variables.getLogger().debug(device.getInfo()+" : Data retreived using a 'get' instruction : "+result);
		}
	
	
	/**
	 * Aims to send telnet credentials once prompted
	 */
	private void telnetAuth() throws Exception
		{
		try
			{
			/*******
			 * Write instruction used to authenticate to gateway using telnet protocol 
			 */
			Variables.getLogger().debug(device.getInfo()+" : CLI : Authentication process begin");
			for(OneLine l : device.getDeviceType().getHowToConnect())
				{
				execute(l);
				}
			
			/**
			 * We check if the authentication was successful
			 * If not we try the secondary credentials
			 */
			receiver.getExchange().clear();
			waitForAReturn();
			clii.sleep(1000);//Just to be sure we received the full answer
			for(String s : receiver.getExchange())
				{
				if(s.toLowerCase().contains(device.getDeviceType().getCheckTelnet().toLowerCase()))
					{
					Variables.getLogger().debug(device.getInfo()+" : CLI : Telnet authentication failed using the first credentials so we try the second ones");
					for(OneLine l : device.getDeviceType().getSecondaryHowToConnect())
						{
						execute(l);
						}
					receiver.getExchange().clear();
					waitForAReturn();
					clii.sleep(1000);//Just to be sure we received the full answer
					for(String s2 : receiver.getExchange())
						{
						if(s.toLowerCase().contains(device.getDeviceType().getCheckTelnet().toLowerCase()))
							{
							Variables.getLogger().debug(device.getInfo()+" : CLI : Telnet authentication failed using the second credentials");
							throw new Exception("Bad credentials");
							}
						}
					}
				}
			
			Variables.getLogger().debug(device.getInfo()+" : CLI : Telnet connection initiated successfully");
			}
		catch(Exception exc)
			{
			throw new ConnectionException(device.getInfo()+" : CLI : Unable to connect using telnet : "+exc.getMessage());
			}
		}
	
	public void execute(OneLine l) throws ConnectionException, Exception
		{
		//Variables.getLogger().debug(device.getInfo()+" : CLI : Sending : "+l.getCommand());
		switch(l.getType())
			{
			case connect:
				{
				connect(l.getCommand());
				break;
				}
			case disconnect:
				{
				disconnect();
				break;
				}
			case wait:
				{
				Variables.getLogger().debug(device.getInfo()+" : CLI : Waiting for "+l.getCommand()+" ms");
				clii.sleep(Long.parseLong(l.getCommand()));
				break;
				}
			case waitfor:
				{
				waitFor(l.getCommand());
				break;
				}
			case write:
				{
				write(l.getCommand());
				break;
				}
			case writeif:
				{
				writeIf(l.getCommand());
				break;
				}
			case get:
				{
				get(l.getCommand());
				break;
				}
			case save:
				{
				for(OneLine ol : device.getDeviceType().getHowToSave())
					{
					execute(ol);
					}
				break;
				}
			case reboot:
				{
				for(OneLine ol : device.getDeviceType().getHowToReboot())
					{
					execute(ol);
					}
				break;
				}
			default:
				{
				write(l.getCommand());
				break;
				}
			}
		}

	public AnswerReceiver getReceiver()
		{
		return receiver;
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
