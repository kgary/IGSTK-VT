/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtConfigurationException.java
 * Language:  Java
 * Date:      May 9, 2008
 * 
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 * 
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.      
 *************************************************************************/

package org.igstk.validation.exception;

/**
 * Configuration Exception class.
 * @author janakiramdandibhotla
 */

public class IgstkvtConfigurationException extends Exception {
    
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     */
    public IgstkvtConfigurationException(){
        super();
    }
    
    /**
     * Constructor with message string.
     * @param message
     */
    public IgstkvtConfigurationException(final String message){
        super(message);
    }
    
    /**
     * Constructor with throw able cause.
     * @param cause
     */
    public IgstkvtConfigurationException(final Throwable cause){
        super(cause);
    }
    
    /**
     * Constructor with throw able cause and message string.
     * @param message
     * @param cause
     */
    public IgstkvtConfigurationException(final String message,
                    final Throwable cause){
        super(message,cause);
    }
}
