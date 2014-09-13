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
public interface IgstkvtCacheableGeneratorInterface extends
        IgstkvtGeneratorInterface {

    /**
     * @param se
     * @return
     */
    public int rewind(IgstkvtSendEvent se);

    /**
     * @param n
     * @return
     */
    public IgstkvtSendEvent lookBehind(int n);

    /**
     * @param n
     * @return
     */
    public int rewind(int n);

}
