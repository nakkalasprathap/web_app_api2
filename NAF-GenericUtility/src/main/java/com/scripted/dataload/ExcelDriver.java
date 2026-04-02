package com.scripted.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.scripted.generic.FileUtils;

import junit.framework.Assert;



public class ExcelDriver {

	//Variables definition for the class
	public static Logger LOGGER = Logger.getLogger(ExcelDriver.class);
	private Sheet currentSheet = null;
	private Workbook currentWorkbook = null;
	private Map<String,Integer> headerMap = new LinkedHashMap<String,Integer>();
	public static  Map<String, String> fRow;
	static String key = "";
	static TestDataFactory test = new TestDataFactory();
	static PropertyDriver propDriver = new PropertyDriver();



	/**
	 * Constructors based on input parameter type
	 * @param filePath - path to the excel file
	 */
	public ExcelDriver(String filePath) {
		if(!filePath.isEmpty()) {
			File excelFile = new File(filePath);
			if(excelFile.canRead()) {
				this.openSheet(excelFile, 0);
			} else {
				LOGGER.info("File could not be read." + filePath);
			}

		} else {
			LOGGER.info("Path is empty.");
		}
	}

	/**
	 * Constructors based on input parameter type
	 * @param excelFile - full name of excel file
	 */
	//CHECK THIS ONE
	public ExcelDriver(File excelFile) {
		if(null != excelFile && excelFile.exists()) {
			this.openSheet(excelFile, 0);
		} else {
			try {
				LOGGER.info("File not found");
				throw new FileNotFoundException("File Not Found");
			} catch (FileNotFoundException e) {
				System.out.println(e);
				LOGGER.error("File not found "+"Exception :"+e);
				Assert.fail("File not found "+"Exception :"+e);
			}
		}
	}

	public static String modifySheetName(String sheetName) {
		if (sheetName.contains("TD") || sheetName.contains("TC")) {
			sheetName = sheetName.replace("TD", "");
			sheetName = sheetName.replace("TC", "");
		}
		for (int i = sheetName.length() - 1; i > 0; i--) {
			if (Character.isDigit(sheetName.charAt(i))) {
				sheetName = sheetName.substring(0, sheetName.length() - 1);
			} else {
				break;
			}
		}
		return sheetName;
	}

	/**
	 * Open a sheet based on excelFile and sheet name
	 * @param excelFile - full name of excel file
	 * @param sheetName - sheet name
	 */
	public void openSheet(File excelFile,String sheetName) {

		try {
			currentWorkbook = createWorkbook(excelFile);
			currentSheet = currentWorkbook.getSheet(sheetName);
			setHeaderMap(currentSheet);
		}
		catch(Exception e)
		{
			LOGGER.error("Error while opening excel sheet "+"Exception :"+e);
			Assert.fail("Error while opening excel sheet "+"Exception :"+e);
		}
	}

	/**
	 * Open a sheet based on excelFile and sheet number
	 * @param excelFile - full name of the excel file
	 * @param sheetNo - sheet number
	 */
	public void openSheet(File excelFile,int sheetNo) {

		try
		{
			currentWorkbook = createWorkbook(excelFile);
			currentSheet = currentWorkbook.getSheetAt(sheetNo);
			setHeaderMap(currentSheet);
		}
		catch(Exception e)
		{
			LOGGER.error("Error while opening excel sheet "+"Exception :"+e);
			Assert.fail("Error while opening excel sheet "+"Exception :"+e);
		}

	}

	/**
	 *To proceed to a sheet based on sheet number 
	 * @param sheetNo - number of the sheet
	 */
	public void goToSheet(int sheetNo)
	{
		try {


			if((sheetNo + 1) <= currentWorkbook.getNumberOfSheets())
			{
				currentSheet = currentWorkbook.getSheetAt(sheetNo);
				setHeaderMap(currentSheet);
			} else
			{
				LOGGER.info("Sheet not available");
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while traversing to the excel sheet with sheet number "+sheetNo+" Exception :"+e);
			Assert.fail("Error while traversing to the excel sheet with sheet number "+sheetNo+" Exception :"+e);
		}
	}

	/**
	 * To proceed to a sheet based on sheet name
	 * @param sheetName - name of the sheet
	 */
	public void goToSheet(String sheetName) {
		try
		{
			currentSheet = currentWorkbook.getSheet(sheetName);
			setHeaderMap(currentSheet);
		}
		catch(Exception e)
		{
			LOGGER.error("Error while traversing to the excel sheet with sheet name "+sheetName +" Exception :"+e);
			Assert.fail("Error while traversing to the excel sheet with sheet name "+sheetName +" Exception :"+e);
		}
	}

	/**
	 * To return the current workbook
	 */
	public Workbook getCurrentWorkbook() {
		return currentWorkbook;
	}

	/**
	 * To return the current sheet in the workbook
	 */
	public Sheet getCurrentSheet() {
		return currentSheet;
	}


	/**
	 * To return the current sheet header in a hashmap
	 */
	public Map<String,Integer> getHeaderMap() {

		try
		{
			if(headerMap.size() > 0)
			{
				return headerMap;
			} else
			{
				LOGGER.info("No headers in active sheet");
				return headerMap;
			}

		}
		catch (Exception e)
		{
			LOGGER.error("Error while getting current sheet header "+"Exception :"+e);
			Assert.fail("Error while getting current sheet header "+"Exception :"+e);
		}
		return headerMap;
	}

	/**
	 * To obtain the column header number
	 * @param columnName - column name 
	 */

	public int getColumnHeaderNo(String columnName) {

		int columnNo = -1;

		try
		{
			if(getHeaderMap().size() > 0)
			{

				if(null != getHeaderMap().get(columnName))
				{
					columnNo = getHeaderMap().get(columnName);
				}
			}

		}
		catch(Exception e)
		{
			LOGGER.error("Error while getting current column header "+"Exception :"+e);
			Assert.fail("Error while getting current column header "+"Exception :"+e);
		}
		return columnNo;
	}

	/**
	 *To obtain a particular cell value based on row and column number 
	 * @param row - row number
	 * @param column - column number
	 */

	public String getCellValueAt(int row,int column) {

		Row currentRow = currentSheet.getRow(row);
		Cell currentCell = currentRow.getCell(column, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		String cellValue = "";
		try
		{
			if(currentCell.getCellTypeEnum() == CellType.NUMERIC)
			{

				if(DateUtil.isCellDateFormatted(currentCell))
				{
					Date dt = currentCell.getDateCellValue();
					//SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					cellValue = sdf.format(dt);
				} else
				{
					cellValue = Long.toString(Math.round(currentCell.getNumericCellValue()));
				}
			}
			else if(currentCell.getCellTypeEnum() == CellType.BLANK)
			{

				cellValue = "";
			} else if(currentCell.getCellTypeEnum() == CellType.BOOLEAN)
			{

				cellValue = String.valueOf(currentCell.getBooleanCellValue());
			} else if(currentCell.getCellTypeEnum() == CellType.ERROR)
			{

				cellValue = Byte.toString(currentCell.getErrorCellValue());
			} else if(currentCell.getCellTypeEnum() == CellType.STRING)
			{
				cellValue = currentCell.getStringCellValue();
			} else
			{
				cellValue = currentCell.getStringCellValue();
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get cell value of current sheet "+"Exception :"+e);
			Assert.fail("Error while trying to get cell value of current sheet "+"Exception :"+e);
		}
		return cellValue;
	}

	/**
	 *To obtain the values of all columns in a row with header row value(assuming that the first row is header row) based 
	 row number as parameter 
	 * @param rowNo- row number
	 */

	public Map<String,String> getRowMap(int rowNo)
	{

		Map<String,String> rowMap = new LinkedHashMap<String,String>();
		Row currentRow = currentSheet.getRow(rowNo);
		try
		{
			for(int i = 0; i < currentRow.getPhysicalNumberOfCells(); i++)
			{
				String headerKey = getCellValueAt(0, i);
				String value = getCellValueAt(rowNo,i);
				rowMap.put(headerKey,value);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get column values for the row : "+rowNo +"Exception :"+e);
			Assert.fail("Error while trying to get column values for the row : "+rowNo +"Exception :"+e);
		}

		return rowMap;
	}

	public Map<String,String> getRowMap(int rowNo, boolean hasHeader) {
		Map<String,String> rowMap = new LinkedHashMap<String,String>();
		Row currentRow = currentSheet.getRow(rowNo);
		Map<Integer, String> headerMap = new LinkedHashMap<Integer, String>();
		try
		{
			if(hasHeader == false)
			{
				for(int i = 0; i < currentRow.getLastCellNum(); i++)
				{
					Integer headerKey = i;
					String value = "Column" + Integer.toString(i);;
					headerMap.put(headerKey,value);
				}
			}
			String headerKey;
			for(int i = 0; i < currentRow.getLastCellNum(); i++)
			{
				if(hasHeader == true)
				{
					headerKey = getCellValueAt(0, i);
				}
				else
				{
					headerKey = headerMap.get(i);
				}
				String value = getCellValueAt(rowNo,i);
				rowMap.put(headerKey,value);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get column values for the row : "+rowNo +"Exception :"+e);
			Assert.fail("Error while trying to get column values for the row : "+rowNo +"Exception :"+e);
		}
		return rowMap;
	}

	/**
	 * To obtain the values of all columns in a row with header row value(assuming that the first row is header row) based on 
	 column number and required matching data as parameter
	 * @param columnNo - column number
	 * @param data - data in the column
	 */
	public Map<String,String> getRowMap(int columnNo,String data)
	{
		Map<String,String> rowMap = new LinkedHashMap<String,String>();
		int rowNo = getRowNoByColumnData(columnNo,data);
		Row currentRow = currentSheet.getRow(rowNo);
		try
		{
			for(int i = 0; i < currentRow.getPhysicalNumberOfCells(); i++)
			{
				String headerKey = getCellValueAt(0, i);
				String value = getCellValueAt(rowNo,i);
				rowMap.put(headerKey,value);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get values of the column : "+columnNo +"Exception :"+e);
			Assert.fail("Error while trying to get values of the column : "+columnNo +"Exception :"+e);
		}
		return rowMap;
	}

	/**
	 *To obtain the values of all columns in a row with header row value(if the first row is header row) based 
	 on column name and matching data as parameter 
	 * @param columnName - column name
	 * @param data - data in the column
	 */
	public Map<String,String> getRowMap(String columnName,String data) {
		Map<String,String> rowMap = new LinkedHashMap<String,String>();
		int rowNo = getRowNoByColumnName(columnName, data);
		Row currentRow = currentSheet.getRow(rowNo);
		try
		{
			for(int i = 0; i < currentRow.getPhysicalNumberOfCells(); i++)
			{
				String headerKey = getCellValueAt(0, i);
				String value = getCellValueAt(rowNo,i);
				rowMap.put(headerKey,value);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get values of the column with column name : "+columnName +"Exception :"+e);
			Assert.fail("Error while trying to get values of the column with column name  : "+columnName +"Exception :"+e);
		}
		return rowMap;
	}

	/**
	 * To obtain row number by column data
	 * @param columnNo - column number
	 * @param data - data in the column
	 */
	public int getRowNoByColumnData(int columnNo,String data) {
		int rowNo = -1;
		try
		{
			for(int i = 1; i < currentSheet.getPhysicalNumberOfRows(); i++)
			{
				String value =  getCellValueAt(i, columnNo);
				if(data.equalsIgnoreCase(value))
				{
					rowNo = i;
					return rowNo;
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get row number with column data : "+columnNo +","+data +"Exception :"+e);
			Assert.fail("Error while trying to get row number with column data :"+columnNo +","+data +"Exception :"+e);
		}
		return rowNo;
	}

	/**
	 *To obtain row number by column name 
	 * @param columnHeader - column header value
	 * @param value - search value
	 */
	public int getRowNoByColumnName(String columnHeader,String value)
	{
		int rowNo = -1;
		try
		{
			if(currentSheet.getPhysicalNumberOfRows() > 1)
			{
				int columnNo = getColumnHeaderNo(columnHeader);

				for(int i = 0; i < currentSheet.getPhysicalNumberOfRows(); i++)
				{

					String cv = getCellValueAt(i,columnNo);
					if(value.equals(cv))
					{
						rowNo = i;
						return rowNo;
					}
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Error while trying to get row number with column name : "+value +"Exception :"+e);
			Assert.fail("Error while trying to get row number with column name :"+value +"Exception :"+e);
		}
		return rowNo;
	}

	/**
	 *To switch to another excel file 
	 * @param filePath -excel file path
	 */
	public void switchToFile(String filePath) {
		this.currentSheet = null;
		this.currentWorkbook = null;
		this.headerMap.clear();
		new ExcelDriver(filePath);
	}

	/**
	 *To Create a workbook 
	 * @param excelFile - excel file name
	 */
	private Workbook createWorkbook(File excelFile) {

		if(isXlsFileType(excelFile))
		{
			try
			{
				currentWorkbook = new XSSFWorkbook(excelFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error("Error while trying to create new workbook "+"Exception :"+e);
				Assert.fail("Error while trying to create new workbook "+"Exception :"+e);
			}
		}
		else
		{
			currentWorkbook = new HSSFWorkbook();
		}

		return currentWorkbook;
	}

	/*
	 *Defining workbook
	 * @param excel file path
	 */
	private boolean isXlsFileType(File excelFile) {

		return (excelFile.getName().endsWith("xlsx")||excelFile.getName().endsWith("xlsm") ? true : false);

	}

	/*
	 * To obtain the current sheet
	 * @param current sheet number
	 */
	private void setHeaderMap(Sheet currentSheet)
	{
		try
		{
			if(currentSheet.getPhysicalNumberOfRows() > 0)
			{
				headerMap.clear();
				Row headerRow = currentSheet.getRow(0);
				for(Cell cell : headerRow)
				{
					headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error while trying to get the current sheet "+"Exception :"+e);
			Assert.fail("Error while trying to get the current sheet "+"Exception :"+e);
		}

	}

	public int getRowCount()
	{
		return currentSheet.getPhysicalNumberOfRows();
	}



	//To obtain the test data according to the column name 

//	public static Map<String, String> readFromExcel(String filePath,String sheetName) {
//        propDriver.setPropFilePath(filePath);
//        String filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
//
//        ExcelConnector con = new ExcelConnector(filename, sheetName);
//        TestDataObject obj = test.GetData(key, con);
//        LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();
//       // System.out.println(getAllData.size());
//        fRow = (getAllData.get("1"));
//
//        return fRow;
//    }

	public static Map<String, String> readFromExcel(String propFilePath, String sheetName) throws IOException, InvalidFormatException {
		return readFromExcel(propFilePath, sheetName, null);
	}

	public static Map<String, String> readFromExcel(String propFilePath, String sheetName, String xlPath) {
		propDriver.setPropFilePath(propFilePath);
		String filename = null;
		if(StringUtils.equals(xlPath, null)){
			filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
		} else {
			filename = FileUtils.getCurrentDir() + PropertyDriver.readProp(xlPath);
		}
		ExcelConnector con = new ExcelConnector(filename, sheetName);
		TestDataObject obj = test.GetData(key, con);
		LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();
		// System.out.println(getAllData.size());
		fRow = (getAllData.get("1"));

		return fRow;
	}

	public static Map<String, List<String>> getXlList(String filePath, String sheetName) throws IOException, InvalidFormatException {
		return getXlList(filePath, sheetName, null);
	}

	public static Map<String, List<String>> getXlList(String filePath, String sheetName, String xlPath) throws IOException, InvalidFormatException {
		propDriver.setPropFilePath(filePath);
		String filename = null;
		if(StringUtils.equals(xlPath, null)){
			filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
		} else {
			filename = FileUtils.getCurrentDir() + PropertyDriver.readProp(xlPath);
		}
		FileInputStream fis = new FileInputStream(filename);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet(sheetName);
		System.out.println("Last row number:"+sheet.getLastRowNum());
		Row row = null;
		Cell cell = null;
		Map<String, List<String>> xlData = new LinkedHashMap<>();
		List<String> dataList;
		String header = null;

		try{
			int colCount = sheet.getRow(0).getLastCellNum();
			int rowCount = sheet.getLastRowNum() + 1;

			column:
			for (int i = 0; i < colCount; i++) {
				boolean valid = true;
				dataList = new ArrayList<>();
				row:
				for (int j = 0; j < rowCount; j++) {
					row = sheet.getRow(j);
					cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String cellData = cell.getStringCellValue().trim();
					if(cellData.equals("")) {
						cell.setCellType(CellType.BLANK);
					}
					if (j == 0) {
						if(!cell.getCellTypeEnum().equals(CellType.BLANK)) {
							header = cellData;
						} else {
							valid = false;
							break row;
						}
					} else if (!cell.getCellTypeEnum().equals(CellType.BLANK)) {
						dataList.add(cellData);
					}
				}
				if (valid) {
					xlData.put(header, dataList);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			LOGGER.error("Exception :" + e);
			Assert.fail("Exception :" + e);
		}
		return xlData;
	}
}