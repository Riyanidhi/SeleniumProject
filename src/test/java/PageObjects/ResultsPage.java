package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.LogWriter;

public class ResultsPage {
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	private final LogWriter traceLogWriter;
	
	private static final By PAGINATION_TEXT_XPATH = By.xpath("//span[@data-key = 'pagination-text']");
	private static final By NEXT_PAGE_XPATH       = By.xpath("//a[@data-key = 'next-btn']");
	
	private static final String PAGE_XPATH        = "//a[@data-page = '%d']";
	
	public ResultsPage(WebDriver driver)
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 30);
		
		this.traceLogWriter = new LogWriter("./target/logs", "trace.log");
	}
	
	
	/**
	 * Method that checks if results page is displayed.
	 * 
	 * Results page is displayed if the page url contains bibliocommons.com.
	 * 
	 * @return boolean - It returns true if results page is displayed.
	 */
	public boolean isDisplayed()
	{
		 boolean result = wait.until(d -> d.getCurrentUrl().contains("bibliocommons.com"));

		 this.traceLogWriter.writeToLog("ResultsPage - isDisplayed()");

		 return result;		 
	}

	
	/**
	 * Method that returns the total results count.
	 * 
	 * From "1 to 10 of 1000 results", total count is 1000.
	 * 
	 * Total Count is extracted based on indexes of "of" and "results".
	 * 
	 * @return int - It returns the value of the total results count.
	 */
	public int totalCount()
	{
		wait.until(d -> d.findElement(PAGINATION_TEXT_XPATH).isDisplayed());		
		WebElement countElement = driver.findElement(PAGINATION_TEXT_XPATH);
		
		String countText = countElement.getText();
		
		int i = countText.indexOf("of") + 3;
		int j = countText.indexOf("results") - 1;
		countText = countText.substring(i,  j);
		
		int count = Integer.parseInt(countText);
		
		this.traceLogWriter.writeToLog("ResultsPage - get TotalCount()");
		
		return count;
	}
	
	/**
	 * Method that returns the results per page value.
	 * 
	 * From "1 to 10 of 1000 results", results per page is "1 to 10".
	 * 
	 * Results per page is extracted based on the index of "of".
	 * 
	 * @return String - It returns the value of the results per page.
	 */
	public String resultsPerPage()
	{
		wait.until(d -> d.findElement(PAGINATION_TEXT_XPATH).isDisplayed());		
		WebElement countElement = driver.findElement(PAGINATION_TEXT_XPATH);
		
		String countText = countElement.getText();
		
		int i = countText.indexOf("of") - 1;
		countText = countText.substring(0, i);
		
		this.traceLogWriter.writeToLog("ResultsPage() - get ResultsPerPage()");
		
		return countText;
	}
	
	/**
	 * Method that goes to a page by clicking the page link
	 * 
	 * Steps:
	 * 
	 * 1. creates the locator for the page link
	 * 2. finds the page link after waiting for the page link to be displayed
	 * 3. clicks the page link 
	 * 4. determines the expected results per page for the next page
	 * 5. waits until the next results page displays the expected results per page 
	 * 
	 * @return ResultsPage - It returns the results page that corresponds to new page.
	 * 
	 * @param pageNumber - the page number
	 */
	public ResultsPage goToPage(int pageNumber)
	{
		if (pageNumber <= 0)
			throw new InvalidArgumentException("pageNumber should be > 0");
		
		By pageLocator = By.xpath(String.format(PAGE_XPATH, pageNumber));
		
		wait.until(d -> d.findElement(pageLocator).isDisplayed());		
		WebElement page = driver.findElement(pageLocator);
		page.click();

		this.traceLogWriter.writeToLog("ResultsPage - goToPage()");
		
		ResultsPage nextPage = goToResultsPage();
		
		String expectedResultsPerPage = resultsPerPage(pageNumber);
		nextPage.waitUntilResultsPerPageIs(expectedResultsPerPage);		
		
		return nextPage;
	}
	
	
	/**
	 * Method that goes to the next page.
	 * 
	 * Steps:
	 * 
	 * 1. finds the next page link after waiting for the next page link to be displayed
	 * 2. click the next page link 
	 * 3. gets the page number of the next results page
	 * 4. determines the expected results per page
	 * 5. waits until the next results page displays the correct results per page 
	 * 
	 * @return ResultsPage - It returns the results page that corresponds to new page.
	 */
	public ResultsPage goToNextPage()
	{
		
		wait.until(d -> d.findElement(NEXT_PAGE_XPATH).isDisplayed());		
		WebElement page = driver.findElement(NEXT_PAGE_XPATH);
		page.click();

		this.traceLogWriter.writeToLog("ResultsPage - goToNextPage()");
		
		ResultsPage nextResultsPage = goToResultsPage();		
		
		String expectedResultsPerPage = resultsPerPage(pageNumber());
		nextResultsPage.waitUntilResultsPerPageIs(expectedResultsPerPage);
						
		return nextResultsPage;
	}
	
	/**
	 * Method that checks if the results page is displayed.
	 * 
	 * @return ResultsPage It returns the object for the next Results Page.
	 */
	private ResultsPage goToResultsPage()
    {
    	ResultsPage resultsPage = new ResultsPage(driver);    	

    	if (!resultsPage.isDisplayed())
    		throw new ElementNotVisibleException("Results Page is not displayed!");	    
    	
    	return resultsPage;
    }
	
	/**
	 * Method that waits until the results per page has the expected value.
	 * 
	 * @param resultsPerPage - the expected results per page 
	 */
	private void waitUntilResultsPerPageIs(String resultsPerPage)
	{
		wait.until(d -> d.findElement(PAGINATION_TEXT_XPATH)
					.getText()
					.contains(resultsPerPage));
	}

	/**
	 * Method that returns the expected results per page for a specific page number
	 * 
	 * @param pageNumber - page number
	 * 
	 * @return String - It returns the expected results per page
	 */
	private String resultsPerPage(int pageNumber)
	{
		int from = (pageNumber - 1) * 10 + 1;
		int to = pageNumber * 10;
		
		return String.format("%d to %d", from, to);
	}
	
	/**
	 * Method that returns the page number.
	 * 
	 * If the page url does not include "page", the page number is 1.
	 * Otherwise, the page number is extracted from the url.
	 * 
	 * @return int - the page number
	 */
	public int pageNumber()
	{
		String url = driver.getCurrentUrl();
		
		if (!url.contains("page"))
			return 1;
		
		int i = url.indexOf("page=") + 5;
		
		String pageValue = url.substring(i);
		
		return Integer.parseInt(pageValue);
	}
	
}
