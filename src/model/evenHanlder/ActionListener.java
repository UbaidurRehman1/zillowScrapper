package model.evenHanlder;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.constant.Constant;
import model.downloads.DHFTS;
import model.export.Export;



public class ActionListener implements EventHandler<ActionEvent>
{

	Controller controller;
	
	
	public ActionListener(Controller controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void handle(ActionEvent event)
	{
		//when we click on start button
		if(event.getSource().equals(controller.getRootPaneController().getButton()))
		{
			try
			{
				String url = controller.getRootPaneController().getUlrField().getText();

				if(url.length() > 0)
				{
					if(url.contains("https://www.zillow.com/homes/"))
					{
						//there are some steps which are to be followed:
						//when we click on this button: 
						//then this url goes to a class which names: dhfts [Download html fies type Search]
						controller.getRootPaneController().getLabel().setText("Scraping " + "........................................................");
						DHFTS dhfts = new DHFTS(url, controller);
						ExecutorService threadPool = Executors.newFixedThreadPool(1);
						threadPool.execute(dhfts);
						
						//------
						controller.getPool().addThreadPool(threadPool);
						
					}
					else
					{
						Constant.getAlert("Your URL seems invalid, Enter correct URL");
					}
				}
				else
				{
					Constant.getAlert("Enter something in the URL Field");
				}
				
				
			}
			catch(Exception exp)
			{
				Constant.getAlert("There is unknown error in ActionListener Line 60");
			}
			
		}
		else if(event.getSource().equals(controller.getRootPaneController().getCombobox()))
		{
			//extracting URI from the combobox
			try
			{
				String uri = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl().trim();
				controller.getRootPaneController().getUlrField().setText(controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl());
				ExecutorService threadPool = Executors.newFixedThreadPool(1);
				threadPool.execute(new ComboBoxHanlder(controller, uri));
				
				//--------
				controller.getPool().addThreadPool(threadPool);
			}
			catch(Exception exp)
			{
				System.out.println("no problem");
			}
		}
		else if(event.getSource().equals(controller.getRootPaneController().getRenameButton()))
		{
			String name = controller.getRootPaneController().getRenameField().getText();
			try
			{
				if(name.length() > 5)
				{
					ExecutorService threadPool = Executors.newFixedThreadPool(1);	
					String uri = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl().trim();
					int index = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedIndex();
					threadPool.execute(new RenameButtonHanlder(controller, uri, name, index));
				
					//-------
					controller.getPool().addThreadPool(threadPool);
				}
				else
				{
					Constant.getAlert("Enter name consisting more than 5 alphanumerics");
				}
				
			}
			catch(Exception exp)
			{
				Constant.getAlert("Select a data from combobox");
			}
			
			
		}
		else if(event.getSource().equals(controller.getRootPaneController().getExportCSVMenuItem()))
		{
			try
			{
				
				String URL = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl();
				if(!(URL.equals(null)))
				{
					ExecutorService threadPool = Executors.newFixedThreadPool(1);
					threadPool.execute(new ExportHandler(Export.Type.CSV, URL, controller));
					
					//-------
					controller.getPool().addThreadPool(threadPool);
				}
				else
				{
					Constant.getAlert("Select Data in the Combobox");
				}
			}
			catch(Exception exp)
			{
				Constant.getAlert("Select A Table");
			}	
		}
		else if(event.getSource().equals(controller.getRootPaneController().getExportXLMSMenuItem()))
		{
			try
			{
				
				String URL = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl();
				if(!(URL.equals(null)))
				{
					ExecutorService threadPool = Executors.newFixedThreadPool(1);
					threadPool.execute(new ExportHandler(Export.Type.XLMS, URL, controller));
					threadPool.shutdown();
					
					//-------
					controller.getPool().addThreadPool(threadPool);
				}
				else
				{
					Constant.getAlert("Select Data in the Combobox");
				}
			}
			catch(Exception exp)
			{
				Constant.getAlert("Unknown Error");
			}	
		}
		else if(event.getSource().equals(controller.getRootPaneController().getUploadLogoItem()))
		{
			try
			{
				ExecutorService threadPool = Executors.newFixedThreadPool(1);
				
				threadPool.execute(new ImageItemHandler(controller));
				threadPool.shutdown();
				
				//-------
				controller.getPool().addThreadPool(threadPool);

				
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
		}
		else if(event.getSource().equals(controller.getRootPaneController().getAboutMenuItem()))
		{
			try
			{
				Constant.getInfo("Scrapper\nDeveloped by: urehman.Inc\nVersion:1.0.3\nContact: urehman.bese16seecs@seecs.edu.pk");
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
		}
		else if(event.getSource().equals(controller.getRootPaneController().getDeleteButton()))
		{
			try
			{
				ExecutorService threadPool = Executors.newFixedThreadPool(1);
				threadPool.execute(new DeleteButtonHandler(controller));
				threadPool.shutdown();
				
			}
			catch(Exception exp)
			{
				Constant.getAlert("There is some unkown  error");
			}
		}
		else if(event.getSource().equals(controller.getRootPaneController().getCloseMenuItem()))
		{
			System.exit(0);
		}
		else if(event.getSource().equals(controller.getRootPaneController().getDeleteTableButton()))
		{
			try
			{
				ExecutorService threadPool = Executors.newFixedThreadPool(1);
				threadPool.execute(new TableDeleteButton(controller));
				threadPool.shutdown();
				
			}
			catch(Exception exp)
			{
				Constant.getAlert("There is some unkown  error");
			}
		}
		else if(event.getSource().equals(controller.getRootPaneController().getDeleteLogoItem()))
		{
			try
			{
				ExecutorService threadPool = Executors.newFixedThreadPool(1);
				threadPool.execute(new DeleteLogoHandler(controller));
				threadPool.shutdown();
			}
			catch(Exception exp)
			{
				Constant.getAlert("There is some unkown  error");
			}
			
		}


	}

}
