/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSCXMLParser.java
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

package org.igstk.validation.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.igstk.validation.IgstkvtState;
import org.igstk.validation.IgstkvtStateMachine;
import org.igstk.validation.IgstkvtTransition;
import org.igstk.validation.exception.IgstkSCXMLParserException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * SCXML file parser.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSCXMLParser {


    /**
     * Parses the scxml file.
     * @return IgstkvtStateMachine
     * @throws Exception
     */
    public static IgstkvtStateMachine parseSCXMLFile(String scxmlFilePath)
        throws IgstkSCXMLParserException {

    	if(scxmlFilePath == null){
    		throw new IgstkSCXMLParserException("scxmlFilepath is null.");
    	}
    	Reader scxmlReader = null;
    	InputSource ip = null;
    	try {
	    	scxmlReader = new FileReader(scxmlFilePath);
			ip = new InputSource(scxmlReader);
			SCXML scxml = SCXMLDigester.digest(ip, null);
			IgstkvtStateMachine igstkvtStateMachine =
			        getIgstkvtStateMachine(scxml);
			igstkvtStateMachine.setName(new File(scxmlFilePath).getName());
	        return igstkvtStateMachine;
    	}catch(FileNotFoundException fnfe){
    	    fnfe.printStackTrace();
    	    throw new IgstkSCXMLParserException(
    	                "FileNotFoundException thrown." , fnfe);
    	}catch(IOException e){
    	    e.printStackTrace();
    	    throw new IgstkSCXMLParserException("IOException thrown.", e);
    	}catch (SAXException e){
    	    e.printStackTrace();
    	    throw new IgstkSCXMLParserException("SAXException thrown." ,e);
    	}catch (Exception e){
    	    e.printStackTrace();
    	    throw new IgstkSCXMLParserException("Exception occurred." ,e);
    	}finally {
    		if(scxmlReader != null) {
    			try {
                    scxmlReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IgstkSCXMLParserException(
                                "IOException thrown." ,e);
                }
    		}
    	}
    }

    /**
     * Method to create a state machine from the SCXML Object.
     * @param scxml
     * @return
     */
    public static IgstkvtStateMachine getIgstkvtStateMachine(SCXML scxml) {

    	IgstkvtStateMachine igstkvtStateMachine = new IgstkvtStateMachine();

    	for(Object o : scxml.getStates().values()) {
    		State state = (State)o;
    		IgstkvtState igstkvtState = new IgstkvtState(state.getId());
    		igstkvtStateMachine.addState(igstkvtState);
    	}

    	for(Object o : scxml.getStates().values()) {
    		State state = (State)o;
    		IgstkvtState igstkvtState = igstkvtStateMachine.
    		        getState(state.getId());

    		for(Object ob : state.getTransitionsList()) {
    			Transition transition = (Transition)ob;
    			igstkvtState.addTransitionEvent(transition.getEvent());
    			IgstkvtTransition igstkvtTransition =
    				new IgstkvtTransition(transition.getEvent());

    			igstkvtTransition.setStartState(igstkvtStateMachine.getState(
    					transition.getParent().getId()));

    			igstkvtTransition.setEndState(igstkvtStateMachine.getState(
    					transition.getTarget().getId()));

    			igstkvtStateMachine.addTransition(igstkvtTransition);
    		}
    	}
    	igstkvtStateMachine.setInitialState(igstkvtStateMachine.getState(
    			scxml.getInitialstate()));

    	return igstkvtStateMachine;
    }
}
