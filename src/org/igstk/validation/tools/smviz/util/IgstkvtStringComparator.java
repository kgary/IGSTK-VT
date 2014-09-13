/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStringComparator.java
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

package org.igstk.validation.tools.smviz.util;

import java.util.Comparator;

/**
 * Comparator class used for grouping transitions.
 * @author Rakesh Kukkamalla
 * @version 
 * @since  jdk1.5
 */
public class IgstkvtStringComparator implements Comparator<String> {

    /**
     * Compares two strings. Returns a negative integer, zero, 
     * or a positive integer as the first argument is less than, 
     * equal to, or greater than the second.
     */
    public int compare(String arg0, String arg1) {
        return ((String) arg0).compareToIgnoreCase((String) arg1);
    }
}
