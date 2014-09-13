/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    IGSTK Validation Tools
  Language:  Java
  Date:      Feb 18, 2008
  Version:

  Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

package org.igstk.validation.generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author kgary
 *
 */
public class IgstkvtBasicPrecomputedGeneratorFactory implements
        IgstkvtGeneratorFactoryInterface {

    private static IgstkvtGeneratorFactoryInterface theFactory;

    /**
     * @return
     */
    public static IgstkvtGeneratorFactoryInterface getGeneratorFactory() {
        if (theFactory == null) {
            theFactory = new IgstkvtBasicPrecomputedGeneratorFactory();
        }
        return theFactory;
    }

    /**
     *
     */
    private IgstkvtBasicPrecomputedGeneratorFactory() {
    }

    /**
     * YYY need to rewrite this to use SAX parsing,
     * see Random Generator's factory.
     * @param params
     * @return
     */
    public IgstkvtGeneratorInterface createGenerator(
    			Map<String, Object> params) {
        try {
            String sendEventsSource = (String) params
                    .get(IgstkvtGeneratorServiceInterface.EVENTS_FILE);
            String stateMachineId = (String) params
                    .get(IgstkvtGeneratorServiceInterface.SM_ID);
            if (sendEventsSource == null || stateMachineId == null) {
                return null;
            }
            // log.info("The SE Source name is : " + sendEventsSource);
            InputSource source = createInputSource(sendEventsSource);
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
            return new IgstkvtBasicPrecomputedGenerator(eventList);
        } catch (FileNotFoundException e) {
            // log.error("File : " + sendEventsFileName + " is not found!! \n"+
            // "Exiting...");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // log.error("Parser Not correctly configured \n" + "Exiting...");
            e.printStackTrace();
        } catch (IOException e) {
            // log.error("An IO Exception Occured\n" + "Exiting...");
            e.printStackTrace();
        } catch (SAXException e) {
            // log.error("SAX Exception occured \n" + "Exiting...");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Little ditty to allow for reading of the file from URL or local file
     * @param sourceName
     * @return InputSource
     * @throws FileNotFoundException
     */
    private InputSource createInputSource(String sourceName)
    	throws FileNotFoundException {
    	InputSource rval = null;
    	// Basically we assume a normal filename or URL
    	try {
    		URL source = new URL(sourceName);
    		rval = new InputSource(new BufferedReader(
    					new InputStreamReader(source.openStream())));
    	} catch (Exception e) {
    		// well, try it as a file then
    		rval = new InputSource(new FileReader(sourceName));
    	}
    	return rval;

    }

    /**
     * @param n
     * @param depth
     * @param eventList
     * @param stateMachineId
     */
    private void walkFromNode(Node n, int depth,
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
}
