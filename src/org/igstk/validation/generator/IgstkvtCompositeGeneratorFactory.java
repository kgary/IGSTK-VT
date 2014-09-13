/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtCompositeGeneratorFactory.java
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author kgary
 *
 */

public class IgstkvtCompositeGeneratorFactory implements
		IgstkvtGeneratorFactoryInterface {

    private static IgstkvtGeneratorFactoryInterface theFactory;

    /**
     * @return
     */
    public static IgstkvtGeneratorFactoryInterface getGeneratorFactory() {
        if (theFactory == null) {
            theFactory = new IgstkvtCompositeGeneratorFactory();
        }
        return theFactory;
    }

    /**
     *
     */
    private IgstkvtCompositeGeneratorFactory() {
    }

	/**
	 * The map has to have 2 things: a generators
	 * property and a selection strategy
	 * For the generators, we will allow existing
	 * generators to be included, though
	 * the client does this at their own risk.
	 * This will allow clients to dynamically
	 * merge generators together at runtime.
	 * YYY right now the arbitrary decision is made
	 * to load pre-existing generators first
	 * and then new ones. We could do something more
	 * sophisticated later.
	 *
	 * @see org.igstk.validation.generator.
	 * IgstkvtGeneratorFactoryInterface#createGenerator(java.util.Map)
	 */
	public IgstkvtGeneratorInterface createGenerator(
			Map<String, Object> params) {
		ArrayList<IgstkvtGeneratorInterface> generators =
			new ArrayList<IgstkvtGeneratorInterface>();
		IgstkvtCompositeGenerator.SelectionStrategy strategy =
			(IgstkvtCompositeGenerator.SelectionStrategy)
			params.get("selectionStrategy");

		try {
			Class generatorFactoryInterfaceClass = Class.
				forName("IgstkvtGeneratorInterfaceFactory");

			// first we'll load any pre-existing generators
			IgstkvtGeneratorInterface[] existingGenerators =
				(IgstkvtGeneratorInterface[])params.get("existingGenerators");

			if (existingGenerators != null) {
				for (int i = 0; i < existingGenerators.length; i++) {
					if (generatorFactoryInterfaceClass.isAssignableFrom(
							existingGenerators[i].getClass())) {
						generators.add(existingGenerators[i]);
					}
				}
			}

			// Then we'll load any we have that do not pre-exist
			String[] generatorNames = (String[])params.get("generatorNames");
			Map<String, Object>[] generatorParams =
				(Map<String, Object>[])params.get("generatorParams");

			if (generatorNames != null) {
				// The strings in the array should be classnames
				//of generator classes
				for (int i = 0; i < generatorNames.length; i++) {
					Class gFactory = Class.forName(generatorNames[i]);
					if (generatorFactoryInterfaceClass.
							isAssignableFrom(gFactory)) {
						Method factoryMethod = generatorFactoryInterfaceClass.
							getMethod("getGeneratorFactory", new Class[0]);
						IgstkvtGeneratorFactoryInterface factory =
							(IgstkvtGeneratorFactoryInterface) factoryMethod.
								invoke(null, new Object[0]);

						// now invoke the createGenerator method with the
						//right nested Map params.
						IgstkvtGeneratorInterface generator = factory.
							createGenerator(generatorParams[i]);
						if (generator != null) {
							generators.add(generator);
						}
					}
				}
			}
		} catch (Throwable t) {
			// we're fail-fast
			t.printStackTrace();
			generators = null;
		}

		// If we got this far and have a list of generators,
		//now we just have to create the Composite
		if (generators != null) {
			return new IgstkvtCompositeGenerator(generators.
					toArray(new IgstkvtGeneratorInterface[0]), strategy);
		}
		return null;
	}

}
