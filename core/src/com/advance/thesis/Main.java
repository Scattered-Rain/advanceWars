package com.advance.thesis;

import java.util.Random;

import lombok.Data;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.game.logic.Combat;
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
	
	private Point[] loc;
	
	
	@Override public void create () {
		int size = 8;
		this.map = new Map(size, size);
		this.renderer = new GameRenderer(map);
		loc = new Point[10];
		for(int c=0; c<loc.length; c++){
			loc[c] = new Point(0, 0, size, size);
		}
	}
	
	
	@Override public void render () {
		for(int c=0; c<loc.length; c++){
			map.debugSpawnUnit(Unit.INFANTRY, Player.P0, loc[c]);
		}
		Combat combat = map.calcComabt(loc[0], loc[1]);
		System.out.println(combat+"\n");
		renderer.render();
		Point[] temp = new Point[loc.length];
		for(int c=0; c<loc.length; c++){
			temp[c] = map.getMovementRange(loc[c]).getRandLegalPoint();
		}
		for(int c=0; c<loc.length; c++){
			map.debugEraseUnit(loc[c]);
		}
		loc = temp;
	}
	
}
