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
import java.util.List;

/**
 * @author kgary
 *
 */
public class IgstkvtBasicPrecomputedGenerator extends
        IgstkvtAbstractBasicGenerator implements
        IgstkvtPrecomputedGeneratorInterface,
        IgstkvtCacheableGeneratorInterface {

    protected List<IgstkvtSendEvent> eventsList;
    protected int currPos;

    /**
     * @param events
     *
     */
    IgstkvtBasicPrecomputedGenerator(List<IgstkvtSendEvent> events) {
        super();
        eventsList = events;
        currPos = 0;
    }

    /**
     * @return
     *
     */
    public IgstkvtSendEvent getNextEvent() {
        if (eventsList != null) {
            if (moreEvents()) {
                return eventsList.get(currPos++);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     *
     */
    public boolean moreEvents() {
        return currPos < eventsList.size();
    }


    /**
     * @param n
     * @return
     */
    public IgstkvtSendEvent lookAhead(int n) {
        if (n < 0 || (currPos + n) >= eventsList.size()) {
            return null;
        }
        return eventsList.get(currPos + n - 1);
    }

    /**
     * @param n
     * @return int indicated the number of elements skipped forward
     */
    public int skip(int n) {
    	int rval = 0;
        if (n < 0) {
            return 0;
        }

        if ((currPos+n) >= eventsList.size()) {
        	rval = eventsList.size() - currPos;
        	currPos = eventsList.size();  // move to one beyond teh list
        } else {
        	rval = n;
        	currPos += n;
        }
        return rval;
    }

    /**
     * @param se
     * @return int indicating how many events were skipped
     */
    public int skip(IgstkvtSendEvent se) {
    	List<IgstkvtSendEvent> selist = eventsList.
    					subList(currPos, eventsList.size());
    	int index = 0;
    	if ((index = selist.indexOf(se)) == -1) {
    		index = 0;
    	} else {
    		currPos += index;
    	}
        return index;
    }

    /**
     * @param n
     * @return
     * @throws IgstkvtGeneratorException
     */
    public Iterator<IgstkvtSendEvent> getNextEvents(int n)
        throws IgstkvtGeneratorException {
        List<IgstkvtSendEvent> se = null;
        int endpoint = 0;
        if (n < 0 || (currPos + n) >= eventsList.size()) {
            endpoint = eventsList.size() - 1; // subList is inclusive
        } else {
            endpoint = currPos + n - 1;
        }
        se = eventsList.subList(currPos, endpoint);
        currPos = endpoint + 1;
        return se.iterator();
    }

    /**
     * @param n
     * @return
     */
	public IgstkvtSendEvent lookBehind(int n) {
		IgstkvtSendEvent se = null;

		if (n > 0 && n <= currPos) {
			se = eventsList.get(currPos-n);
		}
		return se;
	}

	/**
	 *This method implements rewind function in Cachable Generator.
	 * @see org.igstk.validation.generator.-
	 * IgstkvtCacheableGeneratorInterface#rewind
	 * (org.igstk.validation.generator.IgstkvtSendEvent)
	 */
	public int rewind(IgstkvtSendEvent se) {
		List<IgstkvtSendEvent> selist = eventsList.subList(0, currPos);
    	int index = 0;
    	if ((index = selist.lastIndexOf(se)) == -1) {
    		index = currPos - index;
    	} else {
    		currPos += index;
    	}
        return index;
	}

	/**
     * @param n
     * @return
     */
	public int rewind(int n) {
		int rval = 0;
		if (n > 0) {
			if (n > currPos) {
				rval = currPos;
				currPos = 0;
			} else {
				rval = n;
				currPos -= n;
			}
		}
		return rval;
	}
}
