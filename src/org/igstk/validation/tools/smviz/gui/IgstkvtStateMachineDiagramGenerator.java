/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachineDiagramGenerator.java
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Transform;

import org.igstk.validation.tools.smviz.IgstkvtSMVIZState;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;

/**
 * Provides methods to calculate coordinate values and draw state machine in
 * a <tt>Canvas</tt> widget.
 * @author Rakesh Kukkamalla
 * @version 
 * @since  jdk1.5
 */
public class IgstkvtStateMachineDiagramGenerator {

    /**
     * Sets the coordinate values needed by <tt>Canvas</tt> widget to draw 
     * the state machine.
     * 
     * @param stateMachine The state machine whose coordinate values 
     *                     need to be set.
     */
    public void setGCValues(IgstkvtSMVIZStateMachine stateMachine) {

        int canvasWidth = 0;

        int xCoordinate = 50 
        		+ stateMachine.getInitialState().getName().length() * 3 / 2;
        int yCoordinate = 150;
        int width = 20;
        int height = 20;

        int topYCoordinate = 100;
        int bottomYCoordinate = 200;
        int transitionArcHeight = stateMachine.getTransitionArcHeight();

        for (IgstkvtSMVIZState state : stateMachine.getStates()) {

            state.setGcXCoordinate(xCoordinate);
            state.setGcYCoordinate(yCoordinate);

            state.setGcWidth(width);
            state.setGcHeight(height);
            xCoordinate += stateMachine.getDistanceBetweenStates();
            canvasWidth = xCoordinate;
        }

        IgstkvtSMVIZTransition previousTransition = null;

        for (IgstkvtSMVIZTransition transition : stateMachine
                .getTransitionsInGroupOrder().values()) {

            IgstkvtSMVIZState startState = transition.getStartState();
            IgstkvtSMVIZState endState = transition.getEndState();

            // Condition to check that its not a self transition.
            if (startState != endState) {

                if (startState.getGcXCoordinate() < endState.getGcXCoordinate()){

                    xCoordinate = startState.getGcXCoordinate()
                            + (startState.getGcWidth() / 2);

                    width = endState.getGcXCoordinate()
                            - startState.getGcXCoordinate();

                    // Condition to check if the previous transition was between
                    // the same states.
                    // This condition is checked so that the height of the arc
                    // drawn can be varied
                    // so that it does not overlap with an arc already drawn
                    // before.
                    if (previousTransition != null
                            && previousTransition.getStartState() 
                            	== transition.getStartState()
                            && previousTransition.getEndState() 
                            	== transition.getEndState()) {
                        height = previousTransition.getGcHeight() + 60;
                    } else {
                        height = ((width * 2) / 5) + transitionArcHeight;
                    }

                    yCoordinate = startState.getGcYCoordinate() - (height / 2);

                    transition.setGcStartAngle(0);
                    transition.setGcArcAngle(180);

                    if (topYCoordinate > yCoordinate) {
                        topYCoordinate = yCoordinate;
                    }
                } else {
                    xCoordinate = endState.getGcXCoordinate()
                            + (endState.getGcWidth() / 2);

                    width = startState.getGcXCoordinate()
                            - endState.getGcXCoordinate();

                    // Condition to check if the previous transition was between
                    // the same states.
                    // This condition is checked so that the height of the arc
                    // drawn can be varied
                    // so that it does not overlap with an arc already drawn
                    // before.
                    if (previousTransition != null
                            && previousTransition.getStartState() 
                            	== transition.getStartState()
                            && previousTransition.getEndState() 
                            	== transition.getEndState()) {
                        height = previousTransition.getGcHeight() + 60;
                    } else {
                        height = ((width * 2) / 5) + transitionArcHeight;
                    }

                    yCoordinate = (endState.getGcYCoordinate() + endState
                            .getGcHeight())
                            - (height / 2);

                    transition.setGcStartAngle(0);
                    transition.setGcArcAngle(-180);

                    if (bottomYCoordinate < (height + yCoordinate)) {
                        bottomYCoordinate = height + yCoordinate;
                    }
                }
            }else {
                // Condition to check if the previous transition was between the
                // same states.
                // This condition is checked so that the height of the arc drawn
                // can be varied
                // so that it does not overlap with an arc already drawn before.
                if (previousTransition != null
                        && previousTransition.getStartState() 
                        	== transition.getStartState()
                        && previousTransition.getEndState() 
                        	== transition.getEndState()) {
                    width = previousTransition.getGcWidth() + 80;
                } else {
                    width = startState.getGcWidth() + 40;
                }

                height = startState.getGcHeight();

                xCoordinate = startState.getGcXCoordinate() - (width / 2)
                        + (startState.getGcWidth() / 2);
                yCoordinate = startState.getGcYCoordinate();

                transition.setGcStartAngle(-90);
                transition.setGcArcAngle(180);

                if (canvasWidth < (xCoordinate + width)) {
                    canvasWidth = xCoordinate + width;
                }
            }

            transition.setGcXCoordinate(xCoordinate);
            transition.setGcYCoordinate(yCoordinate);
            transition.setGcWidth(width);
            transition.setGcHeight(height);

            previousTransition = transition;
        }

        stateMachine.setCanvasWidth(canvasWidth);
        stateMachine.setCanvasHeight(bottomYCoordinate - topYCoordinate + 100);

        for (IgstkvtSMVIZState state : stateMachine.getStates()) {
            state.setGcYCoordinate(state.getGcYCoordinate() - topYCoordinate
                    + 50);
        }

        for (IgstkvtSMVIZTransition transition : stateMachine.getTransitions()) {
            transition.setGcYCoordinate(transition.getGcYCoordinate()
                    - topYCoordinate + 50);
        }
    }

    /**
     * Draws the state machine in a canvas using the coordinate values in 
     * the state machine. <tt>Canvas</tt> widget paint listener can use this
     * method to draw the state machine.
     * 
     * NOTE: <tt>setGCValues()</tt> should be called on the state machine first
     *        before calling this method.
     * 
     * @param gc the graphics class of SWT for drawing the state machine.
     * @param stateMachine The state machine to be drawn.
     */
    public void drawStateMachineDiagram(GC gc, 
    				IgstkvtSMVIZStateMachine stateMachine) {

        // Draw all the states
        for (IgstkvtSMVIZState state : stateMachine.getStates()) {
            gc.setForeground(gc.getDevice().getSystemColor(state.getGcColor()));
            gc.setLineWidth(4);
            gc.drawOval(state.getGcXCoordinate(), state.getGcYCoordinate(),
                    state.getGcWidth(), state.getGcHeight());
            gc.setBackground(gc.getDevice().getSystemColor(
                    state.getGcFillColor()));
            gc.fillOval(state.getGcXCoordinate(), state.getGcYCoordinate(),
                    state.getGcWidth(), state.getGcHeight());
        }
        
        gc.setLineWidth(2);
        gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

        // Arrows that show transition directions.
        Image rightArrow = new Image(gc.getDevice(),
                IgstkvtStateMachineDiagramGenerator.class
                        .getResourceAsStream("/RightArrow.jpg"));
        Image leftArrow = new Image(gc.getDevice(),
                IgstkvtStateMachineDiagramGenerator.class
                        .getResourceAsStream("/LeftArrow.jpg"));
        Image downArrow = new Image(gc.getDevice(),
                IgstkvtStateMachineDiagramGenerator.class
                        .getResourceAsStream("/DownArrow.jpg"));

        // Draw all transitions.
        for (IgstkvtSMVIZTransition transition : stateMachine
                .getTransitionsInGroupOrder().values()) {
            // Get the foreground color for the transition to be drawn.
            gc.setForeground(gc.getDevice().getSystemColor(
                    transition.getGcColor()));
            IgstkvtSMVIZState fromState = stateMachine.getState(transition
                    .getStartState().getName());
            IgstkvtSMVIZState toState = stateMachine.getState(transition
                    .getEndState().getName());

            int xCoordinate = transition.getGcXCoordinate();
            int yCoordinate = transition.getGcYCoordinate();
            int width = transition.getGcWidth();
            int height = transition.getGcHeight();
            int startAngle = transition.getGcStartAngle();
            int arcAngle = transition.getGcArcAngle();

            // Condition to check if its not a self transition.
            if (!fromState.getName().equalsIgnoreCase(toState.getName())) {

                if (fromState.getGcXCoordinate() < toState.getGcXCoordinate()) {

                    gc.drawArc(xCoordinate, yCoordinate, width, height,
                            startAngle, arcAngle);
                    gc.drawImage(rightArrow, xCoordinate + (width / 2) - 5,
                            yCoordinate - 5);
                }else {
                    gc.drawArc(xCoordinate, yCoordinate, width, height,
                            startAngle, arcAngle);
                    gc.drawImage(leftArrow, xCoordinate + (width / 2) - 5,
                            yCoordinate + height - 6);
                }
            } else {
                gc.drawArc(xCoordinate, yCoordinate, width, height, startAngle,
                        arcAngle);
                gc.drawImage(downArrow, xCoordinate + width - 9, yCoordinate
                        + height / 2 - 5);
            }
        }

        nameStatesAndTransitions(gc, stateMachine);
        rightArrow.dispose();
        leftArrow.dispose();
        downArrow.dispose();
    }

    /**
     * Labels the states and transitions on the canvas widget.
     * 
     * @param gc the graphics class of SWT for drawing the state machine.
     * @param stateMachine The state machine to be drawn.
     */
    private void nameStatesAndTransitions(GC gc,
            IgstkvtSMVIZStateMachine stateMachine) {

        Font font = new Font(gc.getDevice(), "", stateMachine.getFontSize(),
                SWT.NORMAL);
        gc.setFont(font);

        // Name the states.
        for (IgstkvtSMVIZState state : stateMachine.getStates()) {
            gc.setForeground(gc.getDevice().getSystemColor(state.getGcColor()));
            gc.drawString(state.getName(), state.getGcXCoordinate()
                    - ((state.getName().length() / 2) * 3), state
                    .getGcYCoordinate()
                    + state.getGcHeight());
        }

        // Name the transitions.
        for (IgstkvtSMVIZTransition transition : stateMachine
                .getTransitionsInGroupOrder().values()) {

            gc.setForeground(gc.getDevice().getSystemColor(
                    transition.getGcColor()));

            IgstkvtSMVIZState fromState = stateMachine.getState(transition
                    .getStartState().getName());
            IgstkvtSMVIZState toState = stateMachine.getState(transition
                    .getEndState().getName());

            if (!fromState.getName().equalsIgnoreCase(toState.getName())) {

                if (fromState.getGcXCoordinate() < toState.getGcXCoordinate()) {
                    gc.drawString(transition.getEvent(),
                            transition.getGcXCoordinate()
                                    + (transition.getGcWidth() / 2)
                                    - ((transition.getEvent()
                                            .length() / 2) * 5), transition
                                    .getGcYCoordinate() - 18);
                }else {
                    gc.drawString(transition.getEvent(),
                            transition.getGcXCoordinate()
                                    + (transition.getGcWidth() / 2)
                                    - ((transition.getEvent()
                                            .length() / 2) * 4), transition
                                    .getGcYCoordinate()
                                    + transition.getGcHeight() + 5);
                }
            } else {

                Transform t = new Transform(gc.getDevice());
                t.translate(transition.getGcXCoordinate()
                        + transition.getGcWidth() + 20, transition
                        .getGcYCoordinate()
                        + transition.getGcHeight()
                        / 2
                        - ((transition.getEvent().length() / 2) * 5));
                t.rotate(90f);

                gc.setTransform(t);
                gc.drawText(transition.getEvent(), 0, 0);

                t.rotate(-90f);
                t.translate(-(transition.getGcXCoordinate()
                        + transition.getGcWidth() + 20), -(transition
                        .getGcYCoordinate()
                        + transition.getGcHeight() / 2 - ((transition
                        .getEvent().length() / 2) * 5)));
                gc.setTransform(t);
                t.dispose();
            }
        }
    }
}
