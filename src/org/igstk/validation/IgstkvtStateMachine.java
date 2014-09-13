/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachine.java
 * Language:  Java
 * Date:      April 15, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.igstk.validation.tools.smviz.util.IgstkvtStringComparator;

/**
 * Class representing a state machine.
 * @author janakiram
 * @version
 * @since  jdk1.5
 */
public class IgstkvtStateMachine {

	private String id;
    private String name;
    private IgstkvtState initialState;
    private Map<String, IgstkvtState> states;
    private Map<String, IgstkvtTransition> transitions;
    private IgstkvtTransition currentTransition;

    /**
     * Constructor for the class.
     */
    public IgstkvtStateMachine() {
        this.states = new LinkedHashMap<String, IgstkvtState>();
        this.transitions = new LinkedHashMap<String, IgstkvtTransition>();
    }

    /**
     * Constructor for the class.
     * @param name name of the state machine.
     */
    public IgstkvtStateMachine(String name) {
        this();
        this.name = name;
    }

	/**
	 * Getter for ID.
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter for Id.
	 * @param id
	 */
	public void setId(String id) {
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
     * Sets the name of the state machine.
     * @param name name of the state machine.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the initial state.
     * @return initial state.
     */
    public IgstkvtState getInitialState() {
		return initialState;
	}

    /**
     * Sets the initial state.
     * @param initialState
     */
	public void setInitialState(IgstkvtState initialState) {
		this.initialState = initialState;
	}

	/**
     * Returns the states present in the state machine.
     * @return Map of states present in the state machine.
     */
    public Collection<IgstkvtState> getStates() {
        return states.values();
    }

    /**
     * Sets states of the state machine.
     * @param states map of states in the state machine.
     */
    public void setStates(Map<String, IgstkvtState> states) {
        this.states = states;
    }

    /**
     * Returns map of transitions made by states in the state machine.
     * @return Map of transitions made by states.
     */
    public Collection<IgstkvtTransition> getTransitions() {
        return transitions.values();
    }

    /**
     * Sets the transitions made by states in the state machine.
     * @param transitions transitions made by states in the state machine.
     */
    public void setTransitions(SortedMap<String,
            IgstkvtTransition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Adds a state to the state machine.
     * @param state state of a state machine.
     */
    public void addState(IgstkvtState state) {
        this.states.put(state.getName(), state);
    }

    /**
     * Adds a transition made by a state in the state machine.
     * @param transition transition made by state.
     */
    public void addTransition(IgstkvtTransition transition) {
        String key = transition.getStartState().getName() + "_"
                + transition.getEvent();

        this.transitions.put(key, transition);
    }

    /**
     * Returns a state in the state machine.
     * @param stateName name of the state.
     * @return state of the state machine.
     */
    public IgstkvtState getState(String stateName) {
        return states.get(stateName);
    }

    /**
     * Returns a transition made by one of the states in the state machine.
     * @param startStateName_event key value passed to the map of
     *                             transitions.
     * @return transition made by one of the states.
     */
    public IgstkvtTransition getTransition(String startStateName_event) {
        return transitions.get(startStateName_event);
    }

    /**
     * Getter for Current transition.
     * @return
     */
    public IgstkvtTransition getCurrentTransition() {
		return currentTransition;
	}

	/**
	 * Setter for current transition.
	 * @param currentTransition
	 */
	public void setCurrentTransition(IgstkvtTransition currentTransition) {
		this.currentTransition = currentTransition;
	}

	/**
     * Returns map of transition in order. Transitions are grouped together
     * such that transitions from state S1 to state S2 are consecutive entries
     * in the map for any given state S1 and S2 of the state machine.
     * @return returns map of transitions in grouped order.
     */
    public Map<String, IgstkvtTransition> getTransitionsInGroupOrder() {

        IgstkvtStringComparator stringComparator = new IgstkvtStringComparator();
        SortedMap<String, IgstkvtTransition> sortedTransitions =
                    new TreeMap<String, IgstkvtTransition>(stringComparator);

        for (IgstkvtTransition transition : transitions.values()) {
            String key = transition.getStartState().getName() + "_"
                    + transition.getEndState().getName() + "_"
                    + transition.getEvent();

            sortedTransitions.put(key, transition);
        }
        return sortedTransitions;
    }

    /**
     * Method to clone the state machine.
     * @author
     * @param
     * @throws
     */
    public IgstkvtStateMachine clone() {

    	IgstkvtStateMachine stateMachine = new IgstkvtStateMachine(name);
    	stateMachine.setId(id);
    	stateMachine.setInitialState(initialState.clone());

    	for(IgstkvtState state : states.values()) {
    		stateMachine.addState(state.clone());
    	}

    	for(IgstkvtTransition transition : transitions.values()) {
    		IgstkvtTransition igstkvtTransition = new IgstkvtTransition(
    				transition.getEvent());
    		igstkvtTransition.setStartState(stateMachine.getState(
    				transition.getStartState().getName()));
    		igstkvtTransition.setEndState(stateMachine.getState(
    				transition.getEndState().getName()));
    		stateMachine.addTransition(igstkvtTransition);
    	}
    	return stateMachine;
    }
}
