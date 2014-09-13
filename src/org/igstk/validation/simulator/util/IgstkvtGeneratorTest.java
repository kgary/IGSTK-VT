/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGeneratorTest.java
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

package org.igstk.validation.simulator.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.igstk.validation.IgstkvtConfigProps;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.generator.IgstkvtSendEvent;
import org.igstk.validation.simulator.rules.IgstkvtMergeList;
import org.igstk.validation.util.IgstkvtConfigParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * This class is a test class for the Proxy generator.
 * This will be removed once we have the Proxy generator framework ready.
 * @author Janakiram Dandibhotla
 * @version
 * @since jdk1.5
 */
public class IgstkvtGeneratorTest {

    private IgstkvtConfigProps configProps =
                    new IgstkvtConfigProps();
    static Logger log = Logger.getLogger(IgstkvtGeneratorTest.class);
    private ArrayList<ArrayList<IgstkvtSendEvent>> eventsListOfAllStateMachines;

    private ArrayList<ArrayList<IgstkvtSendEvent>> finalEventsList;

    private ArrayList<IgstkvtSendEvent> finalEventListTakenToTest;

    int count;

    int noOfEvents;
    String seFile1;
    String seFile2;

    String id1 = "1";
    String id2 = "2";

    /**
     * This method prints the test data.
     */
    public void printTestData() {
        for (int i = 0; i < finalEventListTakenToTest.size(); i++) {
            System.out.println("The Event is : "
                    + ((IgstkvtSendEvent) finalEventListTakenToTest.get(i))
                            .getEventName());
            System.out.println("The Event is : "
                    + ((IgstkvtSendEvent) finalEventListTakenToTest.get(i))
                            .getId());

        }
    }

    /**
     * This method initializes the properties required by this test generator.
     * @throws IgstkvtConfigurationException
     * @throws IgstkvtFatalConfigException
     */
    public void init()
    	throws IgstkvtFatalConfigException, IgstkvtConfigurationException {
    	IgstkvtConfigParser configParser = IgstkvtConfigParser.getInstance(
        		IgstkvtConfigParser.getPropertiesFileNameFromPropertiesFile(
        				"igstkvt.properties"));
    	configProps = configParser.getConfigProps();
        eventsListOfAllStateMachines =
                new ArrayList<ArrayList<IgstkvtSendEvent>>();
        seFile1 = configProps.getSendEventFiles().get(0);
        seFile2 = configProps.getSendEventFiles().get(1);


        eventsListOfAllStateMachines.add(generateSendEvents(seFile1, id1));

        log.info("Size of generateSendEvents1 : "
                + generateSendEvents(seFile1, id1).size());

        eventsListOfAllStateMachines.add(generateSendEvents(seFile2, id2));
        createFinalEventsListToBeFired();
        // printTestData();
    }

    /**
     * This method is a getter for the next event.
     *
     * @return
     * @throws IgstkvtGeneratorException
     */
    public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException {

        if (count >= noOfEvents) {
            return null;
        } else {
            return finalEventListTakenToTest.get(count++);
        }
    }

    /**
     * This method rewinds the sequence of events.
     * @param n
     */
    public void rewind(int n){

        if(count >= n){
            count -= n;
        }
    }

    /**
     * This method chooses one list from multiple lists generated.
     */
    public void createFinalEventsListToBeFired() {
        IgstkvtMergeList mergeList = new IgstkvtMergeList();
        finalEventsList = new ArrayList<ArrayList<IgstkvtSendEvent>>();
        finalEventsList = mergeList.getAllList(eventsListOfAllStateMachines);
        log.info("Size of finalEventsList :" + finalEventsList.size());
        finalEventListTakenToTest = finalEventsList.get(5);
        if (finalEventListTakenToTest.size() <= 0) {
            log.error("Events List Empty");
            System.exit(1);
        }

        noOfEvents = finalEventListTakenToTest.size();
        log.warn("Size of noOfEvents: " + noOfEvents);
    }

    /**
     * This method generates the list of lists which are created by ordering the
     * events.
     * @param sendEventsFileName
     * @param stateMachineId
     * @return
     */
    public ArrayList<IgstkvtSendEvent> generateSendEvents(
            String sendEventsFileName, String stateMachineId) {
        try {
            log.info("The SE File name is : " + sendEventsFileName);
            InputSource source = new InputSource(new FileReader(
                    sendEventsFileName));
            Document doc = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.parse(source);
            Element root = doc.getDocumentElement();
            root.normalize();
            ArrayList<IgstkvtSendEvent> eventList =
                    new ArrayList<IgstkvtSendEvent>();
            walkFromNode(root, 0, eventList, stateMachineId);
            return eventList;
        } catch (FileNotFoundException e) {
            log.error("File : " + sendEventsFileName + " is not found!! \n"
                    + "Exiting...");
            e.printStackTrace();
            System.exit(1);
        } catch (ParserConfigurationException e) {
            log.error("Parser Not correctly configured \n" + "Exiting...");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log.error("An IO Exception Occured\n" + "Exiting...");
            e.printStackTrace();
            System.exit(1);
        } catch (SAXException e) {
            log.error("SAX Exception occured \n" + "Exiting...");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * This is a method used to check if the "send" tag is encountered in the
     * send events file.
     * @param n
     * @param depth
     * @param eventList
     * @param stateMachineId
     */
    private static void walkFromNode(Node n, int depth,
            ArrayList<IgstkvtSendEvent> eventList, String stateMachineId) {
        NodeList rootChildren = n.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node x = rootChildren.item(i);
            if (x.getNodeName().equals("send")) {
                int length = (x.getAttributes() != null) ? x.getAttributes()
                        .getLength() : 0;
                if (length > 0) {
                    eventList.add(new IgstkvtSendEvent(x.getAttributes()
                            .item(0).getNodeValue(), stateMachineId));
                }
            }
            walkFromNode(x, depth + 1, eventList, stateMachineId);
        }
    }

    /**
     * Getter for Config propeties.
     * @return
     */
    public IgstkvtConfigProps getConfigProps() {
        return configProps;
    }

    /**
     * Setter for Config Properties.
     * @param configProps
     */
    public void setConfigProps(IgstkvtConfigProps configProps) {
        this.configProps = configProps;
    }
}
