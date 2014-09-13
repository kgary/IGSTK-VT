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

package org.igstk.validation.simulator.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.SCXML;
import org.igstk.validation.IgstkvtConfigProps;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.simulator.IgstkvtStateMachineExecutor;
import org.xml.sax.SAXException;

/**
 * @author Janakiram Dandibhotla
 * @version
 * @since jdk1.5
 */
public class IgstkvtVMHelper {
    /**
     * This method creates the executors for each of the
     * state machines used.
     * @param configProps
     */
    public static Map<String, IgstkvtStateMachineExecutor> createExecutors(IgstkvtConfigProps configProps)
    	throws IgstkvtConfigurationException {

    	if(configProps == null){
    		throw new IgstkvtConfigurationException("ConfigProps "
    				+"object sent is null.");
    	}
        ArrayList<String> scxmlFiles = (ArrayList<String>)
        	configProps.getScxmlFiles();
        ArrayList<String> stateMachineNames = (ArrayList<String>)
        	configProps.getStateMachineNames();
        ArrayList<String> stateMachineIds = (ArrayList<String>)
        	configProps.getStateMachineIds();

        Map<String, IgstkvtStateMachineExecutor> executors = new
        	HashMap<String, IgstkvtStateMachineExecutor>();

        for (int i = 0; i < stateMachineNames.size(); i++) {
        	executors.put(stateMachineIds.get(i), 
            			createExecutor(scxmlFiles.get(i), 
            		  		  		   stateMachineIds.get(i),
            						   stateMachineNames.get(i)));
        }
        return executors;
    }
    
    public static IgstkvtStateMachineExecutor createExecutor(String filename, String id, String name) 
    	throws IgstkvtConfigurationException {
    	
        try {
        	File stateFile = new File(filename);
        	URL url = stateFile.toURL();
        	SCXML scxml = SCXMLDigester.digest(url, null);

        	return new IgstkvtStateMachineExecutor(id, name, scxml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IgstkvtConfigurationException(
                    "Malformed URL Exception",e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IgstkvtConfigurationException(
                    "IOException Occurred", e);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IgstkvtConfigurationException(
                    "SAXException Occurred",e);
        } catch (Exception e){
            e.printStackTrace();
            throw new IgstkvtConfigurationException("Exception occurred",e);
        }
    }
}
