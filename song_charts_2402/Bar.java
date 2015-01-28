package song_charts_2402;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class Bar {
	
	public static enum BarLine {NORMAL_BAR_LINE, 
		                        LEFT_DOUBLE_BAR_LINE, 
		                        RIGHT_DOUBLE_BAR_LINE, 
		                        LEFT_REPEAT, 
		                        RIGHT_REPEAT, 
		                        FINAL_BAR_LINE
		                        };
	
	//XML Tags 
	public static String XMLbarStartTag = "<bar>";
	public static String XMLbarEndTag = "</bar>";
	
	public static String XMLchordsStartTag = "<chords>";
	public static String XMLchordsEndTag = "</chords>";
	public static String XMLtimeSignatureStartTag = "<timeSignature>";
	public static String XMLtimeSignatureEndTag = "</timeSignature>";
	public static String XMLendingsStartTag = "<endings>";
	public static String XMLendingsEndTag = "</endings>";
	public static String XMLtextStartTag = "<text>";
	public static String XMLtextEndTag = "</text>";
	public static String XMLrehearsalLetterStartTag = "<rehearsalLetter>";
	public static String XMLrehearsalLetterEndTag = "</rehearsalLetter>";
	public static String XMLdaCapoStartTag = "<daCapo>";
	public static String XMLdaCapoEndTag = "</daCapo>";
	public static String XMLdalSegnoStartTag = "<dalSegno>";
	public static String XMLdalSegnoEndTag = "</dalSegno>";
	public static String XMLphraseAbreviationStartTag = "<phraseAbreviation>";
	public static String XMLphraseAbreviationEndTag = "</phraseAbreviation>";

	public static String XMLCodaTag = "<coda/>";
	public static String XMLSignTag = "<sign/>";
	public static String XMLLeftDoubleBarLineTag = "<leftDoubleBarLine/>";
	public static String XMLRightDoubleBarLineTag = "<rightDoubleBarLine/>";
	public static String XMLLeftRepeatTag = "<leftRepeat/>";
	public static String XMLRightRepeatTag = "<rightRepeat/>";
	public static String XMLFinalBarLineTag = "<finalBarLine/>";



	//Private variables
	private String timeSignature = null; 
	private String chords = null; 
	private String phraseAbreviation = null; //used for x and xx 
	private String endings = null;  //first, second ending etc.
	private String text = null;  //arbitrary text displayed above a bar
	private String rehearsalLetter = null;  //[intro] [A] [B] etc.
	private boolean sign = false; //Sign or Segno
	private boolean coda = false; //Coda sign
	private String daCapo = null;  //D.C. (Da Capo: go to the top)
	private String dalSegno = null; //D.S. (Dal Segno: go to the sign)	
	private  BarLine leftBarLine = BarLine.NORMAL_BAR_LINE;
	private  BarLine rightBarLine = BarLine.NORMAL_BAR_LINE;
	
	//Graphic Location Variables
	//specify the drawing location and drawing area of the bar
	private int originX; //upper left corner X
	private int originY; //upper left corner Y
	private int width;
	private int height;
	
	public Bar(){
	}
	
	
	public String getChords() {return chords;}
	public BarLine getLeftBarLine() {return leftBarLine;}
	public BarLine getRightBarLine() {return rightBarLine;}
	//to do: add other needed get methods
	
	//get methods for drawing location and size
	public int getOriginX(){return originX;}
	public int getOriginY(){return originY;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public boolean locationContains(int x, int y){
		//answer whether x,y is a point inside the drawing area of this bar
		if(x < originX) return false;
		if(x > originX + width) return false;
		if(y < originY) return false;
		if(y > originY+height) return false;
		
		return true;
		
	}
	
	//various set methods
	private void setChords(String aChordString) {chords = aChordString;}
	private void setTimeSignature(String aTimeSignatureString) {timeSignature = aTimeSignatureString;}
	private void setEndings(String anEndingsString) {endings = anEndingsString;}
	private void setText(String aTextString) {text = aTextString;}
	private void setRehearsalLetter(String aRehearLetterString) {rehearsalLetter = aRehearLetterString;}
	private void setDaCapo(String aDaCapoString) {daCapo = aDaCapoString;}
	private void setDalSegno(String aDalSegnoString) {dalSegno = aDalSegnoString;}
	private void setPhraseAbreviation(String aPhraseString) {phraseAbreviation = aPhraseString;}
	public void setLeftBarLine(BarLine aBarLineConstant) {leftBarLine = aBarLineConstant;}
	public void setRightBarLine(BarLine aBarLineConstant) {rightBarLine = aBarLineConstant;}
	
	public void parseDataString(String aDataString){
		//for now assume aDataString just has chord data
		setChords(aDataString);
	
	}

	
	public String toString(){
				
		return "    chords:" +  chords + "\n" +
			   "   timeSig:" + timeSignature + "\n" + 
			   "      Lbar:" + leftBarLine + "\n" + 
			   "      Lbar:" + rightBarLine + "\n" 
			  ;
	}
	
	public void drawInArea(Graphics aPen, int areaX, int areaY, int areaWidth, int areaHeight){
        //draw the bar in the area provided
		
		Graphics2D aPen2D = (Graphics2D) aPen; //cast Graphics to get more operations available.
        aPen2D.setStroke(ChartView.stroke); 
        
        //set graphic location of bar
        originX = areaX;
        originY = areaY;
        width = areaWidth;
        height = areaHeight;
       
        int stringHeight = areaHeight *2/3; //strings are drawn above their y-location
       
  	   aPen2D.drawString("  " + getChords(), areaX, areaY + stringHeight);
  	   
       if(getLeftBarLine() == Bar.BarLine.NORMAL_BAR_LINE)
   	      aPen2D.drawString("|", areaX, areaY + stringHeight);        
       else if(getLeftBarLine() == Bar.BarLine.LEFT_DOUBLE_BAR_LINE)
    	      aPen2D.drawString("||", areaX, areaY + stringHeight);        
       else if(getLeftBarLine() == Bar.BarLine.LEFT_REPEAT)
    	      aPen2D.drawString("|:", areaX, areaY + stringHeight);    
       
       if(getRightBarLine() == Bar.BarLine.NORMAL_BAR_LINE)
   	      aPen2D.drawString("|", areaX + areaWidth, areaY + stringHeight);
       else if(getRightBarLine() == Bar.BarLine.RIGHT_DOUBLE_BAR_LINE)
    	      aPen2D.drawString("||", areaX+ areaWidth, areaY + stringHeight);
       else if(getRightBarLine() == Bar.BarLine.RIGHT_REPEAT)
 	      aPen2D.drawString(":|", areaX+ areaWidth, areaY + stringHeight);
       else if(getRightBarLine() == Bar.BarLine.FINAL_BAR_LINE)
 	      aPen2D.drawString("|]", areaX+ areaWidth, areaY + stringHeight);
 
	}
	
	public void exportXMLToFile(String indent, PrintWriter outputFile){
		//Export this bar as XML data to output file
		
		String nextIndent = indent + "   ";

		
		outputFile.println(indent+XMLbarStartTag);

		
		if(sign)
		    outputFile.println(nextIndent + XMLSignTag);
		if(coda)
		    outputFile.println(nextIndent + XMLCodaTag);
		
		if(leftBarLine == BarLine.LEFT_DOUBLE_BAR_LINE)
		    outputFile.println(nextIndent + XMLLeftDoubleBarLineTag);
		else if(leftBarLine == BarLine.LEFT_REPEAT)
		    outputFile.println(nextIndent + XMLLeftRepeatTag);

		if(rightBarLine == BarLine.RIGHT_DOUBLE_BAR_LINE)
		    outputFile.println(nextIndent + XMLRightDoubleBarLineTag);
		else if(rightBarLine == BarLine.RIGHT_REPEAT)
		    outputFile.println(nextIndent + XMLRightRepeatTag);
		else if(rightBarLine == BarLine.FINAL_BAR_LINE)
		    outputFile.println(nextIndent + XMLFinalBarLineTag);

		
		if(rehearsalLetter != null && !rehearsalLetter.equals(""))
		    outputFile.println(nextIndent + XMLrehearsalLetterStartTag + rehearsalLetter + XMLrehearsalLetterEndTag);
		
		if(daCapo != null && !daCapo.equals(""))
		    outputFile.println(nextIndent + XMLdaCapoStartTag + daCapo + XMLdaCapoEndTag);
		
		if(dalSegno != null && !dalSegno.equals(""))
		    outputFile.println(nextIndent + XMLdalSegnoStartTag + dalSegno + XMLdalSegnoEndTag);
		
		if(endings != null && !endings.equals(""))
		    outputFile.println(nextIndent + XMLendingsStartTag + endings + XMLendingsEndTag);
		
		if(timeSignature != null && !timeSignature.equals(""))
		    outputFile.println(nextIndent + XMLtimeSignatureStartTag + timeSignature + XMLtimeSignatureEndTag);

		if(chords != null && !chords.equals(""))
		    outputFile.println(nextIndent + XMLchordsStartTag + chords + XMLchordsEndTag);
		
		if(phraseAbreviation != null && !phraseAbreviation.equals(""))
		    outputFile.println(nextIndent + XMLphraseAbreviationStartTag + phraseAbreviation + XMLphraseAbreviationEndTag);
		
		if(text != null && !text.equals(""))
		    outputFile.println(nextIndent + XMLtextStartTag + text + XMLtextEndTag);
		
		
		outputFile.println(indent+XMLbarEndTag);
	}


	
	public  static Bar parseFromFile(BufferedReader inputFileReader){

		
		if(inputFileReader == null) return null;
		
		//System.out.println("Parse Bar");
		
		Bar theBar  = new Bar();
		
		
	    String inputLine; //current input line
		try{
			    
			   while(!(inputLine = inputFileReader.readLine().trim()).startsWith(Bar.XMLbarEndTag)){
				   
				   inputLine = inputLine.trim();
				   
				   if(inputLine.startsWith(Bar.XMLchordsStartTag) && 
						   inputLine.endsWith(Bar.XMLchordsEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLchordsStartTag.length(), 
								   inputLine.length()- Bar.XMLchordsEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setChords(dataString);					   
				   }
				   else if(inputLine.startsWith(Bar.XMLtimeSignatureStartTag) && 
						   inputLine.endsWith(Bar.XMLtimeSignatureEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLtimeSignatureStartTag.length(), 
								   inputLine.length()- Bar.XMLtimeSignatureEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setTimeSignature(dataString);					   
					   }
				   else if(inputLine.startsWith(Bar.XMLendingsStartTag) && 
						   inputLine.endsWith(Bar.XMLendingsEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLendingsStartTag.length(), 
								   inputLine.length()- Bar.XMLendingsEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setEndings(dataString);					   
					   }
				   else if(inputLine.startsWith(Bar.XMLtextStartTag) && 
						   inputLine.endsWith(Bar.XMLtextEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLtextStartTag.length(), 
								   inputLine.length()- Bar.XMLtextEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setText(dataString);					   
						   
					   }
				   else if(inputLine.startsWith(Bar.XMLrehearsalLetterStartTag) && 
						   inputLine.endsWith(Bar.XMLrehearsalLetterEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLrehearsalLetterStartTag.length(), 
								   inputLine.length()- Bar.XMLrehearsalLetterEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setRehearsalLetter(dataString);					   
						   
					   }
				   else if(inputLine.startsWith(Bar.XMLdaCapoStartTag) && 
						   inputLine.endsWith(Bar.XMLdaCapoEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLdaCapoStartTag.length(), 
								   inputLine.length()- Bar.XMLdaCapoEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setDaCapo(dataString);					   
						   
					   }
				   else if(inputLine.startsWith(Bar.XMLdalSegnoStartTag) && 
						   inputLine.endsWith(Bar.XMLdalSegnoEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLdalSegnoStartTag.length(), 
								   inputLine.length()- Bar.XMLdalSegnoEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setDalSegno(dataString);					   
						   
					   }
				   else if(inputLine.startsWith(Bar.XMLphraseAbreviationStartTag) && 
						   inputLine.endsWith(Bar.XMLphraseAbreviationEndTag)){
						   
						   String dataString = inputLine.substring(Bar.XMLphraseAbreviationStartTag.length(), 
								   inputLine.length()- Bar.XMLphraseAbreviationEndTag.length()).trim();
						   
						   if(dataString != null && dataString.length() > 0)
							   theBar.setPhraseAbreviation(dataString);					   
						   
					   }
				   else if(inputLine.startsWith(Bar.XMLLeftDoubleBarLineTag)){
					   theBar.setLeftBarLine(Bar.BarLine.LEFT_DOUBLE_BAR_LINE);					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLRightDoubleBarLineTag)){
					   theBar.setRightBarLine(Bar.BarLine.RIGHT_DOUBLE_BAR_LINE);					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLLeftRepeatTag)){
					   theBar.setLeftBarLine(Bar.BarLine.LEFT_REPEAT);					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLRightRepeatTag)){
					   theBar.setRightBarLine(Bar.BarLine.RIGHT_REPEAT);					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLFinalBarLineTag)){
					   theBar.setRightBarLine(Bar.BarLine.FINAL_BAR_LINE);					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLCodaTag)){
					   theBar.coda = true;					   						   
			       }
				   else if(inputLine.startsWith(Bar.XMLSignTag)){
					   theBar.sign = true;					   						   
			       }

			   			   			   
			   } //end while
			   
			   
		}catch (EOFException e) {
            System.out.println("File Read Error: EOF encountered, file may be corrupted.");
        } catch (IOException e) {
            System.out.println("File Read Error: Cannot read from file.");
        }
		
		
		//System.out.println("END Song Data Parse");
		return theBar;			
				
	}

}
