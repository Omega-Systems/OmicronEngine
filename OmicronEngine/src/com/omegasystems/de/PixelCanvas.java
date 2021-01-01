package com.omegasystems.de;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class PixelCanvas extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	
	public static int frames;
	
	public BufferedImage img;
	Font font = new Font("Dialog", Font.PLAIN, 12);
	
	public PixelCanvas(long width, long height) {
		this.width = (int)width;
		this.height = (int)height;
		img = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
	}
	
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(width, height);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g.create();
	    g2d.setFont(font);
	    //g2d.clearRect(0, 0, Core.renderer.WIDTH, Core.renderer.HEIGHT);
	    g2d.drawImage(img, 0, 0, this);

	    for (Quad quad: Core.renderer.getQuadQueue()){
	    	g2d.setColor(new Color(quad.color));
	    	g2d.fillPolygon(quad.getXcoords(), quad.getYcoords(), 4);
	    }
	    
	    g2d.setColor(new Color(0xf0f0f0));
	    g2d.drawString("TPS: " + Integer.toString(Core.tps), 5, 15);
	    g2d.drawString("FPS: " + Integer.toString(Core.fps), 5, 30);
	    g2d.drawString("WallC: " + Integer.toString(Core.renderer.debugWC), 80, 15);
	    g2d.drawString("WallD: " + Integer.toString(Core.renderer.debugWD), 80, 30);
	    g2d.dispose();
	    
	    frames++;
    }
}
