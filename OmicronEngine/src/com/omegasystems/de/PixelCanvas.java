package com.omegasystems.de;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
	    g.setFont(font);
	    
	    //g2d.setBackground(new Color(0x404040));
	    g.setColor(new Color(0x404040));
	    g.fillRect(0, 0, Core.renderer.WIDTH, Core.renderer.HEIGHT);

	    for (Quad quad: Core.renderer.getQuadQueue()){
	    	g.setColor(new Color(quad.color));
	    	g.fillPolygon(quad.getXcoords(), quad.getYcoords(), 4);
	    }
	    
	    g.setColor(new Color(0xf0f0f0));
	    g.drawString("TPS: " + Integer.toString(Core.tps), 5, 15);
	    g.drawString("FPS: " + Integer.toString(Core.fps), 5, 30);
	    g.drawString("WallC: " + Integer.toString(Core.renderer.debugWC), 100, 15);
	    g.drawString("WallD: " + Integer.toString(Core.renderer.debugWD), 100, 30);
	    g.drawLine(Core.renderer.HALF_WIDTH, Core.renderer.HALF_HEIGHT - 4, Core.renderer.HALF_WIDTH, Core.renderer.HALF_HEIGHT + 4);
	    g.drawLine(Core.renderer.HALF_WIDTH - 4, Core.renderer.HALF_HEIGHT, Core.renderer.HALF_WIDTH + 4, Core.renderer.HALF_HEIGHT);
	    g.dispose();
	    
	    frames++;
    }
}
