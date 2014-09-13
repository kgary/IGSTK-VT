 /*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    $RCSfile: SAXHandler.cxx,v $
  Language:  C++
  Date:      $Date: 2009/01/14 00:06:27 $
  Version:   $Revision: 1.1.1.1 $

  Copyright (c) ISIS Georgetown University. All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#include "SAXHandler.h"

#include <iostream>


namespace igstk
{
namespace validation
{
  // ---------------------------------------------------------------------------
  //  SAXHandler : Constructors and Destructor
  // ---------------------------------------------------------------------------

  SAXHandler::SAXHandler(Graph* pathcoverage)
  {
	  pathHeuristics = 0;
	  combination = 0;
	  pathP = pathcoverage;
	  heuristicEvent = 0;
	  heuristicSource = 0;
	  heuristicTarget = 0;
     
	 // cout<<"componentPathAndName "<<componentPathAndName<<endl;
  }

  SAXHandler::SAXHandler(char* coveragetype, Graph* coverageGraph)
  {
	pathHeuristics = 0;
	componentFound = 0;
    elementCount = 0;
    parseType =0;
	heuristics_text.clear();
	
    coverageType =  new char [1 + strlen(coveragetype)];
    strcpy(coverageType, coveragetype);
	pGraph = coverageGraph;

    //based on coveragetype specified from the command prompt create an object
    //for one of the derived classes NodeCoverage,EdgeCoverage
   /* if(!strcmp(coverageType, "nodecoverage"))
    {
      pGraph= new NodeCoverage();
    }
    else  if(!strcmp(coverageType, "edgecoverage"))
    {
      pGraph= new EdgeCoverage();
    }
	else  if(!strcmp(coverageType, "pathcoverage"))
    {
      pGraph= new PathCoverage();
    }*/
   

  }

  SAXHandler::~SAXHandler()
  {
    
  }

  // -----------------------------------------------------------------------
  //  Setter methods
  // -----------------------------------------------------------------------
  void SAXHandler::setParseType(unsigned int type)
  {
    parseType = type;
  }

  void SAXHandler::setComponentName(const char* name)
  {
    componentPathAndName = new char [1 + strlen(name)];
    strcpy(componentPathAndName, name);

	const std::string& delimiters = "/,\\";
    std::string::size_type pos1,pos2;
    std::string comp_name( componentPathAndName );
    pos1     = comp_name.find_last_of(delimiters, comp_name.length());
	std::string pathName = comp_name.substr(0, pos1);
    pos2     = pathName.find_last_of(delimiters, comp_name.length());
    pathName = pathName.substr(0, pos2);
    componentName = comp_name.substr(pos1+1,comp_name.length());
	
  }

  void SAXHandler::setheuristicFileName(char* name)
  {
    heuristicFileName = new char [1 + strlen(name)];
    strcpy(heuristicFileName, name);
  }


  // ---------------------------------------------------------------------------
  //  SAXHandler: Implementation of the SAX DocumentHandler interface
  // ---------------------------------------------------------------------------

  void SAXHandler::startDocument()
  {
    if(parseType == 1) 
	{
		pGraph->initializeNodeArray();
	}
    if (parseType == 2)
    {
		
      pGraph->initializeEdgeArray(elementCount);
    }
	heuristics_text.clear();
	
	componentFound = 0;
	
  }

  void SAXHandler::startElement(const XMLCh* const name,
                                AttributeList& attributes)
  {
    Vertex  pVertex;
	char*   attr;
	char * pch = NULL;
    char* message = XMLString::transcode(name);
   
   
	/**********************************************************/
	//Heuristics
	 if(componentFound)
	 {
		
		 if (attributes.getLength()==0)
		 {
			heuristics_text <<"  <" <<message<<">"<<endl;
			
			}
		 else
		 {
		 heuristics_text <<"  <"<<message<<" ";
	
		 for (int i=0;i < attributes.getLength();i++)
		 {
			 heuristics_text<< XMLString::transcode(attributes.getName(i))<<" = ";
			 heuristics_text << "\""<<XMLString::transcode(attributes.getValue(i))<<"\" ";
		
		 }
		 heuristics_text <<">"<<endl;
		 }
		 if(!strcmp(message, "combination"))
		{
			combination = 1;
			
		}
		if(!strcmp(message, "selection"))
		{
			attr = XMLString::transcode(attributes.getValue("criteria"));
			((PathCoverage*)pathP)->addBaselineSelectionCriteria(attr);
		}
	
		if(!strcmp(message, "maxTraversal")&& componentFound)
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("count",XMLString::transcode(attributes.getName(i))))
				{
					int count = XMLString::parseInt(attributes.getValue("count"));
					((PathCoverage*)pathP)->addHeuristicLoopInformation(heuristicEvent, heuristicSource, count);
					heuristicEvent = 0;
					heuristicSource = 0;
					
				}
			}
					
		}
		if(!strcmp(message, "exception_input"))
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("name",XMLString::transcode(attributes.getName(i))))
				{
					attr = XMLString::transcode(attributes.getValue("name"));
					((PathCoverage*)pathP)->addExceptionInput(attr);
				}
			}
		}
		if(!strcmp(message, "include_state"))
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("name",XMLString::transcode(attributes.getName(i))))
				{
					attr = XMLString::transcode(attributes.getValue("name"));
					((PathCoverage*)pathP)->addStateToBeIncluded(attr);
				}
			}

		}
		if(!strcmp(message, "exclude_state"))
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("name",XMLString::transcode(attributes.getName(i))))
				{
					attr = XMLString::transcode(attributes.getValue("name"));
					((PathCoverage*)pathP)->addStateToBeExcluded(attr);
				}
			}
		}
		if(!strcmp(message, "include_transition"))
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("source",XMLString::transcode(attributes.getName(i))))
				{
					heuristicSource = XMLString::transcode(attributes.getValue("source"));
					
				}
				if(XMLString::equals("event",XMLString::transcode(attributes.getName(i))))
				{
					heuristicEvent = XMLString::transcode(attributes.getValue("event"));
					
				}
			}
			((PathCoverage*)pathP)->addTransitionToBeIncluded(heuristicSource, heuristicEvent);
			heuristicEvent = 0;
			heuristicSource = 0;
			
		}
		if(!strcmp(message, "exclude_transition"))
		{
			for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("source",XMLString::transcode(attributes.getName(i))))
				{
					heuristicSource = XMLString::transcode(attributes.getValue("source"));
					
				}
				
				if(XMLString::equals("event",XMLString::transcode(attributes.getName(i))))
				{
					heuristicEvent = XMLString::transcode(attributes.getValue("event"));
					
				}
			}
			((PathCoverage*)pathP)->addTransitionToBeExcluded(heuristicSource, heuristicEvent);
			heuristicEvent = 0;
			heuristicSource = 0;
			
		}
	 }
	 if(!strcmp(message, "component"))
    {
		for (int i=0;i < attributes.getLength();i++)
		{
			if(XMLString::equals("name",XMLString::transcode(attributes.getName(i))))
			{
				attr = XMLString::transcode(attributes.getValue("name"));
	
				if ( !strcmp (componentName.c_str(),attr) ) 
				{
					componentFound = 1;
					heuristics_text<<endl;
			
					heuristics_text<<"<heuristics>"<<endl;
				}else
				{
					componentFound = 0;
					
				}
			}
			if(componentFound)
			{
				if(XMLString::equals("entry",XMLString::transcode(attributes.getName(i))))
				{
					attr = XMLString::transcode(attributes.getValue("entry"));
				}

				if(XMLString::equals("exit",XMLString::transcode(attributes.getName(i))))
				{
					attr = XMLString::transcode(attributes.getValue("exit"));
					((PathCoverage*)pathP)->addFinalState(attr);
				}
			}
		
		}
      
    }
	if(!strcmp(message, "path_heuristics"))
	{
		pathHeuristics = 1;
	}
	

    /*********************************************************/
	//If tag is "scxml" then the initialstate is an attribute
    if(!strcmp(message, "scxml"))
    {
      initialState = XMLString::transcode(attributes.getValue("initialstate"));
      pGraph->setInitialVertex(initialState);
    }
    //If tag is "state" then add to vertex vector
    if(!strcmp(message, "state"))
    {
      for (int i=0;i < attributes.getLength();i++)
      {
        state = XMLString::transcode(attributes.getValue(i));

        if(parseType == 1)
        {
          pVertex.setName(state);
          pGraph->addNode(pVertex);
	
          elementCount++;
        }
      }
    }
    //If second pass of SCXML file then populate Edge array
    if(parseType !=1)
    {
      if(!strcmp(message, "transition"))
      {
		  if(componentFound)
		  {
			  for (int i=0;i < attributes.getLength();i++)
			{
				if(XMLString::equals("event",XMLString::transcode(attributes.getName(i))))
				{
					heuristicEvent = XMLString::transcode(attributes.getValue("event"));
				}
				if(XMLString::equals("source",XMLString::transcode(attributes.getName(i))))
				{
				  heuristicSource = XMLString::transcode(attributes.getValue("source"));
				}
			  }
			
		  }
        for (int i=0;i < attributes.getLength();i++)
        {
          transition = XMLString::transcode(attributes.getValue(i));
        }
      }
      if(!strcmp(message, "target"))
      {
        for (int i=0;i < attributes.getLength();i++)
        {
			
          target = XMLString::transcode(attributes.getValue(i));
	
          int row = pGraph->findNode(state);
          int col = pGraph->findNode(target);
	
          pGraph->addEdge(row,col,transition);
		  
        }
      }
    }

    XMLString::release(&message);
  }

  void SAXHandler::endElement(const XMLCh *const name)
{
  char* message = XMLString::transcode(name);;

  if (componentFound) 
  {
	  if (!strcmp(message, "component"))
	  {
		  heuristics_text<<"</heuristics>"<<endl;
		  heuristics_text<<endl;
	
		 		 
		((PathCoverage*)pathP)->setheuristicMetaData(heuristics_text.str());
		
	  }
	  else
	  {
		  heuristics_text<<"  </"<<message<<">"<<endl;
	  }
  }
  
  
}


  void SAXHandler::endDocument()
  {
	  if(pathHeuristics)
	  {
		  pathHeuristics = 0;
	  }
	  
     

    
	
  }

  void SAXHandler::characters(const     XMLCh* const    chars
                                  , const   unsigned int    length)
{
	
	
}

  
  // ---------------------------------------------------------------------------
  // SAXHandler : Overrides of the SAX ErrorHandler interface
  // ---------------------------------------------------------------------------
  void SAXHandler::fatalError(const SAXParseException& exception)
  {
    char* message = XMLString::transcode(exception.getMessage());
    cout << "Fatal Error: " << message
         << " at line: " << exception.getLineNumber()
         << endl;
  }
}
}
