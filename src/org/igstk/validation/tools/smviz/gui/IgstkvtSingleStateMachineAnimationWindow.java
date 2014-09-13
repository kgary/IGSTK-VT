/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSingleStateMachineAnimation.java
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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZState;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;

/**
 * GUI class which provides animation capabilities for a single state machine.
 * @author Rakesh Kukkamalla
 * @version 
 * @since  jdk1.5
 */
public class IgstkvtSingleStateMachineAnimationWindow extends Composite {

    private Shell animationWindow;
    private SashForm sashForm;
    private ScrolledComposite eventGroup;
    private final IgstkvtSMVIZStateMachine stateMachine;
    private IgstkvtZoomedScrolledSMCanvas smCanvas;

    /**
     * Sole constructor for the class.
     * @param parent the parent widget.
     * @param style style to be applied to this widget.
     * @param stateMachine the state machine that needs animation.
     */
    public IgstkvtSingleStateMachineAnimationWindow(Composite parent, int style,
            IgstkvtSMVIZStateMachine stateMachine) {
        super(parent, style);
        this.stateMachine = stateMachine;
    }

    /**
     * Gets the animation window.
     * @return Shell
     */
    public Shell getAnimationWindow() {
        return animationWindow;
    }

    /**
     * Instantiates a new <tt>Shell</tt> widget.
     */
    private void setAnimationWindow() {
        animationWindow = new Shell(this.getDisplay());
        animationWindow.setLocation(10, 10);
        animationWindow.setSize(900, 600);
        animationWindow.setLayout(new FillLayout());

    }

    /**
     * Returns the <tt>SashForm</tt> widget.
     * @return SashForm
     */
    public SashForm getSashForm() {
        return sashForm;
    }

    /**
     * Creates a <tt>SashForm</tt> widget in the animation window.
     * State machine is painted in one part of the <tt>SashForm</tt>.
     */
    private void setSashForm() {
        sashForm = new SashForm(animationWindow, SWT.HORIZONTAL);
        sashForm.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

        eventGroup = new ScrolledComposite(sashForm, SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL);

        eventGroup.setExpandHorizontal(true);
        eventGroup.setExpandVertical(true);  
        
        smCanvas = new IgstkvtZoomedScrolledSMCanvas(sashForm, SWT.BORDER,
                stateMachine, true);

        smCanvas.viewStateMachineDiagram();
        addTransitionsToGroup();
        int[] weights = new int[2];
        weights[0] = 30;
        weights[1] = 70;

        sashForm.setWeights(weights);
    }

    /**
     * Adds transitions as radio buttons to the <tt>SashForm</tt> widget.
     * Users can select the radio buttons which generate events for the
     * state machine and paint it with colors displaying which transition
     * was selected.
     */
    private void addTransitionsToGroup() {
        int yCoordinate = 20;
        IgstkvtSMVIZState state = stateMachine.getStates().iterator()
                .next();

        final IgstkvtStateMachineAnimator smAnimator = 
        	new IgstkvtStateMachineAnimator(stateMachine, 
        								smCanvas.getSmCanvas());

        final Button startState = new Button(eventGroup, SWT.RADIO);
        startState.setText(state.getName());
        startState.setLocation(10, yCoordinate);
        startState.setSize(state.getName().length() * 20, 25);
        int width = state.getName().length();

        for(IgstkvtSMVIZState smvizState : stateMachine.getStates()) {
        	for(String event : smvizState.getTransitionEvents()) {
	        	IgstkvtSMVIZTransition transition = stateMachine.getTransition(
	        			smvizState.getName() + "_" + event);
	        
	            final Button button = new Button(eventGroup, SWT.RADIO);
	            button.setText(transition.getStartState().getName() + "_"
	                    + transition.getEvent());
	            yCoordinate += 20;
	            button.setLocation(10, yCoordinate);
	            button.setSize((smvizState.getName().length() 
	            		+ transition.getEvent().length() + 1) * 20, 25);
	            if (width < transition.getEvent().length()) {
	                width = transition.getEvent().length();
	            }
	            button.addSelectionListener(new SelectionListener() {
	
	                public void widgetDefaultSelected(SelectionEvent arg0) {
	                }
	
	                public void widgetSelected(SelectionEvent arg0) {
	                    IgstkvtSMVIZTransition t = stateMachine.getTransition(
	                    		button.getText());
	                    smAnimator.sendNextEvent(t.getEvent());
	                }
	            });
	        }
        }
        eventGroup.pack();
    }
    
    /**
     * 
     */
    public void open() {
    	setAnimationWindow();
        setSashForm();
        animationWindow.open();
    }
}
