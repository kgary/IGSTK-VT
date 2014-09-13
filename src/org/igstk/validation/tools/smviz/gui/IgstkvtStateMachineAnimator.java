/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachineAnimator.java
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

package org.igstk.validation.tools.smviz.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.igstk.validation.generator.IgstkvtSendEvent;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZState;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;

/**
 * Provides animation capabilities to state machines by coloring
 * the states and transitions.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtStateMachineAnimator implements Observer {

    private IgstkvtSMVIZStateMachine stateMachine;
    private Canvas smCanvas;

    private IgstkvtSMVIZTransition currentTransition;
    private List<IgstkvtSMVIZTransition> previousTransitions;
    private IgstkvtSMVIZState currentState;

    /**
     * Constructor class which sets the state machine for which it acts as an
     * animator. Initializes previous transitions list and also sets the current
     * state to be the starting state of the state machine.
     *
     * @param stateMachine the state machine to be animated.
     */
    public IgstkvtStateMachineAnimator(IgstkvtSMVIZStateMachine stateMachine,
            Canvas smCanvas) {
        setStateMachine(stateMachine);
        setSmCanvas(smCanvas);
        this.previousTransitions = new ArrayList<IgstkvtSMVIZTransition>();
        setCurrentState(stateMachine.getStates().iterator().next());
        smCanvas.redraw();
    }

    /**
     * Returns the state machine.
     * @return <tt>IgstkvtStateMachine</tt>
     */
    public IgstkvtSMVIZStateMachine getStateMachine() {
        return stateMachine;
    }

    /**
     * Sets the state machine.
     * @param stateMachine <tt>IgstkvtStateMachine</tt>
     */
    public void setStateMachine(IgstkvtSMVIZStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * Returns the canvas widget that painted the state machine.
     * @return the <tt>Canvas</tt> widget.
     */
    public Canvas getSmCanvas() {
        return smCanvas;
    }

    /**
     * Sets the canvas widget that painted the state machine.
     * @param smCanvas the <tt>Canvas</tt> widget.
     */
    public void setSmCanvas(Canvas smCanvas) {
        this.smCanvas = smCanvas;
    }

    /**
     * Returns the current transition that the state machine made.
     * @return the transition state machine made.
     */
    public IgstkvtSMVIZTransition getCurrentTransition() {
        return currentTransition;
    }

    /**
     * Sets the transition the state machine made.
     * @param currentTransition the transition state machine made.
     */
    public void setCurrentTransition(IgstkvtSMVIZTransition currentTransition) {
        this.currentTransition = currentTransition;
        this.currentTransition.setGcColor(SWT.COLOR_RED);
    }

    /**
     * Returns a list of previous transitions the state machine made.
     * @return List of previous transitions.
     */
    public List<IgstkvtSMVIZTransition> getPreviousTransitions() {
        return previousTransitions;
    }

    /**
     * Adds a transition to the list of previous transitions.
     * @param transition the transition to be added.
     */
    public void addPreviousTransition(IgstkvtSMVIZTransition transition) {
        previousTransitions.add(transition);
    }

    /**
     * Returns the current state the state machine is in.
     * @return the current state of state machine.
     */
    public IgstkvtSMVIZState getCurrentState() {
        return currentState;
    }

    /**
     * Sets the current state of the state machine.
     * @param currentState the current state of state machine.
     */
    public void setCurrentState(IgstkvtSMVIZState currentState) {
        this.currentState = currentState;
        this.currentState.setGcFillColor(SWT.COLOR_RED);
    }

    /**
     * Sends the next event to the state machine.
     * @param event The event passed to the state machine.
     * @return Returns true if the transition can be made based on the event.
     *         Returns false otherwise.
     */
    public boolean sendNextEvent(IgstkvtSendEvent event) {
        return sendNextEvent(event.getEventName());
    }

    /**
     * Sends the next transition input to the state machine. Checks to see
     * if the transition can be made and color codes the states and transitions
     * to represent the transition made.
     * @param transitionInput the transition input to be passed to the
     *        state machine.
     * @return Returns true if the transition can be made based on the input.
     *         Returns false otherwise.
     */
    public boolean sendNextEvent(String transitionInput) {
        IgstkvtSMVIZTransition transition = stateMachine.
            getTransition(currentState.getName()
                + "_" + transitionInput);

        if (transition != null) {
            if (currentTransition != null) {
                stateMachine.getState(currentTransition.
                        getStartState().getName()).setGcColor(SWT.COLOR_BLACK);
                stateMachine.getState(currentTransition.getStartState().
                        getName()).setGcFillColor(SWT.COLOR_WHITE);

                currentTransition.setGcColor(SWT.COLOR_BLACK);
                addPreviousTransition(currentTransition);
            }

            setCurrentState(stateMachine.
                        getState(transition.getEndState().getName()));

            setCurrentTransition(transition);
            smCanvas.redraw();
            return true;
        }
        return false;
    }

	/**
	 * Observe method listening for transition changes from state machine.
	 * This method colors the state machine displayed based on the current state.
	 */
	public void update(Observable arg0, Object arg1) {
		if(currentTransition != null) {
			currentTransition.getStartState().setGcFillColor(SWT.COLOR_WHITE);
			currentTransition.getEndState().setGcFillColor(SWT.COLOR_WHITE);
			currentTransition.setGcColor(SWT.COLOR_BLACK);
		}

		currentTransition = stateMachine.getCurrentTransition();

		if(currentTransition != null) {
			currentTransition.getStartState().setGcFillColor(SWT.COLOR_RED);
			currentTransition.getEndState().setGcFillColor(SWT.COLOR_RED);
			currentTransition.setGcColor(SWT.COLOR_RED);
		}	else {
			stateMachine.getInitialState().setGcFillColor(SWT.COLOR_RED);
		}
		smCanvas.redraw();
	}
}
