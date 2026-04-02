
package com.scripted.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.scripted.utils.WebCommonFunctions;

import io.cucumber.core.api.Scenario;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class WebTestBase {

	public static WebDriver webDriver = null;
	public static Collection<String> tagNames;
	public static String sheetName = null;
	public static ThreadLocal<String> thSheetName = new ThreadLocal<>();

	private Map<String, String> excelData = WebCommonFunctions.readFromExcelForDasboard("ApplicationURL");
	public static SoftAssert softAssert=null;
	
	@Before
	public void before() throws InterruptedException, IOException {

		
		webDriver =BrowserDriver.funcGetWebdriver();

		// BrowserDriver.launchWebURL("https://jumpree.smartenspaces.com/saasAdminDashboard/#/login");
		BrowserDriver.launchWebURL(excelData.get("URL").trim());
		//BrowserDriver.launchWebURL("https://hswdb.smartenspaces.com/spacemanagementV2/#/login");

		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		softAssert=new SoftAssert();

	}

	@Before
	public void getSheetName(Scenario scenario) {

		if (tagNames == null) {
			tagNames = scenario.getSourceTagNames();
		}
		for (String tag : tagNames) {
			System.out.println(tag);
			if (tag.contains("TD")) {
				tag = tag.replace("@", "");
				thSheetName.set(tag);
				tagNames = null;
				break;
			}
		}
	}

	@After
	public void after(Scenario scenario) {
		if (scenario.isFailed()) {
			TakesScreenshot scrShot = ((TakesScreenshot) WebTestBase.webDriver);
			byte[] screenshot = scrShot.getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
			//scenario.attach(screenshot, "image/png", "Screenshot attached");
			// scenario.tt
			System.out.println("screenshot taken");
		}
		softAssert.assertAll();
	}

	@After
	public void browserClose() throws InterruptedException {
		BrowserDriver.quitBrowser();
		System.out.println("Browser closed");
		
	}

}
