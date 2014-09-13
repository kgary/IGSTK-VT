/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStatementCoverageTest.java
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


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParserFactory;

import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Santhosh"
 */


public class IgstkvtStatementCoverageTest {

	private IgstkvtStatementCoverage sc;
	private IgstkvtSAXHandler handler;
	private javax.xml.parsers.SAXParser parser;
	IgstkvtConfigParser configParser;
	String IGSTKVT_HOME;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sc = new IgstkvtStatementCoverage();
		configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
				getPropertiesFileNameFromPropertiesFile("igstkvt.properties"));
		IGSTKVT_HOME = configParser.getIGSTKVT_HOME();
		File f = new File(IGSTKVT_HOME+"/xmlFiles/coverage/" 
				+"igstkCylinderObjectRepresentation_nodecoverage_test1.xml");
		f.delete();
		handler = new IgstkvtSAXHandler("nodecoverage",sc);
		parser = SAXParserFactory.newInstance().newSAXParser();
		handler.setParseType(1);
        parser.parse(IGSTKVT_HOME+"/xmlFiles/scxmlFiles/igstkCylinder" 
        		+"ObjectRepresentation.xml", handler);
		handler.setParseType(2);
        parser.parse(IGSTKVT_HOME+"/xmlFiles/scxmlFiles/igstkCylinder" 
        		+"ObjectRepresentation.xml", handler);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.igstk.validation.generator.
	 * coverage.IgstkvtStatementCoverage
	 * #writeToFile(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testWriteToFile() {
		boolean caughtException =false;
		try{
			sc.writeToFile(null,null);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			caughtException =true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		if (caughtException){
			fail("It shouldn't  catch this exception");
		}
	}
	
	/**
	 * Test method for {@link org.igstk.validation.generator.
	 * coverage.IgstkvtStatementCoverage
	 * #writeToFile(java.lang.String, java.lang.String)}.
	 * 
	 */
	
	@Test
	public void testWriteToFileHappy(){
		try{
			sc.writeToFile("igstkCylinderObjectRepresentation", 
					IGSTKVT_HOME+"/xmlFiles/coverage/");
		}catch(Exception e){
			e.printStackTrace();
		}
		File f = new File(IGSTKVT_HOME+"/xmlFiles/coverage/" 
				+"igstkCylinderObjectRepresentation_nodecoverage_test1.xml");
		assertTrue(f.exists());
	}
	
}
