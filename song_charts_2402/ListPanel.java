package song_charts_2402;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


import javax.swing.*;

// This is the Panel that contains represents the view of the
// Music Store

public class ListPanel extends JPanel {

	// These are the components
	private JButton		searchButton;
	private JTextField  searchText;
	private JList		songJList;
	
	private Font UIFont = new Font("Courier New", Font.BOLD, 16);


	// These are the get methods that are used to access the components
	public JButton getSearchButton() { return searchButton; }
	public JList getSongJList() { return songJList; }
	
	public void setSongListData(SongList aSongList) {
	        Song[] songArray = new Song[1]; //just to establish array type
	        songArray =  aSongList.getSongs().toArray(songArray);
			songJList.setListData(songArray);

	}
	public JTextField getSearchText() { return searchText; }
	
	


	// This is the default constructor
	public ListPanel(SongList aSongList){
		super();

		// Use a GridBagLayout (lotsa fun)
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		setLayout(layout);


		// Add the Add button
		searchText = new JTextField("");
		searchText.setFont(UIFont);

		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.EAST;
		layoutConstraints.weightx = 1.0;
		layoutConstraints.weighty = 0.0;
		layout.setConstraints(searchText, layoutConstraints);
		add(searchText);

		// Add the Remove button
		searchButton = new JButton("Search");
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.EAST;
		layoutConstraints.weightx = 0.0;
		layoutConstraints.weighty = 0.0;
		layout.setConstraints(searchButton, layoutConstraints);
		add(searchButton);

		// Add the songList list
		songJList = new JList();
        Song[] songArray = new Song[1]; //just to establish array type
        songArray =  aSongList.getSongs().toArray(songArray);
		songJList.setListData(songArray);
		
		songJList.setFont(UIFont);
		songJList.setPrototypeCellValue("xxxxxxxxxxxxxxxxxxxxxx");
		JScrollPane scrollPane = new JScrollPane( songJList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.gridwidth = 2;
		layoutConstraints.gridheight = 4;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
		layoutConstraints.weightx = 2.0;
		layoutConstraints.weighty = 1.0;
		layout.setConstraints(scrollPane, layoutConstraints);
		add(scrollPane);


	}
}