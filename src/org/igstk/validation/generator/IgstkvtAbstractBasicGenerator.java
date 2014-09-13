/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    IGSTK Validation Tools
  Language:  Java
  Date:      Jan 30, 2008
  Version:   

  Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

package org.igstk.validation.generator;

/**
 * @author kgary
 *
 */
public abstract class IgstkvtAbstractBasicGenerator implements
        IgstkvtGeneratorInterface {

    IgstkvtGeneratorSpecificProperties gsprops;

    /**
     * This is the constructor.
     */
    protected IgstkvtAbstractBasicGenerator() {
        gsprops = new IgstkvtGeneratorSpecificProperties();
    }

    /**
     * This is a setter method for the properties object.
     * @param gsp
     */
    public void setGeneratorSpecificProperties(
            IgstkvtGeneratorSpecificProperties gsp) {
        this.gsprops = gsp;
    }

    /**
     * This is a getter method for the properties object.
     * @return
     */
    public IgstkvtGeneratorSpecificProperties getGeneratorSpecificProperties() {
        return gsprops;
    }

}
