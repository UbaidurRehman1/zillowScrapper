package model.downloads;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import model.constant.Constant;
import model.downloads.url.URL;
import model.table.PropertyInfo;

/**
 * this class scrap information from tagSearch file to table and database
 * a new thread created to insert information in the database
 * @author ubaid
 *
 */
public class Scrapper implements Runnable
{
	private DHFTS dhfts;
	private final File file;

	public Scrapper(DHFTS dhfts, File file)
	{
		this.file = file;
		setDhfts(dhfts);
	}
	
	@Override
	public void run()
	{
		try
		{
			if(doScrap())
			{
				System.out.println("Done Successfully");
				Constant.setLabel("Scrap from " + file.getAbsolutePath() + " done successfully", getDhfts().getController());
			}
			else
			{
				Constant.setLabel("Scrap failed from " + file.getAbsolutePath() + " failed, try again later", getDhfts().getController());
				System.out.println("Failed try later");
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		
	}
	
	public Boolean doScrap() throws InterruptedException
	{
		Boolean done = false;
		
		try
		{
			//declaring local variables
			String address = "";
			String bed = "";
			String bath = "";
			String sqft = "";
			
			
			//loading file
			Document document = Jsoup.parse(getFile(), "UTF-8");
			Elements elements = null;
			
			//getting address
			elements = document.getElementsByClass("addr");
			address = elements.select("h1").text();

			//getting number of beds, baths, and sqft
			String [] bbs = elements.select("h3").text().split(" ");
			
			try
			{
				bed = bbs[0];
				
			}
			catch(ArrayIndexOutOfBoundsException exp)
			{
				bed = "-1";
			}

			try
			{
				bath = bbs[1].substring(4).trim();
					
			}
			catch(ArrayIndexOutOfBoundsException exp)
			{
				bath = "-1";
			}

			try
			{
				sqft = bbs[2].substring(5).trim();
				
			}
			catch(ArrayIndexOutOfBoundsException exp)
			{
				sqft = "-1";
			}

						
			int squarFeet = 0;
			try
			{
				squarFeet = convertToNumber(sqft);				
			}
			catch(NumberFormatException exp)
			{
				squarFeet = -1;
			}
			
			int bed_count = 0;
			try
			{
				bed_count = convertToNumber(bed);
			}
			catch(NumberFormatException exp)
			{
				bed_count = -1;
			}
			
			int bath_count = -1;
			try
			{
				bath_count = convertToNumber(bath);
			}
			catch(NumberFormatException exp)
			{
				bath_count = -1;
			}
			
			//declaring variables
			String listingType = "";
			int price_actual = -1;
			String description = "";
			
			//listing type
			elements = document.getElementsByClass("home-summary-row");
			try
			{
				listingType = elements.get(0).text();				
			}
			catch(Exception exp)
			{
				listingType = "No Information"; 
			}
			
			//price
			elements = document.getElementsByClass("main-row");
			String price = elements.text();
			
			try
			{
				price_actual = convertToNumber(price);				
			}
			catch (NumberFormatException e)
			{
				price_actual = -1;
			}
			
			//description

			elements = document.getElementsByClass("zsg-content-item");

			try
			{
				description = elements.get(1).text();				
			}
			catch(Exception exp)
			{
				description = "No Information";
			}
			
			//type yearBuilt and days on zillow
			elements = document.getElementsByClass("hdp-fact-ataglance-value");
			
			String type = "";
			String yearBuilt = "";
			String daysInZillow = "";
			
			try
			{
				type = elements.get(0).text();
				
			}
			catch(IndexOutOfBoundsException exp)
			{
				type = "No Information";
			}
			
			try
			{
				yearBuilt = elements.get(1).text();
				
			}
			catch(IndexOutOfBoundsException exp)
			{
				yearBuilt = "-1";
			}

			try
			{
				daysInZillow = elements.get(6).text();			
			}
			catch(IndexOutOfBoundsException exp)
			{
				daysInZillow = "-1";
			}

			
			int days = -1;
			int year_built = -1;
			
			try
			{
				days = convertToNumber(daysInZillow);
			}
			catch(NumberFormatException exp)
			{
				days = -1;
			}
			
			try
			{
				year_built = convertToNumber(yearBuilt);	
			}
			catch(NumberFormatException exp)
			{
				year_built = -1;
			}
			
			//phone number
			elements = document.getElementsByClass("phone");
			String phone = elements.get(elements.size() - 1).text();
			phone = phoneFormat(phone);
			
			//Date
			LocalDate datePosted = toLocalDate(days);
			
			//URL of this add
			URL url = new URL(getURLofThisTag(), null);
			
			//URL of base url
			URL base_url = getDhfts().getController().getDatabase().getURL(getDhfts().getBaseUrl());
						
			//making an object
			PropertyInfo propertyInfo = new PropertyInfo(address, bed_count, bath_count,
					squarFeet, listingType, price_actual, phone, description, year_built,
					type, datePosted,
					Integer.parseInt(getFile().getName().substring(0, getFile().getName().length() - 5).trim()),
					url, base_url, "new");
			
			
			getDhfts().getList().add(propertyInfo);
			getDhfts().getController().getRootPaneController().getTable().setItems(getDhfts().getList());

			if(getDhfts().getController().getDatabase().insertAdd(propertyInfo))
				System.out.println("propertyInfo document added successfully in the database");
			done = true;
		}
		catch (IOException e)
		{
			
			return false;
		}	
		
		return done;
	}
	
	private int convertToNumber(String number)
	{
		char[] array = number.toCharArray();
		
		char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		
		String number_mod = "";
		for(int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < numberArray.length; j++)
			{
				if(array[i] == numberArray[j])
				{
					number_mod += array[i];
					break;
				}
				else
				{
					continue;
				}
			}
		}
		
		return Integer.parseInt(number_mod.trim());
	}

	public DHFTS getDhfts() {
		return dhfts;
	}

	public void setDhfts(DHFTS dhfts) {
		this.dhfts = dhfts;
	}

	public File getFile() {
		return file;
	}
	
	private LocalDate toLocalDate(int days)
	{
	
		LocalDate date = LocalDate.now().minusDays(days);
		return date;
	}

	
	private String getURLofThisTag()
	{
		String[] index_s = getFile().getAbsolutePath().split("\\\\");
		String index = index_s[index_s.length - 1];
		
		//we subtract a one, becauase the numbering of file starts from 1 not from 0. So file number 1 is 1 - 1 is zero
		//and its ad url index will be rightly placed at 0.
		return getDhfts().getTagCollections().get(convertToNumber(index) - getDhfts().getSizeOfTable() - 1);
	}
	
	private String phoneFormat(String phoneNumber)
	{
		char[] permittedChars = {'(', ')', '-', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		char[] oldPhoneNumChars = phoneNumber.toCharArray();
		
		String newPhoneNumber = "";
		
		for(int i = 0; i < oldPhoneNumChars.length; i++)
		{
			for(int j = 0; j < permittedChars.length; j++)
			{
				if(oldPhoneNumChars[i] == permittedChars[j])
				{
					newPhoneNumber += oldPhoneNumChars[i];
					break;
				}
			}
		}
		
		return newPhoneNumber;
	}
}
