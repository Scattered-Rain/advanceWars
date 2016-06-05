package com.advance.thesis;

import java.util.Random;

import lombok.Data;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.mapRenderer.GameRenderer;
import com.advance.thesis.game.mapRenderer.MapRenderer;
import com.advance.thesis.util.Direction;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.RangeExpander;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Main Node for the Game on its own. */
public class Main extends ApplicationAdapter {
	
	private Map map;
	private MapRenderer renderer;
	
	private MapController m;
	private int[] loc = new int[]{0, 0};
	
	
	@Override public void create () {
		this.map = new Map(16, 16);
		this.renderer = new GameRenderer(map);
		this.m = new MapController(map);
	}
	
	
	@Override public void render () {
		Random rand = new Random();
		int x = rand.nextInt(16);
		int y = rand.nextInt(16);
		m.move(loc[0], loc[1], x, y);
		this.loc[0] = x;
		this.loc[1] = y;
		System.out.println(map.getUnit(x, y).getName());
		System.out.println(RangeExpander.calcMoveRange(map, new Point<Integer>(x, y)));
		renderer.render();
	}
	
}
