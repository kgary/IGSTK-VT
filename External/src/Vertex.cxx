/*=========================================================================

  Program:   Image Guided Surgery Software Toolkit
  Module:    $RCSfile: Vertex.cxx,v $
  Language:  C++
  Date:      $Date: 2009/01/14 00:06:27 $
  Version:   $Revision: 1.1.1.1 $

  Copyright (c) ISIS Georgetown University. All rights reserved.
  See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.

     This software is distributed WITHOUT ANY WARRANTY; without even
     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
     PURPOSE.  See the above copyright notices for more information.

=========================================================================*/

#include "Vertex.h"
#include <assert.h>
#include <string.h>

namespace igstk
{
  namespace validation
  {
    Vertex ::Vertex()
    {
      vertexName =NULL;
      markFlag = 0;
    }

    void Vertex :: setName(char* name)
    {
      vertexName = new char [1 + strlen(name)];
      assert (vertexName);
      strcpy(vertexName, name);
    }

    char* Vertex :: getName()
    {
      return vertexName;
    }
  }
}
