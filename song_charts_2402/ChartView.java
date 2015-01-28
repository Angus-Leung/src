package song_charts_2402;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class ChartView extends JPanel implements MouseListener, MouseMotionListener{
 
    final public static BasicStroke stroke = new BasicStroke(3.0f);
    //title and bar number font
    public final static int titlePointSize = 30;
    public final static Font defaultTitleFont = new Font("Times New Roman", Font.BOLD, titlePointSize);
    
    public final static int chartPointSize = 20;
    public final static Font defaultChartFont = new Font("Times New Roman", Font.BOLD, chartPointSize);

	private Song songToView = null; //song being displayed
	private Bar currentBar = null; //bar being played
	private Bar barBeingEdited = null;  //bar being edited by moving text field
	private Bar popUpBar = null; //bar being edited with popup menu
	
	//highlight box so show over bar being played
	private int boxX = 10, boxY = 10, boxWidth = 100, boxHeight = 50;
    
	//textfield used to edit bar chords etc.
	private JTextField movingTextField = new JTextField("Hello World");
	
	//popup menu for Bars to edit properties of bars
	private JPopupMenu barPopupMenu = new JPopupMenu(); //context menu for Bars
	
	private JMenu leftBarLinePopupMenu = new JMenu("Left Bar Lines"); 
	private JMenu rightBarLinePopupMenu = new JMenu("Right Bar Lines"); 
	private JMenu insertOrDeleteBarsMenu = new JMenu("Insert/Delete Bars"); 
	
	private JMenuItem leftNormalBarLineMenuItem = new JMenuItem("Left Normal Bar Line:    |");
	private JMenuItem leftDoubleBarLineMenuItem = new JMenuItem("Left Double Bar Line:    ||");
	private JMenuItem leftRepeatBarLineMenuItem = new JMenuItem("Left Repeat Bar Line:    |:");
	private JMenuItem rightNormalBarLineMenuItem = new JMenuItem("Right Normal Bar Line:    |");
	private JMenuItem rightDoubleBarLineMenuItem = new JMenuItem("Right Double Bar Line:    ||");
	private JMenuItem rightRepeatBarLineMenuItem = new JMenuItem("Right Repeat Bar Line:    :|");
	private JMenuItem finalBarLineMenuItem = new JMenuItem("Right Repeat Bar Line:    |]");
	
	private JMenuItem insertBarAfterMenuItem = new JMenuItem("Insert Bar After");
	private JMenuItem insertBarBeforeMenuItem = new JMenuItem("Insert Bar Before");
	private JMenuItem deleteBarMenuItem = new JMenuItem("Delete Bar");
	
	public void buildBarPopupMenu(){
		
		//left bar line sub menu
		leftBarLinePopupMenu.add(leftNormalBarLineMenuItem);
		leftBarLinePopupMenu.add(leftDoubleBarLineMenuItem);        
		leftBarLinePopupMenu.add(leftRepeatBarLineMenuItem);

		//right bar line sub menu
		rightBarLinePopupMenu.add(rightNormalBarLineMenuItem);
		rightBarLinePopupMenu.add(rightDoubleBarLineMenuItem);        
		rightBarLinePopupMenu.add(rightRepeatBarLineMenuItem);
		rightBarLinePopupMenu.add(finalBarLineMenuItem);
		
		//insert or delete bars
		insertOrDeleteBarsMenu.add(insertBarBeforeMenuItem);
		insertOrDeleteBarsMenu.add(insertBarAfterMenuItem);
		insertOrDeleteBarsMenu.add(deleteBarMenuItem);

		//bar pop up menu
		barPopupMenu.add(leftBarLinePopupMenu);
		barPopupMenu.add(rightBarLinePopupMenu);
		barPopupMenu.add(new JSeparator());
		barPopupMenu.add(insertOrDeleteBarsMenu);


        //Build Action Listener for all menu items
		ActionListener barPopUpMenuListener = 	new ActionListener() { 
		  public void actionPerformed(ActionEvent event){
		
		   			
	        	if (event.getSource() == leftNormalBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.NORMAL_BAR_LINE);
	        	else if (event.getSource() == leftDoubleBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.LEFT_DOUBLE_BAR_LINE);
	        	else if (event.getSource() == leftRepeatBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.LEFT_REPEAT);
	        	else if (event.getSource() == rightNormalBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.NORMAL_BAR_LINE);
	        	else if (event.getSource() == rightDoubleBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.RIGHT_DOUBLE_BAR_LINE);
	        	else if (event.getSource() == rightRepeatBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.RIGHT_REPEAT);
	        	else if (event.getSource() == finalBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.FINAL_BAR_LINE);
	        	
	        	else if (event.getSource() == insertBarBeforeMenuItem)
	        		songToView.insertBarBefore(popUpBar);
	        	else if (event.getSource() == insertBarAfterMenuItem)
	        		songToView.insertBarBefore(popUpBar);
	        	else if (event.getSource() == deleteBarMenuItem)
	        		songToView.deleteBar(popUpBar);
        

        	update(); //update GUI
           		
			} 
		}; 
        
        //add action listener to menu items
		leftNormalBarLineMenuItem.addActionListener(barPopUpMenuListener);
		leftDoubleBarLineMenuItem.addActionListener(barPopUpMenuListener);
		leftRepeatBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightNormalBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightDoubleBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightRepeatBarLineMenuItem.addActionListener(barPopUpMenuListener);
		finalBarLineMenuItem.addActionListener(barPopUpMenuListener);
		insertBarBeforeMenuItem.addActionListener(barPopUpMenuListener);
		insertBarAfterMenuItem.addActionListener(barPopUpMenuListener);
		deleteBarMenuItem.addActionListener(barPopUpMenuListener);
		
	}

	
	public ChartView(){
		
		setSize(700, 700);
		setBackground(Color.white);
		
    	//add event listeners
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	
	    movingTextField.setLocation(50,50); //arbitrary default location
	    movingTextField.setSize(70,20); //arbitrary default size
	    movingTextField.setFont(defaultChartFont);
	    movingTextField.setBackground(new Color(186,234,255)); //light blue
	    movingTextField.setBorder(null); //no border around text field
        add(movingTextField); //add moving text edit field to this panel
	    movingTextField.setVisible(true); //hide text edit field
	    
        movingTextField.addActionListener( 
			new ActionListener() { 
				public void actionPerformed(ActionEvent event){ 
					if(barBeingEdited != null){
						barBeingEdited.parseDataString(movingTextField.getText());
						barBeingEdited = songToView.getBarAfter(barBeingEdited);
	
						//Advance text edit field to next bar

						movingTextField.setLocation(barBeingEdited.getOriginX(), barBeingEdited.getOriginY());
					    movingTextField.setText(barBeingEdited.getChords());
					    movingTextField.setSize(barBeingEdited.getWidth(), barBeingEdited.getHeight());			 
					    movingTextField.setVisible(true);
					    movingTextField.requestFocus(); //request keyboard focus

					}
					
					update(); //update GUI

				} 
			} 
		);
        
        buildBarPopupMenu();

		 		
	}
	
	public void update(){
		repaint(); //repaint the GUI
	}
	
	public void showSong(Song aSong){ 
		
		if(aSong == null) return;
		
		songToView = aSong;
		if(songToView.getBars().size()>0){
		   currentBar = aSong.getBars().get(0); //get first bar
		   
		   //highlight box
	       boxX = currentBar.getOriginX();
	       boxY = currentBar.getOriginY();     
	       boxWidth = currentBar.getWidth();
		   boxHeight = currentBar.getHeight();

		}
		
	}
	
	
	public void advanceCurrentBar(){
		//called in response to time event
		//advance the current bar and the highlight box
		
		if(songToView ==null)return;
		if(currentBar == null) return;
		
		currentBar = songToView.getBarAfter(currentBar);

		//set highlight box to cover current bar
        boxX = currentBar.getOriginX();
        boxY = currentBar.getOriginY();     
        boxWidth = currentBar.getWidth();
		boxHeight = currentBar.getHeight();
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		
	     movingTextField.setVisible(false); //hide editing text field
		
		if(event.getClickCount() == 2){
		   System.out.println("Mouse Double Clicked");
		   //handle double click
		   if(songToView != null){
			 barBeingEdited = songToView.getBarAtLocation(event.getX(), event.getY());
			 if(barBeingEdited != null){
				 
		   	     //movingTextField.setFont(...);
			     movingTextField.setLocation(barBeingEdited.getOriginX(), barBeingEdited.getOriginY());
			     movingTextField.setText(barBeingEdited.getChords());
			     movingTextField.setSize(barBeingEdited.getWidth(), barBeingEdited.getHeight());			 
			     movingTextField.setVisible(true);
			     movingTextField.requestFocus(); //request keyboard focus
			 }
		   }
		   
		}
		else{
			System.out.println("Mouse Clicked");
		}

	}

	//MouseListener and MouseMotionListner Event handlers
	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
        if (event.isPopupTrigger()){
        	
        	System.out.println("PopUp  Click");
        
            
            if(songToView == null) return;
 
            popUpBar = songToView.getBarAtLocation(event.getX(), event.getY());
           	System.out.println("Pop Up Bar: " + popUpBar);

            if(popUpBar == null) return;

            barPopupMenu.show(event.getComponent(), event.getX(), event.getY());
		       
      }    
		
	}

	
	private void drawInArea(Graphics aPen, int areaX, int areaY, int areaWidth, int areaHeight){
		
		//switch to Graphics2D pen so we can control stroke widths better etc.	    
		Graphics2D aPen2D = (Graphics2D) aPen; //cast Graphics to get more operations available.
        aPen2D.setStroke(stroke); 
        
        if(songToView != null){
        	
        	songToView.drawInArea(aPen, areaX, areaY, areaWidth, areaHeight);
        	
        }
        
        //draw moving highlight box
        if (true){ //for now always draw
        	
        	 Color cacheColor = aPen.getColor();
        	 //aPen.setColor(Color.red);   //opaque colour    	 
        	 aPen.setColor(new Color(1,0,0,0.3f)); //semi transparent colour   	 
          	 aPen.fillRect(boxX, boxY, boxWidth, boxHeight);
          	 //aPen.drawRect(boxX, boxY, boxWidth, boxHeight);
        	 aPen.setColor(cacheColor);      	 

        }
        


	}
	
    public void paintComponent(Graphics aPen) {
		super.paintComponent(aPen);
		
		//draw chart in entire panel area
		drawInArea(aPen, 0,0, getWidth(), getHeight());
		
    }
    


}
