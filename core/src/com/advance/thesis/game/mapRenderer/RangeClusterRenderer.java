package com.advance.thesis.game.mapRenderer;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.range.RangeCluster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/** Renders a Range Cluster */
public class RangeClusterRenderer extends MapRenderer{
	
	private RangeCluster cluster;
	
	public RangeClusterRenderer(Map map, RangeCluster cluster) {
		super(map.clone());
		this.cluster = cluster;
	}

	@Override protected void renderTile(SpriteBatch batch, int x, int y) {
		boolean inRange = false;
		batch.setColor(Color.RED);
		if(cluster.inRangeGlobal(x, y)){
			inRange = true;
			batch.setColor(Color.BLUE);
			if(cluster.localToGlobal(cluster.getOrigin()).equals(new Point(x, y))){
				batch.setColor(Color.CYAN);
			}
		}
		batch.draw(map.getTerrain(x, y).getImg(), calcX(x, 0), calcY(y, 0));
		//Draws normal units, boring 
		if(map.getUnit(x, y).isUnit()){
			batch.setColor(Color.WHITE);
			AtlasRegion toDraw = map.getUnit(x, y).getImg();
			if(map.getUnitContainer(new Point(x, y)).getOwner().equals(Player.P1)){
				batch.setColor(Color.BLUE);
			}
			batch.draw(toDraw, calcX(x, 0), calcY(y, 0));
			GameConstants.FONT.draw(batch, ""+map.getUnitContainer(new Point(x, y)).getHp(), calcX(x, 0), calcY(y-1, 0));
		}
		//Draw in Range Values
		if(inRange){
			GameConstants.FONT.setColor(Color.RED);
			GameConstants.FONT.draw(batch, ""+cluster.getValueAtGlobal(new Point(x, y)), calcX(x, 1), calcY(y-1, 6));
		}
	}

}
