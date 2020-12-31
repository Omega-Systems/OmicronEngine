package com.omegasystems.de;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
	static final int version = 2;
	String filename;
	
	public FileHandler(String filename) {
		this.filename = filename;
	}
	
	public void loadMap() {
		
		File basePath = new File(System.getProperty("user.dir"));
		File file = new File(basePath, "data/maps/" + filename);
		
		Scanner sc;
		
		try {
			sc = new Scanner(file);
			String object = "";
			int index = 0;
			
			while (sc.hasNextLine()) {
				
				String actLine = sc.nextLine();
				String[] tokens = actLine.split(" ");
				
				if (tokens.length != 0 && !tokens[0].isBlank()) {
					switch (object) {
					case "":
						switch (tokens[0]) {
						case "def":
							object = tokens[1];
							index = tokens.length == 3 ? Integer.valueOf(tokens[2]) : 0;
							switch (object) {
							case "sector":
								Core.renderer.sectors.add(index, new Sector());
								break;
							default:
								break;
							}
							break;
						case "//":
							break;
						default:
							System.out.println("Unknown command in OMD-File: " + actLine);
							break;
						}
						break;
						
					case "header":
						switch (tokens[0]) {
						case "version":
							if (Integer.valueOf(tokens[1]) != version) {
								System.out.println("Version mismatch");
							}
							break;
						case "enddef":
							object = "";
							break;
						default:
							System.out.println("Unknown command in OMD-File: " + actLine);
							break;
						}
						break;
						
					case "spawn":
						switch (tokens[0]) {
						case "pos":
							Core.renderer.cameraPos = new Vec3(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]), Double.valueOf(tokens[3]));
							break;
						case "rot":
							Core.renderer.cameraYaw = Double.valueOf(tokens[1]);
							break;
						case "enddef":
							object = "";
							break;
						default:
							System.out.println("Unknown command in OMD-File: " + actLine);
							break;
						}
						break;
						
					case "sector":
						Sector sector = Core.renderer.sectors.get(index);
						switch (tokens[0]) {
						case "queue":
							sector.drawQueue.add(Integer.valueOf(tokens[1]));
							break;
						case "area":
							sector.bottomLeft = new Vec2(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
							sector.topRight = new Vec2(Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]));
							sector.origin = sector.bottomLeft.add(sector.topRight).div(2.);
							break;
						case "wall":
							sector.walls.add(new Wall(new Vec2(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2])),
													  new Vec2(Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4])),
													  Integer.parseUnsignedInt(tokens[5].substring(2), 16)));
							break;
						case "enddef":
							object = "";
							break;
						default:
							System.out.println("Unknown command in OMD-File: " + actLine);
							break;
						}
						break;
						
					default:
						System.out.println("Unknown object in OMD-File: " + object);
						break;
				}
			}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
}
