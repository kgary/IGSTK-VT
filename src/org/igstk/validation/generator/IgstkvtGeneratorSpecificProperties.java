/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGeneratorSpecificProperties.java
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

package org.igstk.validation.generator;

/**
 * This class is used as a java bean to put all
 * the properties of a generator and pass around.
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtGeneratorSpecificProperties {

    private String sendEventsSource;
    private String typeOfSourceOfSendEvents;
    private String stateMachineName;

    private boolean generateSendEvents;
    private String sendEventsDirectory;
    private String scxmlFile;
    private String generatorType;

    /**
     * Getter for generator type.
     * @return
     */
    public String getGeneratorType() {
        return generatorType;
    }

    /**
     * Setter for generator type.
     * @param generatorType
     */
    public void setGeneratorType(String generatorType) {
        this.generatorType = generatorType;
    }

    /**
     * Getter for SCXML File.
     * @return
     */
    public String getScxmlFile() {
        return scxmlFile;
    }

    /**
     * Setter for SCXML File.
     * @param scxmlFile
     */
    public void setScxmlFile(String scxmlFile) {
        this.scxmlFile = scxmlFile;
    }

    /**
     * Boolean getter to check whether to generate new
     * send events file.
     * @return
     */
    public boolean isGenerateSendEvents() {
        return generateSendEvents;
    }

    /**
     * Boolean setter to check whether to generate new
     * send events file.
     * @param generateSendEvents
     */
    public void setGenerateSendEvents(boolean generateSendEvents) {
        this.generateSendEvents = generateSendEvents;
    }

    /**
     * Getter for Send events file Directory.
     * @return
     */
    public String getSendEventsDirectory() {
        return sendEventsDirectory;
    }

    /**
     * Setter for Send events file Directory.
     * @param sendEventsDirectory
     */
    public void setSendEventsDirectory(String sendEventsDirectory) {
        this.sendEventsDirectory = sendEventsDirectory;
    }

    /**
     * Getter for State Machine Name.
     * @return
     */
    public String getStateMachineName() {
        return stateMachineName;
    }

    /**
     * Setter for State Machine Name.
     * @param stateMachineName
     */
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }

    /**
     * Setter for send events source.
     * @param sendEventsSource
     * @param typeOfSourceOfSendEvents
     */
    public void setSendEventsSource(String sendEventsSource,
            String typeOfSourceOfSendEvents) {
        this.sendEventsSource = sendEventsSource;
        this.typeOfSourceOfSendEvents = typeOfSourceOfSendEvents;
    }

    /**
     * Getter for Send events source.
     * @return
     */
    public String getSendEventsSource() {
        return sendEventsSource;
    }

    /**
     * Getter for Type of sources of send events.
     * @return
     */
    public String getTypeOfSourceOfSendEvents() {
        return typeOfSourceOfSendEvents;
    }
}
