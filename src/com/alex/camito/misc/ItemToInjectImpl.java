package com.alex.camito.misc;

/**********************************
 * Interface design to force to define the following method
 * 
 * @author RATEL Alexandre
 **********************************/
public interface ItemToInjectImpl
	{
	public void doBuild(CUCM cucm) throws Exception; //Used to prepare the item for the injection by gathering the needed UUID from the CUCM
	public String doInject(CUCM cucm) throws Exception; //Return the UUID of the injected item
	public void doDelete(CUCM cucm) throws Exception; //Delete the item in the CUCM
	public void doUpdate(CUCM cucm) throws Exception; //Update the item in the CUCM
	public boolean isExisting(CUCM cucm) throws Exception; //Check if the item exists in the CUCM
	public String getInfo(); //Get item's info
	public void resolve() throws Exception; //Resolve the item content using the xml templates
	public void manageTuList() throws Exception;
	
	/*2020*//*RATEL Alexandre 8)*/
	}

