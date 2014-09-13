/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtConfigParser.java
 * Language:  Java
 * Date:      Apr 27, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.igstk.validation.IgstkvtConfigProps;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Janakiram Dandibhotla
 * @version
 * @since jdk1.5
 */
public class IgstkvtConfigParser {

	private static final String IGSTKVT_HOME_DEFAULT = ".";
    // These are the variables built from the vtconfig.xml file.
    private static IgstkvtConfigProps configProps;
    private String xmlFile;
    private static String IGSTKVT_HOME;
    private static String IGSTKVT_SCXML_DIR;
    private static String IGSTKVT_SENDEVENT_DIR;
    static Logger log = Logger.getLogger(IgstkvtConfigParser.class.getName());


    /**
     * Constructor which initializes the variables.
     * @throws IgstkvtFatalConfigException
     */
    private IgstkvtConfigParser(String xmlPropertiesFile)
    	throws IgstkvtFatalConfigException{
    	xmlFile = xmlPropertiesFile;
    	IGSTKVT_HOME = System.getenv("IGSTKVT_HOME");
        if(IGSTKVT_HOME == null){
        	IGSTKVT_HOME = IGSTKVT_HOME_DEFAULT;
            log.warn("IGSTKVT_HOME not set, using " + IGSTKVT_HOME_DEFAULT + " as the default");
        }
        File file = new File(IGSTKVT_HOME);

        if(!file.isDirectory()){
            throw new IgstkvtFatalConfigException(
                    "IGSTKVT_HOME doesn't point to a directory.");
        }

        IGSTKVT_HOME = appendSlash(IGSTKVT_HOME);

        IGSTKVT_SCXML_DIR = System.getenv("IGSTKVT_SCXML_DIR");
        if(IGSTKVT_SCXML_DIR != null){
            IGSTKVT_SCXML_DIR = appendSlash(IGSTKVT_SCXML_DIR);
            file = new File(IGSTKVT_SCXML_DIR);
            if(!file.isDirectory()){
                log.warn("IGSTKVT_SCXML_DIR doesn't point to a directory");
                IGSTKVT_SCXML_DIR = null;
            }
        }

        IGSTKVT_SENDEVENT_DIR = System.getenv("IGSTKVT_SENDEVENT_DIR");
        if(IGSTKVT_SENDEVENT_DIR != null){
            IGSTKVT_SENDEVENT_DIR = appendSlash(IGSTKVT_SENDEVENT_DIR);
            file = new File(IGSTKVT_SENDEVENT_DIR);
            if(!file.isDirectory()){
                log.warn("IGSTKVT_SENDEVENT_DIR doesn't point to a directory");
                IGSTKVT_SENDEVENT_DIR = null;
            }
        }
    }

    //One instance per configuration file.
    private static HashMap<String,IgstkvtConfigParser> instancesList =
    	new HashMap<String, IgstkvtConfigParser>();

    /**
     * Method to create an instance of config parser.
     * @param xmlPropertiesFile
     * @return
     * @throws IgstkvtFatalConfigException
     */
    public static IgstkvtConfigParser getInstance(String xmlPropertiesFile)
    	throws IgstkvtFatalConfigException{
    	if(instancesList.get(xmlPropertiesFile) != null){
    		return instancesList.get(xmlPropertiesFile);
    	}else {
    		IgstkvtConfigParser parser = new IgstkvtConfigParser(
    				xmlPropertiesFile);
    		instancesList.put(xmlPropertiesFile, parser);
    		return parser;
    	}
    }

    /**
     * This method parses the XML properties file to get the
     * required state machine id's and match them accordingly with
     * the state machines given.
     * @param xmlFile
     * @throws IgstkvtFatalConfigException
     */
    private void parseXMLandCreateConfigProps()
        throws IgstkvtConfigurationException, IgstkvtFatalConfigException{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream inStream = IgstkvtConfigParser.
                                class.getResourceAsStream("/" +xmlFile);
            Document dom = db.parse(inStream);
            String scxmlFilePath = null;
            String sendEventsPath = null;
            if(IGSTKVT_SCXML_DIR != null){
                scxmlFilePath = IGSTKVT_SCXML_DIR;
            }else {
                scxmlFilePath = IGSTKVT_HOME + "/xmlFiles/scxmlFiles";
            }

            if(IGSTKVT_SENDEVENT_DIR != null){
                sendEventsPath = IGSTKVT_SENDEVENT_DIR;
            }else {
                sendEventsPath = IGSTKVT_HOME + "/xmlFiles/coverage";
            }


            scxmlFilePath = appendSlash(scxmlFilePath);
            sendEventsPath = appendSlash(sendEventsPath);

            log.info("The scxmlFile Path is : " + scxmlFilePath);
            log.info("The sendEventsPath Path is : "
                    + sendEventsPath);

            NodeList stateMachineNodes = dom
                            .getElementsByTagName("state-machine"); // (NodeList)
            String id;
            String file = null;
            String name = null;
            String sendEventFile = null;
            List<String> scxmlFiles = new ArrayList<String>();
            List<String> stateMachineNames = new ArrayList<String>();
            List<String> stateMachineIds = new ArrayList<String>();
            List<String> sendEventFiles = new ArrayList<String>();
            List<String> droolFiles = new ArrayList<String>();
            configProps = new IgstkvtConfigProps();
            
            NodeList droolFilesNodes = dom
            .getElementsByTagName("drool-files").item(0).getChildNodes(); // (NodeList)
           // System.out.println("The number of droolfile nodes are "+droolFilesNodes.getLength());
            for (int i=0;i < droolFilesNodes.getLength(); i++){
            	file = droolFilesNodes.item(i).getTextContent().trim();
            //	System.out.println("The drool file is "+file);
            	if (file !=null && file.length()!=0){
            		droolFiles.add(file);
            	}
            }
            //System.out.println(droolFiles.size());

            for (int i = 0; i < stateMachineNodes.getLength(); i++) {
                id = stateMachineNodes.item(i).getAttributes()
                        .getNamedItem("id").getTextContent();
                file = "" + stateMachineNodes.item(i).getChildNodes().
                                item(1).getTextContent().trim();
                sendEventFile = "" + stateMachineNodes.item(i).getChildNodes().
                                item(3).getTextContent().trim();
                name = stateMachineNodes.item(i).getAttributes()
                        .getNamedItem("name").getTextContent();
                log.info("The file name is : " + file);

                scxmlFiles.add(scxmlFilePath + file);
                stateMachineNames.add(name);
                stateMachineIds.add(id);
                sendEventFiles.add(sendEventsPath + sendEventFile);
                log.info("The sendevents File is : "
                                + sendEventsPath + sendEventFile);
                log.info("The id is : " + id);
                log.info("The name is : " + name);
            }
            configProps.setScxmlFiles(scxmlFiles);
            configProps.setSendEventFiles(sendEventFiles);
            configProps.setStateMachineIds(stateMachineIds);
            configProps.setStateMachineNames(stateMachineNames);
            configProps.setDroolFiles(droolFiles);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            throw new IgstkvtConfigurationException(
                        "ParserConfiguration Exception was thrown!",pce);
        } catch (SAXException se) {
            se.printStackTrace();
            throw new IgstkvtConfigurationException(
                            "SAX Exception was thrown!", se);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IgstkvtConfigurationException(
                        "IOException was thrown!" , ioe);
        } catch (Exception e){
            e.printStackTrace();
            throw new IgstkvtConfigurationException("Exception was thrown!" , e);
        }
    }

    /**
     * @param scxmlFilePath
     */
    private static String appendSlash(String path) {
        if(path.endsWith("/")){
            return path;
        }else {
            return path + "/";
        }
    }

    /**
     * Getter for Config properties.
     * @return
     * @throws IgstkvtFatalConfigException
     * @throws IgstkvtConfigurationException
     */
    public IgstkvtConfigProps getConfigProps()
    	throws IgstkvtFatalConfigException, IgstkvtConfigurationException {
        if(configProps == null){
        	parseXMLandCreateConfigProps();
        }
        return configProps;
    }

    /**
     * Getter for IGSTKVT_HOME env variable.
     * @return
     * @throws IgstkvtConfigurationException
     */
    public String getIGSTKVT_HOME(){
        return IGSTKVT_HOME;
    }

    /**
     * Getter for IGSTKVT_SCXML_DIR env variable.
     * @return
     * @throws IgstkvtConfigurationException
     */
    public String getIGSTKVT_SCXML_DIR(){
        return IGSTKVT_SCXML_DIR;
    }

    /**
     * Getter for IGSTKVT_SENDEVENT_DIR env variable.
     * @return
     * @throws IgstkvtConfigurationException
     */
    public String getIGSTKVT_SENDEVENT_DIR(){
        return IGSTKVT_SENDEVENT_DIR;
    }

    /**
     * Method to get the properties file name.
     * @param igstkvtPropertiesFile
     * @return
     * @throws IgstkvtConfigurationException
     */
    public static String getPropertiesFileNameFromPropertiesFile(
    		String igstkvtPropertiesFile)
    	throws IgstkvtConfigurationException{
    	String xmlFile = null;

        try{
            InputStream is = IgstkvtConfigParser.class.
                            getResourceAsStream("/" + igstkvtPropertiesFile);
            if(is == null){
                throw new IOException(
                		"Could Not locate" +igstkvtPropertiesFile + " file.");
            }
            Properties properties = new Properties();
            properties.load(is);
            xmlFile = properties.getProperty("vtconfigFile");
            log.info("The vtconfigFile got is: " + xmlFile);
            return xmlFile;
        }catch (IOException e){
            e.printStackTrace();
            throw new IgstkvtConfigurationException(
                    "Error reading igstkvt.properties",e);
        }
    }
}
