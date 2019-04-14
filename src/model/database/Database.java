package model.database;



import model.downloads.url.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.bson.conversions.Bson;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;


import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.constant.Constant;
import model.evenHanlder.Type;
import model.table.PropertyInfo;


/**
 * this is database class
 * it has three methods
 * getDB which returns the zillow_database if exists
 * if not then it create database and then return 
 * in database there are two collections (1. adviretisement and 2.adds)
 * 
 * 

 * structure of adds collection
 *	{
 *		name_baseURL: "name of base url"
 *		base_url	: "the URL in which these adds are lying
 *		id			: "id_number"
 *		url			: "url_of_add"
 *		address		: "address"
 *		bed			: "number of beds"
 *		bath		: "number of baths"
 *		sqft		: "square feet of home"
 *		listing		: "type of listing"
 *		price		: "price of the house"
 *		phone		: "phone of the property owner"
 *		descrp		: "description of house"
 * 		year		: "year built"
 *		type		: "type of house"
 *		date		: "posted date of this add"
 *	}
 * 
 * @author ubaid
 *
 */
public class Database
{
	final Controller controller;
	private final MongoDatabase db = getConnection();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public Database(Controller controller)
	{
		this.controller = controller;
		getDB();
	}
	
	/**
	 * 
	 * @return this method return an array of unique base_urls in database
	 * 
	 */
	public ObservableList<URL> getUniqueBaseURLs()
	{	
//		Vector<String> array2 = new Vector<String>();
		ObservableList<URL> array = FXCollections.observableArrayList();
		DistinctIterable<String> id= getAds().distinct("base_url", String.class);
		
		MongoCursor<String> iteration = id.iterator();
		
		while(iteration.hasNext())
		{
			String URL = iteration.next();
			//find the document associated to this URL
			//if this document has name (not null) then 
			//assign its name
			Bson filter = Filters.eq("base_url", URL);
			MongoIterable<Document> document = getAds().find(filter);
			Document doc = document.first();
			
			String name = doc.getString("name_base_url");
			
			URL uri = new URL(URL, name);
			
			array.add(uri);
		}

		return array;
	}
	
	/**
	 * 
	 * @return collection in the database (only one collection is in the database)
	 */
	private MongoCollection<Document> getAds()
	{
		MongoCollection<Document> collection = getDB().getCollection("ads");
		return collection;
	}
	
	
	/**
	 * 
	 * @param info is an object of PropertyInfo
	 * this method create a document and then insert in the ads collection
	 * @return true if added successfully in the collection
	 */
	public Boolean insertAdd(PropertyInfo info)
	{
		Boolean decision = true;
		
		Document ad = new Document("name_base_url", info.getBaseURL().getName())
							.append("base_url", info.getBaseURL().getUrl())
							.append("id", info.getId())
							.append("url", info.getUrl().getUrl())
							.append("address", info.getAddress())
							.append("bed", info.getBed())
							.append("bath", info.getBathCount())
							.append("sqft", info.getSqft())
							.append("listing", info.getListingType())
							.append("price", info.getPrice())
							.append("phone", info.getProperty())
							.append("description", info.getDescription())
							.append("year", info.getYearBuilt())
							.append("type", info.getType())
							.append("date", info.getDatePosted().toString());
		
		try
		{
			getAds().insertOne((Document) ad);
			Constant.setLabel("Document added into the database successfully", controller);
		}
		catch(Exception exp)
		{
			Constant.setLabel("Document not added", controller);			
			decision = false;
		}
		
		return decision;
	}


	/**
	 * 
	 * @return this method return the database zillow_ads
	 * @throws IllegalArgumentException
	 */
	private MongoDatabase getConnection() throws IllegalArgumentException
	{
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = null;
		
		
		if(!(databaseFound("zillow_ads", mongoClient)))
		{
			System.out.println("Database not exists, Creating Database");
			MongoCredential.createCredential("json", "zillow_ads", "zillow".toCharArray());
			database = mongoClient.getDatabase("zillow_ads");
			database.createCollection("ads");
		}
		else
		{
			System.out.println("Database exists");
			database = mongoClient.getDatabase("zillow_ads");
		}
		
		
		return database;
	}
	
	/**
	 * 
	 * @param databaseName
	 * @param mongoClient
	 * @return true if database exists false otherwise
	 */
	private Boolean databaseFound(String databaseName, MongoClient mongoClient)
	{
	    MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
	    while(dbsCursor.hasNext())
	    {
	        if(dbsCursor.next().equals(databaseName))
	            return true;
	    }
	    return false;
	}

	/**
	 * 
	 * @return zillow_ads database
	 */
	public MongoDatabase getDB() {
		return db;
	}
	
	/**
	 * 
	 * @param uri
	 * @return all scrapped data in Obserable list of the give uri
	 */
	public ObservableList<PropertyInfo> getAllScrapedData(String uri, Type type)
	{
		ObservableList<PropertyInfo> array = FXCollections.observableArrayList();
	

		Bson filter = null;
		uri = uri.trim();
		
		if(type == Type.URL)
		{
			filter = Filters.eq("base_url", uri);	
		}
		else if(type == Type.NAME)
		{
			filter = Filters.eq("name_base_url", uri);
		}
		
		MongoIterable<Document> documents = getAds().find(filter);
				
		MongoCursor<Document> arrayOfDocument = documents.iterator();
		
		while(arrayOfDocument.hasNext())
		{
			Document document = arrayOfDocument.next();
			
			String name_base_url 		= document.getString("name_base_url");
			String base_url				= document.getString("base_url");
			int id						= document.getInteger("id");
			String url					= document.getString("url");
			String address				= document.getString("address");
			int bed						= document.getInteger("bed");
			int bath					= document.getInteger("bath");
			int sqft					= document.getInteger("sqft");
			String listing				= document.getString("listing");
			int price 					= document.getInteger("price");
			String phone				= document.getString("phone");
			String description			= document.getString("description");
			int year					= document.getInteger("year");
			String type_				= document.getString("type");
			String date					= document.getString("date");			
			LocalDate localDate 		= LocalDate.parse(date, formatter);
			
			PropertyInfo info = new PropertyInfo(address, bed, bath, sqft, listing,
								price, phone, description, year,
								type_, localDate, id, new URL(url, null), new URL(base_url, name_base_url), null);
			array.add(info);
			
		}


		return array;
	}
	
	/**
	 * 
	 * @param URI (we parse all documents having this base uri and)
	 * @param name ( rename all the documents with this name)
	 * @return true if process gone successful
	 */
	public Boolean updateDocumentsName(String URI, String name)
	{
		Boolean decision = true;
		
		try
		{
			Bson filter = Filters.eq("base_url", URI.trim());
//			MongoIterable<Document>	documents = getAds().find(filter);
//			MongoCursor<Document> documentsArray = documents.iterator();
			
			Bson newValue = new Document("name_base_url", name);
			Bson updateOperationDocument = new Document("$set", newValue);
			getAds().updateMany(filter, updateOperationDocument);
		
		}
		catch(Exception exception)
		{
			Constant.getAlert("Renaming a URL Failed due to some unknown error, try again");
			decision = false;
		}

		
		return decision;
	}
	
	public Boolean isURLPresentInDataBase(String URL)
	{
		Boolean decision = false;
		
		try
		{
			Bson filter = Filters.eq("base_url", URL);
			MongoIterable<Document> documents = getAds().find(filter);
			
			MongoCursor<Document> docs = documents.iterator();
			
			if(docs.hasNext())
				decision = true;
			else
				decision = false;
		}
		catch(Exception exp)
		{
			Constant.getAlert("Cannot extract due to some unknown database error, try again");
			decision = false;
		}
		
		return decision;
	}
	
	/**
	 * 
	 * @param base_url
	 * @return a URL object using base_url
	 */
	public URL getURL(String base_url)
	{
		try
		{
			Bson filter = Filters.eq("base_url", base_url);
			MongoIterable<Document> documents = getAds().find(filter);

			Document document = documents.first();

			if(documents == null || document == null)
			{
				return new URL(base_url, null);
			}
			
			String name = document.getString("name_base_url");
			
			URL url = new URL(base_url, name);
			
			return url;
			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			Constant.getAlert("Renaming a URL Failed due to some database unknown error, try again");
		}
		
		return null;
	}
	
	public Boolean isAdURLPresent(String base_url, String ad_url)
	{
		try
		{
			Bson filter = Filters.eq("base_url", base_url);
			Bson filter2 = Filters.eq("url", ad_url);
			Bson filter3 = Filters.and(filter, filter2);
			
			MongoIterable<Document> documents = getAds().find(filter3);
						
			MongoCursor<Document> docs = documents.iterator();
			
			if(docs.hasNext())
				return true;
			else
				return false;
		}
		catch(Exception exp)
		{
			Constant.getAlert("Renaming a URL Failed due to some database unknown error, try again");
		}
		
		
		return null;
	}
	
	/**
	 * this method delete a record from the database
	 * @param base_url
	 * @param ad_url
	 */
	public void deleteRecord(String base_url, String ad_url)
	{
		Bson base_url_filter = Filters.eq("base_url", base_url);
		Bson ad_url_filter	 = Filters.eq("url", ad_url);
		Bson filter			 = Filters.and(base_url_filter, ad_url_filter);		
		Document document =  getAds().findOneAndDelete(filter);
		Constant.getInfo(document.getString("url") + "\n------------Removed-------------");
		
	}
	
	public Boolean deleteTable(String base_url)
	{
		
		try
		{
			Bson base_url_filter = Filters.eq("base_url", base_url);
			getAds().deleteMany(base_url_filter);
			return true;
		}
		catch(Exception exp)
		{
			return false;
		}
	}
}
