package com.advance.thesis;

import lombok.Data;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.Map;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.mapRenderer.GameRenderer;
import com.advance.thesis.game.mapRenderer.MapRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Main Node for the Game on its own. */
public class Main extends ApplicationAdapter {
	
	private Map map;
	private MapRenderer renderer;
	
	
	@Override public void create () {
		this.map = new Map(16, 16);
		this.renderer = new GameRenderer(map);
	}
	
	@Override public void render () {
		renderer.render();
	}
	
}
