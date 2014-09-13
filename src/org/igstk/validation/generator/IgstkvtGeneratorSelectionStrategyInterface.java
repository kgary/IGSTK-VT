/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGeneratorSelectionStrategyInterface.java
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

interface IgstkvtGeneratorSelectionStrategyInterface {
	/**
	 * @param generators
	 * @return
	 * @throws IgstkvtGeneratorException
	 */
	public IgstkvtSendEvent selectNextEvent(
		IgstkvtGeneratorInterface[] generators)
		throws IgstkvtGeneratorException;
}
