package com.advance.thesis.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.advance.thesis.Main;

public class AdvanceThesis {
	
	public static void main (String[] arg) {
		pack();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.title = "Advance Thesis";
		config.fullscreen = false;
		config.width = 16*16*2;
		config.height = 16*16*2;
		Main map = new Main();
		new LwjglApplication(map, config);
	}
	
	private static void pack(){
		Autopacker.packFolder("map", "map", "atlas");
	}
	
}
