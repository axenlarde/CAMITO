package com.alex.camito.cli;

import java.util.ArrayList;

import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;



/**
 * Used to manage cli injection sessions
 *
 * @author Alexandre RATEL
 */
public class CliManager extends Thread
	{
	/**
	 * Variables
	 */
	private ArrayList<CliInjector> cliIList;
	private boolean stop, pause;
	private int maxThread;
	
	public CliManager()
		{
		stop = false;
		pause = false;
		cliIList = new ArrayList<CliInjector>();
		
		try
			{
			this.maxThread = Integer.parseInt(UsefulMethod.getTargetOption("maxclithread"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Couldn't find the max simultaneous thread value. Applying default value");
			this.maxThread = 10;
			}
		}
	
	/*****
	 * Here we will start cliInjector all at the same time
	 * according to the max thread value 
	 */
	public void run()
		{
		try
			{
			int index = 0;
			while((index < cliIList.size()) && (!stop))
				{
				for(int i=index; i<cliIList.size(); i++)
					{
					if(startNewThread()&&(!pause))
						{
						cliIList.get(i).start();
						index++;
						}
					else
						{
						/**
						 * If the max thread is reach or if the pause is on there is no point of trying the next cliInjector
						 * So we break here to directly go to the sleep step
						 */ 
						break;
						}
					}
				this.sleep(100);
				}
			
			/**
			 * To end this thread we wait for all the cliInjector to end
			 */
			boolean alive = true;
			while(alive)
				{
				alive = false;
				for(CliInjector clii : cliIList)
					{
					if(clii.isAlive())
						{
						alive = true;
						break;
						}
					}
				this.sleep(100);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong with the cli manager : "+e.getMessage(),e);
			}
		}
	
	private boolean startNewThread()
		{
		int totalInProgress = 0;
		for(CliInjector clii : cliIList)
			{
			if(clii.isAlive())totalInProgress++;
			}
		
		if(totalInProgress<=maxThread)return true;
		return false;
		}
	
	public ArrayList<CliInjector> getCliIList()
		{
		return cliIList;
		}

	public void setCliIList(ArrayList<CliInjector> cliIList)
		{
		this.cliIList = cliIList;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		this.stop = stop;
		}
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
