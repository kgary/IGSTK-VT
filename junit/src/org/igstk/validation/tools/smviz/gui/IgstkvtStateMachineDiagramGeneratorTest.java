/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachineDiagramGeneratorTest.java
 * Language:  Java
 * Date:      May 13, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.tools.smviz.gui;

import static org.junit.Assert.assertEquals;

import org.igstk.validation.tools.smviz.IgstkvtSMVIZState;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;
import org.igstk.validation.util.IgstkvtProtocols;
import org.junit.Test;

/**
 * @author Rakesh Kukkamalla
 */
public class IgstkvtStateMachineDiagramGeneratorTest {

	/**
	 * Testing a method.
	 */
	@Test
	public void testSetGCValues() {
		IgstkvtSMVIZStateMachine smvizStateMachine = null;
		smvizStateMachine = IgstkvtProtocols.
					getSMVIZStateMachineFromProtocol(
					"new=SM=1=Tracker=IdleState;CommunicationEstablishedState;"
					+"AttemptingToCloseCommunicationState;ToolsActiveState;"
					+"AttemptingToUpdateState;AttemptingToStopTrackingState;"
					+"AttemptingToTrackState;AttemptingToActivateToolsState;"
					+"AttemptingToEstablishCommunicationState;TrackingState;"
					+"=CommunicationEstablishedState-closeCommunicationInput-"
					+"AttemptingToCloseCommunicationState;CommunicationEstabli"
					+"shedState-activateToolsInput-AttemptingToActivateToolsSt"
					+"ate;AttemptingToCloseCommunicationState-successInput-I"
					+"dleState;AttemptingToCloseCommunicationState-failure"
					+"Input-CommunicationEstablishedState;ToolsActiveState-"
					+"resetInput-CommunicationEstablishedState;ToolsActive"
					+"State-closeCommunicationInput-AttemptingToCloseCommuni"
					+"cationState;ToolsActiveState-startTrackingInput-Attempt"
				    +"ingToTrackState;AttemptingToUpdateState-successInput-"
				    +"TrackingState;AttemptingToUpdateState-failureInput-"
				    +"TrackingState;IdleState-establishCommunicationInput-"
				    +"AttemptingToEstablishCommunicationState;AttemptingTo"
				    +"StopTrackingState-successInput-ToolsActiveState;Atte"
				    +"mptingToStopTrackingState-failureInput-TrackingState;"
				    +"AttemptingToTrackState-successInput-TrackingState;"
				    +"AttemptingToTrackState-failureInput-ToolsActiveState;"
				    +"AttemptingToActivateToolsState-successInput-ToolsAct"
				    +"iveState;AttemptingToActivateToolsState-failureInput-"
				    +"CommunicationEstablishedState;AttemptingToEstablishC"
				    +"ommunicationState-successInput-CommunicationEstablished"
				    +"State;AttemptingToEstablishCommunicationState-failure"
				    +"Input-IdleState;TrackingState-resetInput-Communication"
				    +"EstablishedState;TrackingState-closeCommunicationInput-"
				    +"AttemptingToCloseCommunicationState;TrackingState-update"
				    +"StatusInput-AttemptingToUpdateState;TrackingState-stopTrac"
				    +"kingInput-AttemptingToStopTrackingState;");


		IgstkvtStateMachineDiagramGenerator smDiagramGenerator =
			new IgstkvtStateMachineDiagramGenerator();


		smDiagramGenerator.setGCValues(smvizStateMachine);

		assertEquals("IdleState", smvizStateMachine.getInitialState().getName());

		for(IgstkvtSMVIZState state : smvizStateMachine.getStates()) {
			assertEquals(20, state.getGcHeight());
			assertEquals(20, state.getGcWidth());
		}

		assertEquals(190, smvizStateMachine.
		            getInitialState().getGcYCoordinate());

		IgstkvtSMVIZTransition smvizTransition = smvizStateMachine.getTransition(
				"IdleState_establishCommunicationInput");
		assertEquals("establishCommunicationInput", smvizTransition.getEvent());

		assertEquals("AttemptingToEstablishCommunicationState",
				smvizTransition.getEndState().getName());

		assertEquals(73, smvizTransition.getGcXCoordinate());
		assertEquals(130, smvizTransition.getGcYCoordinate());
		assertEquals(300, smvizTransition.getGcWidth());
		assertEquals(120, smvizTransition.getGcHeight());
	}

}
