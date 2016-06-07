package com.advance.thesis.util.range;

import com.advance.thesis.util.Array2dPrinter;
import com.advance.thesis.util.Point;

import lombok.Getter;

/** This Object represents the range of any kind of action (movement, etc.) relative to the map */
public class RangeCluster {
	
	/** The number indicating out of range-ness */
	public static final int OUT_OF_RANGE = -1;
	
	/** The origin point of the Range Cluster on the map (0|0 for the cluster) */
	@Getter private Point<Integer> mapLocation;
	
	/** Denotes the local origin of the range of this Range Cluster */
	@Getter private Point<Integer> origin;
	
	/** The dimensions of the Range bounding box */
	@Getter private int width;
	@Getter private int height;
	
	/** Holds the information about the range expansion */
	private int[][] range;
	
	
	protected RangeCluster(int[][] range, Point<Integer> mapLocation, Point<Integer> origin){
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
	
	/** Returns whether the global coordinates given are in range */
	public boolean inRangeGlobal(int x, int y){
		return inRange(x-mapLocation.getX(), y-mapLocation.getY());
	}
	
	/** Checks whether the given local coordinates are within the bounding box of the Cluster */
	private boolean checkBounds(int x, int y){
		return x>=0 && x<width && y>=0 && y<height;
	}
	
	/** Returns deep clone of this Range Cluster */
	public RangeCluster clone(){
		return new RangeCluster(this);
	}
	
	/** Returns String representing this RangeCluster */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Center of range on Map: "+new Point<Integer>(this.mapLocation.getX()+this.origin.getX(), this.mapLocation.getY()+this.origin.getY()));
		buffer.append("\n");
		buffer.append("Origin of RangeCluster on Map: "+this.mapLocation);
		buffer.append("\n");
		buffer.append("Center of range in RangeCluster: "+this.origin);
		buffer.append("\n");
		buffer.append(Array2dPrinter.printIntArray(range));
		return buffer.toString();
	}
	
}
