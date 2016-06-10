package com.advance.thesis.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Holds Constants relevant to Game mechanics themselves */
public class GameConstants {
	
	/** The Atlas holding all the assets necessary for rendering the map */
	public static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal("map/atlas.atlas"));
	
	/** Random object to be generally used by systems in the game */
	public static final Random RANDOM = new Random();
	
	/** Movement Cost that represents impassability */
	public static final int IMPASSABLE = -1;
	
	/** Size in Pixels of a Tile */
	public static final int TILE = 16;
	
	
}
