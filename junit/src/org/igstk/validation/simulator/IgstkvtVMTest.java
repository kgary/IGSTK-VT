/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtVMTest.java
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
package org.igstk.validation.simulator;

import static org.junit.Assert.fail;

import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for VM.
 * @author janakiram
 */
public class IgstkvtVMTest {

	private IgstkvtVM vm;
	IgstkvtConfigParser configParser;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		vm = new IgstkvtVM();
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
	 * Test method for {@link org.igstk.validation.simulator.IgstkvtVM#
	 * init(org.igstk.validation.IgstkvtConfigProps)}.
	 */
	@Test
	public void testInitCheckForNull() {
		boolean caughtException = false;
		try {
			vm.init(null);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Did not throw exception.");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.simulator.IgstkvtVM#
	 * init(org.igstk.validation.IgstkvtConfigProps)}.
	 */
	@Test
	public void testInit() {
		boolean caughtException = false;
		try {
			vm.init(configParser.getConfigProps());
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.simulator.IgstkvtVM#
	 * fireEvents(int, int)}.
	 */
	@Test
	public void testFireEvents() {
		boolean caughtException = false;
		try{
			vm.init(configParser.getConfigProps());
		}catch (Exception e) {
			e.printStackTrace();
			fail("Should not throw exception.");
		}
		try {
			vm.fireEvents(1, 10);
			vm.fireEvents(-1,1);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("FireEvents: Should not throw exception.");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.simulator.
	 * IgstkvtVM#rewind(int)}.
	 */
	@Test
	public void testRewind() {
		boolean caughtException = false;
		try {
			vm.init(configParser.getConfigProps());
			vm.fireEvents(-1, 0);
			vm.rewind(3);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtRewindException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if(caughtException){
			fail("Should not throw exception.");
		}
	}

}
