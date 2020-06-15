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

import com.alex.camito.device.BasicPhone;
import com.alex.camito.device.BasicPhone.PhoneStatus;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.office.items.CalledPartyTransformationPattern;
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
			csvBuffer.write("Coda"+splitter+"Device Pool"+splitter+"Office Name"+splitter+"Status"+splitter+"Detailed Status"+cr);
			
			for(Office o : officeList)
				{
				csvBuffer.write(o.getCoda()+splitter+o.getDevicePool()+splitter+o.getName()+splitter+o.getStatus()+splitter+o.getDetailedStatus()+cr);
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
	public static void forwardOfficeLines(Office office, String forwardPrefix, String forwardCSSName, CUCM srccucm, CUCM dstcucm)
		{
		try
			{
			/**
			 * 1. We get the line list
			 */
			String request = "select nm.dnorpattern as pattern,nm.tkpatternusage as usage,rp.name as partition from numplan nm, routepartition rp where nm.fkroutepartition=rp.pkid and nm.dnorpattern like '"+office.getCoda()+"%'";
			
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
				try
					{
					//We create a destination line to allow comparison and modification
					Line dstLine = new Line(l.getName(), l.getRoutePartitionName());
					
					//We first check for exist and also get the current fwall values
					l.isExisting(srccucm);
					dstLine.isExisting(dstcucm);
					
					//We then copy the current forward all destination to the destination cluster
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
					Variables.getLogger().debug(office.getInfo()+" Line "+l.getInfo()+" :  forwarded");
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+l.getInfo()+" : ERROR while forwarding the line : "+e.getMessage(),e);
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
					
					CalledPartyTransformationPattern calledPTP = null;
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
						calledPTP = new CalledPartyTransformationPattern(pattern, partition);
						break;//Should only return one object
						}
					
					//We now update the Called Party Transformation Pattern
					calledPTP.isExisting(srccucm);
					calledPTP.setCalledPartyTransformationMask(forwardPrefix+calledPTP.getCalledPartyTransformationMask());
					calledPTP.resolve();
					calledPTP.update(srccucm);
					Variables.getLogger().debug(office.getInfo()+" Hunt Pilot "+hp.getInfo()+" :  forwarded");
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+hp.getInfo()+" : ERROR while forwarding hunt pilot : "+e.getMessage(),e);
					}
				}
			
			/**
			 * 4. we now forward each translation pattern
			 */
			for(TranslationPattern tp : tpList)
				{
				try
					{
					tp.isExisting(srccucm);
					tp.setCalledPartyTransformationMask(forwardPrefix+tp.getCalledPartyTransformationMask());
					tp.resolve();
					tp.update(srccucm);
					}
				catch(Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+tp.getInfo()+" : ERROR while forwarding translation pattern : "+e.getMessage(),e);
					}
				}
			
			/**
			 * 5. We get the cti route point list and forward them
			 */
			for(String s : ctiRPNumberList)
				{
				try
					{
					//If the line begins with the office prefix we ignore it because it has already been processed
					if(s.startsWith(office.getCoda()))
						{
						//Ignore
						}
					else
						{
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
						ctiRP.isExisting(srccucm);
						ctiRP.setFwAllCallingSearchSpaceName(forwardCSSName);
						ctiRP.setFwAllDestination(forwardPrefix+ctiRP.getName());
						ctiRP.resolve();
						ctiRP.update(srccucm);
						Variables.getLogger().debug(office.getInfo()+" : CTI RP '"+s+"' forwarded");
						}
					}
				catch (Exception e)
					{
					Variables.getLogger().error(office.getInfo()+" : "+s+" : ERROR while forwarding cti route point : "+e.getMessage(),e);
					}
				}
			}
		catch(Exception e)
			{
			Variables.getLogger().error(office.getInfo()+" : ERROR while forwardinglines : "+e.getMessage(),e);
			}
		}
	
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
