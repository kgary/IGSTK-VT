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

import java.util.Map;

/**
 * @author kgary
 * 
 */
public interface IgstkvtGeneratorFactoryInterface {
    /**
     * @param params
     * @return
     */
    public IgstkvtGeneratorInterface createGenerator(Map<String, Object> params);
}
