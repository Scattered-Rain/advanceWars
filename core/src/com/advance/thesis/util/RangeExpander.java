package com.advance.thesis.util;

import com.advance.thesis.game.enums.MoveType;
import com.advance.thesis.game.logic.Map;

public class RangeExpander {
	
	
	/** Returns RangeCluster of the Movement the given unit can make */
	public static RangeCluster calcMoveRange(Map map, Point<Integer> unit){
		RangeExpander re = new RangeExpander(map, unit);
		return re.process();
	}
	
	
	/** 2D Array capable of holding the entire possible range of ranges */
	private int[][] maxCluster;
	
	/** The Coordinate of origin */
	private Point<Integer> origin;
	
	/** The MoveType exhibited by the unit in question */
	private MoveType moveType;
	
	/** The Map this problem is concerned about */
	private Map map;
	
	/** The size of the cluster to either side from the center (length 5 -> 2) (Equals movement of unit) */
	private int oneSideClusterLength;
	
	
	/** Constructor for the utility class solely concerned about figuring out the movement range of the given problem */
	private RangeExpander(Map map, Point<Integer> origin){
		this.map = map;
		this.origin = origin;
		int movement = map.getUnit(origin).getMovement();
		this.maxCluster = new int[movement*2+1][movement*2+1];
		this.moveType = map.getUnit(origin).getMoveType();
		this.oneSideClusterLength = movement;
		recCalcInit(new Point<Integer>(oneSideClusterLength, oneSideClusterLength), movement);
	}
	
	/** Creates the Range Cluster */
	private RangeCluster process(){
		int lowestX = maxCluster.length-1;
		int lowestY = maxCluster.length-1;
		int highestX = 0;
		int highestY = 0;
		for(int cy=0; cy<maxCluster.length; cy++){
			for(int cx=0; cx<maxCluster.length; cx++){
				//Checks for optimizing bounding box
				if(maxCluster[cy][cx]!=0){
					if(maxCluster[cy][cx]==-1){
						maxCluster[cy][cx] = 0;
					}
					//Checks for optimizing bounding box
					if(lowestX>cx){
						lowestX = cx;
					}
					if(lowestY>cy){
						lowestY = cy;
					}
					if(highestX<cx){
						highestX = cx;
					}
					if(highestY<cy){
						highestY = cy;
					}
				}
				else{
					//Sets all 0 values to be out of range
					maxCluster[cy][cx] = RangeCluster.OUT_OF_RANGE;
				}
			}
		}
		int[][] fittetCluster = new int[highestY-lowestY+1][highestX-lowestX+1];
		for(int cy=0; cy<fittetCluster.length; cy++){
			for(int cx=0; cx<fittetCluster[0].length; cx++){
				int y = cy+lowestY;
				int x = cx+lowestX;
				fittetCluster[cy][cx] = maxCluster[y][x];
			}
		}
		Point<Integer> mapLocation = new Point<Integer>(origin.getX()-oneSideClusterLength+lowestX, origin.getY()-oneSideClusterLength+lowestY);
		Point<Integer> origin = new Point<Integer>(oneSideClusterLength-lowestX, oneSideClusterLength-lowestY);
		RangeCluster cluster = new RangeCluster(fittetCluster, mapLocation, origin);
		return cluster;
	}
	
	/** Sets up for recClac */
	private void recCalcInit(Point<Integer> loc, int movementLeft){
		maxCluster[loc.getY()][loc.getX()] = movementLeft;
		for(Direction dir : Direction.values()){
			recCalc(new Point<Integer>(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movementLeft);
		}
	}
	
	/** Recursively expands the range of the unit tile by tile, dumping the remainder of movement+this tile's movement cost in the cell */
	private void recCalc(Point<Integer> loc, int movementLeft){
		//Checks whether out of local bounds
		if(loc.getX()<0 || loc.getX()>=maxCluster.length || loc.getY()<0 || loc.getY()>=maxCluster.length){
			return;
		}
		Point<Integer> globLoc = convert(loc);
		//Checks whether out of global bounds
		if(globLoc.getX()<0 || globLoc.getX()>=map.getWidth() || globLoc.getY()<0 || globLoc.getY()>=map.getHeight()){
			return;
		}
		//Calc move cost
		int moveCost = map.getTerrain(globLoc).getMoveCost(moveType);
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
		return new Point<Integer>(localCoords.getX()+origin.getX()-oneSideClusterLength, localCoords.getY()+origin.getY()-oneSideClusterLength);
	}
	
}
