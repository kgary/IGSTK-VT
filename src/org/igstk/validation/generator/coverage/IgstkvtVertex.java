/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtVertex.java
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

package org.igstk.validation.generator.coverage;

/**
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtVertex {

    String vertexName;
    int markFlag;

    /**
     * 
     */
    public IgstkvtVertex() {
        vertexName = "";
        markFlag = 0;
    }

    /**
     * Setter for name.
     * @param name
     */
    void setName(String name) {
        vertexName = name;
    }

    /**
     * Getter for name.
     * @return
     */
    String getName() {
    //	System.out.println("THe vertex is "+vertexName);
        return vertexName;
    }

}
