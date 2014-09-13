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

import java.util.Iterator;

/**
 * @author kgary
 *
 */
public interface IgstkvtGeneratorServiceInterface {
    String EVENTS_FILE = "EventsFile";
    String SM_ID = "StateMachineId";

    /**
     *
     */
    public IgstkvtSendEvent getNextEvent() throws IgstkvtGeneratorException;

    /**
     * @author Kevin Gary
     * @return
     */
    public IgstkvtGeneratorInfo getCurrentGeneratorInfo();

    /**
     * @author Kevin Gary
     * @return A Iterator over the entire sequence of available generators.
     */
    public Iterator<IgstkvtGeneratorInfo> getAvailableGenerators();

    /**
     * @author Kevin Gary
     * @return A Iterator over the entire sequence of generators.
     */
    public Iterator<IgstkvtGeneratorInfo> getAllGenerators();

    /**
     * @author Kevin Gary
     * @return A Iterator over the list of open generators.
     */
    public Iterator<IgstkvtGeneratorInfo> getOpenGenerators();

    /**
     * Open a Generator and prepare it for reading.
     *
     * @author Kevin Gary
     * @param props
     * @throws IgstkvtGeneratorException
     */
    public void openGenerator(IgstkvtGeneratorSpecificProperties props)
        throws IgstkvtGeneratorException;

    /**
     * Closes a Generator and makes it unavailable for reading, but does not
     * remove it from the managed list of Generators.
     *
     * @author Kevin Gary
     * @param gen
     * @throws IgstkvtGeneratorException
     */
    public void closeGenerator(int genId) throws IgstkvtGeneratorException;

    /**
     * Lifecycle management method that completely removes the Generator from
     * memory.
     *
     * @author Kevin Gary
     * @param genId
     * @throws IgstkvtGeneratorException
     */
    public void destroyGenerator(int genId) throws IgstkvtGeneratorException;

}
