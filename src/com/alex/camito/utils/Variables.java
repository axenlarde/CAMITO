package com.alex.camito.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alex.camito.action.Task;
import com.alex.camito.cli.CliGetOutput;
import com.alex.camito.cli.CliProfile;
import com.alex.camito.device.BasicDevice;
import com.alex.camito.device.DeviceType;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.Did;
import com.alex.camito.misc.ValueMatcher;
import com.alex.camito.misc.storedUUID;
import com.alex.camito.office.misc.BasicOffice;
import com.alex.camito.office.misc.CMG;


/**********************************
 * Used to store static variables
 * 
 * @author RATEL Alexandre
 **********************************/
public class Variables
	{
	/**
	 * Variables
	 */
	//Enum
	/***
	 * itemType :
	 * Is used to give a type to the request ready to be injected
	 * This way we can manage or sort them more easily
	 * 
	 * The order is important here, indeed, it will define later
	 * how the items are injected
	 */
	public enum ItemType
		{
		location,
		region,
		srstreference,
		devicepool,
		commondeviceconfig,
		commonPhoneConfig,
		securityProfile,
		conferencebridge,
		mediaresourcegroup,
		mediaresourcegrouplist,
		partition,
		callingsearchspace,
		trunksip,
		vg,
		routegroup,
		routelist,
		translationpattern,
		callingpartytransformationpattern,
		calledpartytransformationpattern,
		physicallocation,
		devicemobilityinfo,
		devicemobilitygroup,
		datetimesetting,
		callmanagergroup,
		phone,
		udp,
		user,
		line,
		voicemail,
		telecasterservice,
		siptrunksecurityprofile,
		sipprofile,
		phonetemplatename,
		linegroup,
		huntlist,
		huntpilot,
		callpickupgroup,
		udplogin,
		aargroup,
		usercontrolgroup,
		analog,
		gateway,
		sqlRequest,
		associateanalog,
		userlocal,
		softkeytemplate,
		unknown
		};
	
	/********************************************
	 * statusType :
	 * Is used to set the status of a request as followed :
	 * - init : the request has to be built
	 * - waiting : The request is ready to be injected. We gonna reach this status after the request has been built or has been deleted
	 * - processing : The injection or the deletion of the request is currently under progress
	 * - disabled : The request has not to be injected
	 * - injected : The request has been injected with success
	 * - error : Something went wrong and an exception has been thrown
	 ***************************************/
	public enum StatusType
		{
		injected,
		error,
		processing,
		waiting,
		disabled,
		init,
		deleted,
		updated
		};
		
	/********************************************
	 * cucmVersion :
	 * Is used to define the cucm version used for the injection
	 ***************************************/
	public enum CucmVersion
		{
		version80,
		version85,
		version90,
		version91,
		version100,
		version105,
		version110,
		version115,
		version120,
		version125
		};
	
	/********************************************
	 * actionType :
	 * Is used to set the type of action is going to do a 
	 ***************************************/
	public enum ActionType
		{
		inject,
		delete,
		update,
		rollback,
		reset
		};
		
	/********************************************
	 * substituteType :
	 * Is used to know what is the current data source to use
	 ***************************************/
	public enum SubstituteType
		{
		phone,
		pbt,
		css,
		profile,
		misc
		};
		
	/**
	 * ascom type
	 */
	public enum AscomType
		{
		master,
		standby,
		ims3,
		slave
		};
	
	/**
	 * Office Type
	 */
	public enum OfficeType
		{
		AGENCE,
		CLR,
		CRCE,
		CRCN,
		OFFICE,
		PFA,
		SMART
		};
		
	public enum Lot
		{
		PILOTE,
		A,
		B,
		C,
		CAMPUS,
		NONE,
		UNKNOWN
		}
		
	public enum ReachableStatus
		{
		reachable,
		unreachable,
		unknown
		};
		
	//Misc
	private static String softwareName;
	private static String softwareVersion;
	private static CucmVersion CUCMVersion;
	private static Logger logger;
	private static ArrayList<String[][]> tabConfig;
	private static ArrayList<BasicOffice> OfficeList;
	private static ArrayList<BasicDevice> DeviceList;
	private static ArrayList<Did> didList;
	private static ArrayList<CMG> cmgList;
	private static ArrayList<DeviceType> deviceTypeList;
	private static eMailSender eMSender;
	private static String mainDirectory;
	private static String configFileName;
	private static String matcherFileName;
	private static String officeListFileName;
	private static String deviceListFileName;
	private static String cliProfileListFileName;
	private static String substitutesFileName;
	private static String didListFileName;
	private static String cmgListFileName;
	private static String deviceTypeListFileName;
	private static ArrayList<String> matcherList;
	private static ArrayList<ValueMatcher> substituteList;
	private static ArrayList<storedUUID> uuidList;
	private static String collectionFileName;
	private static ArrayList<Task> taskList;
	private static ArrayList<String> migratedItemList;
	private static String migratedItemFileName;
	private static String logFileName;
	
	//Langage management
	public enum language{english,french};
	private static String languageFileName;
	private static ArrayList<ArrayList<String[][]>> languageContentList;
	
	//AXL & RIS
    private static CUCM srccucm;
    private static CUCM dstcucm;
    
    //CLI
    private static ArrayList<CliProfile> cliProfileList;
    private static String cliGetOutputFileName;
    private static ArrayList<CliGetOutput> cliGetOutputList;
    
    /**************
     * Constructor
     **************/
	public Variables()
		{
		uuidList = new ArrayList<storedUUID>();
		mainDirectory = ".";
		configFileName = "configFile.xml";
		matcherFileName = "matchers.xml";
		officeListFileName = "officeList.xml";
		deviceListFileName = "deviceList.xml";
		cliProfileListFileName = "cliProfileList.xml";
		languageFileName = "languages.xml";
		substitutesFileName = "substitutes.xml";
		collectionFileName = "database.xlsx";
		migratedItemFileName = "migratedItemsList.xml";
		didListFileName = "didList.xml";
		cmgListFileName = "cmgList.xml";
		cliGetOutputFileName = "CliGetOutput";
		deviceTypeListFileName = "deviceTypeList.xml";
		}

	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static CucmVersion getCUCMVersion()
		{
		if(CUCMVersion == null)
			{
			//It has to be initiated
			try
				{
				CUCMVersion = UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("axlversion"));
				Variables.getLogger().info("CUCM version : "+Variables.getCUCMVersion());
				}
			catch(Exception e)
				{
				getLogger().debug("The AXL version couldn't be parsed. We will use the default version", e);
				CUCMVersion = CucmVersion.version85;
				}
			}
		
		return CUCMVersion;
		}

	public static void setCUCMVersion(CucmVersion cUCMVersion)
		{
		CUCMVersion = cUCMVersion;
		}

	public synchronized static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static ArrayList<String[][]> getTabConfig()
		{
		return tabConfig;
		}

	public static void setTabConfig(ArrayList<String[][]> tabConfig)
		{
		Variables.tabConfig = tabConfig;
		}

	public static ArrayList<BasicOffice> getOfficeList() throws Exception
		{
		if(OfficeList == null)
			{
			Variables.getLogger().debug("Initialisation of OfficeList");
			Variables.setOfficeList(UsefulMethod.initOfficeList());
			}
		
		return OfficeList;
		}

	public static void setOfficeList(ArrayList<BasicOffice> officeList)
		{
		OfficeList = officeList;
		}

	public static eMailSender geteMSender()
		{
		return eMSender;
		}

	public static void seteMSender(eMailSender eMSender)
		{
		Variables.eMSender = eMSender;
		}

	public static String getMainDirectory() throws Exception
		{
		return mainDirectory;
		}

	public static void setMainDirectory(String mainConfigFileDirectory)
		{
		Variables.mainDirectory = mainConfigFileDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static String getMatcherFileName()
		{
		return matcherFileName;
		}

	public static void setMatcherFileName(String matcherFileName)
		{
		Variables.matcherFileName = matcherFileName;
		}

	public static String getOfficeListFileName()
		{
		return officeListFileName;
		}

	public static void setOfficeListFileName(String officeListFileName)
		{
		Variables.officeListFileName = officeListFileName;
		}

	public static String getLanguageFileName()
		{
		return languageFileName;
		}

	public static void setLanguageFileName(String languageFileName)
		{
		Variables.languageFileName = languageFileName;
		}

	public static ArrayList<String> getMatcherList() throws Exception
		{
		if(matcherList == null)
			{
			Variables.getLogger().debug("Initialisation of matcherList");
			Variables.setMatcherList(UsefulMethod.readFile("matchers", Variables.getMatcherFileName()));
			}
		return matcherList;
		}

	public static void setMatcherList(ArrayList<String> matcherList)
		{
		Variables.matcherList = matcherList;
		}

	public static ArrayList<storedUUID> getUuidList()
		{
		return uuidList;
		}

	public static void setUuidList(ArrayList<storedUUID> uuidList)
		{
		Variables.uuidList = uuidList;
		}

	public static ArrayList<ArrayList<String[][]>> getLanguageContentList() throws Exception
		{
		if(languageContentList == null)
			{
			Variables.getLogger().debug("Initialisation of languageContentList");
			Variables.setLanguageContentList(UsefulMethod.readExtFile("language", Variables.getLanguageFileName()));
			}
		
		return languageContentList;
		}

	public static void setLanguageContentList(
			ArrayList<ArrayList<String[][]>> languageContentList)
		{
		Variables.languageContentList = languageContentList;
		}

	public static String getSubstitutesFileName()
		{
		return substitutesFileName;
		}

	public static void setSubstitutesFileName(String substitutesFileName)
		{
		Variables.substitutesFileName = substitutesFileName;
		}

	public static ArrayList<ValueMatcher> getSubstituteList() throws Exception
		{
		if(substituteList == null)
			{
			Variables.getLogger().debug("Initialisation of substituteList");
			Variables.setSubstituteList(UsefulMethod.initSubstituteList(UsefulMethod.readFileTab("substitute", Variables.getSubstitutesFileName())));
			}
		
		return substituteList;
		}

	public static void setSubstituteList(ArrayList<ValueMatcher> substituteList)
		{
		Variables.substituteList = substituteList;
		}

	public static ArrayList<BasicDevice> getDeviceList() throws Exception
		{
		if(DeviceList == null)
			{
			Variables.getLogger().debug("Initialisation of DeviceList");
			Variables.setDeviceList(UsefulMethod.initDeviceList());
			}
		
		return DeviceList;
		}

	public static void setDeviceList(ArrayList<BasicDevice> deviceList)
		{
		DeviceList = deviceList;
		}

	public static String getCollectionFileName()
		{
		return collectionFileName;
		}

	public static void setCollectionFileName(String collectionFileName)
		{
		Variables.collectionFileName = collectionFileName;
		}

	public static String getDeviceListFileName()
		{
		return deviceListFileName;
		}

	public static void setDeviceListFileName(String deviceListFileName)
		{
		Variables.deviceListFileName = deviceListFileName;
		}

	public static synchronized ArrayList<Task> getTaskList()
		{
		if(taskList == null)
			{
			taskList = new ArrayList<Task>();
			}
		return taskList;
		}

	public static synchronized void setTaskList(ArrayList<Task> taskList)
		{
		Variables.taskList = taskList;
		}

	public static ArrayList<CliProfile> getCliProfileList() throws Exception
		{
		if(cliProfileList == null)
			{
			UsefulMethod.initCliProfileList();
			}
		return cliProfileList;
		}

	public static void setCliProfileList(ArrayList<CliProfile> cliProfileList)
		{
		Variables.cliProfileList = cliProfileList;
		}

	public static String getCliProfileListFileName()
		{
		return cliProfileListFileName;
		}

	public static void setCliProfileListFileName(String cliProfileListFileName)
		{
		Variables.cliProfileListFileName = cliProfileListFileName;
		}

	public static ArrayList<String> getMigratedItemList()
		{
		return migratedItemList;
		}

	public static void setMigratedItemList(ArrayList<String> migratedItemList)
		{
		Variables.migratedItemList = migratedItemList;
		}

	public static String getMigratedItemFileName()
		{
		return migratedItemFileName;
		}

	public static void setMigratedItemFileName(String migratedItemFileName)
		{
		Variables.migratedItemFileName = migratedItemFileName;
		}

	public static String getLogFileName()
		{
		return logFileName;
		}

	public static void setLogFileName(String logFileName)
		{
		Variables.logFileName = logFileName;
		}

	public static ArrayList<Did> getDidList()
		{
		return didList;
		}

	public static void setDidList(ArrayList<Did> didList)
		{
		Variables.didList = didList;
		}

	public static String getDidListFileName()
		{
		return didListFileName;
		}

	public static void setDidListFileName(String didListFileName)
		{
		Variables.didListFileName = didListFileName;
		}

	public static CUCM getSrccucm()
		{
		return srccucm;
		}

	public static void setSrccucm(CUCM srccucm)
		{
		Variables.srccucm = srccucm;
		}

	public static CUCM getDstcucm()
		{
		return dstcucm;
		}

	public static void setDstcucm(CUCM dstcucm)
		{
		Variables.dstcucm = dstcucm;
		}

	public static ArrayList<CMG> getCmgList()
		{
		return cmgList;
		}

	public static void setCmgList(ArrayList<CMG> cmgList)
		{
		Variables.cmgList = cmgList;
		}

	public static String getCmgListFileName()
		{
		return cmgListFileName;
		}

	public static void setCmgListFileName(String cmgListFileName)
		{
		Variables.cmgListFileName = cmgListFileName;
		}
	
	public static ArrayList<CliGetOutput> getCliGetOutputList()
		{
		if(cliGetOutputList == null)
			{
			cliGetOutputList = new ArrayList<CliGetOutput>();
			}
		return cliGetOutputList;
		}

	public static void setCliGetOutputList(ArrayList<CliGetOutput> cliGetOutputList)
		{
		Variables.cliGetOutputList = cliGetOutputList;
		}

	public static String getCliGetOutputFileName()
		{
		return cliGetOutputFileName;
		}

	public static void setCliGetOutputFileName(String cliGetOutputFileName)
		{
		Variables.cliGetOutputFileName = cliGetOutputFileName;
		}

	public static ArrayList<DeviceType> getDeviceTypeList()
		{
		return deviceTypeList;
		}

	public static void setDeviceTypeList(ArrayList<DeviceType> deviceTypeList)
		{
		Variables.deviceTypeList = deviceTypeList;
		}

	public static String getDeviceTypeListFileName()
		{
		return deviceTypeListFileName;
		}

	public static void setDeviceTypeListFileName(String deviceTypeListFileName)
		{
		Variables.deviceTypeListFileName = deviceTypeListFileName;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

