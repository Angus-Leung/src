package song_charts_2402;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class SongList {
	
	private ArrayList<Song> songs;
	
	public SongList(){
		songs = new ArrayList<Song>();
	}
	
	public ArrayList<Song> getSongs(){ return songs; }
	public void add(Song aSong) {songs.add(aSong);}
	
	public void exportXMLToFile(String indent, PrintWriter outputFile){
		if(songs.isEmpty()) return;
		for(Song song : songs) song.exportXMLToFile(indent, outputFile);
	}
	
	public static SongList parseFromFile(File inputFile){

		System.out.println("Parse File Data:");
		
		if(inputFile == null) return null;
		
		SongList theSongs  = new SongList();

		
		BufferedReader inputFileReader;
		
	    String inputLine; //current input line
		try{
			   inputFileReader= new BufferedReader(new FileReader(inputFile));
			    
			   while((inputLine = inputFileReader.readLine()) != null){
				   
				   inputLine = inputLine.trim();
				   
				   //WARNING: this code does not yet validate the XML file
				   //it current ignore the XML tags
				   //<?xml version = "1.0"?>, <fakebookXML>

				   
				   if(inputLine.startsWith(Song.XMLsongStartTag) ){
					   
					   Song song = Song.parseFromFile(inputFileReader);					   
					   if(song != null) theSongs.add(song);
					   
				   }
				   

			   
			   
			   } //end while
			   
			   
		}catch (EOFException e) {
            System.out.println("File Read Error: EOF encountered, file may be corrupted.");
        } catch (IOException e) {
            System.out.println("File Read Error: Cannot read from file.");
        }
		
		
		System.out.println("END Data Parse");
		return theSongs;			
				
	}

}
