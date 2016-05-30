package com.advance.thesis.game.enums;

import com.advance.thesis.game.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/** Units in the game */
public enum Unit {
	
	NONE(-1, "Litterally Empty Space", "tSea"),
	INFANTRY(0, "Infantry", "uInfantry"),
	MECH(1, "Mech Infantry", "uMechInfantry"),
	TANK(2, "Tank", "uTank"),
	MEDTANK(3, "Med Tank", "uMedTank"),
	ARTILLERY(4, "Artillery", "uArtillery");
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	
	public AtlasRegion getImg(){
		return GameConstants.atlas.findRegion(imgName);
	}
	
}
