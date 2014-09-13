/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGraphTest.java
 * Language:  Java
 * Date:      Nov 21, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/
package org.igstk.validation.generator.coverage;

import junit.framework.TestCase;

/**
 * @author Sany
**/
public class IgstkvtGraphTest extends TestCase {

	private IgstkvtGraph gc;
	/** (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 **/
	protected void setUp() throws Exception {
		gc = new IgstkvtGraph();
		
	}

	/** (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 **/
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.igstk.validation.
	 * generator.coverage.IgstkvtGraph#addEdge(int, int, java.lang.String)}.
	 */
	public final void testAddEdgeHappy() {
		gc.initializeEdgeArray(2);
		gc.addEdge(0,0,"test");
		String trans = gc.edges[0][0].getTransition();
		assertEquals("test", trans);
		
	}

	/**
	 * Testing for negative numbers.
	 */
	public final void testAddEdgeNull() {
		boolean caughtException = false;
		gc.initializeEdgeArray(2);
		try{
			gc.addEdge(-5,-5,"test");
		}catch(ArrayIndexOutOfBoundsException e){
			caughtException = true;
		}
		if (!caughtException){
			fail("The Exception should be caputured");
		}
		
	}
	
	/**
	 * Test method for {@link org.igstk.validation.generator
	 * .coverage.IgstkvtGraph#findANodeInGraph(int)}.
	 */
	public final void testFindANodeInGraph() {
		gc.vertexNo=2;
		int result = gc.findANodeInGraph(1);
		if (result == 0){
			fail("Result shouldn't be 0");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.generator
	 * .coverage.IgstkvtGraph#findANodeInGraph(int)}.
	 */
	public final void testFindANodeInGraphReturnZero() {
		gc.vertexNo=2;
		gc.statesVisited.add(1);
		int result = gc.findANodeInGraph(1);
		if (result != 0){
			fail("Result should be 0");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.generator
	 * .coverage.IgstkvtGraph#findVisitedParent(int, int)}.
	 */
	public final void testFindVisitedParent() {
		gc.vertexNo=2;
		gc.initializeEdgeArray(2);
		int result = gc.findVisitedParent(1,0);
		if (result != -1){
			fail("Should return -1");
		}
	}

	/**
	 * Test method for {@link org.igstk.validation.generator
	 * .coverage.IgstkvtGraph#findVisitedParent(int, int)}.
	 */
	public final void testFindVisitedParentReturnsNode() {
		gc.vertexNo=2;
		gc.initializeEdgeArray(2);
		gc.edges[0][1]= new IgstkvtTransition();
		gc.statesVisited.add(0);
		int result = gc.findVisitedParent(1,0);
	
		if (result == -1){
			fail("Shouldn't return -1");
		}
	}
	
}
