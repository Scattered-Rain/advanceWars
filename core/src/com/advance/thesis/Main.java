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
	private int size;
	private Point[] loc;
	
	
	@Override public void create () {
		this.size = 8;
		this.map = new Map(size, size);
		this.renderer = new GameRenderer(map);
		loc = new Point[10];
		for(int c=0; c<loc.length; c++){
			loc[c] = new Point(0, 0, size, size);
			map.debugSpawnUnit(Unit.getRandomUnit(), Player.P0, loc[c]);
		}
	}
	
	
	@Override public void render () {
		renderer.render();
		for(int c=0; c<loc.length; c++){
			int target = (c+1)%loc.length;
			if(!map.getUnit(loc[c]).isUnit()){
				loc[c] = new Point(0, 0, size, size);
				map.debugSpawnUnit(Unit.getRandomUnit(), Player.P0, loc[c]);
				System.out.println("Spawn: "+loc[c]+" "+map.getUnit(loc[c]).getName());
			}
			boolean mayMove = true;
			if(map.getUnit(loc[c]).isRanged()){
				Combat comb = map.calcCombat(loc[c], loc[target]);
				if(comb!=null){
					map.doCombat(loc[c], loc[target]);
					mayMove = false;
					System.out.println("Ranged: "+loc[c]+" "+loc[target]+"\n"+comb);
					System.out.println();
				}
			}
			if(mayMove){
				Point potNext = map.getMovementRange(loc[c]).getRandLegalPoint();
				System.out.println(map.getMovementRange(loc[c]));
				System.out.println("--");
				System.out.println(RangeExpander.calcCloseCombatRange(map, loc[c]));
				boolean legal = true;
				for(int c2=0; c2<loc.length; c2++){
					if(c2!=c){
						if(loc[c2].isIdentical(potNext)){
							legal = false;
						}
					}
				}
				if(legal){
					map.move(loc[c], potNext);
					loc[c] = potNext;
				}
			}
			if(!map.getUnit(loc[c]).isRanged()){
				Combat comb = map.calcCombat(loc[c], loc[target]);
				if(comb!=null){
					map.doCombat(loc[c], loc[target]);
					System.out.println("Close: "+loc[c]+" "+loc[target]+"\n"+comb);
					System.out.println();
					if(!comb.defenderAlive()){
						//loc[0] = loc[target];
					}
				}
			}
		}
	}
	
}
