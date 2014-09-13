/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtRandomGeneratorFactory.java
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
package org.igstk.validation.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kgary
 *
 */

public class IgstkvtRandomGeneratorFactory implements
		IgstkvtGeneratorFactoryInterface {

	private static final String SENDEVENTS_FILE = "SENDEVENTS_FILE";
	private static final String SCXML_FILE = "SCXML_FILE";

	// A cheesy trick to make sure the current
	//tagname is available for SAX parsing
	private String tagname = "";
	private HashSet<IgstkvtSendEvent> events = new HashSet<IgstkvtSendEvent>();
	private String stateMachineId = "";

	// This is the Singleton instance
	private static IgstkvtRandomGeneratorFactory theFactory;
	/**
	 * Private constructor for Singleton pattern
	 */
	private IgstkvtRandomGeneratorFactory() {}

	/**
	 * Singleton.
     * @return
     */
    public static IgstkvtGeneratorFactoryInterface getGeneratorFactory() {
        if (theFactory == null) {
            theFactory = new IgstkvtRandomGeneratorFactory();
        }
        return theFactory;
    }

	/**
	 * This method constructs a set of events based
	 * on the total bag of events contained in either
	 * a send events file or an SCXML file. At least
	 * one or the other of these must be specified
	 * in the configuration parameters. If both are
	 * specified it will read both.
	 * @param params is a set of name-value configuration pairs.
	 * For this generator factory, we need
	 * either a send events file or an scxml file.
	 * @return a new IgstkvtGenerator of type IgstkvtRandomGenerator
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorFactoryInterface#createGenerator(java.util.Map)
	 */
	public IgstkvtGeneratorInterface createGenerator(
			Map<String, Object> params) {
		String se_filename 		= (String)params.get(SENDEVENTS_FILE);
		String scxml_filename 	= (String)params.get(SCXML_FILE);
		stateMachineId			= (String)params.
								get(IgstkvtGeneratorServiceInterface.SM_ID);

		try {
			if (se_filename != null) {
				tagname = "send";
				readInEvents(se_filename);
			}
			if (scxml_filename != null) {
				tagname = "transition";
				readInEvents(se_filename);
			}
		} catch (IOException ie) {
			return null;
		}
		return new IgstkvtRandomGenerator(
					events.toArray(new IgstkvtSendEvent[0]));
	}

	/**
	 * This method takes advantage of the fact
	 * that a send events file uses the tag "send" while
	 * an SCXML file uses the tag "transition",
	 * but they both use the attribute "event".
	 *
	 * We SAX parse here.
	 *
	 * @param filename
	 * @param tagname
	 * @param events
	 */
	private void readInEvents(String filename) throws IOException {
		SAXParserFactory spf = null;
		SAXParser sp = null;

		try {
			EventSAXHandler handler = new EventSAXHandler();
		    spf = SAXParserFactory.newInstance();
		    sp  = spf.newSAXParser();
		    XMLReader xr = sp.getXMLReader();
		    xr.setContentHandler(handler);
		    xr.setErrorHandler(handler);

		    sp.parse(new File(filename), handler);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw ie;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *Private class.
	 */
	private class EventSAXHandler extends DefaultHandler {
		/**
		 * @see org.xml.sax.helpers.
		 * DefaultHandler#startElement(java.lang.String,
		 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String uri, String localName,
				String qName, Attributes attributes) {
			String eventName = null;
			if (localName.equalsIgnoreCase(tagname)) {
				if ((eventName = attributes.getValue(uri, "event")) != null) {
					events.add(new IgstkvtSendEvent(eventName, stateMachineId));
				}
			}
		}
	    // for Error Handler - just outputting for now.
		//YYY should be in a logger.
	    /**
	     * @see org.xml.sax.helpers.
	     * DefaultHandler#warning(org.xml.sax.SAXParseException)
	     */
	    public void warning(SAXParseException spe) {
	    	spe.printStackTrace();
	    }
	    /**
	     * @see org.xml.sax.helpers.
	     * DefaultHandler#error(org.xml.sax.SAXParseException)
	     */
	    public void error(SAXParseException spe) {
	    	spe.printStackTrace();
	    }
	}
}
