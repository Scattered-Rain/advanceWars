package com.advance.thesis.util;

public class Array2dPrinter {
	
	/** Prints given 2d Array to console */
	public static void printIntArray(int[][] array){
		for(int cy=0; cy<array.length; cy++){
			for(int cx=0; cx<array[0].length; cx++){
				System.out.print(array[cy][cx]+"\t");
			}
			System.out.println("");
		}
		System.out.println();
	}
	
}
