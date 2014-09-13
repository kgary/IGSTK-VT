/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtFatalConfigException.java
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

import org.igstk.validation.generator.IgstkvtSendEvent;


/**
 * Invalid Event Exception class.
 * @author janakiramdandibhotla
 */

public class IgstkvtFatalConfigException extends Exception {
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;
    
    private IgstkvtSendEvent sendEvent;
    
    /**
     * Getter for sendEvent variable.
     * @return
     */
    public IgstkvtSendEvent getTransition() {
        return sendEvent;
    }

    /**
     * Default Constructor.
     */
    public IgstkvtFatalConfigException(){
        super();
    }
    
    /**
     * Constructor to take a message string for the exception.
     * @param message
     */
    public IgstkvtFatalConfigException(final String message){
        super(message);
    }
    
    /**
     * Constructor to take a Throw able object.
     * @param cause
     */
    public IgstkvtFatalConfigException(final Throwable cause){
        super(cause);
    }
    
    /**
     * Constructor to take a Throw able object and a message string.
     * @param message
     * @param cause
     */
    public IgstkvtFatalConfigException(final String message, 
                final Throwable cause){
        super(message,cause);
    }
    
}
