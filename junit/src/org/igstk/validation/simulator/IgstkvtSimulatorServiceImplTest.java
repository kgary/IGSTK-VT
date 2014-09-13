package org.igstk.validation.simulator;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.igstk.validation.exception.IgstkvtConfigurationException;
import org.igstk.validation.exception.IgstkvtFatalConfigException;
import org.igstk.validation.exception.IgstkvtInvalidEventException;
import org.igstk.validation.exception.IgstkvtRewindException;
import org.igstk.validation.generator.IgstkvtGeneratorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IgstkvtSimulatorServiceImplTest {

	IgstkvtSimulatorServiceImpl simService;

	private class TestObserver implements Observer{
		boolean calledUpdate = false;
		public void update(Observable o, Object arg) {
			calledUpdate = true;
		}

	}

	@Before
	public void setUp() throws Exception {
		simService = new IgstkvtSimulatorServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInit() {
		boolean caughtException = false;
		//Happy case..
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Something wrong. Should not throw exception.");
		}

		caughtException = false;
		//Null case.
		try {
			simService.init(null);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("This should throw an exception.");
		}

		caughtException = false;
		//Some vName case.
		try {
			simService.init("test");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Should throw exception.");
		}
	}

	@Test
	public void testFireEvents() {
		boolean caughtException = false;
		//Happy case.
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception");
		}

		caughtException = false;

		try {
			simService.fireEvents("main", 1, 10);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
	}

	@Test
	public void testRewind() {
		boolean caughtException = false;
		//Happy case.
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception");
		}

		caughtException = false;
		try {
			simService.fireEvents("main", 1, 10);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
		caughtException = false;

		try {
			simService.rewind("main", 1);
		} catch (IgstkvtRewindException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
	}

	@Test
	public void testAddVirtualMachine() {
		boolean caughtException = false;
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception");
		}

		caughtException = false;

		try {
			simService.addVirtualMachine("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("This should throw exception.");
		}

		caughtException = false;

		try {
			simService.addVirtualMachine("test");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if(caughtException){
			fail("Should not throw exception.");
		}

		caughtException = false;
		try {
			simService.addVirtualMachine(null);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if(!caughtException){
			fail("Should throw exception.");
		}

	}

	@Test
	public void testRemoveVirtualMachine() {
		boolean caughtException = false;
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception");
		}

		caughtException = false;
		try {
			simService.removeVirtualMachine("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Show throw exception.");
		}

		caughtException = false;
		try {
			simService.removeVirtualMachine(null);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Should throw exception.");
		}

		caughtException = false;

		try {
			simService.addVirtualMachine("test");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}
		if(caughtException){
			fail("Should not throw exception.");
		}
		caughtException = false;
		try {
			simService.removeVirtualMachine("test");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}

	}

	@Test
	public void testAddAsObserver() {
		boolean caughtException = false;
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Something wrong. Should not throw exception.");
		}

		caughtException = false;
		try {
			simService.addAsObserver(null, null);
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Should throw exception.");
		}

		//Happy case.
		caughtException = false;
		TestObserver newObserver = new TestObserver();
		try {
			simService.addAsObserver(newObserver, "main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}

		caughtException = false;
		try {
			simService.fireEvents("main", 1, 10);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
		assertTrue(newObserver.calledUpdate);
	}

	@Test
	public void testDeleteAsObserver() {
		boolean caughtException = false;
		try {
			simService.init("main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtFatalConfigException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Something wrong. Should not throw exception.");
		}

		caughtException = false;

		caughtException = false;
		TestObserver newObserver = new TestObserver();
		try {
			simService.addAsObserver(newObserver, "main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}
		caughtException = false;

		try {
			simService.fireEvents("main", 1, 10);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}

		assertTrue(newObserver.calledUpdate);
		caughtException = false;
		try {
			simService.deleteAsObserver(newObserver, "main");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}

		caughtException = false;
		newObserver.calledUpdate = false;

		try {
			simService.fireEvents("main", 1, 10);
		} catch (IgstkvtInvalidEventException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (IgstkvtGeneratorException e) {
			e.printStackTrace();
			caughtException = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(caughtException){
			fail("Should not throw exception.");
		}

		assertFalse(newObserver.calledUpdate);
		caughtException = false;

		try {
			simService.deleteAsObserver(newObserver, "noSuchMachine");
		} catch (IgstkvtConfigurationException e) {
			e.printStackTrace();
			caughtException = true;
		}

		if(!caughtException){
			fail("Should throw expception.");
		}

	}

}
