/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtScrolledSMSashform.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZTransition;
import org.igstk.validation.util.IgstkvtProtocols;

/**
 * GUI class which displays multiple state machines.
 * The {@link ScrolledComposite} widget holds the {@link SashForm} widget
 * which displays multiple state machines.
 * @author Rakesh Kukkamalla
 * @version
 * @since jdk1.5
 */
public class IgstkvtScrolledSMSashform extends Composite implements Observer {

    private ScrolledComposite scrolledComposite;
    private SashForm sashForm;
    private Map<String,IgstkvtSMVIZStateMachine> smvizStateMachines =
    		new HashMap<String, IgstkvtSMVIZStateMachine>();

    /**
     * Sole constructor for the class. Constructs a new instance based on
     * the parent and the style value.
     * @param parent the {@link Composite} which will be parent of the
     *               new instance.
     * @param style the style of widget to construct.
     */
    public IgstkvtScrolledSMSashform(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * Returns the {@link ScrolledComposite} which holds the {@link SashForm}.
     * @return the {@link ScrolledComposite}
     */
    public ScrolledComposite getScrolledComposite() {
        return scrolledComposite;
    }

    /**
     * Creates a new instance of scrolled composite widget with the current
     * composite widget instance as parent.
     */
    private void setScrolledComposite() {

        this.scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL
                | SWT.V_SCROLL);

        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setBackground(this.getDisplay().getSystemColor(
                SWT.COLOR_WHITE));
    }

    /**
     * Returns the {@link SashForm} widget of the composite.
     * @return
     */
    public SashForm getSashForm() {
        return sashForm;
    }

    /**
     * Creates a new instance of {@link SashForm} widget with
     * {@link ScrolledComposite} as its parent.
     * This method should be called after setScrolledComposite method of this
     * class.
     */
    private void setSashForm() {

        this.sashForm = new SashForm(scrolledComposite, SWT.VERTICAL);

        scrolledComposite.setContent(sashForm);
    }

    /**
     * Adds {@link IgstkvtZoomedScrolledSMCanvas} widget to the composite.
     * @param smZCanvas instance of {@link IgstkvtZoomedScrolledSMCanvas}
     *                  widget to be added.
     */
    public void addZoomedScrolledSMCanvas(
            IgstkvtZoomedScrolledSMCanvas smZCanvas) {

    }

    /**
     * Adds {@link IgstkvtZoomedScrolledSMCanvas} widget to the composite.
     * Creates a new instance of the {@link IgstkvtZoomedScrolledSMCanvas}
     * widget and adds it to the composite.
     * @param stateMachine the {@link IgstkvtSMVIZStateMachine} to be drawn and
     *                     displayed.
     * @return
     */
    public IgstkvtZoomedScrolledSMCanvas addZoomedScrolledSMCanvas(
            IgstkvtSMVIZStateMachine stateMachine) {

        int[] weights = new int[sashForm.getWeights().length + 1];
        int weightIndex = 0;
        for (int i = 0; i < sashForm.getChildren().length; i++) {
            Control c = sashForm.getChildren()[i];
            if (c instanceof IgstkvtZoomedScrolledSMCanvas) {
                weights[weightIndex] = c.getSize().y;
                weightIndex++;
            }
        }
        sashForm.setSize(sashForm.getSize().x, sashForm.getSize().y + 315 + 3);

        IgstkvtZoomedScrolledSMCanvas smCanvas =
            new IgstkvtZoomedScrolledSMCanvas(sashForm, SWT.BORDER,
                    stateMachine);
        smCanvas.setSashFormWeight(315);
        smCanvas.viewStateMachineDiagram();

        weights[weightIndex] = 315;
        sashForm.setWeights(weights);
        return smCanvas;
    }

    /**
     * Lays out the widgets in this composite.
     * This method has to be called for the composite to display widgets.
     */
    public void layoutWidgets() {
        this.setLayout(new FillLayout());
        setScrolledComposite();
        setSashForm();
    }

    /**
     * Displays the state machine passed by simulator. Adds an animator
     * to the state machine.
     */
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String){
			if(((String) arg1).startsWith("new")){
				IgstkvtSMVIZStateMachine smvizStateMachine = IgstkvtProtocols.
					getSMVIZStateMachineFromProtocol((String) arg1);
				IgstkvtZoomedScrolledSMCanvas smZCanvas =
					addZoomedScrolledSMCanvas(smvizStateMachine);
				IgstkvtStateMachineAnimator smAnimator =
					new IgstkvtStateMachineAnimator(smvizStateMachine,smZCanvas.
							getSmCanvas());
				String id = ((String) arg1).split("=")[2];
				smvizStateMachine.addObserver(smAnimator);
				smvizStateMachines.put(id,smvizStateMachine);
			}else if(((String) arg1).startsWith("update")){
				System.out.println("The argument is: " + arg1);
				String id = ((String) arg1).split("=")[2];
				IgstkvtSMVIZStateMachine smvizStateMachine =
					smvizStateMachines.get(id);
				IgstkvtSMVIZTransition transition = smvizStateMachine.
				getTransition(IgstkvtProtocols.getTransitionKeyFromUpdate(
						(String) arg1));
				smvizStateMachine.setCurrentTransition(transition);
			}
        }
	}
}
