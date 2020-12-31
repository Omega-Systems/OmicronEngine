package com.omegasystems.de;

public class Core {
	public static Renderer renderer;
	public static FileHandler fileHandler;
	
	public static Boolean running;
	public static long ticks;
	public static double timeSinceStart;
	public static double deltatime;
	
	public static int tps;
	public static int deltaLogic;
	public static int deltaGraphic;
	
	public static long debugFrames;
	public static long debugLogic;
	public static long debugGraphic;
	
	public static long LDUtime;
	public static long LDUticks;
	
	public static void main(String[] args) {
		System.out.println("Starting OmicronEngine");
		
		renderer = new Renderer();
		fileHandler = new FileHandler("testMap1.omd");
		
		fileHandler.loadMap();
		
		gameLoop();
	}
	
	public static void gameLoop() {
		running = true;
		timeSinceStart = 0;
		ticks = 0;
		LDUtime = System.nanoTime();
		LDUticks = 0;
		long start = System.nanoTime();
		
		while (running) {
			deltatime = (System.nanoTime() - start) / 1_000_000_000.;
			timeSinceStart += deltatime;
			ticks++;
			
			start = System.nanoTime();
			
			renderer.render();
			renderer.handleInputs();
			
			debugFrames += 1;
			debugLogic += renderer.timerLogic - start;
			debugGraphic += renderer.timerGraphic - start;
			
			if (start - LDUtime > 0.5 * 1_000_000_000) {
				double deltaLDU = (start - LDUtime) / 1_000_000_000d;
				long deltaLDUt = (ticks - LDUticks);

				tps = (int) (debugFrames /  deltaLDU);
				deltaLogic = (int) ((debugLogic / deltaLDUt) / 1_000_000d);
				deltaGraphic = (int) ((debugGraphic / deltaLDUt) / 1_000_000d);
				
				LDUtime = start;
				LDUticks = ticks;
				debugFrames = 0;
				debugLogic = 0;
				debugGraphic = 0;
			}
			
			//try {Thread.sleep(5);} catch (InterruptedException e) {}
		}
	}
}
