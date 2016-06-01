package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.logic.Map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameRenderer extends MapRenderer{
	
	
	/** Constructs new GameRenderer */
	public GameRenderer(Map map){
		super(map);
	}
	
	
	/** Renders Tile, Unit & Unit HP */
	@Override protected void renderTile(SpriteBatch batch, int x, int y){
		batch.draw(map.getTerrain(x, y).getImg(), calcX(x, 0), calcY(y, 0));
		if(map.getUnit(x, y).isUnit()){
			batch.draw(map.getUnit(x, y).getImg(), calcX(x, 0), calcY(y, 0));
		}
	}
	
}
