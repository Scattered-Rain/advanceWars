package com.advance.thesis.game.logic;

public class MapController {
	
	/** The Map this Controller influences */
	private Map map;
	
	
	/** Constructs new Map Controller */
	public MapController(Map map){
		this.map = map;
	}
	
	/** Moves unit at origin to target if possible, returns whether movement was successful */
	public boolean move(int originX, int originY, int targetX, int targetY){
		map.move(originX, originY, targetX, targetY);
		return true;
	}
	
}
