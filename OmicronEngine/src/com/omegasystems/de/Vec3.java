package com.omegasystems.de;

public class Vec3 {
	public double x;
	public double y;
	public double z;
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 add(Vec3 v) { // Returns the sum of this and v
		return new Vec3(this.x + v.x,
						this.y + v.y,
						this.z + v.z);
	}
	
	public Vec3 sub(Vec3 v) { // Returns the difference between this and v
		return new Vec3(this.x - v.x,
						this.y - v.y,
						this.z - v.z);
	}
	
	public Vec3 mul(double d) { // Returns the product of this and d
		return new Vec3(this.x * d,
						this.y * d,
						this.z * d);
	}
	
	public Vec3 div(double d) { // Returns the quotient of this and d
		return new Vec3(this.x / d,
						this.y / d,
						this.z / d);
	}
	
	public double length() { // Returns the length of this
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public Vec3 normalize() { // Returns a normalized version of this
		return this.div(this.length());
	}
	
	public Vec2 toScreenSpace() { // Returns the projections of this
		return new Vec2(this.x / this.z, this.y / this.z);
	}
	
	public Vec3 yaw(double angle) { // Rotation on the XZ-Plane (Y-Axis)
		return new Vec3(Math.cos(angle) * this.x + Math.sin(angle) * z, this.y, - Math.sin(angle) * this.x + Math.cos(angle) * this.z);
	}
	
	public Vec2 xz() {
		return new Vec2(this.x, this.z);
	}
	
	@Override
	public String toString() { // Returns a string representing this
		return Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z);
	}
}
