package com.alex.camito.axl.misc;

import java.util.ArrayList;

import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToInject;




/**********************************
 * Interface of an AXLItem
 * 
 * @author RATEL Alexandre
 **********************************/
public interface AXLItemLinkerImpl
	{
	/**
	 * Injection
	 */
	public String inject(CUCM cucm) throws Exception; //Return the UUID of the injected item
	
	//Used in addition of the previous one to force the developer to implement a method dedicated to the good version
	public String doInjectVersion105(CUCM cucm) throws Exception;
	/**************/
	
	/***********
	 * Deletion
	 */
	public void delete(CUCM cucm) throws Exception; //Delete the item in the CUCM
	
	//Used in addition of the previous one to force the developer to implement a method dedicated to the good version
	public void doDeleteVersion105(CUCM cucm) throws Exception;
	/***************/
	
	/***********
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> init() throws Exception; //Initialize the item
	
	//Used in addition of the previous one to force the developer to implement a method dedicated to the good version
	public ArrayList<ErrorTemplate> doInitVersion105() throws Exception;
	/***************/
	
	/***********
	 * Update
	 */
	public void update(ArrayList<ToUpdate> tulist, CUCM cucm) throws Exception; //Initialize the item
	
	//Used in addition of the previous one to force the developer to implement a method dedicated to the good version
	public void doUpdateVersion105(ArrayList<ToUpdate> tulist, CUCM cucm) throws Exception;
	/***************/
	
	/***********
	 * Get
	 */
	public ItemToInject get(CUCM cucm) throws Exception; //Initialize the item
	
	//Used in addition of the previous one to force the developer to implement a method dedicated to the good version
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception;
	/***************/
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

