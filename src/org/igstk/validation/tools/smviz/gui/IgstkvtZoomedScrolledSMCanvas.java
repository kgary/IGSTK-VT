/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtZoomedScrolledSMCanvas.java
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.igstk.validation.tools.codegeneration.CodeGeneration;
import org.igstk.validation.tools.codegeneration.CodeGenerationFactory;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;

/**
 * Custom widget GUI class to draw state machines. State machine diagrams
 * displayed through this widget are scrollable and zoomable.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtZoomedScrolledSMCanvas extends Composite {

    private Label smNameLabel;
    private ToolBar smZoomToolBar;
    private ScrolledComposite smScrolledComposite;
    private Canvas smCanvas;
    private int sashFormWeight;
    private boolean disableAnimationButton;

    private final IgstkvtSMVIZStateMachine stateMachine;

    /**
     * Sole constructor for the class. Creates a new
     * <tt>IgstkvtZoomedScrolledSMCanvas</tt> widget to display a state machine.
     * @param parent the parent widget.
     * @param style the style of this composite.
     * @param stateMachine the state machine to be drawn.
     */
    public IgstkvtZoomedScrolledSMCanvas(Composite parent, int style,
            IgstkvtSMVIZStateMachine stateMachine) {
        super(parent, style);
        this.stateMachine = stateMachine;
    }

    /**
     * Sole constructor for the class. Creates a new
     * <tt>IgstkvtZoomedScrolledSMCanvas</tt> widget to display a state machine.
     * @param parent the parent widget.
     * @param style the style of this composite.
     * @param stateMachine the state machine to be drawn.
     */
    public IgstkvtZoomedScrolledSMCanvas(Composite parent, int style,
            IgstkvtSMVIZStateMachine stateMachine,
            boolean disableAnimationWindow) {
        super(parent, style);
        this.stateMachine = stateMachine;
        if(disableAnimationWindow) {
        	this.disableAnimationButton = disableAnimationWindow;
        }
    }


    /**
     * Returns the state machine name.
     * @return state machine name.
     */
    public Label getSmNameLabel() {
        return smNameLabel;
    }

    /**
     * Creates a new label to display the state machine name.
     */
    private void setSmNameLabel() {

        this.smNameLabel = new Label(this, 0);
        smNameLabel.setText(stateMachine.getId() + " : "
                + stateMachine.getName());

        Font font = new Font(this.getDisplay(), "", 12, SWT.BOLD);
        smNameLabel.setFont(font);

        smNameLabel.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent arg0) {

                if (smScrolledComposite.getSize().y == 0) {
                    smScrolledComposite.setSize(
                            smScrolledComposite.getSize().x,
                            312 - smZoomToolBar.getSize().y);
                    setSize(getSize().x, 312);
                } else {
                    smScrolledComposite.setSize(
                            smScrolledComposite.getSize().x, 0);
                    setSize(getSize().x, smZoomToolBar.getSize().y + 3);
                }

                if (getParent() instanceof SashForm) {

                    SashForm sashForm = (SashForm) getParent();
                    int[] weights = sashForm.getWeights();
                    int weightIndex = 0;
                    int sashFormHeight = 0;

                    for (Control c : sashForm.getChildren()) {
                        sashFormHeight += c.getSize().y;

                        if (c instanceof IgstkvtZoomedScrolledSMCanvas) {
                            weights[weightIndex] = c.getSize().y;
                            weightIndex++;
                        }
                    }

                    sashForm.setSize(sashForm.getSize().x, sashFormHeight);
                    sashForm.setWeights(weights);
                }
            }

            public void mouseDown(MouseEvent arg0) {
            }

            public void mouseUp(MouseEvent arg0) {
            }

        });
    }

    /**
     * Returns the tool bar widget of this widget.
     * @return tool bar of the widget.
     */
    public ToolBar getSmZoomToolBar() {
        return smZoomToolBar;
    }

    /**
     * Creates a new tool bar for the widget and adds buttons to it.
     */
    private void setSmZoomToolBar() {
        this.smZoomToolBar = new ToolBar(this, SWT.HORIZONTAL);

        ToolItem zoomInToolItem = new ToolItem(smZoomToolBar, SWT.PUSH);
        Image zoomIn = new Image(this.getDisplay(),
                IgstkvtZoomedScrolledSMCanvas.class
                        .getResourceAsStream("/ExpandArrow.jpg"));
        zoomInToolItem.setImage(zoomIn);
        zoomInToolItem.setToolTipText("Increase distance between states");

        zoomInToolItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                IgstkvtStateMachineDiagramGenerator smGenerator =
                        new IgstkvtStateMachineDiagramGenerator();
                stateMachine.setDistanceBetweenStates(stateMachine
                        .getDistanceBetweenStates() + 50);
                stateMachine.setFontSize(stateMachine.getFontSize() + 1);
                smGenerator.setGCValues(stateMachine);
                smCanvas.setSize(stateMachine.getCanvasWidth(), stateMachine
                        .getCanvasHeight());
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        ToolItem zoomOutToolItem = new ToolItem(smZoomToolBar, SWT.PUSH);
        Image zoomOut = new Image(this.getDisplay(),
                IgstkvtZoomedScrolledSMCanvas.class
                        .getResourceAsStream("/ContractArrow.jpg"));
        zoomOutToolItem.setImage(zoomOut);

        zoomOutToolItem.setToolTipText("Decrease distance between States");

        zoomOutToolItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                IgstkvtStateMachineDiagramGenerator smGenerator =
                        new IgstkvtStateMachineDiagramGenerator();
                stateMachine.setDistanceBetweenStates(stateMachine
                        .getDistanceBetweenStates() - 50);
                stateMachine.setFontSize(stateMachine.getFontSize() - 1);
                smGenerator.setGCValues(stateMachine);
                smCanvas.setSize(stateMachine.getCanvasWidth(), stateMachine
                        .getCanvasHeight());
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        ToolItem expandCurveToolItem = new ToolItem(smZoomToolBar, SWT.PUSH);
        Image increaseArcHeight = new Image(this.getDisplay(),
                IgstkvtZoomedScrolledSMCanvas.class
                        .getResourceAsStream("/IncreaseArcHeight.jpg"));
        expandCurveToolItem.setImage(increaseArcHeight);
        expandCurveToolItem.setToolTipText("Increase height of arcs");

        expandCurveToolItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                stateMachine.setTransitionArcHeight(
                		stateMachine.getTransitionArcHeight() + 50);

                IgstkvtStateMachineDiagramGenerator smGenerator =
                    new IgstkvtStateMachineDiagramGenerator();
                smGenerator.setGCValues(stateMachine);
                smCanvas.setSize(stateMachine.getCanvasWidth(), stateMachine
                        .getCanvasHeight());
                smCanvas.redraw();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });

        ToolItem reduceCurveToolItem = new ToolItem(smZoomToolBar, SWT.PUSH);
        Image decreaseArcHeight = new Image(this.getDisplay(),
                IgstkvtZoomedScrolledSMCanvas.class
                        .getResourceAsStream("/DecreaseArcHeight.jpg"));
        reduceCurveToolItem.setImage(decreaseArcHeight);
        reduceCurveToolItem.setToolTipText("Decrease height of arcs");

        reduceCurveToolItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
            	stateMachine.setTransitionArcHeight(
            			stateMachine.getTransitionArcHeight() - 50);
            	IgstkvtStateMachineDiagramGenerator smGenerator =
                    new IgstkvtStateMachineDiagramGenerator();
                smGenerator.setGCValues(stateMachine);
                smCanvas.setSize(stateMachine.getCanvasWidth(), stateMachine
                        .getCanvasHeight());
                smCanvas.redraw();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });
        
        /** Added for code generation **/
        
        ToolItem codeGenerationToolItem = new ToolItem(smZoomToolBar, SWT.PUSH);
        Image generateCodeImg = new Image(this.getDisplay(),
                IgstkvtZoomedScrolledSMCanvas.class
                        .getResourceAsStream("/CodeGenPic.png"));
        codeGenerationToolItem.setImage(generateCodeImg);
        codeGenerationToolItem.setToolTipText("Generate Code");

        codeGenerationToolItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
            	
            	/** Generate the code for the selected diagram **/
        
             	//System.out.println("name = " + stateMachine.getName());
             	    	
             	String scxmlFileName = stateMachine.getName();
            
             	CodeGeneration codeGenerator = CodeGenerationFactory
             									.getGenerator(scxmlFileName);
             	      	
             	codeGenerator.generate();
            	
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
        });
        
        /** End Code Generation icon block **/
        

        if(!disableAnimationButton) {
	        ToolItem animateButton = new ToolItem(smZoomToolBar, SWT.PUSH);
	        Image animate = new Image(this.getDisplay(),
	                IgstkvtZoomedScrolledSMCanvas.class
	                        .getResourceAsStream("/Animate.jpg"));
	        animateButton.setImage(animate);
	        //animateButton.setText("Animate");
	        animateButton.setToolTipText("Open new window for animation");

	        animateButton.addSelectionListener(new SelectionListener() {
	            public void widgetSelected(SelectionEvent event) {
	                IgstkvtSingleStateMachineAnimationWindow smAnimation =
	                		    new IgstkvtSingleStateMachineAnimationWindow(
	                		    	getShell(), SWT.NULL, stateMachine.clone());
	                smAnimation.open();
	            }

	            public void widgetDefaultSelected(SelectionEvent event) {
	            }
	        });
        }

        smZoomToolBar.pack();
    }

    /**
     * Returns the <tt>ScrolledComposite</tt> widget.
     * @return
     */
    public ScrolledComposite getSmScrolledComposite() {
        return smScrolledComposite;
    }

    /**
     * Creates a scrolled composite for the widget and adds a canvas to it
     * where state machines are drawn.
     */
    private void setSmScrolledComposite() {
        this.smScrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL
                | SWT.V_SCROLL);

        GridData gridData = new GridData();
        gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        gridData.widthHint = 650;
        gridData.heightHint = 430;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        smScrolledComposite.setLayoutData(gridData);

        smCanvas = new Canvas(smScrolledComposite, SWT.NULL);
        smCanvas.setBackground(this.getDisplay()
                .getSystemColor(SWT.COLOR_WHITE));

        smScrolledComposite.setContent(smCanvas);
        smScrolledComposite.setMinSize(smCanvas.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
        smScrolledComposite.setBackground(this.getDisplay().getSystemColor(
                SWT.COLOR_WHITE));

        final IgstkvtStateMachineDiagramGenerator smDiagramGenerator =
        			new IgstkvtStateMachineDiagramGenerator();
        smDiagramGenerator.setGCValues(stateMachine);
        smCanvas.setSize(stateMachine.getCanvasWidth(), stateMachine
                .getCanvasHeight());

        smCanvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (stateMachine != null) {
                    smDiagramGenerator.drawStateMachineDiagram(e.gc,
                            stateMachine);
                }
            }
        });
    }

    /**
     * Returns the <tt>Canvas</tt> widget where state machines are drawn.
     * @return canvas where state machine is drawn.
     */
    public Canvas getSmCanvas() {
        return smCanvas;
    }

    /**
     * Returns the weight of the composite in a sash form widget.
     * @return weight of the widget in a sash form.
     */
    public int getSashFormWeight() {
    	return sashFormWeight;
    }

    /**
     * Sets the weight of the widget if added as a child to a <tt>SashForm</tt>.
     * @param sashFormWeight weight of the widget when added to a
     *                       <tt>SashForm</tt>.
     */
    public void setSashFormWeight(int sashFormWeight) {
        this.sashFormWeight = sashFormWeight;
    }

    /**
     * Returns the state machine drawn in the widget.
     * @return IgstkvtStateMachine state machine drawn in this widget.
     */
    public IgstkvtSMVIZStateMachine getStateMachine() {
        return stateMachine;
    }

    /**
     * Sets all the widgets that are children to this widget and draws
     * the state machine.
     */
    public void viewStateMachineDiagram() {

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.horizontalSpacing = 20;
        this.setLayout(gridLayout);

        setSmNameLabel();
        setSmZoomToolBar();
        setSmScrolledComposite();
    }
}
