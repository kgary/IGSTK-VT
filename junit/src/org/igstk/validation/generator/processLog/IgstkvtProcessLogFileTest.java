/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtProcessLogFileTest.java
 * Language:  Java
 * Date:      Nov 12, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.generator.processLog;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sany
 */
public class IgstkvtProcessLogFileTest{
	
	private IgstkvtProcessLogFile test;
	IgstkvtConfigParser configParser;
	String IGSTKVT_HOME = null;
	
	/**
	 * @throws java.lang.Exception
	 */

	@Before
	public void setUp() throws Exception {
		test = new IgstkvtProcessLogFile();
		configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
				getPropertiesFileNameFromPropertiesFile("igstkvt.properties"));
		IGSTKVT_HOME = configParser.getIGSTKVT_HOME();
		File f = new File(IGSTKVT_HOME+"/xmlFiles/replay/igstkMouseTracker_" +
				"02CE0FE0_test.xml");
		f.delete();
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.igstk.validation.generator.
	 * 	processLog.IgstkvtProcessLogFile#
	 * 	createSendEventFile(java.lang.String, 
	 * java.lang.String)}.
	 */
	
	@Test
	public void testCreateSendEventFileAllNull() {
		boolean caughtException = false;
		try{
			test.createSendEventFile(null,null);
		}catch (IOException e){
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
			caughtException = true;
		}
		if (!caughtException){
			fail("Should throw some exception");
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testCreateSendEventFileNoFile(){
		// test2
		boolean caughtException = false;
		try{
			test.createSendEventFile("nofile.txt", IGSTKVT_HOME+"xmlFiles/replay");
		}catch (NullPointerException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if (!caughtException){
			fail("Should throw some exception");
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testCreateSendEventFileHappyCase(){
		boolean	caughtException = false;
		try{
			test.createSendEventFile(IGSTKVT_HOME+"/xmlFiles/replay/logHelloWorld.txt",IGSTKVT_HOME+"xmlFiles/replay");
		}catch (NullPointerException e){
			e.printStackTrace();
			caughtException = true;
		} catch (IOException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if (caughtException){
			fail("Should throw some exception");
		}
		File f = new File(IGSTKVT_HOME+"/xmlFiles/replay/igstkMouseTracker_" +
		"02CE0FE0_test.xml");
		assertTrue(f.exists());
		
	}
	
	

}

