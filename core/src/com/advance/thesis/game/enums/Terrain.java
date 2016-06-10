package com.advance.thesis.game.enums;

import static com.advance.thesis.game.GameConstants.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Terrain of the map */
public enum Terrain {
	PLAINS(0, "Plains", "tPlains", 1, false, 
			new int[]{1, 1, 1, 1}),
	HILL(1, "Hill", "tHill", 4, false, 
			new int[]{2, 1, IMPASSABLE, IMPASSABLE}),
	ROAD(2, "Road", "tRoad", 0, false, 
			new int[]{1, 1, 1, 1}),
	SEA(3, "Sea", "tSea", 0, false, 
			new int[]{IMPASSABLE, IMPASSABLE, IMPASSABLE, IMPASSABLE}),
	FOREST(4, "Forest", "tForest", 2, false, 
			new int[]{1, 1, 2, 3}),
	CITY(5, "City", "tCity", 3, true, 
			new int[]{1, 1, 1, 1});
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	@Getter private int defence;
	@Getter private boolean ownable;
	//Lists all movement costs, indexed according to MoveType indexes (-1 represents no possible movement)
	@Getter private int[] moveCosts;
	
	/** Returns the movement costs of this terrain type given the Movement Type */
	public int getMoveCost(MoveType moveType){
		if(moveCosts[moveType.getIndex()]==IMPASSABLE){
			return Integer.MAX_VALUE;
		}
		return moveCosts[moveType.getIndex()];
	}
	
	/** Returns the movement costs of this terrain type given a specific unit */
	public int getMoveCost(Unit unit){
		return getMoveCost(unit.getMoveType());
	}
	
	/** Returns name of image used for representing Terrain on map */
	public AtlasRegion getImg(){
		return ATLAS.findRegion(imgName);
	}
	
}
