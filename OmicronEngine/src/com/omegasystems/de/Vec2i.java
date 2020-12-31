package com.omegasystems.de;

public class Vec2i {
	public int x;
	public int y;
	
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2i(double x, double y) {
		this((int)x, (int)y);
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.x) + "," + Integer.toString(this.y);
	}
}
