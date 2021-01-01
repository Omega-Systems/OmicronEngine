package com.omegasystems.de;

public class Vec2 {
	public double x;
	public double y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2 add(Vec2 v) { // Returns the sum of this and v
		return new Vec2(this.x + v.x,
						this.y + v.y);
	}
	
	public Vec2 sub(Vec2 v) { // Returns the difference between this and v
		return new Vec2(this.x - v.x,
						this.y - v.y);
	}
	
	public Vec2 mul(double d) { // Returns the product of this and d
		return new Vec2(this.x * d,
						this.y * d);
	}
	
	public Vec2 div(double d) { // Returns the quotient of this and d
		return new Vec2(this.x / d,
						this.y / d);
	}
	
	public Vec2 yaw(double angle) { // Rotation on the XZ-Plane (Y-Axis)
		return new Vec2(Math.cos(angle) * this.x + Math.sin(angle) * y, - Math.sin(angle) * this.x + Math.cos(angle) * this.y);
	}
	
	public double length() { // Returns the length of this
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public double squaredLength() { // Returns the squared length of this
		return this.x * this.x + this.y * this.y;
	}
	
	public Vec2 normalize() { // Returns a normalized version of this
		return x != 0. || y != 0. ? this.div(this.length()) : new Vec2(0., 0.);
	}
	
	public Vec2i toPixelSpace(long width, long height) { // Returns the pixel space coords of this
		long halfwidth = width / 2;
		long halfheight = height / 2;
		return new Vec2i((int)((this.x * halfwidth) + halfwidth),
						 (int)((this.y * -halfheight) + halfheight));
	}
	
	public boolean inArea(Vec2 bottomLeft, Vec2 topRight) {
		return (this.x > bottomLeft.x && this.x < topRight.x) && (this.y > bottomLeft.y && this.y < topRight.y);
	}
	
	@Override
	public String toString() { // Returns a string representing this
		return Double.toString(x) + "," + Double.toString(y);
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(x) ^ Integer.rotateLeft(Double.hashCode(y), 13);
	}
}
