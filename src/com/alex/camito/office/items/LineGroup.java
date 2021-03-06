package com.alex.camito.office.items;

import java.util.ArrayList;

import com.alex.camito.axl.items.LineGroupMember;
import com.alex.camito.axl.linkers.LineGroupLinker;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;



/**********************************
 * Class used to define an item of type "LineGroup"
 * 
 * @author RATEL Alexandre
 **********************************/

public class LineGroup extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String distributionAlgorithm,
	rnaReversionTimeOut,
	huntAlgorithmNoAnswer,
	huntAlgorithmBusy,
	huntAlgorithmNotAvailable;
	
	private ArrayList<LineGroupMember> lineList;
	
	private int index;
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public LineGroup(String name,
			String distributionAlgorithm,
			String rnaReversionTimeOut, String huntAlgorithmNoAnswer,
			String huntAlgorithmBusy, String huntAlgorithmNotAvailable) throws Exception
		{
		super(ItemType.linegroup, name, new LineGroupLinker(name));
		this.distributionAlgorithm = distributionAlgorithm;
		this.rnaReversionTimeOut = rnaReversionTimeOut;
		this.huntAlgorithmNoAnswer = huntAlgorithmNoAnswer;
		this.huntAlgorithmBusy = huntAlgorithmBusy;
		this.huntAlgorithmNotAvailable = huntAlgorithmNotAvailable;
		this.lineList = new ArrayList<LineGroupMember>();
		
		index = 0;
		}

	public LineGroup(String name) throws Exception
		{
		super(ItemType.linegroup, name, new LineGroupLinker(name));
		this.lineList = new ArrayList<LineGroupMember>();
		}

	/***********
	 * Method used to prepare the item for the injection
	 * by gathering the needed UUID from the CUCM 
	 */
	public void doBuild(CUCM cucm) throws Exception
		{
		errorList.addAll(linker.init(cucm));
		}
	
	
	/**
	 * Method used to inject data in the CUCM using
	 * the Cisco API
	 * 
	 * It also return the item's UUID once injected
	 */
	public String doInject(CUCM cucm) throws Exception
		{
		return linker.inject(cucm);//Return UUID
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doDelete(CUCM cucm) throws Exception
		{
		linker.delete(cucm);
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doUpdate(CUCM cucm) throws Exception
		{
		linker.update(tuList, cucm);
		}
	
	/**
	 * Method used to check if the element exist in the CUCM
	 */
	public boolean isExisting(CUCM cucm) throws Exception
		{
		LineGroup myLG = (LineGroup)linker.get(cucm);
		this.UUID = myLG.getUUID();
		this.lineList = myLG.getLineList();
		//Has to be enhanced
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	public String getInfo()
		{
		return name+" "
		+UUID;
		}
	
	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		/*
		name = CollectionTools.getValueFromCollectionFile(index, name, this, true);
		distributionAlgorithm = CollectionTools.getValueFromCollectionFile(index, distributionAlgorithm, this, true);
		rnaReversionTimeOut = CollectionTools.getValueFromCollectionFile(index, rnaReversionTimeOut, this, true);
		huntAlgorithmNoAnswer = CollectionTools.getValueFromCollectionFile(index, huntAlgorithmNoAnswer, this, false);
		huntAlgorithmBusy = CollectionTools.getValueFromCollectionFile(index, huntAlgorithmBusy, this, false);
		huntAlgorithmNotAvailable = CollectionTools.getValueFromCollectionFile(index, huntAlgorithmNotAvailable, this, false);
		*/
		for(LineGroupMember lgm : lineList)
			{
			this.getErrorList().addAll(lgm.getErrorList());
			this.getCorrectionList().addAll(lgm.getCorrectionList());
			}
		
		LineGroupLinker myLineGroup = (LineGroupLinker) linker;
		myLineGroup.setName(name);
		myLineGroup.setDistributionAlgorithm(distributionAlgorithm);
		myLineGroup.setRnaReversionTimeOut(rnaReversionTimeOut);
		myLineGroup.setHuntAlgorithmBusy(huntAlgorithmBusy);
		myLineGroup.setHuntAlgorithmNoAnswer(huntAlgorithmNoAnswer);
		myLineGroup.setHuntAlgorithmNotAvailable(huntAlgorithmNotAvailable);
		myLineGroup.setLineList(lineList);
		}
	
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(distributionAlgorithm))tuList.add(LineGroupLinker.toUpdate.distributionAlgorithm);
		if(UsefulMethod.isNotEmpty(rnaReversionTimeOut))tuList.add(LineGroupLinker.toUpdate.rnaReversionTimeOut);
		if(UsefulMethod.isNotEmpty(huntAlgorithmNoAnswer))tuList.add(LineGroupLinker.toUpdate.huntAlgorithmNoAnswer);
		if(UsefulMethod.isNotEmpty(huntAlgorithmBusy))tuList.add(LineGroupLinker.toUpdate.huntAlgorithmBusy);
		if(UsefulMethod.isNotEmpty(huntAlgorithmNotAvailable))tuList.add(LineGroupLinker.toUpdate.huntAlgorithmNotAvailable);
		if((lineList != null) && (lineList.size() != 0))tuList.add(LineGroupLinker.toUpdate.lineList);
		}

	public String getDistributionAlgorithm()
		{
		return distributionAlgorithm;
		}

	public void setDistributionAlgorithm(String distributionAlgorithm)
		{
		this.distributionAlgorithm = distributionAlgorithm;
		}

	public String getRnaReversionTimeOut()
		{
		return rnaReversionTimeOut;
		}

	public void setRnaReversionTimeOut(String rnaReversionTimeOut)
		{
		this.rnaReversionTimeOut = rnaReversionTimeOut;
		}

	public String getHuntAlgorithmNoAnswer()
		{
		return huntAlgorithmNoAnswer;
		}

	public void setHuntAlgorithmNoAnswer(String huntAlgorithmNoAnswer)
		{
		this.huntAlgorithmNoAnswer = huntAlgorithmNoAnswer;
		}

	public String getHuntAlgorithmBusy()
		{
		return huntAlgorithmBusy;
		}

	public void setHuntAlgorithmBusy(String huntAlgorithmBusy)
		{
		this.huntAlgorithmBusy = huntAlgorithmBusy;
		}

	public String getHuntAlgorithmNotAvailable()
		{
		return huntAlgorithmNotAvailable;
		}

	public void setHuntAlgorithmNotAvailable(String huntAlgorithmNotAvailable)
		{
		this.huntAlgorithmNotAvailable = huntAlgorithmNotAvailable;
		}

	public ArrayList<LineGroupMember> getLineList()
		{
		return lineList;
		}

	public void setLineList(ArrayList<LineGroupMember> lineList)
		{
		this.lineList = lineList;
		}

	public int getIndex()
		{
		return index;
		}

	public void setIndex(int index)
		{
		this.index = index;
		}

	
	/*2020*//*RATEL Alexandre 8)*/
	}

