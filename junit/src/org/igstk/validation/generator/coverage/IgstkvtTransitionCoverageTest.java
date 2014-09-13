/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTransitionCoverageTest.java
 * Language:  Java
 * Date:      Nov 13, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.generator.coverage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParserFactory;

import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sany
 * 
 **/
public class IgstkvtTransitionCoverageTest {

	private IgstkvtTransitionCoverage tc;
	private IgstkvtSAXHandler handler;
	private javax.xml.parsers.SAXParser parser;
	IgstkvtConfigParser configParser;
	String IGSTKVT_HOME;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tc = new IgstkvtTransitionCoverage();
		configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
				getPropertiesFileNameFromPropertiesFile("igstkvt.properties"));
		IGSTKVT_HOME = configParser.getIGSTKVT_HOME();
		File f = new File(IGSTKVT_HOME+"/xmlFiles/coverage/" 
				+"igstkDummyTracker_edgecoverage_test_basispath1.xml.xml");
		f.delete();
		handler = new IgstkvtSAXHandler("edgecoverage",tc);
		parser = SAXParserFactory.newInstance().newSAXParser();
		handler.setParseType(1);
        parser.parse(IGSTKVT_HOME+"/xmlFiles/scxmlFiles/igstkDummy"
        		+"Tracker.xml", handler);
		handler.setParseType(2);
		parser.parse(IGSTKVT_HOME+"/xmlFiles/scxmlFiles/igstkDummy"
        		+"Tracker.xml", handler);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.igstk.validation.generator.coverage.
	 * IgstkvtTransitionCoverage
	 * #writeToFile(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testWriteToFile() {
		boolean caughtException =false;
		try{
			tc.writeToFile(null,null);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			caughtException =true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		if (caughtException){
			fail("It shouldn't catch this exception");
		}
	}
	
	/**
	 * Test method for {@link org.igstk.validation.generator.
	 * coverage.IgstkvtTransistionCoverage
	 * #writeToFile(java.lang.String, java.lang.String)}.
	 * 
	 */
	
	@Test
	public void testWriteToFileHappy(){

		try{
			tc.writeToFile("igstkCylinderObjectRepresentation", 
					IGSTKVT_HOME+"/xmlFiles/coverage/");
		}catch(Exception e){
			e.printStackTrace();
		}
		File f = new File(IGSTKVT_HOME+"/xmlFiles/coverage/" 
				+"igstkDummyTracker_edgecoverage_test_basispath1.xml");
		assertTrue(f.exists());
	}
	
}
