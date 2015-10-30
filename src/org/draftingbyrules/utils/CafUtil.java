package org.draftingbyrules.utils;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.draftingbyrules.bean.ArgumentNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CafUtil {

	private static int uniqueStatementId = 0;
	private static int uniqueArgumentId = 0;
	
	public static void createCaf(ArrayList<ArgumentNode> argumentNodes, String filename) {
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder;
		try {
			icBuilder = icFactory.newDocumentBuilder();
			Document doc = icBuilder.newDocument();
			Element mainRootElement = doc.createElement("caf");
			mainRootElement.setAttribute("version", "1.3");
			doc.appendChild(mainRootElement);

			Element elMetadata = doc.createElement("metadata");
			mainRootElement.appendChild( elMetadata );

			Element elStatements = doc.createElement("statements");
			mainRootElement.appendChild( elStatements );
			
			Element elArguments = doc.createElement("arguments");
			mainRootElement.appendChild( elArguments );

			// ArgumentNode root = argumentNodes.get(0);
			appendChildren(argumentNodes, doc, elStatements, elArguments, null, null);

			Element elReferences = doc.createElement("references");
			mainRootElement.appendChild( elReferences );

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(new File(filename));
			transformer.transform(source, console);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void appendChildren(ArrayList<ArgumentNode> argumentNodes, Document doc, Element elStatements, Element elArguments, Element elParentPremises, ArgumentNode currentNode) {
		for (ArgumentNode node: argumentNodes) {
			if (node.getParent() == currentNode) {
				Element elStatement = doc.createElement("statement");
				elStatement.setAttribute("id", "id" + ++uniqueStatementId);
				elStatements.appendChild( elStatement );
				Element elDescriptions = doc.createElement("descriptions");
				elStatement.appendChild( elDescriptions );
				Element elDescription = doc.createElement("description");
				elDescription.setAttribute("lang", "en");
				elDescription.setTextContent(node.getText());
				elDescriptions.appendChild( elDescription );
				
				if (elParentPremises != null) {
					Element elPremise = doc.createElement("premise");
					elPremise.setAttribute("statement", "id" + uniqueStatementId);
					elParentPremises.appendChild(elPremise);
				}
				
				Element elArgument = doc.createElement("argument");
				elArgument.setAttribute("id", "arg" + ++uniqueArgumentId);
				//elArguments.appendChild( elArgument );
				
				Element elConclusion = doc.createElement("conclusion");
				elConclusion.setAttribute("statement", "id" + uniqueStatementId);
				elArgument.appendChild(elConclusion);
				
				Element elPremises = doc.createElement("premises");
				elArgument.appendChild(elPremises);
				
				appendChildren(argumentNodes, doc, elStatements, elArguments, elPremises, node);
				if (elPremises.hasChildNodes())
					elArguments.appendChild( elArgument );
			}
		}
	}
	
}
