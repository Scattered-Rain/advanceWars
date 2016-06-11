package com.advance.thesis.game.logic;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.range.RangeCluster;
import com.advance.thesis.util.range.RangeExpander;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Map {
	
	private static final UnitContainer NO_UNIT = new UnitContainer(Unit.NONE, Player.NONE, -1);
	
	/** The dimensions of the map */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the terrain info of the map */
	private Terrain[][] terrain;
	/** Holds the unit info of the map */
	private UnitContainer[][] units;
	
	
	/** Constructs Default, empty map of given dimensions */
	public Map(int width, int height){
		setMap(width, height);
	}
	
	/** Constructs new Map based on raw values (for cloning) */
	private Map(int width, int height, Terrain[][] terrain, UnitContainer[][] units){
		this.width = width;
		this.height = height;
		this.terrain = new Terrain[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				this.terrain[cy][cx] = terrain[cy][cx];
				this.units[cy][cx] = units[cy][cx].clone();
			}
		}
	}
	
	
	/** Basic empty setup of the map */
	private void setMap(int width, int height){
		this.width = width;
		this.height = height;
		this.terrain = new Terrain[height][width];
		this.units = new UnitContainer[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				terrain[cy][cx] = Terrain.getRandomTerrain();
				units[cy][cx] = NO_UNIT;
			}
		}
	}
	
	//Movement Related
	/** Moves Unit from origin to target location - Note: Does not perform ANY checking! */
	protected void rawMove(int originX, int originY, int targetX, int targetY){
		UnitContainer temp = this.units[originY][originX];
		this.units[originY][originX] = NO_UNIT;
		this.units[targetY][targetX] = temp;
	}
	
	/** Moves unit at origin to target if possible, returns whether movement was successful */
	public boolean move(Point origin, Point target){
		if(checkMoveLegality(origin, target)){
			this.rawMove(origin, target);
			return true;
		}
		return false;
	}
	
	/** Moves Unit from origin to target location - Note: Does not perform ANY checking! */
	private void rawMove(Point origin, Point target){
		rawMove(origin.getX(), origin.getY(), target.getX(), target.getY());
	}
	
	/** Returns whether the requested movement is legal */
	public boolean checkMoveLegality(Point origin, Point target){
		if(!(this.inBounds(origin) && this.inBounds(target))){
			System.out.println("Movement parameters out of bound");
			return false;
		}
		if(!(this.getUnit(origin).isUnit() && !this.getUnit(target).isUnit())){
			if(!origin.isIdentical(target)){
				System.out.println("Movement obscured");
				return false;
			}
		}
		if(!this.getMovementRange(origin).inRangeGlobal(target)){
			System.out.println("Movement out of legal move range");
			return false;
		}
		return true;
	}
	
	//Combat Related
	/** Enacts the given combat scenario on the Map, returns COmbat Event (null if not legal and hence not acted upon) */
	public Combat doCombat(Point attacker, Point defender){
		Combat combat = calcCombat(attacker, defender);
		if(combat!=null){
			setUnitHp(attacker, combat.getAttackerHp());
			setUnitHp(defender, combat.getDefenderHp());
			if(!combat.defenderAlive()){
				//If defending Unit has been killed and the attacker is not a ranged unit it will move onto the defenders location
				if(!getUnit(attacker).isRanged()){
					//TODO: MIGHT be buggy? Idunno, recheck.
					this.rawMove(attacker, defender);
				}
			}
			return combat;
		}
		return null;
	}
	
	/** Calculates the Combat Event of the given two Points, returns null if not legal */
	public Combat calcCombat(Point attacker, Point defender){
		if(inBounds(attacker) && inBounds(defender)){
			Unit aUnit = getUnit(attacker);
			Unit dUnit = getUnit(defender);
			if(aUnit.isUnit() && dUnit.isUnit()){
				if(!aUnit.isRanged() && attacker.isAdjacent(defender) || aUnit.isRanged() && this.getShootingRange(attacker).inRangeGlobal(defender)){
					Combat combat = new Combat(aUnit, dUnit, getUnitContainer(attacker).getHp(), 
							getUnitContainer(defender).getHp(), getTerrain(attacker), getTerrain(defender), aUnit.isRanged());
					return combat;
				}
			}
		}
		return null;
	}
	
	/** Sets the HP of the given Unit, if HP=0 Unit is removed, returns whether Unit is removed */
	public boolean setUnitHp(Point unit, int hp){
		if(getUnit(unit).isUnit()){
			if(hp>0){
				getUnitContainer(unit).setHp(hp);
			}
			else if(hp==0){
				this.eraseUnit(unit);
				return true;
			}
		}
		else{
			System.out.println("Unit to be changed HP upon is not existent");
		}
		return false;
	}
	
	//Utility
	/** Returns Terrain Type at x|y */
	public Terrain getTerrain(int x, int y){
		return this.terrain[y][x];
	}
	
	/** Returns Terrain Type at given Point */
	public Terrain getTerrain(Point point){
		return getTerrain(point.getX(), point.getY());
	}
	
	/** Returns Unit Type at x|y */
	public Unit getUnit(int x, int y){
		return  this.units[y][x].getType();
	}
	
	/** Returns Unit Type at given Point */
	public Unit getUnit(Point point){
		return getUnit(point.getX(), point.getY());
	}
	
	/** Returns Unit Container at given location */
	public UnitContainer getUnitContainer(Point loc){
		return units[loc.y][loc.x];
	}
	
	/** Returns whether the given point is actually on the map */
	public boolean inBounds(Point loc){
		return loc.x>=0 && loc.x<this.width && loc.y>=0 && loc.y<this.height;
	}
	
	/** Returns RangeCluster of the movement at the given point */
	public RangeCluster getMovementRange(Point unit){
		return RangeExpander.calcMoveRange(this, unit);
	}
	
	/** Returns the Shooting Range of the unit at the given point */
	public RangeCluster getShootingRange(Point unit){
		return RangeExpander.calcShootingRange(this, unit);
	}
	
	/** Erases Unit at the given location */
	private void eraseUnit(Point unit){
		this.units[unit.y][unit.x] = NO_UNIT;
	}
	
	/** Returns deep clone of this map */
	public Map clone(){
		return new Map(width, height, terrain, units);
	}
	
	
	//Debug
	public void debugSpawnUnit(Unit unit, Player owner, Point point){
		this.units[point.y][point.x] = new UnitContainer(unit, owner);
	}
	
	public void debugEraseUnit(Point point){
		this.units[point.y][point.x] = NO_UNIT;
	}
	
	//Inner Utility classes -----------
	
	@AllArgsConstructor
	/** Inner class for simplifying storage of active units on the map */
	protected static class UnitContainer{
		@Getter private Unit type;
		@Getter private Player owner;
		@Setter @Getter private int hp;
		/** Spawn Constructor */
		public UnitContainer(Unit type, Player owner){
			this.type = type;
			this.owner = owner;
			this.hp = 10;
		}
		/** Returns a deep clone of this object */
		public UnitContainer clone(){
			return new UnitContainer(type, owner, hp);
		}
	}
	
}
