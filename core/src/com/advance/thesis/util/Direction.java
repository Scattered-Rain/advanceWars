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
	private static final Point[] DIR = new Point[]{new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0)};
	
	/** Index of this direction */
	@Getter private int index;
	
	/** Horizontal movement of this direction (X Plane) */
	public int getX(){
		return DIR[index].getX();
	}
	
	/** Vertical movement of this direction (Y Plane) */
	public int getY(){
		return DIR[index].getY();
	}
	
	/** Returns a Point containing vertical and horizontal movement corresponding to this direction */
	public Point getDir(){
		return DIR[index];
	}
	
	/** Returns the direction 90 degrees clockwise from this direction */
	public Direction turnClockwise(){
		return Direction.values()[index+1%4];
	}
	
	/** Returns the direction 90 degrees counterclockwise from this direction */
	public Direction turnCounterClockwise(){
		return this.turnClockwise().turnClockwise().turnClockwise();
	}
	
	/** Returns the direction opposite from this direction */
	public Direction turn180(){
		return this.turnClockwise().turnClockwise();
	}
	
}
