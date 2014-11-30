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

	private Utility util = new Utility();

	public CodeGeneration() {
	}

	public void generate(String scxmlFileName) {

		XPathParser parser = new XPathParser();
		Utility utl = new Utility();
		
		File sourceFile = new File(SCXML_DIR + scxmlFileName);

		String resultFileName = OUTPUT_DIR + util.removeWord(sourceFile.getName(), ".xml");
		
		List<String> states = parser.parse(sourceFile.getAbsolutePath(), "/*/state");
		List<String> methods = parser.parse(sourceFile.getAbsolutePath(), "/*/state/transition");
		String[] tmp = utl.removeDuplicates( methods.toArray(new String[methods.size()]));
			
		//comma separated sequence of methods to pass to the XSL
		String methodsStr = utl.strArrayToSequence(tmp);
		
		//generate the context header file
		generateFile(sourceFile, resultFileName + "Context.h", TEMPLATE_FILE_PATH
				+ "ContextHeaderTemplate.xsl", methodsStr);
		
		//generate the context class file
		generateFile(sourceFile, resultFileName + "Context.cxx", TEMPLATE_FILE_PATH
						+ "ContextClassTemplate.xsl",methodsStr);
		
		//generate the state header file
		generateFile(sourceFile, resultFileName + "State.h", TEMPLATE_FILE_PATH
				+ "StateHeaderTemplate.xsl", methodsStr);
		
		//generate the state class file
		//generateFile(sourceFile, resultFileName + "State.cxx", TEMPLATE_FILE_PATH
			//			+ "ContextClassTemplate.xsl","");
		
	
		
		String state,resultStateFileName;
		//generate the concrete state class files
		for(int i = 0; i < states.size(); i++)
		{
			state = states.get(i);
			resultStateFileName = OUTPUT_DIR + state + ".h";
			
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
			System.out.println(sourceFile.getName());

			String className = util.removeWord(sourceFile.getName(), "igstk");

			className = util.removeWord(className, ".xml");

			params.put("className", className);
			params.put("namespace", "igstk");
			params.put("methods", methods);

			XMLTransform t = new XMLTransform();

			t.transform(sourceFile, resultFile, templateFile, params);

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
			System.out.println(sourceFile.getName());

			String className = util.removeWord(sourceFile.getName(), "igstk");

			className = util.removeWord(className, ".xml");

			params.put("className", className);
			params.put("namespace", "igstk");
			params.put("stateName", state);

			XMLTransform t = new XMLTransform();

			t.transform(sourceFile, resultFile, templateFile, params);

		} catch (Exception e) {
			System.out.println("exception thrown");
		}
	}


}
