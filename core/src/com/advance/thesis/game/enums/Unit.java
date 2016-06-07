package com.advance.thesis.game.enums;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.util.Point;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Units in the game */
public enum Unit {
	NONE(-1, "No Unit", "tSea", MoveType.FOOT, 0, new Point<Integer>(0, 0)),
	INFANTRY(0, "Infantry", "uInfantry", MoveType.FOOT, 3, new Point<Integer>(0, 0)),
	MECH(1, "Mech Infantry", "uMechInfantry", MoveType.MECH, 2, new Point<Integer>(0, 0)),
	TANK(2, "Tank", "uTank", MoveType.TREADS, 6, new Point<Integer>(0, 0)),
	MEDTANK(3, "Med Tank", "uMedTank", MoveType.TREADS, 5, new Point<Integer>(0, 0)),
	ARTILLERY(4, "Artillery", "uArtillery", MoveType.TREADS, 5, new Point<Integer>(2, 3));
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	@Getter private MoveType moveType;
	//Describes the amount of movement this unit has in one turn
	@Getter private int movement;
	//Describes the range this unit has for ranged attacks
	@Getter private Point<Integer> shootingRange;
	
	/** Returns whether this unit is an actual Unit */
	public boolean isUnit(){
		return id!=NONE.getId();
	}
	
	public AtlasRegion getImg(){
		return GameConstants.atlas.findRegion(imgName);
	}
	
}
