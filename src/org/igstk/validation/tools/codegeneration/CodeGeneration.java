package org.igstk.validation.tools.codegeneration;

import java.io.*;
import java.util.HashMap;

import org.igstk.validation.tools.codegeneration.XMLTransform;
import org.igstk.validation.tools.codegeneration.Utility;


public class CodeGeneration {
	
	private final String SCXML_DIR = "xmlFiles/scxmlFiles/";
	
	private final String OUTPUT_DIR = "output/";
	
	private final String TEMPLATE_FILE = "src/org/igstk/validation/tools/codegeneration/ClassTemplate.xsl";
	
	public CodeGeneration() { }

	public void generate(String scxmlFileName) {
		
		 File sourceFile, resultFile, templateFile;
		 HashMap<String,String> params;
		 Utility util = new Utility();
		
		 
		 try {
			 sourceFile = new File(SCXML_DIR + scxmlFileName); 
			 String resultFileName = util.removeWord(sourceFile.getName(), ".xml");
			 resultFile = new File(OUTPUT_DIR + resultFileName + ".cxx"); 
			 templateFile = new File(TEMPLATE_FILE); 
			 
			 params	= new HashMap<String,String>();
			 System.out.println(sourceFile.getName());
			 
			 String className = util.removeWord(sourceFile.getName(), "igstk");
			 
			 className = util.removeWord(className, ".xml");
			 
			 params.put("className", className);
			 params.put("namespace", "igstk");
			 
			 XMLTransform t	= new XMLTransform();
			 
			t.transform(sourceFile, resultFile, templateFile, params);
			 
		 } catch(Exception e){
			 System.out.println("exception thrown");
		 }

		
	}
	

}

