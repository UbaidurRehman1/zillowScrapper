package model.constant;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.downloads.url.URL;


public class Constant
{
	
	
	public static final String getUserPath()
	{
		return System.getProperty("user.home");
	}
	
	public static final File getAppDirectory()
	{
		File file = new File(getUserPath() + "\\Scrapper");
		
		return file;
	}
	
	public static final String getScrapperPath()
	{
		File file = new File(getAppDirectory() + "\\Scrapper.py ");
		if(file.exists())
			return file.getAbsolutePath();
		else return null;
	}
	
	
	public static final void setImageURL(String url)
	{
		try
		{
			File file = new File(getAppDirectory() + "\\imageURL.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			dBuilder = dbFactory.newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = null;

			
			if(file.exists())
			{
			
				Document doc = dBuilder.parse(file);	
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("URL");
				Node nNode = nList.item(0);
				
				nNode.setTextContent(url);
				transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(getAppDirectory() + "\\imageURL.xml"));
				transformer.transform(source, result);
				
			}			
			else
			{
				getAlert(file.getAbsolutePath() + " is not exists. Close Application\nTry to delete Scrapper Folder and then restart application.");
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			getAlert("Error 94");
			
		}

	}
	
	
	public static final String getImageURL()
	{
		
		String url = "";
		
		try
		{
			File file = new File(getAppDirectory() + "\\imageURL.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			dBuilder = dbFactory.newDocumentBuilder();

			
			
			if(file.exists())
			{
			
				Document doc = dBuilder.parse(file);	
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("URL");
				Node nNode = nList.item(0);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) nNode;
					try
					{
						url = element.getTextContent();
						
					}
					catch(Exception exp)
					{
						url = null;
					}
				}
				
			
			}			
			else
			{
				getAlert(file.getAbsolutePath() + " is not exists. Close Application\nTry to delete Scrapper Folder and then restart application.");
			}

		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			getAlert("Error 94");
			
		}

		return url;
	}
	
	public static final int getFolderNumber()
	{

		int number = 0;

		
		try
		{
			File file = new File(getAppDirectory() + "\\folderNumber.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			dBuilder = dbFactory.newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = null;

			
			if(file.exists())
			{
			
				Document doc = dBuilder.parse(file);	
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("folderNumber");
				Node nNode = nList.item(0);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) nNode;
					number = Integer.parseInt(element.getTextContent());
				}
				
				number = number + 1;
				nNode.setTextContent(Integer.toString(number));
				transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(getAppDirectory() + "\\folderNumber.xml"));
				transformer.transform(source, result);
				
			}			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			getAlert("Error 94");
			
		}
		
		
		return number - 1;
	}
	
	public static void getAlert(String message)
	{
		Platform.runLater(new Runnable() {
		    @Override
		    public void run()
		    {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText(message);
				alert.showAndWait();

		    }
		});
	}
	
	public static void getInfo(String message)
	{
		Platform.runLater(new Runnable() {
		    @Override
		    public void run()
		    {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Information");
				alert.setContentText(message);
				alert.showAndWait();

		    }
		});
	}

	
	
	public static void setLabel(String message, Controller controller)
	{
		Platform.runLater(new Runnable()
		{
		    @Override
		    public void run()
		    {
		    	controller.getRootPaneController().getLabel().setText("");
		    	controller.getRootPaneController().getLabel().setText(message);		    	
		    }
		});
	
	}
	
	public static void setCounterLabel(String message, Controller controller)
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				controller.getRootPaneController().getCounterLabel().setText("");
				controller.getRootPaneController().getCounterLabel().setText(message);
			}
		});
	}
	
	/**
	 * this method first empty the combobox and then filled with
	 * the given array 
	 * @param array
	 * @param controller
	 */
	public static void setInComboBox(ObservableList<URL> array, Controller controller)
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				controller.getRootPaneController().getCombobox().getItems().clear();
				controller.getRootPaneController().getCombobox().setItems(array);
			}
		});
	}
	
	
	
	/**
	 * 
	 * @return if prerequistes are not installed
	 */
	public static Boolean appStart()
	{	
		String mongoLocation = "C:\\Program Files\\MongoDB\\Server\\4.0\\bin\\mongod.exe";
		String pythonLocation = Constant.getUserPath() +  "\\AppData\\Local\\Programs\\Python\\Python37-32\\python.exe";
//		String dataPath = Constant.getUserPath() + "\\mongoDB\\db";
//		String command = "start /B call \"C:\\Program Files\\MongoDB\\Server\\4.0\\bin\\mongod.exe\" --dbpath \"" + dataPath + "\"";		
//		File batFile = new File(Constant.getAppDirectory().getAbsolutePath() + "\\runMongoServer.bat");
		File mongo = new File(mongoLocation);
		File python = new File(pythonLocation);
		File pip_bat_file = new File(Constant.getAppDirectory().getAbsolutePath() + "\\" + "pip.bat");
		String pip_install_bs4 = "start call pip install beautifulsoup4";
		
		Boolean decision = true;
		
		if(!mongo.exists() || !python.exists())
		{
			if(!mongo.exists())
			{
				getAlert("\"" + mongo.getAbsolutePath() +  "\" is not exists\nPlease Install MongoDB 4.0 Enterprise Edition\nContact: urehman.bese16seecs@seecs.edu.pk");
				decision = false;						
			}
			else if(!python.exists())
			{
				pythonLocation = Constant.getUserPath() +  "\\AppData\\Local\\Programs\\Python\\Python37\\python.exe";
				python = new File(pythonLocation);
				if(!(python.exists()))
				{
					getAlert("\"" + python.getAbsolutePath() +  "\" is not exists\nPlease Install Python 3.7.0\nContact: urehman.bese16seecs@seecs.edu.pk");
					decision = false;														
				}
			}
		}
		else
		{
			if(!pip_bat_file.exists())
			{
				try
				{
					FileUtils.writeStringToFile(pip_bat_file, pip_install_bs4, "UTF-8");
					@SuppressWarnings("unused")
					Process p = Runtime.getRuntime().exec(pip_bat_file.getAbsolutePath());
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}
			}
			else
			{
				System.out.println("bs4 exists");
			}
						
		}
		
		return decision;
	}
	
	public static File getMongoDir()
	{
		String dataPath = Constant.getUserPath() + "\\mongoDB\\db";
		return new File(dataPath);
	}
	
	/**
	 * 
	 * @param index (we give an index so the given index field show in the combobox)
	 * @param controller
	 */
	public static void showFieldOfComboBoxOnIndex(int index, Controller controller)
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				controller.getRootPaneController().getCombobox().getSelectionModel().clearAndSelect(index);
			}
		});
	}

	/**
	 * 
	 * @param object (this method shows the object which is given the parameter)
	 * @param controller
	 */
	public static void showFieldOfComboBoxOnObject(URL object, Controller controller)
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				controller.getRootPaneController().getCombobox().getSelectionModel().select(object);
			}
		});
	}
}
