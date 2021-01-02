package com.omegasystems.de;

public class Core {
	public static Renderer renderer;
	public static FileHandler fileHandler;
	
	public static Boolean running;
	public static long ticks;
	public static double timeSinceStart;
	public static double deltatime;
	
	public static int tps;
	public static int fps;

	public static long LDUtime;
	public static long LDUticks;
	public static long LDUframes;
	
	public static void main(String[] args) {
		System.out.println("Starting OmicronEngine");
		
		renderer = new Renderer();
		fileHandler = new FileHandler("testMap2.omd");
		
		fileHandler.loadMap();
		
		gameLoop();
	}
	
	public static void gameLoop() {
		running = true;
		timeSinceStart = 0;
		ticks = 0;
		LDUtime = System.nanoTime();
		LDUticks = 0;
		LDUframes = 0;
		long start = System.nanoTime();
		
		while (running) {
			deltatime = (System.nanoTime() - start) / 1_000_000_000.;
			timeSinceStart += deltatime;
			ticks++;
			
			start = System.nanoTime();
			
			renderer.handleInputs();
			renderer.render();
			
			if (start - LDUtime > 0.5 * 1_000_000_000) {
				double deltaLDU = (start - LDUtime) / 1_000_000_000d;
				long deltaLDUt = (ticks - LDUticks);
				long deltaLDUf = (PixelCanvas.frames - LDUframes);

				tps = (int) (deltaLDUt /  deltaLDU);
				fps = (int) (deltaLDUf / deltaLDU);
				
				LDUtime = start;
				LDUticks = ticks;
				LDUframes = PixelCanvas.frames;
			}
			
			renderer.canvas.repaint();
		}
	}
}
