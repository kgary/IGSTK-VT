/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSimulatorServiceImpl.java
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

import java.util.HashMap;
import java.util.Observer;

import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.igstk.validation.util.IgstkvtConfigParser;
/**
 * Default implementation of SimulatorServiceInterface.
 *@author janakiram
*/
public class IgstkvtSimulatorServiceImpl
	implements IgstkvtSimulatorServiceInterface {

	private HashMap<String, IgstkvtVM> vMachines = new
		HashMap<String, IgstkvtVM>();

	/**
	 * Constructor.
	 */
	public IgstkvtSimulatorServiceImpl() {
		IgstkvtVM vMachine = new IgstkvtVM();
		vMachines.put("main", vMachine);
	}

	/**
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#init(java.lang.String)
	 */
	public void init(String vMachineName) throws
			IgstkvtConfigurationException, IgstkvtFatalConfigException{
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			IgstkvtConfigParser configParser = IgstkvtConfigParser.
				getInstance(IgstkvtConfigParser.
						getPropertiesFileNameFromPropertiesFile(
								"igstkvt.properties"));
			vMachine.init(configParser.getConfigProps());
		}else {
			throw new IgstkvtConfigurationException("No such virtual machine.");
		}
	}

	/**
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#fireEvent(java.lang.String, int)
	 */
	public void fireEvents(String vMachineName, int n,
			int delayBetweenEventsInMillis)
		throws IgstkvtInvalidEventException, IgstkvtGeneratorException,
		InterruptedException{
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			vMachine.fireEvents(n,delayBetweenEventsInMillis);
		}
	}

	/**
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#rewind(java.lang.String, int)
	 */
	public void rewind(String vMachineName, int numberOfSteps)
		throws IgstkvtRewindException{
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			vMachine.rewind(numberOfSteps);
		}
	}


	/**
	 * @throws IgstkvtConfigurationException
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#addVirtualMachine(java.lang.String)
	 */
	public void addVirtualMachine(String vMachineName)
		throws IgstkvtConfigurationException{
		if(vMachineName == null){
			throw new IgstkvtConfigurationException(
					"Name is null in addVirtualName method call.");
		}
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			throw new IgstkvtConfigurationException("Trying to add virtual "
					+"machine which already exists: " + vMachineName);
		}
		vMachines.put(vMachineName, new IgstkvtVM());
	}

	/**
	 * @throws IgstkvtConfigurationException
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#removeVirtualMachine(java.lang.String)
	 */
	public void removeVirtualMachine(String vMachineName)
		throws IgstkvtConfigurationException{
		if(vMachineName == null){
			throw new IgstkvtConfigurationException(
					"Name is Null in removeVirtualMethod call.");
		}
		if(vMachineName.equals("main")){
			throw new IgstkvtConfigurationException("Cannot remove virtual machine :main");
		}
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine == null){
			throw new IgstkvtConfigurationException("No such virtual "
					+"machine to remove: " + vMachineName);
		}
		vMachines.remove(vMachineName);
	}

	/**
	 * @throws IgstkvtConfigurationException
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#registerAsObserver(
	 * java.util.Observer, java.lang.String)
	 */
	public void addAsObserver(Observer observer, String vMachineName)
		throws IgstkvtConfigurationException{
		if(observer == null){
			throw new IgstkvtConfigurationException(
					"Observer cannot be null.");
		}
		if(vMachineName == null){
			throw new IgstkvtConfigurationException(
				"virtual machineName cannot be null.");
		}
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			vMachine.addObserver(observer);
		}else {
			throw new IgstkvtConfigurationException(
				"There is no such virtual machine.");
		}
	}

	/**
	 * @see org.igstk.validation.simulator.
	 * IgstkvtSimulatorServiceInterface#unregisterAsObserver(
	 * java.util.Observer, java.lang.String)
	 */
	public void deleteAsObserver(Observer observer, String vMachineName)
	 	throws IgstkvtConfigurationException{
		if(observer == null){
			throw new IgstkvtConfigurationException(
					"Observer cannot be null.");
		}
		if(vMachineName == null){
			throw new IgstkvtConfigurationException(
				"virtual machineName cannot be null.");
		}
		IgstkvtVM vMachine = vMachines.get(vMachineName);
		if(vMachine != null){
			vMachine.deleteObserver(observer);
		}else {
			throw new IgstkvtConfigurationException(
					"There is no such virtual machine.");
		}
	}
}
