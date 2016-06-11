package com.advance.thesis.game.logic;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.advance.thesis.game.enums.Player;
import com.advance.thesis.util.Point;

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
	
	
	//Inner Utility Class
	/** Keeps track of the unit, its hp AND Location */
	@AllArgsConstructor
	public class LocUnitContainer{
		@Getter private Map.UnitContainer unitCont;
		@Getter @Setter private Point location;
	}
	
}
