package com;

import controller.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.NewStart.NewStart;
import model.constant.Constant;


public class App extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
			Pane rootPane = loader.load();
			Controller controller = new Controller(primaryStage);
			controller.setRootPaneController(loader.getController());
			
			//new start
			new NewStart(controller);
			controller.doRegisterComponents();
		
			
			//setting gui
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/ico.png")));
			primaryStage.setTitle("Scraper");
			Scene scene = new Scene(rootPane);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
	           public void handle(WindowEvent we)
	           {
	               System.exit(0);
	           }
			}); 
			
			
			//checking app
			if(Constant.appStart())
			{

				controller.getRootPaneController().getButton().setDisable(false);
				controller.getRootPaneController().getUlrField().setEditable(true);
				Constant.setInComboBox(controller.getDatabase().getUniqueBaseURLs(), controller);

				String imageURL = Constant.getImageURL();
				if(!(imageURL == null || imageURL == ""))
				{
					controller.getRootPaneController().getImageView().setImage(new Image(imageURL));					
				}
			}
			
		
			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}

	
	public static void main(String [] args)
	{
		launch(args);
	}
}
