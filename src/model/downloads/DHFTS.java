package model.downloads;

import java.io.File;
import java.time.LocalDate;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import model.constant.Constant;
import model.downloads.url.URL;
import model.evenHanlder.Type;
import model.table.PropertyInfo;

//this first download all the pages of url 1 to 7 or any things:
//then extracting anchor tags from all these pages
//then from all anchor tags, download again pages of a tags
//Pattern of Directory
//....Scrapper
//......dateTime
//........Pages
//........tags
public class DHFTS implements Runnable
{
	
	private File fullSearchDir;
	private File tagsSearchDir;
	private String url;
	private int index;
	private final Vector<String> tagCollections = new Vector<String>();
	private final ObservableList<PropertyInfo> list = FXCollections.observableArrayList();
	private final Controller controller;
	private final String baseUrl;
	private final Boolean url_present_in_database;
	private int sizeOfTable = 0;
	private int lastIndexOfTable;
	
	
	@Override
	public void run()
	{
		File file = new File(Constant.getAppDirectory() + "/" + LocalDate.now() +  Constant.getFolderNumber());
		if(file.mkdir())
		{
			File fullSearchDir = new File(file.getAbsolutePath() + "/fullSearch");
			File tagSearchDir = new File(file.getAbsolutePath() + "/tagSearch");
			
			if(fullSearchDir.mkdir() & tagSearchDir.mkdir())
			{
				Constant.setLabel(fullSearchDir.getAbsolutePath() + " is created \n" + tagSearchDir.getAbsolutePath() + " is created", controller);
				setFullSearchDir(fullSearchDir);
				setTagsSearchDir(tagSearchDir);
			}
			else
			{
				Constant.getAlert("Cannot be made " + fullSearchDir.getAbsolutePath() + " or " + tagSearchDir.getAbsolutePath());
				System.exit(0);
			}
		}
		else
		{
			Constant.getAlert("Cannot be made " + fullSearchDir.getAbsolutePath());
			System.exit(0);
		}
		
		//downloading the search files
		Downloader downloader = new Downloader(this);
		final ExecutorService threadPool = Executors.newFixedThreadPool(1);
		threadPool.execute(downloader);
		controller.getPool().addThreadPool(threadPool);
		threadPool.shutdown();
						
	}
	

	//constructor
	public DHFTS(String url, Controller controller)
	{
		controller.getRootPaneController().getProgressIndicator().setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		baseUrl = url;
		this.controller = controller;
		setUrl(url);
		setIndex(1);
		
		tagCollections.removeAllElements();
		getList().clear();
		
		controller.getRootPaneController().getButton().setDisable(true);
		controller.getRootPaneController().getRenameButton().setDisable(true);
		controller.getRootPaneController().getCombobox().setDisable(true);
		controller.getRootPaneController().getUlrField().setDisable(true);
		controller.getRootPaneController().getRenameField().setDisable(true);
		
		//emptying the table
		setTableEmpty();
		
		if(controller.getDatabase().isURLPresentInDataBase(baseUrl))
		{
			//populating the table with base url data
			populateTable();
			url_present_in_database = true;
		}
		else
		{
			url_present_in_database = false;			
		}
		
		if(getUrl_present_in_database())
		{
			int size = getController().getRootPaneController().getTable().getItems().size();
			setLastIndexOfTable(getController().getRootPaneController().getTable().getItems().get(size - 1).getId());
		}

		Constant.setCounterLabel("", controller);
		
	}

	
	
	public File getFullSearchDir() {
		return fullSearchDir;
	}

	public void setFullSearchDir(File fullSearchDir) {
		this.fullSearchDir = fullSearchDir;
	}

	public File getTagsSearchDir() {
		return tagsSearchDir;
	}

	public void setTagsSearchDir(File tagsSearchDir) {
		this.tagsSearchDir = tagsSearchDir;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public synchronized int getIndex() {
		return index;
	}

	public synchronized void setIndex(int index) {
		this.index = index;
	}

	public Vector<String> getTagCollections() {
		return tagCollections;
	}



	public Controller getController() {
		return controller;
	}



	public ObservableList<PropertyInfo> getList() {
		return list;
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	public Boolean getUrl_present_in_database() {
		return url_present_in_database;
	}

	public int getSizeOfTable() {
		return sizeOfTable;
	}

	public void setSizeOfTable(int sizeOfTable) {
		this.sizeOfTable = sizeOfTable;
	}
	
	private void populateTable()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				
				System.out.println("Table Populating start");
				
				//getting list of all property info of given uri
				ObservableList<PropertyInfo> list = getController().getDatabase().getAllScrapedData(getController().getDatabase().getURL(getBaseUrl()).toString(), getType());
				
				//setting the same info in the list(DHFTS)
				for(PropertyInfo info: list)
				{
					System.out.println(info);
					getList().add(info);
				}

				
				//setting the info in table
				getController().getRootPaneController().getTable().setItems(list);
				
				
				//storing size of the table in DHFTS
				setSizeOfTable(getController().getRootPaneController().getTable().getItems().size());
				
				System.out.println("Size of the table: " + getSizeOfTable());
				
			}
		});
	}
	
	/**
	 * the getType method was induce due 
	 * to [becuase in early version of this app
	 * the type of combobox was String
	 * so we need to find if it was a URL or name
	 * so it is the reason
	 * @return
	 */
	private Type getType()
	{
		
		URL url = null;
		url = getController().getDatabase().getURL(getBaseUrl());
		String URI = url.toString();
		
		
		if(URI.length() > 5)
		{
			if(URI.substring(0, 5).equals("https"))
				return Type.URL;
			else
				return Type.NAME;
		}
		
		return null;
	}
	
	private void setTableEmpty()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				getController().getRootPaneController().getTable().setItems(null);
			}
		});
	}


	public int getLastIndexOfTable() {
		return lastIndexOfTable;
	}


	public void setLastIndexOfTable(int lastIndexOfTable) {
		this.lastIndexOfTable = lastIndexOfTable;
	}




}
