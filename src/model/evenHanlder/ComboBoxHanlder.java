package model.evenHanlder;

import controller.Controller;
import javafx.application.Platform;


public class ComboBoxHanlder implements Runnable
{
	
	protected final Controller controller;
	protected final String URI;
	
	public ComboBoxHanlder(Controller controller, String URI)
	{
		this.controller = controller;
		this.URI = URI;
	}
	
	
	@Override
	public void run()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				controller.getRootPaneController().getTable().setItems(controller.getDatabase().getAllScrapedData(URI, getType()));				
			}
		});
	}
	
	private Type getType()
	{
		if(URI.length() > 5)
		{
			if(URI.substring(0, 5).equals("https"))
				return Type.URL;
			else
				return Type.NAME;
		}
		
		return null;
	}

}
