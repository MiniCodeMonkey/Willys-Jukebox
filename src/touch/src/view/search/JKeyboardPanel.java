package view.search;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

import view.JImageButton;
import view.browse.BrowseAlbumsView;

public class JKeyboardPanel extends JLayeredPane {
	private static final long serialVersionUID = -4987339302468923826L;
	private ArrayList<JImageButton> keyboardButtons = new ArrayList<JImageButton>();
	private javax.swing.JButton backspaceButton = new javax.swing.JButton();
	private javax.swing.JButton spaceButton = new javax.swing.JButton();
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			JKeyboardPanel.class);
	
	public JKeyboardPanel(final BrowseAlbumsView browseAlbumsView)
	{
		final String buttons = "QWERTYUIOPÅ|ASDFGHJKLÆØ|ZXCVBNM";
		int x = 0;
		int y = 0;
		int buttonWidth = 75;
		int buttonHeight = 75;
		int margin = 10;
		
		for (int i = 0; i < buttons.length(); i++)
		{
			if (buttons.charAt(i) == '|')
			{
				x = 0;
				y += buttonHeight + margin;
				
				if (y == (buttonHeight + margin) * 2)
				{
					x += (buttonWidth + margin) * 2;
				}
				
				continue;
			}
			
			final String buttonChar = String.format("%c", buttons.charAt(i));
			JImageButton button = new JImageButton();
			button.setText(String.format("%c", buttons.charAt(i)).toUpperCase());
			button.setFocusPainted(false);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			button.setOpaque(false);
			button.setBounds(x, y, buttonWidth, buttonHeight);
			button.setBackgroundImage(resourceMap.getImageIcon("keyboardButtonBackground.icon").getImage());
			button.setFont(new Font("Thonburi", Font.PLAIN, 40));
			button.setHorizontalTextPosition(javax.swing.JButton.CENTER);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseAlbumsView.searchBoxPanel.addText(buttonChar.toLowerCase());
				}
				
			});
			x += buttonWidth + margin;
			this.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			
			keyboardButtons.add(button);
		}
		
		JImageButton button = new JImageButton();
		button.setText("123");
		button.setBounds(0, y, 113, 75);
		button.setBackgroundImage(resourceMap.getImageIcon("keyboardOneHalfBackground.icon").getImage());
		button.setFont(new Font("Thonburi", Font.PLAIN, 40));
		button.setHorizontalTextPosition(javax.swing.JButton.CENTER);
		//this.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
		//keyboardButtons.add(button);
		
		backspaceButton.setName("backspaceButton");
		backspaceButton.setText("");
		backspaceButton.setFocusPainted(false);
		backspaceButton.setMargin(new Insets(0, 0, 0, 0));
		backspaceButton.setContentAreaFilled(false);
		backspaceButton.setBorderPainted(false);
		backspaceButton.setOpaque(false);
		backspaceButton.setBounds(11 * (buttonWidth + margin) - 113 - margin, y + buttonHeight + margin, 113, 75);
		backspaceButton.setIcon(resourceMap.getIcon("backspaceButton.icon"));
		backspaceButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				browseAlbumsView.searchBoxPanel.removeLastLetter();
			}
			
		});
		this.add(backspaceButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		spaceButton.setName("spaceButton");
		spaceButton.setText("");
		spaceButton.setFocusPainted(false);
		spaceButton.setMargin(new Insets(0, 0, 0, 0));
		spaceButton.setContentAreaFilled(false);
		spaceButton.setBorderPainted(false);
		spaceButton.setOpaque(false);
		spaceButton.setBounds(((buttonWidth + margin) * 11) / 2 - 525 / 2, y + buttonHeight + margin, 525, 75);
		spaceButton.setIcon(resourceMap.getIcon("spaceButton.icon"));
		spaceButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				browseAlbumsView.searchBoxPanel.addText(" ");
			}
			
		});
		this.add(spaceButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		final String numpad = "789|456|123|0.-";
		int startX = (buttonWidth + margin) * 11 + (margin * 2);
		x = startX;
		y = 0;
		
		for (int i = 0; i < numpad.length(); i++)
		{
			if (numpad.charAt(i) == '|')
			{
				x = startX;
				y += buttonHeight + margin;
				continue;
			}
			
			final String buttonChar = String.format("%c", numpad.charAt(i));
			button = new JImageButton();
			button.setText(String.format("%c", numpad.charAt(i)));
			button.setFocusPainted(false);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			button.setOpaque(false);
			button.setBounds(x, y, buttonWidth, buttonHeight);
			button.setBackgroundImage(resourceMap.getImageIcon("keyboardButtonBackground.icon").getImage());
			button.setFont(new Font("Thonburi", Font.PLAIN, 40));
			button.setHorizontalTextPosition(javax.swing.JButton.CENTER);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseAlbumsView.searchBoxPanel.addText(buttonChar.toLowerCase());
				}
				
			});
			x += buttonWidth + margin;
			this.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			
			keyboardButtons.add(button);
		}
		
		this.setBounds(0, 0, 14 * (buttonWidth + margin) + (margin * 2), 4 * (buttonHeight + margin));
	}
}
