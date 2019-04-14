package model.export;

import java.io.File;

import controller.Controller;
import javafx.collections.ObservableList;
import model.table.PropertyInfo;

public interface Export
{
	public Boolean export(File file, ObservableList<PropertyInfo> list, Controller controller) throws Exception;
	
	enum Type
	{
		CSV,
		XLMS;
	}
}
