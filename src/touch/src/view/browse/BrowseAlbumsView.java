package view.browse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.front.TouchView;
import view.search.JKeyboardPanel;
import view.search.SearchBoxPanel;
import view.search.SearchResultsPanel;
import view.shared.JConfirmPanel;
import view.shared.JLoadingIndicator;
import dk.wdk.shared.AlbumBrowseUpdate;
import dk.wdk.shared.BrowseAlbumAvailability;
import dk.wdk.shared.SearchResultUpdate;

public class BrowseAlbumsView extends JPanel {
	private static final long serialVersionUID = 2914747713134245216L;
	
	final int letterWidth = 58;
	final int letters = 29;
	final int letterStartX = 1920 / 2 - (letterWidth * letters) / 2 - 15;
	final int letterEndX = letterStartX + (letters * letterWidth);
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
			.getInstance(view.TouchApp.class).getContext().getResourceMap(
					BrowseAlbumsView.class);
	private TouchView touchView = null;
	private BrowseAlbumAvailability browseAlbumAvailability = null;
	private String selectedLetter = null;
	private int selectedIndex = 0;
	private AlbumPanel leftPanel = null;
	private AlbumPanel rightPanel = null;
	private JLoadingIndicator leftLoadingIndicator = null;
	private JLoadingIndicator rightLoadingIndicator = null;
	public JConfirmPanel confirmPanel = null;
	private int currentTrackId = -1;
	public JKeyboardPanel keyboardPanel;
	private String lastSearchQuery = null;
	public SearchResultsPanel searchResultsPanel = null;
	private javax.swing.Timer exitTimer;
	
	public BrowseAlbumsView(TouchView touchView2) {
		this.touchView = touchView2;

		initComponents();
		
		exitTimer = new javax.swing.Timer(1000 * 60, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	exitButtonClicked(null);
		    	
		    	if (BrowseAlbumsView.this.isVisible())
		    		exitButtonClicked(null);
		    }    
		});
		exitTimer.start();
	}

	private void initComponents() {
		confirmPanel = new JConfirmPanel(this);
		keyboardPanel = new JKeyboardPanel(this);
		searchResultsPanel = new SearchResultsPanel(this);
		
		jLayeredPane = new javax.swing.JLayeredPane();
		jLabelBackground = new javax.swing.JLabel();
		jExitButton = new javax.swing.JButton();
		jLetterLabels = new javax.swing.JLabel[30];
		jLetterSelectedLabel = new javax.swing.JLabel();
		leftLoadingIndicator = new JLoadingIndicator();
		rightLoadingIndicator = new JLoadingIndicator();
		searchBoxPanel = new SearchBoxPanel(this);
		scrollIndicator = new ScrollIndicator(this);

		this.addMouseListener(new MouseAdapter() {
			Point pressed = null;

			public void mousePressed(MouseEvent me)
			{
				exitTimer.restart(); // Restart exit timer
				
				if (confirmPanel.isVisible() || searchResultsPanel.isVisible())
					return;
				
				pressed = me.getPoint();
				
				if (me.getY() > 140 && me.getY() < 250)
				{
					jLetterSelectedLabel.setBounds(me.getX() - (letterWidth / 2),
						(int)jLetterSelectedLabel.getBounds().getY(),
						(int)jLetterSelectedLabel.getBounds().getWidth(),
						(int)jLetterSelectedLabel.getBounds().getHeight());
				}
			}

			public void mouseReleased(MouseEvent me)
			{
				exitTimer.restart(); // Restart exit timer
				
				if (confirmPanel.isVisible() || searchResultsPanel.isVisible())
					return;
				
				if (me.getY() > 140 && me.getY() < 250)
				{
					// Find closest active letter
					int leftClosestX = letterStartX;
					int rightClosestX = letterEndX;
					int labelX = (int)jLetterSelectedLabel.getBounds().getX();
					char leftSelectedChar = 'A';
					char rightSelectedChar = 'A';
					char selectedChar = 'A';
					
					// Loop through all letters and find closest active letter on left and right side
					for (int i = 0; i <= letters; i++)
					{
						JLabel currentLabel = jLetterLabels[i];
						
						if (browseAlbumAvailability.getAlbumsForLetter(String.format("%c", currentLabel.getText().charAt(0))) > 0)
						{	
							if (currentLabel.getBounds().getX() > leftClosestX &&
									currentLabel.getBounds().getX() < labelX)
							{
								leftClosestX = (int) currentLabel.getBounds().getX();
								leftSelectedChar = currentLabel.getText().charAt(0);
							}
							
							if (currentLabel.getBounds().getX() < rightClosestX &&
									currentLabel.getBounds().getX() > labelX)
							{
								rightClosestX = (int) currentLabel.getBounds().getX();
								rightSelectedChar = currentLabel.getText().charAt(0);
							}
						}
					}
					
					// Find out if the left or the right letter is the closest one
					int diffLeft = Math.abs(leftClosestX - labelX);
					int diffRight = Math.abs(rightClosestX - labelX);
					int newX = 0;
					
					if (diffLeft < diffRight)
					{
						newX = leftClosestX;
						selectedChar = leftSelectedChar;
					}
					else
					{
						newX = rightClosestX;
						selectedChar = rightSelectedChar;
					}
					
					// If the closest letter is the start or end position it may be inactive
					if (newX == letterStartX  &&
							browseAlbumAvailability.getAlbumsForLetter(String.format("%c", jLetterLabels[0].getText().charAt(0))) <= 0)
					{
						// There is no albums for the letter, find first active album
						for (int i = 0; i <= letters; i++)
						{
							JLabel currentLabel = jLetterLabels[i];
							
							if (browseAlbumAvailability.getAlbumsForLetter(String.format("%c", currentLabel.getText().charAt(0))) > 0)
							{
								selectedChar = currentLabel.getText().charAt(0);
								newX = (int)currentLabel.getBounds().getX();
								break;
							}
						}
					}
					else if (newX == letterEndX && browseAlbumAvailability.getAlbumsForLetter(String.format("%c", jLetterLabels[letters].getText().charAt(0))) <= 0)
					{
						// There is no albums for the letter, find last active album
						for (int i = letters; i >= 0; i--)
						{
							JLabel currentLabel = jLetterLabels[i];
							
							if (browseAlbumAvailability.getAlbumsForLetter(String.format("%c", currentLabel.getText().charAt(0))) > 0)
							{	
								selectedChar = currentLabel.getText().charAt(0);
								newX = (int)currentLabel.getBounds().getX();
								break;
							}
						}
					}
					
					jLetterSelectedLabel.setBounds(newX,
							(int)jLetterSelectedLabel.getBounds().getY(),
							(int)jLetterSelectedLabel.getBounds().getWidth(),
							(int)jLetterSelectedLabel.getBounds().getHeight());
					
					setSelectedLetter(String.format("%c", selectedChar));
				}
				
				double diff = Math.abs(pressed.getX() - me.getPoint().getX());
				if (me.getPoint().getY() > 800 && pressed.getY() > 800
						&& Math.abs(me.getPoint().getY() - pressed.getY()) < 200 && diff > 100) {
					
					if (pressed.getX() < me.getPoint().getX()) {
						scrollBack();
					} else {
						scrollNext();
					}
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent me)
			{
				exitTimer.restart(); // Restart exit timer
				
				if (confirmPanel.isVisible() || searchResultsPanel.isVisible())
					return;
				
				if (me.getY() > 140 && me.getY() < 250)
				{
					jLetterSelectedLabel.setBounds(me.getX(),
						(int)jLetterSelectedLabel.getBounds().getY(),
						(int)jLetterSelectedLabel.getBounds().getWidth(),
						(int)jLetterSelectedLabel.getBounds().getHeight());
				}
			}

			public void mouseMoved(MouseEvent me) {
				exitTimer.restart(); // Restart exit timer
			}
		});

		this.setMaximumSize(new java.awt.Dimension(1920, 1080));
		this.setMinimumSize(new java.awt.Dimension(1920, 1080));
		this.setName("BrowseAlbumsView"); // NOI18N
		this.setPreferredSize(new java.awt.Dimension(1920, 1080));
		this.setSize(new java.awt.Dimension(1920, 1080));

		jLayeredPane.setBounds(new java.awt.Rectangle(0, 0, 1920, 1080));
		jLayeredPane.setMaximumSize(new java.awt.Dimension(1920, 1080));
		jLayeredPane.setName("jLayeredPane"); // NOI18N
		
		confirmPanel.setBounds(this.getWidth() / 2 - confirmPanel.getWidth() / 2,
				this.getHeight() / 2 - confirmPanel.getHeight() / 2,
				confirmPanel.getWidth(), confirmPanel.getHeight());
		
		jLayeredPane.add(confirmPanel, javax.swing.JLayeredPane.MODAL_LAYER);
		
		keyboardPanel.setBounds(this.getWidth() / 2 - keyboardPanel.getWidth() / 2,
				this.getHeight() - keyboardPanel.getHeight() - 50,
				keyboardPanel.getWidth(), keyboardPanel.getHeight());
		keyboardPanel.setVisible(false);
		jLayeredPane.add(keyboardPanel, javax.swing.JLayeredPane.POPUP_LAYER);
		
		searchResultsPanel.setBounds(this.getWidth() / 2 - searchResultsPanel.getWidth() / 2,
				110, searchResultsPanel.getWidth(), searchResultsPanel.getHeight());
		jLayeredPane.add(searchResultsPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLayeredPane.add(searchBoxPanel, javax.swing.JLayeredPane.MODAL_LAYER);
		jLayeredPane.add(scrollIndicator, javax.swing.JLayeredPane.MODAL_LAYER);

		jExitButton.setIcon(resourceMap.getIcon("jExitButton.icon"));
		jExitButton.setText("");
		jExitButton.setFocusPainted(false);
		jExitButton.setMargin(new Insets(0, 0, 0, 0));
		jExitButton.setContentAreaFilled(false);
		jExitButton.setBorderPainted(false);
		jExitButton.setOpaque(false);
		jExitButton.setName("jExitButton"); // NOI18N
		jExitButton.setBounds(52, 32, 100, 100);
		jExitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitButtonClicked(evt);
			}
		});
		jLayeredPane.add(jExitButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		int x = letterStartX;
		
		char letter = 'A';
		for (int i = 0; i <= letters; i++) {
			jLetterLabels[i] = new javax.swing.JLabel(String.format("%c",
					letter));
			jLetterLabels[i].setFont(new Font("Thonburi", Font.PLAIN, 48));
			jLetterLabels[i].setForeground(Color.GRAY);
			jLetterLabels[i].setBounds(x, 155, letterWidth, 55);
			jLetterLabels[i].setHorizontalAlignment(javax.swing.JLabel.CENTER);

			jLayeredPane.add(jLetterLabels[i],
					javax.swing.JLayeredPane.DEFAULT_LAYER);

			switch (i) {
			case 25:
				letter = '®';
				break;

			case 26:
				letter = '¯';
				break;

			case 27:
				letter = '';
				break;

			case 28:
				letter = '#';
				break;

			default:
				letter++;
				break;
			}

			x += letterWidth;
		}
		
		leftLoadingIndicator.setBounds(45 + (880 / 2 - leftLoadingIndicator.getWidth() / 2),
				250 + (800 / 2 - leftLoadingIndicator.getHeight() / 2),
				leftLoadingIndicator.getWidth(), leftLoadingIndicator.getHeight());
		leftLoadingIndicator.setVisible(false);
		jLayeredPane.add(leftLoadingIndicator,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		rightLoadingIndicator.setBounds(975 + (880 / 2 - rightLoadingIndicator.getWidth() / 2),
				250 + (800 / 2 - leftLoadingIndicator.getHeight() / 2),
				rightLoadingIndicator.getWidth(), rightLoadingIndicator.getHeight());
		rightLoadingIndicator.setVisible(false);
		jLayeredPane.add(rightLoadingIndicator,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLetterSelectedLabel.setIcon(resourceMap.getIcon("jLetterSelectedLabel.icon"));
		jLetterSelectedLabel.setText("");
		jLetterSelectedLabel.setName("jLetterSelectedLabel");
		jLetterSelectedLabel.setSize(new java.awt.Dimension(65, 65));
		jLetterSelectedLabel.setBounds(letterStartX, 155 - 2, 65, 65);
		jLetterSelectedLabel.setVisible(false);
		jLayeredPane.add(jLetterSelectedLabel,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLabelBackground.setIcon(resourceMap.getIcon("jLabelBackground.icon")); // NOI18N
		jLabelBackground
				.setText(resourceMap.getString("jLabelBackground.text")); // NOI18N
		jLabelBackground.setName("jLabelBackground"); // NOI18N
		jLabelBackground.setSize(new java.awt.Dimension(1920, 1080));
		jLabelBackground.setBounds(0, 0, 1920, 1080);
		jLayeredPane.add(jLabelBackground,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(thisLayout);
		thisLayout.setHorizontalGroup(thisLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				thisLayout.createSequentialGroup().add(jLayeredPane,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1920,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(0, Short.MAX_VALUE)));
		thisLayout.setVerticalGroup(thisLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				thisLayout.createSequentialGroup().add(jLayeredPane,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1080,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
	}

	public void setBrowseViewVisible(boolean visible) {
		scrollIndicator.setVisible(visible);
		
		char letter = 'A';
		for (int i = 0; i <= letters; i++)
		{
			jLetterLabels[i].setVisible(visible);
			
			switch (i)
			{
				case 25:
					letter = '®';
					break;

				case 26:
					letter = '¯';
					break;

				case 27:
					letter = '';
					break;

				case 28:
					letter = '#';
					break;

				default:
					letter++;
					break;
			}
		}
		
		if (!visible)
		{
			jLetterSelectedLabel.setVisible(visible);
			leftLoadingIndicator.setVisible(visible);
			rightLoadingIndicator.setVisible(visible);
		
			if (leftPanel != null)
			{
				leftPanel.setVisible(false);
				jLayeredPane.remove(leftPanel);
				leftPanel = null;
			}
			
			if (rightPanel != null)
			{
				rightPanel.setVisible(false);
				jLayeredPane.remove(rightPanel);
				rightPanel = null;
			}
		}
		else
		{
			jLetterSelectedLabel.setVisible(false);
			this.setSelectedLetter("A");
		}
		
		jLayeredPane.repaint();
	}
	
	private void setSelectedLetter(String letter)
	{
		this.selectedIndex = 0;
		this.setSelectedLetter(letter, 0);
	}
	
	private void setSelectedLetter(String letter, int selectedIndex)
	{
		int albumCount = browseAlbumAvailability.getAlbumsForLetter(letter);
		
		// If to low index value...
		if (selectedIndex < 0)
			selectedIndex = 0;
		
		// If to large index value..
		if (selectedIndex >= albumCount)
			selectedIndex = albumCount - 1;
		
		// Set scroll indicator
		scrollIndicator.setDots((int)Math.ceil(albumCount / 2.0), selectedIndex / 2);
		
		if (albumCount > 0) {
			// If the letter selected label is not yet placed, place it with the current letter
			if (!jLetterSelectedLabel.isVisible())
			{
				for (int i = 0; i <= letters; i++)
				{
					javax.swing.JLabel label = jLetterLabels[i];
					
					if (label.getText().equalsIgnoreCase(letter))
					{
						jLetterSelectedLabel.setBounds((int)label.getBounds().getX(), (int)jLetterSelectedLabel.getBounds().getY(),
								jLetterSelectedLabel.getWidth(), jLetterSelectedLabel.getHeight());
						break;
					}
				}
				jLetterSelectedLabel.setVisible(true);
			}
				
			selectedLetter = letter;
			
			try {
				if (leftPanel != null)
				{
					leftPanel.setVisible(false);
					jLayeredPane.remove(leftPanel);
					leftPanel = null;
				}
				
				if (rightPanel != null)
				{
					rightPanel.setVisible(false);
					jLayeredPane.remove(rightPanel);
					rightPanel = null;
				}
				jLayeredPane.revalidate();
				
				leftLoadingIndicator.setVisible(true);
				touchView.connection.browseAlbum(letter, selectedIndex);

				if (albumCount - 1 > selectedIndex) {
					selectedIndex++;
					
					rightLoadingIndicator.setVisible(true);
					touchView.connection.browseAlbum(letter, selectedIndex);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			char l = letter.charAt(0);
			if (l < 'Z') {
				l++;
				setSelectedLetter(String.format("%c", l));
			}
		}
	}
	
	private void scrollNext()
	{
		int albumCount = browseAlbumAvailability.getAlbumsForLetter(selectedLetter);
		
		if ((selectedIndex + 2) > albumCount - 1)
		{
			return;
		}
		
		selectedIndex += 2;
		
		if (leftPanel != null)
		{
			leftPanel.setVisible(false);
			jLayeredPane.remove(leftPanel);
			leftPanel = null;
		}
		
		if (rightPanel != null)
		{
			rightPanel.setVisible(false);
			jLayeredPane.remove(rightPanel);
			rightPanel = null;
		}
		jLayeredPane.revalidate();
		
		setSelectedLetter(selectedLetter, selectedIndex);
	}
	
	private void scrollBack()
	{
		if (selectedIndex < 2)
			return;
		
		selectedIndex -= 2;
		
		if (leftPanel != null)
		{
			leftPanel.setVisible(false);
			jLayeredPane.remove(leftPanel);
			leftPanel = null;
		}
		
		if (rightPanel != null)
		{
			rightPanel.setVisible(false);
			jLayeredPane.remove(rightPanel);
			rightPanel = null;
		}
		jLayeredPane.revalidate();
		
		setSelectedLetter(selectedLetter, selectedIndex);
	}

	public void albumBrowseUpdate(AlbumBrowseUpdate albumBrowseUpdate)
	{
		if (searchResultsPanel.isVisible())
			return;
		
		if (albumBrowseUpdate.getArtist().toLowerCase().startsWith(selectedLetter.toLowerCase())
				&& !keyboardPanel.isVisible() && albumBrowseUpdate.getIndex() == selectedIndex || albumBrowseUpdate.getIndex() == selectedIndex + 1)
		{
			if (leftPanel == null) {
				leftPanel = new AlbumPanel(this, albumBrowseUpdate.getAlbumImage(),
						albumBrowseUpdate.getArtist(),
						albumBrowseUpdate.getAlbum(), albumBrowseUpdate
								.getTrackList());
				leftPanel.setBounds(new java.awt.Rectangle(45, 250, 880, 800));
				leftPanel.setMaximumSize(new java.awt.Dimension(880, 800));
				leftPanel.setName("LeftAlbumPanel");
				
				leftLoadingIndicator.setVisible(false);
				jLayeredPane.add(leftPanel, javax.swing.JLayeredPane.MODAL_LAYER);
			} else if (rightPanel == null) {
				rightPanel = new AlbumPanel(this, albumBrowseUpdate.getAlbumImage(),
						albumBrowseUpdate.getArtist(),
						albumBrowseUpdate.getAlbum(), albumBrowseUpdate
								.getTrackList());
				rightPanel.setBounds(new java.awt.Rectangle(975, 250, 880, 800));
				rightPanel.setMaximumSize(new java.awt.Dimension(880, 800));
				rightPanel.setName("RightAlbumPanel");
				
				rightLoadingIndicator.setVisible(false);
				jLayeredPane.add(rightPanel, javax.swing.JLayeredPane.MODAL_LAYER);
			}
		}
	}

	public void exitButtonClicked(ActionEvent evt)
	{	
		if (!confirmPanel.isVisible())
		{
			if (!keyboardPanel.isVisible())
			{
				this.setVisible(false);
			}
			else
			{
				setBrowseViewVisible(true);
			
				searchResultsPanel.setTracks(null);
				searchBoxPanel.setInactive(true);
				searchResultsPanel.setVisible(false);
				keyboardPanel.setVisible(false);
			}
		}
	}
	
	public void playTrack(int trackId, String trackTitle)
	{
		if (confirmPanel.isVisible())
			return;
		
		// requestSong() is called if "Yes" button is clicked.
		currentTrackId = trackId;
		confirmPanel.setMessage("Do you want to put " + trackTitle + " in the queue?");
		confirmPanel.showDialog();
	}
	
	public void searchQuery(String query)
	{
		if (query.length() < 2)
		{
			searchResultsPanel.setTracks(null);
			return;
		}
		
		searchResultsPanel.setSearching(true);
		lastSearchQuery = query;
		touchView.connection.searchQuery(query);
	}
	
	public void searchResultUpdate(SearchResultUpdate searchResultUpdate)
	{
		if (lastSearchQuery != null && lastSearchQuery.equals(searchResultUpdate.getQuery()))
		{
			searchResultsPanel.setSearching(false);
			searchResultsPanel.setTracks(searchResultUpdate.getTracks());
		}
	}

	public void setAlbumAvailability(BrowseAlbumAvailability browseAlbumAvailability)
	{
		this.browseAlbumAvailability = browseAlbumAvailability;

		if (this.selectedLetter == null)
			this.setSelectedLetter("A");

		char letter = 'A';
		for (int i = 0; i <= letters; i++) {
			
			if (browseAlbumAvailability.getAlbumsForLetter(String.format("%c",
					letter)) > 0) {
				jLetterLabels[i].setForeground(Color.WHITE);
			} else {
				jLetterLabels[i].setForeground(Color.GRAY);
			}

			switch (i) {
			case 25:
				letter = '®';
				break;

			case 26:
				letter = '¯';
				break;

			case 27:
				letter = '';
				break;

			case 28:
				letter = '#';
				break;

			default:
				letter++;
				break;
			}
		}
	}
	
	public void requestSong()
	{
		if (currentTrackId >= 0)
		{
			touchView.connection.requestAudioTrack(currentTrackId);
		}
	}
	
	public void showView()
	{
		this.setVisible(true);
		this.setBrowseViewVisible(true);
		exitTimer.restart();
	}

	private javax.swing.JLabel jLabelBackground;
	public javax.swing.JLayeredPane jLayeredPane;
	private javax.swing.JButton jExitButton;
	private javax.swing.JLabel[] jLetterLabels;
	private javax.swing.JLabel jLetterSelectedLabel;
	public SearchBoxPanel searchBoxPanel;
	private ScrollIndicator scrollIndicator;
}
