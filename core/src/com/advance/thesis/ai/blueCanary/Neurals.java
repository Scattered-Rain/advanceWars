package com.advance.thesis.ai.blueCanary;

import java.util.List;

import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.game.logic.Map.LocUnitContainer;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.Tuple;
import com.advance.thesis.util.range.RangeCluster;

import lombok.Getter;

/** The Brain of blueCanary */
public class Neurals {
	
	/** Amount of Range Clusters in the input */
	private static final int INPUTS = 23;
	/** The amount of Neurons in the hidden layer */
	private static final int HIDDEN = 40;
	
	/** The range teh weights can possibly take */
	public static final float[] WEIGHT_RANGE = new float[]{-1, 1};
	
	/** Float array holding the weights accoridng to which this ai will operate */
	@Getter private float[] move;
	@Getter private float[] attack;
	@Getter private float[] shoot;
	
	/** Means of constructing blueCanary with defined weights */
	public Neurals(float[] move, float[] attack, float[] shoot){
		this.move = move;
		this.attack = attack;
		this.shoot = shoot;
	}
	
	/** Constructs new random ANN */
	public Neurals(){
		this.move = new float[INPUTS*2*HIDDEN+HIDDEN];
		this.attack = new float[INPUTS*3*HIDDEN+HIDDEN];
		this.shoot = new float[INPUTS*2*HIDDEN+HIDDEN];
		float[][] all = new float[][]{move, shoot, attack};
		for(int c=0; c<all.length; c++){
			for(int c2=0; c2<all[c].length; c2++){
				all[c][c2] = (float)(GameConstants.RANDOM.nextDouble()*2-1);
			}
		}
	}
	
	
	/** Asks blueCanary to enact actions for all units */
	public void process(MapController controller, List<RangeCluster> sweep){
		float temp = Float.NEGATIVE_INFINITY;
		for(Map.LocUnitContainer unit : controller.getUnits()){
			//Init possible actions and scores thereof
			Point toMove = null;
			float bestMoveScore = Float.NEGATIVE_INFINITY;
			Point toShoot = null;
			float bestShootScore = Float.NEGATIVE_INFINITY;
			Tuple<Point> toAttack = null; //With a being the moveTo and be the opponent unit
			float bestAttackScore = Float.NEGATIVE_INFINITY;
			//Process and evaluate all actions
			//Moving
			temp = Float.NEGATIVE_INFINITY;
			for(Point loc : controller.getMap().getMovementRange(unit.getLocation()).getAllPointsInRange(controller.getMap())){
				temp = calcMove(unit.getLocation(), sweep, loc);
				if(temp>bestMoveScore){
					toMove = loc;
					bestMoveScore = temp;
				}
			}
			//Shooting
			if(unit.getUnitCont().getType().isRanged()){
				temp = Float.NEGATIVE_INFINITY;
				for(LocUnitContainer loc : controller.getAttackableUnits(unit.getLocation(), controller.getPlayer())){
					temp = calcShoot(unit.getLocation(), sweep, loc.getLocation());
					if(temp>bestShootScore){
						toShoot = loc.getLocation();
						bestShootScore = temp;
					}
				}
			}
			//Attacking
			else if(!unit.getUnitCont().getType().isRanged()){
				temp = Float.NEGATIVE_INFINITY;
				for(List<Tuple<Point>> loc : controller.getAttackableUnitsCloseRange(unit.getLocation())){
					for(int c=0; c<loc.size(); c++){
						temp = calcAttack(unit.getLocation(), sweep, loc.get(c).getA(), loc.get(c).getB());
						if(temp>bestAttackScore){
							toAttack = loc.get(c);
							bestAttackScore = temp;
						}
					}
				}
			}
			//Check for the best action and enact it
			if(bestMoveScore>=bestShootScore && bestMoveScore>=bestAttackScore){
				controller.move(unit.getLocation(), toMove);
			}
			else if(unit.getUnitCont().getType().isRanged()){
				controller.doCombat(unit.getLocation(), toShoot);
			}
			else{
				controller.moveAndDoCombat(unit.getLocation(), toAttack.getA(), toAttack.getB());
			}
		}
	}
	
	private float calcMove(Point unit, List<RangeCluster> sweep, Point target){
		float out = 0;
		int weightCounter = 0;
		float[] hidden = new float[HIDDEN];
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(unit) * move[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(target) * move[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<HIDDEN; c++){
			out += hidden[c] * move[weightCounter];
		}
		return out/weightCounter;
	}
	
	private float calcShoot(Point unit, List<RangeCluster> sweep, Point target){
		float out = 0;
		int weightCounter = 0;
		float[] hidden = new float[HIDDEN];
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(unit) * shoot[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(target) * shoot[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<HIDDEN; c++){
			out += hidden[c] * shoot[weightCounter];
		}
		return out/weightCounter;
	}
	
	private float calcAttack(Point unit, List<RangeCluster> sweep, Point target, Point defender){
		float out = 0;
		int weightCounter = 0;
		float[] hidden = new float[HIDDEN];
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(unit) * attack[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(target) * attack[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<INPUTS; c++){
			for(int c2=0; c2<HIDDEN; c2++){
				hidden[c2] += sweep.get(c).getValueAtGlobal(defender) * attack[weightCounter];
				weightCounter++;
			}
		}
		for(int c=0; c<HIDDEN; c++){
			out += hidden[c] * attack[weightCounter];
		}
		return out/weightCounter;
	}
	
	
	/** Returns a String of the weights of the brain */
	public String toString(){
		StringBuffer out = new StringBuffer();
		//Move
		out.append("{");
		for(int c=0; c<move.length; c++){
			out.append(move[c]+", ");
		}
		out.append("}");
		//Attack
		out.append("\n");
		out.append("{");
		for(int c=0; c<attack.length; c++){
			out.append(attack[c]+", ");
		}
		out.append("}");
		//Shoot
		out.append("\n");
		out.append("{");
		for(int c=0; c<shoot.length; c++){
			out.append(shoot[c]+", ");
		}
		out.append("}");
		return out.toString();
	}
	
}
