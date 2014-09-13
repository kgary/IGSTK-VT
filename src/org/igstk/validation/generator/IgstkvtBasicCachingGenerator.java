/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    IGSTK Validation Tools
  Language:  Java
  Date:      Feb 18, 2008
  Version:

  Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

package org.igstk.validation.generator;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author kgary
 *
 */
public class IgstkvtBasicCachingGenerator implements
        IgstkvtCacheableGeneratorInterface {

    private int currPos;
    private IgstkvtGeneratorInterface __sourceGenerator;
    private Stack<IgstkvtSendEvent> __pastEvents;

    /**
     * This is the constructor.
     * @param src
     */
    IgstkvtBasicCachingGenerator(IgstkvtGeneratorInterface src) {
        super();
        __sourceGenerator = src;
        __pastEvents = new Stack<IgstkvtSendEvent>();
    }

    /**
     *
     * @return
     */
    public boolean moreEvents() {
        return (currPos > 0 && !__pastEvents.empty())
                || __sourceGenerator.moreEvents();
    }

    /**
     * @param n
     * @return
     */
    public int rewind(int n) {
        int rval = n;
        int s = __pastEvents.size();
        if (currPos + n > s) {
            rval = s - currPos;
            currPos = s;
        } else {
            currPos += n;
        }
        return rval;
    }

    /**
     *
     * @param se
     * @return
     */
    public int rewind(IgstkvtSendEvent se) {
        int rval = 0;
        do {
            rval = __pastEvents.lastIndexOf(se);
        } while (rval > __pastEvents.size() - currPos);
        if (rval == -1) {
            // item not found between 0 and currPos
            rval = 0;
        } else { // item found between 0 and currPos, reset currPos
            currPos = __pastEvents.size() - rval;
        }
        return rval;
    }

    /**
     * lookBehind supports retrieving an event n places back without rewinding
     * through the cache.
     * @param n
     * @return
     */
    public IgstkvtSendEvent lookBehind(int n) {
        IgstkvtSendEvent se = null;
        if (n + currPos > __pastEvents.size()) {
            se = __pastEvents.firstElement();
        } else {
            se = __pastEvents.get(__pastEvents.size() - (currPos + n));
        }
        return se;
    }

    /**
     *
     * @throws IgstkvtGeneratorException
     * @return
     */
    public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException {
        /*
         * If the current position indicator says we rewound at some point, then
         * retrieve from the cache, else retrieve by simply getting the next
         * event and adding it to the cache.
         */
        IgstkvtSendEvent se = null;
        if (currPos > 0) {
            se = lookBehind(0); // use zero here as offset from currPos
            currPos--;
        } else {
            se = __sourceGenerator.getNextEvent();
            if (se != null) {
                __pastEvents.push(se);
            }
        }
        return se;
    }

    /**
     * @param n
     * @return
     * @throws IgstkvtGeneratorException
     */
    public Iterator<IgstkvtSendEvent> getNextEvents(int n)
        throws IgstkvtGeneratorException {
        Iterator<IgstkvtSendEvent> events = __sourceGenerator.getNextEvents(n);
        while (events.hasNext()) {
            __pastEvents.push(events.next());
        }
        return events;
    }

}
