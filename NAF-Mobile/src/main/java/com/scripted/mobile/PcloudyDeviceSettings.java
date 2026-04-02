package com.scripted.mobile;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableMap;
//import com.ssts.pcloudy.Connector;
//import com.ssts.pcloudy.Version;
//import com.ssts.pcloudy.appium.PCloudyAppiumSession;
//import com.ssts.pcloudy.dto.appium.booking.BookingDtoDevice;
//import com.ssts.pcloudy.dto.device.MobileDevice;
//import com.ssts.pcloudy.dto.file.PDriveFileDTO;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class PcloudyDeviceSettings {

	public static Logger LOGGER = Logger.getLogger(PcloudyDeviceSettings.class);
	public static AndroidDriver<WebElement> androidDriver = null;

	public static IOSDriver<WebElement> iosDriver = null;
	//public static PCloudyAppiumSession pCloudySession;
	DesiredCapabilities capability = null;

	/*
	 * public static ThreadLocal<AndroidDriver<WebElement>> thDriver = new
	 * ThreadLocal<AndroidDriver<WebElement>>();
	 * 
	 * public static ThreadLocal<AndroidDriver<WebElement>> getThDriver() { return
	 * thDriver; }
	 * 
	 * public static void setThDriver(ThreadLocal<AndroidDriver<WebElement>>
	 * thDriver) { PcloudyDeviceSettings.thDriver = thDriver; }
	 */

	public static ThreadLocal<AppiumDriver<WebElement>> thDriver = new ThreadLocal<AppiumDriver<WebElement>>();
	public PcloudyDeviceSettings() {
		this.capability = new DesiredCapabilities();
	}

	public AndroidDriver<WebElement> getConnectionPcloudyAndroidNativeApp(Properties mobConfigProp) {
		try {

			capability.setCapability("pCloudy_Username", "rbalu@smartenspaces.com");
			capability.setCapability("pCloudy_ApiKey", "bfw53hxybmfqf9g445yqzs6c");
			capability.setCapability("pCloudy_DurationInMinutes", 20);
			capability.setCapability("newCommandTimeout", 600);
			capability.setCapability("launchTimeout", 90000);
			//capability.setCapability("pCloudy_DeviceFullName", "ONEPLUS_Nord_Android_10.0.0_a1ff3");
			//capability.setCapability("platformVersion", "10.0.0");
			capability.setCapability("platformName", "Android");
			capability.setCapability("automationName", "uiautomator2");
			capability.setCapability("pCloudy_ApplicationName", mobConfigProp.getProperty("appName"));
			capability.setCapability("appActivity", mobConfigProp.getProperty("appActivity"));
			capability.setCapability("appPackage", mobConfigProp.getProperty("appPackage"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			/*
			 * androidDriver = new AndroidDriver<WebElement>(new
			 * URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capability);
			 * androidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			 */
			thDriver.set(new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
               		capability));
             System.out.println("Thread driver is"+thDriver+'\n');
             MobileDriverSettings.getCurrentDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return androidDriver;
	}

	public AndroidDriver<WebElement> getConnectionPcloudyAndroidWeb(Properties mobConfigProp) {
		try {
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			// capability.setCapability("pCloudy_ApplicationName",
			// mobConfigProp.getProperty("appName"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
			capability.setCapability("appPackage", "");
			capability.setCapability("appActivity", "");
			capability.setCapability("pCloudy_WildNet", "false");
			capability.setBrowserName("Chrome");
			/*
			 * androidDriver = new AndroidDriver<WebElement>(new
			 * URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capability);
			 */
			thDriver.set(new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
               		capability));
             System.out.println("Thread driver is"+thDriver+'\n');  
             MobileDriverSettings.getCurrentDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return androidDriver;
	}

	/*
	 * public ThreadLocal<AndroidDriver<WebElement>>
	 * getConnectionPcloudyAndroidWebTh(String mobConfigPropf) { Properties
	 * mobConfigProp = new Properties(); try { System.out.println(mobConfigPropf);
	 * FileReader reader = new FileReader(
	 * "src/main/resources/MobileConfigurations/" + mobConfigPropf + ".properties");
	 * mobConfigProp.load(reader); capability.setCapability("pCloudy_Username",
	 * mobConfigProp.getProperty("Username"));
	 * capability.setCapability("pCloudy_ApiKey",
	 * mobConfigProp.getProperty("ApiKey"));
	 * capability.setCapability("pCloudy_DurationInMinutes",
	 * mobConfigProp.getProperty("DurationInMinutes"));
	 * capability.setCapability("pCloudy_DeviceManafacturer",
	 * mobConfigProp.getProperty("DeviceManafacturer"));
	 * capability.setCapability("pCloudy_DeviceVersion",
	 * mobConfigProp.getProperty("DeviceVersion"));
	 * capability.setCapability("newCommandTimeout",
	 * mobConfigProp.getProperty("newCommandTimeout"));
	 * capability.setCapability("launchTimeout",
	 * mobConfigProp.getProperty("launchTimeout"));
	 * capability.setCapability("pCloudy_DeviceFullName",
	 * mobConfigProp.getProperty("DeviceFullName"));
	 * capability.setCapability("automationName", "UiAutomator2");
	 * capability.setBrowserName("Chrome"); thDriver.set(new
	 * AndroidDriver<WebElement>(new
	 * URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capability)); } catch
	 * (Exception e) { e.printStackTrace(); } return thDriver; }
	 */

	public IOSDriver<WebElement> getConnectionPcloudyIOSNativeApp(Properties mobConfigProp) {
		try {
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DeviceManafacturer", "APPLE");
			capability.setCapability("platformVersion", mobConfigProp.getProperty("platformVersion"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("platformName", "ios");
			capability.setCapability("acceptAlerts", true);
			capability.setCapability("automationName", "XCUITest");
			capability.setCapability("pCloudy_ApplicationName", mobConfigProp.getProperty("appName"));
			capability.setCapability("bundleId", mobConfigProp.getProperty("bundleId"));
			capability.setCapability("pCloudy_EnableVideo",true);
			capability.setCapability("pCloudy_EnableDeviceLogs", true);
			capability.setCapability("pCloudy_EnablePerformanceData", true);

			/*
			 * iosDriver = new IOSDriver<WebElement>(new
			 * URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capability);
			 * iosDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			 * System.out.println("ios driver initialized");
			 */
			 thDriver.set(new IOSDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
	               		capability));
	             System.out.println("Thread driver is"+thDriver+'\n');
	             
	             MobileDriverSettings.getCurrentDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iosDriver;
	}

	public IOSDriver<WebElement> getConnectionPcloudyIOSWeb(Properties mobConfigProp) {
		try {
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DeviceManafacturer", "APPLE");
			capability.setCapability("platformVersion", mobConfigProp.getProperty("platformVersion"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("platformName", "ios");
			capability.setCapability("acceptAlerts", true);
			capability.setCapability("automationName", "XCUITest");
			capability.setBrowserName(mobConfigProp.getProperty("browserName"));
			capability.setCapability("pCloudy_WildNet", "false");
			/*
			 * iosDriver = new IOSDriver<WebElement>(new
			 * URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capability);
			 * iosDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			 */
			
			thDriver.set(new IOSDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
               		capability));
             System.out.println("Thread driver is"+thDriver+'\n');
            
             MobileDriverSettings.getCurrentDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iosDriver;
	}

//	public static PCloudyAppiumSession getAppiumPCloudySession() {
//		return pCloudySession;
//	}

	public DesiredCapabilities getPcloudyCapabilities() {
		return capability;
	}

}
