package org.igstk.validation.tools.codegeneration;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import org.igstk.validation.tools.codegeneration.XMLTransform;
import org.igstk.validation.tools.codegeneration.Utility;

public class CodeGeneration {

	private final String SCXML_DIR = "xmlFiles/scxmlFiles/";

	private final String OUTPUT_DIR = "output/";

	private final String TEMPLATE_FILE_PATH = "src/org/igstk/validation/tools/codegeneration/xsl/";
	
	private final String NAMESPACE = "igstk";

	private Utility util;
	
	private XMLTransform transformer;

	public CodeGeneration() {
		transformer = XMLTransformFactory.getTransformer();
		util		= UtilityFactory.getUtility();
	}

	/**
	 * Generates a file based off of an SCXML file
	 * @param scxmlFileName
	 */
	public void generate(String scxmlFileName) {

		XPathParser parser = XPathParserFactory.getParser();

		File sourceFile = new File(SCXML_DIR + scxmlFileName);

		String resultFileName = OUTPUT_DIR + util.removeWord(sourceFile.getName(), ".xml");
		
		
		List<String> methods = parser.parse(sourceFile.getAbsolutePath(), "/*/state/transition");
		String[] tmp = util.removeDuplicates( methods.toArray(new String[methods.size()]));
			
		//comma separated sequence of methods to pass to the XSL
		String methodsStr = util.strArrayToSequence(tmp);
		
		//generate the context header file
		generateFile(sourceFile, resultFileName + "Context.h", TEMPLATE_FILE_PATH
				+ "ContextHeaderTemplate.xsl", methodsStr);
		
		//generate the context class file
		generateFile(sourceFile, resultFileName + "Context.cxx", TEMPLATE_FILE_PATH
						+ "ContextClassTemplate.xsl",methodsStr);
		
		//generate the state header file
		generateFile(sourceFile, resultFileName + "State.h", TEMPLATE_FILE_PATH
				+ "StateHeaderTemplate.xsl", methodsStr);
		
		
		//generate the AttemptingToState abstract header file
		generateFile(sourceFile, OUTPUT_DIR + NAMESPACE + "AttemptingToState.h", TEMPLATE_FILE_PATH
				+ "AttemptingToStateHeaderTemplate.xsl", methodsStr);
		
	
		//get all the states
		List<String> states = parser.parse(sourceFile.getAbsolutePath(), "/*/state");
		
		String state,resultStateFileName;
		//generate the concrete state class files
		for(int i = 0; i < states.size(); i++)
		{
			state = states.get(i);
			resultStateFileName = OUTPUT_DIR + NAMESPACE + state + ".h";
			
			generateConcreteStateFile(sourceFile, resultStateFileName, TEMPLATE_FILE_PATH 
					+ "ConcreteStateHeaderTemplate.xsl", state);
		}

	}

	private void generateFile(File sourceFile, String resultFileName,
			String templateFileName, String methods) {

		File resultFile, templateFile;
		HashMap<String, String> params;

		try {
			resultFile = new File(resultFileName);
			templateFile = new File(templateFileName);

			params = new HashMap<String, String>();

			String className = sourceFile.getName();
			className = className.replace(NAMESPACE, "");
			className = util.removeWord(className, ".xml");

			params.put("className", className);
			params.put("namespace", NAMESPACE);
			params.put("methods", methods);

			transformer.transform(sourceFile, resultFile, templateFile, params);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception thrown");
		}
	}
	

	private void generateConcreteStateFile(File sourceFile, String resultFileName,
			String templateFileName, String state) {

		File resultFile, templateFile;
		HashMap<String, String> params;

		try {
			resultFile = new File(resultFileName);
			templateFile = new File(templateFileName);

			params = new HashMap<String, String>();
			
			String className = sourceFile.getName();
			className = className.replace(NAMESPACE, "");
			className = util.removeWord(className, ".xml");

			params.put("className", className);
			params.put("namespace", NAMESPACE);
			params.put("stateName", state);

			transformer.transform(sourceFile, resultFile, templateFile, params);

		} catch (Exception e) {
			System.out.println("exception thrown");
		}
	}


}
