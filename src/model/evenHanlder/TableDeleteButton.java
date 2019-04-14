package model.evenHanlder;



import controller.Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.constant.Constant;

public class TableDeleteButton implements Runnable
{

	final Controller controller;
	
	public TableDeleteButton(Controller controller)
	{
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
				try
				{
					if(doDeleteTable())
						Constant.setLabel("Table deleted successfully", controller);
				} 
				catch (Exception e)
				{
					Constant.getAlert("Select A Table");
				}
			}
		});
	}
	
	
	public Boolean doDeleteTable() throws Exception
	{
		
		String base_url = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl();
		String name = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().toString();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Are You sure to delete this table " + "\n" + name);
		alert.setHeaderText("Confirm");
		alert.showAndWait();
		
		Boolean decision = alert.getResult().getButtonData().isCancelButton();
		
		if(!decision)
		{
			if(controller.getDatabase().deleteTable(base_url))
			{
				Constant.getInfo(name + "\n" + "Successfully deleted");
				Constant.setInComboBox(controller.getDatabase().getUniqueBaseURLs(), controller);
				
				try
				{
					controller.getRootPaneController().getCombobox().getSelectionModel().select(0);
					String URL = controller.getRootPaneController().getCombobox().getSelectionModel().getSelectedItem().getUrl();
					controller.getRootPaneController().getTable().setItems(controller.getDatabase().getAllScrapedData(URL, Type.URL));
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
					return false;
				}
				
				return true;
				
			}
			
		}
		
		return false;
	}
	
}
