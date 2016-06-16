package com.advance.thesis.util.range;

import java.util.ArrayList;
import java.util.List;

import com.advance.thesis.game.logic.Map;
import com.advance.thesis.util.Array2dPrinter;
import com.advance.thesis.util.Point;

import lombok.Getter;

/** This Object represents the range of any kind of action (movement, etc.) relative to the map */
public class RangeCluster {
	
	/** The number indicating out of range-ness */
	public static final int OUT_OF_RANGE = -1;
	public static final int BINARY_IN_RANGE = 1;
	
	/** The origin point of the Range Cluster on the map (0|0 for the cluster) */
	@Getter private Point mapLocation;
	
	/** Denotes the local origin of the range of this Range Cluster */
	@Getter private Point origin;
	
	/** The dimensions of the Range bounding box */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the information about the range expansion */
	private int[][] range;
	
	
	public RangeCluster(int[][] range, Point mapLocation, Point origin){
		this.mapLocation = mapLocation;
		this.origin = origin;
		this.range = range;
		this.height = range.length;
		this.width = range[0].length;
	}
	
	
	/** Clone Constructor */
	private RangeCluster(RangeCluster toClone){
		this.mapLocation = toClone.mapLocation;
		this.origin = toClone.origin;
		this.width = toClone.width;
		this.height = toClone.height;
		this.range = new int[height][width];
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				this.range[cy][cx] = toClone.range[cy][cx];
			}
		}
	}
	
	
	/** Returns whether the local coordinates given are in range */
	public boolean inRangeLocal(int x, int y){
		if(checkBounds(x, y)){
			return range[y][x] != OUT_OF_RANGE;
		}
		return false;
	}
	
	/** Returns whether the local coordinates given are in range */
	public boolean inRangeLocal(Point loc){
		return inRangeLocal(loc.x, loc.y);
	}
	
	/** Returns whether the global coordinates given are in range */
	public boolean inRangeGlobal(int x, int y){
		return inRangeLocal(x-mapLocation.getX(), y-mapLocation.getY());
	}
	
	/** Returns whether the global coordinates given are in range */
	public boolean inRangeGlobal(Point loc){
		return inRangeGlobal(loc.x, loc.y);
	}
	
	/** Returns local point given global point */
	public Point globalToLocal(Point loc){
		return loc.substract(this.mapLocation);
	}
	
	/** Returns global point given local point */
	public Point localToGlobal(Point loc){
		return loc.add(this.mapLocation);
	}
	
	/** Returns the global position of origin point on map */
	public Point getGlobalOrigin(){
		return localToGlobal(origin);
	}
	
	/** Checks whether the given local coordinates are within the bounding box of the Cluster */
	public boolean checkBounds(int x, int y){
		return x>=0 && x<width && y>=0 && y<height;
	}
	
	/** Returns random global point which is legal within the range this cluster describes - note: leads to infinite loop if none are legal */
	public Point getRandLegalPoint(){
		Point p = new Point(mapLocation.x, mapLocation.y, width, height);
		while(!inRangeGlobal(p)){
			p = new Point(mapLocation.x, mapLocation.y, width, height);
		}
		return p;
	}
	
	/** Returns the value at the given local point, OUT_OF_RANGE if not legal */
	public int getValueAtLocal(Point loc){
		if(!this.inRangeLocal(loc)){
			return OUT_OF_RANGE;
		}
		return this.range[loc.y][loc.x];
	}
	
	/** Returns the value at the given local point, OUT_OF_RANGE if not legal */
	public int getValueAtGlobal(Point loc){
		return getValueAtLocal(this.globalToLocal(loc));
	}
	
	/** Returns list of all Units on the map that are within this cluster (not including the origin) */
	public List<Map.LocUnitContainer>getAllUnitsInRange(Map map){
		List<Map.LocUnitContainer> listicle = new ArrayList<Map.LocUnitContainer>();
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				Point loc = new Point(cx, cy);
				if(this.inRangeLocal(loc)){
					if(!loc.equals(this.origin)){
						loc = this.localToGlobal(loc);
						Map.UnitContainer cont = map.getUnitContainer(loc);
						if(cont.getType().isUnit()){
							listicle.add(new Map.LocUnitContainer(cont, loc));
						}
					}
				}
			}
		}
		return listicle;
	}
	
	/** Sets value at global location to the given value */
	public void localSetValue(Point loc, int value){
		this.range[loc.getY()][loc.getX()] = value;
	}
	
	/** Sets given value at given Point */
	public void globalSetValue(Point loc, int value){
		localSetValue(globalToLocal(loc), value);
	}
	
	//RangeCluster math operations:
	/** Sets any value in range to 1 */
	public RangeCluster makeBinary(){
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				Point loc = this.localToGlobal(new Point(cx, cy));
				if(this.inRangeGlobal(loc)){
					this.range[cy][cx] = RangeCluster.BINARY_IN_RANGE;
				}
			}
		}
		return this;
	}
	
	/** Adds the values of the given cluster to this one, in binary fashion, so that any field that is in either becomes 1 (does not exceed this' borders) */
	public RangeCluster binaryAdd(RangeCluster cluster){
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				Point loc = this.localToGlobal(new Point(cx, cy));
				if(this.inRangeGlobal(loc) || cluster.inRangeGlobal(loc)){
					this.range[cy][cx] = RangeCluster.BINARY_IN_RANGE;
				}
			}
		}
		return this;
	}
	
	public RangeCluster binarySubstract(RangeCluster cluster){
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				Point loc = this.localToGlobal(new Point(cx, cy));
				if(this.inRangeGlobal(loc) && !cluster.inRangeGlobal(loc)){
					this.range[cy][cx] = RangeCluster.BINARY_IN_RANGE;
				}
				else{
					this.range[cy][cx] = RangeCluster.OUT_OF_RANGE;
				}
			}
		}
		return this;
	}
	
	public RangeCluster numericAdd(int scaleThis, RangeCluster cluster, int scaleCluster){
		for(int cy=0; cy<height; cy++){
			for(int cx=0; cx<width; cx++){
				Point loc = this.localToGlobal(new Point(cx, cy));
				if(this.inRangeGlobal(loc) && cluster.inRangeGlobal(loc)){
					this.range[cy][cx] = scaleThis*this.getValueAtGlobal(loc)+scaleCluster*cluster.getValueAtGlobal(loc);
				}
				else if(!this.inRangeGlobal(loc) && cluster.inRangeGlobal(loc)){
					this.range[cy][cx] = scaleCluster*cluster.getValueAtGlobal(loc);
				}
				else if(this.inRangeGlobal(loc) && !cluster.inRangeGlobal(loc)){
					this.range[cy][cx] = scaleThis*this.getValueAtGlobal(loc);
				}
				else{
					this.range[cy][cx] = RangeCluster.OUT_OF_RANGE;
				}
				if(this.range[cy][cx]==0){
					System.out.println("WTF?");
				}
			}
		}
		return this;
	}
	
	/** Returns deep clone of this Range Cluster */
	public RangeCluster clone(){
		return new RangeCluster(this);
	}
	
	/** Returns String representing this RangeCluster */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Global origin of range: "+new Point(this.mapLocation.getX()+this.origin.getX(), this.mapLocation.getY()+this.origin.getY()));
		buffer.append("\n");
		buffer.append("Local origin of range: "+this.origin);
		buffer.append("\n");
		buffer.append("Global location of RangeCluster upper left corner point: "+this.mapLocation);
		buffer.append("\n");
		buffer.append(Array2dPrinter.printIntArray(range));
		return buffer.toString();
	}
	
}
