package com.omegasystems.de;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Renderer {
	private final String TITLE = "OmegaSystems 3D Engine - Rendering Test 4";
	private final double aspectRatio = 16. / 9.;
	public final int WIDTH = 1200;
	public final int HEIGHT = (int) (WIDTH / aspectRatio);
	
	private Window window;
	private PixelCanvas canvas;
	private InputHandler inputHandler;
	
	private final double eyeheight = 0.5;
	public Vec3 playerPos;
	public double playerYaw;
	public Vec3 velocity = new Vec3(0., 0., 0.);
	public double upwardsVelocity;
	
	public ArrayList<Sector> sectors = new ArrayList<Sector>();
	
	private List<Wall> wallQueue;
	private List<Quad> tempQuadQueue;
	public List<Quad> quadQueue;
	
	public int debugWC;
	public int debugWD;
	
	public Renderer() {
		System.out.println("Setting up window with " + Integer.toString(WIDTH) + " x " + Integer.toString(HEIGHT));
		wallQueue = new ArrayList<Wall>();
		tempQuadQueue = new ArrayList<Quad>();
		quadQueue = new ArrayList<Quad>();
		
		window = new Window(WIDTH, HEIGHT, TITLE);
		canvas = window.getPixelCanvas();
		inputHandler = window.getInputHandler();
	}
	
	public void render() {
		
		debugWC = 0;
		debugWD = 0;
		
		Vec3 cameraPos = playerPos.add(new Vec3(0., eyeheight, 0.));
		
		Sector actSector = new Sector();
		for (Sector sector: sectors) {
			if (playerPos.xz().inArea(sector.bottomLeft, sector.topRight)) {
				actSector = sector;
			}
		}
		for (int i: actSector.drawQueue) {
			for (Wall wall: sectors.get(i).walls) {
				wallQueue.add(wall.transform(cameraPos, playerYaw));
			}
		}
		for (Wall wall: actSector.walls) {
			wallQueue.add(wall.transform(cameraPos, playerYaw));
		}
		
		Collections.sort(wallQueue, new Comparator<Wall>() {
			@Override
			public int compare(Wall l, Wall r) {
				return (int)Math.signum(Math.max(r.a.squaredLength(), r.b.squaredLength()) - Math.max(l.a.squaredLength(), l.b.squaredLength()));
			}
		});
		
		tempQuadQueue.clear();
		while (!wallQueue.isEmpty()) {
			drawWall(wallQueue.get(0));
			wallQueue.remove(0);
		}
		updateQuadQueue();
		
		canvas.repaint();
	}
	
	public void updateQuadQueue() {
		synchronized (quadQueue) {
			quadQueue = List.copyOf(tempQuadQueue);
		}
	}

	public List<Quad> getQuadQueue() {
		synchronized (quadQueue) {
			return List.copyOf(quadQueue);
		}
	}
	
	public void drawWall(Wall wall) {
		debugWC += 1;
		
		Vec3 aLower = wall.getLowerA();
		Vec3 bLower = wall.getLowerB();
		
		if (aLower.z < 0. && bLower.z < 0.) {
			return;
		}
		
		double d = (aLower.x - bLower.x) / (aLower.z - bLower.z);
		
		if (aLower.z < 0.001) {
			aLower.x -= d * aLower.z;
			aLower.z = 0.001;
		}
		if (bLower.z < 0.001) {
			bLower.x -= d * bLower.z;
			bLower.z = 0.001;
		}
		
		Vec2 aLowerS = aLower.toScreenSpace();
		Vec2 bLowerS = bLower.toScreenSpace();
		Vec2 aUpperS = aLower.add(new Vec3(0., wall.ha, 0.)).toScreenSpace();
		Vec2 bUpperS = bLower.add(new Vec3(0., wall.hb, 0.)).toScreenSpace();
		
		if (aLowerS.x > bLowerS.x) {
			return;
		}
		
		Vec2i aLowerP = aLowerS.toPixelSpace(WIDTH, HEIGHT);
		Vec2i bLowerP = bLowerS.toPixelSpace(WIDTH, HEIGHT);
		Vec2i aUpperP = aUpperS.toPixelSpace(WIDTH, HEIGHT);
		Vec2i bUpperP = bUpperS.toPixelSpace(WIDTH, HEIGHT);
		
		if ((aLowerP.x < 0 && bLowerP.x < 0) || (aLowerP.x > WIDTH && bLowerP.x > WIDTH)) {
			return;
		}
		
		tempQuadQueue.add(new Quad(aLowerP, bLowerP, bUpperP, aUpperP, wall.color));
		debugWD += 1;
	}
	
	public int getHeight(int horStart, int horEnd, int heightA, int heightB, int x) {
		double v = (double)(x - horStart) / (horEnd - horStart);
		return (int) (heightA * (1. - v) + heightB * v);
	}
	
	public void handleInputs() {
		playerYaw += 2. * inputHandler.rotationSpeed * Core.deltatime;
		
		Vec2 movementXZ = new Vec2(inputHandler.rightSpeed, inputHandler.forwardSpeed).normalize();
		
		velocity = velocity.add(new Vec3(movementXZ.x, 0, movementXZ.y).yaw(-playerYaw).mul(Core.deltatime).mul(2.));
		velocity = velocity.mul(0.5);
		
		Vec3 newPos = playerPos.add(velocity);
		Vec2 xz = newPos.xz();
		for (Sector sector: sectors) {
			if (xz.inArea(sector.bottomLeft, sector.topRight)) {
				playerPos = newPos;
				break;
			}
		}
		
		if (newPos.y <= 0.) {
			upwardsVelocity = 0.;
			if (inputHandler.jump == 1) {
				upwardsVelocity = .000008;
			}
		}
		else {
			upwardsVelocity -= Core.deltatime * 0.00003;
		}
		
		playerPos.y += upwardsVelocity;
		
		if (inputHandler.debugInterrupt) {
			System.out.println("DEBUG INTERRUPT");
			System.out.println(playerPos);
			System.out.println(playerYaw);
			Core.running = false;
		}
	}
}
