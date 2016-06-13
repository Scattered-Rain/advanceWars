package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.logic.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Base class used for the implementations of Map Renderers */
public abstract class MapRenderer {
	
	/** The Camera used for drawing */
	private Camera cam;
	/** The Sprite Batch used to render the map */
	private SpriteBatch batch;
	
	/** The Map this MapRenderer is supposed to visualize */
	protected Map map;
	
	
	/** Constructs new MapRenderer */
	public MapRenderer(Map map){
		this.map = map;
		this.batch = new SpriteBatch();
		this.cam = new OrthographicCamera();
		int camWidth = map.getWidth()*GameConstants.TILE;
		int camHeight = map.getHeight()*GameConstants.TILE;
		cam.viewportWidth = camWidth;
		cam.viewportHeight = camHeight;
		cam.position.x = camWidth/2;
		cam.position.y = -camHeight/2;
		cam.update();
	}
	
	
	/** Renders the entirety of the map */
	public final void render(){
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		for(int cy=0; cy<map.getHeight(); cy++){
			for(int cx=0; cx<map.getWidth(); cx++){
				batch.setColor(Color.WHITE);
				renderTile(batch, cx, cy);
			}
		}
		batch.end();
	}
	
	/** Renders the Tile at given x|y */
	protected abstract void renderTile(SpriteBatch batch, int x, int y);
	
	/** Returns calculated x position of current tile */
	protected int calcX(int x, int space){
		return GameConstants.TILE*x + space;
	}
	
	/** Returns calculated y position of current tile */
	protected int calcY(int y, int space){
		return -GameConstants.TILE*(y+1) - space;
	}
	
	
	/** Disposes MapRenderer */
	public void dispose(){
		this.batch.dispose();
	}
	
}
