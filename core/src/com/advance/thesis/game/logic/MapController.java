package com.advance.thesis.game.logic;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.util.Direction;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.Tuple;
import com.advance.thesis.util.range.RangeCluster;

import static com.advance.thesis.game.logic.Map.*;

/** General means of controlling the control based part of the game logic */
public class MapController {
	
	/** The Player this Controller is registered under */
	@Getter private Player player;
	
	/** The Map this Controller influences */
	@Getter private Map map;
	
	/** List of all units available to this player */
	@Getter private List<LocUnitContainer> units;
	/** List of all units still movable by this player */
	@Getter private List<LocUnitContainer> stillMovableUnits;
	
	/** Constructs new Map Controller */
	public MapController(Map map){
		this.map = map;
		this.units = new ArrayList<Map.LocUnitContainer>();
		this.stillMovableUnits = new ArrayList<Map.LocUnitContainer>();
		refreshUnits();
	}
	
	/** Resets Units and Movable Units list to get it up to date */
	public void refreshUnits(){
		this.units.clear();
		this.stillMovableUnits.clear();
		for(int cy=0; cy<map.getHeight(); cy++){
			for(int cx=0; cx<map.getWidth(); cx++){
				Point loc = new Point(cx, cy);
				if(map.getUnitContainer(loc).getOwner().equals(player)){
					LocUnitContainer cont = new LocUnitContainer(map.getUnitContainer(loc), loc);
					this.units.add(cont);
					this.stillMovableUnits.add(cont);
				}
			}
		}
	}
	
	/** Returns a list of all units within attacking range of the given unit that do not belong to this Player */
	public List<LocUnitContainer> getAttackableUnits(Point loc){
		UnitContainer unit = map.getUnitContainer(loc);
		RangeCluster range = null;
		if(!unit.getType().isRanged()){
			range = map.getCloseCombatRange(loc);
		}
		else{
			range = map.getShootingRange(loc);
		}
		List<LocUnitContainer> units = range.getAllUnitsInRange(map);
		for(int c=0; c<units.size(); c++){
			if(units.get(c).getUnitCont().getOwner().equals(this.player)){
				units.remove(c);
				c--;
			}
		}
		return units;
	}
	
	/** Returns a list containing all close range attackable units of the given location, stored in a separate list containing
	 	all possible directions that can be attacked from as represented with a Tuple that stores the target location of the attack 
	 	at index 1 and the position the given unit has to attack from at index 0 */
	public List<List<Tuple<Point>>> getAttackableUnitsCloseRange(Point loc){
		List<LocUnitContainer> list = getAttackableUnits(loc);
		List<List<Tuple<Point>>> out = new ArrayList<List<Tuple<Point>>>();
		RangeCluster cluster = map.getMovementRange(loc);
		for(int c=0; c<list.size(); c++){
			List<Tuple<Point>> dirList = new ArrayList<Tuple<Point>>();
			LocUnitContainer cont = list.get(c);
			Point contLoc = cont.getLocation();
			for(int c2=0; c2<Direction.values().length; c2++){
				Point standOn = contLoc.add(Direction.values()[c2].getDir());
				if(cluster.inRange(standOn)){
					dirList.add(new Tuple<Point>(standOn, contLoc));
				}
			}
			out.add(dirList);
		}
		return out;
	}
	
	/** Calculates Combat */
	public Combat calcCombat(Point attacker, Point defender){
		return map.calcCombat(attacker, defender);
	}
	
	
	//Acting upon the game world--
	/** Moves unit at origin to target if possible, returns whether movement was successful */
	public boolean move(Point origin, Point target){
		int stillMovable = getStillMovable(origin);
		if(stillMovable>=0){
			if(map.checkMoveLegality(origin, target)){
				map.move(origin, target);
				stillMovableUnits.remove(stillMovable);
			}
		}
		return false;
	}
	
	/** Does Combat, checks whether move legal and given attacker unmoved, consumes move if so, if not Combat = null */
	public Combat doCombat(Point attacker, Point defender){
		int stillMovable = getStillMovable(attacker);
		if(stillMovable>=0){
			Combat combat = map.doCombat(attacker, defender);
			if(combat!=null){
				stillMovableUnits.remove(stillMovable);
				return combat;
			}
		}
		return null;
	}
	
	/** Moves the given unit to the given point (moveTo) and then attacks the defender, consumes move, if anything in the
		instructions is not legal returns null and doesn't enact anything */
	public Combat moveAndDoCombat(Point unit, Point moveTo, Point defender){
		int stillMovable = getStillMovable(unit);
		if(stillMovable>=0){
			if(!map.getUnit(unit).isRanged() || unit.isIdentical(moveTo)){
				if(map.checkMoveLegality(unit, moveTo)){
					UnitContainer help = map.getUnitContainer(moveTo);
					map.debugSpawnUnit(map.getUnitContainer(unit), moveTo);
					Combat combat = map.calcCombat(moveTo, defender);
					map.debugSpawnUnit(help, moveTo);
					if(combat!=null){
						map.move(unit, moveTo);
						map.doCombat(moveTo, defender);
						stillMovableUnits.remove(stillMovable);
						return combat;
					}
				}
			}
		}
		return null;
	}
	
	//Utility
	/** Returns index of the given unit in the stillMovable list, if it's not contained returns -1 (i.e. stillMovabilit = !-1)*/
	private int getStillMovable(Point unit){
		for(int c=0; c<this.stillMovableUnits.size(); c++){
			if(stillMovableUnits.get(c).getLocation().isIdentical(unit)){
				return c;
			}
		}
		return -1;
	}
}
