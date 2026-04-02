package com.scripted.web;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.scripted.dataload.PropertyDriver;

import com.scripted.utils.WebCommonFunctions;

import io.github.bonigarcia.wdm.WebDriverManager;



public class BrowserDriver {
	public static String BrowserDriver = null;
	public static WebDriver driver = null;
	public static ThreadLocal<RemoteWebDriver> thDriver = new ThreadLocal<RemoteWebDriver>();
	public static ThreadLocal<WebDriver> lthDriver = new ThreadLocal<>();

	

	public static String strBrowserName = null;
	public static String strBrowserVersion = null;
	public static boolean strenableVideo;
	public static boolean strrecordVideo;
	public static boolean strenableVNC;
	public static String strhostURL = null;
	public static String strBrowserNameAndVersion = null;
	public static String strApplicationURL = null;
	// private static final Logger log = Logger.getLogger(BrowserDriver.class);
	public static Logger LOGGER = Logger.getLogger(BrowserDriver.class);
	
	
	
	
	public static WebDriver funcGetWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				//p.
				p.setPropFilePath("src/main/resources/properties/browserconfig.properties");
				strBrowserName = p.readProp("browserName");
				
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();
				System.out.println("Browser Name is:"+strBrowserName);

				switch (strBrowserName) {

				case "chrome":

//					ChromeSettings chromeSettings = new ChromeSettings();
//					driver = new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath()));
					 WebDriverManager.chromedriver().setup();
					 driver=new ChromeDriver();


					break;

				case "ie":
					IExplorerSettings iExplorerSettings = new IExplorerSettings();
					driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
					break;
					
				case "chromeheadless":
                	ChromeOptions options = new ChromeOptions();
					options.addArguments("headless");
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "src/main/resources/Drivers/chromedriver");
					driver = new ChromeDriver(options);
					break;

				/*case "firefox":
					FireFoxSettings fireFoxSettings = new FireFoxSettings();
					driver = new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath()));
					break;

				case "phantom":

					PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
					driver = new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath()));
					break;*/
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}
	public static WebDriver funcGetNgWebdriver() {
			try {
				PropertyDriver p = new PropertyDriver();
				if (driver == null) {
					p.setPropFilePath("src/main/resources/properties/browserconfig.properties");
					strBrowserName = p.readProp("browserName");
				}
				if (strBrowserName == null || strBrowserName.equals(" ")) {
					LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
					System.exit(0);
				} else {
					LOGGER.info("Browser : " + strBrowserName);
					strBrowserName = strBrowserName.toLowerCase();

					switch (strBrowserName) {

					case "chrome":
						ChromeSettings chromeSettings = new ChromeSettings();
						driver = new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath()));
						break;

					case "ie":
						IExplorerSettings iExplorerSettings = new IExplorerSettings();
						driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
						break;

					/*case "firefox":
						FireFoxSettings fireFoxSettings = new FireFoxSettings();
						driver = new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath()));
						break;

					case "phantom":
						PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
						driver = new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath()));
						break;*/
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
				Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
			}

			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			JavascriptExecutor jsdriver=(JavascriptExecutor)driver;
	        NgWebDriver ngdriver=new NgWebDriver(jsdriver);
			return driver;
	}
	
	public static void launchWebURL(String strApplicationURL) {
		try {
			
			//Map<String, List<String>> excelData = WebCommonFunctions.getXlListForDashboard("ApplicationURL");
			//PropertyDriver p = new PropertyDriver();
			//p.setPropFilePath("src/main/resources/properties/applicationURL.properties");
			//strApplicationURL = p.readProp("URL");
			getDriver().get(strApplicationURL);
			pageWait();
			LOGGER.info("Application launched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while launching Web URL" + "Exception :" + e);
			Assert.fail("Error occurred while launching Web URL" + "Exception :" + e);
		}
	}
	
	public static void getApplicationURL() {
		
		
	}


	public static void closeBrowser() {
		try {
			getDriver().close();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while closing browser" + "Exception :" + e);
			Assert.fail("Error occurred while closing browser" + "Exception :" + e);
		}
	}

	public static void quitBrowser() {
		try {
			getDriver().quit();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while quit browser" + "Exception :" + e);
			Assert.fail("Error occurred while quit browser" + "Exception :" + e);
		}
	}

	public static WebDriver getDriver() {
		if (driver == null) {
			if (thDriver.get() != null) {
				WebDriver rmDriver = thDriver.get();
				return rmDriver;
			} else {
				WebDriver lDriver = lthDriver.get();
				return lDriver;
			}
		} else {
			return driver;
		}
	}

	public static void pageWait() {
		getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	
	public static void getSeleniunGridDriver(String browser) {
		try {
			URL url = null;
			LOGGER.info("Browser : " + browser);
			// LOGGER.info("Version : " + version);
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/properties/seleniumGridConfig.properties");
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.addArguments("--start-maximized");
				cCaps.setBrowserName(browser);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, cCaps));
				break;

			case "internet explorer":
				DesiredCapabilities iCaps = new DesiredCapabilities();
				iCaps = DesiredCapabilities.internetExplorer();
				iCaps.setCapability(CapabilityType.BROWSER_NAME, browser);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, iCaps));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fCaps.setBrowserName(browser);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, fCaps));
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				OperaOptions oOptions = new OperaOptions();
				oCaps.setBrowserName(browser);
				oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, oCaps));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void getCuPalDriver() {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/properties/browserconfig.properties");
			String browser = p.readProp("browserName");
			LOGGER.info("Browser : " + browser);

			switch (browser) {
			case "chrome":
				ChromeSettings chromeSettings = new ChromeSettings();
				lthDriver.set(new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			case "internet explorer":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				lthDriver.set(new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			/*case "firefox":
				FireFoxSettings fireFoxSettings = new FireFoxSettings();
				lthDriver.set(new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			case "phantom":
				PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
				lthDriver.set(new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPopup(String message) {
		return "var infoSpan = document.createElement('div');\r\n" + 
					"infoSpan.id = 'infoSpan';\r\n" + 
					"infoSpan.innerHTML = '"+message+"';\r\n" + 
					"var style = document.createElement('style');\r\n" + 
					"style.innerHTML = '#infoSpan {font-family: Arial;font-size: larger;top: 1px;position: absolute;color: #ffffff;background-color: #ff0000;padding: 20px;;width: 1400px;height: 50px;z-index:2000;}';\r\n" + 
					"document.head.appendChild(style);\r\n" + 
					"document.body.appendChild(infoSpan);\r\n";
		}
	
	public static void launchWebURL_Auth(String strURL, String userName, String password) {
        try {
               strURL = createAuthUrl(strURL, userName, password);
               getDriver().get(strURL);
               pageWait();
               if (getDriver().getCurrentUrl() != strURL) {
                     getDriver().get(strURL);
               }
        } catch (Exception e) {
               e.printStackTrace();
               LOGGER.error("Error occurred while launching Web URL" + "Exception :" + e);
               Assert.fail("Error occurred while launching Web URL" + "Exception :" + e);
        }
  }

  public static String createAuthUrl(String url, String usr, String pwd) throws Exception {
        int p1 = url.indexOf("://");
        String http = "http://";
        String site = "";
        if (p1 != -1) {
               http = url.substring(0, p1) + "://";
               site = url.substring(p1 + 3);
        }
        //return http + usr + ":" + GenericUtils.decryptPass(pwd) + "@" + site;
        return http + usr + ":" + pwd + "@" + site;
  }



}
