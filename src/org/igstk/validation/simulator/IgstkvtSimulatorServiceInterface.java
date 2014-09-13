/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSimulatorServiceInterface.java
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

import java.util.Observer;

import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;

/**
 * Interface for simulator service.
 * @author janakiram
*/
public interface IgstkvtSimulatorServiceInterface {

	/**
	 * Method to initialize a virtual machine.
	 * @param vMachineName
	 * @throws IgstkvtConfigurationException
	 * @throws IgstkvtFatalConfigException
	 */
	public abstract void init(String vMachineName)
		throws IgstkvtConfigurationException, IgstkvtFatalConfigException;

	/**
	 * Method to fire events on the virtual machine.
	 * @param vMachineName
	 * @param n
	 * @param delayBetweenEventsInMillis
	 * @throws IgstkvtInvalidEventException
	 * @throws IgstkvtGeneratorException
	 * @throws InterruptedException
	 */
	public abstract void fireEvents(String vMachineName, int n,
			int delayBetweenEventsInMillis)
		throws IgstkvtInvalidEventException,
		IgstkvtGeneratorException, InterruptedException;

	/**
	 * Method to rewind the virtual machine.
	 * @param vMachineName
	 * @param numberOfSteps
	 * @throws IgstkvtRewindException
	 */
	public abstract void rewind(String vMachineName, int numberOfSteps)
		throws IgstkvtRewindException;

	/**
	 * Method to add a new virtual machine.
	 * @param vMachineName
	 */
	public abstract void addVirtualMachine(String vMachineName)
		throws IgstkvtConfigurationException;

	/**
	 * Method to remove a virtual machine.
	 * @param vMachineName
	 */
	public abstract void removeVirtualMachine(String vMachineName)
		throws IgstkvtConfigurationException;

	/**
	 * Method to add observers to virtual machine.
	 * @param observer
	 * @param vMachineName
	 */
	public abstract void addAsObserver(Observer observer,
			String vMachineName) throws IgstkvtConfigurationException;

	/**
	 * Method to delete observers to virtual machine.
	 * @param observer
	 * @param vMachineName
	 */
	public abstract void deleteAsObserver(Observer observer,
			String vMachineName) throws IgstkvtConfigurationException;
}