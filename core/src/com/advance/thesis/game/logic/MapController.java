package com.advance.thesis.game.logic;

import com.advance.thesis.util.Point;

/** General means of controlling the control based part of the game logic */
public class MapController {
	
	/** The Map this Controller influences */
	private Map map;
	
	
	/** Constructs new Map Controller */
	public MapController(Map map){
		this.map = map;
	}
	
	
	/** Moves unit at origin to target if possible, returns whether movement was successful */
	public boolean move(Point origin, Point target){
		return map.move(origin, target);
	}
	
	
	
}
