/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtProtocols.java
 * Language:  Java
 * Date:      Nov 3, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.igstk.validation.IgstkvtState;
import org.igstk.validation.IgstkvtStateMachine;
import org.igstk.validation.IgstkvtTransition;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZState;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;
import org.igstk.validation.tools.smviz.util.IgstkvtStringComparator;

/**
 * Class to code and decode protocols.
 * @author janakiram
*/
public class IgstkvtProtocols {
	/**
	 * Method to create string for new SM protocol.
	 * @param sm
	 * @return
	 */
	public static String createNewStateMachineProtocol(IgstkvtStateMachine sm){
		Collection<IgstkvtState> states = sm.getStates();
		Collection<IgstkvtTransition> transitions = sm.getTransitions();

		//Protocol: new+SM+SMID+SMName+state1;state2;state3+
		//state1-event1-state2;state2-event2-state3

		StringBuffer returnSBuffer = new StringBuffer();
		returnSBuffer.append("new=SM=" + sm.getId() + "=" + sm.getName() + "=");
		IgstkvtState initialState = sm.getInitialState();
		returnSBuffer.append(initialState.getName()+ ";");
		for(IgstkvtState state: states){
			if(!state.getName().equals(initialState.getName())){
				returnSBuffer.append(state.getName() + ";");
			}
		}
		returnSBuffer.append("=");
		for(IgstkvtTransition transition : transitions){
			returnSBuffer.append(transition.getStartState().
					getName() + "-" +transition.getEvent() + "-"
					+transition.getEndState().getName() + ";");
		}
		return returnSBuffer.toString();
	}

	/**
	 * Method to create the update protocol string.
	 * @param sm
	 * @return
	 */
	public static String createUpdateStateMachineProtocol(
			IgstkvtStateMachine sm){
		StringBuffer sBuffer = new StringBuffer();

		//Protocol: update+SM+SMID+state3-event10-state7
		sBuffer.append("update=SM=" + sm.getId() + "=");
		sBuffer.append(sm.getCurrentTransition().getStartState().
				getName() + "-" + sm.getCurrentTransition().
				getEvent() + "-" + sm.getCurrentTransition().
				getEndState().getName());
		return sBuffer.toString();
	}

	/**
	 * Method to get the states from the protocol.
	 * @param newSMProtocol
	 * @return
	 */
	public static List<IgstkvtSMVIZState> getSMVIZStatesFromProtocol(
			String newSMProtocol){
		return null;
	}

	/**
	 * Method to get transitions from protocol.
	 * @param newSMProtocol
	 * @return
	 */
	public static List<IgstkvtSMVIZTransition> getSMVIZTransitionsFromProtocol(
			String newSMProtocol){
		return null;
	}

	/**
	 * Method to create SMVIZStateMachine.
	 * @param newSMProtocol
	 * @return
	 */
	public static IgstkvtSMVIZStateMachine getSMVIZStateMachineFromProtocol(
			String newSMProtocol){
		String[] stringArray = newSMProtocol.split("=");
		IgstkvtSMVIZStateMachine smvizStateMachine =
			new IgstkvtSMVIZStateMachine();
		IgstkvtStringComparator stringComparator = new IgstkvtStringComparator();
        Map<String, IgstkvtSMVIZState> states =
        	new TreeMap<String, IgstkvtSMVIZState>(stringComparator);
        Map<String, IgstkvtSMVIZTransition> transitions =
        	new LinkedHashMap<String, IgstkvtSMVIZTransition>();
        for(String state: stringArray[4].split(";")){
        	IgstkvtSMVIZState smvizState = new IgstkvtSMVIZState();
        	smvizState.setName(state);
        	states.put(state, smvizState);
        }

        for(String transition: stringArray[5].split(";")){
        	String[] tempArray = transition.split("-");
        	IgstkvtSMVIZTransition smvizTransition =
        		new IgstkvtSMVIZTransition();
        	smvizTransition.setStartState(states.get(tempArray[0]));
        	smvizTransition.setEndState(states.get(tempArray[2]));
        	smvizTransition.setEvent(tempArray[1]);

        	String key = smvizTransition.getStartState().getName() + "_"
            	+ smvizTransition.getEvent();
        	transitions.put(key, smvizTransition);
        }



		if(stringArray[1] != null && stringArray[1].equals("SM")){
			smvizStateMachine.setId(stringArray[2]);
			smvizStateMachine.setName(stringArray[3]);
			smvizStateMachine.setStates(states);
			smvizStateMachine.setTransitions(transitions);
			smvizStateMachine.setInitialState(states.get(
					stringArray[4].split(";")[0]));
		}
		return smvizStateMachine;
	}

	/**
	 *Method to get the transition key from update protocol.
	 * @param updateProtocol
	 * @return
	 */
	public static String getTransitionKeyFromUpdate(String updateProtocol){
		String[] stringArray = updateProtocol.split("=");
		String[] transitionStringArray = stringArray[3].split("-");
		return transitionStringArray[0]+ "_" + transitionStringArray[1];
	}
}
