package org.igstk.validation.tools.codegeneration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLTransform {


	public XMLTransform() { }


	public void transform(File sourceFile, File resultFile, File templateFile,
			HashMap<String, String> paramMap) {
		try {

			TransformerFactory f = TransformerFactory.newInstance();

			Transformer t = f.newTransformer(new StreamSource(templateFile));
			
			addParams(paramMap, t);
			
			Source s = new StreamSource(sourceFile);
			Result r = new StreamResult(resultFile);

			t.transform(s, r);

		} catch (TransformerConfigurationException e) {
			System.out.println(e.toString());
		} catch (TransformerException e) {
			System.out.println(e.toString());
		}

	}

	public void addParams(Map<String, String> params, Transformer trans) {
		Iterator<Map.Entry<String, String>> entries = params.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			trans.setParameter(entry.getKey(), entry.getValue());
		}
	}

}
