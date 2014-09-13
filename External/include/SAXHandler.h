/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    $RCSfile: SAXHandler.h,v $
  Language:  C++
  Date:      $Date: 2009/01/14 00:06:27 $
  Version:   $Revision: 1.1.1.1 $

  Copyright (c) ISIS Georgetown University. All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/
#ifndef __SAXHandler_h
#define __SAXHandler_h

#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/sax/AttributeList.hpp>
#include "Graph.h"
#include "Vertex.h"
#include "PathCoverage.h"



#ifdef XERCES_CPP_NAMESPACE_USE
XERCES_CPP_NAMESPACE_USE
#endif

namespace igstk
{
namespace validation
  {
  class SAXHandler : public HandlerBase
    {
      public:
      // -----------------------------------------------------------------------
      //  Constructors and Destructor
      // -----------------------------------------------------------------------
	  SAXHandler(Graph* pathcoverage);
      SAXHandler(char* coverageType, Graph* coverageGraph);
      ~SAXHandler();


      // -----------------------------------------------------------------------
      //  Handlers for the SAX ContentHandler interface
      // -----------------------------------------------------------------------
      void startDocument();
      void startElement(const XMLCh* const name,
                        AttributeList& attributes);
      void endDocument();
	  void endElement(const XMLCh *const name);
	  void characters(const     XMLCh* const    chars
                                  , const   unsigned int    length);

      // -----------------------------------------------------------------------
      //  Getter methods
      // -----------------------------------------------------------------------
      unsigned int getElementCount() const
      {
        return elementCount;
      }

      // -----------------------------------------------------------------------
      //  Setter methods
      // -----------------------------------------------------------------------
      void setParseType(unsigned int type);
      void setComponentName(const char* name);
	  void setheuristicFileName(char* name);

      // -----------------------------------------------------------------------
      //  Handlers for the SAX ErrorHandler interface
      // -----------------------------------------------------------------------

      void fatalError(const SAXParseException& exception);

      private:
      // -----------------------------------------------------------------------
      //  Private data members
      // -----------------------------------------------------------------------
      unsigned int   elementCount; //
      unsigned int   parseType;    //Specifies first pass or second pass
                                   //of the scxml file
      char* state;
      char* initialState;          //Stores root node of the Graph
      char* transition;            //transition name
      char* target;                //target name
      char* componentPathAndName;
      char* coverageType;          //Contains the value for coverage type.
                                   //Value set from the main function
      Graph* pGraph;
	  int    pathHeuristics;
	  int    componentFound;
	  char*  heuristicEvent;
	  char*  heuristicSource;
	  char*  heuristicTarget;
	  int    iterationCount;
	  int    combination;
	  char*    heuristicFileName;
	  Graph* pathP;
	  std::ostringstream heuristics_text;
	  std::string componentName;
	  
    };
  }
}
#endif //__SAXHandler_h
