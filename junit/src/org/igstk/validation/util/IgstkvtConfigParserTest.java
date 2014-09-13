/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtConfigParserTest.java
 * Language:  Java
 * Date:      May 12, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.igstk.validation.IgstkvtConfigProps;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author janakiramdandibhotla
 */
public class IgstkvtConfigParserTest {
    IgstkvtConfigParser configParser;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        configParser = IgstkvtConfigParser.getInstance(IgstkvtConfigParser.
                getPropertiesFileNameFromPropertiesFile("igstkvt.properties"));
    }

    /**
     *
     */
    @Test
    public void testGetConfigProps() {

        //Test 1
        //Testing if this function is getting.
        boolean caughtException = false;
        IgstkvtConfigProps configProps = null;
        try {
            configProps = configParser.getConfigProps();
        } catch (IgstkvtConfigurationException e) {
        	e.printStackTrace();
            caughtException = true;
        } catch (IgstkvtFatalConfigException e) {
            e.printStackTrace();
            caughtException = true;
        }

        if(caughtException){
            fail("This should not throw exception");
        }
        assertNotNull(configProps);
    }

}
