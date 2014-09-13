/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtMain.java
 * Language:  Java
 * Date:      Mar 18, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.generator.coverage;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtMain {
	private String sendEventsCreatedFileName;
	
	static Logger log = Logger.getLogger(IgstkvtMain.class);

    /**
     * Getter for Send Events Created file name.
     * @return
     */
    public String getSendEventsCreatedFileName() {
        return sendEventsCreatedFileName;
    }

    /**
     * Setter for send events created file name.
     * @param sendEventsCreatedFileName
     */
    public void setSendEventsCreatedFileName(String sendEventsCreatedFileName) {
        this.sendEventsCreatedFileName = sendEventsCreatedFileName;
    }

    /**
     * Method to parse the Document.
     * @param coveragetype
     * @param xmlFile
     * @param sendEventsDirectory
     */
    private void parseDocument(String coveragetype, String xmlFile,
    		String sendEventsDirectory, String heuristicFile) {

    	IgstkvtGraph pGraph = new IgstkvtGraph();
    	SAXParserFactory factory = SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser parser = null;
        // Set up output stream
        try {
            // Parse the input
            parser = factory.newSAXParser();
        } catch (SAXException s) {
            s.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (coveragetype.equals("nodecoverage")){
        	pGraph = new IgstkvtStatementCoverage();
        }else if (coveragetype.equals("edgecoverage")){
        	pGraph = new IgstkvtTransitionCoverage();
        }

        IgstkvtSAXHandler handler = new IgstkvtSAXHandler(coveragetype,pGraph);
        int endPosition = 0;
        String fileName = xmlFile.substring(xmlFile.lastIndexOf("/") + 1);
        endPosition = fileName.indexOf(".");

        String componentName = fileName.substring(0, endPosition);
        handler.setSendEventsDirectory(sendEventsDirectory);

        handler.setComponentName(componentName);
        if (!heuristicFile.equals("")){
        	handler.setheuristicFileName(heuristicFile);
        }

        try {

            // The scxml file is parsed twice. The first time a vertex array
            // is created which helps determine the number of vertices(N) in the
            // IgstkvtGraph being created. During the second pass a 2D-array of
            // size
            // N*N
            // is created and populated with the edge information
            handler.setParseType(1);
            parser.parse(xmlFile, handler);
            handler.setParseType(2);
            parser.parse(xmlFile, handler);
            setSendEventsCreatedFileName(handler.getFilename());



        } catch (SAXParseException e) {
        	log.error("Exception message is: \n" + e.getMessage() + "\n");
        	System.exit(0);
        } catch (SAXException se) {
        	log.error("Exception message is: \n" + se.getMessage() + "\n");
        	System.exit(0);
        } catch (IOException e) {
    	   	log.error("Check whether the specified files are available or not"+ "\n");
    	   	System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Method to print out the usage.
     *
     */
    public static void usage() {
    	System.out.println("Usage: java igstk.coverage.Main <nodecoverage|" 
    			+"edgecoverage> "
    			+"<scxmlfilename> <outputDirectory> "
    			+"\n");
    	System.exit(1);
    }

    /**
     * IgstkvtMain function.
     * @param args
     *
     */
    public static void main(String[] args) {
        if (args.length < 3 || args[0]==null
        		|| args[1] ==null || args[2]==null) {
            usage();
        }
        IgstkvtMain main = new IgstkvtMain();
        String coverageType = null;
        String xmlFile = null;
        String sendEventsDirectory=".",heuristicFile="";
        if (args[0].length()>0 && !args[0].equalsIgnoreCase("")) {
            coverageType = args[0];
        }
        if (args[1].length()>0 && !args[1].equalsIgnoreCase("")) {
            xmlFile = args[1];
        }
        if (args[2].length()>0 && !args[2].equalsIgnoreCase("")) {
            sendEventsDirectory = args[2];
        }
        
        try{
        if (coverageType !=null && xmlFile !=null 
        		&& coverageType.length()>0 && !coverageType.equalsIgnoreCase("")
        		&& xmlFile.length()>0 && !xmlFile.equalsIgnoreCase("")) {
            main.parseDocument(coverageType, xmlFile,
            		sendEventsDirectory,heuristicFile);
        } else {
            IgstkvtMain.usage();
        }
        }catch(Exception e){}

    }
}
