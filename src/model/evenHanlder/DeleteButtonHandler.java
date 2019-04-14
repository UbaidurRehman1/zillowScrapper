package model.evenHanlder;

import controller.Controller;
import javafx.application.Platform;
import model.constant.Constant;
import model.table.PropertyInfo;

public class DeleteButtonHandler implements Runnable
{
	final Controller controller;
	
	public DeleteButtonHandler(Controller controller)
	{
		this.controller = controller;
	}

	@Override
	public void run()
	{
		try
		{
			PropertyInfo propertyInfo = controller.getRootPaneController().getTable().getSelectionModel().getSelectedItem();
			String base_url = propertyInfo.getBaseURL().getUrl();
			String ad_url	= propertyInfo.getUrl().getUrl();
			
			controller.getDatabase().deleteRecord(base_url, ad_url);
			Platform.runLater(new Runnable()
			{
				
				@Override
				public void run()
				{
					controller.getRootPaneController().getTable().setItems(controller.getDatabase().getAllScrapedData(base_url, Type.URL));				
				}
			});
			
		}
		catch(Exception exp)
		{
			Constant.getAlert("Select a record");
		}
	}
}
