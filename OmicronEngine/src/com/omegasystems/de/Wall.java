package com.omegasystems.de;

public class Wall {
	public Vec2 a;
	public Vec2 b;
	public int color;
	
	public Wall(Vec2 a, Vec2 b, int color) {
		this.a = a;
		this.b = b;
		this.color = color;
	}
	
	public Wall(Vec2 a, Vec2 b) {
		this(a, b, 0x000000);
	}
	
	public Vec3 getUpperA() {
		return new Vec3(a.x, 0.5, a.y);
	}
	
	public Vec3 getUpperB() {
		return new Vec3(b.x, 0.5, b.y);
	}
	
	public Wall transform(Vec3 pos, double angle) {
		return new Wall(a.sub(pos.xz()).yaw(angle), b.sub(pos.xz()).yaw(angle), this.color);
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() ^ Integer.rotateLeft(b.hashCode(), 9);
	}
}
