package Tests;

import java.lang.reflect.Field;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import framework.LogWriter;
import framework.ScreenshotWriter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/*
   the test listener captures in the OnTestFailure() method the    
   screenshot of the current page both 
   — when an exception happens in a test script
   — when a test script fails because of an assertion
   none of the other listener methods are implemented.
 */
public class Listener implements ITestListener {
 
	private LogWriter traceWriter;
	private LogWriter exceptionsWriter;
	
    public Listener()
    {
	   this.exceptionsWriter = new LogWriter("./target/logs", "exceptions.log");
	   this.traceWriter      = new LogWriter("./target/logs", "trace.log");
    }
	
    @Override
    public void onTestStart(ITestResult r) 
    {  
	  
	  this.traceWriter.writeToLog("\n\n Test Name: " + r.getName() + "\n\n");
  
    }
 
    @Override
    public void onTestSuccess(ITestResult result) 
    {
	  
	   saveScreenshot(result, "passed_" + result.getName());   
  
    }
 
    /*
   		the driver parameter of Screenshot constructor is provided by the 
   		driver() method;
 
   		testResult.getName() returns the name of the test script being 
   		executed;
 
   		we pass this as parameter to capture() so that the screenshot is 
   		named after the test script;
    */
    @Override
    public void onTestFailure(ITestResult result) 
    { 
    
    	saveScreenshot(result, "failed_" + result.getName());
    	
        saveExceptionInfo(result);
        
    }	
  
    private void saveScreenshot(ITestResult result, String testName)
    {
	    WebDriver driver = driver(result);    	
  	    byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
  	
  	    ScreenshotWriter writer = new ScreenshotWriter();    	
        writer.writeToFile(screenshot, testName);
    }
  
    private void saveExceptionInfo(ITestResult result)
    {
	    String testName = result.getName();
        String exceptionName = result.getThrowable().getMessage();
      
        String stackTrace = "";
      
        StackTraceElement[] stackElements = result.getThrowable().getStackTrace();
      
        for (int i = 0; i < stackElements.length - 1; i++)
      	  stackTrace = stackTrace + stackElements[i].toString() + "\n";        	
      
        this.exceptionsWriter.writeToLog(String.format("test name = %s\n exception = %s\n stack trace = %s\n",
      	  	                                  testName,
      		                                  exceptionName,
      		                                  stackTrace));
    }
  
    @SuppressWarnings("unchecked")
    private WebDriver driver(ITestResult result)      
    {
  
    	try {
 
    		/*
       			we use reflection and generics to find the driver object:
 
       			1. testResult.getInstance() 
          			returns the running test class object
 
       			2. testResult.getInstance().getClass() 
          			returns the class of testResult (TestClass)
 
       			3. testClass.getSuperclass() 
          			returns the parent of the test class (BaseTestClass)
 
       			4. Field driverField = baseTestClass.getDeclaredField(“driver”)
          			returns the driver field of the BaseTestClass
   
       			5. driver = (WebDriver)driverField.get(testResult.getInstance());
          			gets the value of the driver field from the BaseTestClass
    		*/
    		
    		Class<?extends ITestResult> testClass = (Class<? extends ITestResult>)result.getInstance()
    		                                                                      .getClass();      
      
    		Class<?extends ITestResult> baseClass = (Class<? extends ITestResult>)testClass.getSuperclass();
      
    		Field field = baseClass.getDeclaredField("driver");      
    		WebDriver driver = (WebDriver)field.get(result.getInstance());
      
    		return driver;
      
    	}  
    	catch (SecurityException | 
    			NoSuchFieldException | 
    			IllegalAccessException |
    			IllegalArgumentException e) 
    	{
    	
    		throw new IllegalStateException("could not get the driver");
      
    	}
 
    }
  
    @Override
    public void onTestSkipped(ITestResult result) 
    {
    }
  
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult r) 
    {
    }
  
    @Override
    public void onStart(ITestContext context) 
    {
    }
  
    @Override
    public void onFinish(ITestContext c) 
    {
    }
  
}