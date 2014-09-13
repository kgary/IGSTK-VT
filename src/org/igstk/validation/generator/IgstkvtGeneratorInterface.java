/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGeneratorInterface.java
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

package org.igstk.validation.generator;

import java.util.Iterator;

/**
 * @author kgary
 *
 */
public interface IgstkvtGeneratorInterface {
    /**
     * @return
     * @throws IgstkvtGeneratorException
     */
    public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException;

    /**
     * @param n
     * @return
     * @throws IgstkvtGeneratorException
     */
    public Iterator<IgstkvtSendEvent> getNextEvents(int n)
        throws IgstkvtGeneratorException;

    /**
     * @return
     */
    public boolean moreEvents();
}
