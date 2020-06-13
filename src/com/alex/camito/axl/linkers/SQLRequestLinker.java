package com.alex.camito.axl.linkers;

import java.util.ArrayList;

import com.alex.camito.axl.misc.AXLItemLinker;
import com.alex.camito.axl.misc.ToUpdate;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.SQLRequestType;



/**********************************
 * Is the AXLItem design to link the item "SQL request"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class SQLRequestLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String sqlRequest;//The name is a description
	private SQLRequestType requestType;
	
	public enum toUpdate implements ToUpdate
		{
		sqlRequest
		}

	/***************
	 * Constructor
	 ***************/
	public SQLRequestLinker()
			throws Exception
		{
		super("");
		}

	/***************
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> doInitVersion105(CUCM cucm) throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		//To be written
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105(CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("There is nothing to delete in the case of an SQL request");
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105(CUCM cucm) throws Exception
		{
		if(requestType.equals(SQLRequestType.query))
			{
			com.cisco.axl.api._10.ExecuteSQLQueryReq req = new com.cisco.axl.api._10.ExecuteSQLQueryReq();
			
			/******************************
			 * We set the item parameters
			 * 
			 */
			req.setSql(this.sqlRequest);
		
			/**********/
				
			com.cisco.axl.api._10.ExecuteSQLQueryRes resp = cucm.getAXLConnectionV105().executeSQLQuery(req);//We send the request to the CUCM
			
			return resp.getReturn().getRow().toString();//Return UUID
			}
		else
			{
			com.cisco.axl.api._10.ExecuteSQLUpdateReq req = new com.cisco.axl.api._10.ExecuteSQLUpdateReq();
			
			/******************************
			 * We set the item parameters
			 * 
			 */
			req.setSql(this.sqlRequest);
		
			/**********/
				
			com.cisco.axl.api._10.ExecuteSQLUpdateRes resp = cucm.getAXLConnectionV105().executeSQLUpdate(req);//We send the request to the CUCM
			
			return resp.getReturn().getRowsUpdated().toString();//Return UUID
			}
		}

	public String doInjectVersion85() throws Exception
		{
		//To be written
		return null;
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("There is nothing to update in the case of a SQL request");
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("There is nothing to get in the case of a SQL request");
		return null;
		}
	/****************/

	public String getSqlRequest()
		{
		return sqlRequest;
		}

	public void setSqlRequest(String sqlRequest)
		{
		this.sqlRequest = sqlRequest;
		}

	public SQLRequestType getRequestType()
		{
		return requestType;
		}

	public void setRequestType(SQLRequestType requestType)
		{
		this.requestType = requestType;
		}

	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

