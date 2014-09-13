/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtMain.java
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

import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.simulator.IgstkvtSimulatorServiceInterface;

/**
 * The main window that will be displayed to the user.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtMainWindow extends Composite {

	private ToolBar toolBar;
	private IgstkvtScrolledSMSashform sashForm;
	private IgstkvtSimulatorServiceInterface simulator;

	/**
	 * Sole constructor for the class.
	 * @param parent the parent widget.
	 * @param style style that has to be applied to the current composite.
	 * @param simulator the
	 */
	public IgstkvtMainWindow(Composite parent, int style,
			IgstkvtSimulatorServiceInterface simulator) {
		super(parent, style);
		this.simulator = simulator;
	}

	/**
	 * Lays out widgets in the Composite.
	 */
	public void layoutWidgets() {
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;

        this.setLayout(gridLayout);

		setToolBar();
		setSashForm();
		try {
			simulator.addAsObserver(sashForm,"main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the tool bar for the Composite and adds listeners.
	 */
	private void setToolBar() {
		toolBar = new ToolBar(this, SWT.HORIZONTAL);

		GridData gridData = new GridData();
        gridData.heightHint = 25;
        toolBar.setLayoutData(gridData);

        ToolItem showEventButton = new ToolItem(toolBar, SWT.PUSH);
        showEventButton.setText("Event");

        ToolItem startAnimationButton = new ToolItem(toolBar, SWT.PUSH);
        startAnimationButton.setText("Start Animation");

        ToolItem stopAnimationButton = new ToolItem(toolBar, SWT.PUSH);
        stopAnimationButton.setText("Stop Animation");

        ToolItem rewindButton = new ToolItem(toolBar, SWT.PUSH);
        rewindButton.setText("Rewind");


        final Text rewindStepCount = new Text(this, SWT.BORDER);

        final Timer[] timer = new Timer[1];

        showEventButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
            	try {
            		if(simulator != null) {
            			simulator.fireEvents("main", 1, 0);
            		}
            	}catch (IgstkvtInvalidEventException e) {
            		e.printStackTrace();
            		MessageBox messageBox = new MessageBox(getShell());
            		messageBox.setMessage("An invalid event "
            				+ "has been passed to a state machine.");
            		messageBox.open();
            	} catch (IgstkvtGeneratorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        startAnimationButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                timer[0] = null;
                timer[0] = new Timer();
                IgstkvtSimulatorEventTimerTask seTimerTask =
                		new IgstkvtSimulatorEventTimerTask(
                		            simulator, getDisplay());
                timer[0].scheduleAtFixedRate(seTimerTask, 100, 3000);
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        stopAnimationButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                if (timer[0] != null) {
                    timer[0].cancel();
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        rewindButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {

            	MessageBox messageBox = new MessageBox(getShell());

            	try {
            		if(simulator != null) {
            			simulator.rewind("main",Integer.
            			            parseInt(rewindStepCount.getText()));
            			messageBox.setMessage(
            			            "State machines have been rewound");
            		}
            	}catch (NumberFormatException nfe) {
            		messageBox.setMessage("Please enter a valid number");
            	} catch (IgstkvtRewindException ire) {
					ire.printStackTrace();
					messageBox.setMessage(
					          "Number of steps given exceeds the event count.");
				}finally {
            		messageBox.open();
            	}
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });
	}

	/**
	 * Returns the Composite that displays the state machines.
	 * @return composite that displays the state machines.
	 */
	public IgstkvtScrolledSMSashform getSashForm() {
		return sashForm;
	}

	/**
	 * Sets the sashform widget that displays the state machines.
	 */
	private void setSashForm() {

		this.sashForm = new IgstkvtScrolledSMSashform(this, SWT.BORDER);

		GridData gridData = new GridData();
        gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        gridData.widthHint = 650;
        gridData.heightHint = 430;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        sashForm.setLayoutData(gridData);

        sashForm.layoutWidgets();
	}

}
