package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Identifies a player */
public enum Player {
	NONE(-1),
	P0(0),
	P1(1);
	@Getter private int id;
	
	/** Returns whether the given Player is identical to this Player */
	public boolean isThis(Player p){
		return this.id==p.getId();
	}
	
}
