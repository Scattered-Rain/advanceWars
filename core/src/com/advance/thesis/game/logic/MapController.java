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
		if(checkMoveLegality(origin, target)){
			map.move(origin, target);
			return true;
		}
		return false;
	}
	
	/** Returns whether the requested movement is legal */
	public boolean checkMoveLegality(Point origin, Point target){
		if(!(map.inBounds(origin) && map.inBounds(target))){
			System.out.println("Movement parameters out of bound");
			return false;
		}
		if(!(map.getUnit(origin).isUnit() && !map.getUnit(target).isUnit())){
			if(!origin.isIdentical(target)){
				System.out.println("Movement obscured");
				return false;
			}
		}
		if(!map.getMovementRange(origin).inRangeGlobal(target)){
			System.out.println("Movement out of legal move range");
			return false;
		}
		return true;
	}
	
}
