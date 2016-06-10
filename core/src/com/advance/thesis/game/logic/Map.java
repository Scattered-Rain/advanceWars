package com.advance.thesis.game.logic;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.range.RangeCluster;
import com.advance.thesis.util.range.RangeExpander;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Map {
	
	private static final UnitContainer NO_UNIT = new UnitContainer(Unit.NONE, Player.NONE, -1);
	
	/** The dimensions of the map */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the terrain info of the map */
	private Terrain[][] terrain;
	/** Holds the unit info of the map */
	private UnitContainer[][] units;
	
	
	/** Constructs Default, empty map of given dimensions */
	public Map(int width, int height){
		setMap(width, height);
		this.units[0][0] = new UnitContainer(Unit.ARTILLERY, Player.P0, 10);
	}
	
	/** Constructs new Map based on raw values (for cloning) */
	private Map(int width, int height, Terrain[][] terrain, UnitContainer[][] units){
		this.width = width;
		this.height = height;
		this.terrain = new Terrain[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				this.terrain[cy][cx] = terrain[cy][cx];
				this.units[cy][cx] = units[cy][cx].clone();
			}
		}
	}
	
	
	/** Basic empty setup of the map */
	private void setMap(int width, int height){
		this.width = width;
		this.height = height;
		this.terrain = new Terrain[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				units[cy][cx] = NO_UNIT;
				terrain[cy][cx] = Terrain.PLAINS;
			}
		}
	}
	
	/** Moves Unit from origin to target location - Note: Does not perform ANY checking! */
	protected void move(int originX, int originY, int targetX, int targetY){
		UnitContainer temp = this.units[originY][originX];
		this.units[originY][originX] = NO_UNIT;
		this.units[targetY][targetX] = temp;
	}
	
	/** Moves Unit from origin to target location - Note: Does not perform ANY checking! */
	protected void move(Point origin, Point target){
		move(origin.getX(), origin.getY(), target.getX(), target.getY());
	}
	
	/** Returns Terrain Type at x|y */
	public Terrain getTerrain(int x, int y){
		return this.terrain[y][x];
	}
	
	/** Returns Terrain Type at given Point */
	public Terrain getTerrain(Point point){
		return getTerrain(point.getX(), point.getY());
	}
	
	/** Returns Unit Type at x|y */
	public Unit getUnit(int x, int y){
		return  this.units[y][x].getType();
	}
	
	/** Returns Unit Type at given Point */
	public Unit getUnit(Point point){
		return getUnit(point.getX(), point.getY());
	}
	
	/** Returns whether the given point is actually on the map */
	public boolean inBounds(Point loc){
		return loc.x>=0 && loc.x<this.width && loc.y>=0 && loc.y<this.height;
	}
	
	/** Returns RangeCluster of the movement at the given point */
	public RangeCluster getMovementRange(Point unit){
		return RangeExpander.calcMoveRange(this, unit);
	}
	
	/** Returns the Shooting Range of the unit at the given point */
	public RangeCluster getShootingRange(Point unit){
		return RangeExpander.calcShootingRange(this, unit);
	}
	
	/** Returns deep clone of this map */
	public Map clone(){
		return new Map(width, height, terrain, units);
	}
	
	
	//Inner Utility classes -----------
	
	@AllArgsConstructor
	/** Inner class for simplifying storage of active units on the map */
	private static class UnitContainer{
		@Getter private Unit type;
		@Getter private Player owner;
		@Getter private int hp;
		/** Returns a deep clone of this object */
		public UnitContainer clone(){
			return new UnitContainer(type, owner, hp);
		}
	}
	
}
