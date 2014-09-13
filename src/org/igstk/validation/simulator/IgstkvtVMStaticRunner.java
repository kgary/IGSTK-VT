/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtVMStaticRunner.java
 * Language:  Java
 * Date:      Nov 3, 2008
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

import org.apache.log4j.Logger;
import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.generator.IgstkvtGeneratorException;

/**
 * Class to run the simulator without SMVIZ.
 * @author janakiram
*/
public class IgstkvtVMStaticRunner {

	static Logger log = Logger.getLogger(IgstkvtVMStaticRunner.class);

	/**
	 * This is the main method to start execution.
	 * @param args
	 */
	public static void main(String args[]) {
		IgstkvtSimulatorServiceInterface simEng = new
			IgstkvtSimulatorServiceImpl();
		try {
			simEng.init("main");
			simEng.fireEvents("main",-1,1000);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			log.error("Exception Occurred: " + e.getMessage());
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			log.fatal(e.getMessage());
		} catch (IgstkvtInvalidEventException e) {
			log.error("InvalidEventException occurred.");
			e.printStackTrace();
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Inside static runner:"+IgstkvtVM.failures);
		if (IgstkvtVM.failures){
			System.exit(1);
		}
	}

}
