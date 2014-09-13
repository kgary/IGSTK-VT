/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSCXMLParserTest.java
 * Language:  Java
 * Date:      May 13, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.util;

import static org.junit.Assert.fail;

import org.igstk.validation.IgstkvtStateMachine;
import org.igstk.validation.exception.IgstkSCXMLParserException;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author janakiramdandibhotla
 */
public class IgstkvtSCXMLParserTest {

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     */
    @Test
    public void testParseSCXMLFile() {
        //Test 1
        //Testing parser
        boolean caughtException = false;
        IgstkvtStateMachine sm = null;
        try {
            sm = IgstkvtSCXMLParser.parseSCXMLFile(null);
        } catch (IgstkSCXMLParserException e) {
            caughtException = true;
        }

        if(!caughtException){
            fail("Exception should be thrown.");
        }

        //Test 2
        //Testing parser by sending it a real file.
        caughtException = false;
        IgstkvtConfigParser configParser = null;
        try {
            configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
    				getPropertiesFileNameFromPropertiesFile(
    						"igstkvt.properties"));
        } catch (IgstkvtFatalConfigException e1) {
            e1.printStackTrace();
        } catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
		}
        try{

            sm = IgstkvtSCXMLParser.parseSCXMLFile(configParser.
                       getIGSTKVT_HOME()+"xmlFiles/scxmlFiles/igstkSpatialObjectReader.xml");
        }catch (IgstkSCXMLParserException e){
            caughtException = true;
        }

        if(caughtException){
            fail("This should not throw exception.");
        }
    }
}
