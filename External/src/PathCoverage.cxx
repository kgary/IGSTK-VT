/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: PathCoverage.cxx,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#include "PathCoverage.h"


namespace igstk
{
  namespace validation
  {
    /*
      Constructor
    */
    PathCoverage :: PathCoverage(void)
    {
     //  cycleExistsInPath = 0;
	  // cyclesToConsider = 1;
	  // loopsTraversed = 0;
		loopStartState = -1;
		heuristicStateFound = -1;
		fileCount = 1;
		isBaseline = 1;
		excludeStates.clear();
		finalStates.clear();
    }

	void PathCoverage::setheuristicFileName(char* name)
	{
		heuristicFilePathAndName = new char [1 + strlen(name)];
		strcpy(heuristicFilePathAndName, name);
	}

    /*
      Traverses the graph and writes the send events to a file
      ([componentname]_[coveragetype]_test.xml). Once the graph traversal
      is complete then checks to see if there are any uncovered edges

    */
    void PathCoverage :: writeToFile(char* componentPathAndName)
    {
      /**********************************************/
	  //testPathCoverage(componentPathAndName);
	  //return;
	  /*********************************************/
	  int currStateId =0;
	  int ret = 0;
	  const std::string& delimiters = "/,\\";
	  std::string::size_type pos1,pos2;
	  std::string name( componentPathAndName );
	  pos1     = name.find_last_of(delimiters, name.length());
	  pathName = name.substr(0, pos1);
	  pos2     = pathName.find_last_of(delimiters, name.length());
	  pathName = pathName.substr(0, pos2);
	  componentName = name.substr(pos1+1,name.length());
      
	  time_t seconds;
      seconds = time (NULL);
	  int status;
	   
	  sprintf(directoryName,"%s/sendEventFiles/%s_pathcoverage_tests_%ld",pathName.c_str(), componentName.c_str(),seconds);
	  cout <<"Creating directory: " <<directoryName <<endl;
      if((mkdir(directoryName, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH)) == -1)
	  {
		  cout <<"Could not create directory to store path files " <<endl;
		  return;
	  }
      cout <<"Created directory: " <<directoryName <<endl;
      
      
       /********************************************************************/
		try

        {

          XMLPlatformUtils::Initialize();

        }

        catch (const XMLException& toCatch)

        {

          char* message = XMLString::transcode(toCatch.getMessage());

          cout << "Error during initialization! :\n" << message << "\n" << endl;

          XMLString::release(&message);

          return;

        }
		if(ValidateHeuristicsXMLFile(componentPathAndName) > 0)
		{
			cout<<"Heuristic validation error"<<endl;
			return;
		}
        SAXParser* parser = new SAXParser();
        parser->setDoValidation(true);
        parser->setDoNamespaces(true);    // optional

		SAXHandler* handler = new SAXHandler(this);

        parser->setDocumentHandler(handler);

        parser->setErrorHandler(handler);
		handler->setComponentName(componentPathAndName);

        try {
			parser->parse(heuristicFilePathAndName);
        }
        catch (const XMLException& toCatch) {
            char* message = XMLString::transcode(toCatch.getMessage());
            cout << "Exception message is: \n"
                 << message << "\n";
            XMLString::release(&message);
            return ;
        }
        catch (const SAXParseException& toCatch) {
            char* message = XMLString::transcode(toCatch.getMessage());
            cout << "Exception message is: \n"
                 << message << "\n";
            XMLString::release(&message);
            return ;
        }
        catch (...) {
            cout << "Unexpected Exception 123 \n" ;
            return ;
        }
     	delete parser;
        delete handler;
		/***************************************************************/
		
      currStateId = traverseGraph(findNode(initialstate));
      
    }

	struct SortBaselinePathsInAscendingOrder {	// compare Records using "name" as the key
		bool operator()(const std::vector<int>& a, const std::vector<int>& b) const
			{ return a.size()<b.size(); }
	};

	struct SortBaselinePathInputsInAscendingOrder {	// compare Records using "name" as the key
		bool operator()(const std::vector<string>& a, const std::vector<string>& b) const
			{ return a.size()<b.size(); }
	};

	void PathCoverage :: WritePathMatchingHeuristicsCriteria(int initialstateid)
	{
		char filename[FILENAME_MAX];
		vector< vector<int> >::iterator  baseline_paths_iterator;
		
		vector< vector<string> >::iterator  path_inputs_iterator;
		std::vector<int>::iterator  smpath_iterator;
		
		path_inputs_iterator = baselinePathInputs.begin();
		//cout<<"baselinePaths.size"<<baselinePaths.size()<<endl;

		//if(!strcmp(baseline_length_criteria, "shortest"))
		{
		//	cout<<"baselinePaths.size"<<baselinePaths.size()<<endl;
		for(baseline_paths_iterator = baselinePaths.begin();
            baseline_paths_iterator != baselinePaths.end(); baseline_paths_iterator++)
		{
			if(!areAllHeuristicsIncluded(*baseline_paths_iterator,*path_inputs_iterator))
			{
				
			}
			else
			{
				if(smbaselinePath != *baseline_paths_iterator)
				{
					baselinePath.clear();
					baselineInputs.clear();
					baselinePath = *baseline_paths_iterator;
				//	cout<<"baselinePaths.size"<<baselinePath.size()<<endl;
					baselineInputs = *path_inputs_iterator;
				//	cout<<"baselinePaths.size"<<baselineInputs.size()<<endl;
					sprintf(filename,"%s/%s_pathcoverage_heuristicpath_%d.xml",directoryName,componentName.c_str(),fileCount);
					fileCount++;
					WritePathInformation(initialstateid,filename);
					WriteBasisPath(initialstateid);
					
					
				}
			}
			path_inputs_iterator++;
			}
		
		}
	/*	if(!strcmp(baseline_length_criteria, "longest"))
		{
			
			baselinePath = baselinePaths.back();
			baselineInputs = baselinePathInputs.back();
			return baselinePath;
		}*/
		


	}
	int PathCoverage :: areAllHeuristicsIncluded(vector<int> path, vector <string> path_inputs)
	{
		multimap <int, transitionStruct> :: iterator m1_TransIter;
		transitionStruct transStruct;
		std::set<int>::iterator  include_state_iterator;
		std::vector<int>::iterator  smpath_iterator;
		//cout<<"Start"<<endl;
		/*for (smpath_iterator = path.begin();
			smpath_iterator != path.end();smpath_iterator++)
		{
			cout<<*smpath_iterator;
		}
		cout<<endl;*/
		for(m1_TransIter = includeTransitions.begin();
            m1_TransIter != includeTransitions.end(); m1_TransIter++)
			{
			
				if(std::find(path.begin(), path.end(), m1_TransIter->first) == path.end())
				{
					
					return 0;
				}
				
				transStruct = m1_TransIter->second ;
				if(find(path_inputs.begin(), path_inputs.end(), transStruct.transition) == path_inputs.end())
				{
					return 0;
				}
			}

		for(include_state_iterator = includeStates.begin();
            include_state_iterator != includeStates.end(); include_state_iterator++)
			{
				if(find(path.begin(), path.end(), *include_state_iterator) == path.end())
				//if(find(path.begin(), path.end(), m1_TransIter->first) == path.end())
				{
					//cout<<"return"<<endl;
					return 0;
				}
				
			}
		return 1;
	}

	
    /*
      Traverses the graph created from scxml file and generates sendevents
    */
    int PathCoverage :: traverseGraph(int initialstateid)
    {
		
		std::vector<int> paths,smpath,smpath1;
		std::vector<string> sminputs;
		std::vector<int>::iterator  path_iterator,smpath_iterator,pi,ii;
		std::vector<string>::iterator  path_inputs_iterator;
		std::vector<Vertex>::iterator  vertex_iterator;
		int count=0;

		
		for(vertex_iterator = vertices.begin();
		vertex_iterator != vertices.end(); vertex_iterator++)
		{
			cout<<count<<": "<<(*vertex_iterator).getName()<<endl;
			count++;
		}
		cout<<"\n"<<endl;
		 
		paths.push_back(initialstateid);
		if(!ValidateInputs(initialstateid))
		{
			cout<<"Input validation failed"<<endl;
			return 0;
		}

		try
		{
			WriteBaselinePath(initialstateid);
		}
		catch( char * str ) 
		{
		  cout << "Exception raised: " << str << '\n';
		  return 0; //check return value assignment
		}
		WriteBasisPath(initialstateid);
		smbaselinePath = baselinePath;
		//sminputs = baselineInputs;
		isBaseline = 0;
		
		WriteBaselinePath(initialstateid);
		WriteHeuristicLoops(initialstateid);
		  		
	}

	int PathCoverage :: ValidateInputs(int initialstateid)
	{
		if(finalStates.size() == 0)
		{
			cout<<"Please enter a final state"<<endl;
			return 0;
		}
		
		return 1;
	}

	void PathCoverage :: WriteBaselinePath(int initialstateid)
	{
		//BASELINE PATH		
		char filename[FILENAME_MAX];
		
		std::vector<int> paths;
		std::vector<int>::iterator  smpath_iterator, it, it1;
		std::vector<string>::iterator  path_inputs_iterator;
		
		
		
		
	    //cout<<"baselinePaths.size() "<<baselinePaths.size()<<endl;
		
		if (isBaseline)
		{
			baselinePaths.clear();
			baselinePathInputs.clear();
			baselinePath.clear();
			baselineInputs.clear();
			pathInputs.clear();
			paths.push_back(initialstateid);
			FindBaselinePath(initialstateid,paths,pathInputs,0);
			cout<<"BASELINE PATH: ";
		  
			sort( baselinePaths.begin( ), baselinePaths.end( ), SortBaselinePathsInAscendingOrder() );
			sort( baselinePathInputs.begin( ), baselinePathInputs.end( ), SortBaselinePathInputsInAscendingOrder() );
			sprintf(filename,"%s/%s_pathcoverage_baselinepath.xml",directoryName,componentName.c_str());
			if(!strcmp(baseline_length_criteria, "longest"))
			{
				//cout<<"longest"<<endl;
				baselinePath = baselinePaths.back();
				baselineInputs = baselinePathInputs.back();

               
				
			}
			if(!strcmp(baseline_length_criteria, "shortest"))
			{
				//cout<<"shortest"<<endl;
				baselinePath = baselinePaths.front();
				baselineInputs = baselinePathInputs.front();
			}
			WritePathInformation(initialstateid,filename);
		}
		else
		{
			WritePathMatchingHeuristicsCriteria(initialstateid);
			
		}
		
    	//fileCount++;
		

		resetAllTransitionMarkings();
	}

	void PathCoverage :: WritePathInformation(int initialstateid,char* filename)
	{

		int previousStateId, currStateId;
		char* stateTransition;
		std::vector<int>::iterator  smpath_iterator;
		std::vector<string>::iterator  path_inputs_iterator;
		
		cout <<"Writing send events to file " <<filename <<endl;
		Log.open(filename);
		writeEventFileHeaderInfo(componentName.c_str(),"pathcoverage",heuristics_text);
		writeEventFileStartTag();

		previousStateId  = initialstateid;
		path_inputs_iterator = baselineInputs.begin();
	
		for (smpath_iterator = baselinePath.begin();
			smpath_iterator != baselinePath.end();smpath_iterator++)
		{
			cout<<*smpath_iterator;
			currStateId = *smpath_iterator;
			if(previousStateId != currStateId )
			{
				stateTransition = new char[(*path_inputs_iterator).length() + 1];
				strcpy(stateTransition, (*path_inputs_iterator).c_str());
				
				writeStateInfoToFile(previousStateId,currStateId,stateTransition);
				path_inputs_iterator++;
				previousStateId = currStateId;
				delete stateTransition;

			}
			
		}
		writeEventFileEndTag();
		Log.close();
		cout<<endl;
		cout<<"\n"<<endl;
	}
		
	void PathCoverage :: WriteBasisPath(int initialstateid)
	{
		//BASIS PATHS
		char filename[FILENAME_MAX];
		int stateid;
		int pos,fork;
		int previousStateId, currStateId;
		char* stateTransition;
		std::vector<int>::iterator  smpath_iterator,ii;
		char buf[100];
		std::vector<string>::iterator  baseline_inputs_iterator,path_inputs_iterator;
		std::vector<int>::iterator  baseline_path_iterator;
		std::vector<int> smpath;

		fork=0;
		stateid= initialstateid;
			
		baseline_path_iterator= baselinePath.begin(); 
		baseline_inputs_iterator = baselineInputs.begin();
	
		do
		{
		pathInputs.clear();
		pathInputs.insert(pathInputs.begin(),baselineInputs.begin(),baseline_inputs_iterator);
		smpath.clear();
		smpath.insert(smpath.begin(),baselinePath.begin(),baseline_path_iterator);
	
		pos = findBasisPaths(stateid,baselinePath,baselineInputs, smpath,fork);
		//cout<<"pos "<<pos<<endl;
		//if (pos > 0)
		if((pos > 0) && (pos<baselinePath.size()))
		{
			cout<<"ALTERNATE PATH: ";
			
			//ii = find(vertices.begin(), vertices.end(), pos);   
			if(isBaseline)
			{
			sprintf(filename,"%s/%s_pathcoverage_basispath_%d.xml",directoryName,componentName.c_str(),fileCount);
			}
			else
			{
			sprintf(filename,"%s/%s_pathcoverage_heuristicpath_%d.xml",directoryName,componentName.c_str(),fileCount);
			}
			cout <<"Writing send events to file " <<filename <<endl;
			Log.open(filename);
			writeEventFileHeaderInfo(componentName.c_str(),"pathcoverage",heuristics_text);
	
			writeEventFileStartTag();

			previousStateId  = initialstateid;
			path_inputs_iterator = pathInputs.begin();
			//cout<<"path inputs size "<<pathInputs.size()<<endl;
		    for (smpath_iterator = smpath.begin();
					smpath_iterator != smpath.end();smpath_iterator++)
			{
				
				currStateId = *smpath_iterator;

				if(previousStateId != currStateId )
				{
					stateTransition = new char[(*path_inputs_iterator).length() + 1];
					//cout<<"before copy"<<previousStateId<<currStateId<<endl;
					strcpy(stateTransition, (*path_inputs_iterator).c_str());
					writeStateInfoToFile(previousStateId,currStateId,stateTransition);
					path_inputs_iterator++;
	     			previousStateId = currStateId;
					//cout<<"donr writing stateTransition "<<stateTransition<<endl;
					delete stateTransition;
					//cout<<"after copy"<<endl;
				
				}
				cout<<*smpath_iterator;
			}
			if((!isBaseline) && (!areAllHeuristicsIncluded(smpath,pathInputs)))
			{
				ii = find(baselinePath.begin(), baselinePath.end(), smpath.back());
				path_inputs_iterator = baselineInputs.begin();
				for (smpath_iterator = baselinePath.begin();
					smpath_iterator != ii;smpath_iterator++)
				{
					path_inputs_iterator++;
				}
				for (smpath_iterator = ii+1;
					smpath_iterator != baselinePath.end();smpath_iterator++)
					{
						cout<<*smpath_iterator;
						stateTransition = new char[(*path_inputs_iterator).length() + 1];
						strcpy(stateTransition, (*path_inputs_iterator).c_str());
						writeStateInfoToFile(previousStateId,currStateId,stateTransition);
						path_inputs_iterator++;
							
						delete stateTransition;
					
				}
			}
			fileCount++;
			writeEventFileEndTag();
		    Log.close();
			cout<<endl;
				
			
			baseline_path_iterator = baselinePath.begin()+pos-1;
			stateid=*(baseline_path_iterator);;
			//cout<<"statedid "<<stateid<<endl;
			baseline_inputs_iterator=baselineInputs.begin()+pos-1;
			//cout<<"donr incr"<<endl;
						
							
		}
		else if (pos == -1)
		{
			cout<<"No basis paths"<<endl;
			break;
		}
		else
		{
		
			baseline_path_iterator++;
			baseline_inputs_iterator++;
			stateid=*baseline_path_iterator;
			//cout<<"statedid "<<stateid<<endl;
			/*std::vector<int>::iterator  pI;
			pi = find(smpath.begin(), smpath.end(), stateid);
		
			while ((pi != smpath.end()) && (path_iterator != paths.end()))
			{
				smpath.push_back(stateid);
				path_iterator += 1;
			    stateid=*path_iterator;
				pi = find(smpath.begin(), smpath.end(), stateid);
				path_inputs_iterator1++;
			
			}*/
			
	    }
	
		
		fork=0;

		
		}while (baseline_path_iterator != baselinePath.end());
		if(!IsInitalStateReachable(initialstateid))
		{
			cout<<"Initial state not reachable"<<endl;
			if(isBaseline)
			{
			sprintf(filename,"%s/%s_pathcoverage_basispath_%d.xml",directoryName,componentName.c_str(),fileCount);
			}
			cout <<"Writing send events to file " <<filename <<endl;
			Log.open(filename);
			writeEventFileHeaderInfo(componentName.c_str(),"pathcoverage",heuristics_text);
			writeEventFileStartTag();
			AddABasisPath(initialstateid);
			fileCount++;
			writeEventFileEndTag();
		    Log.close();
			cout<<endl;
		}
		resetAllTransitionMarkings();
	}
   
	int PathCoverage :: AddABasisPath(int initialstateid)
	{
		std::vector<int> adjVertices; 
		std::vector<int>::iterator vertex_iterator, ii;
		std::vector<string>::iterator baseline_inputs_iterator;
		int previousStateId,nextStateId,finalstateid;
		char* stateTransition;

		finalstateid = *(finalStates.begin());
		adjVertices = findChildren(finalstateid); //currently assumes one final state at a time
		if(adjVertices.size()>0)
		{
			ii = find(baselinePath.begin(), baselinePath.end(), finalstateid);
			cout<<"*ii"<<*ii<<endl;
			
			vertex_iterator = baselinePath.begin();
			cout<<"*vertex_iterator"<<*vertex_iterator<<endl;
			previousStateId = *vertex_iterator;
			vertex_iterator++;
			cout<<"*vertex_iterator"<<*vertex_iterator<<endl;
			baseline_inputs_iterator = baselineInputs.begin();
			while(vertex_iterator != ii+1) 
			{
				cout<<"inside"<<endl;
				nextStateId = *vertex_iterator;
				stateTransition = new char[(*baseline_inputs_iterator).length() + 1];
				strcpy(stateTransition, (*baseline_inputs_iterator).c_str());
				writeStateInfoToFile(previousStateId,nextStateId,stateTransition);
				delete stateTransition;
				previousStateId = nextStateId;
				baseline_inputs_iterator++;
				vertex_iterator++;
								
			}
		}
	}

	void PathCoverage :: WriteHeuristicLoops(int initialstateid)
	{
		multimap<int, inputIterationStruct>::iterator nodeInIterationList;
		std::vector<int> smpath;
		std::vector<int>::iterator  smpath_iterator;

		for (nodeInIterationList = inputIterationList.begin();
		nodeInIterationList != inputIterationList.end();nodeInIterationList++)
		{
			statesVisited.clear();
			smpath.push_back(initialstateid);
			FindLoopsInSMGraph(initialstateid,smpath,initialstateid,nodeInIterationList->first);
		}
	
		for (nodeInIterationList = inputIterationList.begin();
		nodeInIterationList != inputIterationList.end();nodeInIterationList++)
		{
			statesVisited.clear();
			resetAllTransitionMarkings();
			smpath.clear();
			smpath.push_back(initialstateid);
			heuristicStateFound = -1;
			FindLoopPathsBasedOnHeuristics(initialstateid,smpath,initialstateid);
								
			for (smpath_iterator = smpath.begin();
			smpath_iterator != smpath.end();smpath_iterator++)
			{
				cout<<*smpath_iterator;
			}
			cout<<endl;
			cout<<"\n"<<endl;
								
			WriteLoopPathsToFile(initialstateid,smpath,heuristicStateFound);
			
		}
	}

	void PathCoverage :: addHeuristicLoopInformation(char* event,char* source, int iterationCount)
	{
		
		inputIterationStruct myStruct;
		myStruct.transition =  new char [1 + strlen(event)];
		strcpy(myStruct.transition, event);
		
		myStruct.markCount = 0;
		myStruct.maxMarkCount = iterationCount;
		
		inputIterationList.insert(pair<int, inputIterationStruct>(findNode(source), myStruct));
	}

	void PathCoverage :: addFinalState(char* finalstate)
	{
		//cout<<"finalStates.size(): "<<finalStates.size()<<"final state "<<finalstate<<endl;
		finalStates.insert(findNode(finalstate));
		//finalStates.insert(findNode("InvalidTimeStampState"));
		
	}
	void PathCoverage :: addExceptionInput(char* exception_input)
	{
		errorInputs.insert(exception_input);
	}
	void PathCoverage :: addStateToBeIncluded(char* included_state)
	{
		includeStates.insert(findNode(included_state));
	}
	void PathCoverage :: addStateToBeExcluded(char* excluded_state)
	{
		excludeStates.insert(findNode(excluded_state));
	}
	void PathCoverage :: addTransitionToBeExcluded(char* source,char* event)
	{
		transitionStruct transStruct;
		transStruct.transition =  new char [1 + strlen(event)];
		strcpy(transStruct.transition, event);
		excludeTransitions.insert(pair<int, transitionStruct>(findNode(source), transStruct));
		
	}
	void PathCoverage :: addTransitionToBeIncluded(char* source,char* event)
	{
		//cout<<"before include transiton"<<endl;
		transitionStruct transStruct;
		transStruct.transition =  new char [1 + strlen(event)];
		strcpy(transStruct.transition, event);
		includeTransitions.insert(pair<int, transitionStruct>(findNode(source), transStruct));
		//cout<<"after include transiton"<<endl;
	}
	void PathCoverage :: addBaselineSelectionCriteria(char* criteria)
	{
		baseline_length_criteria = new char [1 + strlen(criteria)];
		strcpy(baseline_length_criteria, criteria);
	}

	void PathCoverage :: setheuristicMetaData(string text)
	{
		heuristics_text = text;
		
	}

	int PathCoverage :: findBasisPaths(int stateid,std::vector<int> paths, std::vector<string> inputs, std::vector<int>& smpath,int& fork)
	{
		std::vector<int>::iterator  adj_vertex_iterator, nodeInNodeList;
		std::vector<int> adjVertices;      // Dynamic array of nodes
		stringstream s;
		int pos =0;
		int nextStateInPath;
		int length;
		int laststate=-1;
		std::vector<int>::iterator ii,pi;
		std::vector<string>::reverse_iterator r_input_iterator;
		
		s<<stateid;
		std::vector<string> inputsInReverseOrder;
		if(smpath == paths)
			return -1;
		adjVertices = findChildren(stateid);
		ii = find(paths.begin(), paths.end(), stateid);
		r_input_iterator = inputs.rbegin();
			
			if((fork>0) && (ii != paths.end()))
			{
				
			 	
				pos=fork;
				smpath.push_back(stateid);
				for (nodeInNodeList = ii+1;
					nodeInNodeList != paths.end();nodeInNodeList++)
				{
				//	cout<<"*(ii+1) "<<*(ii+1)<<endl;
					smpath.push_back(*nodeInNodeList);
					
					inputsInReverseOrder.insert(inputsInReverseOrder.begin(),*r_input_iterator);
					//cout<<"*r_input_iterator "<<*r_input_iterator<<endl;
				    r_input_iterator++;
				}
				//cout<<"pathInputs.back()"<<pathInputs.back()<<endl;
				pathInputs.insert(pathInputs.end(),inputsInReverseOrder.begin(),inputsInReverseOrder.end());	
				return pos;
					
			}
		
		int prioritystate= FindStateWithHigherPriority(adjVertices,stateid);
		
		if ( prioritystate > -1)
		{
		
			char* transition = FindATransitionBetweenStates(stateid,prioritystate);
			
			if(transition != NULL)
			{
				if(!(edges[stateid][prioritystate])->getMarkFlag(transition))
				{
					(edges[stateid][prioritystate])->setMarkFlag(transition);
			
					if(smpath.size() > 0)
					{
					laststate = smpath.back();
			
					}
					if(laststate!=stateid) 
					{
						smpath.push_back(stateid);
						pathInputs.push_back(string(transition));
					}
			
					pos = findBasisPaths(prioritystate,paths, inputs,smpath,fork);
				
				}
			}
			
		}	
		else
		{
			if(smpath.size() > 0)
			{
			laststate = smpath.back();

			}
			
			if(laststate!=stateid) 
			{
			smpath.push_back(stateid);
		
			
			}
		
			for(adj_vertex_iterator = adjVertices.begin();
					adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
			{
				//cout<<"adj_vertex_iterator: "<<*adj_vertex_iterator<<endl;
				if((isBaseline) || (excludeStates.find(*adj_vertex_iterator) == excludeStates.end()))
				{
					char* transition = FindATransitionBetweenStates(stateid,*adj_vertex_iterator);
			
					if (transition == NULL)
					{
						continue;
					}
					if((adjVertices.size()>1) )//|| (((edges[stateid][*adj_vertex_iterator])->getTransitionCount()) > 1)
					{
					//	cout<<"adj_vertex_iterator: "<<*adj_vertex_iterator<<endl;		
						if(((*(ii+1) == *adj_vertex_iterator)  )
							//&& finalStates.find(*(ii+1)) == finalStates.end())  //if transition has to be included then dont look at alternate decision
							|| (edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition) ||
							((!isBaseline) && (includeTransitions.find(*(ii+1)) != includeTransitions.end())))

						{
					//		cout<<"continuem adj_vertex_iterator: "<<*adj_vertex_iterator<<"*(ii+1) "<<*(ii+1)<<endl;
					//		cout<<"(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition) "<<(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition)<<endl;
							(edges[stateid][*adj_vertex_iterator])->setMarkFlag(transition);
							char* transition1 = (edges[stateid][*adj_vertex_iterator])->getTransition();
					//		cout<<"transition:"<<transition<<"TRANSITION1"<<transition1<<endl;
							if((((edges[stateid][*adj_vertex_iterator])->getTransitionCount()) > 1)&&
							((strcmp(transition1,transition))))
							{
								//transition = FindATransitionBetweenStates(stateid,*adj_vertex_iterator);
					//			cout<<"INSIDE:"<<transition<<endl;
								fork = smpath.size();
						//		cout<<"fork:"<<fork<<endl;
								
		
								
							}
							else
							continue;
						}
						else 
						{
					//		cout<<"(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition) "<<(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition)<<endl;
							
							//if(*(ii+1) != *adj_vertex_iterator)
							{
					//			cout<<"INSIDE2:"<<transition<<endl;
							fork = smpath.size();
		
							(edges[stateid][*adj_vertex_iterator])->setMarkFlag(transition);
							}
		
						}
					}
					else
					{
					//	cout<<"INSIDE3: "<<*adj_vertex_iterator<<"stateid "<<stateid<<endl;
					//	cout<<"(edges[stateid][*adj_vertex_iterator])->getTransitionCount() "<<(edges[stateid][*adj_vertex_iterator])->getTransitionCount()<<endl;
					//	cout<<"(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition) "<<(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition)<<endl;
						char* transition1 = (edges[stateid][*adj_vertex_iterator])->getTransition();
					//	cout<<"INSIDFE3transition:"<<transition<<"TRANSITION1"<<transition1<<endl;
						if((((edges[stateid][*adj_vertex_iterator])->getTransitionCount()) > 1) &&
								(!(edges[stateid][*adj_vertex_iterator])->getMarkFlag(transition)))//more than one transition exist between 2 states// &&
						{
							
							if(*(ii+1) == *adj_vertex_iterator) 
							{
								(edges[stateid][*adj_vertex_iterator])->setMarkFlag(transition);
								transition = FindATransitionBetweenStates(stateid,*adj_vertex_iterator);
								if (transition == NULL)
								{
									continue;
								}
								
							}
							fork = smpath.size();;
					//		cout<<"fork: "<<fork<<endl;
					//		cout<<"INSIDE3:"<<transition<<endl;
							(edges[stateid][*adj_vertex_iterator])->setMarkFlag(transition);
										
						}
						
			
					}
					
					
					
					if(laststate!=stateid) 
					{
					
						//cout<<"added "<<transition<<endl;
						pathInputs.push_back(string(transition));
					
					}
					
					
					pos = findBasisPaths(*adj_vertex_iterator,paths, inputs, smpath,fork);
					
					if (pos >0) break;
				}
			}
		}
		
		return pos;
		
	}
	
	
	char* PathCoverage :: FindATransitionBetweenStates(int startStateId,int targetStateId)
	{
		
		char* transition = 0;
		if((startStateId < GraphVertexCount()) || (targetStateId < GraphVertexCount()))
		{
			if(edges[startStateId][targetStateId])
		    {
			//	cout<<"startStateId: "<<startStateId<<endl;
				transition = (edges[startStateId][targetStateId])->
														getTransition();

				int transitionAvailable = 1;
				int transitionsConsidered = 1;
			
				transitionAvailable = FindTransitionAvailability(startStateId,transition);

				int flag =(edges[startStateId][targetStateId])->getMarkFlag(transition);

				while((flag || (!transitionAvailable)) && 
								 (transitionsConsidered<(edges[startStateId][targetStateId])->getTransitionCount()))
								 //(!((edges[startStateId][targetStateId])->areAllTransitionsCovered())))//
				{
					transition= (edges[startStateId][targetStateId])->
													getNextTransition();

					transitionsConsidered++;
					if(transition==NULL)
						continue;
					flag =(edges[startStateId][targetStateId])->getMarkFlag(transition);
					transitionAvailable = FindTransitionAvailability(startStateId,transition);

				}
				if(transitionAvailable == 0)
					transition=NULL;
			}
		}
		return transition;
		
	}

	int PathCoverage :: FindTransitionAvailability(int startStateId, char* transition)
	{
		multimap <int, transitionStruct> :: iterator m1_TransIter;
		transitionStruct transStruct;
		if(!isBaseline)
		{
			for (m1_TransIter = includeTransitions.lower_bound(startStateId);
							m1_TransIter != includeTransitions.upper_bound(startStateId); m1_TransIter++)
			{
				transStruct = m1_TransIter->second ;
				if(!strcmp(transition,transStruct.transition))
					return 1;
			}
			for (m1_TransIter = excludeTransitions.lower_bound(startStateId);
							m1_TransIter != excludeTransitions.upper_bound(startStateId); m1_TransIter++)
			{
				transStruct = m1_TransIter->second ;
				if(!strcmp(transition,transStruct.transition))
					return 0;
			}
		}
		return 1;
	}

	int PathCoverage :: FindStateWithHigherPriority(std::vector<int>& adj_states, int parentstate, int includeLoops)
		{
			
			vector<int>::iterator it;
			multimap <int, cycleStruct> :: iterator m1_AcIter;
			cycleStruct myStruct;
			multimap <int, transitionStruct> :: iterator m1_TransIter;
			transitionStruct transStruct;
		
			for ( it=adj_states.begin() ; it != adj_states.end(); it++ )
			{
		
				if(!isBaseline)
				{
				// If the state is in the exclude states then ignore
				 if (excludeStates.find(*it) != excludeStates.end())
					 continue;
		
				// If the state is in the include states then it has higher priority
				if((includeStates.find(*it) != includeStates.end()))
				{
					return *it;
				}
			/*	if(includeTransitions.find(parentstate)!= includeTransitions.end())
				{
				for (m1_TransIter = includeTransitions.lower_bound(parentstate);
						m1_TransIter != includeTransitions.upper_bound(parentstate); m1_TransIter++)
				{
					transStruct = m1_TransIter->second ;
					if(transStruct.target == *it)
					{
						return *it;
						cout<<"*it ok: "<<*it<<endl;
					}
				}
				}
				if(excludeTransitions.find(parentstate)!= excludeTransitions.end())
				{
				for (m1_TransIter = excludeTransitions.lower_bound(parentstate);
						m1_TransIter != excludeTransitions.upper_bound(parentstate); m1_TransIter++)
				{
					transStruct = m1_TransIter->second ;
					if(transStruct.target == *it)
					{
						continue;
						cout<<"*it not ok: "<<*it<<endl;
					}
				}
				}*/
		
				}
				if ((includeLoops) && (cycleList.count(*it)>0))
				{
		
					for (m1_AcIter = cycleList.lower_bound(*it);
						m1_AcIter != cycleList.upper_bound(*it); ++m1_AcIter)
					{
						myStruct = m1_AcIter->second ;
                  
              
		
						if((myStruct.markValue < myStruct.maxMarkValue))
						{
							return *it;
						}
					}
				}


			}
			return -1;
			
		
	}

		int PathCoverage :: FindIfAdjacentStateFinal(std::vector<int>& adj_states)
		{
			vector<int>::iterator it;
			for ( it=adj_states.begin() ; it != adj_states.end(); it++ )
			{
				if (finalStates.find(*it) != finalStates.end())
					return *it;
			}
			return -1;
		}

	int PathCoverage :: FindBaselinePath(int startnode,std::vector<int>& pathInfo,std::vector<string>& pathInputs, int parentstate)
	{
		std::vector<int>::iterator  adj_vertex_iterator, nodeInNodeList,ii;
		std::set<int>::iterator vertex_iterator;
		std::vector<int> adjVertices;      // Dynamic array of nodes
	//	cout<<"final sttes size "<<finalStates.size()<<endl;
		if((!isBaseline) && (excludeStates.find(startnode) != excludeStates.end()))
			return 1;
		statesVisited.insert(startnode);
		adjVertices = findChildren(startnode);
	//	cout<<"startnode: "<<startnode<<endl;
		if (adjVertices.size() ==0)
		{
			cout<<"No transitions found for state= "<<startnode<<" cannot continue"<<endl;
			return -1;
		}
		parentstate = startnode;
	//	cout<<"after exclude state"<<endl;
		int stateid= FindStateWithHigherPriority(adjVertices,startnode);
		if ( stateid > -1)
		{
			
			pathInfo.push_back(stateid);;
			char* transition = FindATransitionBetweenStates(startnode,stateid);
			if(transition==NULL)
				return -1;

			int flag = (edges[startnode][stateid])->getMarkFlag(transition);

			(edges[startnode][stateid])->setMarkFlag(transition);
			flag = (edges[startnode][stateid])->getMarkFlag(transition);

			pathInputs.push_back(string(transition));
			FindBaselinePath(stateid,pathInfo,pathInputs,parentstate);
			
		}

		else
		{
		
			for(adj_vertex_iterator = adjVertices.begin();
					adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
			{
		//		cout<<"before find transition"<<endl;
				char* transition = FindATransitionBetweenStates(startnode,*adj_vertex_iterator);
		//		cout<<"after find transition"<<endl;
				if (transition == NULL)
					continue;
				//int flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);

				if((errorInputs.find(transition) == errorInputs.end())&& 
					(!(edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition)))
				{
		//			cout<<"*adj_vertex_iterator:"<<*adj_vertex_iterator<<endl;
					if(finalStates.find(*adj_vertex_iterator) == finalStates.end())
					{
		//				cout<<"reached"<<endl;
						int currstate; 
						int prevstate; 
						if(pathInfo.size() > 2)
						{
							currstate = pathInfo.back();
							prevstate = *(pathInfo.end()-2);
							while (currstate != startnode) 
			  				{
								if(!(edges[prevstate][currstate]))
								{
									cout<<"Unexpected condition. Cannot continue"<<endl;
									return -1;
								}
								char* transition1 = (edges[prevstate][currstate])->
														getTransition();
							//	int flag = (edges[prevstate][currstate])->getMarkFlag(transition1);

								(edges[prevstate][currstate])->resetMarkFlag(transition1);
								
							//	flag = (edges[prevstate][currstate])->getMarkFlag(transition1);
								pathInputs.pop_back();

								pathInfo.pop_back();;
												
								currstate = prevstate;
								prevstate = *(pathInfo.end()-2);
								

							}
						}
						
						//int flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);
	//					cout<<"reached here also"<<endl;
						(edges[startnode][*adj_vertex_iterator])->setMarkFlag(transition);
						//flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);

						pathInputs.push_back(string(transition));

						pathInfo.push_back(*adj_vertex_iterator);;
						FindBaselinePath(*adj_vertex_iterator,pathInfo,pathInputs,parentstate);
					}
				
				
					else
					{
					
						pathInfo.push_back(*adj_vertex_iterator);;

						transition = (edges[startnode][*adj_vertex_iterator])->
													getTransition();
						if (transition == NULL)
						{

								continue;
						}
						int flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);

						(edges[startnode][*adj_vertex_iterator])->setMarkFlag(transition);
						flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);
						pathInputs.push_back(string(transition));
						baselinePathInputs.push_back(pathInputs);
						baselinePaths.push_back(pathInfo);
						if(FinalStateHaveUnvisitedAdjacentStates(*adj_vertex_iterator))
							FindBaselinePath(*adj_vertex_iterator,pathInfo,pathInputs,parentstate);
						//cout<<"adjVertices.size():"<<adjVertices.size()<<endl;
						//cout<<"statesVisited.size():"<<statesVisited.size()<<"GraphVertexCount():"<<GraphVertexCount()<<endl;
						//if((adjVertices.size()==1)&& (statesVisited.size() < GraphVertexCount()))
						//	FindBaselinePath(*adj_vertex_iterator,pathInfo,pathInputs,parentstate);
						
					
					}
				}
				
			}
		}
		
		return 0;
	}

	int PathCoverage :: FinalStateHaveUnvisitedAdjacentStates(int startnode)
	{
		std::vector<int>::iterator  adj_vertex_iterator;
		std::vector<int> adjVertices;  
		adjVertices = findChildren(startnode);
		char* transition;
		for(adj_vertex_iterator = adjVertices.begin();
		          adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
        {
			transition = FindATransitionBetweenStates(startnode,*adj_vertex_iterator);
			if(!(edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition))
				return 1;

		}
		return 0;
	}

	void PathCoverage :: FindLoopsInSMGraph(int startnode,std::vector<int> pathInfo,int parentstate, int heuristicState)
	{
		std::vector<int>::iterator  adj_vertex_iterator;
		std::vector<int> adjVertices;      // Dynamic array of nodes
		std::set<int>::iterator vertex_iterator;
		std::vector<int>::iterator nodeInNodeList;
		if(excludeStates.find(startnode) != excludeStates.end())
			return;
	
		statesVisited.insert(startnode);
		adjVertices = findChildren(startnode);

		
		
		if (startnode==heuristicState)
		{
			loopStartState = startnode;
		}
		

		for(adj_vertex_iterator = adjVertices.begin();
		          adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
        {
			
		
			if(statesVisited.find(*adj_vertex_iterator) == statesVisited.end())
			{
				while (pathInfo.back() != startnode)
  
				{
		
					pathInfo.pop_back();;
				}
				pathInfo.push_back(*adj_vertex_iterator);;
				
		
				parentstate = startnode;
            	  FindLoopsInSMGraph(*adj_vertex_iterator,pathInfo,parentstate,heuristicState);
		    }
		    else
		    {
				
		
				if (loopStartState >-1)
				{

					multimap<int, inputIterationStruct>::iterator m1_AcIter;
					inputIterationStruct myStruct1;
					std::vector<int>::iterator ii,ii1;
		
					ii = find(pathInfo.begin(), pathInfo.end(), heuristicState);
					char* transition;
					if((ii+1) !=pathInfo.end())
					{
		
					transition = (edges[*ii][*(ii+1)])->
                                            getTransition();
					}
					else
					{
						transition = (edges[*ii][*adj_vertex_iterator])->
                                            getTransition();
					}
					
					for (m1_AcIter = inputIterationList.lower_bound(heuristicState);
									m1_AcIter != inputIterationList.upper_bound(heuristicState); m1_AcIter++)
					{
						myStruct1 = m1_AcIter->second ;
		
						if(strstr (myStruct1.transition,transition)!= NULL)
						{
					
							cout<<"loopPath==";
							cycleStruct myStruct;
						  
                          for (nodeInNodeList = ii;
			                   nodeInNodeList != pathInfo.end();nodeInNodeList++)
			                   {
								   (myStruct.strCycle).push_back(*nodeInNodeList);
								   cout<<*nodeInNodeList;

								}
							  
								ii1 = find(pathInfo.begin(), pathInfo.end(), *adj_vertex_iterator);

                          for (nodeInNodeList = ii1;
			                   nodeInNodeList != ii+1;nodeInNodeList++)
			                   {

								(myStruct.strCycle).push_back(*nodeInNodeList);
								cout<<*nodeInNodeList;
		



								}
							   cout<<endl;
							    if((myStruct.strCycle).size() >0) 
							   {
								   myStruct.markValue = 0;
									myStruct.maxMarkValue = myStruct1.maxMarkCount;
									cycleList.insert(pair<int, cycleStruct>(loopStartState, myStruct));
									loopStartState = -1;
							   }
						}
					}
				}
			}
		}
		
	    
	    vertex_iterator = statesVisited.find( startnode );
	    statesVisited.erase(vertex_iterator);

	}

	int PathCoverage :: FindLoopPathsBasedOnHeuristics(int startnode,std::vector<int>& pathInfo,int parentstate)
	{
		std::vector<int> adjVertices;      // Dynamic array of nodes
		std::vector<int>::iterator  adj_vertex_iterator;
        int retval = 0;
		if(excludeStates.find(startnode) != excludeStates.end())
			return retval;
        
		statesVisited.insert(startnode);
		adjVertices = findChildren(startnode);
        if ( heuristicStateFound == -1)
		{
			int stateid= FindStateWithHigherPriority(adjVertices,startnode,1);
			if ( stateid > -1)
			{
		
				pathInfo.push_back(stateid);;
		
				heuristicStateFound= stateid;
				parentstate = startnode;
				retval = FindLoopPathsBasedOnHeuristics(stateid,pathInfo,parentstate);
				
			}
		}
		
			if ( heuristicStateFound > -1)
			{
				int finalstate = FindIfAdjacentStateFinal(adjVertices);
				if(finalstate > -1)
				{
					pathInfo.push_back(finalstate);;
							retval = 1;
				}
			}
				
			for(adj_vertex_iterator = adjVertices.begin();
					adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
			{
				char* transition = (edges[startnode][*adj_vertex_iterator])->
                                            getTransition();
			
		
				int flag = (edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition);
				
	           
				if(!(edges[startnode][*adj_vertex_iterator])->getMarkFlag(transition) && !(retval))
				{
		
					
						int currstate = *(pathInfo.end()-1);
					int prevstate = *(pathInfo.end()-2);
					while (currstate != startnode)
	  
					{
						transition = (edges[prevstate][currstate])->
                                            getTransition();
						(edges[prevstate][currstate])->resetMarkFlag(transition);
						int flag = (edges[prevstate][currstate])->getMarkFlag(transition);
		
						pathInfo.pop_back();;
						
										
						currstate = prevstate;
						prevstate = *(pathInfo.end()-2);
					}
					transition = (edges[startnode][*adj_vertex_iterator])->
                                            getTransition();
						(edges[startnode][*adj_vertex_iterator])->setMarkFlag(transition);
						pathInfo.push_back(*adj_vertex_iterator);;
		
						parentstate = startnode;
						retval = FindLoopPathsBasedOnHeuristics(*adj_vertex_iterator,pathInfo,parentstate);
				}
			
			if (retval)
			{
		
				break;
			}
		}
		return retval;	
				
			
		
	}

		
	void PathCoverage :: WriteLoopPathsToFile(int initialstateid, std::vector<int>& pathInfo,int heuristicsState)
	{
		std::vector<int> looppath;
		multimap <int, cycleStruct> :: iterator m1_AcIter;
		multimap <int, cycleStruct> :: reverse_iterator m1_RcIter;
		vector<int>::iterator it, smpath_iterator;
		multimap<int, cycleStruct>::iterator mmit,mmet;
		cycleStruct myStruct;
        mmit = cycleList.upper_bound(heuristicsState);
		char filename[FILENAME_MAX];
	    int previousStateId;
	    char* stateTransition;
	    int currStateId;
		
		int loopCount = 0;
	
        
		for (m1_AcIter = cycleList.lower_bound(heuristicsState);
						m1_AcIter != cycleList.upper_bound(heuristicsState); m1_AcIter++)
		{
	
						myStruct = m1_AcIter->second ;
						it = find(pathInfo.begin(), pathInfo.end(), m1_AcIter->first);  
						if(it != pathInfo.end())
						{
	
						//	while(loopCount < myStruct.maxMarkValue)
							//if (loopCount < myStruct.maxMarkValue)
							//{
				
									looppath.insert (looppath.begin(),pathInfo.begin(),it+1);
	
				
								
								//loopCount++;
	
								for (int i=0;i<myStruct.maxMarkValue;i++)
								{
									looppath.insert (looppath.end(),(myStruct.strCycle).begin()+1,(myStruct.strCycle).end());
	
								}
	
				
								looppath.insert (looppath.end(),it+1,pathInfo.end());
	
				
								

								cout<<"LOOP PATH: ";
								
								sprintf(filename,"%s/%s_pathcoverage_heuristiclooppath_test%d.xml",directoryName,componentName.c_str(),fileCount);
								cout <<"Writing send events to file " <<filename <<endl;
								Log.open(filename);
								writeEventFileHeaderInfo(componentName.c_str(),"pathcoverage",heuristics_text);
								writeEventFileStartTag();

								previousStateId  = initialstateid;
								for (smpath_iterator = looppath.begin();
										smpath_iterator != looppath.end();smpath_iterator++)
								{
									currStateId = *smpath_iterator;
									if(previousStateId != currStateId )
									{
										stateTransition = (edges[previousStateId][currStateId])->
														getTransition();
										writeStateInfoToFile(previousStateId,currStateId,stateTransition);
										previousStateId = currStateId;
									//cout<<*smpath_iterator;
									}
									cout<<*smpath_iterator;
								}
								fileCount++;
								writeEventFileEndTag();
								Log.close();
								cout<<endl;
								looppath.clear();
							}
						
					//	}
					//	break;
		}
	
			if(m1_AcIter != cycleList.upper_bound(heuristicsState))
			  {
			   
				  cycleList.insert(m1_AcIter,pair<int, cycleStruct>(heuristicsState, myStruct));
				  cycleList.erase(m1_AcIter);
	
			  }
		
	}

	int PathCoverage :: ValidateHeuristicsXMLFile(char* componentPathAndName)
	{
		int retval = 0;
		char*                    gXmlFile               = 0;
		XMLCh*                   gOutputEncoding        = 0;
		SAXParser::ValSchemes    gValScheme       = SAXParser::Val_Always;
		// Initialize 
		try
		{
			XMLPlatformUtils::Initialize();
		}

		catch(const XMLException &toCatch)
		{
			XERCES_STD_QUALIFIER cerr << "Error during Xerces-c Initialization.\n"
				 << "  Exception message:"
				 << StrX(toCatch.getMessage()) << XERCES_STD_QUALIFIER endl;
			return 1;
		}
		
		//cout<<"heuristicFilePathAndName"<<heuristicFilePathAndName<<endl;
       //gXmlFile = "..\\..\\IGSTK-VT-Abba\\xmlFiles\\heuristics\\heuristics1.xml";

		//
		//  Create our parser, then attach an error handler to the parser.
		//  The parser will call back to methods of the ErrorHandler if it
		//  discovers errors during the course of parsing the XML document.
		//
		SAXParser *parser = new SAXParser;
		parser->setValidationScheme(gValScheme);
		parser->setDoNamespaces(true);
		parser->setDoSchema(true);
		parser->setValidationSchemaFullChecking(true);
		parser->setExternalNoNamespaceSchemaLocation(HEURISTICS_XSD);
   // parser->setCreateEntityReferenceNodes(gDoCreate);

		SAXTreeErrorReporter *errReporter = new SAXTreeErrorReporter();
		parser->setErrorHandler(errReporter);

		//
		//  Parse the XML file, catching any XML exceptions that might propogate
		//  out of it.
		//
		bool errorsOccured = false;
		try
		{
			parser->parse(heuristicFilePathAndName);
			
		}
		catch (const OutOfMemoryException& e)
		{
			XERCES_STD_QUALIFIER cerr << "OutOfMemoryException" << XERCES_STD_QUALIFIER endl;
			errorsOccured = true;
		}
		catch (const XMLException& e)
		{
			XERCES_STD_QUALIFIER cerr << "An error occurred during parsing\n   Message: "
				 << StrX(e.getMessage()) << XERCES_STD_QUALIFIER endl;
			errorsOccured = true;
		}

		catch (const SAXParseException& e)
		{
			const unsigned int maxChars = 2047;
			XMLCh errText[maxChars + 1];

			XERCES_STD_QUALIFIER cerr << "\nDOM Error during parsing: '" << heuristicFilePathAndName << "'\n"
				 << "DOMException code is:  " <<  XERCES_STD_QUALIFIER endl;

		  //  if (DOMImplementation::loadDOMExceptionMsg(e.code, errText, maxChars))
		  //       XERCES_STD_QUALIFIER cerr << "Message is: " << StrX(errText) << XERCES_STD_QUALIFIER endl;

			errorsOccured = true;
		}

		catch (...)
		{
			XERCES_STD_QUALIFIER cerr << "An error occurred during parsing\n " << XERCES_STD_QUALIFIER endl;
			errorsOccured = true;
		}

		// If the parse was successful, output the document data from the DOM tree
		if (!errorsOccured && !errReporter->getSawErrors())
		{
			std::cout<<"Schema Validation Successful."<<std::endl;

		} 
		else
			retval = 4;

		//
		//  Clean up the error handler. The parser does not adopt handlers
		//  since they could be many objects or one object installed for multiple
		//  handlers.
		//
		delete errReporter;

		//
		//  Delete the parser itself.  Must be done prior to calling Terminate, below.
		//
		delete parser;

		// And call the termination method
		XMLPlatformUtils::Terminate();

		XMLString::release(&gOutputEncoding);

		return retval;
	}

	
	 

	 void SAXTreeErrorReporter::warning(const SAXParseException&)
{
    //
    // Ignore all warnings.
    //
}

	void SAXTreeErrorReporter::error(const SAXParseException& toCatch)
	{
		fSawErrors = true;
		XERCES_STD_QUALIFIER cerr << "Error at file \"" << StrX(toCatch.getSystemId())
			 << "\", line " << toCatch.getLineNumber()
			 << ", column " << toCatch.getColumnNumber()
			 << "\n   Message: " << StrX(toCatch.getMessage()) << XERCES_STD_QUALIFIER endl;
	}

	void SAXTreeErrorReporter::fatalError(const SAXParseException& toCatch)
	{
		fSawErrors = true;
		XERCES_STD_QUALIFIER cerr << "Fatal Error at file \"" << StrX(toCatch.getSystemId())
			 << "\", line " << toCatch.getLineNumber()
			 << ", column " << toCatch.getColumnNumber()
			 << "\n   Message: " << StrX(toCatch.getMessage()) << XERCES_STD_QUALIFIER endl;
	}

	void SAXTreeErrorReporter::resetErrors()
	{
		fSawErrors = false;
	}
		

}//end of namespace validation
}//end if namespace igstk
