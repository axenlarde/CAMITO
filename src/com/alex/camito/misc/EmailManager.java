package com.alex.camito.misc;

import java.util.ArrayList;

import com.alex.camito.device.Device;
import com.alex.camito.office.misc.Office;
import com.alex.camito.utils.LanguageManagement;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;


/**
 * Used to send email in a dedicated thread
 *
 * @author Alexandre RATEL
 */
public class EmailManager extends Thread
	{
	/**
	 * Variables
	 */
	ArrayList<Office> officeList;
	
	public EmailManager(ArrayList<Office> officeList)
		{
		super();
		this.officeList = officeList;
		start();
		}
	
	public void run()
		{
		sendReportEmail();
		}

	private void sendReportEmail()
		{
		try
			{
			Variables.getLogger().debug("Preparing email content");
			
			StringBuffer content = new StringBuffer("");
			content.append(LanguageManagement.getString("emailreportcontent").replaceAll("\\\\r\\\\n", "\t\r\n"));
			for(Office o : officeList)
				{
				content.append(o.getInfo()+" : "+o.getStatus()+" : "+o.getDetailedStatus()+"\t\r\n");
				if(o.getErrorList().size() > 0)
					{
					for(ErrorTemplate err : o.getErrorList())
						{
						content.append("\t- "+err.getErrorDesc()+"\t\r\n");
						}
					}
				for(Device d : o.getDeviceList())
					{
					if(d.getErrorList().size() > 0)
						{
						for(ErrorTemplate err : d.getErrorList())
							{
							content.append("\t- "+err.getErrorDesc()+"\t\r\n");
							}
						}
					}
				}
			content.append("\t\r\n");
			content.append(LanguageManagement.getString("emailfooter").replaceAll("\\\\r\\\\n", "\t\r\n"));

			//Variables.getLogger().debug("Email content ready to be sent : "+content.toString());
			
			UsefulMethod.sendEmailToTheAdminList(
					LanguageManagement.getString("emailreportsubject"),
					content.toString());
			
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while sending email : "+e.getMessage());
			}
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
