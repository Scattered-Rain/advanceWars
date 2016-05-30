package com.advance.thesis.game;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Map {
	
	/** The dimensions of the map */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the terrain info of the map */
	private TerrainContainer[][] terrain;
	/** Holds the unit info of the map */
	private UnitContainer[][] units;
	
	
	/** Constructs Default, empty map of given dimensions */
	public Map(int width, int height){
		setMap(width, height);
	}
	
	/** Constructs new Map based on raw values (for cloning) */
	private Map(int width, int height, TerrainContainer[][] terrain, UnitContainer[][] units){
		this.width = width;
		this.height = height;
		this.terrain = new TerrainContainer[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				this.terrain[cy][cx] = terrain[cy][cx].clone();
				this.units[cy][cx] = units[cy][cx].clone();
			}
		}
	}
	
	
	/** Basic empty setup of the map */
	private void setMap(int width, int height){
		this.width = width;
		this.height = height;
		this.terrain = new TerrainContainer[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				units[cy][cx] = new UnitContainer(Unit.NONE, Player.NONE, -1);
				terrain[cy][cx] = new TerrainContainer(Terrain.PLAIN, Player.NONE);
			}
		}
	}
	
	/** Returns deep clone of this map */
	public Map clone(){
		return new Map(width, height, terrain, units);
	}
	
	/** Returns Terrain Type at x|y */
	public Terrain getTerrain(int x, int y){
		return this.terrain[y][x].getType();
	}
	
	/** Returns Unit Type at x|y */
	public Unit getUnit(int x, int y){
		return  this.units[y][x].getType();
	}
	
	
	
	
	
	//Inner Classes-----
	@AllArgsConstructor
	/** Inner class for simplifying storage of terrains on the map */
	private static class TerrainContainer{
		
		@Getter private Terrain type;
		@Getter private Player owner;
		
		
		/** Creates default Terrain without any owner */
		public TerrainContainer(Terrain type){
			this.type = type;
			this.owner = Player.NONE;
		}
		
		
		/** Returns a deep clone of this object */
		public TerrainContainer clone(){
			return new TerrainContainer(type, owner);
		}
	}
	
	
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
