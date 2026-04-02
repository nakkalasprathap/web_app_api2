package com.scripted.mobile;

import java.io.FileReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.scripted.dataload.PropertyDriver;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import com.scripted.mobile.*;

public class MobileDriverSettings {

	public static Logger LOGGER = Logger.getLogger(MobileDriverSettings.class);
	public static AndroidDriver<WebElement> androidDriver = null;
	public static IOSDriver<WebElement> iosDriver = null;
	private static AndroidDeviceSettings androidSettings = null;
	private static IOSDeviceSettings iOSSettings = null;
	private static PcloudyDeviceSettings pcloudyDeviceSettings = null;
	
	MobileDriverSettings base = new MobileDriverSettings();

//	private static final Logger log = Logger.getLogger(MobileDriverSettings.class);

	public static AndroidDriver<WebElement> funcGetNativeAndroiddriver(String configFileName) {
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath("src/main/resources/MobileConfigurations/"+configFileName+".properties");
		Map<String, String> androidProperties = propertyDriver.readProp();
		androidProperties.forEach((k, v) -> {
			if (k.equalsIgnoreCase("android.server.ip"))
				getAndroidSettings().setServerIp(v);
			if (k.equalsIgnoreCase("android.server.port"))
				getAndroidSettings().setServerPort(v);
			if (k.equalsIgnoreCase("android.app.path"))
				getAndroidSettings().setAppPath(v);
			if (k.equalsIgnoreCase("android.app.activity"))
				getAndroidSettings().setAppActivity(v);
			if (k.equalsIgnoreCase("android.app.package"))
				getAndroidSettings().setAppPackage(v);
			if (k.equalsIgnoreCase("android.deviceName"))
				getAndroidSettings().setDeviceName(v);
			if (k.equalsIgnoreCase("android.deviceID"))
				getAndroidSettings().setDeviceID(v);
			if (k.equalsIgnoreCase("android.emulator.name"))
				getAndroidSettings().setEmulator(v);
			if (k.equalsIgnoreCase("android.platformVersion"))
				getAndroidSettings().setAndroidVersion(v);
		});
		getAndroidSettings().setBrowserName("");
		AppiumSettings.startAppiumServer(androidProperties);
		AndroidUtil androidDriver = new AndroidUtil(androidSettings.getHubUrl().toString(), androidSettings);
		setAndroidDriver(androidDriver.getAndroidDriver());
		androidDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return getAndroidDriver();
	}

	public static AndroidDriver<WebElement> funcGetWebAndroidDriver(String configFileName) {
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath("src/main/resources/MobileConfigurations/"+configFileName+".properties");
		Map<String, String> androidProperties = propertyDriver.readProp();
		androidProperties.forEach((k, v) -> {
			if (k.equalsIgnoreCase("android.deviceName"))
				getAndroidSettings().setDeviceName(v);
			if (k.equalsIgnoreCase("android.deviceID"))
				getAndroidSettings().setDeviceID(v);
			if (k.equalsIgnoreCase("android.platformVersion"))
				getAndroidSettings().setAndroidVersion(v);
			if (k.equalsIgnoreCase("android.browser.name"))
				getAndroidSettings().setBrowserName(v);
		});
		getAndroidSettings().setAppActivity("");
		getAndroidSettings().setAppPackage("");
		AppiumSettings.startAppiumServer(androidProperties);
		AndroidUtil androidDriver = new AndroidUtil(androidSettings.getHubUrl().toString(), androidSettings);
		setAndroidDriver(androidDriver.getAndroidDriver());
		androidDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		androidDriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		androidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return getAndroidDriver();
	}

	/*
	 * public static AndroidDriver<WebElement>
	 * funcGetpCloudyNativeAndroiddriver(String configFileName) { Properties
	 * mobConfigReader = new Properties(); try { FileReader reader = new
	 * FileReader("src/main/resources/MobileConfigurations/"+configFileName+
	 * ".properties"); mobConfigReader.load(reader); androidDriver =
	 * getPcloudyDeviceSettings().getConnectionPcloudyAndroidNativeApp(
	 * mobConfigReader); } catch (Exception e) { e.printStackTrace();
	 * LOGGER.error("Error while reading pCloudy Android native Apps Config file"
	 * +"Exception :"+e); }
	 * 
	 * return androidDriver; }
	 * 
	 * 
	 * 
	 * public static AndroidDriver<WebElement> funcGetpCloudyWebAndroiddriver(String
	 * configFileName) { Properties mobConfigReader = new Properties(); try {
	 * FileReader reader = new
	 * FileReader("src/main/resources/MobileConfigurations/"+configFileName+
	 * ".properties"); mobConfigReader.load(reader); androidDriver =
	 * getPcloudyDeviceSettings().getConnectionPcloudyAndroidWeb(mobConfigReader); }
	 * catch (Exception e) { e.printStackTrace();
	 * LOGGER.error("Error while reading pCloudy Android web Apps Config file"
	 * +"Exception :"+e); }
	 * 
	 * return androidDriver; }
	 */
	
	
	public static MobileDriver<WebElement> funcGetpCloudyNativeAndroiddriver(String configFileName) {
		Properties mobConfigReader = new Properties();
		try {
			FileReader reader = new FileReader(
					"src/main/resources/MobileConfigurations/" + configFileName + ".properties");
			mobConfigReader.load(reader);
			if (PcloudyDeviceSettings.thDriver.get() == null) {
	
			getPcloudyDeviceSettings().getConnectionPcloudyAndroidNativeApp(mobConfigReader);
			System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());
			} else {
				MobileDriverSettings.getCurrentDriver().launchApp();
				System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());

							}
			} 
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while reading pCloudy Android native Apps Config file" + "Exception :" + e);
		}

		return androidDriver;
	}
	
	public static MobileDriver<WebElement> funcGetpCloudyWebAndroiddriver(String configFileName) {
		Properties mobConfigReader = new Properties();
		try {
			FileReader reader = new FileReader(
					"src/main/resources/MobileConfigurations/" + configFileName + ".properties");
			mobConfigReader.load(reader);
			if (PcloudyDeviceSettings.thDriver.get() == null) {
	
			getPcloudyDeviceSettings().getConnectionPcloudyAndroidWeb(mobConfigReader);
			System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());
			} else {
				MobileDriverSettings.getCurrentDriver().launchApp();
				System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());

							}
			} 
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while reading pCloudy Android native Apps Config file" + "Exception :" + e);
		}

		return androidDriver;
	}
	

	public static IOSDriver<WebElement> funcGetNativeIOSdriver(String configFileName){
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath("src/main/resources/MobileConfigurations/"+configFileName+".properties");
		Map<String, String> iOSProperties = propertyDriver.readProp();
		iOSProperties.forEach((k, v) -> {
			if (k.equalsIgnoreCase("iOS.App_Path"))
				getIOSSettings().setAppPath(v);
			if (k.equalsIgnoreCase("iOS.bundleId"))
				getIOSSettings().setAppBundleId(v);
			if (k.equalsIgnoreCase("iOS.DeviceName"))
				getIOSSettings().setDeviceName(v);
			if (k.equalsIgnoreCase("iOS.DeviceID"))
				getIOSSettings().setDeviceUDID(v);
			if (k.equalsIgnoreCase("iOS.Platform_Version"))
				getIOSSettings().setIOSVersion(v);
		});
		
		getIOSSettings().setBrowserName("");
		AppiumSettings.startAppiumServer(iOSProperties);
		IOSUtil iOSDriver = new IOSUtil(iOSSettings.getHuburl().toString(), iOSSettings);
		setIOSDriver(iOSDriver.getIOSDriver());
		iOSDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return getIOSDriver();
		
	}
	
	public static IOSDriver<WebElement> funcGetWebIOSdriver(String configFileName)
	{
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath("src/main/resources/MobileConfigurations/"+configFileName+".properties");
		Map<String, String> iOSProperties = propertyDriver.readProp();
		iOSProperties.forEach((k, v) -> {
			
			if (k.equalsIgnoreCase("iOS.browserName"))
				getIOSSettings().setBrowserName(v);
			if (k.equalsIgnoreCase("iOS.DeviceName"))
				getIOSSettings().setDeviceName(v);
			if (k.equalsIgnoreCase("iOS.DeviceID"))
				getIOSSettings().setDeviceUDID(v);
			if (k.equalsIgnoreCase("iOS.Platform_Version"))
				getIOSSettings().setIOSVersion(v);
		});
		getIOSSettings().setAppPath("");
		getIOSSettings().setAppBundleId("");
		AppiumSettings.startAppiumServer(iOSProperties);
		IOSUtil iOSDriver = new IOSUtil(iOSSettings.getHuburl().toString(), iOSSettings);
		setIOSDriver(iOSDriver.getIOSDriver());
		iOSDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return getIOSDriver();
		
	}
	
	/*
	 * public static IOSDriver<WebElement> funcGetpCloudyNativeIOSdriver(String
	 * configFileName){ Properties mobConfigReader = new Properties(); try {
	 * FileReader reader = new
	 * FileReader("src/main/resources/MobileConfigurations/"+configFileName+
	 * ".properties"); mobConfigReader.load(reader); iosDriver=
	 * getPcloudyDeviceSettings().getConnectionPcloudyIOSNativeApp(mobConfigReader);
	 * } catch (Exception e) { e.printStackTrace();
	 * LOGGER.error("Error while reading pCloudy IOS native Apps Config file"
	 * +"Exception :"+e); }
	 * 
	 * return getIOSDriver(); }
	 * 
	 * public static IOSDriver<WebElement> funcGetpCloudyWebIOSdriver(String
	 * configFileName){ Properties mobConfigReader = new Properties(); try {
	 * FileReader reader = new
	 * FileReader("src/main/resources/MobileConfigurations/"+configFileName+
	 * ".properties"); mobConfigReader.load(reader); iosDriver=
	 * getPcloudyDeviceSettings().getConnectionPcloudyIOSWeb(mobConfigReader); }
	 * catch (Exception e) { e.printStackTrace();
	 * LOGGER.error("Error while reading pCloudy IOS web Apps Config file"
	 * +"Exception :"+e); }
	 * 
	 * return getIOSDriver(); }
	 */
	
	public static MobileDriver<WebElement> funcGetpCloudyNativeIOSdriver(String configFileName) {
		Properties mobConfigReader = new Properties();
		try {
			FileReader reader = new FileReader(
					"src/main/resources/MobileConfigurations/" + configFileName + ".properties");
			mobConfigReader.load(reader);
			if (PcloudyDeviceSettings.thDriver.get() == null) {
			getPcloudyDeviceSettings().getConnectionPcloudyIOSNativeApp(mobConfigReader);
			System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());
			} else {
				MobileDriverSettings.getCurrentDriver().launchApp();
				System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());
							}
			} 
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while reading pCloudy Android native Apps Config file" + "Exception :" + e);
		}

		return iosDriver;
	}
	
	public static MobileDriver<WebElement> funcGetpCloudyWebIOSdriver(String configFileName) {
		Properties mobConfigReader = new Properties();
		try {
			FileReader reader = new FileReader(
					"src/main/resources/MobileConfigurations/" + configFileName + ".properties");
			mobConfigReader.load(reader);
			if (PcloudyDeviceSettings.thDriver.get() == null) {
			getPcloudyDeviceSettings().getConnectionPcloudyIOSWeb(mobConfigReader);
			System.out.println("ThDriver is "+PcloudyDeviceSettings.thDriver.get());
			} else {
				MobileDriverSettings.getCurrentDriver().launchApp();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while reading pCloudy IOS web Apps Config file" + "Exception :" + e);
		}

		return getIOSDriver();
	}
	
	public static AndroidDriver<WebElement> getAndroidDriver() {
		return androidDriver;
	}

	public static void setAndroidDriver(AndroidDriver<WebElement> currandroidDriver) {
		androidDriver = currandroidDriver;
	}
	
	public static void setIOSDriver(IOSDriver<WebElement> currIOSDriver) {
		iosDriver = currIOSDriver;
	}
	
	public static IOSDriver<WebElement> getIOSDriver(){
		return iosDriver;
		
	}

	public static AndroidDeviceSettings getAndroidSettings() {
		if (androidSettings == null)
			androidSettings = new AndroidDeviceSettings();
		return androidSettings;
	}

	public static PcloudyDeviceSettings getPcloudyDeviceSettings() {
		if (pcloudyDeviceSettings == null)
			pcloudyDeviceSettings = new PcloudyDeviceSettings();
		return pcloudyDeviceSettings;
	}

	public static void launchURL(String url) {
		getCurrentDriver().get(url);
	}

	public static void closeDriver() {
		//getCurrentDriver().closeApp();
		getCurrentDriver().quit();
		//AppiumSettings.stopAppiumServer();
			
	}
	
	
	/*
	 * public static MobileDriver<WebElement> getCurrentDriver(){ if (androidDriver
	 * != null) { return androidDriver; } else if (getIOSDriver() != null) { return
	 * iosDriver; } else if(androidDriver == null) { AndroidDriver<WebElement>
	 * thandroidDriver = PcloudyDeviceSettings.thDriver.get();
	 * System.out.println("----------------------- : "+thandroidDriver); return
	 * thandroidDriver; } return null; }
	 */
	
	public static MobileDriver<WebElement> getCurrentDriver() {
		MobileDriver<WebElement> currDriver = null;
		if (androidDriver != null) {
			currDriver = androidDriver;
		return currDriver;
		}
		else if (getIOSDriver() != null) {
			currDriver = iosDriver;
		return currDriver;
		}
		 else if (PcloudyDeviceSettings.thDriver != null) {
	        	currDriver = PcloudyDeviceSettings.thDriver.get();
	        return currDriver;
		 }
		
		return null;
	}
	

	private static IOSDeviceSettings getIOSSettings() {
		if (iOSSettings == null)
			iOSSettings = new IOSDeviceSettings();
		return iOSSettings;
	}
}
