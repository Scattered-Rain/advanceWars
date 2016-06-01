package com.advance.thesis.game.enums;

import com.advance.thesis.game.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Units in the game */
public enum Unit {
	
	NONE(-1, "Litterally Empty Space", "tSea", MoveType.FOOT),
	INFANTRY(0, "Infantry", "uInfantry", MoveType.FOOT),
	MECH(1, "Mech Infantry", "uMechInfantry", MoveType.MECH),
	TANK(2, "Tank", "uTank", MoveType.TREADS),
	MEDTANK(3, "Med Tank", "uMedTank", MoveType.TREADS),
	ARTILLERY(4, "Artillery", "uArtillery", MoveType.TREADS);
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	@Getter private MoveType moveType;
	
	/** Returns whether this unit is an actual Unit */
	public boolean isUnit(){
		return id!=NONE.getId();
	}
	
	public AtlasRegion getImg(){
		return GameConstants.atlas.findRegion(imgName);
	}
	
}
