package org.igstk.validation.tools.codegeneration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XPathParser {

	public XPathParser() {
	}

	public List<String> parse(String XMLFilePath, String XPathExpression) {

		List<String> attrList = new ArrayList<String>();
		

		
		
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(XMLFilePath);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(XPathExpression);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			
			for (int i = 0; i < nodes.getLength(); i++) {
				NamedNodeMap attributes = nodes.item(i).getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					attrList.add(attributes.item(j).getNodeValue().replace("Input", ""));
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
		}

		return attrList;

	}

}
