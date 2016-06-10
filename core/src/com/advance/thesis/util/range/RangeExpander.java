package com.advance.thesis.util.range;

import com.advance.thesis.game.enums.MoveType;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.util.Direction;
import com.advance.thesis.util.Point;

public abstract class RangeExpander {
	
	
	/** Returns RangeCluster of the Movement the given unit can make */
	public static RangeCluster calcMoveRange(Map map, Point unit){
		RangeExpander re = new MovementRangeExpander(map, unit);
		return re.process();
	}
	
	/** Returns RangeCluster of the Shooting Range the given unit can make */
	public static RangeCluster calcShootingRange(Map map, Point unit){
		RangeExpander re = new ShootingRangeExpander(map, unit);
		return re.process();
	}
	
	
	//Class
	
	/** 2D Array capable of holding the entire possible range of ranges */
	protected int[][] maxCluster;
	
	/** The Coordinate of origin */
	protected Point origin;
	
	/** The Map this problem is concerned about */
	protected Map map;
	
	/** The size of the cluster to either side from the center (length 5 -> 2) (Equals movement of unit) */
	protected int maxMove;
	
	
	/** Constructor for the utility class solely concerned about figuring out the movement range of the given problem */
	private RangeExpander(Map map, Point origin){
		this.map = map;
		this.origin = origin;
		int movement = calcMovement(origin);
		this.maxCluster = new int[movement*2+1][movement*2+1];
		this.maxMove = movement;
		recCalcInit(new Point(maxMove, maxMove), movement);
	}
	
	/** Creates the Range Cluster */
	public RangeCluster process(){
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
		Point mapLocation = new Point(origin.getX()-maxMove+lowestX, origin.getY()-maxMove+lowestY);
		Point origin = new Point(maxMove-lowestX, maxMove-lowestY);
		fittetCluster = postProcessFittetCluster(fittetCluster, mapLocation, origin);
		RangeCluster cluster = new RangeCluster(fittetCluster, mapLocation, origin);
		return cluster;
	}
	
	/** Sets up for recClac */
	private void recCalcInit(Point loc, int movement){
		maxCluster[loc.getY()][loc.getX()] = movement;
		for(Direction dir : Direction.values()){
			recCalc(new Point(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movement);
		}
	}
	
	/** Recursively expands the range of the unit tile by tile, dumping the remainder of movement+this tile's movement cost in the cell */
	private void recCalc(Point loc, int movementLeft){
		//Checks whether out of local bounds
		if(loc.getX()<0 || loc.getX()>=maxCluster.length || loc.getY()<0 || loc.getY()>=maxCluster.length){
			return;
		}
		Point globLoc = convert(loc);
		//Checks whether out of global bounds
		if(globLoc.getX()<0 || globLoc.getX()>=map.getWidth() || globLoc.getY()<0 || globLoc.getY()>=map.getHeight()){
			return;
		}
		//Calc move cost
		int moveCost = calcMoveCost(globLoc, loc, movementLeft);
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
						recCalc(new Point(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movementAtStep);
					}
				}
			}
			else if(maxCluster[loc.getY()][loc.getX()]<movementAtStep){
				maxCluster[loc.getY()][loc.getX()] = movementAtStep;
				for(Direction dir : Direction.values()){
					recCalc(new Point(loc.getX()+dir.getX(), loc.getY()+dir.getY()), movementAtStep);
				}
			}
		}
	}
	
	/** Converts local maxCluster coordinates to global map coordinates */
	private Point convert(Point localCoords){
		return new Point(localCoords.getX()+origin.getX()-maxMove, localCoords.getY()+origin.getY()-maxMove);
	}
	
	
	//Generic methods (subject to polymorphism)
	/** Abstract method for calculating tile's movemnent cost */
	protected abstract int calcMoveCost(Point globLoc, Point loc, int movementLeft);
	
	/** Calculate the amount of movement available */
	protected abstract int calcMovement(Point unit);
	
	/** Allows post processing on the cluster grid used as the direct base of the RangeCluster */
	protected int[][] postProcessFittetCluster(int[][] fittetCluster, Point mapLocation, Point origin){
		return fittetCluster;
	}
	
	
	//Inner classes ---
	/** Range Expander for the purposes of Unit Movement */
	private static class MovementRangeExpander extends RangeExpander{
		public MovementRangeExpander(Map map, Point origin){
			super(map, origin);
		}
		@Override protected int calcMoveCost(Point globLoc, Point loc, int movementLeft){
			return super.map.getTerrain(globLoc).getMoveCost(super.map.getUnit(super.origin).getMoveType());
		}
		@Override protected int calcMovement(Point unit){
			if(!super.map.getUnit(super.origin).isUnit()){
				System.out.println("Try to move Non Unit");
				System.exit(0);
			}
			return map.getUnit(origin).getMovement();
		}
	}
	
	/** Range Expander for the purposes of Ranged Attacks */
	private static class ShootingRangeExpander extends RangeExpander{
		public ShootingRangeExpander(Map map, Point origin){
			super(map, origin);
		}
		@Override protected int calcMoveCost(Point globLoc, Point loc, int movementLeft){
			return 1;
		}
		@Override protected int calcMovement(Point unit){
			return map.getUnit(origin).getShootingRange().getY();
		}
		@Override protected int[][] postProcessFittetCluster(int[][] fittetCluster, Point mapLocation, Point origin){
			//TODO: Make more general if neccessary
			int minRange = Unit.ARTILLERY.getShootingRange().getX();
			for(int cy=0; cy<fittetCluster.length; cy++){
				for(int cx=0; cx<fittetCluster[0].length; cx++){
					if(fittetCluster[cy][cx]>=minRange){
						fittetCluster[cy][cx] = RangeCluster.OUT_OF_RANGE;
					}
				}
			}
			return fittetCluster;
		}
	}
	
}
