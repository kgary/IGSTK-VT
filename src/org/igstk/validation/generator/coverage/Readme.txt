ReadMe file for Node and Edge coverage
--------------------------------------
Updated on: November 20, 2008

Purpose:
--------
	Coverage tools were created for ensuring that every state of a state machine is
visited during a "replay" (node coverage), or that every transition is fired during
a replay (edge coverage).

Steps to run Coverage tools:
----------------------------

	1) Check out the project.
	2) #>ant compile
	3) #>ant run-coverage -Dcoveragetype=[nodecoverage|edgecoverage] -Dfilename=[filepath for scxmlfiles]
			-Doutputdir=[output directory where the send event files are to be created]

The required softwares and versions are:
----------------------------------------

	1.	JDK 1.5 and above.
	2. 	Ant 1.7 and above.

			