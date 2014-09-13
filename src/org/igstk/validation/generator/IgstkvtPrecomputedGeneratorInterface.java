/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    IGSTK Validation Tools
  Language:  Java
  Date:      Feb 18, 2008
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
public interface IgstkvtPrecomputedGeneratorInterface extends
        IgstkvtGeneratorInterface {
    /**
     * @param n
     * @return
     */
    public IgstkvtSendEvent lookAhead(int n);

    /**
     * @param n
     * @return
     */
    public int skip(int n);

    /**
     * @param se
     * @return
     */
    public int skip(IgstkvtSendEvent se);
}
