/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: PathCoverage.h,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/
#ifndef __PathCoverage_h
#define __PathCoverage_h

#include <sstream>
#include <map>
#include <string>
#include <algorithm>
#include <unistd.h>
#include <sys/param.h>
#include <sys/stat.h>
#include <xercesc/parsers/SAXParser.hpp>
#include <xercesc/parsers/XercesDOMParser.hpp>

#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/sax2/XMLReaderFactory.hpp>
#include <xercesc/sax2/SAX2XMLReader.hpp>
#include <xercesc/sax2/DefaultHandler.hpp>
#include <xercesc/validators/common/Grammar.hpp>
#include <xercesc/dom/DOMImplementation.hpp>
#include <xercesc/dom/DOMImplementationLS.hpp>
#include <xercesc/dom/DOMImplementationRegistry.hpp>
#include <xercesc/dom/DOMBuilder.hpp>
#include <xercesc/dom/DOMException.hpp>
#include <xercesc/dom/DOMDocument.hpp>
#include <xercesc/framework/StdOutFormatTarget.hpp>
#include <xercesc/framework/LocalFileFormatTarget.hpp>
#include <xercesc/util/OutOfMemoryException.hpp>




#include "SAXHandler.h"
#include "Graph.h"
#include "Edge.h"
#include "SAXTreeErrorReporter.h"

#ifdef XERCES_CPP_NAMESPACE_USE
XERCES_CPP_NAMESPACE_USE
#endif


using namespace std;

namespace igstk
{
  namespace validation
  {
	#define HEURISTICS_XSD       "heuristics.xsd"
   
    class PathCoverage : public Graph
    {

      public:
        PathCoverage();
        ~PathCoverage(){};

      //Overridden functions from base class, Graph
      void writeToFile(char* componentPathAndName);
      int traverseGraph(int initialstateid);

      //Derived class functions
      int FindBaselinePath(int startnode, std::vector<int>& pathInfo,std::vector<string>& pathInputs,int parentstate);
	  
	  int findBasisPaths(int initialstateid,std::vector<int> paths,std::vector<string> inputs, std::vector<int>& smpath,int& fork1);

	  void FindLoopsInSMGraph(int startnode,std::vector<int> pathInfo,int parentstate,int heuristicState);

	  int FindLoopPathsBasedOnHeuristics(int startnode,std::vector<int>& pathInfo,int parentstate);

	  int FindStateWithHigherPriority(std::vector<int>& adj_states, int parentstate, int includeLoops=0);

	  int FindIfAdjacentStateFinal(std::vector<int>& adj_states);

	  void WriteLoopPathsToFile(int initialstateid, std::vector<int>& pathInfo,int heuristicsState);

	  void addHeuristicLoopInformation(char* event,char* source, int iterationCount);

	  void addFinalState(char* finalstate);

	  void addExceptionInput(char* exception_input);
	  void addStateToBeIncluded(char* included_state);
	  void addStateToBeExcluded(char* excluded_state);

	  void addTransitionToBeIncluded(char* source,char* event);

	  void addTransitionToBeExcluded(char* source,char* event);
	  void setheuristicFileName(char* name);
	  void addBaselineSelectionCriteria(char* criteria);
	  
	  void setheuristicMetaData(string text);

	  char* FindATransitionBetweenStates(int startStateId,int targetStateId);
	  int FindTransitionAvailability(int startStateId, char* transition);
	  void WritePathMatchingHeuristicsCriteria(int initialstateid);
	  int areAllHeuristicsIncluded(vector<int> path, vector <string> path_inputs);
	  void WriteBaselinePath(int initialstateid);
	  void WriteBasisPath(int initialstateid);
	  void WritePathInformation(int initialstateid,char* filename);
	
	  int ValidateHeuristicsXMLFile(char* componentPathAndName);
	  int ValidateInputs(int initialstateid);

	  void WriteHeuristicLoops(int initialstateid);
	  int FinalStateHaveUnvisitedAdjacentStates(int startnode);
	  int AddABasisPath(int initialstateid);

    protected:
	  //string path;
		struct cycleStruct
		{
			std::vector<int> strCycle;
			int    markValue;
			int    maxMarkValue;
		};

		struct inputIterationStruct
		{
			char* transition;
			int    markCount;   //# of times input has been considered
			int    maxMarkCount;//# of times input needs to be considered
		};

		struct transitionStruct
		{
			char* transition;
			
		};
	  std::set<int> nodesVisited,finalStates,finalStateReachable, includeStates, excludeStates;
	  std::set<int> decisionStates; 
	  std::set<string> errorInputs;//,excludeInputs,includeInputs;
	
	  std::multimap<int, cycleStruct > cycleList;
	  std::multimap<int, inputIterationStruct > inputIterationList;
	  std::multimap<int, transitionStruct > excludeTransitions,includeTransitions;
    
	  
	  int parentstate;
	  std::vector<int> baselinePath, smbaselinePath ;
	  std::vector<string> pathInputs, baselineInputs;
	  int loopStartState;
	  int heuristicStateFound;
	  char directoryName[FILENAME_MAX];
	  std::string pathName;
	  std::string componentName;
	  int fileCount;
	  char* heuristicFilePathAndName;
	  char* baseline_length_criteria;
	  vector <vector <int> > baselinePaths;
	  vector <vector <string> > baselinePathInputs;
	  string heuristics_text;
	  int isBaseline;
	 // int DoesPathHaveAllDecisionStates(std::vector<int> temp_path,std::set<int> decisionStates);
	 
      
	 

    };
  }
}

#endif //__PathCoverage_h
