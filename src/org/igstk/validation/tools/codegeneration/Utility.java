package org.igstk.validation.tools.codegeneration;

public class Utility {

	public Utility() { }

	public String removeWord(String str, String word) {

		String regex = "\\s*\\b" + word + "\\b\\s*";

		return str.replaceAll(regex, "");
	}

	public String capitalize(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
