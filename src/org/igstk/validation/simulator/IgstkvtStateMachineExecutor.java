/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachineExecutor.java
 * Language:  Java
 * Date:      Apr 27, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.Tracer;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.apache.log4j.Logger;

import org.igstk.validation.IgstkvtStateMachine;
import org.igstk.validation.IgstkvtTransition;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtSendEvent;
import org.igstk.validation.util.IgstkvtSCXMLParser;

/**
 * @author Janakiram Dandibhotla
 * @version
 * @since jdk1.5
 */
public class IgstkvtStateMachineExecutor {

    private SCXMLExecutor scxmlExecutor;
    private IgstkvtStateMachine stateMachine;
    static Logger log = Logger.
                    getLogger(IgstkvtStateMachineExecutor.class.getName());
    private List<Object> listeners = new ArrayList<Object>();

    /**
     * This is a constructor for the class.
     * @param id
     * @param name
     * @param scxml
     */
    public IgstkvtStateMachineExecutor(String id, String name, SCXML scxml)
        throws IgstkvtConfigurationException{
        setStateMachine(id,name,scxml);
        setScxmlExecutor(scxml);
    }

    /**
     * This is the method to add a listener to the
     * changes made to this state machine.
     * @param obj
     */
    public void addListener(Object obj) {
        this.listeners.add(obj);
    }

    /**
     * This is the method used to notify all the registered listeners.
     * @param message
     */
    public void notifyListeners(String message) {
        ((IgstkvtVM) listeners.iterator().next()).notification(message);
    }

    /**
     * @param scxmlExecutor
     */
    private void setScxmlExecutor(SCXML scxml) {
        try{
            Evaluator evaluator = new JexlEvaluator();
            EventDispatcher eventdispatcher = new SimpleDispatcher();
            Tracer trc = new Tracer();
            Context context = new JexlContext();
            scxmlExecutor = new SCXMLExecutor(evaluator, eventdispatcher, null);
            scxmlExecutor.setStateMachine(scxml);
            scxmlExecutor.addListener(scxml, trc);
            scxmlExecutor.setRootContext(context);
            scxmlExecutor.setSuperStep(true);
            scxmlExecutor.go();
        } catch (ModelException e) {
            log.error("ModelException occurred \n " + "Exiting...");
            e.printStackTrace();
            log.error("An Error Occurred");
        }
    }

    /**
     * The method which is invoked to cause a trigger on this state machine.
     * @param sendEvent
     * @throws IgstkvtInvalidEventException
     */
    public void triggerEvent(IgstkvtSendEvent sendEvent)
        throws IgstkvtInvalidEventException{

        if(sendEvent == null){
            throw new IgstkvtInvalidEventException("Send Event is null.");
        }
        TriggerEvent triggerEvent = new TriggerEvent(sendEvent.getEventName(),
                                TriggerEvent.SIGNAL_EVENT);
        try {
            String currentStateId = null;
            Set currentStates = null;

            currentStates = scxmlExecutor.getCurrentStatus().getStates();

            currentStateId = ((State) currentStates.iterator().next()).getId();

            scxmlExecutor.triggerEvent(triggerEvent);

            IgstkvtTransition transition = stateMachine.getTransition(
                currentStateId+ "_" +sendEvent.getEventName());
            if(transition == null){
            	throw new IgstkvtInvalidEventException(
            			"The transition could not be made!");
            }
            stateMachine.setCurrentTransition(transition);

        }catch (ModelException me) {
            me.printStackTrace();
            throw new IgstkvtInvalidEventException(
                        "ModelException Occurred", me, sendEvent);
        }
    }

    /**
     * The method which is invoked to cause a trigger on this state machine.
     * @param sendEvent
     * @param lastStep
     * @throws IgstkvtInvalidEventException
     */
    public void rewind(IgstkvtSendEvent sendEvent, boolean lastStep)
        throws IgstkvtRewindException{

        if(sendEvent == null){
            throw new IgstkvtRewindException("Send Event is null.");
        }
        TriggerEvent triggerEvent = new TriggerEvent(sendEvent.getEventName(),
                                TriggerEvent.SIGNAL_EVENT);
        try {
            String currentStateId = null;

            if(lastStep){
                Set currentStates = null;
                currentStates = scxmlExecutor.getCurrentStatus().getStates();
                currentStateId = ((State) currentStates.
                                    iterator().next()).getId();
            }

            scxmlExecutor.triggerEvent(triggerEvent);

            if(lastStep){
                IgstkvtTransition transition = stateMachine.getTransition(
                    currentStateId+ "_" +sendEvent.getEventName());
                stateMachine.setCurrentTransition(transition);
            }

        }catch (ModelException me) {
            me.printStackTrace();
            throw new IgstkvtRewindException(
                        "ModelException Occurred", me, sendEvent);
        }
    }

    /**
     * This method is used to reset the executor for this state machine.
     */
    public void reset(){
        try {
            scxmlExecutor.reset();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates the stateMachine and sets it.
     * @param id
     * @param name
     * @param scxml
     */
    private void setStateMachine(String id,String name,SCXML scxml)
        throws IgstkvtConfigurationException{
        if(scxml != null){
            this.stateMachine = IgstkvtSCXMLParser.getIgstkvtStateMachine(scxml);
            if(id == null){
                throw new IgstkvtConfigurationException(
                            "ID is null. Please set the id attribute.");
            }
            if(name == null){
                throw new IgstkvtConfigurationException(
                            "Name is null. Please set the name property.");
            }
            stateMachine.setId(id);
            stateMachine.setName(name);
        }else {
            throw new IgstkvtConfigurationException("SCXML is null!!");
        }
    }

    /**
     * This method is a getter for state machine variable.
     * @return
     */
    public IgstkvtStateMachine getStateMachine() {
        return stateMachine;
    }

    /**
     * Getter for StateMachine Name. This is for Drools.See igstk.drl file.
     * @return
     */
    public String getStateMachineName(){
        return stateMachine.getName();
    }

    /**
     * Getter for State Machine Id. This is for Drools.See igstk.drl files.
     * @return
     */
    public String getStateMachineId(){
        return stateMachine.getId();
    }

    /**
     * Getter for current state. This is for Drools. See igstk.drl file.
     * @return
     */
    public String getCurrentState(){
        IgstkvtTransition transition =  stateMachine.getCurrentTransition();
        if(transition != null){
            return transition.getEndState().getName();
        }else {
            return stateMachine.getInitialState().getName();
        }
    }

}
