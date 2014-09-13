/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: Graph.h,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#ifndef __Graph_h
#define __Graph_h

#include <vector>
#include <stack>
#include <set>
#include <sstream>
#include <string>
#include <cstring>
#include <fstream>
#include <iostream>
#include <assert.h>
#include <cstdlib>

#include "Vertex.h"
#include "Edge.h"

namespace igstk
{
namespace validation
  {
  class Graph
    {
    public:
      Graph();
      ~Graph();
      void setInitialVertex(char* state);
      void addNode(Vertex pVertex);
      void addEdge(int row, int col,char* transition);
      void initializeEdgeArray(int size);
      virtual void writeToFile(char* componentPathAndName){};
      virtual int traverseGraph(int parentid){};
      int findNode(char* vertexName);
	  void resetAllTransitionMarkings();
	  void setEdgeMarking(int startnode,int targetnode, char* trans);
	  void initializeNodeArray();
	  


    protected:
      std::vector<Vertex> vertices;      // Dynamic array of nodes
      std::vector<Vertex>::iterator  vertex_iterator;
      std::set<int> statesVisited;
	  std::set<int> statesList;
      std::set<int> parentList;
      Edge**  *edges;      // Dynamic 2-d array of pointers

      int vertexNo;        // #of vertices in the graph / states in the state machine (SM)
      char* initialstate;
      int totalEdgeCount;  // Total number of edges in the graph / inputs in the SM
     std:: ofstream Log;

      int findANodeInGraph(int startNode);
	  int findVisitedParent(int Dst, int startNode = 0);
	  int findUnvisitedChild(int src);

      //int  findPath(int Src, int Dst);
	  void findPath(int Src, int Dst, std::vector<int>& path);
	  void writeStateInfoToFile(int previousStateId, int currStateId,
                   char* transition);
	  void writeEventFileHeaderInfo(const char* componentName,const char* coverageType,std::string heuristics_text="",int includeSelfTrans = 0);
	  void writeEventFileStartTag();
	  void writeEventFileEndTag();
	  std::vector<int>  findChildren(int start);
	  std::vector<int> findParents(int start);
	  void WriteHeuristicsData(std::string heuristics_text);
	  int GraphVertexCount();
	  char* findVertexName(int node);
	  int findVisitedChild(int src, int startNode = 0);
	  int IsInitalStateReachable(int startnode);

    };
  }
}
#endif //__Graph_h
