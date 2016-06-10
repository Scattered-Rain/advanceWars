package com.advance.thesis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Class storing two objects of a generic kind */
@AllArgsConstructor
public class Tuple<Generic>{
	
	/** The value A */
	@Setter @Getter Generic a;
	/** The value B */
	@Setter @Getter Generic b;
	
	/** Returns clone of this Tuple with A and B swapped */
	public Tuple<Generic> reverse(){
		return new Tuple<Generic>(b, a);
	}
	
	/** Returns clone of this Tuple */
	public Tuple<Generic> clone(){
		return new Tuple<Generic>(a, b);
	}
	
}
