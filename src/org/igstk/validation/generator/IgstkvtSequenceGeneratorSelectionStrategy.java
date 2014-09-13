/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtSequenceGeneratorSelectionStrategy.java
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

final class IgstkvtSequenceGeneratorSelectionStrategy implements
		IgstkvtGeneratorSelectionStrategyInterface {

	int currentGenerator;

	/**
	 *
	 */
	IgstkvtSequenceGeneratorSelectionStrategy() {
	}

	/**
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorSelectionStrategyInterface#selectNextEvent(
	 * org.igstk.validation.generator.IgstkvtGeneratorInterface[])
	 */
	public IgstkvtSendEvent selectNextEvent(
			IgstkvtGeneratorInterface[] generators)
		throws IgstkvtGeneratorException {

		IgstkvtSendEvent rval = null;

		if (generators == null || generators.length == 0) {
			return null;
		}

		while (currentGenerator < generators.length && rval == null) {
			if (generators[currentGenerator].moreEvents()) {
				rval = generators[currentGenerator].getNextEvent();
			} else {
				currentGenerator++;
			}
		}
		return rval;
	}

}
