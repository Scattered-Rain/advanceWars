package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.Map;
import com.advance.thesis.game.enums.Terrain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Base class used for the implementations of Map Renderers */
public class MapRenderer {
	
	/** The Atlas holding all the assets necessary for rendering the map */
	private static TextureAtlas atlas = null;
	
	/** The Map this MapRenderer is supposed to visualize */
	protected Map map;
	
	/** Constructs new MapRenderer */
	public MapRenderer(Map map){
		this.map = map;
		initAtlas();
	}
	
	/** Initializes atlas */
	private static void initAtlas(){
		if(atlas==null){
			atlas = new TextureAtlas(Gdx.files.internal("map/atlas.atlas"));
			System.out.println(atlas.findRegion(Terrain.FOREST.getImgName()+"hi"));
		}
	}
	
}
