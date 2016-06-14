package com.advance.thesis.game.logic;

import com.advance.thesis.game.enums.MapTiled;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.game.logic.Map.UnitContainer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TiledMapFactory {
	
	private static final String TERRAIN_PROP = "TERRAIN";
	private static final String UNIT_PROP = "UNIT";
	private static final String PLAYER_PROP = "PLAYER";
	
	/** Returns Map that corresponds to the given TiledMap */
	public static Map importMap(MapTiled tMap){
		TmxMapLoader mapLoader = new TmxMapLoader();
		TiledMap tiledMap = mapLoader.load(tMap.getPath());
		TiledMapTileLayer terrain = (TiledMapTileLayer)tiledMap.getLayers().get(0);
		TiledMapTileLayer units = (TiledMapTileLayer)tiledMap.getLayers().get(1);
		int width = terrain.getWidth();
		int height = terrain.getHeight();
		Terrain[][] terrainArray = new Terrain[height][width];
		Map.UnitContainer[][] unitArray = new Map.UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				//Terrain
				TiledMapTile terrainTile = terrain.getCell(cx, cy).getTile();
				int terrainIndex = Integer.parseInt(terrainTile.getProperties().get(TERRAIN_PROP, String.class));
				terrainArray[cy][cx] = Terrain.values()[terrainIndex];
				//Unit
				UnitContainer unit = Map.NO_UNIT;
				if(units.getCell(cx, cy)!=null){
					TiledMapTile unitTile = units.getCell(cx, cy).getTile();
					int unitIndex = Integer.parseInt(unitTile.getProperties().get(UNIT_PROP, String.class));
					int player = Integer.parseInt(unitTile.getProperties().get(PLAYER_PROP, String.class));
					unit = new UnitContainer(Unit.values()[unitIndex], Player.values()[player]);
				}
				unitArray[cy][cx] = unit;
			}
		}
		return new Map(width, height, terrainArray, unitArray);
	}
	
}
