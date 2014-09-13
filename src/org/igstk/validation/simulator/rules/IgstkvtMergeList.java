/**************************************************************************
 * Program:   Image Guided Surgery Software Validation Toolkit
 * File:      IgstkvtMergeList.java
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

package org.igstk.validation.simulator.rules;

import java.util.ArrayList;

import org.igstk.validation.generator.IgstkvtSendEvent;

/**
 * This is test program to merge "n" number of lists.
 * @author janakiramdandibhotla
 *
 */
public class IgstkvtMergeList {

    /**
     * This is the main method for this class.
     * @param args
     */
    public static void main(String[] args) {

        ArrayList<IgstkvtSendEvent> l1 = new ArrayList<IgstkvtSendEvent>();
        l1.add(new IgstkvtSendEvent("test1", "testId1"));
        l1.add(new IgstkvtSendEvent("test1", "testId1"));

        ArrayList<IgstkvtSendEvent> l2 = new ArrayList<IgstkvtSendEvent>();
        l2.add(new IgstkvtSendEvent("test2", "testId2"));
        l2.add(new IgstkvtSendEvent("test2", "testId2"));

        ArrayList<ArrayList<IgstkvtSendEvent>> masterTempList = 
            new ArrayList<ArrayList<IgstkvtSendEvent>>();

        masterTempList.add(l1);
        masterTempList.add(l2);

        IgstkvtMergeList mergeList = new IgstkvtMergeList();

        ArrayList<ArrayList<IgstkvtSendEvent>> allTempList = mergeList
                .getAllList(masterTempList);

        // Initialization of list pointers.

        System.out.println("The size of the final list is : "
                + allTempList.size());

        for (int i = 0; i < allTempList.size(); i++) {
            for (int j = 0; j < allTempList.get(i).size(); j++) {
                System.out.print("\t" + allTempList.get(i).get(j).toString());
            }
            System.out.print("\n");
        }

    }

    /**
     * This method is a setter for the master list.
     * @param masterList
     * @return
     */
    public ArrayList<ArrayList<IgstkvtSendEvent>> getAllList(
            ArrayList<ArrayList<IgstkvtSendEvent>> masterList) {
        setMasterList(masterList);
        int[] listPointers = new int[getMasterList().size()];
        for (int i = 0; i < listPointers.length; i++) {
            listPointers[i] = 0;
        }
        insert(listTemp, listPointers);
        return allList;
    }

    private ArrayList<ArrayList<IgstkvtSendEvent>> masterList = 
        new ArrayList<ArrayList<IgstkvtSendEvent>>();

    ArrayList<ArrayList<IgstkvtSendEvent>> allSendEvents;

    ArrayList<ArrayList<IgstkvtSendEvent>> allList = 
        new ArrayList<ArrayList<IgstkvtSendEvent>>();

    ArrayList<IgstkvtSendEvent> listTemp = new ArrayList<IgstkvtSendEvent>();

    /**
     * 
     * This method is used to insert an array in the master list.
     * @param list
     * @param listPointers
     */
    private void insert(ArrayList<IgstkvtSendEvent> list, int[] listPointers) {

        ArrayList<IgstkvtSendEvent> tempList;
        int[] tempListPointers;

        int maxListSize = 0;

        for (int i = 0; i < getMasterList().size(); i++) {
            maxListSize += getMasterList().get(i).size();
        }

        if (list.size() == maxListSize) {
            tempList = new ArrayList<IgstkvtSendEvent>();
            tempList.addAll(list);
            allList.add(tempList);
            return;
        }

        for (int i = 0; i < listPointers.length; i++) {

            if (listPointers[i] <= (getMasterList().get(i).size() - 1)) {
                tempList = new ArrayList<IgstkvtSendEvent>();
                tempList.addAll(list);
                tempList.add(getMasterList().get(i).get(listPointers[i]));

                tempListPointers = new int[listPointers.length];
                for (int j = 0; j < listPointers.length; j++) {
                    tempListPointers[j] = listPointers[j];
                }

                tempListPointers[i] = listPointers[i] + 1;

                insert(tempList, tempListPointers);
            }

        }
    }

    
    /**
     * This is the setter method for master list.
     * @param masterList
     */
    private void setMasterList(ArrayList<ArrayList<IgstkvtSendEvent>> 
    								masterList) {
        this.masterList = masterList;
    }

    /**
     * This is the getter method for the master list.
     * @return
     */
    private ArrayList<ArrayList<IgstkvtSendEvent>> getMasterList() {
        return masterList;
    }
}
