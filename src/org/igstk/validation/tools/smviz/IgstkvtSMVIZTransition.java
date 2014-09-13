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

package org.igstk.validation.tools.smviz;

import org.eclipse.swt.SWT;

/**
 * Class representing a transition in a state machine.
 * Wraps {@link IgstkvtTransition}. Used for drawing by SMVIZ tool.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSMVIZTransition {

	private IgstkvtSMVIZState startState;
	private IgstkvtSMVIZState endState;
	private String event;

    private int gcXCoordinate;
    private int gcYCoordinate;

    private int gcWidth;
    private int gcHeight;

    private int gcStartAngle;
    private int gcArcAngle;

    private int gcColor;

    /**
     * Class constructor.
     */
    public IgstkvtSMVIZTransition() {
        setGcColor(SWT.COLOR_BLACK);
    }

    /**
     * Gets the event which causes the transition.
     * @return event which causes the transition.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Setter for event.
     * @param event
     */
    public void setEvent(String event){
    	this.event = event;
    }

    /**
     * Returns the SMVIZ state where the transition begins.
     * @return state where transition begins.
     */
    public IgstkvtSMVIZState getStartState() {
		return startState;
	}

    /**
     * Sets the state where transition begins.
     * @param startState state where transition begins.
     */
	public void setStartState(IgstkvtSMVIZState startState) {
		this.startState = startState;
	}

	/**
	 * Returns the state where transition ends.
	 * @return state where transition ends.
	 */
	public IgstkvtSMVIZState getEndState() {
		return endState;
	}

	/**
	 * Sets the state where transition ends.
	 * @param endState state where transition ends.
	 */
	public void setEndState(IgstkvtSMVIZState endState) {
		this.endState = endState;
	}

	/**
     * Gets the x coordinate value required to draw the transition in a
     * canvas widget.
     * @return x coordinate value required to draw the transition.
     */
    public int getGcXCoordinate() {
        return gcXCoordinate;
    }

    /**
     * Sets the x coordinate value required to draw the transition in a
     * canvas widget.
     * @param gcXCoordinate x coordinate value required to draw the transition.
     */
    public void setGcXCoordinate(int gcXCoordinate) {
        this.gcXCoordinate = gcXCoordinate;
    }

    /**
     * Gets the y coordinate value required to draw the transition in a
     * canvas widget.
     * @return y coordinate value required to draw the transition.
     */
    public int getGcYCoordinate() {
        return gcYCoordinate;
    }

    /**
     * Sets the y coordinate value required to draw the transition in a
     * canvas widget.
     * @param gcYCoordinate y coordinate value required to draw the transition.
     */
    public void setGcYCoordinate(int gcYCoordinate) {
        this.gcYCoordinate = gcYCoordinate;
    }

    /**
     * Gets width value required to draw the transition in a canvas widget.
     * @return width value required to draw the transition.
     */
    public int getGcWidth() {
        return gcWidth;
    }

    /**
     * Sets the width value required to draw the transition in a
     * canvas widget.
     * @param gcWidth width value required to draw the transition.
     */
    public void setGcWidth(int gcWidth) {
        this.gcWidth = gcWidth;
    }

    /**
     * Gets the height value required to draw the transition in a
     * canvas widget.
     * @return height value required to draw the transition.
     */
    public int getGcHeight() {
        return gcHeight;
    }

    /**
     * Sets the height value required to draw the transition in a
     * canvas widget.
     * @param gcHeight height value required to draw the transition.
     */
    public void setGcHeight(int gcHeight) {
        this.gcHeight = gcHeight;
    }

    /**
     * Gets the start ange value required to draw the transition in a
     * canvas widget.
     * @return start angle value required to draw the transition.
     */
    public int getGcStartAngle() {
        return gcStartAngle;
    }

    /**
     * Sets the start angle value required to draw the transition in a
     * canvas widget.
     * @param gcStartAngle start angle value required to draw the transition.
     */
    public void setGcStartAngle(int gcStartAngle) {
        this.gcStartAngle = gcStartAngle;
    }

    /**
     * Gets the arc angle value required to draw the transition in a
     * canvas widget.
     * @return arc angle value required to draw the transition.
     */
    public int getGcArcAngle() {
        return gcArcAngle;
    }

    /**
     * Sets the arc angle value required to draw the transition in a
     * canvas widget.
     * @param gcArcAngle arc angle value required to draw the transition.
     */
    public void setGcArcAngle(int gcArcAngle) {
        this.gcArcAngle = gcArcAngle;
    }

    /**
     * Gets the color value required to draw the transition in a
     * canvas widget.
     * @return color value required to draw the transition.
     */
    public int getGcColor() {
        return gcColor;
    }

    /**
     * Sets the color value required to draw the transition in a
     * canvas widget.
     * @param gcColor color value required to draw the transition.
     */
    public void setGcColor(int gcColor) {
        this.gcColor = gcColor;
    }

    /**
     * Method to clone a Transition.
     * @author
     * @param
     * @throws
     */
    public IgstkvtSMVIZTransition clone() {

    	IgstkvtSMVIZTransition smvizTransition =
    		new IgstkvtSMVIZTransition();

    	smvizTransition.setGcArcAngle(gcArcAngle);
    	smvizTransition.setGcColor(gcColor);
    	smvizTransition.setGcHeight(gcHeight);
    	smvizTransition.setGcStartAngle(gcStartAngle);
    	smvizTransition.setGcWidth(gcWidth);
    	smvizTransition.setGcXCoordinate(gcXCoordinate);
    	smvizTransition.setGcYCoordinate(gcYCoordinate);
    	smvizTransition.setEvent(event);
//    	smvizTransition.setEndState(endState);
//    	smvizTransition.setStartState(startState);

    	return smvizTransition;
    }
}
