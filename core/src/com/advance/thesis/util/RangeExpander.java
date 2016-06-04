package com.advance.thesis.util;

import com.advance.thesis.game.enums.MoveType;
import com.advance.thesis.game.logic.Map;

public class RangeExpander {
	
	
	/** Returns RangeCluster of the Movement the given unit can make */
	public static RangeCluster calcMoveRange(Map map, Point<Integer> unit){
		RangeExpander re = new RangeExpander(map, unit);
		return null;
	}
	
	
	/** The Expansion the range has from the origin */
	private int upperExpansion;
	private int lowerExpansion;
	private int rightExpansion;
	private int leftExpansion;
	
	/** 2D Array capable of holding the entire possible range of ranges */
	private int[][] maxCluster;
	
	/** The Coordinate of origin */
	private Point<Integer> origin;
	
	/** The Map this problem is concerned about */
	private Map map;
	
	
	/** Constructor for the utility class solely concerned about figuring out the movement range of the given problem */
	private RangeExpander(Map map, Point<Integer> origin){
		this.upperExpansion = 0;
		this.lowerExpansion = 0;
		this.rightExpansion = 0;
		this.leftExpansion = 0;
		this.map = map;
		this.origin = origin;
		int speed = map.getUnit(origin).getSpeed();
		this.maxCluster = new int[speed*2+1][speed*2+1];
		MoveType type = map.getUnit(origin).getMoveType();
		recCalc(new Point<Integer>(0, 0), speed);
	}
	
	
	/** Recursively expands the range of the unit tile by tile, dumping the remainder of movement+this tile's movement cost in the cell */
	private void recCalc(Point<Integer> loc, int speed){
		
		
		
		convert(null);
	}
	
	/** Converts local maxCluster coordinates to global map cooridnates */
	private Point<Integer> convert(Point<Integer> localCoords){
		return new Point<Integer>(localCoords.getX()+origin.getX(), localCoords.getY()+origin.getY());
	}
	
}
