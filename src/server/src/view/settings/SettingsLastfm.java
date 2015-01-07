package view.settings;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class SettingsLastfm implements ItemListener
{

	protected JComponent SettingsLastfm() {
		JPanel panel = new JPanel(new GridBagLayout());
		return panel;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}
