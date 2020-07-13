import java.io.File; 
  
import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) 
    { 
	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sys?serverTimezone=US/Pacific", "root", "sql99server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		//clock have picture taken every something seconds
		//increment file name by 1
		//sleep for x seconds
		Tesseract tesseract = new Tesseract(); 
		tesseract.setDatapath("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/"); 
		File image = new File("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/1handicap1ar.png");
		//watch directory for changes
		//if file created,
		//process that file
		
		process_image(tesseract, image);
		        	
    }
	
	private static void process_image(Tesseract tesseract, File image) {
        try { 
        	//get text from image tesseract ocr
        	//filter out text and clean up results
        	//
        	//add capacity to database - additionally: verify spots added actually exist to reduce false positives
        	
        	String text = tesseract.doOCR(image); 
            text = filter_results(text);
            System.out.print(text); 
           
        }
    	catch (TesseractException e) { 
            e.printStackTrace(); 
        } 
		//addto db
	}
	
	private static String filter_results(String text) {
		return text.replaceAll("[^A-GKM-Z]+", "");
	}
	
	
	
}
