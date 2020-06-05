package com.alex.camito.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alex.camito.device.Device;
import com.alex.camito.office.misc.Office;
import com.alex.camito.utils.ClearFrenchString;
import com.alex.camito.utils.UsefulMethod;
import com.alex.camito.utils.Variables;
import com.alex.camito.utils.Variables.SubstituteType;


/**********************************
 * Class used to store static method used to work with the collection and template file
 * 
 * @author RATEL Alexandre
 **********************************/
public class CollectionTools
	{
	
	/****
	 * Return the row to target to get a value from the collection file
	 */
	private static int getRowNumber(String row, int currentRow) throws Exception
		{
		int rowNumber;
		
		if(row.contains("-"))
			{
			rowNumber = currentRow+Integer.parseInt(row.split("-")[0]);
			
			//We check if we are out of range
			int limit;
			if(row.split("-")[1].equals("*"))
				{
				limit = Integer.parseInt(UsefulMethod.getTargetOption("maxdataprocessed"));
				}
			else
				{
				limit = Integer.parseInt(row.split("-")[1]);
				}
			
			if(rowNumber>limit)
				{
				throw new Exception("The requested value is out of range");
				}
			}
		else
			{
			rowNumber = Integer.parseInt(row);
			}
		
		return rowNumber;
		}
			
			
	/**
	 * Return "true" if the value from the collection file is empty
	 */
	private static boolean checkEmptyValue(String value)
		{
		/************************
		 * If the value is empty or contains #, it maybe means that we reach the end of the collection file
		 */
		if((Pattern.matches("^$", value)) || (value.contains("#")))
			{
			return true;
			}
		else
			{
			return false;
			}
		/***********************/
		}
			
	/****
	 * Method used to apply a regex to a value	
	 * @throws Exception 
	 */
	private static String applyRegex(String newValue, String param) throws Exception
		{
		try
			{
			/*********
			 * Number before
			 **/
			if(Pattern.matches(".*\\*\\d+_\\*.*", param))
				{
				int number = howMany("\\*\\d+_\\*", param);
				if(newValue.length() >= number)
					{
					newValue = newValue.substring(0, number);
					}
				}
			/**
			 * End number before
			 *************/
			
			/*********
			 * Number after
			 **/
			if(Pattern.matches(".*\\*_\\d+\\*.*", param))
				{
				int number = howMany("\\*_\\d+\\*", param);
				if(newValue.length() >= number)
					{
					newValue = newValue.substring(newValue.length()-number, newValue.length());
					}
				}
			/**
			 * End number after
			 *************/
			
			/*************
			 * Majuscule
			 **/
			if(Pattern.matches(".*\\*M\\*.*", param))
				{
				newValue = newValue.toUpperCase();
				}
			if(Pattern.matches(".*\\*\\d+M\\*.*", param))
				{
				int majuscule = howMany("\\*\\d+M\\*", param);
				if(newValue.length() >= majuscule)
					{
					String first = newValue.substring(0, majuscule);
					String last = newValue.substring(majuscule,newValue.length());
					first = first.toUpperCase();
					last = last.toLowerCase();
					newValue = first+last;
					}
				}
			/**
			 * End majuscule
			 ****************/
			
			/*************
			 * Minuscule
			 **/
			if(Pattern.matches(".*\\*m\\*.*", param))
				{
				newValue = newValue.toLowerCase();
				}
			if(Pattern.matches(".*\\*\\d+m\\*.*", param))
				{
				int minuscule = howMany("\\*\\d+m\\*", param);
				if(newValue.length() >= minuscule)
					{
					String first = newValue.substring(0, minuscule);
					String last = newValue.substring(minuscule,newValue.length());
					first = first.toLowerCase();
					last = last.toUpperCase();
					newValue = first+last;
					}
				}
			/**
			 * End minuscule
			 ****************/
			
			/*************
			 * Split
			 * 
			 * Example : *1S/*
			 * means to split using "/" and to keep the first value
			 **/
			if(Pattern.matches(".*\\*\\d+S.+\\*.*", param))
				{
				int split = howMany("\\*\\d+S.+\\*", param);
				String splitter = getSplitter("\\*\\d+S.+\\*", param);
				newValue = newValue.split(splitter)[split-1];
				}
			/**
			 * End Split
			 ****************/
			
			/*************
			 * Replace
			 * 
			 * Example : *"test"R"testo"*
			 **/
			if(Pattern.matches(".*\\*\".+\"R\".*\"\\*.*", param))
				{
				String pattern = null;
				String replaceBy = null;
				Pattern begin = Pattern.compile("\".+\"R");
				Matcher mBegin = begin.matcher(param);
				Pattern end = Pattern.compile("R\".*\"");
				Matcher mEnd = end.matcher(param);
				
				if(mBegin.find())
					{
					String str = mBegin.group();
					str = str.substring(0,str.length()-1);//We remove the "R"
					str = str.replace("\"", "");
					pattern = str;
					}
				if(mEnd.find())
					{
					String str = mEnd.group();
					str = str.substring(1,str.length());//We remove the "R"
					str = str.replace("\"", "");
					replaceBy = str;
					}
				if((pattern != null) && (replaceBy != null))
					{
					newValue = newValue.replace(pattern, replaceBy);
					}
				}
			/**
			 * End Replace
			 ****************/
			
			/*************
			 * Clear French Char
			 * 
			 * Example : *C*
			 **/
			if(Pattern.matches(".*\\*C\\*.*", param))
				{
				newValue = ClearFrenchString.translate(newValue);
				}
			/**
			 * End Clear French Char
			 ****************/
			
			/**
			 * End
			 ****************/
			
			/*************
			 * Convert values into CUCM acceptable ones
			 * 
			 * For instance : "7962" into "cisco 7962" etc...
			 * LF Means "Look For"
			 * Example : *LFcss* or *LFphone*
			 **/
			if(Pattern.matches(".*\\*LF\\w+\\*.*", param))
				{
				//We extract the substitute type
				String substitute = null;
				Pattern p = Pattern.compile("\\*LF\\w+\\*");
				Matcher m = p.matcher(param);
				
				if(m.find())
					{
					String str = m.group();
					str = str.replace("*LF", "").replace("*", "");
					substitute = str;
					}
				
				if(substitute != null)
					{
					newValue = UsefulMethod.findSubstitute(newValue, SubstituteType.valueOf(substitute));
					}
				}
			/**
			 * End Convert values into CUCM acceptable ones
			 ****************/
			
			
			/**************************************/
			return newValue;
			}
		catch(Exception exc)
			{
			throw new Exception("An issue occured while applying the regex : "+exc.getMessage());
			}
		}
	
	/**
	 * Method used to return a number present in a regex
	 * 
	 * for instance : *1M* return 1
	 */
	private static int howMany(String regex, String param) throws Exception
		{
		Pattern p = Pattern.compile(regex);
		Pattern pChiffre = Pattern.compile("\\d+");
		Matcher m = p.matcher(param);
		
		if(m.find())
			{
			Matcher mChiffre = pChiffre.matcher(m.group());
			if(mChiffre.find())
				{
				return Integer.parseInt(mChiffre.group());
				}
			}
		return 0;
		}
	
	/**
	 * Method used to find and return 
	 * Character used to split
	 */
	private static String getSplitter(String regex, String param) throws Exception
		{
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(param);
		
		if(m.find())
			{
			String temp = m.group().replace("*", "");
			return temp.split("S")[1];
			}
		throw new Exception();
		}
	
	/**
	 * Method which return as an integer array the sheet, column and row number
	 * of a specified matcher
	 * 
	 * tab[0] : sheet number
	 * tab[1] : column number
	 * tab[2] : row number
	 * @throws Exception 
	 */
	public static int[] getMatcherInfo(String matcher) throws Exception
		{
		int[] matcherInfos = new int[3];  
		
		try
			{
			for(String s : Variables.getMatcherList())
				{
				String[] tab = s.split(":");//As a reminder a matcher is : cucm.firstname:4:4:4+*
				if(tab[0].equals(matcher))
					{
					matcherInfos[0] = Integer.parseInt(tab[1]);//Sheet number
					matcherInfos[1] = Integer.parseInt(tab[2]);//Column number
					
					//Row number
					if(tab[3].contains("-"))
						{
						String[] row = tab[3].split("-");
						matcherInfos[2] = Integer.parseInt(row[0]);
						}
					else
						{
						matcherInfos[1] = Integer.parseInt(tab[3]);
						}
					
					return matcherInfos;
					}
				}
			}
		catch (Exception e)
			{
			e.printStackTrace();
			throw new Exception("Error while looking for a matcher infos");
			}
		
		throw new Exception("No matcher found");
		}
	
	/**
	 * Used to split a value while using the escape character "\"
	 * @param pat
	 * @param splitter
	 * @return
	 */
	private static String[] getSplittedValue(String pat, String splitter)
		{
		pat = pat.replace("'", "");
		String splitRegex = "(?<!\\\\)" + Pattern.quote(splitter);//To activate "\" as an escape character
		
		String[] tab = pat.split(splitRegex);
		//We now remove the remaining \
		for(int i=0; i<tab.length; i++)
			{
			if(tab[i].contains("\\\\"))
				{
				//to keep one \ when it has been escaped \\
				tab[i] = tab[i].replace("\\\\", "\\");
				}
			else if(tab[i].contains("\\"))
				{
				tab[i] = tab[i].replace("\\", "");
				}
			}
		
		return tab;
		}
	
	public static String resolveDeviceValue(Device d, String pattern) throws Exception
		{
		StringBuffer regex = new StringBuffer("");
		String[] param = getSplittedValue(pattern, UsefulMethod.getTargetOption("splitter"));
		
		for(int i = 0; i<param.length; i++)
			{
			String value = null;
			String pat = null;
			
			if(param[i].contains("*"))
				{
				String[] rornot = param[i].split("\\*");
				pat = rornot[rornot.length-1];//To remove the regex and keep the pattern
				}
			else
				{
				pat = param[i];
				}
			
			if(Pattern.matches(".*device\\..*", pat))
				{
				value = d.getString(pat);
				}
			else if(Pattern.matches(".*office\\..*", pat))
				{
				value = UsefulMethod.getOffice(d.getOfficeid()).getString(pat);
				}
			else if(Pattern.matches(".*config\\..*", pat))
				{
				String[] tab = pat.split("\\.");
				value = UsefulMethod.getTargetOption(tab[1]);
				}
						
			if(value != null)
				{
				if(param[i].contains("*"))
					{
					//We apply regex
					Variables.getLogger().debug("Value before "+param[i]+" regex : "+value);
					value = applyRegex(value, param[i]);
					Variables.getLogger().debug("Value after applying "+param[i]+" regex : "+value);
					}
				regex.append(value);
				}
			else
				{
				regex.append(param[i]);
				}
			}
	
		return regex.toString();
		}
	
	public static String resolveOfficeValue(Office o, String pattern) throws Exception
		{
		StringBuffer regex = new StringBuffer("");
		String[] param = getSplittedValue(pattern, UsefulMethod.getTargetOption("splitter"));
		
		for(int i = 0; i<param.length; i++)
			{
			String value = null;
			String pat = null;
			
			if(param[i].contains("*"))
				{
				String[] rornot = param[i].split("\\*");
				pat = rornot[rornot.length-1];//To remove the regex and keep the pattern
				}
			else
				{
				pat = param[i];
				}
			
			if(Pattern.matches(".*office\\..*", pat))
				{
				value = o.getString(pat);
				}
			else if(Pattern.matches(".*config\\..*", pat))
				{
				String[] tab = pat.split("\\.");
				value = UsefulMethod.getTargetOption(tab[1]);
				}
			
			if(value != null)
				{
				if(param[i].contains("*"))
					{
					//We apply regex
					Variables.getLogger().debug("Value before "+param[i]+" regex : "+value);
					value = applyRegex(value, param[i]);
					Variables.getLogger().debug("Value after applying "+param[i]+" regex : "+value);
					}
				regex.append(value);
				}			
			else
				{
				regex.append(param[i]);
				}
			}
	
		return regex.toString();
		}
	
	/**
	 * Used to return what match the given regex
	 * here we use common java regex instead of my simplified version
	 */
	public synchronized static String resolveRegex(String content, String regex)
		{
		Pattern begin = Pattern.compile(regex);
		Matcher mBegin = begin.matcher(content);
		
		if(mBegin.find())
			{
			return mBegin.group();
			}
		
		//No match found
		return content;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

