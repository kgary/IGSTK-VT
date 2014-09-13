/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: Graph.cxx,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#include "Graph.h"

using namespace std;

namespace igstk
{
  namespace validation
  {
    /*
     Constructor
    */
    Graph::Graph()
    {
	  totalEdgeCount = 0;
	  
    }

    /*
     Destructor
     */
    Graph::~Graph()
    {
    }

	void Graph::initializeNodeArray()
	{
	  vertices.clear();
	}


    /*
     This function is called from SaxHandler to set the root node for the graph
    */
    void Graph::setInitialVertex(char* state)
    {
      initialstate = new char [1 + strlen(state)];
      strcpy(initialstate, state);
	 
    }

    /*
      Adds a node to the vector called, vertices
    */
    void Graph::addNode(Vertex pVertex)
    {
      vertices.push_back(pVertex);
    }

    /*
      Adds an edge to the two-dimensional adjacency matrix called, edges.
      If an edge is already present this implies there are more than one
      transitions between those two nodes.Therefore a new entry is
      added to the transition list
    */
    void Graph::addEdge(int row, int col, char*  transition)
    {
		
      if( edges[row][col])
	  
	  	        (edges[row][col])->addTransition(transition);
	  	      else
	  	        edges[row][col] = new Edge(transition);
	 
      ++totalEdgeCount;
    }

    /*
      Create a two dimensional edge array where the number of rows and
      column equal the number of nodes in the Graph(or vertexNo).
      Initialize the edge to null pointer
    */
    void Graph::initializeEdgeArray(int size)
    {
      vertexNo = size;
      edges = new Edge** [size];
      assert (edges != NULL);
      for (int row = 0; row < size; row++ )
      {
        //    Allocate the row AND fill it with NULL pointers
        edges[row] = (Edge**) calloc (size, sizeof(Edge**));
      }
    }


    /*
      Finds an unvisited node in the graph
    */
    int Graph :: findANodeInGraph(int startNode)
    {

      for (int node = startNode;node < vertexNo; node++)
      {
        if (statesVisited.find(node) == statesVisited.end())
        {
          return node;
        }
      }
    }

    /*
      Finds an already visited parent node given a node,Dst. If there
      are no parent nodes which have been visited then return -1
    */
    int Graph ::findVisitedParent(int Dst,int startNode )
    {
      for (int node = startNode; node < vertexNo; node++)
      {
		
        if((node != Dst ) && edges[node][Dst]
          && (statesVisited.find(node) != statesVisited.end()))
        {
          return node;
        }
      }
      return -1;
    }

    /*
      Find an unvisited child node for a node in the Graph
    */
    int Graph::findUnvisitedChild(int src)
    {

      for (int node = 0;node < vertexNo; node++)
      {
        if(src!=node && edges[src][node] &&
          (statesVisited.find(node) == statesVisited.end()))
        {
          return node;
        }
      }
      return -1;
    }

	/*
      Find a visited child node for a node in the Graph
    */
    int Graph::findVisitedChild(int src,int startNode)
    {

      for (int node = startNode;node < vertexNo; node++)
      {
        if(src!=node && edges[src][node] )
        {
          return node;
        }
      }
      return -1;
    }

    /*
      Given the name of a node determine its index in the vector,vertices
    */
    int Graph::findNode(char* vertexName)
    {
	
      int count = 0;
      for(vertex_iterator = vertices.begin();
            vertex_iterator != vertices.end(); vertex_iterator++)
      {
	
        if ( strcmp(vertexName,(*vertex_iterator).getName()) == 0 )
          return count;
        else count++;
      }
      return -1;
    }

	char* Graph::findVertexName(int node)
    {
		return ((vertices.at(node)).getName());
	}
	
	/*
      Find a path between two nodes
    */
	void Graph :: findPath(int Src, int Dst, std::vector<int>& path)
	{
		std::vector<int>::iterator  adj_vertex_iterator;
		std::vector<int> adjVertices;
        statesList.insert(Src);
		path.push_back(Src);
		adjVertices = findChildren(Src);
		for(adj_vertex_iterator = adjVertices.begin();
					adj_vertex_iterator != adjVertices.end(); adj_vertex_iterator++)
		{
			if(*adj_vertex_iterator == Dst)
			{
				
				return;
			}
			if(statesList.find(*adj_vertex_iterator)!= statesList.end())
				continue;
			while (path.back() != Src) 
			{
				path.pop_back();
			}

			findPath(*adj_vertex_iterator, Dst, path);
		}
	}
    
    /*
      Write to the send events file with all the meta information
    */
    void Graph:: writeStateInfoToFile(int previousStateId,
                                    int currStateId, char* transition)
    {
      
      Log <<"\t<send event=\""<< transition<<"\"/>\n"<<endl;
    

    }

    /*
	      write the header information for the xml file
	*/
	void Graph :: writeEventFileHeaderInfo(const char* componentName, const char* coverageType,std::string heuristics_text,int includeSelfTrans)
	{
	  time_t currtime;
	  struct tm * timeinfo;
	  char buffer [80];

	  Log <<"<?xml version=\"1.0\"?> \n"<<endl;
	  Log <<"<igstk> \n"<<endl;
	  Log <<"<metadata> \n"<<endl;
	  Log <<"<factory type=\""<< coverageType<<"\"";
	  
	  time ( &currtime );
	  timeinfo = localtime ( &currtime );
	  strftime (buffer,80,"%x",timeinfo);
	  Log <<"\t date=\""<<buffer<<"\"";
	  strftime (buffer,80,"%X",timeinfo);
	  Log <<"\t time=\""<<buffer<<"\">"<<endl;
	  if(!includeSelfTrans)
		Log <<"<factoryparam name=\"includeselftransitions\" value=\"false\""<<"/>"<<endl;
	  else
		  Log <<"<factoryparam name=\"includeselftransitions\" value=\"true\""<<"/>"<<endl;
	  Log <<"</factory>"<<endl;
	  Log <<"<component type=\""<< componentName<<"\"/>\n"<<endl;
	  if(!strcmp(coverageType, "pathcoverage"))
		  WriteHeuristicsData(heuristics_text);
	
	
	  Log <<"</metadata> \n"<<endl;
	  
    }

	void Graph :: WriteHeuristicsData(std::string heuristics_text)
	{
	   Log <<heuristics_text<<endl;
	}
	void Graph :: writeEventFileStartTag()
	{
		Log <<"<events> \n"<<endl;
	}

    void Graph :: writeEventFileEndTag()
	{
	      Log <<"</events> \n"<<endl;
	      Log <<"</igstk>"<<endl;
    }

    //find adjacencies
    std::vector<int> Graph :: findChildren(int start)
    {
		std::vector<int> adjVertices;      // Dynamic array of nodes
		if(start < 0 || start >= vertexNo)
			return adjVertices;
		
	
		for (int node = 0;node < vertexNo; node++)
		{
			
				if(start!=node && edges[start][node])
				{
				  adjVertices.push_back(node);
				 
				}
			
		}
		
	    return adjVertices;
    }

	//find states related to by incoming links
    std::vector<int> Graph :: findParents(int start)
    {
		std::vector<int> adjVertices;      // Dynamic array of nodes
		if(start < 0 || start >= vertexNo)
			return adjVertices;
		
	
		for (int node = 0;node < vertexNo; node++)
		{
			
				if(start!=node && edges[node][start])
				{
				  adjVertices.push_back(node);
				
				}
			
		}
		
	    return adjVertices;
    }
	/*
      Reset all transitions
    */
    void Graph::resetAllTransitionMarkings()
    {
		
		for (int row = 0; row < vertexNo; row++ )
			for (int col = 0; col < vertexNo; col++ )
			{
				if( edges[row][col])
				{
	              (edges[row][col])->resetAllTransitions();
				}
			}
    }

	/*
      Reset all transitions
    */
    void Graph::setEdgeMarking(int startnode,int targetnode, char* trans)
    {
	  if( edges[startnode][targetnode])
	  {
        (edges[startnode][targetnode])->setMarkFlag(trans);;
	  }
			
    }

	int Graph::GraphVertexCount()
	{
		return vertexNo;
	}

	int Graph::IsInitalStateReachable(int startnode)
	{
		for (int i = 0; i < vertexNo; i++ )
		{
			if( edges[i][startnode])
				return 1;
		}
		return 0;
	}

  }//end of namespace validation
}//end of namespace igstk
