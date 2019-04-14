package model.table;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.downloads.url.URL;

/**
 * this is an class represent an object which is to be stored in the table
 * @author ubaid
 *
 */
public class PropertyInfo
{
	private final SimpleStringProperty address;
	private final SimpleIntegerProperty bed;
	private final SimpleIntegerProperty bathCount;
	private final SimpleIntegerProperty sqft;
	private final SimpleStringProperty listingType;
	private final SimpleIntegerProperty price;
	private final SimpleStringProperty property;
	private final SimpleStringProperty description;
	private final SimpleIntegerProperty yearBuilt;
	private final SimpleStringProperty type;
	private final SimpleObjectProperty<LocalDate> datePosted;
	private final SimpleIntegerProperty id;
	private final SimpleObjectProperty<URL> url;
	private final SimpleObjectProperty<URL> baseURL;
	private final SimpleStringProperty status;
	private final SimpleFloatProperty priceBySqFt;
	
	public PropertyInfo(String address, Integer bed, Integer bath_count, Integer sqft,
			String listingType, Integer price, String property, String description, Integer year_built,
			String type, LocalDate datePosted, int id, URL url, URL baseURL, String status)
	{
		this.address 		= new SimpleStringProperty(address);
		this.bed			= new SimpleIntegerProperty(bed);
		this.bathCount		= new SimpleIntegerProperty(bath_count);
		this.sqft			= new SimpleIntegerProperty(sqft);
		this.listingType	= new SimpleStringProperty(listingType);
		this.price			= new SimpleIntegerProperty(price);
		this.property		= new SimpleStringProperty(property);
		this.yearBuilt		= new SimpleIntegerProperty(year_built);
		this.description	= new SimpleStringProperty(description);
		this.type			= new SimpleStringProperty(type);
		this.datePosted		= new SimpleObjectProperty<LocalDate>(datePosted);
		this.id				= new SimpleIntegerProperty(id);
		this.url			= new SimpleObjectProperty<URL>(url);
		this.baseURL		= new SimpleObjectProperty<URL>(baseURL);
		this.status			= new SimpleStringProperty(status);
	
		
		float pricePerSQFT = (float) ((float) getPrice() / (float) getSqft());

		NumberFormat format = new DecimalFormat("#.##");
		String temp = format.format(pricePerSQFT);
		pricePerSQFT = Float.valueOf(temp);

		
		this.priceBySqFt	= new SimpleFloatProperty(pricePerSQFT);
		
	}
	
	public String getAddress()
	{
		return address.get();
	}

	public Integer getBed() {
		return bed.get();
	}

	public Integer getBathCount() {
		return bathCount.get();
	}

	public int getSqft() {
		return sqft.get();
	}

	public String getListingType() {
		return listingType.get();
	}

	public int getPrice() {
		return price.get();
	}

	public String getProperty() {
		return property.get();
	}

	public String getDescription() {
		return description.get();
	}

	public int getYearBuilt() {
		return yearBuilt.get();
	}

	public String getType() {
		return type.get();
	}

	public LocalDate getDatePosted() {
		return datePosted.get();
	}
	
	public int getId()
	{
		return id.get();
	}
	
	public URL getUrl()
	{
		return url.get();
	}
	
	public URL getBaseURL()
	{
		return baseURL.get();
	}
	
	public String getStatus()
	{
		return status.get();
	}

	public float getPriceBySqFt() {
		return priceBySqFt.get();
	}
}
