/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSimulatorEventTimerTask.java
 * Language:  Java
 * Date:      Mar 12, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.tools.smviz.gui;

import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.simulator.IgstkvtSimulatorServiceInterface;

/**
 * A timer task to pull events from the simulator.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSimulatorEventTimerTask extends TimerTask {

    private IgstkvtSimulatorServiceInterface simulator;
    private Display display;

    /**
     * Constructor.
     * @param simulator
     * @param display
     */
    public IgstkvtSimulatorEventTimerTask(
    		IgstkvtSimulatorServiceInterface simulator,Display display) {
    	this.simulator = simulator;
    	this.display = display;
    }
    /**
     * Overriden method which would pull events from simulator.
     */
    public void run() {
        display.asyncExec(new Runnable() {
            public void run() {
                try {
                	if(simulator != null) {
                		simulator.fireEvents("main", 1, 1000);
                	}
				} catch (IgstkvtInvalidEventException e) {
					e.printStackTrace();
            		MessageBox messageBox =
            		        new MessageBox(display.getActiveShell());
            		messageBox.setMessage("An invalid "
            				+ "event has been passed to a state machine.");
            		messageBox.open();
				} catch (IgstkvtGeneratorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
