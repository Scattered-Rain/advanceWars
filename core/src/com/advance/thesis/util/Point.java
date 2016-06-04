package com.advance.thesis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Represents a 2 dimensional point in code */
@AllArgsConstructor
public class Point<Generic>{
	
	@Getter private Generic x;
	@Getter private Generic y;
	
	/** Returns a copy of this Point (with x and y being shallow copies) */
	public Point<Generic> clone(){
		return new Point<Generic>(x, y);
	}
	
}
