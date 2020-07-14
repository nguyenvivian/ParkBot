import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.*;

import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.sql.ResultSet;

public class Main {
	public static void main(String[] args) 
    { 

		Tesseract tesseract = new Tesseract(); 
		tesseract.setDatapath("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/"); 
		
		//watch directory for changes
		//if file created,
		//process that file
		WatchService watcher;
		try {
			watcher = FileSystems.getDefault().newWatchService();
		Path directory = FileSystems.getDefault().getPath("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/img");
			for(;;) {
				WatchKey key = directory.register(watcher, ENTRY_CREATE);
//				
				try {
					key = watcher.take();
				}catch(Exception e) {
					return;
				}
			   for (WatchEvent<?> event: key.pollEvents()) {
			        WatchEvent.Kind<?> kind = event.kind();
			        if (kind == OVERFLOW) {
			            continue;
			        }
			        WatchEvent<Path> ev = (WatchEvent<Path>)event;
			        Path filename = ev.context();
			        System.out.println(filename);
			   }
			    boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			}
			
		}catch(Exception e) {
            e.printStackTrace(); 
          
		}
		
			File image = new File("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/img/1handicap1ar.png");
	
			process_image(tesseract, image);
    }        	
    
	
	private static void process_image(Tesseract tesseract, File image) {
        try { 
        	//get text from image tesseract ocr
        	//filter out text and clean up results
       
    //    	for (int i = 0; i< 24; ++i) {
        		String text = tesseract.doOCR(image); 
            	text = filter_results(text);
            	add_database(text,0);
  //      	}
           
        }
    	catch (TesseractException e) { 
            e.printStackTrace(); 
        } 
	}
	
	private static String filter_results(String text) {
		return text.replaceAll("[^A-GKM-Z]+", "");
	}
	
	private static void add_database(String text, Integer testOffset) {
	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sys?serverTimezone=US/Pacific", "root", "sql99server");
		
			HashMap<String, Integer> indicatorCount = new HashMap<String, Integer>();
		
			String[] textToSort = text.split("");
			for (String spot: textToSort) {
				if (!indicatorCount.containsKey(spot)) {
					indicatorCount.put(spot,1);
				}else {
					indicatorCount.put(spot, indicatorCount.get(spot)+1);
				}
			}
			
			System.out.println(indicatorCount);
			for(Entry<String, Integer> entry : indicatorCount.entrySet()) {		
				Statement statement = connection.createStatement();
				ResultSet spotSet= statement.executeQuery("SELECT * FROM SPOT_TYPE WHERE INDICATOR="+"\""+entry.getKey()+"\"");
				spotSet.next();
				int spotTypeId = spotSet.getInt("SPOT_TYPE_ID");
				spotSet.close();
				
				ResultSet numSpotSet= statement.executeQuery("SELECT * FROM LOT_SPOT_RELATIONSHIP WHERE LOT_ID = 1 AND SPOT_TYPE_ID ="+spotTypeId);
				numSpotSet.next();
				
				int spotsFilled = numSpotSet.getInt("MAX_CAPACITY") - entry.getValue();
				//error handling 
				
				System.out.println("INSERT INTO LOG (TIMESTAMP, LOT_ID, SPOT_TYPE_ID, NUM_FILLED) VALUES ("+"\""+LocalDateTime.now().plusMinutes(testOffset*5)+"\","+1+","+spotTypeId+","+spotsFilled+")");
				statement.executeUpdate("INSERT INTO LOG (TIMESTAMP, LOT_ID, SPOT_TYPE_ID, NUM_FILLED) VALUES ("+"\""+LocalDateTime.now().plusMinutes(testOffset*5)+"\","+1+","+spotTypeId+","+spotsFilled+")");

				numSpotSet.close();
				statement.close();

			}
			connection.close();
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	    
	}
	
	
	
}
