package com.alex.camito.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFileChooser;
import javax.xml.ws.BindingProvider;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Level;

import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.cliProtocol;
import com.alex.camito.cli.OneLine;
import com.alex.camito.cli.OneLine.cliType;
import com.alex.camito.device.BasicAscom;
import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.BasicPhone;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.Did.DidType;
import com.alex.camito.misc.IPRange;
import com.alex.camito.misc.SimpleItem.basicItemStatus;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.misc.ValueMatcher;
import com.alex.camito.office.misc.BasicOffice;
import com.alex.camito.risport.RisportTools;
import com.alex.camito.utils.Variables.AscomType;
import com.alex.camito.utils.Variables.CMG;
import com.alex.camito.utils.Variables.CucmVersion;
import com.alex.camito.utils.Variables.ItemType;
import com.alex.camito.utils.Variables.ItmType;
import com.alex.camito.utils.Variables.Lot;
import com.alex.camito.utils.Variables.OfficeType;
import com.alex.camito.utils.Variables.StatusType;
import com.alex.camito.utils.Variables.SubstituteType;
import com.cisco.schemas.ast.soap.RISService70;
import com.cisco.schemas.ast.soap.RisPortType;


/**********************************
 * Class used to store the useful static methods
 * 
 * @author RATEL Alexandre
 **********************************/
public class UsefulMethod
	{
	
	/*****
	 * Method used to read the main config file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readMainConfigFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead("./"+fileName);
			
			listParams.add("config");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		
		}
	
	/**
	 * Method used to init the userlocal arraylist
	 */
	public static ArrayList<String> initUserLocalList()
		{
		ArrayList<String> userLocal = new ArrayList<String>();
		userLocal.add("French France");
		userLocal.add("Italian Italy");
		userLocal.add("English United States");
		
		return userLocal;
		}
	
	/**
	 * Method used to init the country arraylist
	 */
	public static ArrayList<String> initCountryList()
		{
		ArrayList<String> country = new ArrayList<String>();
		country.add("France");
		country.add("Italy");
		country.add("United Kingdom");
		
		return country;
		}
	
	
	/***************************************
	 * Method used to get a specific value
	 * in the user preference XML File
	 ***************************************/
	public synchronized static String getTargetOption(String node) throws Exception
		{
		//We first seek in the configFile.xml
		for(String[] s : Variables.getTabConfig().get(0))
			{
			if(s[0].equals(node))return s[1];
			}
		
		/***********
		 * If this point is reached, the option looked for was not found
		 */
		throw new Exception("Option \""+node+"\" not found"); 
		}
	/*************************/
	
	
	
	/************************
	 * Check if java version
	 * is correct
	 ***********************/
	public static void checkJavaVersion()
		{
		try
			{
			String jVer = new String(System.getProperty("java.version"));
			Variables.getLogger().info("Detected JRE version : "+jVer);
			
			/*Need to use the config file value*/
			
			if(jVer.contains("1.6"))
				{
				/*
				if(Integer.parseInt(jVer.substring(6,8))<16)
					{
					Variables.getLogger().info("JRE version is not compatible. The application will now exit. system.exit(0)");
					System.exit(0);
					}*/
				}
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().info("ERROR : It has not been possible to detect JRE version",exc);
			}
		}
	/***********************/
	
	/*********************************************
	 * Used to get a file path
	 * @throws Exception 
	 *********************************************/
	public static String getFilePath(ArrayList<String> allowedExtensionList, String invitPhrase, String selectButton) throws Exception
		{
		JFileChooser fcSource = new JFileChooser();
		try
			{
			fcSource.setCurrentDirectory(new File(Variables.getMainDirectory()));
			
			fcSource.setDialogTitle(invitPhrase);
			
			EasyFileFilter myFilter = new EasyFileFilter(allowedExtensionList);
			fcSource.setFileFilter(myFilter);
			
			int resultat = fcSource.showDialog(fcSource, selectButton);
			if(resultat == fcSource.APPROVE_OPTION)
				{
				return fcSource.getSelectedFile().toString();
				}
			else
				{
				return null;
				}
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR : While fetching a file");
			}
		}
	
	/*********************************************
	 * Used to get a file path
	 * @throws Exception 
	 *********************************************/
	public static String getDirectoryPath(String baseDirectory, String invitPhrase, String selectButton) throws Exception
		{
		JFileChooser fcSource = new JFileChooser();
		try
			{
			fcSource.setCurrentDirectory(new File(baseDirectory));
			fcSource.setDialogTitle(LanguageManagement.getString("invitPhrase"));
			
			int resultat = fcSource.showDialog(fcSource, LanguageManagement.getString("selectButton"));
			if(resultat == fcSource.APPROVE_OPTION)
				{
				File mFile = new File(fcSource.getSelectedFile().toString());
				return mFile.getParent();
				}
			else
				{
				throw new Exception(LanguageManagement.getString("errordirectory"));
				}
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR : While fetching a file : "+exc.getMessage());
			}
		}
	
	
	/************
	 * Method used to read a simple configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String> readFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			
			return xMLGear.getResultList(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("ERROR : The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+fileName+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read a configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readFileTab(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the "+param+" file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+param+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read an advanced configuration file
	 * @throws Exception 
	 */
	public static ArrayList<ArrayList<String[][]>> readExtFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTabExt(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+exc.getMessage());
			}
		}
	
	/**
	 * Used to return the file content regarding the data source (xml file or database file)
	 * @throws Exception 
	 */
	public static String getFlatFileContent(String fileName) throws Exception, FileNotFoundException
		{
		return xMLReader.fileRead(Variables.getMainDirectory()+"/"+fileName);
		}
	
	/**
	 * Method used to initialize the database from
	 * a collection file
	 * @throws 
	 */
	public static void initDatabase() throws Exception
		{
		Variables.setMigratedItemList(initMigratedItemList());
		Variables.setCliProfileList(initCliProfileList());
		Variables.setDeviceList(initDeviceList());
		Variables.setDidList(initDidList());
		Variables.setOfficeList(initOfficeList());
		}
	
	/**
	 * Method used to initialize the device list from
	 * the collection file
	 */
	public static ArrayList<BasicDevice> initDeviceList() throws Exception
		{
		try
			{
			Variables.getLogger().info("Initializing the device list from collection file");
			ArrayList<BasicDevice> deviceList = new ArrayList<BasicDevice>();
			ArrayList<String> params = new ArrayList<String>();
			params.add("devices");
			params.add("device");
			
			ArrayList<String[][]> content = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getDeviceListFileName()), params);
			
			for(String[][] s : content)
				{
				try
					{
					ItmType type = getITMType(UsefulMethod.getItemByName("type", s));
					BasicDevice d;
					
					if(type.equals(ItmType.ascom))
						{
						d = new BasicAscom(type,
								UsefulMethod.getItemByName("name", s),
								UsefulMethod.getItemByName("ip", s),
								UsefulMethod.getItemByName("mask", s),
								UsefulMethod.getItemByName("gateway", s),
								UsefulMethod.getItemByName("officeid", s),
								UsefulMethod.getItemByName("user", s),
								UsefulMethod.getItemByName("password", s),
								getCliProfile(UsefulMethod.getItemByName("cliprofile", s)),
								getProtocolType(UsefulMethod.getItemByName("protocol", s)),
								getAscomType(UsefulMethod.getItemByName("ascomtype", s)));
						}
					else
						{
						d = new BasicDevice(type,
								UsefulMethod.getItemByName("name", s),
								UsefulMethod.getItemByName("ip", s),
								UsefulMethod.getItemByName("mask", s),
								UsefulMethod.getItemByName("gateway", s),
								UsefulMethod.getItemByName("officeid", s),
								UsefulMethod.getItemByName("user", s),
								UsefulMethod.getItemByName("password", s),
								getCliProfile(UsefulMethod.getItemByName("cliprofile", s)),
								getProtocolType(UsefulMethod.getItemByName("protocol", s)));
						}
					
					/**
					 * We avoid duplicate
					 */
					boolean found = false;
					for(BasicDevice bd : deviceList)
						{
						if(bd.getIp().equals(d.getIp()))
							{
							Variables.getLogger().debug(bd.getInfo()+ " : Device ip duplicate found so we do not add the following new device : "+d.getInfo());
							found = true;
							break;
							}
						}
					
					if(!found)
						{
						Variables.getLogger().debug("New device added to the device list : "+d.getInfo());
						deviceList.add(d);
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following device : "+UsefulMethod.getItemByName("name", s)+" : "+e.getMessage());
					}
				}
			Variables.getLogger().debug(deviceList.size()+ " devices found in the database");
			return deviceList;
			}
		catch (Exception e)
			{
			throw new Exception("ERROR while initializing the device list : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Method used to initialize the DID list from
	 * the collection file
	 */
	public static ArrayList<Did> initDidList() throws Exception
		{
		try
			{
			Variables.getLogger().info("Initializing the did list from collection file");
			ArrayList<Did> didList = new ArrayList<Did>();
			ArrayList<String> params = new ArrayList<String>();
			params.add("dids");
			params.add("did");
			
			ArrayList<String[][]> content = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getDidListFileName()), params);
			
			for(String[][] s : content)
				{
				try
					{
					DidType didType = DidType.none; 
					String sType = UsefulMethod.getItemByName("type", s);
					String internalNumber = UsefulMethod.getItemByName("internalNumber", s);
					
					if(!internalNumber.isEmpty() && sType.isEmpty())
						{
						didType = DidType.lg;
						}
					else if(sType.toLowerCase().contains("analog"))
						{
						didType = DidType.analog;
						}
					else if(!internalNumber.isEmpty() && !sType.isEmpty())
						{
						didType = DidType.phone;
						}
					
					Did did = new Did(UsefulMethod.getItemByName("officeid", s),
							UsefulMethod.getItemByName("did", s),
							UsefulMethod.getItemByName("internalNumber", s),
							UsefulMethod.getItemByName("description", s),
							(UsefulMethod.getItemByName("toTest", s).isEmpty()?false:true),
							didType);
					
					/**
					 * We avoid duplicate
					 */
					boolean found = false;
					for(Did d : didList)
						{
						if(did.getDid().equals(d.getDid()))
							{
							Variables.getLogger().debug("DID duplicate found so we do not add the following new DID : "+d.getDid());
							found = true;
							break;
							}
						}
					
					if(!found)
						{
						Variables.getLogger().debug("New DID added to the DID list : "+did.getDid());
						didList.add(did);
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following DID : "+UsefulMethod.getItemByName("did", s)+" : "+e.getMessage());
					}
				}
			Variables.getLogger().debug(didList.size()+ " DID found in the database");
			return didList;
			}
		catch (Exception e)
			{
			throw new Exception("ERROR while initializing the DID list : "+e.getMessage(),e);
			}
		}
	
	/************
	 * Method used to initialize the office list from
	 * the collection file
	 * @throws Exception 
	 */
	public static ArrayList<BasicOffice> initOfficeList() throws Exception
		{
		try
			{
			Variables.getLogger().info("Initializing the office list from collection file");
			ArrayList<BasicOffice> officeList = new ArrayList<BasicOffice>();
			ArrayList<String> params = new ArrayList<String>();
			params.add("offices");
			params.add("office");
			ArrayList<String[][]> content = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getOfficeListFileName()), params);
			
			for(String[][] s : content)
				{
				String name = UsefulMethod.getItemByName("name", s);
				String coda = UsefulMethod.getItemByName("coda", s);
				ArrayList<Did> didList = new ArrayList<Did>();
				
				for(Did did : Variables.getDidList())
					{
					if(did.getOfficeid().equals(coda))didList.add(did);
					}
				
				try
					{
					BasicOffice o = new BasicOffice(coda,
							name,
							UsefulMethod.getItemByName("pole", s),
							UsefulMethod.getItemByName("dxi", s),
							UsefulMethod.getItemByName("devicepool", s),
							OfficeType.valueOf(UsefulMethod.getItemByName("officeType", s).toUpperCase()),
							CMG.valueOf(UsefulMethod.getItemByName("cmg", s).toUpperCase()),
							Lot.valueOf(UsefulMethod.getItemByName("lot",s).toUpperCase()),
							didList);
					
					/**
					 * We avoid duplicate
					 */
					boolean found = false;
					for(BasicOffice bo : officeList)
						{
						if(bo.getCoda().equals(o.getCoda()))
							{
							Variables.getLogger().debug(bo.getInfo()+ " : Office duplicate found so we do not add the following new office : "+o.getInfo());
							found = true;
							break;
							}
						}
					
					if(!found)
						{
						Variables.getLogger().debug("New office added to the office list : "+o.getInfo());
						officeList.add(o);
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following office : "+name+" : "+e.getMessage());
					}
				}
			Variables.getLogger().debug(officeList.size()+ " offices found in the database");
			return officeList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while initializing the office list : "+exc.getMessage(),exc);
			}
		}
	
	/**
	 * Used to initialize CliProfile list
	 * @throws Exception 
	 */
	public static ArrayList<CliProfile> initCliProfileList() throws Exception
		{	
		try
			{
			Variables.getLogger().info("Initializing the CliProfile list from collection file");
			ArrayList<CliProfile> cliProfileList = new ArrayList<CliProfile>();
			ArrayList<String> params = new ArrayList<String>();
			params.add("profiles");
			params.add("profile");
			ArrayList<ArrayList<String[][]>> content = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName()), params);
			
			for(ArrayList<String[][]> ast : content)
				{
				try
					{
					CliProfile cp = new CliProfile("", null, null, null, 50);
					
					for(int i=0; i<ast.size(); i++)
						{
						String[][] s = ast.get(i);
						if(i==0)//Misc
							{
							cp.setName(UsefulMethod.getItemByName("name", s));
							cp.setType(getITMType(UsefulMethod.getItemByName("type", s)));
							cp.setDefaultInterCommandTimer(Integer.parseInt(UsefulMethod.getItemByName("defaultintercommandtimer", s)));
							}
						else if(i==1)//How to authenticate
							{
							ArrayList<OneLine> l = new ArrayList<OneLine>();
							for(String[] t : s)
								{
								l.add(new OneLine(t[1], cliType.valueOf(t[0])));
								}
							cp.setHowToAuthenticate(l);
							}
						else if(i==2)//Config
							{
							ArrayList<OneLine> l = new ArrayList<OneLine>();
							for(String[] t : s)
								{
								l.add(new OneLine(t[1], cliType.valueOf(t[0])));
								}
							cp.setCliList(l);
							}
						}
					
					boolean found = false;
					for(CliProfile clip : cliProfileList)
						{
						if(clip.getName().equals(cp.getName()))
							{
							Variables.getLogger().debug(clip.getName()+ " : CliProfile duplicate found so we do not add the following new CliProfile : "+cp.getName());
							found = true;
							break;
							}
						}
					
					if(!found)
						{
						Variables.getLogger().debug("New CliProfile added to the CliProfile list : "+cp.getName());
						cliProfileList.add(cp);
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following CliProfile : "+UsefulMethod.getItemByName("name", ast.get(0))+" : "+e.getMessage(), e);
					}
				}
			
			Variables.getLogger().debug(cliProfileList.size()+ " cliProfiles found in the database");
			return cliProfileList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while initializing the CliProfile list : "+exc.getMessage(),exc);
			}
		}
	
	public static ArrayList<String> initMigratedItemList()
		{
		Variables.getLogger().info("Initializing the migrated item list from file");
		ArrayList<String> mil = new ArrayList<String>();
		ArrayList<String> params = new ArrayList<String>();
		params.add("items");
		
		try
			{
			ArrayList<String[][]> content = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getMigratedItemFileName()), params);
			
			for(String[] s : content.get(0))
				{
				mil.add(s[1]);
				}
			
			Variables.getLogger().debug("Migration item list reading, found "+mil.size()+" items");
			}
		catch (FileNotFoundException e)
			{
			Variables.getLogger().error("The migration item file '"+Variables.getMigratedItemFileName()+"' was not found");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while reading the migrated item file : "+e.getMessage(),e);
			}
		
		return mil;
		}
	
	
	/******
	 * Method used to initialize the AXL Connection to the CUCM
	 */
	public static void initAXLConnectionToCUCM(CUCM cucm) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(cucm.getVersion().equals(CucmVersion.version85))
				{
				throw new Exception("AXL unsupported version");
				}
			else if(cucm.getVersion().equals(CucmVersion.version105))
				{
				com.cisco.axlapiservice10.AXLAPIService axlService = new com.cisco.axlapiservice10.AXLAPIService();
				com.cisco.axlapiservice10.AXLPort axlPort = axlService.getAXLPort();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+cucm.getIp()+":"+cucm.getPort()+"/axl/";
				
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, cucm.getUser());
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, cucm.getPassword());
				
				cucm.setAXLConnectionV105(axlPort);
				}
			
			Variables.getLogger().debug("AXL WSDL Initialization done");
			
			/**
			 * We now check if the CUCM is reachable by asking him its version
			 */
			Variables.getLogger().debug("CUCM version : "+SimpleRequest.getCUCMVersion(cucm));
			cucm.setReachable(true);
			}
		catch (Exception e)
			{
			Variables.getLogger().error(cucm.getIp()+" : Error while initializing AXL CUCM connection : "+e.getMessage(),e);
			cucm.setReachable(false);
			throw e;
			}
		}
	
	/******
	 * Method used to initialize the RIS Connection to the CUCM
	 */
	public static void initRISConnectionToCUCM(CUCM cucm) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(Variables.getCUCMVersion().equals(CucmVersion.version85))
				{
				throw new Exception("RIS unsupported version");
				}
			else if(Variables.getCUCMVersion().equals(CucmVersion.version105))
				{
				RISService70 ris = new RISService70();
				RisPortType risPort = ris.getRisPort70();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+cucm.getIp()+":"+cucm.getPort()+"/realtimeservice2/services/RISService70";
				
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, cucm.getUser());
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, cucm.getPassword());
				
				cucm.setRISConnection(risPort);
				}
			
			/**
			 * Looking for a fake phone to test the RIS connection
			 */
			Variables.getLogger().debug("Looking for a fake phone to test the RIS connection");
			ArrayList<BasicPhone> phoneList = new ArrayList<BasicPhone>();
			phoneList.add(new BasicPhone("SEP1234567890EF", "Test", "7961"));
			RisportTools.doPhoneSurvey(cucm, phoneList);
			
			Variables.getLogger().debug("RIS WSDL Initialization done");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(cucm.getIp()+" : Error while initializing RIS CUCM connection : "+e.getMessage(),e);
			cucm.setReachable(false);
			throw e;
			}
		}
	
	/**
	 * Method used when the application failed to 
	 * initialize
	 */
	public static void failedToInit(Exception exc)
		{
		Variables.getLogger().error(exc.getMessage(),exc);
		Variables.getLogger().error("Application failed to init : System.exit(0)");
		System.exit(0);
		}
	
	/**
	 * Initialization of the internal variables from
	 * what we read in the configuration file
	 * @throws Exception 
	 */
	public static void initInternalVariables() throws Exception
		{
		/***********
		 * Logger
		 */
		String level = UsefulMethod.getTargetOption("log4j");
		if(level.compareTo("DEBUG")==0)
			{
			Variables.getLogger().setLevel(Level.DEBUG);
			}
		else if (level.compareTo("INFO")==0)
			{
			Variables.getLogger().setLevel(Level.INFO);
			}
		else if (level.compareTo("ERROR")==0)
			{
			Variables.getLogger().setLevel(Level.ERROR);
			}
		else
			{
			//Default level is INFO
			Variables.getLogger().setLevel(Level.INFO);
			}
		Variables.getLogger().info("Log level found in the configuration file : "+Variables.getLogger().getLevel().toString());
		/*************/
		
		/************
		 * Etc...
		 */
		//If needed, just write it here
		/*************/
		}
	
	/**
	 * Method which convert a string into cucmAXLVersion
	 */
	public static CucmVersion convertStringToCUCMAXLVersion(String version)
		{
		if(version.contains("80"))
			{
			return CucmVersion.version80;
			}
		else if(version.contains("85"))
			{
			return CucmVersion.version85;
			}
		else if(version.contains("105"))
			{
			return CucmVersion.version105;
			}
		else if(version.contains("115"))
			{
			return CucmVersion.version115;
			}
		else if(version.contains("125"))
			{
			return CucmVersion.version125;
			}
		else
			{
			//Default : 10.5
			return CucmVersion.version105;
			}
		}
	
	
	/**************
	 * Method aims to get a template item value by giving its name
	 * @throws Exception 
	 *************/
	public static String getItemByName(String name, String[][] itemDetails) throws Exception
		{
		for(int i=0; i<itemDetails.length; i++)
			{
			if(itemDetails[i][0].equals(name))
				{
				return itemDetails[i][1];
				}
			}
		//throw new Exception("Item not found : "+name);
		Variables.getLogger().debug("Item not found : "+name);
		return "";
		}
	
	/**********************************************************
	 * Method used to disable security in order to accept any
	 * certificate without trusting it
	 */
	public static void disableSecurity()
		{
		try
        	{
            X509TrustManager xtm = new HttpsTrustManager();
            TrustManager[] mytm = { xtm };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, mytm, null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            	{
                public boolean verify(String hostname, SSLSession session)
                	{
                    return true;
                	}
            	}
            );
        	}
        catch (Exception e)
        	{
            e.printStackTrace();
        	}
		}
	
	/**
	 * Method used to convert a long network mask into a short one
	 * 
	 * For instance :
	 * Convert 255.255.255.0 into 24
	 * Convert 255.255.240.0 into 20
	 */
	public static String convertlongMaskToShortOne(String mask)
		{
		//We check if mask is a long or a CIDR one
		if(!InetAddressValidator.getInstance().isValidInet4Address(mask))return mask;
		
		SubnetUtils subnet = new SubnetUtils("192.168.1.0", mask.trim());
		return subnet.getInfo().getCidrSignature().split("/")[1];
		}
	
	/**
	 * Method used to convert a short network mask into a long one
	 * 
	 * For instance :
	 * Convert 24 into 255.255.255.0
	 * Convert 20 into 255.255.240.0
	 */
	public static String convertShortMaskToLongOne(String mask)
		{
		//We check if mask is a long or a CIDR one
		if(mask.length()>2)return mask;
		
		SubnetUtils subnet = new SubnetUtils("192.168.1.0/"+mask);
		return subnet.getInfo().getNetmask();
		}
	
	/**********
	 * Method used to convert a value from the collection file
	 * into a value expected by the CUCM using the substitute list
	 */
	public static String findSubstitute(SubstituteType type, String toConvert) throws Exception
		{
		for(ValueMatcher vm : Variables.getSubstituteList())
			{
			if(vm.getType().equals(type))
				{
				if(vm.getCollectionName().equals(toConvert))
					{
					Variables.getLogger().debug(toConvert+" converted into : "+vm.getConvertName());
					return vm.getConvertName();
					}
				}
			}
		throw new Exception("Impossible to convert \""+toConvert+"\"");
		}
	
	
	/***************
	 * Method used to convert a profile name from the collection file
	 * into a profile name expected by the CSV
	 * @throws Exception 
	 */
	public static synchronized String findCodecBandwidth(String codec) throws Exception
		{
		if(codec.equals("G.711"))
			{
			return "64";
			}
		else if(codec.equals("G.729"))
			{
			return "10";
			}
		
		throw new Exception("Profile "+codec+" not found");
		}
	
	
	/********************************************
	 * Method used to init the class eMailsender
	 * @throws Exception 
	 ********************************************/
	public synchronized static void initEMailServer() throws Exception
		{
		Variables.seteMSender(new eMailSender(UsefulMethod.getTargetOption("smtpemailport"),
				 UsefulMethod.getTargetOption("smtpemailprotocol"),
				 UsefulMethod.getTargetOption("smtpemailserver"),
				 UsefulMethod.getTargetOption("smtpemail"),
				 UsefulMethod.getTargetOption("smtpemailpassword")));
		}
	
	/**
	 * Method used to send an email to the admin group
	 */
	public synchronized static void sendEmailToTheAdminList(String subject, String content)
		{
		try
			{
			Variables.getLogger().debug("Sending emails to the admin group");
			String adminEmails = UsefulMethod.getTargetOption("smtpemailreceiver");
			String eMailDesc = "Multiple email sending";
			
			String[] adminList = adminEmails.split(";");
			for(String s : adminList)
				{
				Variables.getLogger().debug("Sending to "+s);
				try
					{
					Variables.geteMSender().send(s,
							subject,
							content,
							eMailDesc);
					Variables.getLogger().debug("Email sent for "+s);
					}
				catch (Exception e)
					{
					Variables.getLogger().debug("Failed to send email to "+s+" : "+e.getMessage());
					}
				}
			
			Variables.getLogger().debug("Finish to send emails to the admin group");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to send emails to the admin list : "+e.getMessage(),e);
			}
		}
	
	
	/**
	 * Method used to find the file name from a file path
	 * For intance :
	 * C:\JAVAELILEX\YUZA\Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 * gives :
	 * Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 */
	public static String extractFileName(String fullFilePath)
		{
		String[] tab =  fullFilePath.split("\\\\");
		return tab[tab.length-1];
		}
	
	/***
	 * Method used to get the AXL version from the CUCM
	 * We contact the CUCM using a very basic request and therefore get the version
	 * @throws Exception 
	 */
	public static CucmVersion getAXLVersionFromTheCUCM() throws Exception
		{
		/**
		 * In this method version we just read the version from the configuration file
		 * This has to be improved to match the method description
		 **/
		CucmVersion AXLVersion;
		
		AXLVersion = UsefulMethod.convertStringToCUCMAXLVersion("version"+getTargetOption("axlversion"));
		
		return AXLVersion;
		}
	
	/**
	 * Used to init the substitute list
	 * @throws Exception 
	 */
	public static ArrayList<ValueMatcher> initSubstituteList(ArrayList<String[][]> list) throws Exception
		{
		ArrayList<ValueMatcher> substituteList = new ArrayList<ValueMatcher>();
		
		for(String[][] tabS : list)
			{
			substituteList.add(new ValueMatcher(
					UsefulMethod.getItemByName("match", tabS),
					UsefulMethod.getItemByName("replaceby", tabS),
					UsefulMethod.convertStringToSubstituteType(UsefulMethod.getItemByName("type", tabS))));
			}
		
		return substituteList;
		}
	
	
	/**
	 * Used to convert a String into SubstituteType
	 * @throws Exception 
	 */
	public static SubstituteType convertStringToSubstituteType(String type) throws Exception
		{
		if(type.equals("phone"))
			{
			return SubstituteType.phone;
			}
		else if(type.equals("pbt"))
			{
			return SubstituteType.pbt;
			}
		else if(type.equals("css"))
			{
			return SubstituteType.css;
			}
		else if(type.equals("profile"))
			{
			return SubstituteType.profile;
			}
		else if(type.equals("misc"))
			{
			return SubstituteType.misc;
			}
		else
			{
			throw new Exception("Substitute type not found");
			}
		}
	
	/*****
	 * Method used to find the substitute corresponding to 
	 * the provided value
	 * @throws Exception 
	 */
	public static String findSubstitute(String s, SubstituteType type) throws Exception
		{
		for(ValueMatcher vm : Variables.getSubstituteList())
			{
			if(vm.getType().equals(type))
				{
				if(vm.getCollectionName().equals(s))
					{
					return vm.getConvertName();
					}
				}
			}
		
		Variables.getLogger().debug("No substitute of type \""+type.name()+"\" have been found for the string : "+s);
		return s;
		}
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(String s)
		{
		if((s == null) || (s.equals("")))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(ArrayList<String> as)
		{
		if((as == null) || (as.size() == 0))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	/******
	 * Used to convert itemType values into more verbose ones
	 */
	public static String convertItemTypeToVerbose(ItemType type)
		{
		switch(type)
			{
			case location:return "Location";
			case region:return "Region";
			case srstreference:return "SRST Reference";
			case devicepool:return "Device Pool";
			case commondeviceconfig:return "Common Device Configuration";
			case conferencebridge:return "Conference Bridge";
			case mediaresourcegroup:return "Media Resource Group";
			case mediaresourcegrouplist:return "Media Resource Group List";
			case partition:return "Partition";
			case callingsearchspace:return "Calling Search Space";
			case trunksip:return "SIP Trunk";
			case vg:return "Voice Gateway";
			case routegroup:return "Route Group";
			case translationpattern:return "Translation Pattern";
			case callingpartytransformationpattern:return "Calling Party Transformation Pattern";
			case calledpartytransformationpattern:return "Called Party Transformation Pattern";
			case physicallocation:return "Physical Location";
			case devicemobilityinfo:return "Device Mobility Info";
			case devicemobilitygroup:return "Device Mobility group";
			case datetimesetting:return "Date Time Settings";
			case callmanagergroup:return "Call Manager group";
			case phone:return "Phone";
			case udp:return "User device profile";
			case user:return "End User";
			case line:return "Line";
			case voicemail:return "Voicemail";
			case telecasterservice:return "Phone Service";
			case siptrunksecurityprofile:return "Sip Trunk Security Profile";
			case sipprofile:return "SIP profile";
			case phonetemplatename:return "Phone Button Template";
			case linegroup:return "Line Group";
			case huntlist:return "Hunt List";
			case huntpilot:return "Hunt Pilot";
			case callpickupgroup:return "Call Pickup Group";
			case udplogin:return "UDP Login";
			case aargroup:return "AAR Group";
			case usercontrolgroup:return "Access Control Group";
			case analog:return "Analog Port";
			case gateway:return "Gateway";
			default:return type.name();
			}
		}
	
	/******
	 * Method used to determine if the fault description means
	 * that the item was not found or something else
	 * If it is not found we return true
	 * For any other reason we return false
	 * @param faultDesc
	 * @return
	 */
	public static boolean itemNotFoundInTheCUCM(String faultDesc)
		{
		ArrayList<String> faultDescList = new ArrayList<String>();
		faultDescList.add("was not found");
		for(String s : faultDescList)
			{
			if(faultDesc.contains(s))return true;
			}
		
		return false;
		}
	
	public static ItmType findDeviceType(String type)
		{
		if(type.contains("ISR"))return ItmType.isr;
		else if(type.contains("VG"))return ItmType.vg;
		else if(type.contains("audiocode"))return ItmType.audiocode;
		else if(type.contains("ascom"))return ItmType.ascom;
		else return ItmType.sip;
		}
	
	/**
	 * Used to check if the IP is in the subnet
	 */
	public static boolean isIPIncludedInThisSubnet(IPRange range, String ip)
		{
		try
			{
			SubnetInfo subnet = new SubnetUtils(range.getCIDRFormat()).getInfo();
			return subnet.isInRange(ip);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			}
		return false;
		}
	
	/**
	 * To make a user authenticate by the CUCM 
	 */
	public static boolean doAuthenticate(CUCM cucm, String userID, String password)
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			try
				{
				com.cisco.axl.api._10.DoAuthenticateUserReq req = new com.cisco.axl.api._10.DoAuthenticateUserReq();
				
				req.setUserid(userID);
				req.setPassword(password);
				
				com.cisco.axl.api._10.DoAuthenticateUserRes resp = cucm.getAXLConnectionV105().doAuthenticateUser(req);
				
				return Boolean.parseBoolean(resp.getReturn().getUserAuthenticated());
				}
			catch (Exception e)
				{
				Variables.getLogger().error(cucm.getInfo()+" : ERROR while authenticating user "+userID+" : "+e.getMessage(),e);
				}
			}
		else
			{
			Variables.getLogger().debug(cucm.getInfo()+" : Unsupported AXL version");
			}
		
		return false;
		}
	
	public static ItmType getITMType(String type) throws Exception
		{
		for(ItmType t : ItmType.values())
			{
			if(type.toLowerCase().replaceAll(" ", "").contains(t.name()))return t;
			}
		
		throw new Exception("No itmType found for type : "+type);
		}
	
	public static AscomType getAscomType(String type) throws Exception
		{
		for(AscomType at : AscomType.values())
			{
			if(type.toLowerCase().replaceAll(" ", "").equals(at.name()))return at;
			}
		
		throw new Exception("No ascomType found for type : "+type);
		}
	
	public static cliProtocol getProtocolType(String protocol) throws Exception
		{
		for(cliProtocol p : cliProtocol.values())
			{
			if(protocol.toLowerCase().replaceAll(" ", "").equals(p.name()))return p;
			}
		
		throw new Exception("No cliProtocol found for protocol : "+protocol);
		}
	
	/*****
	 * Return a cli profile looking by the profile name then the type
	 */
	public static CliProfile getCliProfile(String s) throws Exception
		{
		for(CliProfile clip : Variables.getCliProfileList())
			{
			if(s.toLowerCase().replaceAll(" ", "").equals(clip.getName().toLowerCase()))return clip;
			}
		
		//If we didn't find the pofile by name, we look for the profile type
		for(CliProfile clip : Variables.getCliProfileList())
			{
			if(s.toLowerCase().replaceAll(" ", "").equals(clip.getType().name().toLowerCase()))return clip;
			}
		
		//throw new Exception("No CliProfile found for value : "+s);
		return null;
		}
	
	
	/**
	 * used to add a new entry to the migrated item list
	 */
	public static void addEntryToTheMigratedList(String id)
		{
		Variables.getLogger().debug("Migrated item file : adding "+id);
		
		try
			{
			//We avoid duplicate
			if(!Variables.getMigratedItemList().contains(id))
				{
				Variables.getMigratedItemList().add(id);
				
				updateMigratedList();
				}
			
			/**
			 * We also update real time status of simple items
			 */
			boolean found = false;
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getId().equals(id))
					{
					o.setStatus(basicItemStatus.migrated);
					found = true;
					break;
					}
				}
			if(!found)
				{
				for(BasicDevice d : Variables.getDeviceList())
					{
					if(d.getId().equals(id))
						{
						d.setStatus(basicItemStatus.migrated);
						break;
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add the entry "+id+" to the migrated list : "+e.getMessage());
			}
		}
	
	/**
	 * used to add a new entry to the migrated item list
	 */
	public static void removeEntryToTheMigratedList(String id)
		{
		Variables.getLogger().debug("Migrated item file : removing "+id);
		try
			{
			if(Variables.getMigratedItemList().contains(id))
				{
				Variables.getMigratedItemList().remove(id);
				
				updateMigratedList();
				}
			
			/**
			 * We also update real time status of simple items
			 */
			boolean found = false;
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getId().equals(id))
					{
					o.setStatus(basicItemStatus.tomigrate);
					found = true;
					break;
					}
				}
			if(!found)
				{
				for(BasicDevice d : Variables.getDeviceList())
					{
					if(d.getId().equals(id))
						{
						d.setStatus(basicItemStatus.tomigrate);
						break;
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to remove the entry "+id+" of the migrated list : "+e.getMessage());
			}
		}
	
	private static void updateMigratedList()
		{
		try
			{
			BufferedWriter buf = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+Variables.getMigratedItemFileName()),false));
			StringBuffer sb = new StringBuffer("");
			
			sb.append("<xml>\r\n");
			sb.append("	<items>\r\n");
			
			for(String s : Variables.getMigratedItemList())
				{
				sb.append("		<item>"+s+"</item>\r\n");
				}
			
			sb.append("	</items>\r\n");
			sb.append("</xml>\r\n");
			
			buf.write(sb.toString());
			buf.flush();
			buf.close();
			
			Variables.getLogger().debug("Migrated item file has been updated");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR wile updating the migrated item file : "+e.getMessage(),e);
			}
		}
	
	/**
	 * used to get an office from the office list
	 */
	public static BasicOffice getOffice(String officeID)
		{
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getCoda().equals(officeID))return o;
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while looking for the office '"+officeID+"' : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Convert status to verbose String status
	 */
	public static String convertStatusToVerboseString(String status)
		{
		try
			{
			for(StatusType s : StatusType.values())
				{
				if(status.toLowerCase().equals(s.name().toLowerCase()))
					{
					return LanguageManagement.getString(s.name());
					}
				}
			
			for(basicItemStatus s : basicItemStatus.values())
				{
				if(status.toLowerCase().equals(s.name().toLowerCase()))
					{
					return LanguageManagement.getString(s.name());
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Unable to find a verbose equivalent for status : "+status);
			}
		
		return status;
		}
	
	/**
	 * Used to initialize the CUCM AXL and RIS Connection
	 * @throws Exception
	 */
	public static void initCUCM() throws Exception
		{
		Variables.getLogger().debug("Initializing CUCM");
		/**
		 * Source CUCM
		 */
		CUCM srccucm = new CUCM(UsefulMethod.getTargetOption("srccucmip"),
				UsefulMethod.getTargetOption("port"),
				UsefulMethod.getTargetOption("username"),
				UsefulMethod.getTargetOption("password"),
				UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("version")));
		
		Variables.getLogger().debug("Initializing source CUCM : "+srccucm.getInfo());
		
		initAXLConnectionToCUCM(srccucm);
		initRISConnectionToCUCM(srccucm);
		Variables.getLogger().debug("Source CUCM done !");
		Variables.setSrccucm(srccucm);
		
		/**
		 * Destination CUCM
		 */
		CUCM dstcucm = new CUCM(UsefulMethod.getTargetOption("dstcucmip"),
				UsefulMethod.getTargetOption("port"),
				UsefulMethod.getTargetOption("username"),
				UsefulMethod.getTargetOption("password"),
				UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("version")));
		
		Variables.getLogger().debug("Initializing destination CUCM : "+dstcucm.getInfo());
		
		initAXLConnectionToCUCM(dstcucm);
		initRISConnectionToCUCM(dstcucm);
		Variables.getLogger().debug("Destination CUCM done !");
		Variables.setDstcucm(dstcucm);
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

