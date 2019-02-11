package com.test.jenkins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.test.util.ResusableMthods;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestRunner {
	
	static WebDriver driver;
	ExtentReports reports;
	ExtentTest testInfo;
	ExtentHtmlReporter htmlReporter;
	
	@BeforeTest
	public void setup(){
		
		htmlReporter = new ExtentHtmlReporter(new File(System.getProperty("user.dir")+"/AutomationReport.html"));
		htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/extent-config.xml");
		reports = new ExtentReports();
		reports.setSystemInfo("SystemEnvironment", "QA");
		reports.attachReporter(htmlReporter);
		
	}
	
	@Test(priority=1)
	public void testOnChrome(){
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.google.co.in/");
		driver.findElement(By.name("q")).sendKeys("java");
		System.out.println("Test on Chrome is passed!");
		Assert.assertTrue(true);
		testInfo.log(Status.INFO, "Test on Chrome is passed!");
		driver.manage().deleteAllCookies();
		driver.quit();
		
	}

	@Test(priority=2)
	public void testOnFirefox(){
		
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability("marionette", false);
		WebDriver driver = new FirefoxDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.google.co.in/");
		driver.findElement(By.name("q")).sendKeys("Selenium");
		System.out.println("Test on Firefox is passed!");
		Assert.assertTrue(true);
		testInfo.log(Status.INFO, "Test on Firefox is passed!");
		driver.manage().deleteAllCookies();
		driver.quit();
	}
	
	@Test(priority=3)
	public void testOnIE(){
		
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		WebDriverManager.iedriver().setup();
		WebDriver driver = new InternetExplorerDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.google.co.in/");
		driver.findElement(By.name("q")).sendKeys("Cucumber");
		System.out.println("Test on InternerExplorer is failed!");
		Assert.assertTrue(false);
		testInfo.log(Status.INFO, "Test on InternerExplorer is failed!");
		driver.manage().deleteAllCookies();
		driver.quit();
		
	}
	
	@BeforeMethod
	public void register(Method method){
		
		String testName = method.getName();
		testInfo = reports.createTest(testName);
	}
	
	@AfterMethod
	public void captureResult(ITestResult result) throws IOException{
		
		
		if(result.getStatus()==ITestResult.SUCCESS){
			
			testInfo.log(Status.PASS, "The test method named as:"+result.getName()+"is passed");
		}
		else if(result.getStatus()==ITestResult.FAILURE){
			testInfo.addScreenCaptureFromPath(result.getThrowable().getMessage(), ResusableMthods.getScreenshot(driver, result.getName()));
			testInfo.log(Status.FAIL, "The test method named as:"+result.getName()+"is failed");
			testInfo.log(Status.FAIL, "Test case is failed."+result.getThrowable());
			
		}
		else if(result.getStatus()==ITestResult.SKIP){
			
			testInfo.log(Status.SKIP, "The test method named as:"+result.getName()+"is skipped");
		}
	}
	
	@AfterTest
	public void tearDown(){
		reports.flush();
	}
}
