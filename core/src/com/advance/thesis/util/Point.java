package com.advance.thesis.util;

import com.advance.thesis.game.GameConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Represents a 2 dimensional point in code (immutable) */
@AllArgsConstructor
public class Point{
	
	/** The X value of this Point */
	@Getter public int x;
	/** The Y value of this Point */
	@Getter public int y;
	
	
	/** Creates Point randomly within the bounding box described by the gien parameters */
	public Point(int x, int y, int width, int height){
		this.x = x+GameConstants.RANDOM.nextInt(width);
		this.y = y+GameConstants.RANDOM.nextInt(height);
	}
	
	
	/** Returns the sum of this Point and the given */
	public Point add(Point other){
		return new Point(this.x+other.x, this.y+other.y);
	}
	
	/** Returns the sum of this Point and the given */
	public Point add(int x, int y){
		return new Point(this.x+x, this.y+y);
	}
	
	/** Returns the difference of this Point and the given */
	public Point substract(Point other){
		return new Point(this.x-other.x, this.y-other.y);
	}
	
	/** Returns the difference of this Point and the given */
	public Point substract(int x, int y){
		return new Point(this.x-x, this.y-y);
	}
	
	/** Returns the product of this Point and the given */
	public Point multiply(int x, int y){
		return new Point(this.x*x, this.y*y);
	}
	
	/** Returns whether the given Point is identical to this Point */
	public boolean isIdentical(Point other){
		return this.x==other.x && this.y==other.y;
	}
	
	
	/** Returns a copy of this Point (with x and y being shallow copies) */
	public Point clone(){
		return new Point(x, y);
	}
	
	
	/** Returns this Point as String */
	public String toString(){
		return x+"|"+y;
	}
	
}
