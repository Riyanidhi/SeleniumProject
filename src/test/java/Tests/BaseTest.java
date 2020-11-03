package Tests;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import PageObjects.HomePage;
import framework.DriverFactory;
import framework.LogWriter;

public class BaseTest {

	public WebDriver driver;
	
	private static final String URL = "http://www.vpl.ca";
	
	private LogWriter traceLogWriter = new LogWriter("./target/logs", "trace.log");

	/**
	 * Sets up the environment by getting the driver for a specific browser.
	 * 
	 * The driver is created for a browser that has the name in an environment variable.
	 * 
	 * @throws MalformedURLException
	 */
	@BeforeMethod(alwaysRun = true)
	public void setUp() throws MalformedURLException 
	{	
	
		String browserName = System.getenv("BROWSER");		
		
		System.out.println("browser name = " + browserName);
		
		this.driver        = DriverFactory.getDriver(browserName);
        
	}
	
	/**
	 * Clears up the environment by quitting the driver.
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown() 
	{
		
		driver.quit();
		
	}
	
	/**
	 * Method that opens the home page.
	 * 
	 * After opening the home page, it waits until the home page url includes "vpl.ca".
	 * 
	 * @return HomePage - object of the Home Page
	 */
	public HomePage openHomePage()    
	{
	    driver.navigate().to(URL);

	    WebDriverWait wait = new WebDriverWait(driver, 30);
	    wait.until(d -> d.getCurrentUrl().contains("vpl.ca"));
	    
	    traceLogWriter.writeToLog("Open Home Page");
	    
	    return new HomePage(driver);
	 }

	
}
