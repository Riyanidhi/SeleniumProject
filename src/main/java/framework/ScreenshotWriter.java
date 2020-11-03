package framework;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class ScreenshotWriter {
 
  private String folderPath = "./target/screenshots/";
 
  public ScreenshotWriter()
  { 
  }

  /**
   * Method that writes the screenshot info to the file.
   * 
   * The file is saved in the target/screenshots folder.
   * If the screenshot folder does not exist, it is created.
   * 
   * @param info - array with the screenshot info
   * @param testName - the test name
   */
  public void writeToFile(byte[] screenshotInfo, String testName) 
  { 
	  File screenshotsFolder = new File(folderPath);
		 
      if (!screenshotsFolder.exists())
    	  screenshotsFolder.mkdir();
		 
	  try { 		   
		  FileOutputStream file = new FileOutputStream(screenshotName(testName));
		  file.write(screenshotInfo);
		  file.close(); 
    } 
    catch (Exception ex) {
          throw new IllegalStateException("cannot create screenshot!", ex);
    }
 
  }
  
  /**
   * Method that generates the screenshot name based on the test name and the "now" value.
   * 
   * Special characters are removed from "now" value before using "now" for the screenshot name.
   * 
   * @param testName - test name
   * @return String - the screenshot name
   */
  private String screenshotName(String testName)
  {
	  String now = LocalDateTime.now().toString();
      
	  now = now.replace(":", "_")
			   .replace(";", "_")
			   .replace(".", "_");

	  return folderPath + testName + now + ".png";
  }

}

