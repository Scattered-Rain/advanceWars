package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Identifies a player */
public enum Player {
	P0(0),
	P1(1),
	NONE(-1);
	@Getter private int id;
	
	/** Returns Player of given index */
	public static Player getPlayer(int index){
		return Player.values()[index];
	}
	
	/** Returns whether the given Player is identical to this Player */
	public boolean isThis(Player p){
		return this.id==p.getId();
	}
	
	public Player getOpponent(){
		if(id==-1){
			return NONE;
		}
		else if(id==0){
			return P1;
		}
		else{
			return P0;
		}
	}
	
}
