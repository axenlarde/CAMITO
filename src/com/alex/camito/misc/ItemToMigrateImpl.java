package com.alex.camito.misc;

/**
 * Interface used to define a basic item to migrate
 *
 * @author Alexandre RATEL
 */
public interface ItemToMigrateImpl
	{
	public void startSurvey() throws Exception;//To get the status of the item
	public void doStartSurvey() throws Exception;//Is called in addition of the main method
	public void update() throws Exception;//To start the migration process of this item
	public void doUpdate() throws Exception;//Is called in addition of the main method
	public void resolve() throws Exception;//To resolve content
	public void doResolve() throws Exception;//Is called in addition of the main method
	public void init() throws Exception;//To init the item
	public void doInit() throws Exception;//Is called in addition of the main method
	public void build() throws Exception;//To build the item
	public void doBuild() throws Exception;//Is called in addition of the main method
	public String getInfo();//To display item info
	public void reset();//To reset the item
	public void doReset();//Is called in addition of the main method
	public String getDetailedStatus();//Return the detailed status of the item
	public String doGetDetailedStatus();//Return the detailed status of the item
	
	/*2019*//*RATEL Alexandre 8)*/
	}
