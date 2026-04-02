package com.scripted.mobile;

import java.io.IOException;
import java.util.Collection;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.scripted.dataload.ExcelDriver;

import io.appium.java_client.MobileDriver;
import io.cucumber.java.Before;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;

public class MobileTestBase {

	public static MobileDriver<WebElement> appiumDriver;
	public static Collection<String> tagNames;
	public static String sheetName = null;
	public static ThreadLocal<String> thSheetName = new ThreadLocal<>();
	public static String platform = null;

	public static void setPlatform(String platform) {
		MobileTestBase.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	@Before
	public void before() throws InterruptedException, IOException {
		if (getPlatform().equals("AndroidNative")) {
			
			System.out.println("The platform is : " + getPlatform());
			// MobileDriverSettings.funcGetNativeAndroiddriver("jumpreeAndroidConfig");
			MobileDriverSettings.funcGetNativeAndroiddriver("V3jumpreeAndroidConfig");
			//MobileDriverSettings.funcGetpCloudyNativeAndroiddriver("pCloudyAndroidAPP");
			System.out.println("Mobiletestbase Driver : " + MobileDriverSettings.getCurrentDriver());

		} else if (getPlatform().equals("IOSNative")) {

			System.out.println("The platform is :" + getPlatform());
			MobileDriverSettings.funcGetpCloudyNativeIOSdriver("pCloudyIOSAPP");
			System.out.println("Mobiletestbase Driver : " + MobileDriverSettings.getCurrentDriver());

		}
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
			TakesScreenshot scrShot = ((TakesScreenshot) MobileDriverSettings.getCurrentDriver());
			byte[] screenshot = scrShot.getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
			System.out.println("screenshot taken");
		}
	}

	@After
	public void mobiledriverClose() throws InterruptedException {

		MobileDriverSettings.closeDriver();
		System.out.println("App closed");
	}

}
