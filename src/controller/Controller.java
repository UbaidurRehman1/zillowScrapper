package controller;

import javafx.stage.Stage;
import model.Threads.Pool;
import model.Threads.PoolOfThread;
import model.database.Database;
import model.evenHanlder.ActionListener;
import view.RootPane;

public class Controller
{
	private RootPane rootPaneController;
	private ActionListener actionListener = new ActionListener(this);
	private final Database database = new Database(this);
	private final Stage stage;
	private final PoolOfThread pool;

	public Controller(Stage stage)
	{
		this.stage = stage;
		pool = new Pool();
	}
	
	public RootPane getRootPaneController()
	{
		return rootPaneController;
	}

	public void setRootPaneController(RootPane rootPaneController)
	{
		this.rootPaneController = rootPaneController;
	}
	
	public void doRegisterComponents()
	{
		getRootPaneController().getButton().setOnAction(actionListener);
		getRootPaneController().getCombobox().setOnAction(actionListener);
		getRootPaneController().getRenameButton().setOnAction(actionListener);
		getRootPaneController().getExportCSVMenuItem().setOnAction(actionListener);
		getRootPaneController().getExportXLMSMenuItem().setOnAction(actionListener);
		getRootPaneController().getUploadLogoItem().setOnAction(actionListener);
		getRootPaneController().getAboutMenuItem().setOnAction(actionListener);
		getRootPaneController().getDeleteButton().setOnAction(actionListener);
		getRootPaneController().getCloseMenuItem().setOnAction(actionListener);
		getRootPaneController().getDeleteTableButton().setOnAction(actionListener);
		getRootPaneController().getDeleteLogoItem().setOnAction(actionListener);
	}

	public Database getDatabase() {
		return database;
	}
	

	public Stage getStage() {
		return stage;
	}

	public PoolOfThread getPool() {
		return pool;
	}


}
