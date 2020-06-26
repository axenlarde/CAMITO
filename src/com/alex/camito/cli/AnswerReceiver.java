
package com.alex.camito.cli;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.alex.camito.utils.Variables;



/**
 * Class aims to manage Gateway input
 */
public class AnswerReceiver extends Thread
	{
	/**
	 * Variables
	 */
	private boolean stop;
	private String info;
	private BufferedReader in;
	private ArrayList<String> exchange;//Used to store the entire conversation
	private int maxBuffer;
	
	
	public AnswerReceiver(String info, BufferedReader in)
		{
		this.info = info;
		this.in = in;
		stop = false;
		exchange = new ArrayList<String>();
		maxBuffer = 100;
		}
	
	
	public void run()
		{
		String row;
		
		try
			{
			while (((row = in.readLine()) != null)&&(!stop))
		    	{
		    	//Store one element of conversation
		    	if(exchange.size()>maxBuffer)exchange.clear();
		    	exchange.add(row);
		    	
		    	if(Pattern.matches(".*Invalid.*",row))
		    		{
		    		//We save the error in an arraylist. Finally it will be possible to display it.
		    		//storeErrorInfo("",3);
		    		}
		    	else if(Pattern.matches(".*%.*",row))
		    		{
		    		//We save the error in an arraylist. Finally it will be possible to display it.
		    		//storeErrorInfo("",1);
		    		}
		    	else
		    		{
		    		Variables.getLogger().debug(info+" : #CLI# "+row);
		    		}
		    	}
			
			/******
			 * Deletion of the conversation to
			 * avoid full memory error.
			 */
			exchange.clear();
			Variables.getLogger().debug(info+" : #CLI : End of the receiver thread");
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(info+" : CLI : ERROR while listening");
			}
		}
	
	public void setStop(boolean stop)
		{
		this.stop = stop;
		}

	public ArrayList<String> getExchange()
		{
		return exchange;
		}
	
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
