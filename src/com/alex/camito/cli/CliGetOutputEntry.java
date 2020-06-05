package com.alex.camito.cli;

/**
 * Used to store one cli get entry
 *
 * @author Alexandre RATEL
 */
public class CliGetOutputEntry
	{
	/**
	 * Variables
	 */
	private String columnName,value;

	public CliGetOutputEntry(String columnName, String value)
		{
		super();
		this.columnName = columnName;
		this.value = value;
		}

	public String getColumnName()
		{
		return columnName;
		}

	public void setColumnName(String columnName)
		{
		this.columnName = columnName;
		}

	public String getValue()
		{
		return value;
		}

	public void setValue(String value)
		{
		this.value = value;
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
