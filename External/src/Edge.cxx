/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: Edge.cxx,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#include "Edge.h"

namespace igstk
{
  namespace validation
  {
    /*
      Constructor
    */
    Edge::Edge(char* trans)
    {
      transitions[trans]=0;
      markedFlagCount = 0;
    }

    /*
      Retrieves the first transition
    */
    char* Edge:: getTransition()
    {
      std::map<char*,int>::iterator iter;
      for(iter = transitions.begin(); iter != transitions.end(); iter++)
      {
        return iter->first;
      }
    }

    /*
      Retrieves the next unmarked transition
    */
    char* Edge::getNextTransition()
    {
      std::map<char*,int>::iterator iter;
      for(iter = transitions.begin(); iter != transitions.end(); iter++)
      {
        if (!(iter->second))
          return iter->first;
      }
	  return NULL;
    }

    /*
      Adds a new transition to the map of transitions, for a particular edge
    */
    void Edge::addTransition(char* trans)
    {
      transitions[trans]=0;
    }

    /*
      Gets the mark flag for a transition
    */
    int Edge::getMarkFlag(char* trans)
    {
      transitionsIter  pos = transitions.find(trans);
      if (pos != transitions.end())
      {
        return pos->second;
      }
    }

    /*
      Sets the mark flag for a transition
    */
    void Edge::setMarkFlag(char* trans)
    {
      transitions[trans]= 1;
	  
      markedFlagCount++;
	 
	  
    }

	/*
      Resets the mark flag for a transition
    */
    void Edge::resetMarkFlag(char* trans)
    {
	 // transitionsIter  pos = transitions.find(trans);
      if ( transitions[trans] != 0)
	  {
      transitions[trans]= 0;
	 
      markedFlagCount--;
	  }
	
    }

	/*
      Reset all transitions
    */
    void Edge::resetAllTransitions()
    {
	  std::map<char*,int>::iterator iter;
      for(iter = transitions.begin(); iter != transitions.end(); iter++)
      {
		  if(iter->second)
		  {
			transitions[iter->first]= 0;
			
			markedFlagCount--;
			
		  }
	  }
	 
    }
	
	int Edge:: getTransitionCount()
	{
		return transitions.size();
	}

    /*
      Checks to see if all transitions between 2 nodes have been covered
      during the Graph traversal and returns appropriate value
    */
    int Edge::areAllTransitionsCovered()
    {
		
      if (markedFlagCount == transitions.size())
        return true;
      return false;
    }
  }//end of namespace validation
}//end of namespace igstk
