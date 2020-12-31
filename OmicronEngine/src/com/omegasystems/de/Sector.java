package com.omegasystems.de;

import java.util.ArrayList;

public class Sector {
	public ArrayList<Integer> drawQueue;
	public ArrayList<Wall> walls;
	public Vec2 bottomLeft;
	public Vec2 topRight;
	public Vec2 origin;
	
	public Sector() {
		drawQueue = new ArrayList<Integer>();
		walls = new ArrayList<Wall>();
	}
}
