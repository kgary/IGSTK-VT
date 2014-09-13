/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtProcessLogFile.java
 * Language:  Java
 * Date:      Nov 3, 2008
 * 
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 * 
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.      
 *************************************************************************/

package org.igstk.validation.generator.processLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Santhosh
 *
 */
public class IgstkvtProcessLogFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String outputDir = null, logfilename = null;

		if (args.length >= 1 && !args[0].equalsIgnoreCase("")) {
			logfilename = args[0];
			if (args.length >= 2) {
				outputDir = args[1];
			} else {
				outputDir = ".";
			}
		}else{
			System.out
				.println("<usage> processLogfile <filename> [output directory]");
			return;
		}
		IgstkvtProcessLogFile f = new IgstkvtProcessLogFile();
		try{
			f.createSendEventFile(logfilename, outputDir);
		}catch (IOException e){
			System.out.println("Please Verify whether the inputfile" 
					+" is present or not !!");
		}
	}

	/**
	 * This function will create File in the given directory.
	 * @param logFileName
	 * @param outputDir
	 * @throws IOException
	 */
	public void createSendEventFile(String logFileName, String outputDir)
		throws IOException{
		String componentname="";
		String filename;
		List<String> fileNameList= new ArrayList<String>();
		String transitionStr;
		int position;
		String[] tokens=null;
		File outFile = null;
		FileWriter log=null;
	    String transitionStrHeader ="(DEBUG) State transition is being made :";

	    File f = new File(logFileName);
		if (outputDir.equals("")) {
			outputDir = ".";
		}
		BufferedReader bin = new BufferedReader(new FileReader(f));
		String str = bin.readLine();
		while(str != null){
			position=str.indexOf(transitionStrHeader);
			tokens = null;
			if(position != -1){
				transitionStr = str.substring(position
						+transitionStrHeader.length()+1);
				tokens = transitionStr.split("\\s+");
			}
			if(tokens != null){
				String strd = tokens[0];
				if (componentname=="" || componentname == null){
					componentname = strd;
					filename = outputDir + "/igstk"+ strd 
						+"_"+tokens[2]+"_"+"test.xml";
					outFile = new File(filename);
					log = new FileWriter(outFile);
					try{
						writeHeaderInfo(logFileName, componentname,log, tokens);
					}catch (Exception e){e.printStackTrace();}
					fileNameList.add(filename);
				}else if(!componentname.equalsIgnoreCase(strd)){
					//close the existing file & open new component file
					log.close();					
					componentname = strd;
					filename = outputDir + "/igstk"+ strd +"_"
						+tokens[2]+"_"+"test.xml";
					if (!fileNameList.contains(filename))  {
						outFile = new File(filename);
						log = new FileWriter(outFile);
						try{
							writeHeaderInfo(logFileName, componentname,
										log,tokens);
						}catch (Exception e){e.printStackTrace();}
						fileNameList.add(filename);
					}else{
						outFile = new File(filename);
						log = new FileWriter(outFile,true);
					}	
				}
				writeToFile(log,tokens);
			}
			str = bin.readLine();
		}
		log.close();
		writeEndTag(fileNameList);
		
	}	

/**
 * Writes the Header Info for the file.
 * @param logFileName
 * @param componentName
 * @param log
 * @param tokens
 * @throws Exception
 */
	private void writeHeaderInfo(String logFileName, String componentName, 
		FileWriter log,String[] tokens)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
		Date date = new Date(System.currentTimeMillis());
		log.write("<?xml version=\"1.0\"?> \n");
		log.write("<igstk> \n");
		log.write("    <metadata> \n");
		log.write("\t	LogFile Name: \""+logFileName+"\" \n");
		log.write("\t	Component Name: \""+componentName+"\" \n");
		log.write("\t	Component Instance: \""+tokens[2]+"\"\n");
    	log.write("\t	Time: " + sdf.format(date).toString() + "\n");
    	log.write("    </metadata> \n\n");
    	log.write("<events>\n");
	}

/**
 * Writes the content to the file.
 * @param log
 * @param tokens
 * @throws IOException
 */
	private void writeToFile(FileWriter log, String[] tokens)throws IOException{
		log.write("\t<send event=\""+tokens[5].substring(0,
				tokens[5].indexOf('('))+"\"/>\n");
	}

/**
 * Writes the end tags to the file.
 * @param fileNamesList
 * @throws IOException
 */
	private void writeEndTag(List<String> fileNamesList)throws IOException{
		for (String filename: fileNamesList){
			File f = new File(filename);
			FileWriter log = new FileWriter(f,true);
			log.write("</events> \n");
			log.write("</igstk> \n");
			log.flush();
			System.out.println("CREATED: "+filename);
		}
	}

}

	
