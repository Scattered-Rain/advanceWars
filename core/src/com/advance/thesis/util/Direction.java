package com.advance.thesis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Direction {
	UP(0),
	RIGHT(1),
	DOWN(2),
	LEFT(3);
	
	/** Defines the movement that each direction evokes on the X and Y plane {x, y} */
	private static final int[][] DIR = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
	
	/** Index of this direction */
	@Getter private int index;
	
	/** Horizontal movement of this direction (X Plane) */
	public int getX(){
		return DIR[index][0];
	}
	
	/** Vertical movement of this direction (Y Plane) */
	public int getY(){
		return DIR[index][1];
	}
}
