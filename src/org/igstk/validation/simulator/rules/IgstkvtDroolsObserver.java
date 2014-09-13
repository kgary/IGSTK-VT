/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtDroolsObserver.java
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
package org.igstk.validation.simulator.rules;

import java.util.Observable;
import java.util.Observer;

import org.igstk.validation.IgstkvtConfigProps;

/**
 *Rules observer.
 * @author janakiram
*/
public class IgstkvtDroolsObserver implements Observer {

	private IgstkvtTestServiceExecutorInterface ruleExecutor = new
		IgstkvtDroolsTestExecutor();

	/**
	 * Setter for context properties.
	 * @param contextProperties
	 */
	public void setContextProperties(IgstkvtTestExecutionContextProperties
				contextProperties){
		ruleExecutor.setContextProperties(contextProperties);
	}
	
	public void setConfigprops (IgstkvtConfigProps configProps){
		ruleExecutor.setDroolFiles(configProps.getDroolFiles());
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		ruleExecutor.execute();
	}
}
