package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Terrain of the map */
public enum Terrain {
	PLAIN(0, "Plain", 1, false);
	@Getter private int id;
	@Getter private String name;
	@Getter private int defence;
	@Getter private boolean ownable;
}
