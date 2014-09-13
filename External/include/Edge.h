/*=========================================================================

Program:   Image Guided Surgery Software Toolkit
Module:    $RCSfile: Edge.h,v $
Language:  C++
Date:      $Date: 2009/01/14 00:06:27 $
Version:   $Revision: 1.1.1.1 $

Copyright (c) ISIS Georgetown University. All rights reserved.
See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

This software is distributed WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#ifndef __Edge_h
#define __Edge_h

#include <map>
#include <iostream>

namespace igstk
{
  namespace validation
  {
    class Edge
    {
      private:
        // A map of transitions between two nodes, name of transition being
        // the key and markFlag being the value
        std::map<char*, int> transitions;
        typedef std::map<char*,int>::iterator transitionsIter;
        int markedFlagCount;

      public:

        Edge(char* trans);
        char* getTransition();
        char* getNextTransition();
        void addTransition(char* trans);
        int getMarkFlag(char* trans);
        void setMarkFlag(char* trans);
		void resetMarkFlag(char* trans);
		void resetAllTransitions();
		int areAllTransitionsCovered();
		int getTransitionCount();
    };
  } //end of namepace validation
} //end of namepace igstk
#endif  //__Edge_h
