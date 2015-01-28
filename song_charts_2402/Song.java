package song_charts_2402;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Song {
	
	//XML Tags 
	public static String XMLsongStartTag = "<song>";
	public static String XMLsongEndTag = "</song>";
	
	public static String XMLtitleStartTag = "<title>";
	public static String XMLtitleEndTag = "</title>";
	
	public static String XMLcomposerStartTag = "<composer>";
	public static String XMLcomposerEndTag = "</composer>";

	public static String XMLmusicalStyleStartTag = "<musicalStyle>";
	public static String XMLmusicalStyleEndTag = "</musicalStyle>";

	public static String XMLkeyStartTag = "<key>";
	public static String XMLkeyEndTag = "</key>";
	
	private String title; //title of song
	private String composer; //composer of song
	private String musicalStyle; //musical style of song
	private String key; //musical key of the song
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	public Song(){
	}
	
	public Song(String aSongTitle){
		title = aSongTitle;
	}
	
	public String getTitle() {return title;}
	private void setTitle(String aTitleString) {title = aTitleString;}
	public String getComposer() {return composer;}
	private void setComposer(String aComposerName) {composer = fixLastNameProblem(aComposerName);}
	public String getMusicalStyle() {return musicalStyle;}
	private void setMusicalStyle(String aMusicalStyle) {musicalStyle = aMusicalStyle;}
	public String getKey() {return key;}
	private void setKey(String aKey) {key = aKey;}
	
	public ArrayList<Bar> getBars() {return bars;}
	private void addBar(Bar aBar) {bars.add(aBar);}
	
	public Bar getBarAfter(Bar aBar){
		
		if(aBar == null) return null;
		if(bars == null) return null;
		
		int indexOfCurrentBar = bars.indexOf(aBar);
        if(indexOfCurrentBar + 1 < bars.size())
        	return bars.get(indexOfCurrentBar + 1);
        else 
        	return bars.get(0);
		
	}
	
	public Bar getBarAtLocation(int x, int y){
		//bar that is currently contains the graphical location (x,y)
		for(Bar bar : this.getBars()){
			if(bar.locationContains(x,y)) return bar;
	
		}
		return null;
	}
	
	public void insertBarBefore(Bar aBar){
		if(aBar == null) return;
		ArrayList<Bar> temp = new ArrayList<Bar>();
		for(Bar bar : bars) temp.add(bar);
		bars.clear();
		for(Bar bar : temp){
			if(bar == aBar) bars.add(new Bar());
			bars.add(bar);			
		}
	}
	
	public void insertBarAfter(Bar aBar){
		if(aBar == null) return;
		ArrayList<Bar> temp = new ArrayList<Bar>();
		for(Bar bar : bars) temp.add(bar);
		bars.clear();
		for(Bar bar : temp){
			bars.add(bar);
			if(bar == aBar) bars.add(new Bar());			
		}
	}

	public void deleteBar(Bar aBar){
		if(aBar == null) return;
		ArrayList<Bar> temp = new ArrayList<Bar>();
		for(Bar bar : bars) temp.add(bar);
		bars.clear();
		for(Bar bar : temp){
		  if(!(bar == aBar)) bars.add(bar);			
		}
	}

	
	
	public String toString(){
				
		return "" +  title;
	}

	public void drawInArea(Graphics aPen, int areaX, int areaY, int areaWidth, int areaHeight){

		Graphics2D aPen2D = (Graphics2D) aPen; //cast Graphics to get more operations available.
        aPen2D.setStroke(ChartView.stroke); 
        
        	
            Font cacheFont = aPen.getFont(); //cache the current pen font           
            
            //Draw song title
        	//draw title centered in drawing area

    		aPen.setFont(ChartView.defaultTitleFont);
            FontMetrics titleFontMetrics = aPen.getFontMetrics(); //allows measuring render width of strings
        	
            String theSongTitle = getTitle();
        	int offsetX = areaX + areaWidth/2 - titleFontMetrics.stringWidth(theSongTitle)/2;
        	int offsetY = areaY + ChartView.titlePointSize + 10;  
        	
        	int lineSpacing = ChartView.titlePointSize + 10;
           	aPen2D.drawString("Title: " + theSongTitle, offsetX, offsetY); //draw title
           	
           	offsetY = offsetY + lineSpacing;
           	aPen2D.drawString("Composer: " + getComposer(), offsetX, offsetY); //draw composer

           	offsetY = offsetY + lineSpacing;
           	aPen2D.drawString("Style: " + getMusicalStyle(), offsetX, offsetY); //draw style

           	offsetY = offsetY + lineSpacing;
           	aPen2D.drawString("Key: " + getKey(), offsetX, offsetY); //draw musical key

           	offsetX = 30; //hard coded for now
           	offsetY = offsetY + lineSpacing;
           	ArrayList<Bar> theBars = getBars();
    		aPen.setFont(ChartView.defaultChartFont);
        	
    		lineSpacing = ChartView.chartPointSize + 10;
    		
    		int origX = areaX;
    		//have all the bars draw themselves in the designated area
           	for(int i=0; i<theBars.size(); i++){
           		
          		
	            int allowedBarWidth = (areaWidth/4) - 5; //arbitrary hardcoded for now

           		if(i%4 == 0){
	           	   //determine area where bar should be drawn
	               offsetY = offsetY + lineSpacing;
	               areaX = origX;
           		}
           		else{
           		 //determine area where bar should be drawn
 	               offsetY = offsetY;
 	               areaX = areaX + allowedBarWidth;
           		}
           		Bar bar = theBars.get(i);
	            //draw the bar in the designated area
	            bar.drawInArea(aPen, areaX, offsetY, allowedBarWidth, lineSpacing);
           	}
 
           	           	           	
        	aPen.setFont(cacheFont); //restore pen font to cached font
       
		
	}
	
	
	
	private String fixLastNameProblem( String aComposer){
		//Fix issue in data where last name first is not followed by a comma
		
		if(aComposer == null) return null;
		if(aComposer.equals("")) return "";
		if(aComposer.indexOf(' ') < 0) return aComposer;
		if(aComposer.indexOf(',') > 0) return aComposer; //already has last name, first name format
		
		int indexOfFirstBlank = aComposer.indexOf(' ');
		
		String newComposer = aComposer.substring(0,indexOfFirstBlank) + "," + aComposer.substring(indexOfFirstBlank, aComposer.length());
		return newComposer;
		
	}
	
	
	public void exportXMLToFile(String indent, PrintWriter outputFile){
		
		String newIndent = indent + "   ";
		
		outputFile.println(indent+XMLsongStartTag);
		outputFile.println(newIndent + XMLtitleStartTag + title + XMLtitleEndTag);
		outputFile.println(newIndent + XMLcomposerStartTag + composer + XMLcomposerEndTag);
		outputFile.println(newIndent + XMLmusicalStyleStartTag + musicalStyle + XMLmusicalStyleEndTag);
		outputFile.println(newIndent + XMLkeyStartTag + key + XMLkeyEndTag);
		
		if(!(bars == null || bars.isEmpty())){
			for(Bar bar : bars) bar.exportXMLToFile(newIndent, outputFile);
		}
		
		outputFile.println(indent+XMLsongEndTag);
	}

	
	public  static Song parseFromFile(BufferedReader inputFileReader){

		
		if(inputFileReader == null) return null;
		
		//System.out.println("Parse Song");
		
		Song theSong  = new Song();
		
		
	    String inputLine; //current input line
		try{
			    
			   while(!(inputLine = inputFileReader.readLine().trim()).startsWith(Song.XMLsongEndTag)){
				   
				   inputLine = inputLine.trim();
				   
				   if(inputLine.startsWith(Song.XMLtitleStartTag) && 
					   inputLine.endsWith(Song.XMLtitleEndTag)){
					   
					   String titleString = inputLine.substring(Song.XMLtitleStartTag.length(), 
							   inputLine.length()- Song.XMLtitleEndTag.length()).trim();
					   
					   if(titleString != null && titleString.length() > 0)
						   theSong.setTitle(titleString);					   
				   }
				   
				   if(inputLine.startsWith(Song.XMLcomposerStartTag) && 
						   inputLine.endsWith(Song.XMLcomposerEndTag)){
						   
						   String composerString = inputLine.substring(Song.XMLcomposerStartTag.length(), 
								   inputLine.length()- Song.XMLcomposerEndTag.length()).trim();
						   
						   if(composerString != null && composerString.length() > 0)
							   theSong.setComposer(composerString);					   
					}
				   
				   if(inputLine.startsWith(Song.XMLmusicalStyleStartTag) && 
						   inputLine.endsWith(Song.XMLmusicalStyleEndTag)){
						   
						   String styleString = inputLine.substring(Song.XMLmusicalStyleStartTag.length(), 
								   inputLine.length()- Song.XMLmusicalStyleEndTag.length()).trim();
						   
						   if(styleString != null && styleString.length() > 0)
							   theSong.setMusicalStyle(styleString);					   
					}

				   if(inputLine.startsWith(Song.XMLkeyStartTag) && 
						   inputLine.endsWith(Song.XMLkeyEndTag)){
						   
						   String keyString = inputLine.substring(Song.XMLkeyStartTag.length(), 
								   inputLine.length()- Song.XMLkeyEndTag.length()).trim();
						   
						   if(keyString != null && keyString.length() > 0)
							   theSong.setKey(keyString);					   
					}
				   
				   if(inputLine.startsWith(Bar.XMLbarStartTag)){
						   
					   Bar bar = Bar.parseFromFile(inputFileReader);					   
					   if(bar != null) theSong.addBar(bar);
					}


				   			   			   
			   } //end while
			   
			   
		}catch (EOFException e) {
            System.out.println("File Read Error: EOF encountered, file may be corrupted.");
        } catch (IOException e) {
            System.out.println("File Read Error: Cannot read from file.");
        }
		
		
		//System.out.println("END Song Data Parse");
		return theSong;			
				
	}

}
