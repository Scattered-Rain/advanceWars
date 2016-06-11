package com.advance.thesis.util.range;

import com.advance.thesis.util.Array2dPrinter;
import com.advance.thesis.util.Point;

import lombok.Getter;

/** This Object represents the range of any kind of action (movement, etc.) relative to the map */
public class RangeCluster {
	
	/** The number indicating out of range-ness */
	public static final int OUT_OF_RANGE = -1;
	
	/** The origin point of the Range Cluster on the map (0|0 for the cluster) */
	@Getter private Point mapLocation;
	
	/** Denotes the local origin of the range of this Range Cluster */
	@Getter private Point origin;
	
	/** The dimensions of the Range bounding box */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the information about the range expansion */
	private int[][] range;
	
	
	protected RangeCluster(int[][] range, Point mapLocation, Point origin){
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
	public boolean inRange(int x, int y){
		if(checkBounds(x, y)){
			return range[y][x] != OUT_OF_RANGE;
		}
		return false;
	}
	
	/** Returns whether the local coordinates given are in range */
	public boolean inRange(Point loc){
		return inRange(loc.x, loc.y);
	}
	
	/** Returns whether the global coordinates given are in range */
	public boolean inRangeGlobal(int x, int y){
		return inRange(x-mapLocation.getX(), y-mapLocation.getY());
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
		if(!this.inRange(loc)){
			return OUT_OF_RANGE;
		}
		return this.range[loc.y][loc.x];
	}
	
	/** Returns the value at the given local point, OUT_OF_RANGE if not legal */
	public int getValueAtGlobal(Point loc){
		return getValueAtLocal(this.globalToLocal(loc));
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
		buffer.append("Global location of RangeCluster root point: "+this.mapLocation);
		buffer.append("\n");
		buffer.append(Array2dPrinter.printIntArray(range));
		return buffer.toString();
	}
	
}
