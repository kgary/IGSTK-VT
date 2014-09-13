/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtDroolsTestExecutor.java
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

package org.igstk.validation.simulator.rules;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.drools.rule.Rule;
import org.igstk.validation.simulator.IgstkvtStateMachineExecutor;

/**
 * This class is a specific implementation of TestService using Drools.
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtDroolsTestExecutor
implements IgstkvtTestServiceExecutorInterface {

	/**
	 * @see org.igstk.validation.simulator.rules.
	 * IgstkvtTestServiceExecutorInterface#
	 * execute(org.igstk.validation.simulator.rules.
	 * IgstkvtTestExecutionContextProperties)
	 */
	private IgstkvtTestExecutionContextProperties context;
	private static List<String> droolFiles;
	private static RuleBase _currentRuleBase = null;
	
	private static RuleBase getCurrentRuleBase() throws Exception {
		if (_currentRuleBase == null) {
			 _currentRuleBase = readRule();
		}
		return _currentRuleBase;
	}
	/**
	 * @see org.igstk.validation.simulator.rules.
	 * IgstkvtTestServiceExecutorInterface#execute()
	 */
	public void execute() {
		try {

			List<IgstkvtStateMachineExecutor> stateMachines = context
			.getObjectsToFireRulesOn();

			RuleBase ruleBase = getCurrentRuleBase();
			WorkingMemory workingMemory = ruleBase.newStatefulSession();

			Iterator<IgstkvtStateMachineExecutor> iter =
				stateMachines.iterator();
			while (iter.hasNext()) {
				workingMemory.insert(iter.next());
			}
			workingMemory.fireAllRules();
			

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * This method reads the rule from the drl file.
	 * @return
	 * @throws Exception
	 */
	private static RuleBase readRule() throws Exception {
		// read in the source
		System.out.println( "size is "+droolFiles.size());

		// add the package to a rulebase (deploy the rule package).
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();

		for (int i=0;i<droolFiles.size();i++){

			//Reader source = new InputStreamReader(IgstkvtDroolsTestExecutor.class
			//    .getResourceAsStream("/igstk.drl"));
			System.out.println("The drool file is :" +droolFiles.get(i));
			Reader source = new InputStreamReader(IgstkvtDroolsTestExecutor.class.getResourceAsStream("/"+droolFiles.get(i)));

			// optionally read in the DSL (if you are using it).
			// Reader dsl = new InputStreamReader(
			// DroolsTest.class.getResourceAsStream( "/mylang.dsl" ) );

			// Use package builder to build up a rule package.
			// An alternative lower level class called "DrlParser" can also be
			// used...

			PackageBuilder builder = new PackageBuilder();

			// this wil parse and compile in one step
			// NOTE: There are 2 methods here, the one argument one is for normal
			// DRL.
			builder.addPackageFromDrl(source);

			// Use the following instead of above if you are using a DSL:
			// builder.addPackageFromDrl( source, dsl );

			// get the compiled package (which is serializable)
			Package pkg = builder.getPackage();
			ruleBase.addPackage(pkg);
		}
		return ruleBase;
	}

	/**
	 * @see org.igstk.validation.simulator.rules.
	 * IgstkvtTestServiceExecutorInterface#setContextProperties(
	 * org.igstk.validation.simulator.rules.
	 * IgstkvtTestExecutionContextProperties)
	 */
	public void setContextProperties(
			IgstkvtTestExecutionContextProperties context) {
		this.context = context;
	}

	/**
	 * @param droolFiles the droolFiles to set
	 */
	public void setDroolFiles(List<String> droolFiles) {
		this.droolFiles = droolFiles;
	}


}
