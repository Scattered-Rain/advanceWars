package com.advance.thesis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Holds Constants relevant to Game mechanics themselves */
public class GameConstants {
	
	/** The Atlas holding all the assets necessary for rendering the map */
	public static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("map/atlas.atlas"));
	
}
