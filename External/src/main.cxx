/*=========================================================================



  Program:   Image Guided Surgery Software Toolkit

  Module:    $RCSfile: main.cxx,v $

  Language:  C++

  Date:      $Date: 2009/01/14 00:06:27 $

  Version:   $Revision: 1.1.1.1 $



  Copyright (c) ISIS Georgetown University. All rights reserved.

  See IGSTKCopyright.txt or http://www.igstk.org/HTML/Copyright.htm for details.



     This software is distributed WITHOUT ANY WARRANTY; without even

     the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR

     PURPOSE.  See the above copyright notices for more information.



=========================================================================*/



#include <xercesc/parsers/SAXParser.hpp>

#include <xercesc/sax/HandlerBase.hpp>

#include <xercesc/util/XMLString.hpp>



#include "SAXHandler.h"

#include <iostream>
#include "PathCoverage.h"



XERCES_CPP_NAMESPACE_USE



namespace igstk

{

  namespace validation

  {

    /*

      Parses SCXML doument using SAXParser

    */

    int parseDocument (char* coveragetype, char*  xmlFile, char* heuristicFile=0)

    {

      try

      {

        XMLPlatformUtils::Initialize();

      }

      catch (const XMLException& toCatch)

      {

        char* message = XMLString::transcode(toCatch.getMessage());

        cout << "Error during initialization! :\n" << message << "\n" << endl;

        XMLString::release(&message);

        return 1;

      }



      //set the parameters

      SAXParser* parser = new SAXParser();

      parser->setDoValidation(true);

      parser->setDoNamespaces(true);



      //Create a handler and associate it with the parser. Also pass the coveragetype

      //as a parameter while constructing the handler

	  //based on coveragetype specified from the command prompt create an object
      //for one of the derived classes NodeCoverage,EdgeCoverage
	  Graph* pGraph;

	   if(!strcmp(coveragetype, "pathcoverage"))
      {
        pGraph= new PathCoverage();
      }

      SAXHandler* handler = new SAXHandler(coveragetype,pGraph);

      parser->setDocumentHandler(handler);

      parser->setErrorHandler(handler);



      //Extract the component name from the scxml filename. This will the part

      //of the filename excluding the .xml extension



      std::string::size_type pos,lastPos;

      const std::string& delimiters = ".,/,\\";

      std::string fileName( xmlFile );

      lastPos = fileName.find_first_not_of(delimiters, 0);

      pos     = fileName.find_last_of(delimiters, fileName.length());





      //std::string componentName = fileName.substr(lastPos, pos-lastPos);

      std::string componentName = fileName.substr(0, pos);

      handler->setComponentName(componentName.c_str());

	  if(heuristicFile != 0)
		  handler->setheuristicFileName(heuristicFile);

      try

	{



        //The scxml file is parsed twice. The first time a vertex array

        //is created which helps determine the number of vertices(N) in the

        //Graph being created. During the second pass a 2D-array of size N*N

        //is created and populated with the edge information



        handler->setParseType(1);



        parser->parse(xmlFile);



        handler->setParseType(2);



        parser->parse(xmlFile);

		
		//After parsing is done traverse the graph and create SendEvent file
   
        //based on coverage type call the writeToFile function to write the
        //events information into the send events file
	     if(!strcmp(coveragetype, "pathcoverage"))
        {
		  ((PathCoverage*)pGraph)->setheuristicFileName(heuristicFile);
		  ((PathCoverage*)pGraph)->writeToFile((char*)componentName.c_str());
			//((PathCoverage*)pGraph)->testPathCoverage(componentPathAndName);
        }
	    if(pGraph)
          delete pGraph;

        delete parser;
        delete handler;

      }

      catch (const XMLException& toCatch)

      {

        char* message = XMLString::transcode(toCatch.getMessage());

        cout << "Exception message is: \n" << message << "\n" << endl;

        XMLString::release(&message);

        return -1;

      }

      catch (const SAXParseException& toCatch)

      {

        char* message = XMLString::transcode(toCatch.getMessage());

        cout << "Exception message is: \n" << message << "\n" << endl;

        XMLString::release(&message);

        return -1;

      }

      catch (...)

      {

        cout << "Unexpected Exception \n" << endl;

        return -1;

      }

    }



    void usage()

    {

      cout <<

           "Usage: coverage -c <pathcoverage> <scxmlfilename>\n"

           <<endl;

    }

  }

} //end of namespace igstk



/*

 Main function

 */

int main ( int argc, char * argv[] )

{
  if (argc < 4)

  {

    igstk::validation::usage();

    return 1;

  }
  if((!strcmp(argv[2], "pathcoverage")) && (argc < 5))
  {
	cout <<"Please Input heuristic file name. " <<endl;

    cout <<"Usage: coverage -c <pathcoverage> <scxmlfilename> <heuristicfilename>\n"<<endl;

	return 1;
  }

  if (argc > 4)

  {
	  igstk::validation::parseDocument(argv[2],argv[3],argv[4]);
  }
  else
  {
	igstk::validation::parseDocument(argv[2],argv[3]);
  }



  return 0;

}

