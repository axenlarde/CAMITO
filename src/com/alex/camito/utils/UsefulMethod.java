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

import com.alex.camito.cli.CliGetOutput;
import com.alex.camito.cli.CliProfile;
import com.alex.camito.cli.CliProfile.CliProtocol;
import com.alex.camito.cli.OneLine;
import com.alex.camito.cli.OneLine.cliType;
import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.Device;
import com.alex.camito.device.DeviceType;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.Did.DidType;
import com.alex.camito.misc.IPRange;
import com.alex.camito.misc.SimpleItem.basicItemStatus;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.misc.ValueMatcher;
import com.alex.camito.office.misc.BasicOffice;
import com.alex.camito.office.misc.CMG;
import com.alex.camito.office.misc.LinkedOffice;
import com.alex.camito.office.misc.Office;
import com.alex.camito.office.misc.CMG.CMGName;
import com.alex.camito.risport.RisportTools;
import com.alex.camito.utils.Variables.AscomType;
import com.alex.camito.utils.Variables.CucmVersion;
import com.alex.camito.utils.Variables.ItemType;
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
		Variables.setDeviceTypeList(initDeviceTypeList());
		Variables.setCliProfileList(initCliProfileList());
		Variables.setDeviceList(initDeviceList());
		Variables.setDidList(initDidList());
		Variables.setCmgList(initCmgList());
		Variables.setOfficeList(initOfficeList());
		}
	
	/************
	 * Method used to initialize the Device Type list from
	 * the xml file
	 */
	public static ArrayList<DeviceType> initDeviceTypeList() throws Exception
		{
		ArrayList<String> listParams = new ArrayList<String>();
		ArrayList<String[][]> result;
		ArrayList<ArrayList<String[][]>> extendedList;
		ArrayList<DeviceType> deviceTypeList = new ArrayList<DeviceType>();
		
		Variables.getLogger().info("Initializing the Device Type list from collection file");
		
		listParams.add("devices");
		listParams.add("device");
		result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getDeviceTypeListFileName()), listParams);
		extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getDeviceTypeListFileName()), listParams);
		
		for(int i=0; i<result.size(); i++)
			{
			try
				{
				String[][] tab = result.get(i);
				
				/**
				 * First we check for duplicates
				 */
				String DeviceTypeName = UsefulMethod.getItemByName("name", tab).toUpperCase();
				boolean found = false;
				for(DeviceType dt : deviceTypeList)
					{
					if(dt.getName().equals(DeviceTypeName))
						{
						Variables.getLogger().debug("Duplicate found, do not adding the following device type : "+DeviceTypeName);
						found = true;
						break;
						}
					}
				if(found)continue;
				
				ArrayList<String[][]> tabE = extendedList.get(i);
				
				ArrayList<OneLine> howToConnect = new ArrayList<OneLine>();
				ArrayList<OneLine> howToSave = new ArrayList<OneLine>();
				ArrayList<OneLine> howToReboot = new ArrayList<OneLine>();
				
				for(int j=0; j<tab.length; j++)
					{
					if(tab[j][0].equals("howtoconnect"))
						{
						for(String[] s : tabE.get(j))
							{
							howToConnect.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tab[j][0].equals("howtosave"))
						{
						for(String[] s : tabE.get(j))
							{
							howToSave.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					else if(tab[j][0].equals("howtoreboot"))
						{
						for(String[] s : tabE.get(j))
							{
							howToReboot.add(new OneLine(s[1],cliType.valueOf(s[0])));
							}
						}
					}
				
				deviceTypeList.add(new DeviceType(DeviceTypeName,
						UsefulMethod.getItemByName("vendor", tab),
						howToConnect,
						howToSave,
						howToReboot));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("Failed to load a new Device Type : "+e.getMessage(), e);
				}
			}
		
		Variables.getLogger().debug(deviceTypeList.size()+" device type found");
		Variables.getLogger().debug("Device Type list initialization done");
		return deviceTypeList;
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
			boolean allowDuplicate = Boolean.parseBoolean(UsefulMethod.getTargetOption("allowdeviceipduplicate"));
			
			for(String[][] s : content)
				{
				String ip = UsefulMethod.getItemByName("ip", s);
				String name = UsefulMethod.getItemByName("name", s);
				
				/**
				 * We avoid duplicate
				 */
				if(allowDuplicate)
					{
					Variables.getLogger().debug("We allow device ip duplicate so we do not look for duplicates");
					}
				else
					{
					boolean found = false;
					for(BasicDevice bd : deviceList)
						{
						if(bd.getIp().equals(ip))
							{
							Variables.getLogger().debug(bd.getInfo()+ " : Device ip duplicate found so we do not add the following new device : "+name);
							found = true;
							break;
							}
						}
					
					if(found)continue;
					}
				
				/**
				 * To allow to resolve the OneLine contained in each device type
				 * we create a copy of it for each device
				 */
				ArrayList<OneLine> howToConnect = new ArrayList<OneLine>();
				ArrayList<OneLine> howToSave = new ArrayList<OneLine>();
				ArrayList<OneLine> howToReboot = new ArrayList<OneLine>();
				
				DeviceType dt = UsefulMethod.getDeviceType(UsefulMethod.getItemByName("type",s));
				
				for(OneLine ol : dt.getHowToConnect())howToConnect.add(new OneLine(ol.getCommand(), ol.getType()));
				for(OneLine ol : dt.getHowToSave())howToSave.add(new OneLine(ol.getCommand(), ol.getType()));
				for(OneLine ol : dt.getHowToReboot())howToReboot.add(new OneLine(ol.getCommand(), ol.getType()));
				
				DeviceType dType = new DeviceType(dt.getName(), dt.getVendor(), howToConnect, howToSave, howToReboot);
				
				try
					{
					BasicDevice	d = new BasicDevice(name,
								ip,
								UsefulMethod.getItemByName("mask", s),
								UsefulMethod.getItemByName("gateway", s),
								UsefulMethod.getItemByName("officeid", s),
								UsefulMethod.getItemByName("user", s),
								UsefulMethod.getItemByName("password", s),
								UsefulMethod.getCliProfile(UsefulMethod.getItemByName("cliprofile", s)),
								UsefulMethod.getCliProfile(UsefulMethod.getItemByName("rollbackcliprofile", s)),
								getProtocolType(UsefulMethod.getItemByName("protocol", s)),
								dType);
					
					Variables.getLogger().debug("New device added to the device list : "+d.getInfo());
					deviceList.add(d);
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following device : "+ip+" : "+e.getMessage());
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
				String didS = UsefulMethod.getItemByName("dn", s);
				
				/**
				 * We avoid duplicate
				 */
				boolean found = false;
				for(Did d : didList)
					{
					if(d.getDid().equals(didS))
						{
						Variables.getLogger().debug("DID duplicate found so we do not add the following new DID : "+didS);
						found = true;
						break;
						}
					}
				
				if(found)continue;
				
				try
					{
					DidType didType = DidType.none; 
					String sType = UsefulMethod.getItemByName("type", s);
					String internalNumber = UsefulMethod.getItemByName("internal", s);
					
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
					
					Did did = new Did(UsefulMethod.getItemByName("coda", s),
							didS,
							internalNumber,
							UsefulMethod.getItemByName("description", s),
							(UsefulMethod.getItemByName("totest", s).isEmpty()?false:true),
							didType);
					
					//Variables.getLogger().debug("New DID added to the DID list : "+did.getDid());
					didList.add(did);
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
	
	/**
	 * Method used to initialize the CMG list from
	 * the collection file
	 */
	public static ArrayList<CMG> initCmgList() throws Exception
		{
		try
			{
			Variables.getLogger().info("Initializing the CMG list from collection file");
			ArrayList<CMG> cmgList = new ArrayList<CMG>();
			ArrayList<String> params = new ArrayList<String>();
			params.add("cmgs");
			params.add("cmg");
			
			ArrayList<String[][]> content = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getCmgListFileName()), params);
			
			for(String[][] s : content)
				{
				try
					{
					CMG cmg = new CMG(CMGName.valueOf(UsefulMethod.getItemByName("name",s).toUpperCase()),
							UsefulMethod.getItemByName("cucm1",s),
							UsefulMethod.getItemByName("cucm2",s),
							UsefulMethod.getItemByName("cucm3",s),
							UsefulMethod.getItemByName("cucm4",s));
					
					/**
					 * We avoid duplicate
					 */
					boolean found = false;
					for(CMG c : cmgList)
						{
						if(c.getName().equals(cmg.getName()))
							{
							Variables.getLogger().debug("CMG duplicate found so we do not add the following new CMG : "+c.getName());
							found = true;
							break;
							}
						}
					
					if(!found)
						{
						Variables.getLogger().debug("New CMG added to the CMG list : "+cmg.getName());
						cmgList.add(cmg);
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following CMG : "+UsefulMethod.getItemByName("name", s)+" : "+e.getMessage());
					}
				}
			Variables.getLogger().debug(cmgList.size()+ " CMG found in the database");
			return cmgList;
			}
		catch (Exception e)
			{
			throw new Exception("ERROR while initializing the CMG list : "+e.getMessage(),e);
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
				String coda = UsefulMethod.getItemByName("coda", s);
				String name = UsefulMethod.getItemByName("name", s);
				String devicePool = UsefulMethod.getItemByName("devicepool", s).toUpperCase();
				String pole = UsefulMethod.getItemByName("pole", s);
				OfficeType type = OfficeType.valueOf(UsefulMethod.getItemByName("type", s).toUpperCase());
				
				/**
				 * We avoid duplicate
				 */
				boolean found = false;
				for(BasicOffice bo : officeList)
					{
					if(bo.getCoda().equals(coda))
						{
						Variables.getLogger().debug(bo.getInfo()+ " : Office duplicate found so we do not add the following new office : "+coda+" "+name);
						found = true;
						break;
						}
					else if(bo.getDevicepool().equals(devicePool))
						{
						/*****
						 * In this case there is already an office with the same devicePool
						 * So we create it as a linked office
						 */
						LinkedOffice linkedOffice = new LinkedOffice(coda,
								name,
								pole,
								type);
						
						bo.addLinkedOffice(linkedOffice);
						found = true;
						break;
						}
					}
				
				if(found)continue;
				
				ArrayList<Did> didList = new ArrayList<Did>();
				ArrayList<BasicDevice> deviceList = new ArrayList<BasicDevice>();
				
				for(BasicDevice d : Variables.getDeviceList())
					{
					if(d.getOfficeid().equals(devicePool))
						{
						d.setOfficename(name);
						deviceList.add(d);
						}
					}
				
				for(Did did : Variables.getDidList())
					{
					if(did.getOfficeid().equals(coda))didList.add(did);
					}
				
				try
					{
					BasicOffice o = new BasicOffice(coda,
							name,
							pole,
							UsefulMethod.getItemByName("dxi", s),
							devicePool,
							type,
							UsefulMethod.getCMG((UsefulMethod.getItemByName("cmg", s).toUpperCase())),
							Lot.valueOf(UsefulMethod.getItemByName("lot",s).toUpperCase()),
							didList,
							deviceList);
					
					Variables.getLogger().debug("New office added to the office list : "+o.getInfo());
					officeList.add(o);
					}
				catch (Exception e)
					{
					Variables.getLogger().error("Could not add the following office : "+coda+" "+name+" : "+e.getMessage());
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
			ArrayList<String> listParams = new ArrayList<String>();
			listParams.add("profiles");
			listParams.add("profile");
			ArrayList<String[][]> result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName()), listParams);
			ArrayList<ArrayList<String[][]>> extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getCliProfileListFileName()), listParams);
			
			for(int i=0; i<result.size(); i++)
				{
				try
					{
					String[][] tab = result.get(i);
					ArrayList<String[][]> tabE = extendedList.get(i);
					ArrayList<OneLine> cliList = new ArrayList<OneLine>();
					DeviceType deviceType = null;
					
					/**
					 * First we check for duplicates
					 */
					String cpName = UsefulMethod.getItemByName("name", tab);
					boolean found = false;
					for(CliProfile cp : cliProfileList)
						{
						if(cp.getName().equals(cpName))
							{
							Variables.getLogger().debug("Duplicate found, do not adding the CliProfile : "+cpName);
							found = true;
							break;
							}
						}
					if(found)continue;
					
					for(DeviceType dt : Variables.getDeviceTypeList())
						{
						if(dt.getName().equals(UsefulMethod.getItemByName("type", tab)))
							{
							deviceType = dt;
							break;
							}
						}
					
					for(int j=0; j<tab.length; j++)
						{
						if(tab[j][0].equals("config"))
							{
							for(String[] s : tabE.get(j))
								{
								cliList.add(new OneLine(s[1],cliType.valueOf(s[0])));
								}
							}
						}
					
					cliProfileList.add(new CliProfile(UsefulMethod.getItemByName("name", tab),
							deviceType,
							cliList,
							Integer.parseInt(UsefulMethod.getItemByName("defaultintercommandtimer", tab))));
					
					
					}
				catch (Exception e)
					{
					Variables.getLogger().error("ERROR while initializing a new cliProfile : "+e.getMessage(), e);
					}
				}
			
			Variables.getLogger().debug(cliProfileList.size()+ " cliProfiles found in the database");
			Variables.getLogger().debug("CliProfiles list initialization done");
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
			
			if(cucm.getVersion().equals(CucmVersion.version85))
				{
				throw new Exception("RIS unsupported version");
				}
			else if(cucm.getVersion().equals(CucmVersion.version105))
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
		/*
		if((s == null) || (s.equals("")))
			{
			return false;
			}
		else
			{
			return true;
			}
		*/
		if(s == null)//To test
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
	/*
	public static TypeName findDeviceType(String type)
		{
		if(type.contains("ISR"))return TypeName.isr;
		else if(type.contains("VG"))return TypeName.vg;
		else if(type.contains("audiocode"))return TypeName.audiocode;
		else if(type.contains("ascom"))return TypeName.ascom;
		else return TypeName.sip;
		}*/
	
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
	
	public static AscomType getAscomType(String type) throws Exception
		{
		for(AscomType at : AscomType.values())
			{
			if(type.toLowerCase().replaceAll(" ", "").equals(at.name()))return at;
			}
		
		throw new Exception("No ascomType found for type : "+type);
		}
	
	public static CliProtocol getProtocolType(String protocol) throws Exception
		{
		for(CliProtocol p : CliProtocol.values())
			{
			if(protocol.toLowerCase().replaceAll(" ", "").contains(p.name().toLowerCase()))return p;
			}
		
		//throw new Exception("No cliProtocol found for protocol : "+protocol);
		return null;
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
		
		return null;//Important;
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
	public static void addEntryToTheMigratedList(ArrayList<String> idList)
		{
		try
			{
			//We avoid duplicate
			for(String id : idList)
				{
				if(!Variables.getMigratedItemList().contains(id))
					{
					Variables.getLogger().debug("Migrated item file : adding "+id);
					Variables.getMigratedItemList().add(id);
					}
				}
			
			updateMigratedList();//We then rewrite the file
			
			/**
			 * We also update real time status of simple items
			 */
			for(String id : idList)
				{
				for(BasicOffice o : Variables.getOfficeList())
					{
					if(o.getId().equals(id))
						{
						o.setStatus(basicItemStatus.migrated);
						break;
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add the entrys to the migrated list : "+e.getMessage());
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
	
	/**
	 * used to remove a bulk of entry of the migrated item list
	 */
	public static void removeEntryToTheMigratedList(ArrayList<String> idList)
		{
		try
			{
			for(String id : idList)
				{
				if(Variables.getMigratedItemList().contains(id))
					{
					Variables.getLogger().debug("Migrated item file : removing "+id);
					Variables.getMigratedItemList().remove(id);
					}
				}
			
			updateMigratedList();//We then rewrite the file
			
			/**
			 * We also update real time status of simple items
			 */
			for(String id : idList)
				{
				for(BasicOffice o : Variables.getOfficeList())
					{
					if(o.getId().equals(id))
						{
						o.setStatus(basicItemStatus.tomigrate);
						break;
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to remove the entrys of the migrated list : "+e.getMessage());
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
			//We also search using the device pool
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getDevicepool().equals(officeID))return o;
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while looking for the office '"+officeID+"' : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * used to get a CMG from the cmg list
	 */
	public static CMG getCMG(String name)
		{
		try
			{
			for(CMG cmg : Variables.getCmgList())
				{
				if(cmg.getName().equals(name))return cmg;
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while looking for the cmg '"+name+"' : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * To get a cligetOutput from the associated device name
	 */
	public static CliGetOutput getCliGetOutput(Device d)
		{
		for(CliGetOutput cgo : Variables.getCliGetOutputList())
			{
			if(cgo.getDevice().getInfo().equals(d.getInfo()))return cgo;
			}
		
		return null;
		}
	
	/**
	 * Used to get a deviceType from the list
	 */
	public static DeviceType getDeviceType(String deviceTypeName) throws Exception
		{
		for(DeviceType dt : Variables.getDeviceTypeList())
			{
			if(dt.getName().equals(deviceTypeName))return dt;
			}
		
		throw new Exception("The given deviceType was not found : "+deviceTypeName);
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
				UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("axlversionsrc")));
		
		Variables.getLogger().debug("Initializing source CUCM : "+srccucm.getInfo());
		
		initAXLConnectionToCUCM(srccucm);
		initRISConnectionToCUCM(srccucm);
		Variables.getLogger().debug("Initializing source CUCM done !");
		Variables.setSrccucm(srccucm);
		
		/**
		 * Destination CUCM
		 */
		CUCM dstcucm = new CUCM(UsefulMethod.getTargetOption("dstcucmip"),
				UsefulMethod.getTargetOption("port"),
				UsefulMethod.getTargetOption("username"),
				UsefulMethod.getTargetOption("password"),
				UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("axlversiondst")));
		
		Variables.getLogger().debug("Initializing destination CUCM : "+dstcucm.getInfo());
		
		initAXLConnectionToCUCM(dstcucm);
		initRISConnectionToCUCM(dstcucm);
		Variables.getLogger().debug("Initializing destination CUCM done !");
		Variables.setDstcucm(dstcucm);
		}
	
	public static String searchForLot(String name)
		{
		if(name.toLowerCase().contains("lot"))
			{
			if(name.contains("1"))return Lot.A.name();
			else if(name.contains("2"))return Lot.B.name();
			else if(name.contains("3"))return Lot.C.name();
			}
		
		return name;
		}
	
	public static String convertLot(Lot lot)
		{
		if(lot.equals(Lot.A))return "Lot 1";
		else if(lot.equals(Lot.B))return "Lot 2";
		else if(lot.equals(Lot.C))return "Lot 3";
		else if(lot.equals(Lot.CAMPUS))return "Lot CAMPUS";
		else if(lot.equals(Lot.PILOTE))return "Lot Pilote";
		else return lot.name();
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

