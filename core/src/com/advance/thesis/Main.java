package com.advance.thesis;

import java.util.List;
import java.util.Random;

import lombok.Data;

import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.ai.basicAi.AgressiveRandomAI;
import com.advance.thesis.ai.blueCanary.BlueCanary;
import com.advance.thesis.ai.blueCanary.CanaryBreeder;
import com.advance.thesis.ai.blueCanary.Crossover;
import com.advance.thesis.ai.blueCanary.Neurals;
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
import com.advance.thesis.game.mapRenderer.MainRenderer;
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
	
	private CanaryBreeder breeder;
	
	private List<Map> game;
	private int gameStep;
	
	@Override public void create () {
		this.breeder = new CanaryBreeder(TiledMapFactory.importMap(MapTiled.DEFAULT));
		Thread thread = new Thread(){
			@Override public void run(){
				breeder.process();
			}
		};
		thread.start();
		setGame();
	}
	
	
	@Override public void render(){
		if(game.size()<=gameStep){
			setGame();
		}
		//MapRenderer renderer = new MainRenderer(breeder.getWindow());
		MapRenderer renderer = new MainRenderer(game.get(gameStep));
		renderer.render();
		renderer.dispose();
		gameStep++;
		try{
			Thread.sleep(1000);
		}catch(Exception ex){}
	}
	
	private void setGame(){
		this.game = breeder.getGame();
		this.gameStep = 0;
	}
	
}
