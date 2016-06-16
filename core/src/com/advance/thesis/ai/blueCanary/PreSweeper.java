package com.advance.thesis.ai.blueCanary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.advance.thesis.Main;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.game.logic.Map.LocUnitContainer;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.mapRenderer.RangeClusterRenderer;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.range.RangeCluster;

/** Presweep Unit */
public class PreSweeper {
	
	private static final int VTRUE = 1;
	
	private Map map;
	private Player player;
	private MapController[] controllers;
	
	public PreSweeper(Map map, Player player){
		this.map = map;
		this.player = player;
		this.controllers = new MapController[]{new MapController(map, Player.P0), new MapController(map, Player.P1)};
	}
	
	/** Executes Presweep on the given map considering the player identity */
	public List<RangeCluster> preSweep(){
		List<RangeCluster> sweep = new ArrayList<RangeCluster>();
		RangeCluster[] temp = null;
		
		//Adds Terrain Defences
		sweep.add(terrainDefence(map));
		
		//Generally Reachable by Player
		sweep.add(inMoveRange(map, player));
		//Generally Reachable by Opponent
		sweep.add(inMoveRange(map, player.getOpponent()));
		
		//Summed attack values by close unit type of player
		temp = inCloseCombatRange(map, player);
		for(int c=0; c<temp.length; c++){
			sweep.add(temp[c]);
		}
		//Summed attack values by close unit type of opponent
		temp = inCloseCombatRange(map, player.getOpponent());
		for(int c=0; c<temp.length; c++){
			sweep.add(temp[c]);
		}
		
		//Summed attack values by ranged unit type of player
		sweep.add(inShootingRangeCombatRange(map, player));
		//Summed attack values by ranged unit type of opponent
		sweep.add(inShootingRangeCombatRange(map, player.getOpponent()));
		
		//Hp of the individual units (order equivalent to their ids) of the player
		temp = unitHp(map, player);
		for(int c=0; c<temp.length; c++){
			sweep.add(temp[c]);
		}
		//Hp of the individual units (order equivalent to their ids) of the opponent
		temp = unitHp(map, player.getOpponent());
		for(int c=0; c<temp.length; c++){
			sweep.add(temp[c]);
		}
		
		return sweep;
	}
	
	
	//Range Clusters-----
	private RangeCluster terrainDefence(Map map){
		RangeCluster cluster = makeCluster(getEmpty(map));
		for(int cy=0; cy<map.getHeight(); cy++){
			for(int cx=0; cx<map.getWidth(); cx++){
				Point loc = new Point(cx, cy);
				cluster.globalSetValue(loc, map.getTerrain(loc).getDefence());
			}
		}
		return cluster;
	}
	
	private RangeCluster inMoveRange(Map map, Player player){
		RangeCluster cluster = makeCluster(getEmpty(map));
		for(LocUnitContainer unit : this.controllers[player.getId()].getUnits()){
			cluster.binaryAdd(map.getMovementRange(unit.getLocation()));
		}
		return cluster;
	}
	
	private RangeCluster[] inCloseCombatRange(Map map, Player player){
		//the 4 cells correspond to the 4 close combat unit types, in order of their ids
		RangeCluster cluster[] = new RangeCluster[]{makeCluster(getEmpty(map)), makeCluster(getEmpty(map)), makeCluster(getEmpty(map)), makeCluster(getEmpty(map))};
		for(LocUnitContainer unit : this.controllers[player.getId()].getUnits()){
			if(!unit.getUnitCont().getType().isRanged()){
				cluster[unit.getUnitCont().getType().getId()].numericAdd(1, map.getCloseCombatRange(unit.getLocation()).makeBinary(), unit.getUnitCont().getHp());
			}
		}
		return cluster;
	}
	
	private RangeCluster inShootingRangeCombatRange(Map map, Player player){
		RangeCluster cluster = makeCluster(getEmpty(map));
		for(LocUnitContainer unit : this.controllers[player.getId()].getUnits()){
			if(unit.getUnitCont().getType().isRanged()){
				cluster.numericAdd(1, map.getShootingRange(unit.getLocation()).makeBinary(), unit.getUnitCont().getHp());
			}
		}
		return cluster;
	}
	
	private RangeCluster[] unitHp(Map map, Player player){
		//the 4 cells correspond to the 4 close combat unit types, in order of their ids
		RangeCluster cluster[] = new RangeCluster[]{makeCluster(getEmpty(map)), makeCluster(getEmpty(map)), makeCluster(getEmpty(map)), makeCluster(getEmpty(map)), makeCluster(getEmpty(map))};
		for(LocUnitContainer unit : this.controllers[player.getId()].getUnits()){
			cluster[unit.getUnitCont().getType().getId()].globalSetValue(unit.getLocation(), unit.getUnitCont().getHp());;
		}
		return cluster;
	}
	
	
	//Utility-----
	/** Returns new empty matrix */
	private int[][] getEmpty(Map map){
		int width = map.getWidth();
		int height = map.getHeight();
		int[][] out = new int[height][width];
		for(int cy=0; cy<out.length; cy++){
			for(int cx=0; cx<out[0].length; cx++){
				out[cy][cx] = RangeCluster.OUT_OF_RANGE;
			}
		}
		return out;
	}
	
	private RangeCluster makeCluster(int[][] matrix){
		return new RangeCluster(matrix, new Point(0, 0), new Point(0, 0));
	}
	
}
