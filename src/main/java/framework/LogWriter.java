package framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogWriter {

	 private final String logPath;
	 private final String logName;
	 
	 public LogWriter(String logPath, String logName)
	 {
		this.logPath = logPath;
		this.logName = logName;
	 }

	 /**
	  * Method that writes a text to a log.
	  * 
	  * The log file is created if it does not exist.
	  * 
	  * @param text - text to be written
	  */
	 public void writeToLog(String text)
	 {
		 
		 File logsFolder = new File(logPath);
		 
		 if (!logsFolder.exists())
			 logsFolder.mkdir();
		 
		 try(FileWriter fw = new FileWriter(logPath + "/" + logName,true))
		 {		     
		     fw.write(LocalDateTime.now().toString() + " - " + text + "\n");
		     fw.close();
		 }
		 catch(IOException ioe)
		 {
		     System.err.println("IOException: " + ioe.getMessage());
		 }
	    
	 }
}
