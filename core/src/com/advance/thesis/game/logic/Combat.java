package com.advance.thesis.game.logic;

import com.advance.thesis.game.enums.Terrain;
import com.advance.thesis.game.enums.Unit;
import com.advance.thesis.util.Tuple;

/** This class is used to both calculate and represent the outcome of a combat event */
public class Combat {
	
	/** Holds the units engaging in combat, a being the aggressor and b the defender */
	private Tuple<Unit> units;
	/** Holds the terrains that the units engaging in combat are located on, a being the aggressor and b the defender */
	private Tuple<Terrain> terrain;
	/** Holds the hp of the units engaging in combat, a being the aggressor and b the defender */
	private Tuple<Integer> hp;
	
	/** Damage that was done to the given units by this encounter */
	private Tuple<Integer> appliedDamage;
	
	
	/** Constructs new Combat Objects */
	public Combat(Unit attackingUnit, Unit defendingUnit, int attackingHp, int defendingHp, Terrain attackingTerrain, Terrain defendingTerrain){
		this.units = new Tuple<Unit>(attackingUnit, defendingUnit);
		this.terrain = new Tuple<Terrain>(attackingTerrain, defendingTerrain);
		this.hp = new Tuple<Integer>(attackingHp, defendingHp);
		calcResult();
	}
	
	/** Calculates outcome of combat */
	private void calcResult(){
		int firstStrikeDamage = calcStrike(units, terrain, hp);
		hp.setB(Math.max(hp.getB()-firstStrikeDamage, 0));
		int retDamage = 0;
		if(hp.getB()>0){
			retDamage = calcStrike(units.reverse(), terrain.reverse(), hp.reverse());
			hp.setA(Math.max(hp.getA()-retDamage, 0));
		}
		this.appliedDamage = new Tuple<Integer>(retDamage, firstStrikeDamage);
	}
	
	/** Calculates the damage of a singular strike (in any Tuple a is the relative attacker with b being the relative defender)*/
	private int calcStrike(Tuple<Unit> units, Tuple<Terrain> terrains, Tuple<Integer> hps){
		int baseDamage = units.getA().getBaseDamage(units.getB());
		float unitStrength = hps.getA()/10f;
		float defence = 1f-((hps.getB()*terrains.getB().getDefence())/100f);
		return (int)((baseDamage*unitStrength*defence)/10f);
	}
	
	public boolean attackerAlive(){
		return hp.getA()>0;
	}
	
	public boolean defenderAlive(){
		return hp.getB()>0;
	}
	
	public int getAttackerHp(){
		return hp.getA();
	}
	
	public int getDefenderHp(){
		return hp.getB();
	}
	
	/** Returns the amount of damage afflicted to the attacker in this Combat Event */
	public int getAttackerDamage(){
		return appliedDamage.getA();
	}
	
	/** Returns the amount of damage afflicted to the defender in this Combat Event */
	public int getDefenderDamage(){
		return appliedDamage.getB();
	}
	
	/** Returns String Representation of this Encounter */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("(Attacker) "+units.getA().getName()+" vs (Defender) "+units.getB().getName());
		buffer.append("\n");
		buffer.append("Terrain: "+terrain.getA().getName()+" vs "+terrain.getB().getName());
		buffer.append("\n");
		buffer.append("Final HP: "+hp.getA()+" vs HP: "+hp.getB());
		buffer.append("\n");
		buffer.append("(Received Damage: "+appliedDamage.getA()+" vs "+appliedDamage.getB()+")");
		return buffer.toString();
	}
	
}
