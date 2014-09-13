System and Software Requirements:

1. Tested on MAC OSX, Windows XP, Linux
2. jdk 1.5 and above
3. ant 1.7
4. Optional (CMake)

Steps to run Simulator (with Drools):

	1. Check out the project IGSTK-VT-ELO.
	2. Modify the configuration file vtconfig.xml (in properties directory).
		This file has details regarding, the state machines to be used in simulation,
		the send event files to be used and others. These properties should be set for
		the simulator to start.
	3. To run the simulator, run the ant target run-simulator.
		$ant run-simulator

Running SMVIZ:

Use the ant target 'run-smviz' to run the tool.

For Design Details please follow the link below:
	http://lead1.poly.asu.edu:8090/x/BgBj
