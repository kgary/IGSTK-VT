/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtTransitionCoverage.java
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author Santhosh
 *
 */
public class IgstkvtTransitionCoverage extends IgstkvtGraph {

    int totalEdgeCount; // Total number of edges in the graph
    int markedEdgeCount; // Number of edges that have been marked
    static Logger logger = Logger.getLogger(IgstkvtTransitionCoverage.class);
    Map<Integer,Integer> stateMarkings= new HashMap<Integer,Integer>();
    /**
     * Constructor.
     */
    public IgstkvtTransitionCoverage() {
        markedEdgeCount = 0;
    }

    /**
     * Traverses the graph and writes the send events to a file
     * ([componentname]_[coveragetype]_test.xml). Once the graph traversal is
     * complete then checks to see if there are any uncovered edges.
     * @param componentName
     * @param sendEventsDirectory
     * @throws Exception
     *
     */
    void writeToFile(String componentName, String sendEventsDirectory)
        throws Exception {
        try{
        	int countparent =1;
        	while (findUnvisitedChild(findNode(initialstate))!= -1){
	        	String filename = componentName + "_edgecoverage_test_basispath"+countparent+".xml";
		    	logger.info("Writing send events to file : " + filename);
		    	int currStateId = 0;
		    	File seFile = new File(sendEventsDirectory + "/" + filename);
		    	log = new FileWriter(seFile);
		    	writeEventFileHeaderInfo(componentName,
		    			"IgstkvtTransitionCoverage",null,0);
		    	writeEventFileStartTag();
		    	
		    		currStateId = traverseGraph(findNode(initialstate));
		    	
		    	writeEventFileEndTag();
		    	log.close();
		    	countparent++;
        	}
	    	int ret = findUncoveredEdges(findNode(initialstate),
	    			sendEventsDirectory, componentName);
	    	//super.setFilename(seFile.getAbsolutePath());
        }catch (FileNotFoundException e) {
        	e.printStackTrace();
    	}catch (Exception e) {
    	    e.printStackTrace();
    	}finally{
    		if (log != null) {
    			log.close();
    	    }
    	}
    }

    



    /**
     * Traverses the graph created from SCXML file and generates send events.
     * @param initialstateid
     * @return
     */

    int traverseGraph(int initialstateid){
    	int previousStateId  = initialstateid;
    	int parentState = initialstateid;
    	int currStateId=0;
    	int startNode;
    	Iterator<Integer>  adj_vertex_iterator;
    	Vector<Integer> adjVertices;
    	Vector<Integer> pathToCurrentNode= new Vector<Integer>();
	    int stateVisitCount=0;
	    int nextStateVisitCount=0;
	    int stateid;
	    try{
	    	IgnoreSelfTransitions();
	        //Maintain a set of vertices that have been visited
	    	if (!statesVisited.contains(initialstateid)){
	    		statesVisited.add(initialstateid);
	    	}
			stateMarkings.put(currStateId,1);
			pathToCurrentNode.addElement(initialstateid);

	        //while there are unvisited nodes present in the graph do
	        //find a child node

			while ((markedEdgeCount < super.totalEdgeCount)
					&&(statesVisited.size() != vertexNo)&& (currStateId != -1)){
				currStateId = findUnvisitedChild(previousStateId);
				if(currStateId != -1){
					stateMarkings.put(currStateId,1);
				}else{
					adjVertices = findChildren(previousStateId);
					if(adjVertices.size()>0){
						adj_vertex_iterator = adjVertices.iterator();
						logger.info("The index is "+new HashMap
									<String, Integer>());
						stateid=adj_vertex_iterator.next();
						if(adjVertices.size()>1){
							 stateVisitCount= stateMarkings.get(stateid);
				             while (adj_vertex_iterator.hasNext()){
								int tempstateid = adj_vertex_iterator.next();
								nextStateVisitCount = stateMarkings.get(
										tempstateid);

								if (nextStateVisitCount < stateVisitCount){
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
						stateMarkings.put(currStateId, (
								stateMarkings.get(currStateId))+1);
					}
					stateVisitCount = 0;
				}
				if(currStateId != -1){
					String transition = (edges[previousStateId][currStateId]).
						getTransition();

		  //Write the edge information into the send event file and mark
		  //the edge as visited and increment the markedEdgeCount for that
		  //particular edge, the edge between the previousStateId and
		  //currStateId

					writeStateInfoToFile(previousStateId,currStateId,
							transition);
					pathToCurrentNode.addElement(currStateId);

					if((edges[previousStateId][currStateId]).
							getMarkFlag(transition)== 0){
						(edges[previousStateId][currStateId]).
							setMarkFlag(transition);
						markedEdgeCount++;
					}else{
						if(!(edges[previousStateId][currStateId]).
								areAllTransitionsCovered()){
							transition = (edges[previousStateId][currStateId]).
								getNextTransition();
							writeStateInfoToFile(previousStateId, currStateId,
										transition);
							if((edges[previousStateId][currStateId]).
										getMarkFlag(transition)==0){
								(edges[previousStateId][currStateId]).
									setMarkFlag(transition);
								markedEdgeCount++;
							}
						}
					}
		  // Traverse back edges(edges from current state to any of previously
		  // visited states) write those edges into the send event file and
		  //  mark those edges as visited
					markEdgesFromChild(currStateId,pathToCurrentNode);

		  //continue traversing through the graph

			//add the current state to statesVisited set, assign it to the
			//variable previousStateId go back to the beginning of while
			//loop and start over until all nodes have been visited
					if (!statesVisited.contains(currStateId)){
						statesVisited.add(currStateId);
					}
					parentState = previousStateId;
					previousStateId = currStateId;
				}
			}
	    }catch(Exception e){
	    	//e.printStackTrace();
	    }

	    return currStateId;
    }


    /**
     *Method to ignore self transitions.
     */
    void IgnoreSelfTransitions(){
		String transition;
		for (int i=0;i<vertexNo;i++){
			if (edges[i][i]!= null){
				while (!edges[i][i].areAllTransitionsCovered()){
					transition = (edges[i][i]).getNextTransition();
					if((edges[i][i]).getMarkFlag(transition)==0){
						(edges[i][i]).setMarkFlag(transition);
						markedEdgeCount++;
					}
				}
			}
		}
	}

    /**
     * Finds an unvisited node in the graph which has a visited parent, this
     * ensures that a path exists.
     * @param parentNode
     * @return
     */

    int findAnUnvisitedNodeWithVisitedParentInGraph(int[] parentNode) {
        int startNode = 0;
        int node = -1;

        // Find an unvisited node in the graph and find its parent, keep
        // looping while the node does not have a visited parent

        while (parentNode[0] == -1) {
            node = findANodeInGraph(startNode);
            // node incremented by one here so that following search by
            // findANodeInGraph does not return the same node and get
            // into an infinite loop
            startNode = node + 1;
            parentNode[0] = findVisitedParent(node,0);
        }

        return node;
    }

    /**
     * Find out the uncovered edges and write them into a new file.
     * @param componentName
     * @param srcNode
     * @throws Exception
     * @return
     */

    int findUncoveredEdges(int srcNode, String pathName, String componentName)
    	throws Exception{
    	
		int count = 1;
		Vector<Integer> path = new Vector<Integer>();
		Iterator<Integer>  node_iterator;
		String transition="";
		int srcState;

        while(markedEdgeCount < super.totalEdgeCount){
        	File filename;
          	for (int i=0;i<vertexNo;i++){
          		for (int j=0;j<vertexNo;j++){
          			if((edges[i][j])!=null && !((edges[i][j]).
          					areAllTransitionsCovered())){
          				path.clear();
          				filename = new File(pathName +"\\"+componentName
          						+"_edgecoverage_test"+count+".xml");
          				System.out.println("All nodes not visited. "
          				     +"Writing unvisited edges into a  new file "
          				     +filename);
          				log = new FileWriter(filename);
          				writeEventFileHeaderInfo(componentName,
          						"EdgeCoverage",null,0);
          				writeEventFileStartTag();

          				if(srcNode != i){
							findPath(srcNode, i,path);
							//if(path.size() > 1)
							//{
							node_iterator = path.iterator();
							srcState = path.get(0);
							node_iterator.next();
							int index =1;
							while(node_iterator.hasNext()){
								transition = (edges[srcState][path.get(index)].
										getTransition());
								if((edges[srcState][path.get(index)]).
										getMarkFlag(transition)==0){
									(edges[srcState][index]).setMarkFlag(
											transition);
									markedEdgeCount++;
								}
								writeStateInfoToFile(srcState,path.get(index),
										transition);
								srcState= path.get(index);
								node_iterator.next();
								index++;
							}
							try{
								transition = (edges[srcState][i]).
									getTransition();
								if((edges[srcState][i]).getMarkFlag(
										transition)==0){
									(edges[srcState][i]).setMarkFlag(transition);
									markedEdgeCount++;
								}
								writeStateInfoToFile(srcState,i, transition);
							}catch(Exception e){
								//e.printStackTrace();
							}
						}
						transition = (edges[i][j]).getTransition();
						if((edges[i][j]).getMarkFlag(transition)!=0){
							transition = (edges[i][j]).getNextTransition();
						}
						writeStateInfoToFile(i,j, transition);
						(edges[i][j]).setMarkFlag(transition);
						markedEdgeCount++;
						writeEventFileEndTag();
						if(log !=null){
							log.close();
						}
						count++;
					}
		        }
          	}

        }
      //  cout <<"All edges have been covered." << endl;
        System.out.println("All edges have been covered. \n");
        return 0;
    }



    /**
     * Marks the back edges from a node
     * @param startnodeid
     * @throws Exception
     *
     */

    void markEdgesFromChild(int startnodeid, Vector<Integer> pathToCurrentNode)
    	throws Exception {

        Collections.sort(statesVisited);
    	
    	if (statesVisited.indexOf(startnodeid)!= -1){
    		return;
    	}

        int noOfElements = statesVisited.size();

        for (int i = noOfElements; i > 0; i--) {

            // If there exists a back edge write that into the send events ---
            // Need to know th usage of iterator
            // file and mark the edge
            int previousValue = statesVisited.get(i - 1).intValue();

            if (edges[startnodeid][previousValue] != null) {
                String transition = (edges[startnodeid][previousValue])
                        .getTransition();
                writeStateInfoToFile(startnodeid, previousValue, transition);
                if((edges[startnodeid][previousValue]).
                		getMarkFlag(transition)==0){
                	(edges[startnodeid][previousValue]).setMarkFlag(transition);
                	markedEdgeCount++;
                }
                // After marking the edge find a path back to the node,
                // startnodeid
                //findPath(previousValue, startnodeid);
                ReturnToCurrentNode(previousValue, pathToCurrentNode);

                // This while loop implemented to make sure that if there is
                // more than one edge present between startnodeid and
                // *nodeInNodeList
                // then all the edges are accounted for
                while (!(edges[startnodeid][previousValue])
                        .areAllTransitionsCovered()) {
                    transition = (edges[startnodeid][previousValue])
                            .getNextTransition();
                    writeStateInfoToFile(startnodeid, previousValue, transition);
                    if((edges[startnodeid][previousValue]).
                    		getMarkFlag(transition)==0){
                    	(edges[startnodeid][previousValue]).
                    	setMarkFlag(transition);
                    	markedEdgeCount++;
      			  	}
                   // findPath(previousValue, startnodeid);
                    ReturnToCurrentNode(previousValue, pathToCurrentNode);
                }
            }
        }
    }

    /**
     * Method to return to current node.
     * @param startnodeid
     * @param pathToCurrentNode
     * @throws Exception
     */
    void ReturnToCurrentNode(int startnodeid, Vector<Integer> pathToCurrentNode)
    	throws Exception{
		Iterator<Integer> ii;
		Iterator<Integer>  nodeInNodeList;
		int previousStateId;
		String transition;
		//int currStateId;

		ii = pathToCurrentNode.listIterator(startnodeid);
		int index =pathToCurrentNode.indexOf(startnodeid);
		 //for ( ;first!=last; first++) if ( *first==value ) break;
		 //   return first;

		if(ii.hasNext()){

			previousStateId = pathToCurrentNode.get(index);
			logger.info("The element of startnodeid  is "+previousStateId);
			logger.info("The index is "+index);

			for (nodeInNodeList = pathToCurrentNode.listIterator(++index);
				nodeInNodeList.hasNext();nodeInNodeList.next(),index++){
				transition = (edges[previousStateId][pathToCurrentNode.
				             get(index)]).getTransition();
				writeStateInfoToFile(previousStateId, pathToCurrentNode.
							get(index), transition);
				previousStateId = pathToCurrentNode.get(index);

			}
		}
	}

}
