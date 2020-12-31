package com.omegasystems.de;

public class Quad {
	public Vec2i a;
	public Vec2i b;
	public Vec2i c;
	public Vec2i d;
	public int color;
	
	public Quad(Vec2i a, Vec2i b, Vec2i c, Vec2i d, int color) {
		if (a.y < 0) {
			System.out.println(a.y);
		}
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.color = color;
	}
	
	public int[] getXcoords() {
		return new int[] {a.x, b.x, c.x, d.x};
	}
	
	public int[] getYcoords() {
		return new int[] {a.y, b.y, c.y, d.y};
	}
}
