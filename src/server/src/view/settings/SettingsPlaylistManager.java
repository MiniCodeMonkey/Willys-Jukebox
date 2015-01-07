package view.settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SettingsPlaylistManager
{
	private int minValue = 1;
	private int maxValue = 300;
	private int stepValue = 1;
	private int queueSizeValue = 7;
	private int artistThrottlingValue = 1;
	private int songFrequencyValue = 15;
	private int artistFrequencyValue = 10;
	
	SpinnerNumberModel queueSizeModel = new SpinnerNumberModel(queueSizeValue,minValue,maxValue,stepValue);
	SpinnerNumberModel artistThrottlingModel = new SpinnerNumberModel(artistThrottlingValue,minValue,maxValue,stepValue);
	SpinnerNumberModel songFrequencyModel = new SpinnerNumberModel(songFrequencyValue,minValue,maxValue,stepValue);
	SpinnerNumberModel artistFrequencyModel = new SpinnerNumberModel(artistFrequencyValue,minValue,maxValue,stepValue);
	
	public JSpinner queueSizeSpinner = new JSpinner(queueSizeModel);
	public JSpinner artistThrottlingSpinner = new JSpinner(artistThrottlingModel);
	public JSpinner songFrequencySpinner = new JSpinner(songFrequencyModel);
	public JSpinner artistFrequencySpinner = new JSpinner(artistFrequencyModel);
	
	public JCheckBox checkBoxPlaylistManager;
	JLabel queueSizeLabel;
	JLabel artistThrottlingLabel;
	JLabel songFrequencyLabel;
	JLabel artistFrequencyLabel;
	JLabel infoLabel;
	JLabel minutesLabel;
	JLabel percentageLabel;
	
	protected JComponent SettingsPlaylist() {

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints();
		
		constraint.anchor = GridBagConstraints.WEST;
		
		//Checkbox
		checkBoxPlaylistManager = new JCheckBox("Enable Playlist Manager");
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.gridwidth = 3;
		panel.add(checkBoxPlaylistManager,constraint);
		constraint.gridwidth = 1;//reset the gridwith
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.gridwidth = 3;
		infoLabel = new JLabel("<html><font color=gray> The Playlist Manager lets you have more control of the music being played by letting <br>" +
														"you set some limits on the queue and on artists in your library. Furthermore you can <br>" +
														"set how many minutes should pass between a song or the percantage an artist can be <br>" +
														"played during a session</font></html>");
		panel.add(infoLabel, constraint);
		constraint.gridwidth = 1;
		
		//Queue size
		queueSizeSpinner.setEnabled(false);
		constraint.gridx = 0;
		constraint.gridy = 2;
		panel.add(queueSizeSpinner,constraint);	
		
		constraint.gridx = 2;
		constraint.gridy = 2;
		queueSizeLabel = new JLabel("Queue size");
		queueSizeLabel.setEnabled(false);
		panel.add(queueSizeLabel,constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 3;
		infoLabel = new JLabel("<html><font color=gray>Use the arrows to define how many songs <br>" +
				"			   the queue can contain</font></html>");
		panel.add(infoLabel, constraint);
		
		//Max. no of same artist in queue
		artistThrottlingSpinner.setEnabled(false);
		constraint.gridx = 0;
		constraint.gridy = 4;
		panel.add(artistThrottlingSpinner,constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 4;
		artistThrottlingLabel = new JLabel("Artist throttling");
		artistThrottlingLabel.setEnabled(false);
		panel.add(artistThrottlingLabel, constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 5;
		infoLabel = new JLabel("<html><font color=gray>Use the arrows to define how many songs by the<br>" +
							   "same artist can be put in the queue</font></html>");
		panel.add(infoLabel, constraint);
		
		//Songs played within defined amount of minutes cannot be played
		songFrequencySpinner.setEnabled(false);
		constraint.gridx = 0;
		constraint.gridy = 6;
		panel.add(songFrequencySpinner, constraint);
		
		
		constraint.gridx = 1;
		constraint.gridy = 6;
		minutesLabel = new JLabel(" min.   ");
		minutesLabel.setEnabled(false);
		panel.add(minutesLabel, constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 6;
		songFrequencyLabel = new JLabel("Song frequency");
		songFrequencyLabel.setEnabled(false);
		panel.add(songFrequencyLabel, constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 7;
		infoLabel = new JLabel("<html><font color=gray>Use the arrows to define how many minutes must elapse<br>" +
							   "before the same song can be played again</font></html>");
		panel.add(infoLabel, constraint);
		
		//Mac % of same artist played in a session
		artistFrequencySpinner.setEnabled(false);
		constraint.gridx = 0;
		constraint.gridy = 8;
		panel.add(artistFrequencySpinner, constraint);
		
		
		constraint.gridx = 1;
		constraint.gridy = 8;
		percentageLabel = new JLabel(" %    ");
		percentageLabel.setEnabled(false);
		panel.add(percentageLabel, constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 8;
		artistFrequencyLabel = new JLabel("Artist frequency");
		artistFrequencyLabel.setEnabled(false);
		panel.add(artistFrequencyLabel, constraint);
		
		constraint.gridx = 2;
		constraint.gridy = 9;
		infoLabel = new JLabel("<html><font color=gray>Use the arrows to define the percentage of how many times<br>" +
							   "the same artist can be played in a session</font></html>");
		panel.add(infoLabel, constraint);
        
		checkBoxPlaylistManager.addActionListener(checkBoxEnableActionListener);
		
        return panel;
    }
	
	public void setEnabled(boolean selected){
		queueSizeSpinner.setEnabled(selected);
		 queueSizeLabel.setEnabled(selected);
		 artistThrottlingSpinner.setEnabled(selected);
		 artistThrottlingLabel.setEnabled(selected);
		 songFrequencySpinner.setEnabled(selected);
		 songFrequencyLabel.setEnabled(selected);
		 artistFrequencySpinner.setEnabled(selected);
		 artistFrequencyLabel.setEnabled(selected);
		 minutesLabel.setEnabled(selected);
		 percentageLabel.setEnabled(selected);
	}
	
	ActionListener checkBoxEnableActionListener = new ActionListener(){

		 public void actionPerformed(ActionEvent e)
		 {
			 boolean selected = checkBoxPlaylistManager.isSelected();
			 
				 setEnabled(selected);
			 }
	 };
	 
	 
}
