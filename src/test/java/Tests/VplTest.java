package Tests;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.Assert;
import PageObjects.HomePage;
import PageObjects.ResultsPage;

@Listeners(Listener.class)
public class VplTest extends BaseTest {	
	
	private static final String KEYWORD = "java";
	
	@Test(groups = "High")
	public void keywordSearchReturnsResults_Test() {
		
	    HomePage homePage = openHomePage();

	    ResultsPage resultsPage = homePage.searchFor(KEYWORD);
	    
	    Assert.assertTrue(resultsPage.totalCount() > 0, 
	    		          "total results count is not positive!");
	
	}

	
	@Test(groups = "Medium")
	public void anyPageHasResults_Test() 
	{
		
	    HomePage homePage = openHomePage();

	    ResultsPage resultsPage1 = homePage.searchFor(KEYWORD);	    	    	    
	    Assert.assertEquals(resultsPage1.resultsPerPage(), "1 to 10");
	    
	    ResultsPage resultsPage3 = resultsPage1.goToPage(3);	    
	    Assert.assertEquals(resultsPage3.resultsPerPage(), "21 to 30");

	    ResultsPage resultsPage5 = resultsPage3.goToPage(5);	    
	    Assert.assertEquals(resultsPage5.resultsPerPage(), "41 to 50");

	}

	
	@Test(groups = "Low")
	public void nextPageHasResults_Test() 
	{
		
	    HomePage homePage = openHomePage();

	    ResultsPage resultsPage1 = homePage.searchFor(KEYWORD);	    
	    Assert.assertEquals(resultsPage1.resultsPerPage(), "1 to 10");
	    
	    ResultsPage resultsPage2 = resultsPage1.goToNextPage();	    
	    Assert.assertEquals(resultsPage2.resultsPerPage(), "11 to 20");
	    
	    ResultsPage resultsPage3 = resultsPage2.goToNextPage();	    
	    Assert.assertEquals(resultsPage3.resultsPerPage(), "21 to 30");
	
	}
	    
}
