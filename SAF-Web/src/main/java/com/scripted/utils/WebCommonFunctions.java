package com.scripted.utils;

import static com.scripted.dataload.ExcelDriver.modifySheetName;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.scripted.dataload.ExcelDriver;
import com.scripted.web.WebTestBase;

public class WebCommonFunctions {
	
static String filePath = "src/main/resources/properties/jumpreeTestDataConfig.properties";
    
    public static Logger LOGGER = Logger.getLogger(WebCommonFunctions.class);
    public static Map<String, List<String>> input;

    public static Map<String, String> readFromExcelForDasboard() {
        return readFromExcelForDasboard(null);
    }

    public static Map<String, String> readFromExcelForDasboard(String sheetName) {
        if (StringUtils.equals(sheetName, null)) {
            return ExcelDriver.readFromExcel(filePath, modifySheetName(WebTestBase.thSheetName.get()), "DashboardExcelName");
        } else {
        	
            return ExcelDriver.readFromExcel(filePath, sheetName, "DashboardExcelName");
            
        }
    }

    public static Map<String, List<String>> getXlListForDashboard() throws IOException, InvalidFormatException {
        return getXlListForDashboard(null);
    }

    public static Map<String, List<String>> getXlListForDashboard(String sheetName) throws IOException, InvalidFormatException {
        if (StringUtils.equals(sheetName, null)) {
            return ExcelDriver.getXlList(filePath, modifySheetName(WebTestBase.thSheetName.get()), "DashboardExcelName");
        } else {
        	System.out.println("Sheet name in WebCommon functions:"+sheetName);
            return ExcelDriver.getXlList(filePath, sheetName, "DashboardExcelName");
        }
    }

}
