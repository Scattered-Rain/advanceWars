package com.advance.thesis.game.enums;

import static com.advance.thesis.game.GameConstants.*;
import static com.advance.thesis.game.enums.MoveType.*;
import com.advance.thesis.util.Point;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Units in the game */
@AllArgsConstructor
public enum Unit {
	INFANTRY(0, "Infantry", "uInfantry", FOOT, 3, new Point(0, 0),
			new UnitList(55, 45, 5, 1, 15)),
	MECH(1, "Mech Infantry", "uMechInfantry", MECH_MOVE, 2, new Point(0, 0),
			new UnitList(65, 55, 55, 15, 70)),
	TANK(2, "Tank", "uTank", TREADS, 6, new Point(0, 0),
			new UnitList(75, 70, 55, 15, 70)),
	MEDTANK(3, "Med Tank", "uMedTank", TREADS, 5, new Point(0, 0),
			new UnitList(105, 95, 85, 55, 105)),
	ARTILLERY(4, "Artillery", "uArtillery", TREADS, 5, new Point(2, 3),
			new UnitList(90, 85, 79, 45, 75)),
	NONE(-1, "No Unit", "tSea", FOOT, 0, new Point(0, 0), 
			new UnitList(0, 0, 0, 0, 0));
	
	@Getter private int id;
	@Getter private String name;
	@Getter private String imgName;
	@Getter private MoveType moveType;
	//Describes the amount of movement this unit has in one turn
	@Getter private int movement;
	//Describes the range this unit has for ranged attacks
	@Getter private Point shootingRange;
	//Describes the base damage the unit does to any given other unit
	@Getter private UnitList baseDamage;
	
	
	public static Unit getRandomUnit(){
		return Unit.values()[RANDOM.nextInt(Unit.values().length-1)];
	}
	
	/** Returns whether this unit is an actual Unit */
	public boolean isUnit(){
		return id!=NONE.getId();
	}
	
	/** Returns the base damage of this unit for the given unit */
	public int getBaseDamage(Unit unit){
		return baseDamage.getValue(unit);
	}
	
	
	/** Returns the image of this unit */
	public AtlasRegion getImg(){
		return ATLAS.findRegion(imgName);
	}
	
	/** Retruns whether this Unit is Ranged */
	public boolean isRanged(){
		return !this.shootingRange.equals(new Point(0, 0));
	}
	
	
	//Inner Utility class----
	/** Class that defines a set of values each of which corresponds to a certain Unit */
	private static class UnitList{
		/** The internal values, index corresponds to unit id (does not contain NONE)*/
		private int[] values;
		/** Constructs new Class */
		public UnitList(int infantary, int mech, int tank, int medTank, int artillery){
			this.values = new int[]{infantary, mech, tank, medTank, artillery};
		}
		/** Returns held value corresponding to given Unit id */
		public int getValue(int unitTypeId){
			if(unitTypeId==NONE.id){
				return -1;
			}
			return values[unitTypeId];
		}
		/** Returns held value corresponding to given Unit Type */
		public int getValue(Unit unit){
			return getValue(unit.getId());
		}
	}
}
