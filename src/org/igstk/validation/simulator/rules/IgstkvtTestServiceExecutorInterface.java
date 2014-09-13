/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTestServiceExecutor.java
 * Language:  Java
 * Date:      Mar 18, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.simulator.rules;

import java.util.List;


/**
 * This is a ngeneric interface for the testing of the specified rules.
 * @author janakiramdandibhotla
 *
 */
public interface IgstkvtTestServiceExecutorInterface {
    /**
     * This method is used to execute the given rules whenever required.
     * @param context
     */
    public void execute();
    public void setDroolFiles(List<String> droolFiles);

    /**
     * Setter for context properties.
     * @param context
     */
    public void setContextProperties(IgstkvtTestExecutionContextProperties
    		context);
}
