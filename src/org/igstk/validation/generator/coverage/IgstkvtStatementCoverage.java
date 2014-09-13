/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtStatementCoverage.java
 * Language:  Java
 * Date:      Mar 18, 2008
 *
 * Copyright (c) ISC  Insight Software Consortium.  All rights reserved.
 * See IGSTKCopyright.txt or http://www.igstk.org/copyright.htm for details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without even
 *    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *    PURPOSE.  See the above copyright notices for more information.
 *************************************************************************/

package org.igstk.validation.generator.coverage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;


/**
 * @author Santhosh
 *
 */
public class IgstkvtStatementCoverage extends IgstkvtGraph {


	private Map<Integer,Integer> stateMarkings= new HashMap<Integer,Integer>();

	static Logger logger = Logger.getLogger(IgstkvtStatementCoverage.class);

    /**
     * This method writes the send events data to file.
     * @param componentName
     * @param sendEventsDirectory
     * @throws IOException
     */
    void writeToFile(String componentName, String sendEventsDirectory)
        throws Exception {
        try{  
        	System.out.println("ComponentName is "+componentName);
        	System.out.println("sendEventDirectory is "+sendEventsDirectory);
	        String filename = componentName + "_nodecoverage_test1.xml";
	        File seFile = new File(sendEventsDirectory + "/" + filename);
	        logger.info("The seFile is :  " + seFile);
	        log = new FileWriter(seFile);
	        logger.info("Writing send events to file : " + filename);
	        writeEventFileHeaderInfo(componentName,
	            		"IgstkvtStatementCoverage",null,0);
	        int initialStateId = findNode(initialstate);
	        logger.info("The initialStateId is "+initialStateId);
	        writeEventFileStartTag();
	        traverseGraph(initialStateId);
	        writeEventFileEndTag();
	        log.close();
	        logger.info("The sendEventsDirectory is : " + sendEventsDirectory);
	        logger.info("The Full file path is : " + seFile.getAbsolutePath());
	        System.out.println("The full path is: " + seFile.getAbsolutePath());
	        super.setFilename(seFile.getAbsolutePath());
	        findUncoveredNodes(initialStateId,sendEventsDirectory,
	            		componentName);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Check if the output directory exists!!!!");
        }finally {
            if (log != null) {
                log.close();
            }
        }

    }

    /**
     * Traverses the graph created from scxml file and generates send events.
     * @param initialStateId
     * @throws Exception
     */


    int traverseGraph(int initialStateId) throws Exception{
	    int node,checkAvailability=0;
	    int parentState = initialStateId;
	    int previousStateId  = initialStateId;
	    int currStateId = initialStateId;
	   Iterator<Integer>  adj_vertex_iterator;
	    //Iterator<Vector> adj_vertex_iterator;
	    Vector<Integer> adjVertices;
	    int stateVisitCount=0;
	    int nextStateVisitCount=0;
	    int stateid;
	    System.out.println("The InitialStateId is: "+initialStateId);
	    if (initialStateId == -1){
	    	return initialStateId;
	    }
	    for (int i=0;i<statesVisited.size();i++){
	    	if(statesVisited.get(i).intValue() != initialStateId){
	    		checkAvailability++;
	    	}
	    }
	    if(checkAvailability == statesVisited.size()){
	    	statesVisited.add(initialStateId);
	    }

	    logger.info("The currentstateid is "+currStateId);
	    stateMarkings.put(currStateId,1);
	    try{
	    //while there are unvisited nodes present in the graph do
	    //find a child node
	    while ((statesVisited.size() != vertexNo)&& (currStateId != -1)){
	    	currStateId = findUnvisitedChild(currStateId);
	    	if(currStateId != -1){
	    		stateMarkings.put(currStateId,1);
	    	}else{
	    		adjVertices = findChildren(previousStateId);
	    		if(adjVertices.size()>0){
	    			adj_vertex_iterator = adjVertices.iterator();
	    			stateid=adj_vertex_iterator.next();
	    			//stateid=Integer.parseInt(adj_vertex_iterator.toString());
	    			if(adjVertices.size()>1){
	    				stateVisitCount = stateMarkings.get(stateid);
	    				//stateVisitCount = stateMarkings.get(adj_vertex_iterator);
	    				//stateid = *adj_vertex_iterator;
	    				//stateid=Integer.parseInt(adj_vertex_iterator.toString());
	    				//adj_vertex_iterator.next();
	    				//stateid=adj_vertex_iterator.next();
	    				while (adj_vertex_iterator.hasNext()){
	    					int tempstateid = adj_vertex_iterator.next();
	    					nextStateVisitCount = stateMarkings.
	    						get(tempstateid);
	    					if(nextStateVisitCount < stateVisitCount){
							//stateid =*adj_vertex_iterator;
								stateid=tempstateid;
								break;
	    					}
	    					if (stateVisitCount < nextStateVisitCount){
	    						break;
	    					}
							stateVisitCount = stateMarkings.get(
										stateid);
							stateid=tempstateid;
							//stateid=adj_vertex_iterator.next();
							
	    				}
	    			}
	    			currStateId = stateid;
	    			stateMarkings.put(currStateId, stateMarkings.
	    						get(currStateId)+1);
	    		}
	    		stateVisitCount = 0;
	    	}
	    	if(currStateId != -1){
	    		String transition = (edges[previousStateId][currStateId]).
	    			getTransition();
	    		writeStateInfoToFile(previousStateId,currStateId,transition);
	    		parentState = previousStateId;
	    		previousStateId = currStateId;
	    		if (!statesVisited.contains(currStateId)){
	    			statesVisited.add(currStateId);
	    		}
			}
	    	
		}
	    }catch (Exception e){e.printStackTrace();}
	    return currStateId;
    }

	/**
	 * Method to find Uncovered nodes.
	 * @param srcNode
	 * @param pathName
	 * @param componentName
	 * @return
	 * @throws Exception
	 */
	int findUncoveredNodes(int srcNode, String pathName,
				String componentName)throws Exception{
		int count = 2;
		int retval = 0;
		while((statesVisited.size() != vertexNo)){
			File filename;
			filename = new File(pathName +"\\"+componentName
					+"_nodecoverage_test"+count+".xml");
			System.out.println("All nodes not visited. Writing "
					+"unvisited nodes into a  new file "+filename);
			// File seFile = new File(fme);
			log = new FileWriter(filename);

			writeEventFileHeaderInfo(componentName,"NodeCoverage",null,0);
			writeEventFileStartTag();
			retval = traverseGraph(srcNode);
			writeEventFileEndTag();
			log.close();
			count++;
		}
        System.out.println("All States have been Visited.");
        return 0;
    }
}
