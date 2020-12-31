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
	private final String TITLE = "OmegaSystems 3D Engine - Rendering Test 3";
	private final double aspectRatio = 16. / 9.;
	public final int WIDTH = 800;
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
	public long timerLogic;
	public long timerGraphic;
	
	public Renderer() {
		System.out.println("Setting up window with " + Integer.toString(WIDTH) + " x " + Integer.toString(HEIGHT));
		window = new Window(WIDTH, HEIGHT, TITLE);
		canvas = window.getPixelCanvas();
		inputHandler = window.getInputHandler();
		
		wallQueue = new ArrayList<Wall>();
		tempQuadQueue = new ArrayList<Quad>();
		quadQueue = new ArrayList<Quad>();
		
		setupBackground();
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
				return (int) Math.signum(Math.max(r.a.length(), r.b.length()) - Math.max(l.a.length(), l.b.length()) );
			}
		});
		
		timerLogic = System.nanoTime();
		
		tempQuadQueue.clear();
		while (!wallQueue.isEmpty()) {
			drawWall(wallQueue.get(0));
			wallQueue.remove(0);
		}
		
		quadQueue.clear();
		for(Quad quad: tempQuadQueue) {
			quadQueue.add(quad);
		}
		
		timerGraphic = System.nanoTime();
		
		canvas.repaint();
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
		
		Vec3 aUpper = wall.getUpperA();
		Vec3 bUpper = wall.getUpperB();
		
		if (aUpper.z < 0. && bUpper.z < 0.) {
			return;
		}
		
		double d = (aUpper.x - bUpper.x) / (aUpper.z - bUpper.z);
		
		if (aUpper.z < 0.001) {
			aUpper.x -= d * aUpper.z;
			aUpper.z = 0.001;
		}
		if (bUpper.z < 0.001) {
			bUpper.x -= d * bUpper.z;
			bUpper.z = 0.001;
		}
		
		Vec2 aUpperS = aUpper.toScreenSpace();
		Vec2 bUpperS = bUpper.toScreenSpace();
		
		if (aUpperS.x > bUpperS.x) {
			return;
		}
		
		Vec2i aUpperP = aUpperS.toPixelSpace(WIDTH, HEIGHT);
		Vec2i bUpperP = bUpperS.toPixelSpace(WIDTH, HEIGHT);
		
		if ((aUpperP.x < 0 && bUpperP.x < 0) || (aUpperP.x > WIDTH && bUpperP.x > WIDTH)) {
			return;
		}
		
		int horStart = (int) aUpperP.x;
		int horEnd = (int) bUpperP.x;
		int heightA = HALF_HEIGHT - aUpperP.y;
		int heightB = HALF_HEIGHT - bUpperP.y;
		
		int heightLeft = getHeight(horStart, horEnd, heightA, heightB, Math.max(0, horStart));
		int heightRight = getHeight(horStart, horEnd, heightA, heightB, Math.min(WIDTH, horEnd));
		
		tempQuadQueue.add(new Quad(new Vec2i(Math.max(0, horStart), HALF_HEIGHT + heightLeft),
							   new Vec2i(Math.max(0, horStart), HALF_HEIGHT - heightLeft),
							   new Vec2i(Math.min(WIDTH, horEnd), HALF_HEIGHT - heightRight),
							   new Vec2i(Math.min(WIDTH, horEnd), HALF_HEIGHT + heightRight),
							   wall.color));
		debugWD += 1;
	}
	
	public int getHeight(int horStart, int horEnd, int heightA, int heightB, int x) {
		double v = (double)(x - horStart) / (double)(horEnd - horStart);
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
