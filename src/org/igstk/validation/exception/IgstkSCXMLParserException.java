/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkSCXMLParserException.java
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

public class IgstkSCXMLParserException extends Exception {
    
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     */
    public IgstkSCXMLParserException(){
        super();
    }
    
    /**
     * Constructor with message string.
     * @param message
     */
    public IgstkSCXMLParserException(final String message){
        super(message);
    }
    
    /**
     * Constructor with throw able cause.
     * @param cause
     */
    public IgstkSCXMLParserException(final Throwable cause){
        super(cause);
    }
    
    /**
     * Constructor with throw able cause and message string.
     * @param message
     * @param cause
     */
    public IgstkSCXMLParserException(final String message,
                    final Throwable cause){
        super(message,cause);
    }
}
