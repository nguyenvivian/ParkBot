import java.io.File; 
  
import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 

public class Main {
	public static void main(String[] args) 
    { 
		 Tesseract tesseract = new Tesseract(); 
	        try { 
	  
	            tesseract.setDatapath("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/"); 
	        	File imageFile = new File("/Users/viviannguyen/eclipse-workspace/ParkBot/src/Tess4J/tessdata/1general.png");

	        	String text 
	                = tesseract.doOCR(imageFile); 
	  
	            System.out.print(text); 
	        } 
	        catch (TesseractException e) { 
	            e.printStackTrace(); 
	        } 
    }
}
