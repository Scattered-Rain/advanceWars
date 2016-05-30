package com.advance.thesis.game.enums;

import com.advance.thesis.game.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Terrain of the map */
public enum Terrain {
	
	PLAIN(0, "Plain", "tPlains", 1, false),
	HILL(1, "Hill", "tHill", -1, false),
	ROAD(2, "Road", "tRoad", 0, false),
	SEA(3, "Sea", "tSea", 0, false),
	FOREST(4, "Forest", "tForest", -1, false),
	CITY(5, "City", "tCity", -1, true);
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	@Getter private int defence;
	@Getter private boolean ownable;
	
	public AtlasRegion getImg(){
		return GameConstants.atlas.findRegion(imgName);
	}
	
}
