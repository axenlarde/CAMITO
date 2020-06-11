package com.alex.camito.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;



/**
 * Static method about cli
 *
 * @author Alexandre RATEL
 */
public class CliTools
	{
	
	public static void writeCliGetOutputToCSV()
		{
		try
			{
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			Variables.getLogger().debug("Writing Cli get output to a file");
			/**
			 * First we need to know how many different cli profile were used
			 * Indeed, we cannot put the device output of different cli profile with probably different column name 
			 * in the same file
			 * 
			 * So there will be one file per cli profile
			 */
			ArrayList<CliProfile> cpList = new ArrayList<CliProfile>();
			
			for(CliGetOutput cgo : Variables.getCliGetOutputList())
				{
				if(!cpList.contains(cgo.getDevice().getCliProfile()))
					{
					cpList.add(cgo.getDevice().getCliProfile());
					}
				}
			Variables.getLogger().debug(cpList.size()+" Cli profile found");
			
			/**
			 * Now we write the files
			 */
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getCliGetOutputFileName()+"_"+time.format(date);
			
			for(CliProfile cp : cpList)
				{
				BufferedWriter xmlBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+"_"+cp.getName()+".csv"), false));
				StringBuffer firstLine = new StringBuffer("");
				firstLine.append("Name"+splitter+"IP"+splitter);
				
				//we start by filling the first line
				for(CliGetOutput cgo : Variables.getCliGetOutputList())
					{
					if(cgo.getDevice().getCliProfile().getName().equals(cp.getName()))
						{
						for(CliGetOutputEntry cgoe : cgo.getEntryList())
							{
							firstLine.append(cgoe.getColumnName()+splitter);
							}
						break;//Only one device is enough to build the first line
						}
					}
				xmlBuffer.write(firstLine.substring(0,firstLine.length()-1)+cr);//The first line without the last comma
				
				//Now we write the lines
				for(CliGetOutput cgo : Variables.getCliGetOutputList())
					{
					if(cgo.getDevice().getCliProfile().getName().equals(cp.getName()))
						{
						StringBuffer line = new StringBuffer("");
						line.append(cgo.getDevice().getName()+splitter+cgo.getDevice().getIp()+splitter);
						for(CliGetOutputEntry cgoe : cgo.getEntryList())
							{
							line.append(cgoe.getValue().replaceAll(splitter, "#")+splitter);
							}
						xmlBuffer.write(line.substring(0,line.length()-1)+cr);//The line without the last comma
						}
					}
				xmlBuffer.flush();
				xmlBuffer.close();
				}
			
			Variables.getLogger().debug("Writing Cli get output : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing the cliget files : "+e.getMessage(),e);
			}
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
