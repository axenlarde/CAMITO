package com.alex.camito.axl.linkers;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.alex.camito.axl.items.LineGroupMember;
import com.alex.camito.axl.misc.AXLItemLinker;
import com.alex.camito.axl.misc.ToUpdate;
import com.alex.camito.misc.CUCM;
import com.alex.camito.misc.ErrorTemplate;
import com.alex.camito.misc.ItemToInject;
import com.alex.camito.misc.SimpleRequest;
import com.alex.camito.office.items.LineGroup;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.ItemType;
import com.cisco.axl.api._10.RLineGroupMember;


/**********************************
 * Is the AXLItem design to link the item "LineGroup"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class LineGroupLinker extends AXLItemLinker
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
	
	public enum toUpdate implements ToUpdate
		{
		distributionAlgorithm,
		rnaReversionTimeOut,
		huntAlgorithmNoAnswer,
		huntAlgorithmBusy,
		huntAlgorithmNotAvailable,
		lineList
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public LineGroupLinker(String name) throws Exception
		{
		super(name);
		}
	
	/***************
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> doInitVersion105(CUCM cucm) throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		
		//Nothing to do here
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.NameAndGUIDRequest deleteReq = new com.cisco.axl.api._10.NameAndGUIDRequest();
		
		deleteReq.setName(this.getName());//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().removeLineGroup(deleteReq);//We send the request to the CUCM
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.AddLineGroupReq req = new com.cisco.axl.api._10.AddLineGroupReq();
		com.cisco.axl.api._10.XLineGroup params = new com.cisco.axl.api._10.XLineGroup();
		
		/**
		 * We set the item parameters
		 */
		params.setName(this.getName());//Name
		params.setDistributionAlgorithm(distributionAlgorithm);
		
		if(UsefulMethod.isNotEmpty(huntAlgorithmBusy))params.setHuntAlgorithmBusy(huntAlgorithmBusy);
		if(UsefulMethod.isNotEmpty(huntAlgorithmNoAnswer))params.setHuntAlgorithmNoAnswer(huntAlgorithmNoAnswer);
		if(UsefulMethod.isNotEmpty(huntAlgorithmNotAvailable))params.setHuntAlgorithmNotAvailable(huntAlgorithmNotAvailable);
		
		params.setRnaReversionTimeOut(rnaReversionTimeOut);
		/************/
		
		/***********************
		 * Members
		 */
		com.cisco.axl.api._10.XLineGroup.Members membersList = new com.cisco.axl.api._10.XLineGroup.Members();
		
		for(LineGroupMember l : lineList)
			{
			com.cisco.axl.api._10.XLineGroupMember myMember = new com.cisco.axl.api._10.XLineGroupMember();
			com.cisco.axl.api._10.XDirn number = new com.cisco.axl.api._10.XDirn();
			number.setPattern(l.getNumber());
			number.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, l.getPartition(), cucm)));
			myMember.setDirectoryNumber(number);
			myMember.setLineSelectionOrder(Integer.toString(l.getOrder()));
			membersList.getMember().add(myMember);
			}
		
		params.setMembers(membersList);
		/***********************/
		
		req.setLineGroup(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().addLineGroup(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList, CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.UpdateLineGroupReq req = new com.cisco.axl.api._10.UpdateLineGroupReq();
		
		/***********
		 * We set the item parameters
		 */
		req.setName(this.getName());
		
		if(tuList.contains(toUpdate.distributionAlgorithm))req.setDistributionAlgorithm(distributionAlgorithm);
		if(tuList.contains(toUpdate.rnaReversionTimeOut))req.setDistributionAlgorithm(rnaReversionTimeOut);
		if(tuList.contains(toUpdate.huntAlgorithmNoAnswer))req.setDistributionAlgorithm(huntAlgorithmNoAnswer);
		if(tuList.contains(toUpdate.huntAlgorithmBusy))req.setDistributionAlgorithm(huntAlgorithmBusy);
		if(tuList.contains(toUpdate.huntAlgorithmNotAvailable))req.setDistributionAlgorithm(huntAlgorithmNotAvailable);
		
		if(tuList.contains(toUpdate.lineList))
			{
			com.cisco.axl.api._10.UpdateLineGroupReq.Members membersList = new com.cisco.axl.api._10.UpdateLineGroupReq.Members();
			
			for(LineGroupMember l : lineList)
				{
				com.cisco.axl.api._10.XLineGroupMember myMember = new com.cisco.axl.api._10.XLineGroupMember();
				com.cisco.axl.api._10.XDirn number = new com.cisco.axl.api._10.XDirn();
				number.setPattern(l.getNumber());
				number.setRoutePartitionName(new JAXBElement(new QName("routePartitionName"), com.cisco.axl.api._10.XFkType.class, SimpleRequest.getUUIDV105(ItemType.partition, l.getPartition(), cucm)));
				myMember.setDirectoryNumber(number);
				myMember.setLineSelectionOrder(Integer.toString(l.getOrder()));
				membersList.getMember().add(myMember);
				}
			
			req.setMembers(membersList);
			}
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().updateLineGroup(req);//We send the request to the CUCM
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105(CUCM cucm) throws Exception
		{
		com.cisco.axl.api._10.GetLineGroupReq req = new com.cisco.axl.api._10.GetLineGroupReq();
		
		/**
		 * We set the item parameters
		 */
		req.setName(this.getName());
		/************/
		
		com.cisco.axl.api._10.GetLineGroupRes resp = cucm.getAXLConnectionV105().getLineGroup(req);//We send the request to the CUCM
		
		LineGroup myLG = new LineGroup(this.getName());
		myLG.setUUID(resp.getReturn().getLineGroup().getUuid());
		
		for(com.cisco.axl.api._10.RLineGroupMember lgm : resp.getReturn().getLineGroup().getMembers().getMember())
			{
			myLG.getLineList().add(new LineGroupMember(lgm.getDirectoryNumber().getPattern(),
					lgm.getDirectoryNumber().getRoutePartitionName().getValue(),
					Integer.parseInt(lgm.getLineSelectionOrder())));
			}
		
		
		//Has to be written
		
		return myLG;
		}
	/****************/

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

	
	/*2020*//*RATEL Alexandre 8)*/
	}

