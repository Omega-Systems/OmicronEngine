package com.omegasystems.de;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Renderer {
	private final String TITLE = "OmegaSystems 3D Engine - Rendering Test 4";
	private final double aspectRatio = 16. / 9.;
	public final int WIDTH = 1200;
	public final int HEIGHT = (int) (WIDTH / aspectRatio);
	//private final int HALF_WIDTH = WIDTH / 2;
	private final int HALF_HEIGHT = HEIGHT / 2;
	
	private Window window;
	private PixelCanvas canvas;
	private InputHandler inputHandler;
	
	public Vec3 cameraPos;
	public double cameraYaw;
	
	private final int COLOR_CEILING = 0x404040;
	private final int COLOR_FLOOR = 0x303030;
	
	private BufferedImage background;
	
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
		
		setupBackground();
		
		window = new Window(WIDTH, HEIGHT, TITLE);
		canvas = window.getPixelCanvas();
		inputHandler = window.getInputHandler();
	}
	
	public void render() {
		WritableRaster raster = background.copyData(null);
		canvas.img = new BufferedImage(background.getColorModel(), raster, false, null);
		
		debugWC = 0;
		debugWD = 0;
		
		Sector actSector = new Sector();
		for (Sector sector: sectors) {
			if (cameraPos.xz().inArea(sector.bottomLeft, sector.topRight)) {
				actSector = sector;
			}
		}
		for (int i: actSector.drawQueue) {
			for (Wall wall: sectors.get(i).walls) {
				wallQueue.add(wall.transform(cameraPos, cameraYaw));
			}
		}
		for (Wall wall: actSector.walls) {
			wallQueue.add(wall.transform(cameraPos, cameraYaw));
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

	
	
	
	public void setupBackground() {
		background = new BufferedImage((int)WIDTH, (int)HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < WIDTH; x++) {
	        for (int y = 0; y < HEIGHT / 2; y++) {
	        	background.setRGB(x, y, COLOR_CEILING);
	        }
	        for (int y = (int)(HEIGHT / 2); y < HEIGHT; y++) {
	        	background.setRGB(x, y, COLOR_FLOOR);
	        }
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
		Vec2 bLowerS =  bLower.toScreenSpace();
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
		
//		int horStart = (int) aLowerP.x;
//		int horEnd = (int) bLowerP.x;
//		int heightA = HALF_HEIGHT - (int)(wall.ha / wall.a.y);
//		int heightB = HALF_HEIGHT - (int)(wall.hb / wall.b.y);
//		
//		int heightLeft = getHeight(horStart, horEnd, heightA, heightB, Math.max(0, horStart));
//		int heightRight = getHeight(horStart, horEnd, heightA, heightB, Math.min(WIDTH, horEnd));
		
		tempQuadQueue.add(new Quad(aLowerP, bLowerP, bUpperP, aUpperP, wall.color));
		debugWD += 1;
	}
	
	public int getHeight(int horStart, int horEnd, int heightA, int heightB, int x) {
		double v = (double)(x - horStart) / (horEnd - horStart);
		return (int) (heightA * (1. - v) + heightB * v);
	}
	
	public void handleInputs() {
		cameraYaw += 2. * inputHandler.rotationSpeed * Core.deltatime;
		Vec3 movement = new Vec3(inputHandler.rightSpeed, 0., inputHandler.forwardSpeed).mul(Core.deltatime).yaw(-cameraYaw).mul(2);
		
		Vec3 newPos = cameraPos.add(movement);
		for (Sector sector: sectors) {
			if (newPos.xz().inArea(sector.bottomLeft, sector.topRight)) {
				cameraPos = newPos;
				break;
			}
		}
		
		if (inputHandler.debugInterrupt) {
			System.out.println("DEBUG INTERRUPT");
			System.out.println(cameraPos);
			System.out.println(cameraYaw);
			Core.running = false;
		}
	}
}
