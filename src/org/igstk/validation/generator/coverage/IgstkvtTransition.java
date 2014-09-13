/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTransition.java
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

package org.igstk.validation.generator.coverage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtTransition {

    Map<String, Integer> transitions= new HashMap<String, Integer>();
    Iterator<String> transitionsIter;
    int markedFlagCount;
    static Logger logger = Logger.getLogger(IgstkvtTransition.class);

    /**
     * This method is ovveridden constructor.
     * @param trans
     */
    public IgstkvtTransition(String trans) {
    	transitions.put(trans, new Integer(0));
        markedFlagCount = 0;
    }

    /**
     * Constructor.
     */
    public IgstkvtTransition() {

    }

    /**
     * Getter for the transition.
     * @return
     */
    public String getTransition() {
        Iterator<String> iter = transitions.keySet().iterator();
        while (iter.hasNext()) {
            return (String) (iter.next());
        }
        return null;
    }

    /**
     * Retrieves the next unmarked transition.
     * @return
     */

    String getNextTransition() {
        Iterator<String> iter = transitions.keySet().iterator();

        String str = "";
        while (iter.hasNext()) {
            str = iter.next();
            if (transitions.get(str) == 0) {
                return str;
            }
        }

        return null;
    }

    /**
     * Adds a new transition to the map of transitions, for a particular edge.
     * @param trans
     */

    void addTransition(String trans) {
        transitions.put(trans, new Integer(0));
    }

    /**
     * Gets the mark flag for a transition.
     * @param trans
     * @return
     */

    int getMarkFlag(String trans) {

    	logger.info("The value to be returned is "
    			+transitions.get(trans).intValue());
    	return transitions.get(trans).intValue();
    }

    /**
     * Sets the mark flag for a transition.
     * @param trans
     *
     */

    void setMarkFlag(String trans) {
        if (transitions.containsKey(trans)) {
            transitions.remove(trans);
            transitions.put(trans, new Integer(1));
            markedFlagCount++;
        }
    }

 /**
  * Resets the mark flag for a transition
  * @param trans
  */
	void resetMarkFlag(String trans){
		 // transitionsIter  pos = transitions.find(trans);
	    if (transitions.get(trans) != 0){
	    	transitions.put(trans, 0);
	    	markedFlagCount--;
		}
	}


/**
 * Reset all transitions
 */
	void resetAllTransitions(){
		Iterator<String> iter = transitions.keySet().iterator();
		String str = "";
		while (iter.hasNext()) {
	        str = iter.next();
	        transitions.put(str,0);
		   	markedFlagCount--;
		}
	}

	/**
	 * Getter for transition count.
	 * @return
	 */
	int getTransitionCount(){
		return transitions.size();
	}


    /**
     * Checks to see if all transitions between 2 nodes have been covered during
     * the IgstkvtGraph traversal and returns appropriate value.
     * @return
     */

    boolean areAllTransitionsCovered() {
        if (markedFlagCount == transitions.size()) {
            return true;
        }
        return false;
    }

}
