/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSAXHandler.java
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

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Sany
 *
 */
public class IgstkvtSAXHandler extends DefaultHandler {

    /**
     * @return
     */
    int getElementCount() {
        return elementCount;
    }

    int elementCount; //
    int parseType; // Specifies first pass or second pass
    // of the scxml file
    String state;
    String initialState; // Stores root node of the IgstkvtGraph
    String transition; // transition name
    String target; // target name
    String componentName;
    private String filename;
    private String sendEventsDirectory;
    String coverageType, heuristicFilename;
    // Contains the value for coverage type.
    // Value set from the main function
    int pathHeuristics;
    int componentFound,combination;
    IgstkvtGraph pGraph,pathP;
    String heuristicEvent,heuristicSource,heuristicTarget;
    StringBuffer heuristics_text;

    static Logger log = Logger.getLogger(IgstkvtSAXHandler.class);

    /**
     * @param coveragetype
     */
    public IgstkvtSAXHandler(IgstkvtGraph pathcoverage){
    	pathHeuristics =0;
    	combination =0;
    	pathP= pathcoverage;

    }


    /**
     * Constructor.
     * @param coveragetype
     * @param coverageGraph
     */
    public IgstkvtSAXHandler(String coveragetype, IgstkvtGraph coverageGraph) {
        elementCount = 0;
        parseType = 0;
        coverageType = coveragetype;
        pathHeuristics=0;
        componentFound =0;
        pGraph = coverageGraph;

        // based on coveragetype specified from the command prompt create an
        // object
        // for one of the derived classes
        // IgstkvtStatementCoverage,IgstkvtTransitionCoverage

    }

    /**
     * Setter method for parse type.
     * @param type
     */
    public void setParseType(int type) {
        parseType = type;
    }

    /**
     * Setter for Component name.
     * @param name
     */
    public void setComponentName(String name) {
        componentName = name;
    }

    /**
     * Setter for heuristic file name.
     * @param heuristicFileName
     */
    public void setheuristicFileName(String heuristicFileName){
    	heuristicFilename= heuristicFileName;

    }
    /**
     * This method is the overridden method from DefaultHandler.
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        if (parseType ==1){
        	pGraph.initializeNodeArray();
        }

        if (parseType == 2) {
            pGraph.initializeEdgeArray(elementCount);
        }
        heuristics_text= new StringBuffer();
        componentFound =0;

    }

    /**
     * This method is the overridden method from DefaultHandler.
     * @param namespaceURI
     * @param sName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String namespaceURI, String sName, // simple name
            // (localName)
            String qName, // qualified name
            Attributes attributes) throws SAXException, NullPointerException {

        IgstkvtVertex pVertex = new IgstkvtVertex();
        String eName = sName; // element name
        String attr;
        if ("".equals(eName)){
            eName = qName;
        }

        /**********************************************************/
    	//Heuristics
    	if(componentFound ==1){
    		if (attributes.getLength()==0){
    			heuristics_text.append("  <"+ eName +">\n");
    		}else{
    			heuristics_text.append("  <"+ eName+" ");
    			for (int i=0;i < attributes.getLength();i++){
    				heuristics_text.append(attributes.getQName(i)+ " = ");
    				heuristics_text.append("\"" +attributes.getValue(i)+ "\"");

    			}
    			heuristics_text.append(">\n");
    		}
    		if(eName.equals("combination")){
    			combination = 1;
    		}
    		if(eName.equals("selection")){
    			attr = attributes.getValue("criteria");
    		}
    		if(eName.equals("maxTraversal") && componentFound ==1){
    			for (int i=0;i < attributes.getLength();i++){
    				if("count".equals(attributes.getQName(i))){
    					int count = Integer.parseInt(attributes.
    							getValue("count"));
    					heuristicEvent = "";
    					heuristicSource = "";
    				}
    			}
    		}
    		if(eName.equals("exception_input")){
    			for (int i=0;i < attributes.getLength();i++){
    				if("name".equals(attributes.getQName(i))){
    					attr = attributes.getValue("name");
    				}
    			}
    		}
    		if(eName.equals("include_state")){
    			for (int i=0;i < attributes.getLength();i++){
    				if("name".equals(attributes.getQName(i))){
    					attr = attributes.getValue("name");
    				}
    			}
    		}
    		if(eName.equals("exclude_state")){
    			for (int i=0;i < attributes.getLength();i++){
    				if("name".equals(attributes.getQName(i))){
    					attr = attributes.getValue("name");
    				}
    			}
    		}
    		if(eName.equals("include_transition")){
    			for (int i=0;i < attributes.getLength();i++){
    				if("source".equals(attributes.getQName(i))){
    					heuristicSource = attributes.getValue("source");
    				}
    				if("event".equals(attributes.getQName(i))){
    					heuristicEvent = attributes.getValue("event");
    				}
    			}
    			heuristicEvent = "";
    			heuristicSource = "";
    		}
    		if(eName.equals("exclude_transition")){
    			for (int i=0;i < attributes.getLength();i++){
    				if("source".equals(attributes.getQName(i))){
    					heuristicSource = attributes.getValue("source");
    				}

    				if("event".equals(attributes.getQName(i))){
    					heuristicEvent =attributes.getValue("event");
    				}
    			}
    			heuristicEvent = "";
    			heuristicSource = "";
    		}
    	}
    	if(eName.equals("component")){
    		for (int i=0;i < attributes.getLength();i++){
    			if("name".equals((attributes.getQName(i)))){
    				attr =attributes.getValue("name");
    				if (componentName.equals(attr)){
    					componentFound = 1;
    					heuristics_text.append("\n");
    					heuristics_text.append("<heuristics> \n");
    				}else{
    					componentFound = 0;
    				}
    			}
    			if(componentFound ==1){
    				if("entry".equals(attributes.getQName(i))){
    					attr = attributes.getValue("entry");
    				}
    				if("exit".equals(attributes.getQName(i))){
    					attr = attributes.getValue("exit");
    				}
    			}
    		}
        }
    	if(eName.equals("path_heuristics")){
    		pathHeuristics = 1;
    	}


        /*********************************************************/

        // If tag is "scxml" then the initialstate is an attribute
        if (eName.equals("scxml")) {
            initialState = attributes.getValue("initialstate");
            pGraph.setInitialVertex(initialState);
        }
        // If tag is "state" then add to vertex vector
        if (eName.equals("state")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                state = attributes.getValue(i); // Attr name
                if ("".equals(state)){
                    state = attributes.getQName(i);
                }

                if (parseType == 1) {
                    pVertex.setName(state);
                    pGraph.addNode(pVertex);
                    elementCount++;
                }
            }
        }
        // If second pass of SCXML file then populate IgstkvtTransition array
        if (parseType != 1) {

            if (eName.equals("transition")) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    transition = attributes.getValue(i); // Attr name
                    if ("".equals(transition)){
                        transition = attributes.getQName(i);
                    }

                }
            }
            if (eName.equals("target")) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    target = attributes.getValue(i); // Attr name
                    if ("".equals(target)){
                        target = attributes.getQName(i);
                    }

                    int row = pGraph.findNode(state);
                    int col = pGraph.findNode(target);
                    pGraph.addEdge(row, col, transition);
                }
            }
        }

    }

    /**
     * This method is the overridden method from DefaultHandler.
     * @throws SAXException
     *
     */
    @Override
    public void endDocument() throws SAXException {
        // After parsing is done traverse the graph and create IgstkvtSendEvent
        // file
        if (parseType != 1) {
            // based on coverage type call the writeToFile function to write the
            // events information into the send events file
            try {
                if (getSendEventsDirectory() == null) {
                    setSendEventsDirectory(".");
                }
                if (coverageType.equals("nodecoverage")) {
                    ((IgstkvtStatementCoverage) pGraph).writeToFile(
                            componentName, sendEventsDirectory);
                } else if (coverageType.equals("edgecoverage")) {
                    ((IgstkvtTransitionCoverage) pGraph).writeToFile(
                            componentName, sendEventsDirectory);
                }

                setFilename(pGraph.getFilename());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    // -------------------------------------------------------------------------
    // IgstkvtSAXHandler : Overrides of the SAX ErrorHandler interface
    // -------------------------------------------------------------------------

    /**
     * This method is the overridden method from DefaultHandler.
     * @param exception
     * @throws SAXException
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        String message = exception.getMessage();
        log.fatal("Fatal Error: " + message + " at line: "
                + exception.getLineNumber());
    }

    /**
     * Setter for file name.
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter for file name.
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter for Send events directory.
     * @param sendEventsDirectory
     */
    public void setSendEventsDirectory(String sendEventsDirectory) {
        this.sendEventsDirectory = sendEventsDirectory;
    }

    /**
     * Getter for send events directory.
     * @return
     */
    public String getSendEventsDirectory() {
        return sendEventsDirectory;
    }
}
