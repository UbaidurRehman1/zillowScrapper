package view;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.table.PropertyInfo;

public class RootPane implements Initializable
{
	
	@FXML TextField ulrField;
	@FXML Button button;
	@FXML ComboBox<model.downloads.url.URL> combobox;
	@FXML TableView<PropertyInfo> table;
	@FXML TableColumn<PropertyInfo, String> address;
	@FXML TableColumn<PropertyInfo, Integer> bed;
	@FXML TableColumn<PropertyInfo, Integer> bath_count;
	@FXML TableColumn<PropertyInfo, Integer> sqft;
	@FXML TableColumn<PropertyInfo, String> listing_type;
	@FXML TableColumn<PropertyInfo, Integer> price;
	@FXML TableColumn<PropertyInfo, String> propery;
	@FXML TableColumn<PropertyInfo, String> description;
	@FXML TableColumn<PropertyInfo, Integer> year_built;
	@FXML TableColumn<PropertyInfo, String> type;
	@FXML TableColumn<PropertyInfo, LocalDate> datePosted;
	@FXML TableColumn<PropertyInfo, Integer> id;
	@FXML TableColumn<PropertyInfo, Float> priceBySqft;
	@FXML Label label;
	@FXML ProgressIndicator progressIndicator;
	@FXML Button renameButton;
	@FXML TextField renameField;
	@FXML TableColumn<PropertyInfo, String> status;
	@FXML MenuItem close;
	@FXML MenuItem about;
	@FXML MenuItem export_to_xmls;
	@FXML MenuItem export_to_csv;
	@FXML Button deleteButton;
	@FXML Label counterLabel;
	@FXML Button deleteTable;
	@FXML ImageView imageView;
	@FXML MenuItem uploadLogo;
	@FXML MenuItem deleteLogo;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		ulrField.setEditable(false);
		button.setDisable(true);
		id.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("id"));
		address.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("address"));
		bed.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("bed"));
		bath_count.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("bathCount"));
		sqft.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("sqft"));
		listing_type.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("listingType"));
		price.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("price"));
		propery.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("property"));
		description.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("description"));
		year_built.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Integer>("yearBuilt"));
		type.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("type"));
		datePosted.setCellValueFactory(new PropertyValueFactory<PropertyInfo, LocalDate>("datePosted"));
		status.setCellValueFactory(new PropertyValueFactory<PropertyInfo, String>("status"));
		priceBySqft.setCellValueFactory(new PropertyValueFactory<PropertyInfo, Float>("priceBySqFt"));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		datePosted.setCellFactory(tc -> new TableCell<PropertyInfo, LocalDate>()
		{
		    @Override
		    protected void updateItem(LocalDate date, boolean empty)
		    {
		        super.updateItem(date, empty);
		        if (empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText(formatter.format(date));
		        }
		    }
		});
		
	}


	public TextField getUlrField() {
		return ulrField;
	}


	public Button getButton() {
		return button;
	}


	public ComboBox<model.downloads.url.URL> getCombobox() {
		return combobox;
	}
	
	public TableView<PropertyInfo> getTable()
	{
		return table;
	}
	
	public Label getLabel()
	{
		return label;
	}
	
	public ProgressIndicator getProgressIndicator()
	{
		return progressIndicator;
	}
	
	public Button getRenameButton()
	{
		return renameButton;
	}
	
	public TextField getRenameField()
	{
		return renameField;
	}
	
	public MenuItem getCloseMenuItem()
	{
		return close;
	}
	
	public MenuItem getAboutMenuItem()
	{
		return about;
	}
	
	public MenuItem getExportCSVMenuItem()
	{
		return export_to_csv;
	}
	
	public MenuItem getExportXLMSMenuItem()
	{
		return export_to_xmls;
	}
	
	public Button getDeleteButton()
	{
		return deleteButton;
	}
	
	public Label getCounterLabel()
	{
		return counterLabel;
	}
	
	public Button getDeleteTableButton()
	{
		return deleteTable;
	}
	
	public ImageView getImageView()
	{
		return imageView;
	}
	
	public MenuItem getUploadLogoItem()
	{
		return uploadLogo;
	}
	
	public MenuItem getDeleteLogoItem()
	{
		return deleteLogo;
	}
}
