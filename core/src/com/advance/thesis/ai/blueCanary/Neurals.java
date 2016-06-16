package com.advance.thesis.ai.blueCanary;

import lombok.Getter;

/** The Brain of blueCanary */
public class Neurals {
	
	/** Amount of Range Clusters in the input */
	private static final int INPUTS = 23;
	
	/** Float array holding the weights accoridng to which this ai will operate */
	@Getter private float[] weights;
	
	/** Means of constructing blueCanary with defined weights */
	public Neurals(float[] weights){
		this.weights = weights;
	}
	
	/** Returns a String of the weights of the brain */
	public String toString(){
		StringBuffer out = new StringBuffer();
		for(int c=0; c<weights.length; c++){
			out.append(weights[c]+", ");
		}
		return out.toString();
	}
	
}
