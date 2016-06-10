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
import com.advance.thesis.util.range.RangeExpander;
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
	private Point loc = new Point(0, 0);
	private int frame = 0;
	
	
	@Override public void create () {
		this.map = new Map(8, 8);
		this.renderer = new GameRenderer(map);
		this.m = new MapController(map);
	}
	
	
	@Override public void render () {
		frame++;
		Point nPoint = map.getMovementRange(loc).getRandLegalPoint();
		System.out.println("Frame: "+frame);
		System.out.println(m.move(loc, nPoint));
		this.loc = nPoint;
		System.out.println(map.getUnit(loc).getName());
		System.out.println(map.getMovementRange(loc));
		renderer.render();
		
	}
	
}
