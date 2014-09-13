    /**************************************************************************
     * Program:   Image Guided Surgery Software Validation Toolkit
     * File:      IgstkvtCompositeGenerator.java
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
package org.igstk.validation.generator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author kgary
 *
 */
public final class IgstkvtCompositeGenerator
	implements IgstkvtGeneratorInterface {

/**
 *Enumeration.
 */
    public static enum SelectionStrategy{ROUND_ROBIN_STRATEGY, SEQUENCE_STRATEGY}
	private IgstkvtGeneratorInterface[] generators;
	private IgstkvtGeneratorSelectionStrategyInterface strategy;

	/**
	 * It is possible to share generators with a composite
	 * and use them on their own
	 * @param generators
	 */
	IgstkvtCompositeGenerator(
			IgstkvtGeneratorInterface[] generators, SelectionStrategy s) {
		this.generators = generators;

		switch (s) {
		case ROUND_ROBIN_STRATEGY :
			strategy = new	IgstkvtRoundRobinGeneratorSelectionStrategy();
			break;
		case SEQUENCE_STRATEGY :
			strategy = new IgstkvtSequenceGeneratorSelectionStrategy();
			break;
		default :
			strategy = new IgstkvtRoundRobinGeneratorSelectionStrategy();
			break;
		}
	}

	/**
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorInterface#getNextEvent()
	 */
	public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException {
		return strategy.selectNextEvent(generators);
	}

	/**
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorInterface#getNextEvents(int)
	 */
	public Iterator<IgstkvtSendEvent> getNextEvents(int n)
		throws IgstkvtGeneratorException {
		// Gotta create this one ourselves
		ArrayList<IgstkvtSendEvent> rval = new ArrayList<IgstkvtSendEvent>();

		IgstkvtSendEvent nextEvent = getNextEvent();
		for (int i = 0; i < n && nextEvent != null; i++) {
			rval.add(nextEvent);
		}
		return rval.iterator();
	}

	/**
	 * @see org.igstk.validation.generator.IgstkvtGeneratorInterface#moreEvents()
	 */
	public boolean moreEvents() {
		boolean rval = false;
		for (int i = 0; !rval && i < generators.length; i++) {
			rval = generators[i].moreEvents();
		}
		return rval;
	}

}
