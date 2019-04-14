package model.NewStart;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import controller.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.constant.Constant;

/**
 * when our application starts for the first time:
 * 		0. if Scrapper is already present in the directory then it skip all further operations
 * 		1. Create the Scraper folder in the user directory
 * 		2. Create a .py file haveing some code in it.
 * @author ubaid
 *
 */
public class NewStart
{
	
	public NewStart(Controller controller) throws IOException
	{
		if(!(Constant.getAppDirectory().exists()))
		{
			Constant.setLabel(Constant.getAppDirectory().getAbsolutePath() + " not exist, creating directory", controller);
			
			if(Constant.getAppDirectory().mkdir())
			{
				try
				{
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = null;
					dBuilder = dbFactory.newDocumentBuilder();
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = null;
					
					
					Document doc = dBuilder.newDocument();
					Element rootElement = doc.createElement("folderNumber");
					rootElement.appendChild(doc.createTextNode("1"));
					doc.appendChild(rootElement);
					transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(Constant.getAppDirectory() + "\\folderNumber.xml"));
					transformer.transform(source, result);

					
					Document doc2 = dBuilder.newDocument();
					Element root = doc2.createElement("URL");
					rootElement.appendChild(doc.createTextNode(""));
					doc2.appendChild(root);
					transformer = transformerFactory.newTransformer();
					DOMSource source2 = new DOMSource(doc2);
					StreamResult result2 = new StreamResult(new File(Constant.getAppDirectory() + "\\imageURL.xml"));
					transformer.transform(source2, result2);
					
					
					File srcFile = new File("py\\Main.py");
					if(srcFile.exists())
					{
						File destFile = new File(Constant.getAppDirectory() + "\\Scrapper.py");
						FileUtils.copyFile(srcFile, destFile);
						Constant.setLabel("Scrapper.py Created", controller);
						
					}
					else
					{
						throw new FileNotFoundException(srcFile.getAbsolutePath() + " not found");
					}
				

				}
				catch(Exception exp)
				{
					exp.printStackTrace();
					Constant.getAlert(exp.getMessage());
				}
				
				
			}
						
		}
		
//		if(!(Constant.getMongoDir().exists()))
//		{
//			if(Constant.getMongoDir().mkdirs())
//				Constant.setLabel(Constant.getMongoDir().getAbsolutePath() + " Created", controller);
//		}
//		else
//		{
//			Constant.setLabel(Constant.getMongoDir().getAbsolutePath() + " exists", controller);			
//		}

	}
}
