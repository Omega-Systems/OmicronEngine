package com.omegasystems.de;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private PixelCanvas pixelCanvas;
	private InputHandler inputHandler;
	
    public Window(long width, long height, String title) 
    {
    	pixelCanvas = new PixelCanvas(width, height);
    	inputHandler = new InputHandler();
    	
    	JFrame frame = new JFrame(title);
    	frame.add(pixelCanvas);
    	frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    frame.setFocusable(true);
	    frame.addKeyListener(inputHandler);
    }
    
    public PixelCanvas getPixelCanvas() {
    	return pixelCanvas;
    }
    
    public InputHandler getInputHandler() {
    	return inputHandler;
    }
}