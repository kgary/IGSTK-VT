package org.igstk.validation.tools.codegeneration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utility {

	public Utility() {
	}

	public String removeWord(String str, String word) {

		String regex = "\\s*\\b" + word + "\\b\\s*";

		return str.replaceAll(regex, "");
	}

	public String capitalize(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Removes duplicate strings from string array
	 * 
	 * @param arr
	 * @return
	 */
	public String[] removeDuplicates(String[] arr) {

		Set<String> uniqueTmp = new HashSet<String>(Arrays.asList(arr));

		return uniqueTmp.toArray(new String[uniqueTmp.size()]);
	}


	
	/**
	 * Converts a String Array to a xPath Sequence
	 * @param arr
	 * @return
	 */
	public String strArrayToSequence(String[] arr){
		String tmp = "";
		for(int i = 0; i < arr.length; i++){
			
			tmp += arr[i] + ",";
	
			
		}
		return tmp;
	}

}
