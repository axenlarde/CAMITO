package com.alex.camito.misc;

import java.util.ArrayList;

import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ActionType;
import com.alex.camito.utils.Variables.StatusType;


/**
 * Abstract Item to migrate class
 *
 * @author Alexandre RATEL
 */
public abstract class ItemToMigrate implements ItemToMigrateImpl
	{
	/**
	 * Variables
	 */	
	public enum ItmStatus
		{
		init,
		preaudit,
		update,
		postaudit,
		reset,
		done,
		disabled,
		error
		};
	
	protected ActionType action;
	protected ItmType type;
	protected ItmStatus status;
	protected String id,name;
	protected int index;
	protected ArrayList<ErrorTemplate> errorList;
	
	//This List contains the AXL items that should be updated if this item is migrated 
	protected ArrayList<ItemToInject> axlList;
	
	/**
	 * Constructor
	 */
	public ItemToMigrate(ItmType type, String name, String id, ActionType action)
		{
		super();
		this.action = action;
		this.type = type;
		this.name = name;
		this.id = id;
		axlList = new ArrayList<ItemToInject>();
		errorList = new ArrayList<ErrorTemplate>();
		status = ItmStatus.init;
		}
	
	@Override
	public void init() throws Exception
		{
		//Write something if needed
		
		doInit();
		}
	
	@Override
	public void build() throws Exception
		{
		Variables.getLogger().debug("Starting build for "+type+" "+name);
		
		doBuild();
		
		for(ItemToInject iti : axlList)
			{
			iti.setAction(action);
			iti.build(action.equals(ActionType.rollback)?Variables.getSrccucm():Variables.getDstcucm());
			}
		}
	
	@Override
	public void startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+type+" "+name);
		
		doStartSurvey();
		}
	
	@Override
	public void update() throws Exception
		{
		Variables.getLogger().debug("Starting migration for "+type+" "+name);
		
		doUpdate();
		
		for(ItemToInject iti : axlList)
			{
			if(!iti.getStatus().equals(StatusType.disabled))
				{
				iti.update(action.equals(ActionType.rollback)?Variables.getSrccucm():Variables.getDstcucm());
				if(iti.getStatus().equals(StatusType.error))
					{
					this.status = ItmStatus.error;
					addError(new ErrorTemplate(name+" : The following item returned an error : "+iti.getInfo()));
					}
				}
			else
				{
				Variables.getLogger().debug("The following item has been disabled so we do not process it : "+iti.getInfo());
				}
			}
		
		if(action.equals(ActionType.update))UsefulMethod.addEntryToTheMigratedList(id);
		else if(action.equals(ActionType.rollback))UsefulMethod.removeEntryToTheMigratedList(id);
		}
	
	@Override
	public void resolve() throws Exception
		{
		Variables.getLogger().debug("Starting resolution for "+type+" "+name);
		doResolve();
		
		for(ItemToInject iti : axlList)
			{
			iti.resolve();
			}
		}
	
	@Override
	public void reset()
		{
		Variables.getLogger().debug("Starting reset for "+type+" "+name);
		doReset();
		}
	
	@Override
	public String getDetailedStatus()
		{
		StringBuffer result = new StringBuffer("");
		/*
		if(errorList.size() > 0)
			{
			result.append("Item error list : \r\n");
			for(ErrorTemplate e : errorList)
				{
				result.append("- "+e.getErrorDesc()+"\r\n");
				}
			result.append("\r\n");
			}
		
		if(axlList.size() > 0)
			{
			result.append("CUCM items : \r\n");
			for(ItemToInject iti : axlList)
				{
				result.append("- "+iti.getName()+" : "+iti.getType().name()+" : "+iti.getStatus().name()+"\r\n");
				for(ErrorTemplate e : iti.getErrorList())
					{
					result.append("	+ "+e+"\r\n");
					}
				}
			result.append("\r\n");
			}*/
		result.append(doGetDetailedStatus());
		
		return result.toString();
		}
	
	/**
	 * Add an error to the error list and check for duplicate
	 */
	public void addError(ErrorTemplate error)
		{
		boolean duplicate = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))duplicate = true;break;//Duplicate found
			}
		if(!duplicate)errorList.add(error);
		}

	public ItmType getType()
		{
		return type;
		}

	public void setType(ItmType type)
		{
		this.type = type;
		}

	public ItmStatus getStatus()
		{
		return status;
		}

	public void setStatus(ItmStatus status)
		{
		this.status = status;
		}

	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public ArrayList<ItemToInject> getAxlList()
		{
		return axlList;
		}

	public void setAxlList(ArrayList<ItemToInject> axlList)
		{
		this.axlList = axlList;
		}

	public int getIndex()
		{
		return index;
		}

	public void setIndex(int index)
		{
		this.index = index;
		}

	public ActionType getAction()
		{
		return action;
		}

	public void setAction(ActionType action)
		{
		this.action = action;
		}

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}
