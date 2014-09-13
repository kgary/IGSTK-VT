/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachine.java
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.igstk.validation.tools.smviz.util.IgstkvtStringComparator;

/**
 * Class representing a state machine. Wraps {@link IgstkvtStateMachine}.
 * Used for drawing by the smviz tool.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSMVIZStateMachine extends Observable{

    //private IgstkvtStateMachine stateMachine;
    private Map<String, IgstkvtSMVIZState> states;
    private Map<String, IgstkvtSMVIZTransition> transitions;

    private int distanceBetweenStates;
    private int canvasWidth;
    private int canvasHeight;
    private int fontSize;
    private int transitionArcHeight;
    private String id;
    private String name;
    private IgstkvtSMVIZState initialState;
    private IgstkvtSMVIZTransition currentTransition;

    /**
     * Constructor for the class.
     */
    public IgstkvtSMVIZStateMachine() {
    	transitions = new LinkedHashMap<String, IgstkvtSMVIZTransition>();
    	IgstkvtStringComparator stringComparator = new IgstkvtStringComparator();
        states = new TreeMap<String, IgstkvtSMVIZState>(stringComparator);
        setDistanceBetweenStates(100);
        setFontSize(7);
        setTransitionArcHeight(0);
    }

    /**
     * Returns the Id of the state machine.
     * @return
     */
    public String getId() {
    	return id;
    }

    /**
     * Setter for Id.
     * @param id
     */
    public void setId(String id){
    	this.id = id;
    }

    /**
     * Returns the name of the state machine.
     * @return name of the state machine.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param name
     */
    public void setName(String name){
    	this.name = name;
    }
    /**
     * Returns the initial start state.
     * @return initial start state.
     */
    public IgstkvtSMVIZState getInitialState() {
    	return initialState;
    }

    /**
     * Setter for initial State.
     * @param state
     */
    public void setInitialState(IgstkvtSMVIZState state){
    	this.initialState = state;
    }

    /**
     * Returns the states present in the state machine.
     * @return Collection of states present in the state machine.
     */
    public Collection<IgstkvtSMVIZState> getStates() {

    	Set<IgstkvtSMVIZState> tempStates =
    		new LinkedHashSet<IgstkvtSMVIZState>();

    	tempStates.add(getInitialState());

    	for(IgstkvtSMVIZState state : states.values()) {
    		tempStates.add(state);
    	}
    	return tempStates;
    }

    /**
     * Sets states of the state machine.
     */

    public void setStates(Map<String, IgstkvtSMVIZState> states) {
		this.states = states;
    }


    /**
     * Returns the transitions made by states in the state machine.
     * @return Collection of transitions made by states.
     */
    public Collection<IgstkvtSMVIZTransition> getTransitions() {
        return transitions.values();
    }

    /**
     * Sets the transitions made by states in the state machine.
     */
    public void setTransitions(Map<String, IgstkvtSMVIZTransition> transitions) {
		this.transitions = transitions;
    }

    /**
     * Adds a state to the state machine.
     * @param state state of a state machine.
     */
    public void addState(IgstkvtSMVIZState state) {
        states.put(state.getName(), state);
    }

    /**
     * Adds a transition made by a state in the state machine.
     * @param transition transition made by state.
     */
    public void addTransition(IgstkvtSMVIZTransition transition) {
        String key = transition.getStartState().getName() + "_"
                + transition.getEvent();

        transitions.put(key, transition);
    }

    /**
     * Returns a state in the state machine.
     * @param stateName name of the state.
     * @return state of the state machine.
     */
    public IgstkvtSMVIZState getState(String stateName) {
        return states.get(stateName);
    }

    /**
     * Returns a transition made by one of the states in the state machine.
     * @param startStateName_Event key value passed to the map of
     *                                  transitions.
     * @return transition made by one of the states.
     */
    public IgstkvtSMVIZTransition getTransition(String startStateName_Event) {
        return transitions.get(startStateName_Event);
    }

//    /**
//     * Method to add obeserver.
//     * @param animator
//     */
//    public void addObserver(IgstkvtStateMachineAnimator animator) {
//    	stateMachine.addObserver(animator);
//    }
//
    /**
     * Getter for current Transition.
     * @return
     */
    public IgstkvtSMVIZTransition getCurrentTransition() {

    	if (currentTransition == null) {
    		return null;
    	}
//    	return currentTransition;

    	return getTransition(currentTransition.getStartState().getName() + "_"
    			+ currentTransition.getEvent());
    }

    /**
     * Returns the distance between states when drawn in a canvas widget.
     * @return distance between states when drawn in a canvas widget.
     */
    public int getDistanceBetweenStates() {
        return distanceBetweenStates;
    }

    /**
     * Sets the distance that needs to be maintained between states when
     * drawn in a canvas widget.
     * @param distanceBetweenStates distance between states when drawn in a
     *                              canvas widget.
     */
    public void setDistanceBetweenStates(int distanceBetweenStates) {
        this.distanceBetweenStates = distanceBetweenStates;
    }

    /**
     * Returns the width of the canvas widget that draws the state machine.
     * @return width of the canvas widget that draws the state machine.
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * Sets the width of the canvas widget that draws the state machine.
     * @param canvasWidth width of the canvas widget that draws the
     *                    state machine.
     */
    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    /**
     * Returns the height of the canvas widget that draws the state machine.
     * @return height of the canvas widget that draws the state machine.
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Sets the height of the canvas widget that draws the state machine.
     * @param canvasHeight height of the canvas widget that draws the
     *                     state machine.
     */
    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    /**
     * Gets the font size of the state name and transition name labels when
     * drawn in a canvas widget.
     * @return font size of the state name and transition name labels.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size of the state name and transition name labels when
     * drawn in a canvas widget.
     * @param fontSize font size of the state name and transition name labels.
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Gets the height of the transition arcs to be drawn.
     * @return height of the transition arc.
     */
    public int getTransitionArcHeight() {
		return transitionArcHeight;
	}

    /**
     * Sets the height of the transition arcs to be drawn.
     * @param transitionArcHeight
     */
	public void setTransitionArcHeight(int transitionArcHeight) {
		this.transitionArcHeight = transitionArcHeight;
	}

	/**
     * Returns map of transition in order. Transitions are grouped together
     * such that transitions from state S1 to state S2 are consecutive entries
     * in the map for any given state S1 and S2 of the state machine.
     * @return returns map of transitions in grouped order.
     */
    public Map<String, IgstkvtSMVIZTransition> getTransitionsInGroupOrder() {

        IgstkvtStringComparator stringComparator = new IgstkvtStringComparator();
        SortedMap<String, IgstkvtSMVIZTransition> sortedTransitions =
                  new TreeMap<String, IgstkvtSMVIZTransition>(stringComparator);

        for (IgstkvtSMVIZTransition transition : transitions.values()) {
            String key = transition.getStartState().getName() + "_"
                    + transition.getEndState().getName() + "_"
                    + transition.getEvent();

            sortedTransitions.put(key, transition);
        }
        return sortedTransitions;
    }

    /**
     * Method to clone a state machine.
     * @author
     * @param
     * @throws
     */
    public IgstkvtSMVIZStateMachine clone() {

    	IgstkvtSMVIZStateMachine smvizStateMachine =
    		new IgstkvtSMVIZStateMachine();
    	IgstkvtStringComparator stringComparator = new IgstkvtStringComparator();
    	Map<String, IgstkvtSMVIZState> clonedStates =
    		new TreeMap<String, IgstkvtSMVIZState>(stringComparator);
        Map<String, IgstkvtSMVIZTransition> clonedTransitions=
        	new LinkedHashMap<String, IgstkvtSMVIZTransition>();

        for(String state : states.keySet()){
        	clonedStates.put(state, states.get(state).clone());
        	for(String transition: transitions.keySet()){
        		if(transition.startsWith(state)){
        			clonedStates.get(state).addTransitionEvent(
        					transitions.get(transition).getEvent());
        		}
        	}
        }

        for(String transition: transitions.keySet()){
        	IgstkvtSMVIZTransition clonedTransition = transitions.
						get(transition).clone();
        	clonedTransition.setStartState(clonedStates.
        			get(transitions.get(transition).getStartState().getName()));
        	clonedTransition.setEndState(clonedStates.
        			get(transitions.get(transition).getEndState().getName()));
        	clonedTransitions.put(transition, clonedTransition);

        }

    	smvizStateMachine.setCanvasHeight(canvasHeight);
    	smvizStateMachine.setCanvasWidth(canvasWidth);
    	smvizStateMachine.setDistanceBetweenStates(distanceBetweenStates);
    	smvizStateMachine.setFontSize(fontSize);
    	smvizStateMachine.setTransitionArcHeight(transitionArcHeight);
    	smvizStateMachine.setStates(clonedStates);
    	smvizStateMachine.setTransitions(clonedTransitions);
    	smvizStateMachine.setInitialState(clonedStates.get(
    			initialState.getName()));
    	smvizStateMachine.setName(name);
    	smvizStateMachine.setId(id);

    	return smvizStateMachine;
    }


    /**
     * Setter for Current Transition.
	 * @param currentTransition
	 */
	public void setCurrentTransition(IgstkvtSMVIZTransition currentTransition) {
		this.currentTransition = currentTransition;
		setChanged();
		notifyObservers();
	}

}
