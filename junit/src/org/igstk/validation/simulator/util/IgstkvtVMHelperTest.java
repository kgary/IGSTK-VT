/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtVMHelperTest.java
 * Language:  Java
 * Date:      Nov 4, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.simulator.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.simulator.IgstkvtStateMachineExecutor;
import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for VM Helper.
 * @author janakiram
 */
public class IgstkvtVMHelperTest {
	IgstkvtConfigParser configParser;
	IgstkvtVMHelper helper;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
				getPropertiesFileNameFromPropertiesFile("igstkvt.properties"));

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.igstk.validation.simulator.util.
	 * IgstkvtVMHelper#createExecutors(org.igstk.validation.IgstkvtConfigProps)}.
	 */
    /**
    *
    */
    @Test
    public void testCreateExecutors() {

       //Test 1
       //Testing if this method throws error if called with null filename.
    	boolean caughtException = false;
    	Map<String,IgstkvtStateMachineExecutor> execs = null;
    	try {
    		execs = IgstkvtVMHelper.createExecutors(null);
    	} catch (IgstkvtConfigurationException e) {
    		caughtException = true;
    		e.printStackTrace();
    	}
    	if(!caughtException){
    		fail("The createExecutors should throw an exception. "
           		+"But it did not!");
    	}else {
    		System.out.println("Exception should be thrown");
    	}

       //Test 2
       //Testing if a fileName is being sent, the executors are being created!
    	caughtException = false;
    	try {
    		execs = IgstkvtVMHelper.createExecutors(configParser.getConfigProps());
    	} catch (IgstkvtConfigurationException e) {
    		caughtException = true;
    	} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

    	if(caughtException){
    		fail("This exception should not be thrown!");
    	}
    	assertNotNull(execs);
    	assertTrue(execs.size() > 0);

    }

}
