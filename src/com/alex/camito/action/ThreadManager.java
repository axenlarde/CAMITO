package com.alex.camito.action;

import java.util.ArrayList;

import com.alex.camito.utils.Variables;


/**
 * Responsible of managing threads
 *
 * @author Alexandre RATEL
 */
public class ThreadManager extends Thread
	{
	/**
	 * Variables
	 */
	private int maxConcurrentThread, sleepTime;
	private ArrayList<Thread> threadList;
	private boolean stop, pause;
	private int index;

	public ThreadManager(int maxConcurrentThread, int sleepTime, ArrayList<Thread> threadList)
		{
		super();
		this.maxConcurrentThread = maxConcurrentThread;
		this.sleepTime = sleepTime;
		this.threadList = threadList;
		stop = false;
		pause = false;
		index = 0;
		}

	public void run()
		{
		try
			{
			while((index < threadList.size()) && (!stop))
				{
				for(int i=index; i<threadList.size(); i++)
					{
					if(startNewThread()&&(!pause))
						{
						threadList.get(i).start();
						index++;
						}
					else
						{
						/**
						 * If the max thread is reach or if the pause is on there is no point of trying the next item
						 * So we break here to directly go to the sleep step
						 */ 
						break;
						}
					}
				this.sleep(sleepTime);
				}
			
			/**
			 * To end this thread we wait for all the thread to end
			 */
			boolean alive = true;
			while(alive)
				{
				alive = false;
				for(Thread t : threadList)
					{
					if(t.isAlive())
						{
						alive = true;
						break;
						}
					}
				this.sleep(sleepTime);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong with the thread manager : "+e.getMessage(),e);
			}
		}
	
	private boolean startNewThread()
		{
		int totalInProgress = 0;
		for(Thread t : threadList)
			{
			if(t.isAlive())totalInProgress++;
			}
		
		if(totalInProgress<=maxConcurrentThread)return true;
		return false;
		}
	
	public int getMaxConcurrentThread()
		{
		return maxConcurrentThread;
		}

	public ArrayList<Thread> getThreadList()
		{
		return threadList;
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		this.stop = stop;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		}
	
	public void shutDown()
		{
		this.stop = true;
		}

	public int getIndex()
		{
		return index;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
