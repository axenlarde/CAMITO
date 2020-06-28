package com.alex.camito.office.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.camito.axl.items.LineGroupMember;
import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.BasicPhone.PhoneStatus;
import com.alex.camito.device.Device;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.office.items.CalledPartyTransformationPattern;
import com.alex.camito.office.items.LineGroup;
import com.alex.camito.office.items.TranslationPattern;
import com.alex.camito.risport.RisportTools;
import com.alex.camito.user.items.HuntPilot;
import com.alex.camito.user.items.Line;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.StatusType;


/**
 * Toolbox of static method about offices
 *
 * @author Alexandre RATEL
 */
public class OfficeTools
	{
	
	/**
	 * Used to get a devicepool associated phones
	 */
	public static ArrayList<BasicPhone> getDevicePoolPhoneList(String DevicePoolName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for devicepool's phone "+DevicePoolName);
		
		ArrayList<BasicPhone> l = new ArrayList<BasicPhone>();
		
		String request = "select d.name, d.description, tm.name as model from device d, devicepool dp, typemodel tm where dp.pkid=d.fkdevicepool and tm.enum=d.tkmodel and d.tkClass='1' and dp.name='"+DevicePoolName+"'";
		
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				BasicPhone bp = new BasicPhone("TBD", "", "");
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						bp.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("description"))
						{
						bp.setDescription(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("model"))
						{
						bp.setModel(list.item(i).getTextContent());
						}
					}
				Variables.getLogger().debug("Phone found : "+bp.getName());
				l.add(bp);
				}
			Variables.getLogger().debug("Found "+l.size()+" phones for "+DevicePoolName);
			return l;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the devicepool's phones for "+DevicePoolName+" "+e.getMessage(),e);
			}
		
		return new ArrayList<BasicPhone>();
		}
	
	/**
	 * Return the device pool name of the given phone
	 */
	public static String getDevicePoolFromPhoneName(String phoneName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for phone's devicePool : "+phoneName);
		
		String request = "select dp.name from device d, devicepool dp where dp.pkid=d.fkdevicepool and d.name='"+phoneName+"'";
		
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						Variables.getLogger().debug("Found devicePool "+list.item(i).getTextContent()+" for phone "+phoneName);
						return list.item(i).getTextContent();
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the phone's devicePool for phone "+phoneName+" : "+e.getMessage(),e);
			}
		return null;
		}
	
	/**
	 * Will execute a streamlined office phone survey
	 * We concatenate all the phone in one big request
	 * to lower the total RIS request per minute and the avoid 
	 * to reach the system limit (15 request per minute max)
	 */
	public static void phoneSurvey(ArrayList<Office> officeList, CUCM srccucm, CUCM dstcucm)
		{
		int officeCount = 0;
		ArrayList<BasicPhone> srcPL = new ArrayList<BasicPhone>();
		ArrayList<BasicPhone> dstPL = new ArrayList<BasicPhone>();
		ArrayList<Office> selectedO = new ArrayList<Office>();
		
		for(Office o : officeList)
			{
			srcPL.addAll(o.getPhoneList());
			dstPL.addAll(o.getPhoneList());
			selectedO.add(o);
			officeCount++;
			}
		
		Variables.getLogger().debug(srccucm.getInfo()+" : Phone survey starts, sending RISRequest for "+officeCount+" offices and "+srcPL.size()+" phones");
		srcPL = RisportTools.doPhoneSurvey(srccucm, srcPL);
		
		Variables.getLogger().debug(dstcucm.getInfo()+" : Phone survey starts, sending RISRequest for "+officeCount+" offices and "+dstPL.size()+" phones");
		dstPL = RisportTools.doPhoneSurvey(dstcucm, dstPL);
		
		//We now put the result back to each office
		for(Office o : selectedO)
			{
			for(BasicPhone bp : o.getPhoneList())
				{
				boolean srcFound = false;
				boolean dstFound = false;
				for(BasicPhone risBP : srcPL)
					{
					if(bp.getName().equals(risBP.getName()))
						{
						srcFound = true;
						break;
						}
					}
				bp.setSrcStatus(srcFound?PhoneStatus.registered:PhoneStatus.unregistered);
				
				for(BasicPhone risBP : dstPL)
					{
					if(bp.getName().equals(risBP.getName()))
						{
						dstFound = true;
						break;
						}
					}
				bp.setDstStatus(dstFound?PhoneStatus.registered:PhoneStatus.unregistered);
				}
			}
		Variables.getLogger().debug("Phone survey ends");
		}
	
	/**
	 * Used to write down the phone survey
	 * Useful to compare the phone status before and after the migration
	 */
	public static void writePhoneSurveyToCSV(ArrayList<Office> officeList)
		{
		try
			{
			Variables.getLogger().debug("Writing phone survey to a file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getPhoneSurveyFileName()+"_"+time.format(date);
			BufferedWriter csvBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+".csv"), false));
			
			//FirstLine
			csvBuffer.write("Coda"+splitter+"Device Pool"+splitter+"Device Name"+splitter+"Device Type"+splitter+"Status Before"+splitter+"Status After"+splitter+"Is OK"+cr);
			
			for(Office o : officeList)
				{
				for(BasicPhone bp : o.getPhoneList())
					{
					csvBuffer.write(o.getCoda()+splitter+o.getDevicePool().getName()+splitter+bp.getName()+splitter+bp.getModel()+splitter+bp.getFirstStatus()+splitter+bp.getDstStatus()+splitter+bp.isOK()+cr);
					}
				}
			
			csvBuffer.flush();
			csvBuffer.close();
			Variables.getLogger().debug("Writing phone survey : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing phone survey to CSV : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Write the overall result to a csv file
	 */
	public static void writeOverallResultToCSV(ArrayList<Office> officeList)
		{
		try
			{
			Variables.getLogger().debug("Writing the overall result to a file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getOverallResultFileName()+"_"+time.format(date);
			BufferedWriter csvBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+".csv"), false));
			
			//FirstLine
			csvBuffer.write("Type"+splitter+"ID"+splitter+"Device Pool"+splitter+"Name"+splitter+"Status"+splitter+"Detailed Status"+cr);
			
			for(Office o : officeList)
				{
				csvBuffer.write("Office"+splitter+o.getCoda()+splitter+o.getDevicePool().getName()+splitter+o.getName()+splitter+o.getStatus()+splitter+o.getDetailedStatus()+cr);
				
				for(LinkedOffice lo : o.getLinkedOffice())
					{
					csvBuffer.write("LinkedOffice"+splitter+lo.getCoda()+splitter+o.getDevicePool().getName()+splitter+lo.getName()+splitter+o.getStatus()+splitter+""+cr);
					}
				
				for(Device d : o.getDeviceList())
					{
					csvBuffer.write(d.getDeviceType().getName()+splitter+d.getIp()+splitter+d.getOfficeid()+splitter+d.getName()+splitter+d.getStatus()+splitter+d.getDetailedStatus()+cr);
					}
				}
			
			csvBuffer.flush();
			csvBuffer.close();
			Variables.getLogger().debug("Writing overall result : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing overall result to CSV : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Will scan for lines using the office prefix in the source CUCM
	 * and forward them to the destination CUCM
	 * 
	 * Will scan for the following line type :
	 * - Phone line
	 * - Hunt pilot
	 * - Translation pattern
	 * 
	 * For hunt pilots we will modify the related Calling Party Transformation Pattern
	 * Because there is no forward all option, just noan which is not enough
	 * 
	 * While forwarding phone lines we also copy the current forward destination of the line
	 * to the destination cluster. So if a user forwarded his line the his mobile, it will still works
	 * 
	 * We will also have to find the related cti route point to forward them too
	 */
	public static void forwardOfficeLines(SourceOffice office, String forwardPrefix, String forwardCSSName, CUCM srccucm, CUCM dstcucm)
		{
		try
			{
			Variables.getLogger().debug(office.getInfo()+" : Forwarding lines");
			
			/**
			 * First we get all the lines associated to the device pool and forward them
			 */
			ArrayList<Line> officelineList = new ArrayList<Line>();
			if(office instanceof Office)
				{
				Office off = (Office)office;
				String request = "select np.dnorpattern as pattern, rp.name as partition from numplan np, routepartition rp, devicenumplanmap dnpm, device d, devicepool dp where d.pkid=dnpm.fkdevice and dnpm.fknumplan=np.pkid and dp.pkid=d.fkdevicepool and rp.pkid=np.fkroutepartition and np.tkpatternusage='2' and dp.name='"+off.getDevicePool().getName()+"'";
					
				List<Object> reply = SimpleRequest.doSQLQuery(request, srccucm);
				for(Object o : reply)
					{
					Element rowElement = (Element) o;
					NodeList list = rowElement.getChildNodes();
					
					String pattern = null, partition = null;
					for(int i = 0; i< list.getLength(); i++)
						{
						if(list.item(i).getNodeName().equals("pattern"))
							{
							pattern = list.item(i).getTextContent();
							}
						else if(list.item(i).getNodeName().equals("partition"))
							{
							partition = list.item(i).getTextContent();
							}
						}
					officelineList.add(new Line(pattern, partition));
					}
				for(Line l : officelineList)
					{
					try
						{
						forwardLine(l, office, forwardPrefix, forwardCSSName, srccucm, dstcucm);
						}
					catch (Exception e)
						{
						Variables.getLogger().error(office.getInfo()+" : "+l.getInfo()+" : ERROR while forwarding the line : "+e.getMessage(),e);
						office.addError(new ErrorTemplate(l.getInfo()+" : ERROR while forwarding the line : "+e.getMessage()));
						}
					}
				}
			
			
			/**
			 * 1. Just to be sure we also get the lines based on the coda
			 */
			String coda = office.getCoda();
			//If the coda starts with 0 it has been replaced with a * in the cucm. so we do the same
			if((coda.startsWith("0")) && (coda.length() == 4))
				{
				coda = coda.substring(1,coda.length());
				coda = "*"+coda;
				}
			
			String request = "select nm.dnorpattern as pattern,nm.tkpatternusage as usage,rp.name as partition from numplan nm, routepartition rp where nm.fkroutepartition=rp.pkid and nm.dnorpattern like '"+coda+"%'";
			
			//The item lists
			ArrayList<Line> lineList = new ArrayList<Line>();
			ArrayList<HuntPilot> hpList = new ArrayList<HuntPilot>();
			ArrayList<TranslationPattern> tpList = new ArrayList<TranslationPattern>();
			ArrayList<String> ctiRPNumberList = new ArrayList<String>();
			//ArrayList<RoutePattern> rpList = new ArrayList<RoutePattern>();//To write if needed
			
			List<Object> reply = SimpleRequest.doSQLQuery(request, srccucm);
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				String pattern = null,usage = null,partition = null;
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("pattern"))
						{
						pattern = list.item(i).getTextContent();
						}
					else if(list.item(i).getNodeName().equals("usage"))
						{
						usage = list.item(i).getTextContent();
						}
					else if(list.item(i).getNodeName().equals("partition"))
						{
						partition = list.item(i).getTextContent();
						}
					}
				
				if(usage.equals("2"))lineList.add(new Line(pattern, partition));//2 is for device
				else if(usage.equals("7"))hpList.add(new HuntPilot(pattern, partition));//7 is for hunt pilot
				else if(usage.equals("3"))tpList.add(new TranslationPattern(pattern, partition));//3 is for translation pattern
				}
			
			/**
			 * 2. We now forward each line using the forward all destination
			 * In addition we also copy the current forward all destination from the source cluster to the
			 * destination one
			 */
			for(Line l : lineList)
				{
				//We check that this line has not already been processed earlier with the device pool
				boolean found = false;
				for(Line ol : officelineList)
					{
					if(ol.getName().equals(l.getName()))
						{
						found = true;
						break;
						}
					}
				if(found)continue;
				
				try
					{
					forwardLine(l, office, forwardPrefix, forwardCSSName, srccucm, dstcucm);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+l.getInfo()+" : ERROR while forwarding the line : "+e.getMessage(),e);
					office.addError(new ErrorTemplate(l.getInfo()+" : ERROR while forwarding the line : "+e.getMessage()));
					}
				}
			
			/**
			 * 3. We get the hunt pilot list and forward their Called Party Transformation Pattern
			 */
			for(HuntPilot hp : hpList)
				{
				try
					{
					//Check for exist
					hp.isExisting(srccucm);
					
					//We check for forward to cti route point
					if(UsefulMethod.isNotEmpty(hp.getForwardHuntNoAnswerDestination()))ctiRPNumberList.add(hp.getForwardHuntNoAnswerDestination());
					
					//We find the related called party transformation pattern using a sql request
					String calledPTPRequest = "select np.dnorpattern as pattern, rp.name as partition from numplan np, routepartition rp where np.fkroutepartition=rp.pkid and tkpatternusage='20' and calledpartytransformationmask='"+hp.getName()+"'";
					List<Object> calledPTPReply = SimpleRequest.doSQLQuery(calledPTPRequest, srccucm);
					
					ArrayList<CalledPartyTransformationPattern> calledPTPList = new ArrayList<CalledPartyTransformationPattern>();
					
					for(Object o : calledPTPReply)
						{
						Element rowElement = (Element) o;
						NodeList list = rowElement.getChildNodes();
						
						String pattern=null,partition=null;
						for(int i = 0; i< list.getLength(); i++)
							{
							if(list.item(i).getNodeName().equals("pattern"))
								{
								pattern = list.item(i).getTextContent();
								}
							else if(list.item(i).getNodeName().equals("partition"))
								{
								partition = list.item(i).getTextContent();
								}
							}
						calledPTPList.add(new CalledPartyTransformationPattern(pattern, partition));
						}
					
					for(CalledPartyTransformationPattern calledPTP : calledPTPList)
						{
						//We create a destination CalledPTP to allow comparison and modification
						CalledPartyTransformationPattern dstCalledPTP = new CalledPartyTransformationPattern(calledPTP.getName(), calledPTP.getRoutePartitionName());
						
						/**
						 * We copy the source data to the destination one
						 * For any reason, if the line is already forwarded we do not copy
						 * the source data to the destination one
						 */
						calledPTP.isExisting(srccucm);
						dstCalledPTP.isExisting(dstcucm);
						
						if(calledPTP.getCalledPartyTransformationMask().startsWith(forwardPrefix))
							{
							Variables.getLogger().debug(office.getInfo()+" Hunt Pilot "+hp.getInfo()+" : was already forwarded so we do nothing");
							}
						else
							{
							dstCalledPTP.setCalledPartyTransformationMask(calledPTP.getCalledPartyTransformationMask());
							dstCalledPTP.resolve();
							dstCalledPTP.update(dstcucm);
							
							//We now forward the Called Party Transformation Pattern
							calledPTP.setCalledPartyTransformationMask(forwardPrefix+calledPTP.getCalledPartyTransformationMask());
							calledPTP.resolve();
							calledPTP.update(srccucm);
							Variables.getLogger().debug(office.getInfo()+" Hunt Pilot "+hp.getInfo()+" : forwarded");
							}
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+hp.getInfo()+" : ERROR while forwarding the hunt pilot : "+e.getMessage(),e);
					office.addError(new ErrorTemplate(hp.getInfo()+" : ERROR while forwarding the hunt pilot : "+e.getMessage()));
					}
				}
			
			/**
			 * 4. we now forward each translation pattern
			 */
			for(TranslationPattern tp : tpList)
				{
				try
					{
					//We first copy the source object
					TranslationPattern dstTP = new TranslationPattern(tp.getName(), tp.getRoutePartitionName());
					
					/**
					 * We copy the source data to the destination one
					 * For any reason, if the line is already forwarded we do not copy
					 * the source data to the destination one
					 */
					tp.isExisting(srccucm);
					dstTP.isExisting(dstcucm);
					
					if(tp.getCalledPartyTransformationMask().startsWith(forwardPrefix))
						{
						Variables.getLogger().debug(office.getInfo()+" Translation Pattern "+tp.getInfo()+" : was already forwarded so we do nothing");
						}
					else
						{
						dstTP.setCalledPartyTransformationMask(tp.getCalledPartyTransformationMask());
						dstTP.resolve();
						dstTP.update(dstcucm);
						
						//We now forward the source translation pattern
						tp.setCalledPartyTransformationMask(forwardPrefix+tp.getCalledPartyTransformationMask());
						tp.resolve();
						tp.update(srccucm);
						Variables.getLogger().debug(office.getInfo()+" Translation Pattern "+tp.getInfo()+" : forwarded");
						}
					}
				catch(Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+tp.getInfo()+" : ERROR while forwarding the translation pattern : "+e.getMessage(),e);
					office.addError(new ErrorTemplate(tp.getInfo()+" : ERROR while forwarding the translation pattern : "+e.getMessage()));
					}
				}
			
			/**
			 * 5. We get the cti route point list and forward them
			 */
			for(String s : ctiRPNumberList)
				{
				try
					{
					/**
					 * If the ctirp is in the line list we do nothing because it has already been forwarded
					 */
					boolean found = false;
					for(Line l : lineList)
						{
						if(l.getName().equals(s))
							{
							found = true;
							break;
							}
						}
					
					//Same with office line list
					for(Line l : officelineList)
						{
						if(l.getName().equals(s))
							{
							found = true;
							break;
							}
						}
					
					if(found)continue;
					
					
					//Should be a cti route point so we forward it
					Line ctiRP = null;
					
					/**
					 * We first have to get the partition so we start with a sql request
					 * This request also get only cti route point device so we are sure
					 * this is a ctirp
					 */
					String ctiRequest = "select np.dnorpattern as pattern,rp.name as partition from numplan np, routepartition rp, devicenumplanmap dnpm, device d where np.fkroutepartition=rp.pkid and dnpm.fknumplan=np.pkid and d.pkid = dnpm.fkdevice and d.tkclass='10' and np.dnorpattern='"+s+"'";
					List<Object> ctiReply = SimpleRequest.doSQLQuery(ctiRequest, srccucm);
					for(Object o : ctiReply)
						{
						Element rowElement = (Element) o;
						NodeList list = rowElement.getChildNodes();
						
						String pattern = null,partition = null;
						for(int i = 0; i< list.getLength(); i++)
							{
							if(list.item(i).getNodeName().equals("pattern"))
								{
								pattern = list.item(i).getTextContent();
								}
							else if(list.item(i).getNodeName().equals("partition"))
								{
								partition = list.item(i).getTextContent();
								}
							}
						ctiRP = new Line(pattern, partition);
						break;//Should only return one line so we can break
						}
					
					forwardLine(ctiRP, office, forwardPrefix, forwardCSSName, srccucm, dstcucm);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+s+" : ERROR while forwarding the cti route point : "+e.getMessage(),e);
					office.addError(new ErrorTemplate(s+" : ERROR while forwarding the cti route point : "+e.getMessage()));
					}
				}
			Variables.getLogger().debug(office.getInfo()+" : Forwarding lines ends");
			}
		catch(Exception e)
			{
			Variables.getLogger().error(office.getInfo()+" : ERROR while forwarding the lines : "+e.getMessage(),e);
			office.addError(new ErrorTemplate("ERROR while forwarding the lines : "+e.getMessage()));
			}
		}
	
	/**
	 * To forward the given line
	 * @throws Exception 
	 */
	private static void forwardLine(Line l, SourceOffice office, String forwardPrefix, String forwardCSSName, CUCM srccucm, CUCM dstcucm) throws Exception
		{
		//We create a destination line to allow comparison and modification
		Line dstLine = new Line(l.getName(), l.getRoutePartitionName());
		
		//We first check for exist and also get the current fwall values
		l.isExisting(srccucm);
		dstLine.isExisting(dstcucm);
		
		/**
		 * We then copy the current forward all destination to the destination cluster
		 * For any reason, if the line is already forwarded we do not copy
		 * the source data to the destination one
		 */
		if(l.getFwAllDestination().startsWith(forwardPrefix))
			{
			Variables.getLogger().debug(office.getInfo()+" Line "+l.getInfo()+" : was already forwarded so we do nothing");
			}
		else
			{
			dstLine.setFwAllCallingSearchSpaceName(l.getFwAllCallingSearchSpaceName());
			dstLine.setFwAllDestination(l.getFwAllDestination());
			dstLine.setFwAllVoicemailEnable(l.getFwAllVoicemailEnable());
			dstLine.resolve();
			dstLine.update(dstcucm);
			
			//Finally we forward the line
			l.setFwAllDestination(forwardPrefix+l.getName());
			l.setFwAllCallingSearchSpaceName(forwardCSSName);
			l.setFwAllVoicemailEnable("false");
			l.resolve();
			l.update(srccucm);
			Variables.getLogger().debug(office.getInfo()+" Line "+l.getInfo()+" : forwarded");
			}
		}
	
	/******
	 * Fix configuration mismatch between the source
	 * and the destination cluster
	 * 
	 * Will look for configuration mismatch among the following :
	 * - Devices
	 * - Line group members
	 * - Logged out members
	 * - Lines
	 * 
	 * !!!! In this version we only report the mismatch, we do not fix them !!!!
	 */
	public static ArrayList<String> fixMismatch(SourceOffice office, CUCM srccucm, CUCM dstcucm)
		{
		ArrayList<String> mismatchList = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().debug(office.getInfo()+" : Fixing mismatch");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			
			/**
			 * 1. Device mismatch
			 * 
			 * The device mismatch have already been discovered during the office build process
			 * So we do not check for it again
			 */
			if(office instanceof Office)
				{
				for(BasicPhone bp : ((Office)office).getMissingPhone())
					{
					mismatchList.add(office.getInfo()+splitter+"Phone"+splitter+"Phone "+bp.getName()+" was not found in the destination cluster\r\n");
					}
				}
			
			/**
			 * 2. Line group members
			 * 
			 * We report mismatch and in the same time we verify that
			 * all the members are logged in
			 */
			ArrayList<LineGroup> lgList = new ArrayList<LineGroup>();
			ArrayList<LineGroup> dstLgList = new ArrayList<LineGroup>();
			
			//List the line groups containing office lines
			String lgRequest = "select distinct lg.pkid as lgUUID, lg.name as lgName from numplan np, linegroup lg, linegroupnumplanmap lgnpm where lg.pkid=lgnpm.fklinegroup and np.pkid=lgnpm.fknumplan and np.dnorpattern like '"+office.getCoda()+"%'";
			
			List<Object> reply = SimpleRequest.doSQLQuery(lgRequest, srccucm);
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				String lgName = null;
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("lgname"))
						{
						lgName = list.item(i).getTextContent();
						break;
						}
					}
				lgList.add(new LineGroup(lgName));
				dstLgList.add(new LineGroup(lgName));
				}
			
			//We check if Line group exists in both cluster, in the same time we retrieve the lg member
			for(LineGroup lg : lgList)
				{
				try
					{
					lg.isExisting(srccucm);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(lg.getInfo()+" : ERROR while retrieving information about the line group in the source cluster : "+e.getMessage());
					lg.setStatus(StatusType.error);
					}
				}
			for(LineGroup lg : dstLgList)
				{
				try
					{
					lg.isExisting(dstcucm);
					}
				catch (Exception e)
					{
					Variables.getLogger().error(lg.getInfo()+" : ERROR while retrieving information about the line group in the destination cluster");
					lg.setStatus(StatusType.error);
					mismatchList.add(office.getInfo()+splitter+"Line Group"+splitter+"Line group "+lg.getName()+" was not found in the destination cluster\r\n");
					}
				}
			
			//We now check that the members are identical
			for(LineGroup lg : lgList)
				{
				if(!lg.getStatus().equals(StatusType.error))
					{
					for(LineGroup dlg : dstLgList)
						{
						if(lg.getName().equals(dlg.getName()))
							{
							//We now compare the members
							for(LineGroupMember lgm : lg.getLineList())
								{
								boolean foundLGM = false;
								for(LineGroupMember dlgm : dlg.getLineList())
									{
									if(lgm.getNumber().equals(dlgm.getNumber()))
										{
										foundLGM = true;
										break;
										}
									}
								if(!foundLGM)
									{
									Variables.getLogger().debug(office.getInfo()+" : "+lg.getName()+" : Line group member missing in the destination cluster : "+lgm.getNumber());
									mismatchList.add(office.getInfo()+splitter+"Line group"+splitter+"Line group "+lg.getName()+" : Line group member missing in the destination cluster : "+lgm.getNumber()+"\r\n");
									}
								}
							break;
							}
						}
					}
				}
			
			//We finally check that the members are logged in
			for(LineGroup lg : dstLgList)
				{
				if(!lg.getStatus().equals(StatusType.error))
					{
					for(LineGroupMember lgm : lg.getLineList())
						{
						try
							{
							Line l = new Line(lgm.getNumber(), lgm.getPartition());
							l.isExisting(dstcucm);
							String LineHLogRequest = "select dnpm.fkdevice as deviceuuid, dhd.hlog as status from devicenumplanmap dnpm, devicehlogdynamic dhd where dnpm.fkdevice=dhd.fkdevice and dnpm.fknumplan='"+l.getUUID().toLowerCase().substring(1, l.getUUID().length()-1)+"'";//Substring to remove UUID{}
							
							List<Object> lineReply = SimpleRequest.doSQLQuery(LineHLogRequest, dstcucm);
							for(Object o : lineReply)
								{
								Element rowElement = (Element) o;
								NodeList list = rowElement.getChildNodes();
								
								String deviceUUID=null, status="t";
								for(int i = 0; i< list.getLength(); i++)
									{
									if(list.item(i).getNodeName().equals("deviceuuid"))
										{
										deviceUUID = list.item(i).getTextContent();
										}
									else if(list.item(i).getNodeName().equals("status"))
										{
										status = list.item(i).getTextContent();
										}
									}
								
								if(status.equals("f"))
									{
									//We now login the user
									String loginRequest = "UPDATE devicehlogdynamic SET hlog='t' where fkdevice='"+deviceUUID+"'";
									SimpleRequest.doSQLUpdate(loginRequest, dstcucm);
									
									Variables.getLogger().debug(office.getInfo()+" : "+lg.getName()+" : Logout line group member FIXED ! in the destination cluster : "+lgm.getNumber());
									mismatchList.add(office.getInfo()+splitter+"Line group"+splitter+"Line group "+lg.getName()+" : Logout line group member FIXED ! in the destination cluster : "+lgm.getNumber()+"\r\n");
									}
								}
							}
						catch (Exception e)
							{
							Variables.getLogger().error(office.getInfo()+" : "+lg.getName()+" ERROR while checking the logged in status : "+e.getMessage(),e);
							}
						}
					}
				}
			
			/**
			 * 3. Lines
			 * We should check for alerting name, description, etc. changes
			 * but it is too time consuming for what it brings  
			 */
			//To be written
			
			Variables.getLogger().debug(office.getInfo()+" : Fixing mismatch ends");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(office.getInfo()+" : ERROR while fixing mismatch : "+e.getMessage(),e);
			}
		
		return mismatchList;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
