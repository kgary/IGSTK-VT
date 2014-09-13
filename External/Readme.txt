README.txt for PathCoverage 
-------------------------------------
Last updated: November 20, 2008


Path Coverage Tool:
-------------------

Purpose:
--------
	This tool is used to generate events for the path covered given by the scxml file, guided by the heuristics file.
	
Steps to build this project:
-----------------------------

# Download xerces library and create symbolic links in the lib directory

    * libxerces-c.so -> libxerces-c.so.28.0
    * libxerces-c.so.28 -> libxerces-c.so.28.0

# #> cd External
# #> ccmake .
# #> export XERCESCROOT=[xerces library]
# #> cp -r $XERCESCROOT/src/xercesc IGSTK-VT-ELO/External/include/
# #> make

Steps to run this Tool:
-----------------------

	1) Check out the code tree and follow the above steps to build it.
	2) #>cd bin
	3) #>coverage -c pathcoverage <scxmlfile> <heuristicsfile>
	4) example: scxmlfiles are present outside the External directory in the xmlfiles/scxmlfiles directory.
	   For heuristics file you can use heuristics/heuristics1.xml file.
	   
Required Compiler:
------------------
g++ 4.3.2
