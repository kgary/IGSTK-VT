/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtState.java
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

package org.igstk.validation.tools.smviz;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

/**
 * Class representing state in a state machine. Wraps {@link IgstkvtState}.
 * Used for drawing by smviz tool.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSMVIZState {

    private int gcXCoordinate;
    private int gcYCoordinate;

    private int gcWidth;
    private int gcHeight;

    private int gcColor;
    private int gcFillColor;
    private String name;

    private List<String> transitionEvents;

    /**
     * Sole constructor of the class.
     */
    public IgstkvtSMVIZState() {
        setGcColor(SWT.COLOR_BLACK);
        setGcFillColor(SWT.COLOR_WHITE);
        transitionEvents = new ArrayList<String>();
    }

    /**
     * Returns the name of the state.
     * @return name of the state.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Name.
     * @param name
     */
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * Returns list of events that cause transitions from this state.
     * @return list of transition events.
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
     * Returns the x coordinate value required for drawing the state in a
     * canvas widget.
     * @return the x coordinate value.
     */
    public int getGcXCoordinate() {
        return gcXCoordinate;
    }

    /**
     * Sets the x coordinate value required for drawing the state in a
     * canvas widget.
     * @param gcXCoordinate the x coordinate value.
     */
    public void setGcXCoordinate(int gcXCoordinate) {
        this.gcXCoordinate = gcXCoordinate;
    }

    /**
     * Returns the y coordinate value required for drawing the state in a
     * canvas widget.
     * @return the y coordinate value.
     */
    public int getGcYCoordinate() {
        return gcYCoordinate;
    }

    /**
     * Sets the y coordinate value required for drawing the state in a
     * canvas widget.
     * @param gcYCoordinate the y coordinate value.
     */
    public void setGcYCoordinate(int gcYCoordinate) {
        this.gcYCoordinate = gcYCoordinate;
    }

    /**
     * Gets the width value required for drawing the state in a
     * canvas widget.
     * @return the width value.
     */
    public int getGcWidth() {
        return gcWidth;
    }

    /**
     * Sets the width value required for drawing the state in a
     * canvas widget.
     * @param gcWidth the width value.
     */
    public void setGcWidth(int gcWidth) {
        this.gcWidth = gcWidth;
    }

    /**
     * Gets the height value required for drawing the state in a
     * canvas widget.
     * @return the height value.
     */
    public int getGcHeight() {
        return gcHeight;
    }

    /**
     * Sets the height value required for drawing the state in a
     * canvas widget.
     * @param gcHeight the height value.
     */
    public void setGcHeight(int gcHeight) {
        this.gcHeight = gcHeight;
    }

    /**
     * Gets the color value required for drawing the state in a
     * canvas widget.
     * @return the color value.
     */
    public int getGcColor() {
        return gcColor;
    }

    /**
     * Sets the color value required for drawing the state in a
     * canvas widget.
     * @param gcColor the color value.
     */
    public void setGcColor(int gcColor) {
        this.gcColor = gcColor;
    }

    /**
     * Gets the fill color value required for drawing the state in a
     * canvas widget.
     * @return the fill color value.
     */
    public int getGcFillColor() {
        return gcFillColor;
    }

    /**
     * Sets the fill color value required for drawing the state in a
     * canvas widget.
     * @param gcFillColor the fill color value.
     */
    public void setGcFillColor(int gcFillColor) {
        this.gcFillColor = gcFillColor;
    }

    /**
     * Method to clone the state.
     * @author
     * @param
     * @throws
     */
    public IgstkvtSMVIZState clone() {
    	IgstkvtSMVIZState smvizState = new IgstkvtSMVIZState();

//    	smvizState.setGcColor(gcColor);
//    	smvizState.setGcFillColor(gcFillColor);
//    	smvizState.setGcHeight(gcHeight);
//    	smvizState.setGcWidth(gcWidth);
//    	smvizState.setGcXCoordinate(gcXCoordinate);
//    	smvizState.setGcYCoordinate(gcYCoordinate);
    	smvizState.setTransitionEvents(transitionEvents);
    	smvizState.setName(name);

    	return smvizState;
    }
}
