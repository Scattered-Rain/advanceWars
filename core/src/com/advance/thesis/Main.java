package com.advance.thesis;

import java.util.List;
import java.util.Random;

import lombok.Data;

import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.ai.basicAi.AgressiveRandomAI;
import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.MapTiled;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.game.logic.Combat;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.game.logic.Map.UnitContainer;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.logic.TiledMapFactory;
import com.advance.thesis.game.mapRenderer.GameRenderer;
import com.advance.thesis.game.mapRenderer.MapRenderer;
import com.advance.thesis.util.Direction;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.Tuple;
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
	private int size;
	
	private AbstractAI[] ais;
	
	
	@Override public void create () {
		this.size = 8;
		newMap(size);
		this.ais = new AbstractAI[]{new AgressiveRandomAI(new MapController(map, Player.P0)), new AgressiveRandomAI(new MapController(map, Player.P1))};
		this.renderer = new GameRenderer(map);
	}
	
	private void newMap(int size){
		this.map = TiledMapFactory.importMap(MapTiled.DEFAULT);
	}
	int[] wins = new int[]{0, 0};
	@Override public void render(){
		
		for(int c=0; c<ais.length; c++){
			//System.out.println("Player "+c+"'s Move");
			ais[c].process();
		}
		if(!map.calcWinner().equals(Player.NONE)){
			wins[map.calcWinner().getId()]++;
			this.newMap(size);
			this.ais = new AbstractAI[]{new AgressiveRandomAI(new MapController(map, Player.P0)), new AgressiveRandomAI(new MapController(map, Player.P1))};
			this.renderer.dispose();
			this.renderer = new GameRenderer(map);
			System.out.println(wins[0]+" "+wins[1]);
		}
		try{Thread.sleep(0);}catch(Exception ex){}
		renderer.render();
	}
	
}
