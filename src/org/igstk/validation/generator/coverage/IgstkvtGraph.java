/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtGraph.java
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

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger; 



/**
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtGraph {

    Vector<IgstkvtVertex> vertices; // Dynamic array of nodes
    Iterator<IgstkvtVertex> vertex_iterator;
    ArrayList<Integer> statesVisited;
    HashSet<Integer> parentList;
    IgstkvtTransition edges[][];
    private String filename;
    private String sendEventsDirectory;
    Set<Integer> stateList= new HashSet<Integer>();

    int vertexNo; // #of vertices in the graph / states in the state machine
    // (SM)
    String initialstate;
    int totalEdgeCount; // Total number of edges in the graph / inputs in the SM
    FileWriter log;

    static Logger logger = Logger.getLogger(IgstkvtGraph.class);

    /**
     *
     *
     */
    public IgstkvtGraph() {
        totalEdgeCount = 0;
        vertices = new Vector<IgstkvtVertex>();
        statesVisited = new ArrayList<Integer>();
        parentList = new HashSet<Integer>();
    }

    /**
     * @param state
     */

    void initializeNodeArray(){
    	vertices.clear();
    }

    /**
     * Setter for initial Vertex.
     * @param state
     */
    void setInitialVertex(String state) {
        initialstate = state;
    }

    /**
     * Adds a node to the vector called, vertices.
     * @param pVertex
     */

    void addNode(IgstkvtVertex pVertex) {
        vertices.add(pVertex);
    }

    /**
     * Adds an edge to the two-dimensional adjacency matrix called, edges. If an
     * edge is already present this implies there are more than one transitions
     * between those two nodes.Therefore a new entry is added to the transition
     * list.
     * @param row
     * @param col
     * @param transition
     */

    void addEdge(int row, int col, String transition)throws 
    	ArrayIndexOutOfBoundsException {
        if (edges[row][col] != null) {
            edges[row][col].addTransition(transition);
        } else {
            edges[row][col] = new IgstkvtTransition(transition);
        }
        ++totalEdgeCount;
    }

    /**
     * Create a two dimensional edge array where the number of rows and column
     * equal the number of nodes in the IgstkvtGraph(or vertexNo). Initialize
     * the edge to null pointer.
     * @param size
     */

    void initializeEdgeArray(int size) {
        vertexNo = size;
        edges = new IgstkvtTransition[size][size];
    }

    /**
     * Finds an unvisited node in the graph.
     * @param startNode
     */

    int findANodeInGraph(int startNode) {
        for (int node = startNode; node < vertexNo; node++) {
            if (!statesVisited.contains(node)) {
                return node;
            }
        }
        return 0; // ---- Should change this appropriately
    }

    /**
     * Finds an already visited parent node given a node,Dst. If there are no
     * parent nodes which have been visited then return -1.
     * @param Dst
     * @return
     */

//    int findParent(int Dst) {
 //       return findParent(Dst, 0);
  //  }

    /**
     * Overloading of the previous method wit a startNode param.
     * @param Dst
     * @param startNode
     * @return
     */
    int findVisitedParent(int Dst, int startNode) {
        for (int node = startNode; node < vertexNo; node++) {
            if ((node != Dst) && edges[node][Dst] != null
                    && statesVisited.contains(node)) {
                return node;
            }
        }
        return -1;
    }

    /**
     * Find an unvisited child node for a node in the IgstkvtGraph.
     * @param src
     * @return
     */

    /**
     *Method to find Unvisited Child.
     * @param src
     * @return
     */
    int findUnvisitedChild(int src) {
        for (int node = 0; node < vertexNo; node++) {
            if (src != node && edges[src][node] != null
            		&& !statesVisited.contains(node)) {
                return node;
            }
        }
        return -1;
    }

 /**
 * Find a visited child node for a node in the Graph
 * @param src
 * @param startNode
 * @return
 */
    int findVisitedChild(int src,int startNode) {
	    for (int node = startNode;node < vertexNo; node++){
	    	if(src!=node && edges[src][node] != null){
	    		return node;
	    	}
	    }
	    return -1;
    }

    /**
     * Given the name of a node determine its index in the vector,vertices.
     * @param vertexName
     * @return
     */

    int findNode(String vertexName) {
        int count = 0;
        logger.info("The size of vertices is "+vertices.size());

        vertex_iterator = vertices.iterator();
        int ind =0;
        while (vertex_iterator.hasNext()) {
            if (vertexName.equals(vertices.get(ind).getName())){
                return count;
            }else{
                count++;
            }
            ind++;
        }
        return -1;
    }

    /**
     * Find a path between two nodes.
     * @param Src
     * @param Dst
     * @return
     * @throws Exception
     */

    String findVertexName(int node){
    	return vertices.get(node).getName();
    }


    /**
     * Finds Path.
     * @param Src
     * @param Dst
     * @param path
     * @throws Exception
     */
    void findPath(int Src, int Dst, Vector<Integer> path) throws Exception {
    	Vector<Integer> adjVertices = new Vector<Integer>();
    	Iterator<Integer> adj_vertex_iterator,stateList_iterator;
    	stateList.add(Src);
    	logger.info("The size of the stateList is "+stateList.size());
    	path.addElement(Src);
    	adjVertices = findChildren(Src);
    	//stateList_iterator = stateList.iterator();
    	Integer[] stateList_arr = new Integer[stateList.size()];

    	stateList_arr =	stateList.toArray(stateList_arr);
    	for (int i=0;i<stateList_arr.length;i++){
    		System.out.println("Index " +i+" : "+ stateList_arr[i].intValue());
    	}
    	int index =0,sindex =0;
    	for(adj_vertex_iterator=adjVertices.iterator();
    		adj_vertex_iterator.hasNext();
    		adj_vertex_iterator.next(),index++){
    		if(adjVertices.get(index).intValue()==Dst){
    			return;
    		}
    		for(int i =0;i<stateList_arr.length;i++){
    			if(stateList_arr[i].intValue()
    					==adjVertices.get(index).intValue()){
    				if(i+1 != stateList_arr.length){
    					sindex =1;
    				}
    			}

    		}
    		if (sindex==1){
    			continue;
    		}
    		while (path.lastElement().intValue() != Src){
    			path.removeElementAt(path.size()-1);
    		}

    		findPath(adjVertices.get(index).intValue(), Dst, path);
    	}
    }





    /**
     * Write to the send events file with all the meta information.
     * @param previousStateId
     * @param currStateId
     * @param transition
     * @throws Exception
     */
    void writeStateInfoToFile(int previousStateId, int currStateId,
            String transition) throws Exception {

        log.write("\t<send event=\"" + transition + "\"/>\n\n");
    }

    /**
     * write the header information for the xml file.
     * @param componentName
     * @param coverageType
     * @throws Exception
     */

    void writeEventFileHeaderInfo(String componentName, String coverageType,
    		StringBuffer heuristics_text, int includeSelfTrans)
        throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

        Date date = new Date(System.currentTimeMillis());

        log.write("<?xml version=\"1.0\"?> \n");
        log.write("  <igstk> \n");
        log.write("    <metadata> \n");
        log.write("\t<factory type=\"" + coverageType + "\"");
       // log.write("Component Name: \"" + componentName + "\"\n");
        log.write("\tTime=\"" + sdf.format(date).toString() + "\">\n");
        if (includeSelfTrans ==0){
        	log.write("\t      <factoryparam name=\"includeselftransitions\" "
        			+ "value=\"false\" /> \n");
        }else{
        	log.write("\t      <factoryparam name=\"includeselftransitions\" "
        			+ "value=\"true\" /> \n");
        }
        log.write("\t</factory> \n");
        log.write("\t<component type=\""+ componentName +"\"/> \n");
        if (coverageType.equals("pathcoverage")){
        	WriteHeuristicsData(heuristics_text);
        }
        log.write("    </metadata> \n\n");
    }

    /**
     * Writes Heuristic Data.
     * @param heuristics_text
     * @throws Exception
     */
    void WriteHeuristicsData(StringBuffer heuristics_text) throws Exception{
    	log.write(heuristics_text + "\n\n");
    }


    /**
     * writes Event files start tag.
     * @throws Exception
     */
    void writeEventFileStartTag() throws Exception{
    	log.write("<events> \n\n");
    }

    /**
     * This method writes the end tag.
     * @throws Exception
     */
    void writeEventFileEndTag() throws Exception {
        log.write("</events> \n");
        log.write("</igstk>\n");
    }

    /**
     * Finds Children.
     * @param start
     * @return
     */
    Vector<Integer> findChildren(int start){
		Vector<Integer> adjVertices = new Vector<Integer>();
			// Dynamic array of nodes
		if(start < 0 || start >= vertexNo){
			return adjVertices;
		}
		for (int node = 0;node < vertexNo; node++){
			if(start!=node && edges[start][node]!=null){
				adjVertices.addElement(node);
			}

		}
	    return adjVertices;
    }


    /**
     * Find states related to by incoming links.
     * @param start
     * @return
     */
    Vector<Integer> findParents(int start){
	    Vector<Integer> adjVertices =new Vector<Integer>();
	    	// Dynamic array of nodes
		if(start < 0 || start >= vertexNo){
			return adjVertices;
		}


		for (int node = 0;node < vertexNo; node++){
			if(start!=node && edges[node][start] !=null){
				adjVertices.addElement(node);
			}

		}
	    return adjVertices;
    }

    /**
     * Reset all transitions.
     */
    void resetAllTransitionMarkings(){
		for (int row = 0; row < vertexNo; row++){
			for (int col = 0; col < vertexNo; col++){
				if(edges[row][col]!=null){
					(edges[row][col]).resetAllTransitions();
				}
			}
		}
    }


    /**
     * Reset all transitions.
     * @param startnode
     * @param targetnode
     * @param trans
     */
    void setEdgeMarking(int startnode,int targetnode, String trans){
    	if(edges[startnode][targetnode]!=null){
    		(edges[startnode][targetnode]).setMarkFlag(trans);
    	}
    }

	/**
	 * Gets the vertex number.
	 * @return
	 */
	int GraphVertexCount(){
		return vertexNo;
	}

	/**
	 * Method to find out if the initial state
	 * is reachable.
	 * @param startnode
	 * @return
	 */
	int IsInitalStateReachable(int startnode){
		for (int i = 0; i < vertexNo; i++){
			if(edges[i][startnode]!=null){
				return 1;
			}
		}
		return 0;
	}


    /**
     * This method sets the file name property.
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter for fileName.
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter for send Events Directory.
     * @param sendEventsDirectory
     */
    public void setSendEventsDirectory(String sendEventsDirectory) {
        this.sendEventsDirectory = sendEventsDirectory;
    }

    /**
     * Getter for send events directory.
     * @return
     */
    public String getSendEventsDirectory() {
        return sendEventsDirectory;
    }

}
