package model.downloads;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javafx.application.Platform;
import model.constant.Constant;


public class Downloader implements Runnable
{
	private DHFTS dhfts;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	ExecutorService scrapperPool = Executors.newCachedThreadPool();
	

	int counter = 1;
	
	
	public Downloader(DHFTS dhfts)
	{
		
		setDhfts(dhfts);
		getDhfts().setIndex(1);
		
		//--------
		getDhfts().getController().getPool().addThreadPool(scrapperPool);
		getDhfts().getController().getPool().addThreadPool(threadPool);
	}

	public DHFTS getDhfts()
	{
		return dhfts;
	}

	public void setDhfts(DHFTS dhfts)
	{
		this.dhfts = dhfts;
	}

	@Override
	public void run()
	{
		//this cmd() loop through till all the files (pages download)
		cmd();
		System.out.println("cmd() loop ended");
		Constant.setLabel("Files Donwloaded completed. Now start downloading Ads file from each file" + "/n Stay Calm", getDhfts().getController());

		//there we will download all the tags
		//: Step#1: first search all the SearchFiles
		//: Step#2: To each search file, run another separate thread
		File fullSearchDir = getDhfts().getFullSearchDir();
		File[] hFiles = fullSearchDir.listFiles();
		for(int i = 0; i < hFiles.length; i++)
		{
			TagScrapper scrapper = new TagScrapper(getDhfts(), hFiles[i]);
			threadPool.execute(scrapper);
		}

		threadPool.shutdown();			
		
		
		//this loop determine if the tagScraper done their work or not
		while(true)
		{
			System.out.println(" is thread pool in line 58 of Donwloader terminated: " + threadPool.isTerminated());

			
			if(threadPool.isTerminated())
			{
				//when downloading all the search files are done then 
				//calling downloadTags, it create its own pool and execute TagDownloader
				//which download all the tags
				try
				{
					//from this line, all the tags are stored in the list (in DHFTS)
					//if the base url in present in the database, then calling refineAdurls
					//so we download only those urls which are not present in the database
					if(getDhfts().getUrl_present_in_database())
						refineAdURLs();

					downloadTags();
				}
				catch (InterruptedException e)
				{
					Constant.getAlert(Arrays.toString(e.getStackTrace()));
				}
				
				break;
			}
			else
			{
				try
				{
					Thread.sleep(100);
					Constant.setLabel("Extracting hyperlinks from the Files " + "/n Stay Calm", getDhfts().getController());

					System.out.println("Extracting hyperlinks from the Files");
					continue;
				}
				catch (InterruptedException e)
				{
					Constant.getAlert(Arrays.toString(e.getStackTrace()));
				}
			}
		}

	}
	
	/**
	 * this method first download  all the tags by executing the TagDonwloader
	 * and then parsing information from all the tags by executing Scrapper
	 * @throws InterruptedException
	 */
	public void downloadTags() throws InterruptedException
	{
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		
		//--------------------
		getDhfts().getController().getPool().addThreadPool(threadPool);
		
		
		getDhfts().setIndex(1);
		
		//now executing downloadTag class which download all the 
		TagDonwloader downloader = new TagDonwloader(getDhfts());
		threadPool.execute(downloader);
		threadPool.shutdown();		

		System.out.println("This thread " + Thread.currentThread().getName() + " going to sleep for 1 minute");
		
		Thread.sleep(60000);
		
		for(int i = 0; i < getDhfts().getTagCollections().size(); i++)
		{
			String filePath = getDhfts().getTagsSearchDir().getAbsolutePath() + "\\" + (getDhfts().getUrl_present_in_database() ? (getDhfts().getLastIndexOfTable() + (i + 1)) : (i + 1)) + ".html";
			File file = new File(filePath);
			//if tag file exists then 

			if(file.exists())
			{

				Constant.setLabel("Scraping from " + file.getAbsolutePath() + "/n Stay Calm", getDhfts().getController());
				Constant.setCounterLabel("Scraping from URL " + (i + 1) + " is completed out of " + getDhfts().getTagCollections().size(), getDhfts().getController());
				//calling parseInoformation method which execute Scrapper class 
				parseInformation(file);
				Thread.sleep(15000);
			}
			else
			{
				i--;
				System.out.println("Line 161 Downloader");
				Constant.setLabel(file.getAbsolutePath() + " is not created yet\nTrying Once again" + "/n Stay Calm", getDhfts().getController());				
				Thread.sleep(3000);
				continue;
			}
		}
		
		//at this point, the srcrapping done
		Constant.setLabel("Scraping Completed from " + getDhfts().getBaseUrl(), getDhfts().getController());
		getDhfts().getController().getRootPaneController().getButton().setDisable(false);
		getDhfts().getController().getRootPaneController().getRenameButton().setDisable(false);
		getDhfts().getController().getRootPaneController().getCombobox().setDisable(false);
		getDhfts().getController().getRootPaneController().getUlrField().setDisable(false);
		getDhfts().getController().getRootPaneController().getRenameField().setDisable(false);
		
		if(!getDhfts().getUrl_present_in_database())
			Constant.setInComboBox(getDhfts().getController().getDatabase().getUniqueBaseURLs(), getDhfts().getController());
				
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				getDhfts().getController().getRootPaneController().getProgressIndicator().setProgress(1);				
			}
		});
		
		Constant.getInfo("Scraping Completed from \n" + getDhfts().getBaseUrl());
		Constant.setCounterLabel("Completed", getDhfts().getController());
	}
	
	
	public void parseInformation(File file)
	{
		scrapperPool.execute(new Scrapper(getDhfts(), file));
	}
	
	//this cmd() loop through till all the Search files (pages download)
	public void cmd()
	{
			//command
//			String cmd = "cmd.exe /c start ";
			String pythonCommand = "python " + "\"" + Constant.getAppDirectory() + "\\Scrapper.py\" " + "\"" + getDhfts().getUrl() + "\"";
			String filePath = " " + "\"" + getDhfts().getFullSearchDir().getAbsolutePath() + "\\" + getDhfts().getIndex() + ".html" + "\"";
//			String command = cmd + pythonCommand + filePath;
			
			
			try
			{
				//writing the above command in the bat file and then executing this file 
				String string_to_write = "start /B call " +  pythonCommand  + filePath;
				File batFile = null;
				
				try
				{
					batFile = new File(Constant.getAppDirectory() + "\\fileDonwload.bat");
					FileUtils.writeStringToFile(batFile, string_to_write, "UTF-8");
				}
				catch(Exception exp)
				{
					Constant.getAlert("fileDownload.bat file is not created, there is an error");
				}
				
				@SuppressWarnings("unused")
				Process child = Runtime.getRuntime().exec(batFile.getAbsolutePath());
				Constant.setLabel("Downloading " + filePath + "/n Stay Calm", getDhfts().getController());
				Thread.sleep(15000);
				
				
			}
			catch (IOException e) 
			{
				Constant.getAlert(Arrays.toString(e.getStackTrace()));
			}
			catch (InterruptedException e)
			{
				Constant.getAlert(Arrays.toString(e.getStackTrace()));
			}
			
			
			try
			{
				//now getting url of next page from the current page
				String url = urlOfNextPage();
				
//				System.out.println(counter++ + " URL" + url);
				
				//if url is not null
				if(url != null)
				{
					//setting url			
					getDhfts().setUrl("https://www.zillow.com/" + url);
					
					//increasing index so we get h1, h2, h3 etc
					int index = getDhfts().getIndex() + 1;
					getDhfts().setIndex(index);

					//now loop again 
					cmd();
				}
				else
				{
					File file = new File(getDhfts().getFullSearchDir().getAbsolutePath() + "\\" + getDhfts().getIndex() + ".html");
				
					if(!file.exists())
					{
						Constant.getAlert(file.getAbsolutePath() + " is not found\nRetrying to download");
						cmd();
					}
				}
			}
			catch(Exception exp)
			{
				Constant.getAlert(Arrays.toString(exp.getStackTrace()));
			}

	}
	
	/**
	 * 
	 * @return the url of Next Page
	 * @throws IOException
	 */
	public String urlOfNextPage() throws IOException
	{
		String url = null;
		File input = new File(getDhfts().getFullSearchDir() + "/" + (getDhfts().getIndex()) + ".html");
		Document document = Jsoup.parse(input, "UTF-8");
		Elements myin = document.getElementsByClass("zsg-pagination-next");

		//when myin in empty list then return otherwise content of a attribute
		if(myin.size() != 0)
			url = myin.get(0).select("a").attr("href");
		else
			url = null;
		
		return url;
	}
	
	/**
	 * this method delete all the 
	 * adURLs which are present in the database
	 */
	private void refineAdURLs()
	{
		

		
		//refineTagsList
		//deleting the tags from the list if they are present in the database
		Vector<String> list = new Vector<String>();
		
		for(int i = 0; i < getDhfts().getTagCollections().size(); i++)
		{
			Boolean present = getDhfts().getController().getDatabase().isAdURLPresent(getDhfts().getBaseUrl(), getDhfts().getTagCollections().get(i));
			
			
			if(present)
			{
				list.add(getDhfts().getTagCollections().get(i));
			}
			
		}
		
		getDhfts().getTagCollections().removeAll(list);
				
		Constant.getInfo("There are " + getDhfts().getTagCollections().size() + " new Ads to be retrieve");
			
	}
	
}
