package com.omegasystems.de;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JPanel;

public class PixelCanvas extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	
	public BufferedImage img;
	
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
	    //g2d.clearRect(0, 0, Core.renderer.WIDTH, Core.renderer.HEIGHT);
	    g2d.drawImage(img, 0, 0, this);
	    
	    Iterator<Quad> iterator = Core.renderer.quadQueue.iterator();
	    while (iterator.hasNext()) {
	    	Quad quad = iterator.next();
	    	g2d.setColor(new Color(quad.color));
	    	g2d.fillPolygon(quad.getXcoords(), quad.getYcoords(), 4);
	    }
	    
	    g2d.setColor(new Color(0xf0f0f0));
	    g2d.drawString("FPS: " + Integer.toString(Core.fps), 5, 15);
	    g2d.drawString("WallC: " + Integer.toString(Core.renderer.debugWC), 5, 30);
	    g2d.drawString("WallD: " + Integer.toString(Core.renderer.debugWD), 5, 45);
	    g2d.drawString("tLC: " + Integer.toString(Core.deltaLogic), 5, 60);
	    g2d.drawString("tGC: " + Integer.toString(Core.deltaGraphic), 5, 75);
	    g2d.dispose();
    }
}