package framework;

import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverFactory {
	
	private static final String DRIVER_PATH = "./src/main/resources/";
	
	private DriverFactory()
	{
		
	}

	/**
	 * Method that returns the driver for a specific browser
	 * 
	 * @param name - the browser name
	 * 
	 * @return WebDriver - the driver object for the specific browser
	 * 
	 * @throws MalformedURLException
	 */
	public static WebDriver getDriver(String name) throws MalformedURLException
	 {
	     if (!name.equals("chrome"))
	    	throw new RuntimeException(name + " is  not a valid driver name!");
	    	
	     if (isWindows())
	     {
	    	System.out.println("--- Windows operating system ---");
	    	
	    	System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "chromedriver.exe");
	    	return new ChromeDriver(options());
	     }
	    	
	     if (isUnix())
	     {
	    	System.out.println(" --- Linux operating system ---");
	    	
	    	//System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "chromedriver");
		   	return new ChromeDriver(headlessOptions());
		 }
	    	
	    	throw new RuntimeException("unknown operating system!");
   
	  }
	 
	  /**
	   * Method that returns chrome driver options
	   * 
	   * @return ChromeOptions - options to be used for the chrome driver
	   */
	  private static ChromeOptions options()
	  {
	     ChromeOptions options = new ChromeOptions();
	     options.addArguments("--window-size=1280,800");

	     return options;
	  }

	  private static ChromeOptions headlessOptions()
	  {
	     ChromeOptions options = new ChromeOptions();
	     
	     options.addArguments("--window-size=1280,800");
	     options.addArguments("--disable-gpu");
	     options.addArguments("--disable-setuid-sandbox");
	     options.addArguments("--no-sandbox");
	     options.addArguments("--headless");
	     options.addArguments("--verbose");
	     options.addArguments("--whitelisted-ips=");
	     options.addArguments("--disable-extensions");

	     return options;
	  }
	  
	  private static boolean isWindows() {

		 String os = OS();
		 return (os.indexOf("win") >= 0);

	  }

	  private static boolean isUnix() {

		 String os = OS();
		 return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );

	  }
	  
	  private static String OS()
	  {
		  return System.getProperty("os.name").toLowerCase();
	  }

}
