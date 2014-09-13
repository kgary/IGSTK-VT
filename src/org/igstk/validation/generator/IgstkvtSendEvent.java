/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    IGSTK Validation Tools
  Language:  Java
  Date:      Jan 28, 2008
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
 * This is the main event file representing a new input to the validator SM
 */
public final class IgstkvtSendEvent {
    private String _eventName;
    private String _id;

    /**
     * Overriding toString behavior.
     * @return
     */
    @Override
    public String toString() {
        return _id + ":" + _eventName;
    }

    /**
     * @param eventStr
     * @param id
     */
    public IgstkvtSendEvent(String eventStr, String id) {
        _eventName = eventStr;
        _id = id;
    }

    /**
     * @return
     */
    public String getEventName() {
        return _eventName;
    }

    /**
     * @return
     */
    public String getId() {
        return _id;
    }
}
