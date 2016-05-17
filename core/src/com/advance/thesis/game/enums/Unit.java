package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Units in the game */
public enum Unit {
	NONE(-1, "Litterally Empty Space"),
	INFANTRY(0, "Infantry");
	@Getter private int id;
	@Getter private String name;
}
