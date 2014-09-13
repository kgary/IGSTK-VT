
/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtState.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class which holds the information about a state.
 * @author janakiramdandibhotla
 */
public class IgstkvtState {

	private String name;
	private List<String> transitionEvents;

	/**
	 * Constructor to take a name.
	 * @param name
	 */
	public IgstkvtState(String name) {
		this.name = name;
		transitionEvents = new ArrayList<String>();
	}

	/**
	 * Getter for name.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for transition events.
	 * @return
	 */
	public List<String> getTransitionEvents() {
		return transitionEvents;
	}

	/**
	 * Setter for transition events.
	 * @param transitionEvents
	 */
	public void setTransitionEvents(List<String> transitionEvents) {
		this.transitionEvents = transitionEvents;
	}

	/**
	 * Method to add an event.
	 * @param event
	 */
	public void addTransitionEvent(String event) {
		transitionEvents.add(event);
	}

	/**
	 * Method to clone the state.
	 * @author
	 * @param
	 * @throws
	 */
	public IgstkvtState clone() {
		IgstkvtState state = new IgstkvtState(name);
		List<String> events = new ArrayList<String>();
		for(String event : transitionEvents) {
			events.add(event);
		}
		state.setTransitionEvents(events);
		return state;
	}
}
