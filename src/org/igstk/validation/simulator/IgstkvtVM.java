/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtVM.java
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

package org.igstk.validation.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.apache.log4j.Logger;
import org.igstk.validation.IgstkvtConfigProps;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.generator.IgstkvtSendEvent;
import org.igstk.validation.simulator.rules.IgstkvtDroolsObserver;
import org.igstk.validation.simulator.rules.
	IgstkvtTestExecutionContextProperties;
import org.igstk.validation.simulator.util.IgstkvtGeneratorTest;
import org.igstk.validation.simulator.util.IgstkvtVMHelper;
import org.igstk.validation.util.IgstkvtProtocols;

/**
 * @author Janakiram Dandibhotla
 * @version
 * @since jdk1.5
 */
class IgstkvtVM extends Observable {

    private ArrayList<IgstkvtSendEvent> eventsStore  =
                        new ArrayList<IgstkvtSendEvent>();

    private ArrayList<String> failedRules = new ArrayList<String>();
    static Logger log = Logger.getLogger(IgstkvtVM.class.getName());
    private Map<String, IgstkvtStateMachineExecutor> executors;
    private Map<String,Integer> eventsCountPerExecutor =
            new HashMap<String, Integer>();

    // Generator Proxy instance.. Change it to ProxyGenerator when done with
    // ProxyGenerator.
    private IgstkvtGeneratorTest generator;
    static public boolean failures;

    /**
     * This method initializes the variables, executors and loads the
     * variables with the required properties.
     * @throws IgstkvtFatalConfigException
     */
    public void init(IgstkvtConfigProps configProps)
        throws IgstkvtConfigurationException, IgstkvtFatalConfigException{

    	if(configProps == null){
    		throw new IgstkvtFatalConfigException("configProps is null");
    	}
        IgstkvtTestExecutionContextProperties contextProperties =
            new IgstkvtTestExecutionContextProperties();
        List<IgstkvtStateMachineExecutor> objectsOnRulesToBefired =
        	new ArrayList<IgstkvtStateMachineExecutor>();
        contextProperties.setObjectsToFireRulesOn(objectsOnRulesToBefired);
        generator = new IgstkvtGeneratorTest();
        executors = IgstkvtVMHelper.createExecutors(configProps);
        generator.init();
        IgstkvtDroolsObserver rulesObserver = new IgstkvtDroolsObserver();
        rulesObserver.setConfigprops(configProps);
        rulesObserver.setContextProperties(contextProperties);
        this.addObserver(rulesObserver);
        for(IgstkvtStateMachineExecutor exec : executors.values()){
            setChanged();
            log.info("The protocol string is: " + IgstkvtProtocols.
            		createNewStateMachineProtocol(exec.getStateMachine()));
            notifyObservers(IgstkvtProtocols.createNewStateMachineProtocol(
            		exec.getStateMachine()));
            //Drools stuff.
            objectsOnRulesToBefired.add(exec);
            exec.addListener(this);
        }

        //Initialize
        for(String id : executors.keySet()){
            eventsCountPerExecutor.put(id,0);
        }
    }

    /**
     * This method helps to remember the events already triggered.
     * @param event
     */
	private void eventStoreProcess(IgstkvtSendEvent event) {
		eventsStore.add(event);
		int count = eventsCountPerExecutor.get(event.getId());
		count++;
		eventsCountPerExecutor.put(event.getId(),count);
	}

    /**
     * This method is called by the main function.
     * If called with a negative number, events will be fired
     * till the stream ends. IF called with a positive
     * number, the stream will end when the number of events
     * are fired or the events stream ends, which ever
     * is less.
     * This method is <b> not  </b>called by the UI.
     * @throws IgstkvtInvalidEventException
     * @throws IgstkvtGeneratorException
     * @throws InterruptedException
     */
    public void fireEvents(int n, int delayBetweenEventsInMillis)
    	throws IgstkvtInvalidEventException, IgstkvtGeneratorException,
    	InterruptedException {
        IgstkvtSendEvent event;

        if(n < 0){
            while ((event = generator.getNextEvent()) != null) {
            	triggerEventOnStateMachine(event);
	        	Thread.sleep(delayBetweenEventsInMillis);
            }
        }else {
            for (int count = 0;n>0 && count != n;count++) {
            	event = generator.getNextEvent();
            	if(event!= null){
            		triggerEventOnStateMachine(event);
            		Thread.sleep(delayBetweenEventsInMillis);
            	}
            }
        }
    }

    /**
     * This method triggers the event on the respective StateMachine.
     * @param event
     * @throws IgstkvtInvalidEventException
     */
	private void triggerEventOnStateMachine(IgstkvtSendEvent event)
		throws IgstkvtInvalidEventException {
		if(executors.get(event.getId()) != null){
			executors.get(event.getId()).triggerEvent(event);
			eventStoreProcess(event);
		    log.info("The update string is: " + IgstkvtProtocols.
		    		createUpdateStateMachineProtocol(executors.get(
		    				event.getId()).getStateMachine()));
		    setChanged();
		    notifyObservers(IgstkvtProtocols.createUpdateStateMachineProtocol(
		    		executors.get(event.getId()).getStateMachine()));
		}else {
			System.out.println("There is no such executor!.");
			throw new IgstkvtInvalidEventException(
					"There is no such Executor with Id:" + event.getId());
		}
	}


    /**
     * This is method is used to rewind back to certain steps.
     * @param n
     * @return
     * @throws IgstkvtInvalidEventException
     */
    public void rewind(int n) throws IgstkvtRewindException{
        log.info("Event store size is: " + eventsStore.size());
        if(n > eventsStore.size()){
            log.error("You cannot rewind " + n + " times!!");
            throw new IgstkvtRewindException("No sufficient steps to go back!!");
        }else {
            resetStateMachines();
            moveSMsForwardAfterReset(n);
            generator.rewind(n);
            resetEventsStore(n);

           //Loop to check the size .
            for(String id : executors.keySet()){
                int count = eventsCountPerExecutor.get(id);
                if(count <= 0){
                    executors.get(id).getStateMachine().
                        setCurrentTransition(null);
                }
            }
        }
    }

    /**
     * This method moves back the event store to the
     * current position of the state machines.
     * @param n
     */
	private void resetEventsStore(int n) {
		int size = eventsStore.size();
		for(int i = eventsStore.size() -1; i > (size - n -1);i--){
		    log.info("I is : " + i);
		    IgstkvtSendEvent event = eventsStore.get(i);
		    int count = eventsCountPerExecutor.get(event.getId());
		    count--;
		    eventsCountPerExecutor.put(event.getId(), count);
		    eventsStore.remove(i);
		}
	}

	/**
	 * This method allows the state machines from their original state to move
	 * forward to the required number of steps.
	 * @param n
	 * @throws IgstkvtRewindException
	 */
	private void moveSMsForwardAfterReset(int n) throws IgstkvtRewindException {
		int loopCount = eventsStore.size() - n;

		for(int i = 0;i < loopCount; i++){
			IgstkvtSendEvent event;
		    if((event = eventsStore.get(i)) != null){
		        if(i == loopCount -1){
		            executors.get(event.getId()).rewind(event,true);
		            setChanged();
				    notifyObservers(IgstkvtProtocols.
				    		createUpdateStateMachineProtocol(executors.get(
				    				event.getId()).getStateMachine()));
		        }else {
		            executors.get(event.getId()).rewind(event,false);
		            setChanged();
				    notifyObservers(IgstkvtProtocols.
				    		createUpdateStateMachineProtocol(executors.get(
				    				event.getId()).getStateMachine()));
		        }

		    }else {
		        log.error("Event is null.");
		    }
		}
	}

	/**
	 * This method helps in resetting all the state
	 * machines to their original state.
	 * @throws IgstkvtRewindException
	 */
	private void resetStateMachines() throws IgstkvtRewindException {
		for(Iterator<IgstkvtSendEvent> eventsIter = eventsStore.iterator();
		                            eventsIter.hasNext();){
		    IgstkvtSendEvent event = eventsIter.next();
		    if(event != null){
		    	if(executors.get(event.getId()) != null){
		    		executors.get(event.getId()).reset();
		    	}else{
		    		throw new IgstkvtRewindException("No such Statemachine!.");
		    	}
		    }else {
		        log.error("Event is null!!!!");
		    }
		}
	}

    /**
     * This method notifies the listeners if there is any rule failed.
     * @param errorMessage
     */
    public void notification(String errorMessage) {
        log.warn("Failed rule is: " + errorMessage);
        failedRules.add(errorMessage);
      	if (failedRules.isEmpty()){
     		failures=false;
     	}else{
     		failures=true;
     	}
     		
    }
}
