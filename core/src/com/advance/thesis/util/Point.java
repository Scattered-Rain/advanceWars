package com.advance.thesis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Reperesents a 2 dimensional point in code */
@AllArgsConstructor
public class Point<Generic>{
	
	@Getter @Setter private Generic x;
	@Getter @Setter private Generic y;
	
	/** Returns dee copy of this Point (with x and y being shallow copies) */
	public Point<Generic> clone(){
		return new Point<Generic>(x, y);
	}
	
}
