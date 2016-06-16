package com.advance.thesis.ai.blueCanary;

import com.advance.thesis.game.GameConstants;

public class NeuralBreeder {
	
	private static final float MUTATION_RATE = 0.01f;
	
	public static Neurals breed(Neurals mother, Neurals father){
		Neurals[] parents = new Neurals[]{mother, father};
		float[] move = new float[mother.getMove().length];
		float[] shoot = new float[mother.getShoot().length];
		float[] attack = new float[mother.getAttack().length];
		for(int c=0; c<move.length; c++){
			move[c] = mutate(parents[getRand()].getMove()[c]);
		}
		for(int c=0; c<shoot.length; c++){
			shoot[c] = mutate(parents[getRand()].getShoot()[c]);
		}
		for(int c=0; c<attack.length; c++){
			attack[c] = mutate(parents[getRand()].getAttack()[c]);
		}
		return null;
	}
	
	/** Returns either 0 or 1 */
	private static int getRand(){
		return GameConstants.RANDOM.nextInt(2);
	}
	
	private static float mutate(float value){
		if(GameConstants.RANDOM.nextDouble()<MUTATION_RATE){
			value += (GameConstants.RANDOM.nextDouble()*2)-1;
			value = Math.max(value, 1);
			value = Math.min(value, -1);
		}
		return value;
	}
	
	
}
