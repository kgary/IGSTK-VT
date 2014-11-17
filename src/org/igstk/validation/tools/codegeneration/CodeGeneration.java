package org.igstk.validation.tools.codegeneration;

import java.io.*;
import java.util.HashMap;

import org.igstk.validation.tools.codegeneration.XMLTransform;
import org.igstk.validation.tools.codegeneration.Utility;

public class CodeGeneration {

	private final String SCXML_DIR = "xmlFiles/scxmlFiles/";

	private final String OUTPUT_DIR = "output/";

	private final String TEMPLATE_FILE_PATH = "src/org/igstk/validation/tools/codegeneration/";

	private Utility util = new Utility();

	public CodeGeneration() {
	}

	public void generate(String scxmlFileName) {

		String templates[] = {
				TEMPLATE_FILE_PATH + "ContextHeaderTemplate.xsl",
				TEMPLATE_FILE_PATH + "ContextClassTemplate.xsl",
				TEMPLATE_FILE_PATH + "StateHeaderTemplate.xsl",
				TEMPLATE_FILE_PATH + "StateClassTemplate.xsl" };

		File sourceFile = new File(SCXML_DIR + scxmlFileName);

		String resultFileName = OUTPUT_DIR + util.removeWord(sourceFile.getName(), ".xml");
		
		//generate the context header file
		generateFile(sourceFile, resultFileName + ".h", TEMPLATE_FILE_PATH
				+ "ContextHeaderTemplate.xsl");
		
		//generate the context class file
		generateFile(sourceFile, resultFileName + ".cxx", TEMPLATE_FILE_PATH
						+ "ContextClassTemplate.xsl");
		
		//generate the state header file
		generateFile(sourceFile, resultFileName + "State.h", TEMPLATE_FILE_PATH
				+ "ContextHeaderTemplate.xsl");
		
		//generate the state class file
		generateFile(sourceFile, resultFileName + "State.cxx", TEMPLATE_FILE_PATH
						+ "ContextClassTemplate.xsl");
		
		
		

	}

	private void generateFile(File sourceFile, String resultFileName,
			String templateFileName) {

		File resultFile, templateFile;
		HashMap<String, String> params;

		try {

			resultFile = new File(resultFileName);
			templateFile = new File(templateFileName);

			params = new HashMap<String, String>();
			System.out.println(sourceFile.getName());

			String className = util.removeWord(sourceFile.getName(), "igstk");

			className = util.removeWord(className, ".xml");

			params.put("className", className);
			params.put("namespace", "igstk");

			XMLTransform t = new XMLTransform();

			t.transform(sourceFile, resultFile, templateFile, params);

		} catch (Exception e) {
			System.out.println("exception thrown");
		}
	}

}
