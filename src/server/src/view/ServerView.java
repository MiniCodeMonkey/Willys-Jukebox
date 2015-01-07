/*
 * ServerView.java
 */

package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.AudioTrack;
import model.DataException;
import model.QueueList;
import model.TrackList;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

import view.settings.SettingsView;
import control.AudioManager;
import control.Indexing;
import control.IndexingEvent;
import control.IndexingEventListener;
import control.PlaylistManagerException;
import control.SoundLevelMeasure;
import control.network.Server;

/**
 * The application's main frame.
 */
public class ServerView extends FrameView
{
	// Track search results
	ArrayList<AudioTrack> tracks;
	private boolean widescreenMode = false;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private final JFileChooser fileChooser = new JFileChooser();
	private AudioManager audioManager = new AudioManager(this);
	//private SoundLevelMeasure soundLevelMeasure = new SoundLevelMeasure(audioManager);
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ServerApp.class).getContext().getResourceMap(ServerView.class);
	public Server server = null;
	private Indexing indexing = null;
	private Color backgroundColor = new Color(0.30f, 0.30f, 0.30f);
	private Color textColor = new Color(0.90f, 0.90f, 0.90f);
	
	public ServerView(SingleFrameApplication app)
	{
		super(app);
		
		if (widescreenMode)
		{
			screenWidth = 1440;
			screenHeight = 900;
		}
		else
		{
			screenWidth = 1024;
			screenHeight = 768;
		}
		
		// Start server
		try
		{
			server = new Server(audioManager, this);
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			JOptionPane.showMessageDialog(this.getFrame(),
					"Could not start server.\nThis means that touch screens can't communicate and get information.\nPlease check if this application is already running.", "Communication error",
					JOptionPane.ERROR_MESSAGE);
		}
		
		// Init GUI components
		initComponents();
		
		// Update queue list in case the queue was saved and not empty
		updateQueueList();
	}
	
	@SuppressWarnings( { "unchecked", "serial" })
	private void initComponents()
	{
		
		mainPanel = new javax.swing.JPanel();
		jLayeredPane1 = new javax.swing.JLayeredPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		jSearchResultsTable = new javax.swing.JTable();
		jSearchTextField = new javax.swing.JTextField();
		jAlbumImage = new javax.swing.JLabel();
		jTitleLabel = new javax.swing.JLabel();
		jArtistLabel = new javax.swing.JLabel();
		jAlbumLabel = new javax.swing.JLabel();
		jButtonPlayPause = new javax.swing.JButton();
		jButtonSkip = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jQueueTable = new javax.swing.JTable();
		jButtonAddMusic = new javax.swing.JButton();
		jButtonSettings = new javax.swing.JButton();
		jButtonInfo = new javax.swing.JButton();
		jProgressIndicator = new javax.swing.JProgressBar();
		jProgressIndicatorLabel = new javax.swing.JLabel();
		jSearchStatusLabel = new javax.swing.JLabel();
		jDeleteButton = new javax.swing.JButton();
		jElapsedLabel = new javax.swing.JLabel();
		jRemainingLabel = new javax.swing.JLabel();
		jTrackProgressBar = new javax.swing.JProgressBar();
		jVolumeProgressBar = new javax.swing.JProgressBar();
		jVolumeDescLabel = new javax.swing.JLabel();
		
		this.getFrame().addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				/*int result = JOptionPane.showConfirmDialog(null, "Do you want to save the current\nprogram state for next launch?\n(Settings, track List, queue, history, etc.)", "Save state?", JOptionPane.YES_NO_OPTION);
				
				if (result != JOptionPane.YES_OPTION)
				{
					// Delete database file
					new File("jukebox.db").delete();
				}*/
			}
		});
		
		mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
		mainPanel.setMaximumSize(new java.awt.Dimension(screenWidth, screenHeight));
		mainPanel.setMinimumSize(new java.awt.Dimension(screenWidth, screenHeight));
		mainPanel.setName("mainPanel"); // NOI18N
		mainPanel.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
		mainPanel.setSize(new java.awt.Dimension(screenWidth, screenHeight));
		mainPanel.setBackground(Color.black);
		
		jLayeredPane1.setBounds(new java.awt.Rectangle(0, 0, screenWidth, screenHeight));
		jLayeredPane1.setMaximumSize(new java.awt.Dimension(screenWidth, screenHeight));
		jLayeredPane1.setName("jLayeredPane1"); // NOI18N
		jLayeredPane1.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
		
		jScrollPane1.setName("jScrollPane1"); // NOI18N
		
		jSearchResultsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Title", "Artist", "Album" })
		{
			Class[] types = new Class[] { java.lang.Object.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false };
			
			public Class getColumnClass(int columnIndex)
			{
				return types[columnIndex];
			}
			
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return canEdit[columnIndex];
			}
			
			
		});
		jSearchResultsTable.setName("jSearchResultsTable"); // NOI18N
		jSearchResultsTable.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				searchResultsTableClicked(evt);
			}
		});
		
		class RowColorRenderer extends DefaultTableCellRenderer 
		{
			public Component getTableCellRendererComponent(  
					JTable table, Object value, boolean isSelected, 
					boolean hasFocus, int row, int col)
					{
					     Component comp = super.getTableCellRendererComponent(
					                      table,  value, isSelected, hasFocus, row, col);

					     comp.setBackground(backgroundColor);
					     comp.setForeground(textColor);

					     return( comp );
					 }
		}
		
		jSearchResultsTable.setDefaultRenderer(java.lang.Object.class, new RowColorRenderer());
		jSearchResultsTable.setGridColor(backgroundColor);
		jSearchResultsTable.setCellSelectionEnabled(false);
		jSearchResultsTable.setRowSelectionAllowed(true);
		
		
		jScrollPane1.setViewportView(jSearchResultsTable);
		jScrollPane1.setBounds((widescreenMode ? 730 : 530), 80, (widescreenMode ? 690 : 490), (widescreenMode ? 660 : 560));
		jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
		
		jLayeredPane1.add(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
        jScrollPane1.getViewport().setBackground(backgroundColor);
        
		
		jSearchTextField.setBackground(backgroundColor);
		jSearchTextField.setFont(resourceMap.getFont("jSearchTextField.font")); // NOI18N
		jSearchTextField.setText("Search title/artist/album..."); // NOI18N
		jSearchTextField.setForeground(Color.gray);
		jSearchTextField.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
		
		jSearchTextField.addFocusListener(new FocusListener()
		{
			
			@Override
			public void focusLost(FocusEvent e)
			{
				if(ServerView.this.jSearchTextField.getText().equals(""))
				{
					ServerView.this.jSearchTextField.setText("Search title/artist/album...");
					ServerView.this.jSearchTextField.setForeground(Color.gray);
				}
				
				ServerView.this.searchTextFieldKeyReleased(null);
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				if (ServerView.this.jSearchTextField.getText().equals("Search title/artist/album..."))
				{
					ServerView.this.jSearchTextField.setText("");
				}
				
				ServerView.this.jSearchTextField.setForeground(textColor);
			}
		});
		jSearchTextField.setName("jSearchTextField"); // NOI18N
		jSearchTextField.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyReleased(java.awt.event.KeyEvent evt)
			{
				searchTextFieldKeyReleased(evt);
			}
		});
		jSearchTextField.setBounds((widescreenMode ? 730 : 530), 30, (widescreenMode ? 690 : 490), 34);
		jLayeredPane1.add(jSearchTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jAlbumImage.setIcon(resourceMap.getIcon("jAlbumImage.icon")); // NOI18N
		jAlbumImage.setText(resourceMap.getString("jAlbumImage.text")); // NOI18N
		jAlbumImage.setName("jAlbumImage"); // NOI18N
		jAlbumImage.setBounds(20, 10, 188, 188);
		jLayeredPane1.add(jAlbumImage, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jTitleLabel.setFont(resourceMap.getFont("jTitleLabel.font")); // NOI18N
		jTitleLabel.setText(resourceMap.getString("jTitleLabel.text")); // NOI18N
		jTitleLabel.setAlignmentX(0.5F);
		jTitleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jTitleLabel.setName("jTitleLabel"); // NOI18N
		jTitleLabel.setBounds(230, 10, (widescreenMode ? 460 : 290), 56);
		jTitleLabel.setForeground(textColor);
		jLayeredPane1.add(jTitleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jArtistLabel.setFont(resourceMap.getFont("jArtistLabel.font")); // NOI18N
		jArtistLabel.setText(resourceMap.getString("jArtistLabel.text")); // NOI18N
		jArtistLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jArtistLabel.setName("jArtistLabel"); // NOI18N
		jArtistLabel.setBounds(230, 70, (widescreenMode ? 460 : 290), 42);
		jArtistLabel.setForeground(textColor);
		jLayeredPane1.add(jArtistLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jAlbumLabel.setFont(resourceMap.getFont("jAlbumLabel.font")); // NOI18N
		jAlbumLabel.setText(resourceMap.getString("jAlbumLabel.text")); // NOI18N
		jAlbumLabel.setName("jAlbumLabel"); // NOI18N
		jAlbumLabel.setBounds(230, 120, (widescreenMode ? 460 : 290), 42);
		jAlbumLabel.setForeground(textColor);
		jLayeredPane1.add(jAlbumLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jElapsedLabel.setName("jElapsedLabel");
		jElapsedLabel.setBounds(20, 202, 40, 20);
		jElapsedLabel.setFont(resourceMap.getFont("timeLabels.font"));
		jElapsedLabel.setForeground(textColor);
		jLayeredPane1.add(jElapsedLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jRemainingLabel.setName("jRemainingLabel");
		jRemainingLabel.setBounds((widescreenMode ? 620 : 470), 202, 50, 20);
		jRemainingLabel.setFont(resourceMap.getFont("timeLabels.font"));
		jRemainingLabel.setForeground(textColor);
		jLayeredPane1.add(jRemainingLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jTrackProgressBar.setName("jTrackProgressBar");
		jTrackProgressBar.setBounds(65, 202, (widescreenMode ? 550 : 400), 20);
		jTrackProgressBar.setMaximum(100);
		jTrackProgressBar.setValue(0);
		jTrackProgressBar.setVisible(false);
		jTrackProgressBar.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(java.awt.event.MouseEvent e)
			{
				int position = (int) (e.getX() / (float) jTrackProgressBar.getSize().width * jTrackProgressBar.getMaximum());
				position = Math.min(position, jTrackProgressBar.getMaximum());
				
				audioManager.setPosition(position);
			}
		});
		jTrackProgressBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(java.awt.event.MouseEvent e)
			{
				int position = (int) (e.getX() / (float) jTrackProgressBar.getSize().width * jTrackProgressBar.getMaximum());
				position = Math.min(position, jTrackProgressBar.getMaximum());
				
				audioManager.setPosition(position);
			}
		});
		jLayeredPane1.add(jTrackProgressBar, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonPlayPause.setIcon(resourceMap.getIcon("jButtonPlayPausePlay.icon")); // NOI18N
		jButtonPlayPause.setText(resourceMap.getString("jButtonPlayPause.text")); // NOI18N
		jButtonPlayPause.setFocusPainted(false);
		jButtonPlayPause.setMargin(new Insets(0, 0, 0, 0));
		jButtonPlayPause.setContentAreaFilled(false);
		jButtonPlayPause.setBorderPainted(false);
		jButtonPlayPause.setOpaque(false);
		jButtonPlayPause.setMaximumSize(new java.awt.Dimension(64, 64));
		jButtonPlayPause.setMinimumSize(new java.awt.Dimension(64, 64));
		jButtonPlayPause.setName("jButtonPlayPause"); // NOI18N
		jButtonPlayPause.setPreferredSize(new java.awt.Dimension(64, 64));
		jButtonPlayPause.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				playPauseButtonClicked(evt);
			}
		});
		jButtonPlayPause.setBounds(20, 230, 64, 64);
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				jButtonPlayPause.requestFocus();
				
			}
		});
		
		jLayeredPane1.add(jButtonPlayPause, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonSkip.setIcon(resourceMap.getIcon("jButtonSkip.icon")); // NOI18N
		jButtonSkip.setBorderPainted(false);
		jButtonSkip.setFocusPainted(false);
		jButtonSkip.setMargin(new Insets(0, 0, 0, 0));
		jButtonSkip.setContentAreaFilled(false);
		jButtonSkip.setBorderPainted(false);
		jButtonSkip.setOpaque(false);
		jButtonSkip.setMaximumSize(new java.awt.Dimension(64, 64));
		jButtonSkip.setMinimumSize(new java.awt.Dimension(64, 64));
		jButtonSkip.setName("jButtonSkip"); // NOI18N
		jButtonSkip.setPreferredSize(new java.awt.Dimension(64, 64));
		jButtonSkip.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				nextButtonClicked(evt);
			}
		});
		jButtonSkip.setBounds(100, 230, 64, 64);
		jLayeredPane1.add(jButtonSkip, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		/*jVolumeProgressBar.setName("jVolumeProgressBar");
		jVolumeProgressBar.setBounds(20, 610, 170, 20);
		jVolumeProgressBar.setMaximum(200);
		jVolumeProgressBar.setValue(0);
		jLayeredPane1.add(jVolumeProgressBar, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jVolumeDescLabel.setName("jVolumeDescLabel");
		jVolumeDescLabel.setBounds(20, 630, 170, 20);
		jVolumeDescLabel.setText("Volume / Max Volume");
		jVolumeDescLabel.setFont(resourceMap.getFont("timeLabels.font"));
		jVolumeDescLabel.setForeground(textColor);
		jLayeredPane1.add(jVolumeDescLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);*/
		
		jScrollPane2.setName("jScrollPane2"); // NOI18N
		
		jQueueTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jQueueTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Queue position", "Title", "Artist", "Album" })
		{
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false };
			
			public Class getColumnClass(int columnIndex)
			{
				return types[columnIndex];
			}
			
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return canEdit[columnIndex];
			}
		});
		jQueueTable.setName("jQueueTable"); // NOI18N
		
		// Change width for first two columns
		jQueueTable.getColumnModel().getColumn(0).setMaxWidth(100);
		jQueueTable.getColumnModel().getColumn(1).setMinWidth(223);
		jQueueTable.setDefaultRenderer(java.lang.Object.class, new RowColorRenderer());
		jQueueTable.setGridColor(backgroundColor);
		jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
		
		jScrollPane2.getViewport().setBackground(backgroundColor);
		jScrollPane2.setViewportView(jQueueTable);
		jScrollPane2.setBounds(20, 320, (widescreenMode ? 650 : 500), 280);
		
		jLayeredPane1.add(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonAddMusic.setBackground(resourceMap.getColor("jButtonAddMusic.background")); // NOI18N
		jButtonAddMusic.setIcon(resourceMap.getIcon("jButtonAddMusic.icon")); // NOI18N
		jButtonAddMusic.setText(resourceMap.getString("jButtonAddMusic.text")); // NOI18N
		jButtonAddMusic.setFocusPainted(false);
		jButtonAddMusic.setMargin(new Insets(0, 0, 0, 0));
		jButtonAddMusic.setContentAreaFilled(false);
		jButtonAddMusic.setBorderPainted(false);
		jButtonAddMusic.setOpaque(false);
		jButtonAddMusic.setName("jButtonAddMusic"); // NOI18N
		jButtonAddMusic.setBounds(20, (widescreenMode ? 680 : 630), 64, 64);
		jButtonAddMusic.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addMusicButtonClicked(evt);
			}
		});
		jLayeredPane1.add(jButtonAddMusic, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonSettings.setBackground(Color.white); // NOI18N
		jButtonSettings.setIcon(resourceMap.getIcon("jButtonSettings.icon")); // NOI18N
		jButtonSettings.setText(""); // NOI18N
		jButtonSettings.setFocusPainted(false);
		jButtonSettings.setMargin(new Insets(0, 0, 0, 0));
		jButtonSettings.setContentAreaFilled(false);
		jButtonSettings.setBorderPainted(false);
		jButtonSettings.setOpaque(false);
		jButtonSettings.setName("jButtonSettings"); // NOI18N
		jButtonSettings.setBounds(116, (widescreenMode ? 680 : 630), 64, 64);
		jButtonSettings.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				//JOptionPane.showMessageDialog(ServerView.this.getFrame(), "You are not allowed to access Settings", "Permission denied", JOptionPane.INFORMATION_MESSAGE);
				settingsButtonClicked(evt); // FIXME: Disable for now
			}
		});
		jLayeredPane1.add(jButtonSettings, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonInfo.setBackground(resourceMap.getColor("jButtonInfo.background")); // NOI18N
		jButtonInfo.setIcon(resourceMap.getIcon("jButtonInfo.icon")); // NOI18N
		jButtonInfo.setText(resourceMap.getString("jButtonInfo.text")); // NOI18N
		jButtonInfo.setFocusPainted(false);
		jButtonInfo.setMargin(new Insets(0, 0, 0, 0));
		jButtonInfo.setContentAreaFilled(false);
		jButtonInfo.setBorderPainted(false);
		jButtonInfo.setOpaque(false);
		jButtonInfo.setName("jButtonInfo"); // NOI18N
		jButtonInfo.setBounds(212, (widescreenMode ? 680 : 630), 64, 64);
		jButtonInfo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				
			}
		});
		jLayeredPane1.add(jButtonInfo, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressIndicator.setBounds(310, (widescreenMode ? 710 : 660), 200, 30);
		jProgressIndicator.setIndeterminate(true);
		jProgressIndicator.setVisible(false);
		jLayeredPane1.add(jProgressIndicator, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressIndicatorLabel.setFont(resourceMap.getFont("jProgressIndicatorLabel.font")); // NOI18N
		jProgressIndicatorLabel.setText("");
		jProgressIndicatorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jProgressIndicatorLabel.setName("jProgressIndicatorLabel"); // NOI18N
		jProgressIndicatorLabel.setBounds(310, (widescreenMode ? 685 : 635), 350, 30);
		jProgressIndicatorLabel.setForeground(textColor);
		jLayeredPane1.add(jProgressIndicatorLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jSearchStatusLabel.setFont(resourceMap.getFont("jSearchStatusLabel.font")); // NOI18N
		jSearchStatusLabel.setText("");
		jSearchStatusLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jSearchStatusLabel.setName("jSearchStatusLabel"); // NOI18N
		jSearchStatusLabel.setBounds((widescreenMode ? 730 : 530), (widescreenMode ? 735 : 635), 350, 30);
		jSearchStatusLabel.setForeground(textColor);
		jLayeredPane1.add(jSearchStatusLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jDeleteButton.setFont(resourceMap.getFont("jDeleteButton.font"));
		//jDeleteButton.setIcon(resourceMap.getIcon("jDeleteButton.icon")); // NOI18N
		jDeleteButton.setText(resourceMap.getString("jDeleteButton.text")); // NOI18N
		jDeleteButton.setName("jDeleteButton"); // NOI18N
		jDeleteButton.setBounds((widescreenMode ? 537 : 387), 602, 135, 42);
		jDeleteButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteButtonClicked(evt);
			}
		});
		jLayeredPane1.add(jDeleteButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				mainPanelLayout.createSequentialGroup().add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, screenWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(1,
						Short.MAX_VALUE)));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				mainPanelLayout.createSequentialGroup().add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, screenHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		
		searchTextFieldKeyReleased(null);
		updateCurrentPlayingInfo();
		
		setComponent(mainPanel);
		
		javax.swing.Timer timer = new javax.swing.Timer(250, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateElapsedTime();
			}
		});
		
		timer.start();
	}
	
	protected void updateElapsedTime()
	{
		if (audioManager.isPlaying())
		{
			int elapsed = (int) audioManager.getElapsedTime();
			int duration = (int) audioManager.getDuration();
			int remaining = duration - elapsed;
			
			int elapsedSeconds = elapsed % 60;
			int elapsedMinutes = (elapsed - elapsedSeconds) / 60;
			jElapsedLabel.setText(String.format("%02d:%02d", elapsedMinutes, elapsedSeconds));
			
			int remainingSeconds = remaining % 60;
			int remainingMinutes = (remaining - remainingSeconds) / 60;
			jRemainingLabel.setText(String.format("-%02d:%02d", remainingMinutes, remainingSeconds));
			
			jTrackProgressBar.setVisible(true);
			jTrackProgressBar.setMaximum((int) audioManager.getDuration());
			jTrackProgressBar.setValue(elapsed);
		}
		else
		{
			jTrackProgressBar.setValue(0);
		}
		
		//jVolumeProgressBar.setValue(soundLevelMeasure.getVolume());
	}
	
	class SearchThread extends Thread
	{
		String query;
		ServerView serverView;
		
        SearchThread(String query, ServerView serverView)
        {
        	this.query = query;
        	this.serverView = serverView;
        }

        public void run()
        {
        	try
			{
				tracks = TrackList.getInstance().search(query);
			}
			catch (DataException e)
			{
				System.out.println("Exception " + e.getMessage());
				tracks = null;
			}
			
			serverView.searchResultsReceived();
        }
    }
	
	private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt)
	{
		if (evt == null || evt.getKeyCode() == KeyEvent.VK_ENTER)
		{
			String query = "";
			
			// Perform search query in thread
			if (!jSearchTextField.getText().equals("Search title/artist/album..."))
			{
				query = jSearchTextField.getText();
			}
			
			SearchThread searchThread = new SearchThread(query, this);
			searchThread.start();
		}
	}
	
	private void searchResultsReceived()
	{
		try
		{
			DefaultTableModel tableModel = (DefaultTableModel) jSearchResultsTable.getModel();
			tableModel.getDataVector().removeAllElements();
			jSearchResultsTable.repaint();
			
			if (tracks != null)
			{
				// Update status label
				if (tracks.size() == 1)
				{
					jSearchStatusLabel.setText(tracks.size() + "/" + TrackList.getInstance().count() + " result");
				}
				else
				{
					jSearchStatusLabel.setText(tracks.size() + "/" + TrackList.getInstance().count() + " results");
				}
				
				for (AudioTrack currentTrack : tracks)
				{
					tableModel.addRow(new Object[] { currentTrack, currentTrack.getArtist(), currentTrack.getAlbum() });
				}
			}
		}
		catch (DataException e)
		{
			JOptionPane.showMessageDialog(this.getFrame(), e.getMessage());
		}
	}
	
	public void updateQueueList()
	{
		// Send update to all clients
		try
		{
			server.sendPlaylistUpdate();
		}
		catch (Exception e)
		{
			// Do nothing if playlist update failed
		}
		
		DefaultTableModel tableModel = (DefaultTableModel) jQueueTable.getModel();
		tableModel.getDataVector().removeAllElements();
		jQueueTable.repaint();
		
		ArrayList<AudioTrack> tracks;
		try
		{
			tracks = QueueList.getInstance().getTracks();
		}
		catch (DataException e)
		{
			e.printStackTrace();
			return;
		}
		
		int num = 1;
		for (AudioTrack currentTrack : tracks)
		{
			tableModel.addRow(new Object[] { num, currentTrack, currentTrack.getArtist(), currentTrack.getAlbum() });
			num++;
		}
		
		jQueueTable.repaint();
	}
	
	public void updateCurrentPlayingInfo()
	{
		// Send update to all clients
		try
		{
			server.sendNowPlayingUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// Do nothing if now playing update failed
		}
		
		if (audioManager.getCurrentTrack() != null)
		{
			jTitleLabel.setText(audioManager.getCurrentTrack().getName());
			jArtistLabel.setText(audioManager.getCurrentTrack().getArtist());
			jAlbumLabel.setText(audioManager.getCurrentTrack().getAlbum());
			
			ImageIcon albumCover = audioManager.getCurrentTrack().getAlbumCoverImage();
			if (albumCover != null)
			{
				albumCover.setImage(albumCover.getImage().getScaledInstance(188, 188, java.awt.Image.SCALE_SMOOTH));
				
				jAlbumImage.setIcon(albumCover);
			}
			else
			{
				jAlbumImage.setIcon(resourceMap.getIcon("jAlbumImage.icon"));
			}
		}
		else
		{
			jTitleLabel.setText("No song chosen");
			jArtistLabel.setText("");
			jAlbumLabel.setText("");
			jAlbumImage.setIcon(resourceMap.getIcon("jAlbumImage.icon"));
		}
	}
	
	private void addMusicButtonClicked(java.awt.event.ActionEvent evt)
	{
		if (indexing != null)
		{
			JOptionPane.showMessageDialog(this.getFrame(), "Please wait until current scan is finished.", "Please wait", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			// Start new indexer thread
			indexing = new Indexing(fileChooser.getSelectedFile().getAbsolutePath());
			indexing.addIndexingEventListener(new IndexingEventListener()
			{
				@Override
				public void indexingEventOccurred(IndexingEvent evt)
				{
					try
					{
						DefaultTableModel tableModel = (DefaultTableModel) jSearchResultsTable.getModel();
						
						if (evt.getAudioTrack() != null)
						{
							AudioTrack currentTrack = evt.getAudioTrack();
							
							jProgressIndicatorLabel.setText("Scanning " + currentTrack.getName() + "...");
							
							// Add track to jSearchResultsTable
							tableModel.addRow(new Object[] { currentTrack, currentTrack.getArtist(), currentTrack.getAlbum() });
							
							// Update status label
							
							if (TrackList.getInstance().count() == 1)
							{
								jSearchStatusLabel.setText(TrackList.getInstance().count() + "/" + TrackList.getInstance().count() + " result");
							}
							else
							{
								jSearchStatusLabel.setText(TrackList.getInstance().count() + "/" + TrackList.getInstance().count() + " results");
							}
						}
						else if (evt.getPath() != null)
						{
							String[] folder = evt.getPath().split("/");
							jProgressIndicatorLabel.setText("Scanning " + folder[folder.length - 1] + "...");
						}
						else if (evt.getMessage() != null)
						{
							// Scanning done
							jProgressIndicator.setVisible(false);
							jProgressIndicatorLabel.setText("");
							server.sendBrowseAlbumAvailability();
							indexing = null;
							
							JOptionPane.showMessageDialog(ServerView.this.getFrame(), evt.getMessage(), "Scanning done", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					catch (DataException e)
					{
						JOptionPane.showMessageDialog(ServerView.this.getFrame(), e.getMessage());
					}
				}
			});
			
			indexing.start();
			
			// Show loading bar
			jProgressIndicator.setVisible(true);
			jProgressIndicatorLabel.setText("Scanner mappe for musik...");
		}
	}
	
	private void settingsButtonClicked(java.awt.event.ActionEvent evt)
	{
		
		SettingsView settingsView = new SettingsView(this.getFrame(), true);
		settingsView.setLocationRelativeTo(this.getFrame());
		ServerApp.getApplication().show(settingsView);
		
	}
	
	private void playPauseButtonClicked(java.awt.event.ActionEvent evt)
	{
		try
		{
			if (TrackList.getInstance().count() <= 0 && audioManager.getCurrentTrack() == null)
			{
				JOptionPane.showMessageDialog(this.getFrame(), "No music in index!\nAdd some music before pressing play.");
				
				return;
			}
		}
		catch (DataException e)
		{
			JOptionPane.showMessageDialog(this.getFrame(), e.getMessage());
			return;
		}
		
		if (!audioManager.isPlaying())
		{
			jButtonPlayPause.setIcon(resourceMap.getIcon("jButtonPlayPausePause.icon"));
			
			try
			{
				audioManager.play(false);
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
			updateCurrentPlayingInfo();
			updateQueueList();
		}
		else
		{
			audioManager.pause();
			
			jButtonPlayPause.setIcon(resourceMap.getIcon(audioManager.isPaused() ? "jButtonPlayPausePlay.icon" : "jButtonPlayPausePause.icon"));
			
			updateCurrentPlayingInfo();
			updateQueueList();
		}
	}
	
	private void searchResultsTableClicked(java.awt.event.MouseEvent evt)
	{
		if (evt.getClickCount() == 2) // Double clicked
		{
			int selectedRow = jSearchResultsTable.getSelectedRow();
			if (selectedRow >= 0)
			{
				AudioTrack selectedTrack = (AudioTrack) jSearchResultsTable.getValueAt(selectedRow, 0);
				
				QueueList queueList = QueueList.getInstance();
				try
				{
					queueList.addTrackToQueue(selectedTrack);
				}
				catch (PlaylistManagerException e)
				{
					JOptionPane.showMessageDialog(this.getFrame(), e.getMessage(), "Could not enqueue song", JOptionPane.ERROR_MESSAGE);
				}
				catch (DataException e)
				{
					JOptionPane.showMessageDialog(this.getFrame(), e.getMessage(), "Could not enqueue song", JOptionPane.ERROR_MESSAGE);
				}
				updateQueueList();
			}
			
			jSearchResultsTable.clearSelection();
		}
	}
	
	private void nextButtonClicked(java.awt.event.ActionEvent evt)
	{
		// Change to pause icon just in case the current song has been paused
		// (the next will start playing regardlessly)
		jButtonPlayPause.setIcon(resourceMap.getIcon("jButtonPlayPausePause.icon"));
		
		try
		{
			audioManager.next(false);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void deleteButtonClicked(java.awt.event.ActionEvent evt)
	{
		int selectedRow = jQueueTable.getSelectedRow();
		if (selectedRow >= 0)
		{
			AudioTrack selectedTrack = (AudioTrack) jQueueTable.getValueAt(selectedRow, 1);
			try
			{
				QueueList.getInstance().removeTrack(selectedTrack);
			}
			catch (DataException e)
			{
				JOptionPane.showMessageDialog(this.getFrame(), "Could not remove track from queue", e.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			updateQueueList();
		}
		
		jSearchResultsTable.clearSelection();
	}
	
	private javax.swing.JLabel jAlbumImage;
	private javax.swing.JLabel jAlbumLabel;
	private javax.swing.JLabel jArtistLabel;
	private javax.swing.JButton jButtonAddMusic;
	private javax.swing.JButton jButtonSettings;
	private javax.swing.JButton jButtonInfo;
	private javax.swing.JButton jButtonPlayPause;
	private javax.swing.JButton jButtonSkip;
	private javax.swing.JLayeredPane jLayeredPane1;
	private javax.swing.JTable jQueueTable;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTable jSearchResultsTable;
	private javax.swing.JTextField jSearchTextField;
	private javax.swing.JLabel jTitleLabel;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JProgressBar jProgressIndicator;
	private javax.swing.JLabel jProgressIndicatorLabel;
	private javax.swing.JLabel jSearchStatusLabel;
	private javax.swing.JButton jDeleteButton;
	private javax.swing.JLabel jElapsedLabel;
	private javax.swing.JLabel jRemainingLabel;
	private javax.swing.JProgressBar jTrackProgressBar;
	private javax.swing.JProgressBar jVolumeProgressBar;
	private javax.swing.JLabel jVolumeDescLabel;
}
