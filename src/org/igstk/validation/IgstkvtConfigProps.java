/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtConfigProps.java
 * Language:  Java
 * Date:      Nov 3, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties to be passed to all the components.
 * @author janakiramdandibhotla
 */

public class IgstkvtConfigProps {

    private List<String> scxmlFiles = new ArrayList<String>();
    private List<String> stateMachineNames = new ArrayList<String>();
    private List<String> stateMachineIds = new ArrayList<String>();
    private List<String> sendEventFiles = new ArrayList<String>();
    private List<String> droolFiles = new ArrayList<String>();

    /**
     * Getter for SCXML Files.
     * @return
     */
    public List<String> getScxmlFiles() {
        return scxmlFiles;
    }

    /**
     * Setter for SCXML Files.
     * @param files
     */
    public void setScxmlFiles(List<String> files) {
        scxmlFiles = files;
    }

    /**
     * Getter for State Machine Names.
     * @return
     */
    public List<String> getStateMachineNames() {
        return stateMachineNames;
    }

    /**
     * Setter for State Machine Names.
     * @param stateMachineNames
     */
    public void setStateMachineNames(List<String> stateMachineNames) {
        this.stateMachineNames = stateMachineNames;
    }

    /**
     * Getter for State Machine Ids.
     * @return
     */
    public List<String> getStateMachineIds() {
        return stateMachineIds;
    }

    /**
     * Setter for State Machine Ids.
     * @param stateMachineIds
     */
    public void setStateMachineIds(List<String> stateMachineIds) {
        this.stateMachineIds = stateMachineIds;
    }

    /**
     * Getter for Send Event files.
     * @return
     */
    public List<String> getSendEventFiles() {
        return sendEventFiles;
    }

    /**
     * Setter for Send Event files.
     * @param sendEventFiles
     */
    public void setSendEventFiles(List<String> sendEventFiles) {
        this.sendEventFiles = sendEventFiles;
    }

	/**
	 * @param droolFiles the droolFiles to set
	 */
	public void setDroolFiles(List<String> droolFiles) {
		this.droolFiles = droolFiles;
	}

	/**
	 * @return the droolFiles
	 */
	public List<String> getDroolFiles() {
		return droolFiles;
	}
}
