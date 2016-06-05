package com.advance.thesis.util;

import com.advance.thesis.game.enums.MoveType;
import com.advance.thesis.game.logic.Map;

public class RangeExpander {
	
	
	/** Returns RangeCluster of the Movement the given unit can make */
	public static RangeCluster calcMoveRange(Map map, Point<Integer> unit){
		RangeExpander re = new RangeExpander(map, unit);
		Array2dPrinter.printIntArray(re.maxCluster);
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
	
	/** The MoveType exhibited by the unit in question */
	private MoveType moveType;
	
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
		this.moveType = map.getUnit(origin).getMoveType();
		recCalc(new Point<Integer>(maxCluster.length/2+1, maxCluster.length/2+1), speed);
	}
	
	
	/** Recursively expands the range of the unit tile by tile, dumping the remainder of movement+this tile's movement cost in the cell */
	private void recCalc(Point<Integer> loc, int movementLeft){
		//Out of Bounds
		if(loc.getX()<0 || loc.getX()>=maxCluster.length || loc.getY()<0 || loc.getY()>=maxCluster.length){
			return;
		}
		//Calc move cost
		int moveCost = map.getTerrain(convert(loc)).getMoveCost(moveType);
		//Calc Movement left on this tile
		int movementAtStep = movementLeft-moveCost;
		if(movementAtStep>=0){
			//Current Cell undefined
			if(maxCluster[loc.getY()][loc.getX()]<=0){
				//We have no moves left, set to -1 to denote this cell as outer most reachable
				if(movementAtStep==0){
					maxCluster[loc.getY()][loc.getX()] = -1;
				}
				else{
					maxCluster[loc.getY()][loc.getX()] = movementAtStep;
					for(Direction dir : Direction.values()){
						recCalc(new Point<Integer>(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movementAtStep);
					}
				}
			}
			else if(maxCluster[loc.getY()][loc.getX()]<movementAtStep){
				maxCluster[loc.getY()][loc.getX()] = movementAtStep;
				for(Direction dir : Direction.values()){
					recCalc(new Point<Integer>(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movementAtStep);
				}
			}
		}
	}
	
	/** Converts local maxCluster coordinates to global map coordinates */
	private Point<Integer> convert(Point<Integer> localCoords){
		return new Point<Integer>(localCoords.getX()+origin.getX()-(maxCluster.length/2+1), localCoords.getY()+origin.getY()-(maxCluster.length/2+1));
	}
	
}
