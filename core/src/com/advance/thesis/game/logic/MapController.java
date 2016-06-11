package com.advance.thesis.game.logic;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.util.Point;
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
	
	/** Moves unit at origin to target if possible, returns whether movement was successful */
	public boolean move(Point origin, Point target){
		return map.move(origin, target);
	}
	
	/** Resets Units and Movable Units list to get it up to date */
	private void refreshUnits(){
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
	
}
