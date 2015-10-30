package org.draftingbyrules.utils;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.draftingbyrules.bean.Fact;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AkomaNtosoUtil {

	public static void updateIndictment(String filename, ArrayList<Fact> facts) {
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = parser.parse(new java.io.File(filename));
			String expression = "//*[@title]";
			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String title = node.getAttributes().getNamedItem("title").getNodeValue();
				String value = null;
				for (Fact f: facts)
					if (f.getFactName().equalsIgnoreCase(title))
						value = f.getFactValue();
				if (value != null)
					node.setTextContent(value);
				else
					node.setTextContent(UiUtil.readValue(title + ": "));
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
