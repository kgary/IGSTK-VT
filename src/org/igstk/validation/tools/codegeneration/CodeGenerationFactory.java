package org.igstk.validation.tools.codegeneration;

public class CodeGenerationFactory {

	public static CodeGeneration getGenerator(String scxmlFileName){
		return new CodeGeneration(scxmlFileName);
	}
	
}
