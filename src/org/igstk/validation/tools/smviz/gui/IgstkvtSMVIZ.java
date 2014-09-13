/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSMVIZ.java
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
package org.igstk.validation.tools.smviz.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.igstk.validation.IgstkvtStateMachine;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.simulator.IgstkvtSimulatorServiceImpl;
import org.igstk.validation.simulator.IgstkvtSimulatorServiceInterface;
import org.igstk.validation.simulator.IgstkvtStateMachineExecutor;
import org.igstk.validation.simulator.util.IgstkvtVMHelper;
import org.igstk.validation.tools.smviz.IgstkvtSMVIZStateMachine;
import org.igstk.validation.util.IgstkvtProtocols;
import org.igstk.validation.util.IgstkvtSCXMLParser;

/**
 * The main class of SMVIZ tool.
 * @author Rakesh Kukkamalla
 * @version
 * @since  jdk1.5
 */
public class IgstkvtSMVIZ {

	private static int defaultId = 3;  // this is a hack for now
	
	/**
	 * The starting point of SMVIZ tool.
	 * @param args
	 */
	public static void main(String [] args) {

        // Creating display and shell
        final Display display = new Display();
        final Shell shell = new Shell(display, SWT.MIN | SWT.MAX | SWT.CLOSE);
        shell.setText("IgstkvtState Machine Diagram");
        shell.setSize(900, 600);
        shell.setLayout(new FillLayout());

        // Creating a menu bar
        Menu menuBar = new Menu(shell, SWT.BAR);

        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
        fileOpenItem.setText("&Open");

        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("&Exit");

        shell.setMenuBar(menuBar);

        final IgstkvtSimulatorServiceInterface simulator =
        	new IgstkvtSimulatorServiceImpl();

        final IgstkvtMainWindow displayWindow = new IgstkvtMainWindow(shell,
        		SWT.BORDER, simulator);
        displayWindow.layoutWidgets();

        displayWindow.setSize(850, 550);

        fileOpenItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                FileDialog fileChooser = new FileDialog(shell, SWT.OPEN);
                fileChooser.setText("Open SCXML File");
                fileChooser.setFilterExtensions(new String[] {"*.xml;" });
                fileChooser.setFilterNames(new String[] {"SCXML File"
                        + " (xml)" });
                String filename = fileChooser.open();
                if (filename != null) {
                    try {
                    	// Changed by Dr. Gary to read from a file.
                    	IgstkvtStateMachineExecutor executor = IgstkvtVMHelper.createExecutor(filename, "" + defaultId++, filename);
                        IgstkvtStateMachine stateMachine = executor.getStateMachine();
                        String smProtocol = IgstkvtProtocols.createNewStateMachineProtocol(stateMachine);
                        IgstkvtSMVIZStateMachine smvizSM = IgstkvtProtocols.getSMVIZStateMachineFromProtocol(smProtocol);
                        	new IgstkvtSMVIZStateMachine();
                        displayWindow.getSashForm().addZoomedScrolledSMCanvas(smvizSM);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

        fileExitItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                display.dispose();
            }
        });

        shell.pack();
        shell.open();

        try {
			simulator.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setMessage("Configuration Error. "
					+"  Could not open configuration file.");
			messageBox.setText("Error!");
			messageBox.open();
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setMessage("Configuration Error. IGSTKVT_HOME "
					+"environment variable not set.");
			messageBox.setText("Error!");
			messageBox.open();
			display.dispose();
			System.exit(1);
		}

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }
}
