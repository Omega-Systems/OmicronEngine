package com.omegasystems.de;

public class Wall {
	public Vec3 a;
	public Vec3 b;
	public double ha;
	public double hb;
	public int color;
	
	public Wall(Vec3 a, Vec3 b, double ha, double hb, int color) {
		this.a = a;
		this.b = b;
		this.ha = ha;
		this.hb = hb;
		this.color = color;
	}
	
	public Wall(Vec2 a, Vec2 b, double ha, double hb, int color) {
		this(new Vec3(a.x, 0., a.y), new Vec3(b.x, 0., b.y), ha, hb, color);
	}
	
	public Vec3 getLowerA() {
		return a;
	}
	
	public Vec3 getUpperA() {
		return new Vec3(a.x, ha, a.y);
	}
	
	public Vec3 getLowerB() {
		return b;
	}
	
	public Vec3 getUpperB() {
		return new Vec3(b.x, hb, b.y);
	}
	
	public Wall transform(Vec3 pos, double angle) {
		return new Wall(a.sub(pos).yaw(angle), b.sub(pos).yaw(angle), this.ha, this.hb, this.color);
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode() ^ Double.hashCode(ha) ^ Double.hashCode(hb) ^ Integer.hashCode(color);
	}
}
