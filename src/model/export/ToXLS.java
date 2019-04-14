package model.export;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.constant.Constant;
import model.table.PropertyInfo;

public class ToXLS implements Export
{

	public ToXLS(File file, ObservableList<PropertyInfo> list, Controller controller) throws Exception
	{
		if(export(file, list, controller))
		{
			Constant.getInfo(file.getAbsolutePath() + " created successfully");
		}
	}
	
	
	@Override
	public Boolean export(File file, ObservableList<PropertyInfo> list, Controller controller) throws Exception
	{
		
		
		 	@SuppressWarnings("resource")
		 	Workbook workbook = new HSSFWorkbook();
		 	
		 	Sheet spreadsheet = workbook.createSheet();
		 	
	        Row row = spreadsheet.createRow(0);
	        TableView<PropertyInfo> table = controller.getRootPaneController().getTable();
	        	        
	        //for headers
	        for (int j = 0; j < table.getColumns().size() - 1; j++)
	        {
	            row.createCell(j).setCellValue(table.getColumns().get(j).getText());
	        }

	        
	        for (int i = 0; i < table.getItems().size(); i++)
	        {
	            row = spreadsheet.createRow(i + 1);
	            for (int j = 0; j < table.getColumns().size(); j++)
	            {
	                if(table.getColumns().get(j).getCellData(i) != null)
	                { 
	                	
	                	switch (j)
	                	{
						case 0:
							row.createCell(j).setCellValue((int) table.getColumns().get(j).getCellData(i));
		                    break;

						case 1:
		                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString()); 							
							break;

						case 2:
							row.createCell(j).setCellValue((int) table.getColumns().get(j).getCellData(i));							
							break;

						case 3:
							row.createCell(j).setCellValue((int) table.getColumns().get(j).getCellData(i));							
							break;

						case 4:
							row.createCell(j).setCellValue((int) table.getColumns().get(j).getCellData(i));							
							break;

						case 5:
							row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());							
							break;

						case 6:
							CellStyle cellStyle2 = workbook.createCellStyle();
							cellStyle2.setDataFormat((short)6);
							Cell cell2 = row.createCell(j);
							cell2.setCellStyle(cellStyle2);
							cell2.setCellValue((int) table.getColumns().get(j).getCellData(i));
							break;

						case 7:
							CellStyle cellStyle3 = workbook.createCellStyle();
							cellStyle3.setDataFormat((short)8);
							Cell cell3 = row.createCell(j);
							cell3.setCellValue((float) table.getColumns().get(j).getCellData(i));
							cell3.setCellStyle(cellStyle3);
							cell3.setCellValue((float) table.getColumns().get(j).getCellData(i));
							break;

						case 8:
							row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
							break;

						case 9:
							row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
							break;

						case 10:
							row.createCell(j).setCellValue((int) table.getColumns().get(j).getCellData(i));
							break;

						case 11:
							row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
							break;

						case 12:
							CellStyle cellStyle = workbook.createCellStyle();
							CreationHelper createHelper = workbook.getCreationHelper();
							cellStyle.setDataFormat(
							    createHelper.createDataFormat().getFormat("m/d/yy"));
							Cell cell = row.createCell(j);
							cell.setCellStyle(cellStyle);
							Date date = Date.from(((LocalDate) table.getColumns().get(j).getCellData(i)).atStartOfDay(ZoneId.systemDefault()).toInstant());
							cell.setCellValue(date);
							break;

							
						default:
							System.out.println("129");
							break;
						}
	                	
	                	
	                }
	                else
	                {
	                    row.createCell(j).setCellValue("");
	                }   
	            }
	        }

	        FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
	        workbook.write(fileOut);
	        fileOut.close();
		
		return true;
	}
	
}
