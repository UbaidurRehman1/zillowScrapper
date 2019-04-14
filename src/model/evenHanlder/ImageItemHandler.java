package model.evenHanlder;

import java.io.File;

import controller.Controller;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import model.constant.Constant;

public class ImageItemHandler implements Runnable
{

	final Controller controller;
	
	public ImageItemHandler(Controller controller)
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
				setImage();
			}
		});
	}
	
	public void setImage()
	{
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Select Logo");
		 
		 FileChooser.ExtensionFilter fileExtensions = 
				  new FileChooser.ExtensionFilter(
				    "Images", "*.jpg", "*.jpeg", "*.png", "*.gif");
		 fileChooser.getExtensionFilters().add(fileExtensions);
		 File file = fileChooser.showOpenDialog(controller.getStage());
		 
		 if(file != null)
		 {
			 Constant.setImageURL(file.toURI().toString());
		     Image image = new Image(file.toURI().toString());
		     controller.getRootPaneController().getImageView().setImage(image);
		 }
	}

}
