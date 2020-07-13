import java.io.File; 
  
import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 

public class Main {
	public static void main(String[] args) 
    { 
		 
		//clock have picture taken every something seconds
		//increment file name by 1
		//sleep for x seconds
		Tesseract tesseract = new Tesseract(); 
		tesseract.setDatapath("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/"); 
		//filter out letters that are not 
		File image = new File("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/1handicap1ar.png");
		process_image(tesseract, image);
		        	
		  
	       
	        
    }
	
	private static void process_image(Tesseract tesseract, File image) {
        try { 
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
