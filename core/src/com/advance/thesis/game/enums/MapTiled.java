package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Represents Maps that are loaded in via Tiled */
public enum MapTiled {
	DEFAULT(0, "Default Map", "default");
	@Getter private int id;
	@Getter private String name;
	@Getter private String localPath;
	/** Returns the global Path of the map */
	public String getPath(){
		return "maps/"+localPath+".tmx";
	}
}
