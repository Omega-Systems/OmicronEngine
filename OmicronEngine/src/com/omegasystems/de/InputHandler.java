package com.omegasystems.de;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener	{
	
	public double forwardSpeed = 0.;
	public double rightSpeed = 0.;
	public double yawSpeed = 0.;
	public double jump = 0.;
	public double crouch = 0.;
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_W: forwardSpeed = 	 1.; break;
		case KeyEvent.VK_S: forwardSpeed = 	-1.; break;
		case KeyEvent.VK_D: rightSpeed = 	 1.; break;
		case KeyEvent.VK_A: rightSpeed = 	-1.; break;
		case KeyEvent.VK_Q: yawSpeed = 		 1.; break;
		case KeyEvent.VK_E: yawSpeed = 		-1.; break;
		case KeyEvent.VK_SPACE: jump = 		 1.; break;
		case KeyEvent.VK_SHIFT: crouch = 	 1.; break;
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_W: forwardSpeed = 	Math.min(0., forwardSpeed); break;
		case KeyEvent.VK_S: forwardSpeed = 	Math.max(0., forwardSpeed); break;
		case KeyEvent.VK_D: rightSpeed =	Math.min(0., rightSpeed); break;
		case KeyEvent.VK_A: rightSpeed = 	Math.max(0., rightSpeed); break;
		case KeyEvent.VK_Q: yawSpeed = 		Math.min(0., yawSpeed); break;
		case KeyEvent.VK_E: yawSpeed = 		Math.max(0., yawSpeed); break;
		case KeyEvent.VK_SPACE: jump = 		0.; break;
		case KeyEvent.VK_SHIFT: crouch = 	0.; break;
		default: break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
