package com.advance.thesis.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.advance.thesis.Main;

public class AdvanceThesis {
	
	public static void main (String[] arg) {
		pack();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Main(), config);
	}
	
	private static void pack(){
		Autopacker.packFolder("terrain", "terrain", "atlas");
		Autopacker.packFolder("units", "units", "atlas");
	}
	
}
