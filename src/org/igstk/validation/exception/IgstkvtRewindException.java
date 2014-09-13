/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtRewindException.java
 * Language:  Java
 * Date:      May 12, 2008
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
 * Rewind Exception class.
 * @author janakiramdandibhotla
 */

public class IgstkvtRewindException extends Exception {
    
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
    public IgstkvtRewindException(){
        super();
    }
    
    /**
     * Constructor to take a message string for the exception.
     * @param message
     */
    public IgstkvtRewindException(final String message){
        super(message);
    }
    
    /**
     * Constructor to take a Throw able object.
     * @param cause
     */
    public IgstkvtRewindException(final Throwable cause){
        super(cause);
    }
    
    /**
     * Constructor to take a Throw able object and a message string.
     * @param message
     * @param cause
     */
    public IgstkvtRewindException(final String message, 
                final Throwable cause){
        super(message,cause);
    }
    
    /**
     * Constructor to take a Throw able object, a message string,
     * and the transition where the problem occurred.
     * @param message
     * @param cause
     * @param transition
     */
    public IgstkvtRewindException(final String message, 
            final Throwable cause, final IgstkvtSendEvent sendEvent){
        this.sendEvent = sendEvent;
    }
    
}
