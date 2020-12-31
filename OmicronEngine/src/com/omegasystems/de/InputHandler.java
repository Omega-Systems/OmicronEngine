package com.omegasystems.de;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener	{
	
	public double forwardSpeed = 0.;
	public double rightSpeed = 0.;
	public double rotationSpeed = 0.;
	public double jump = 0.;
	public boolean debugInterrupt;
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if (keyCode == KeyEvent.VK_W) {
			forwardSpeed = 1.;
		}
		if (keyCode == KeyEvent.VK_S) {
			forwardSpeed = -1.;
		}
		if (keyCode == KeyEvent.VK_D) {
			rightSpeed = 1.;
		}
		if (keyCode == KeyEvent.VK_A) {
			rightSpeed = -1.;
		}
		if (keyCode == KeyEvent.VK_Q) {
			rotationSpeed = 1.;
		}
		if (keyCode == KeyEvent.VK_E) {
			rotationSpeed = -1.;
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			jump = 1.;
		}
		if (keyCode == KeyEvent.VK_I) {
			debugInterrupt = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if (keyCode == KeyEvent.VK_W) {
			forwardSpeed = Math.min(0., forwardSpeed);
		}
		if (keyCode == KeyEvent.VK_S) {
			forwardSpeed = Math.max(0., forwardSpeed);
		}
		if (keyCode == KeyEvent.VK_D) {
			rightSpeed = Math.min(0., rightSpeed);
		}
		if (keyCode == KeyEvent.VK_A) {
			rightSpeed = Math.max(0., rightSpeed);
		}
		if (keyCode == KeyEvent.VK_Q) {
			rotationSpeed = Math.min(0., rotationSpeed);
		}
		if (keyCode == KeyEvent.VK_E) {
			rotationSpeed = Math.max(0., rotationSpeed);
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			jump = 0.;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
