package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class IconPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage icon;
	
	public IconPanel(BufferedImage icon, int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		setOpaque(false);
		this.icon = icon;
	}

	public IconPanel(String path, int size) {
		this.setPreferredSize(new Dimension(size, size));
		this.setMaximumSize(new Dimension(size, size));
		setOpaque(false);
		path = convertPath(path);
		
		try {
			icon = ImageIO.read(new File(path));
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println(path);
		}

	}
	
	public String convertPath(String path) {
		path = path.charAt(0)+path.substring(1).toLowerCase();
		path = "src/resources/"+path+".png";
		return path;
	}
	
	public void changeSize(int size) {
		this.setPreferredSize(new Dimension(size, size));
		this.setMaximumSize(new Dimension(size, size));
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		revalidate();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(icon != null && isEnabled()) {
			g.drawImage(icon, 0, 0, getWidth(), getHeight(), null);	
		}
	}
}