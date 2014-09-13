/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtRoundRobinGeneratorSelectionStrategy.java
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

/**
 * @author kgary
 *
 */

final class IgstkvtRoundRobinGeneratorSelectionStrategy implements
		IgstkvtGeneratorSelectionStrategyInterface {

	private int nextSelected;

	/**
	 *
	 */
	public IgstkvtRoundRobinGeneratorSelectionStrategy() {
	}

	/**
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorSelectionStrategyInterface#selectNextEvent
	 * (org.igstk.validation.generator.IgstkvtGeneratorInterface[])
	 */
	public IgstkvtSendEvent selectNextEvent(
			IgstkvtGeneratorInterface[] generators)
		throws IgstkvtGeneratorException {
		IgstkvtSendEvent rval = null;

		if (generators == null || generators.length == 0) {
			return null;
		}

		int lastSelected = nextSelected;
		while (!generators[nextSelected].moreEvents()) {
			nextSelected = (nextSelected+1) % generators.length;
			if (nextSelected == lastSelected) {
				break;
			}
		}
		// there has to be a cleaner way than this
		if (generators[nextSelected].moreEvents()) {
			rval = generators[nextSelected].getNextEvent();
		}

		return rval;
	}
}
