package com.advance.thesis.util;

public class Array2dPrinter {
	
	/** Prints given 2d Array to console */
	public static String printIntArray(int[][] array){
		StringBuffer buffer = new StringBuffer();
		for(int cy=0; cy<array.length; cy++){
			for(int cx=0; cx<array[0].length; cx++){
				buffer.append(array[cy][cx]+"\t");
			}
			if(cy!=array.length-1){
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
	
}
