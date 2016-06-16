package com.advance.thesis;

import java.util.List;
import java.util.Random;

import lombok.Data;

import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.ai.basicAi.AgressiveRandomAI;
import com.advance.thesis.ai.blueCanary.BlueCanary;
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
	
	public static Main main;
	
	private Map map;
	private MapRenderer renderer;
	private int size;
	
	private int[] wins = new int[]{0, 0};
	
	private AbstractAI[] ais;
	
	
	@Override public void create () {
		main = this;
		this.size = 8;
		newMap(size);
		setAi();
		this.renderer = new MainRenderer(map);
	}
	
	private void newMap(int size){
		this.map = TiledMapFactory.importMap(MapTiled.STRAT);
	}
	
	@Override public void render(){
		//while(true){
			for(int c=0; c<ais.length; c++){
				//System.out.println("Player "+c+"'s Move");
				ais[c].process();
			}
			if(!map.calcWinner().equals(Player.NONE)){
				this.wins[map.calcWinner().getId()]++;
				System.out.println(wins[0]+" "+wins[1]);
				this.newMap(size);
				setAi();
				resetRenderer();
			}
			try{Thread.sleep(0);}catch(Exception ex){}
			renderer.render();
		//}
	}
	
	private void setAi(){
		this.ais = new AbstractAI[]{new BlueCanary(new MapController(map, Player.P0)), new AgressiveRandomAI(new MapController(map, Player.P1))};
	}
	
	public static void resetRenderer(){
		main.renderer.dispose();
		main.renderer = new MainRenderer(main.map);
	}
	
	public static void setRenderer(MapRenderer renderer){
		main.renderer.dispose();
		main.renderer = renderer;
	}
	
}
