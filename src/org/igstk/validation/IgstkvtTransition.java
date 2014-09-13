/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTransition.java
 * Language:  Java
 * Date:      May 9, 2008
 * 
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 * 
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.      
 *************************************************************************/
package org.igstk.validation;

/**
 * Class which is a holder for a transition.
 * @author janakiramdandibhotla
 */

public class IgstkvtTransition {

	private String event;
	private IgstkvtState startState;
	private IgstkvtState endState;
	
	/**
	 * Constructor which takes a event name.
	 * @param event
	 */
	public IgstkvtTransition(String event) {
		this.event = event;
	}
	
	/**
	 * Getter for event.
	 * @return
	 */
	public String getEvent() {
		return event;
	}
			
	/**
	 * Getter for start state.
	 * @return
	 */
	public IgstkvtState getStartState() {
		return startState;
	}

	/**
	 * Setter for start state.
	 * @param startState
	 */
	public void setStartState(IgstkvtState startState) {
		this.startState = startState;
	}

	/**
	 * Getter for end state.
	 * @return
	 */
	public IgstkvtState getEndState() {
		return endState;
	}

	/**
	 * Setter for end state.
	 * @param endState
	 */
	public void setEndState(IgstkvtState endState) {
		this.endState = endState;
	}
}
