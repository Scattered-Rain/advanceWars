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
	private Terrain[][] terrain;
	/** Holds the unit info of the map */
	private UnitBox[][] units;
	
	
	/** Constructs new Map based on raw values (for cloning) */
	private Map(int width, int height, Terrain[][] terrain, UnitBox[][] units){
		this.width = width;
		this.height = height;
		this.terrain = new Terrain[height][width];
		this.units = new UnitBox[height][width];
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
		this.units = new UnitBox[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				units[cy][cx] = new UnitBox(Unit.NONE, Player.NONE, -1);
			}
		}
	}
	
	/** Returns deep clone of this map */
	public Map clone(){
		return new Map(width, height, terrain, units);
	}
	
	
	//Inner Classes-----
	@AllArgsConstructor
	/** Inner class for simplifying storage of active units on the map */
	private static class UnitBox{
		
		@Getter private Unit type;
		@Getter private Player player;
		@Getter private int hp;
		
		/** Returns a deep clone of this object */
		public UnitBox clone(){
			return new UnitBox(type, player, hp);
		}
	}
	
}
