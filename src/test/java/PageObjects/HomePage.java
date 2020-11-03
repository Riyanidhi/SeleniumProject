package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.LogWriter;

public class HomePage {

    private static final By SEARCH_BOX_ID    = By.id("edit-search");
    private static final By SEARCH_BUTTON_ID = By.id("edit-submit");

    private final WebDriver driver;
    private final WebDriverWait wait;
    
    private final LogWriter traceLogWriter;
    
    public HomePage(WebDriver driver)
    {
    	this.driver = driver;
    	this.wait = new WebDriverWait(driver, 30);
    	
    	this.traceLogWriter = new LogWriter("./target/logs", "trace.log");
    }

    /**
     * Method that searches for a keyword.
     * 
     * It types the keyword in the search box.
     * It executes the search by clicking the search button.
     * 
     * @param keyword - the keyword to search for
     * 
     * @return ResultsPage - object of the ResultsPage displayed after the search is completed.
     */
    public ResultsPage searchFor(String keyword)
    {
    	typeKeyword(keyword);
    	executeSearch();
    	
    	this.traceLogWriter.writeToLog("HomePage - searchFor()");
    	    	    	
    	return goToResultsPage();
    }
    
    /**
     * Method that checks if the Results Page is displayed.
     * 
     * @return ResultsPage - object for the next Results Page to be displayed
     */
    private ResultsPage goToResultsPage()
    {
    	ResultsPage resultsPage = new ResultsPage(driver);

    	this.traceLogWriter.writeToLog("HomePage - goToResultsPage()");

    	if (!resultsPage.isDisplayed())
    		throw new ElementNotVisibleException("Results Page is not displayed!");	    
    	
    	return resultsPage;
    }
    
    /**
     * Method that types a keyword in the search box.
     * 
     * @param keyword - keyword to be searched for
     */
	private void typeKeyword(String keyword)
	{
	    wait.until(d -> d.findElement(SEARCH_BOX_ID).isEnabled());
	            
	    WebElement searchBox = driver.findElement(SEARCH_BOX_ID);
	    searchBox.sendKeys(keyword);
	}
	 
	/**
	 * Method that executes the search by clicking the search button
	 */
	private void executeSearch()
	{
	    wait.until(d -> d.findElement(SEARCH_BUTTON_ID).isEnabled());

	    WebElement searchButton = driver.findElement(SEARCH_BUTTON_ID);
	    searchButton.click();
	}

}
