package com.alex.camito.misc;

import java.util.ArrayList;

import com.alex.camito.axl.misc.AXLItemLinker;
import com.alex.camito.axl.misc.ToUpdate;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.ItemType;
import com.alex.camito.utils.Variables.StatusType;




/**********************************
 * Interface aims to define one item to inject
 * 
 * @author RATEL Alexandre
 **********************************/
public abstract class ItemToInject implements ItemToInjectImpl
	{
	/**********
	 * Variables
	 */
	protected ItemType type;
	protected String UUID;
	protected String name;
	protected StatusType status;
	protected ActionType action;
	protected ArrayList<ErrorTemplate> errorList;
	protected ArrayList<Correction> correctionList;
	protected ArrayList<ToUpdate> tuList;
	protected AXLItemLinker linker;
	
	/***************
	 * Constructor
	 ***************/
	public ItemToInject(ItemType type, String name, AXLItemLinker linker)
		{
		this.type = type;
		this.name = name;
		this.linker = linker;
		this.UUID = "";
		this.status = StatusType.init;
		tuList = new ArrayList<ToUpdate>();
		errorList = new ArrayList<ErrorTemplate>();
		correctionList = new ArrayList<Correction>();
		}
	
	/****
	 * Method used to launch the build process :
	 * - Prepare the request for injection
	 * - Check if the item already exist and if yes get the UUID
	 */
	public void build(CUCM cucm) throws Exception
		{
		try
			{
			//We check that the item doesn't already exist
			boolean exists = false;
			
			try
				{
				exists = isExisting(cucm);//Will throw an exception if not
				}
			catch (Exception e)
				{
				if(UsefulMethod.itemNotFoundInTheCUCM(e.getMessage()))
					{
					Variables.getLogger().debug("Item "+this.name+" doesn't already exist in the CUCM");
					exists = false;
					}
				else
					{
					//Another error happened
					throw e;
					}
				}
			
			
			if(exists)
				{
				if((action.equals(ActionType.delete))||(action.equals(ActionType.update))||(action.equals(ActionType.rollback))||(action.equals(ActionType.reset)))
					{
					this.status = StatusType.waiting;
					}
				else
					{
					this.status = StatusType.injected;
					}
				}
			else
				{
				//The item doesn't already exist we have to inject it except if it is a deletion task
				if(action.equals(ActionType.delete))
					{
					this.status = StatusType.disabled;
					}
				else
					{
					this.status = StatusType.waiting;
					}
				}
			
			this.errorList.addAll(linker.init(cucm));
			
			doBuild(cucm);
			}
		catch (Exception e)
			{
			this.status = StatusType.error;
			throw e;
			}
		}
	
	
	/**
	 * Method used to launch the injection process
	 * @throws Exception 
	 */
	public void inject(CUCM cucm) throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" of type "+this.getType().name()+" injection process begin");
		
		if(this.status.equals(StatusType.waiting))
			{
			try
				{
				this.status = StatusType.processing;
				this.UUID = doInject(cucm);//Item successfully injected
				Variables.getLogger().info("Item "+this.getName()+" successfuly injected");
				this.status = StatusType.injected;
				}
			catch (Exception e)
				{
				this.status = StatusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while injecting the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else if(this.status.equals(StatusType.init))
			{
			throw new Exception("Item "+this.getName()+" was not ready for the injection : build it first");
			}
		else
			{
			Variables.getLogger().info("Not to inject : "+this.getName()+" Status : "+this.getStatus().name());
			}
		}
	
	/**
	 * Method used to launch the deletion process
	 * @throws Exception 
	 */
	public void delete(CUCM cucm) throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" deletion process begin");
		
		//If we got the UUID we can proceed
		if((!this.UUID.equals(""))&&(this.UUID != null)&&(status.equals(StatusType.waiting)))
			{
			try
				{
				this.status = StatusType.processing;
				doDelete(cucm);
				Variables.getLogger().info("Item "+this.getName()+" deleted successfully");
				this.status = StatusType.deleted;//Item successfully deleted
				}
			catch (Exception e)
				{
				this.status = StatusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while deleting the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else
			{
			Variables.getLogger().info("The item "+this.getName()+" of type "+this.getType().name()+" can't be deleted because it doesn't exist in the CUCM");
			status = StatusType.disabled;
			}
		}
	
	/**
	 * Method used to launch the update process
	 * @throws Exception 
	 */
	public void update(CUCM cucm) throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" update process begin");
		
		//If we got the UUID we can proceed
		if((!this.UUID.equals(""))&&(this.UUID != null)&&(status.equals(StatusType.waiting)))
			{
			try
				{
				this.status = StatusType.processing;
				manageTuList();//To setup what to update
				doUpdate(cucm);//Item successfully updated
				Variables.getLogger().info("Item "+this.getName()+" updated successfully");
				this.status = StatusType.updated;//Item successfully deleted
				}
			catch (Exception e)
				{
				this.status = StatusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while updating the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else
			{
			Variables.getLogger().info("The item "+this.getName()+" of type "+this.getType().name()+" can't be updated because it doesn't exist in the CUCM");
			status = StatusType.disabled;
			}
		}
	
	/**
	 * Method used to display the item informations
	 */
	public String getInfo()
		{
		return name+" "
		+UUID;
		}
	
	public void addNewError(ErrorTemplate error)
		{
		//We check for duplicate
		boolean exists = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))
				{
				exists = true;
				break;
				}
			}
		if(!exists)errorList.add(error);
		}
	
	public void addNewCorrection(Correction correction)
		{
		//We check for duplicate
		boolean exists = false;
		for(Correction c : correctionList)
			{
			if(c.getDescription().equals(correction.getDescription()))
				{
				exists = true;
				break;
				}
			}
		if(!exists)correctionList.add(correction);
		}

	public ItemType getType()
		{
		return type;
		}
	public void setType(ItemType type)
		{
		this.type = type;
		}
	public String getUUID()
		{
		return UUID;
		}
	public void setUUID(String uUID)
		{
		UUID = uUID;
		}
	public String getName()
		{
		return name;
		}
	public void setName(String name)
		{
		this.name = name;
		linker.setName(name);
		}
	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}
	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}
	public synchronized StatusType getStatus()
		{
		return status;
		}
	public synchronized void setStatus(StatusType status)
		{
		this.status = status;
		}

	public ActionType getAction()
		{
		return action;
		}

	public void setAction(ActionType action) throws Exception
		{
		this.action = action;
		if(action.equals(ActionType.update))
			{
			manageTuList();
			}
		}

	public ArrayList<ToUpdate> getTuList()
		{
		return tuList;
		}

	public void setTuList(ArrayList<ToUpdate> tuList)
		{
		this.tuList = tuList;
		}

	public ArrayList<Correction> getCorrectionList()
		{
		return correctionList;
		}

	public void setCorrectionList(ArrayList<Correction> correctionList)
		{
		this.correctionList = correctionList;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

