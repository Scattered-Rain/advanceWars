package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.util.Point;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MainRenderer extends MapRenderer{
	
	
	/** Constructs new GameRenderer */
	public MainRenderer(Map map){
		super(map);
	}
	
	
	/** Renders Tile, Unit & Unit HP */
	@Override protected void renderTile(SpriteBatch batch, int x, int y){
		batch.draw(map.getTerrain(x, y).getImg(), calcX(x, 0), calcY(y, 0));
		if(map.getUnit(x, y).isUnit()){
			AtlasRegion toDraw = map.getUnit(x, y).getImg();
			if(map.getUnitContainer(new Point(x, y)).getOwner().equals(Player.P1)){
				batch.setColor(Color.BLUE);
			}
			batch.draw(toDraw, calcX(x, 0), calcY(y, 0));
		}
	}
	
}
