package view;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class JImageButton extends JButton {
	private static final long serialVersionUID = 675430992082833295L;
	Image backgroundImage;

	public JImageButton() {
		super();
	}

	public JImageButton(Action a) {
		super(a);
	}

	public JImageButton(Icon icon) {
		super(icon);
	}

	public JImageButton(String text) {
		super(text);
	}

	public JImageButton(String text, Icon icon) {
		super(text, icon);
	}

	public void setBackgroundImage(Image image) {
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
			backgroundImage = image;
		} catch (InterruptedException x) {
			System.err
					.println("Specified background image could not be loaded.");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Color saved = g.getColor();
		//g.setColor(getBackground());
		//g.fillRect(0, 0, getWidth(), getHeight());
		//g.setColor(saved);

		if (backgroundImage != null) {
			int imageX = (getWidth() - backgroundImage.getWidth(this)) / 2;
			int imageY = (getHeight() - backgroundImage.getHeight(this)) / 2;
			g.drawImage(backgroundImage, imageX, imageY, this);
		}

		if (!getText().equals("")) {
			FontMetrics fm = this.getFontMetrics(this.getFont());
			
			if (this.getHorizontalAlignment() == javax.swing.JButton.CENTER)
			{
				g.drawString(super.getText(), getWidth() / 2 - fm.stringWidth(super.getText()) / 2,
						getHeight() / 2 + (fm.getAscent()) / 2);
			}
			else
			{
				g.drawString(super.getText(), 5, getHeight() / 2 + 5);
			}
		}

		if (getIcon() != null) {
			Icon icon = getIcon();
			icon.paintIcon(this, g, 0, 0);
		}
	}

	public Dimension getPreferredSize() {
		Dimension oldSize = super.getPreferredSize();
		Dimension newSize = new Dimension();
		Dimension returnSize = new Dimension();

		if (backgroundImage != null) {
			newSize.width = backgroundImage.getWidth(this) + 1;
			newSize.height = backgroundImage.getHeight(this) + 1;
		}

		if (oldSize.height > newSize.height) {
			returnSize.height = oldSize.height;
		} else {
			returnSize.height = newSize.height;
		}

		if (oldSize.width > newSize.width) {
			returnSize.width = oldSize.width;
		} else {
			returnSize.width = newSize.width;
		}

		return (returnSize);
	}
}
