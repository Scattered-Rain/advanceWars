package com.advance.thesis.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Describes all types of possible movement */
public enum MoveType {
	FOOT(0),
	MECH(1),
	TREADS(2),
	TIRES(3);//Tires aren't actually used by any of the units
	
	/** Index doubles as id for the movement type and the index for the movement costs in Terrain */
	@Getter private int index;
}
