package model.evenHanlder;

import java.io.File;

import controller.Controller;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import model.export.Export;
import model.export.ToCSV;
import model.export.ToXLS;

public class ExportHandler implements Runnable
{

	protected final Export.Type type;
	protected final String URL;
	protected final Controller controller;
	
	public ExportHandler(Export.Type type, String URL, Controller controller)
	{
		this.type = type;
		this.URL = URL;
		this.controller = controller;
	}
		
	@Override
	public void run()
	{
		Platform.runLater(new Runnable()
		{		
			@Override
			public void run()
			{
				handleExportFunction();
			}
		});
		
		 		
	}

	private void handleExportFunction()
	{
		 FileChooser fileChooser = new FileChooser();
		 if(type == Export.Type.XLMS)
		 {
			 fileChooser.getExtensionFilters().addAll(
		               new FileChooser.ExtensionFilter("XLS File", "*.xls")
				 );
			 
		 }
		 else if(type == Export.Type.CSV)
		 {
			 fileChooser.getExtensionFilters().addAll(
		               new FileChooser.ExtensionFilter("Comma Separated File", "*.csv")
				 );

		 }
		 
		 fileChooser.setInitialFileName("scraped");
		 fileChooser.setTitle("Save File");		 
		 File file = fileChooser.showSaveDialog(controller.getStage());
		 
		 if(file != null)
		 {
			 if(type == Export.Type.CSV)
			 {
				 try
				 {
					new ToCSV(file, controller.getDatabase().getAllScrapedData(URL, Type.URL), controller);
				 }
				 catch (Exception e)
				 {
					e.printStackTrace();
				 }
			 }
			 else if(type == Export.Type.XLMS)
			 {
				 try
				 {
					 new ToXLS(file, controller.getDatabase().getAllScrapedData(URL, Type.URL), controller);
				 }
				 catch(Exception e)
				 {
					 e.printStackTrace();
				 }
			 }
		 }
		
	}
	
}
