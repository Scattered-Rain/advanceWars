package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.Map;
import com.advance.thesis.game.enums.Terrain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Base class used for the implementations of Map Renderers */
public abstract class MapRenderer {
	
	/** The dimensions of the screen in pixels */
	private int width;
	private int height;
	
	/** The Map this MapRenderer is supposed to visualize */
	protected Map map;
	
	
	/** Constructs new MapRenderer */
	public MapRenderer(Map map){
		this.map = map;
	}
	
	
	/** Renders the entirety of the map */
	public final void render(){
		
	}
	
	protected abstract void renderSomething();
	
}
