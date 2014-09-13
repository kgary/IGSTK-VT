/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStateMachineExecutorTest.java
 * Language:  Java
 * Date:      May 12, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.simulator;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.SCXML;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtSendEvent;
import org.igstk.validation.util.IgstkvtConfigParser;
import org.junit.Before;
import org.junit.Test;

/**
 * @author janakiramdandibhotla
 */
public class IgstkvtStateMachineExecutorTest {

    File stateFile;
    SCXML scxml;
    IgstkvtStateMachineExecutor exec;
    IgstkvtSendEvent event =
            new IgstkvtSendEvent("objectFileNameValidInput","1");

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        IgstkvtConfigParser configParser = IgstkvtConfigParser.getInstance(
        		IgstkvtConfigParser.getPropertiesFileNameFromPropertiesFile(
        				"igstkvt.properties"));
        stateFile = new File(configParser.getIGSTKVT_HOME()
                    + "xmlFiles/scxmlFiles/igstkSpatialObjectReader.xml");
        try {
            URL url = stateFile.toURL();
            System.out.println("The File is : " + url.toString());
            scxml = SCXMLDigester.digest(url, null);
            exec = new  IgstkvtStateMachineExecutor("1","SpatialObjectReader",scxml);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Test
    public void testIgstkvtStateMachineExecutor() {
        //Test 1
        //Testing if the constructor will throw exception.
        boolean caughtException = false;

        try {
            IgstkvtStateMachineExecutor smExec =
                    new IgstkvtStateMachineExecutor(null,null,null);
        } catch (IgstkvtConfigurationException e) {
            caughtException = true;
        }

        if(!caughtException){
            fail("This should throw an exception!");
        }

        //Test 2
        caughtException = false;

        try{
            IgstkvtStateMachineExecutor smExec =
                    new IgstkvtStateMachineExecutor(null,"SpatialObjectReader",scxml);
        }catch (IgstkvtConfigurationException e){
            caughtException = true;
        }

        if(!caughtException){
            fail("This should throw an exception!");
        }

        //Test 3
        caughtException = false;

        try{
            IgstkvtStateMachineExecutor smExec =
                    new IgstkvtStateMachineExecutor("1",null,scxml);
        }catch (IgstkvtConfigurationException e){
            caughtException = true;
        }

        if(!caughtException){
            fail("THis should throw an exception.");
        }

        //Test 4
        caughtException = false;

        try {
            IgstkvtStateMachineExecutor smExec =
                    new IgstkvtStateMachineExecutor("1","SpatialObjectReader",scxml);
        }catch (IgstkvtConfigurationException e){
            caughtException = true;
        }

        if(caughtException){
            fail("THis should NOT throw an exception!");
        }

    }

    /**
     *
     */
    @Test
    public void testTriggerEvent() {
        //Test 1
        //Testing sending null to trigger event.
        boolean caughtException = false;
        try {
            exec.triggerEvent(null);
        } catch (IgstkvtInvalidEventException e) {
            caughtException = true;
        }

        if(!caughtException){
            fail("This should throw an Exception.");
        }

        //Test 2
        //TEsting if the functionality works.
        caughtException = false;

        try{
            exec.triggerEvent(event);
        }catch (IgstkvtInvalidEventException e){
        	e.printStackTrace();
            caughtException = true;
        }

        if(caughtException){
            fail("This should NOT throw exception.");
        }

    }

    /**
     *
     */
    @Test
    public void testRewind() {
        //Test 1
        //Testing if rewind works with send event being null.
        boolean caughtException = false;
        try {
            exec.rewind(null, false);
        } catch (IgstkvtRewindException e) {
            caughtException = true;
        }

        if(!caughtException){
            fail("This should throw an Exception.");
        }

        //Test 2
        //TEsting if the functionality works.
        caughtException = false;

        try{
            exec.rewind(event, false);
        }catch (IgstkvtRewindException e){
            caughtException = true;
        }

        if(caughtException){
            fail("This should NOT throw exception.");
        }

    }

    /**
     *
     */
    @Test
    public void testReset() {
        //Test 1
        //Testing if reset works.

        exec.reset();

        assertTrue(exec.getCurrentState().equals("IdleState"));

    }

}
