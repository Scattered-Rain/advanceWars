package com.advance.thesis.game.logic;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Map {
	
	private static final UnitContainer NONE_UNIT = new UnitContainer(Unit.NONE, Player.NONE, -1);
	
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
		this.units[3][3] = new UnitContainer(Unit.INFANTRY, Player.P0, 10);
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
				units[cy][cx] = NONE_UNIT;
				terrain[cy][cx] = Terrain.PLAIN;
			}
		}
	}
	
	/** Moves Unit from origin to target location - Note: Does not perform ANY checking! */
	protected void move(int originX, int originY, int targetX, int targetY){
		UnitContainer temp = this.units[originY][originX];
		this.units[targetY][targetX] = temp;
		this.units[originY][originX] = NONE_UNIT;
	}
	
	/** Returns Terrain Type at x|y */
	public Terrain getTerrain(int x, int y){
		return this.terrain[y][x];
	}
	
	/** Returns Unit Type at x|y */
	public Unit getUnit(int x, int y){
		return  this.units[y][x].getType();
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
