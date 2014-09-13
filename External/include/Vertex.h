/*=========================================================================



  Program:   Image Guided Surgery Software Toolkit

  Module:    $RCSfile: Vertex.h,v $

  Language:  C++

  Date:      $Date: 2009/01/14 00:06:27 $

  Version:   $Revision: 1.1.1.1 $



  Copyright (c) ISIS Georgetown University. All rights reserved.

  See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.



     This software is distributed WITHOUT ANY WARRANTY; without even

     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR

     PURPOSE.  See the above copyright notices for more information.



=========================================================================*/



#ifndef __Vertex_h

#define __Vertex_h





namespace igstk

{

  namespace validation

  {

    class Vertex

    {

      public:



      Vertex();

      //  void setFlag(int N)       { markFlag = N;  }

      //  int  getFlag(void)  const { return markFlag;  }

      void setName(char* name);

      char* getName();



      private:

      char*  vertexName;

      int   markFlag;



    };

  }

}

#endif  // __Vertex_h

