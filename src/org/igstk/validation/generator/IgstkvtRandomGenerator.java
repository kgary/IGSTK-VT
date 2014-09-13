/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtRandomGenerator.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * This class needs an IgstkvtGeneratorFactoryInterface type that takes an SCXML
 * file and identifies the set of possible events. Or possibly an inputs file
 * like the type Ben created.
 *
 * @author kgary
 *
 */
public class IgstkvtRandomGenerator extends IgstkvtAbstractBasicGenerator {

    private IgstkvtSendEvent[] events;
    private Random rand;

    /**
     * @param events
     */
    IgstkvtRandomGenerator(IgstkvtSendEvent[] events) {
        this.events = events;
        rand = new Random(System.currentTimeMillis());
    }

    /**
     * @throws IgstkvtGeneratorException
     * @return
     */
    public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException {
        if (moreEvents()) {
            return events[rand.nextInt(events.length)];
        }
        return null;
    }

    /**
     * @param n
     * @throws IgstkvtGeneratorException
     * @return
     */
    public Iterator<IgstkvtSendEvent> getNextEvents(int n)
        throws IgstkvtGeneratorException {
        ArrayList<IgstkvtSendEvent> l = new ArrayList<IgstkvtSendEvent>(n);
        for (int i = 0; i < n; i++) {
            l.add(getNextEvent());
        }
        return l.iterator();
    }

    /**
     * @return
     */
    public boolean moreEvents() {
        // The random generator can always create more events
        return (events != null && events.length > 0);
    }
}
