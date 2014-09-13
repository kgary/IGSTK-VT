/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTestExecutionContextProperties.java
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

import org.igstk.validation.simulator.IgstkvtStateMachineExecutor;

/**
 * This is a class which is used to store all the properties to be
 * used in a test.
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtTestExecutionContextProperties {
    private List<IgstkvtStateMachineExecutor> objectsToFireRulesOn;

    /**
     * This is a getter method for list of objects to fire rules on.
     * @return
     */
    public List<IgstkvtStateMachineExecutor> getObjectsToFireRulesOn() {
        return objectsToFireRulesOn;
    }

    /**
     * This is a setter method for list of objects to file rules on.
     * @param objectToFireRulesOn
     */
    public void setObjectsToFireRulesOn(
            List<IgstkvtStateMachineExecutor> objectsToFireRulesOn) {
        this.objectsToFireRulesOn = objectsToFireRulesOn;
    }
}
