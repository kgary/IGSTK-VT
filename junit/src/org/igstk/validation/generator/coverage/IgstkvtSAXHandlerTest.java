/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSAXHandlerTest.java
 * Language:  Java
 * Date:      Nov 21, 2008
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
import org.xml.sax.Attributes;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


/**
 * @author Sany 
 **/
public class IgstkvtSAXHandlerTest {

	IgstkvtSAXHandler sh;
	IgstkvtGraph gc;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		gc = new IgstkvtGraph();
		sh = new IgstkvtSAXHandler("nodecoverage",gc);
			
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.igstk.validation
	 * .generator.coverage.IgstkvtSAXHandler#startDocument()}.
	 * @throws SAXException 
	 */
	@Test
	public final void testStartDocument() throws SAXException {
		Attributes a=null;
		boolean caughtExcpetion = false;
		try{
			sh.startElement("","","scxml",a);
		}catch (NullPointerException e){
			caughtExcpetion = true;
		}
		if (!caughtExcpetion){
			fail("This should throw exception");
		}
		
	}

}
