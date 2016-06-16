package com.advance.thesis.ai.basicAi;

import java.util.Iterator;
import java.util.List;

import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.logic.Map.LocUnitContainer;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.util.Point;
import com.advance.thesis.util.Tuple;

public class AgressiveRandomAI extends AbstractAI{
	
	
	/** Constructs new Agressive Random AI */
	public AgressiveRandomAI(MapController controller){
		super(controller);
	}
	
	
	/** Enacts the actual ai behaviour upon the map */
	@Override protected void doProcess() {
		Object[] units = super.controller.getStillMovableUnits().toArray();
		for(int c=0; c<units.length; c++){
			LocUnitContainer unit = (LocUnitContainer)units[c];
			processUnit(unit);
		}
	}
	
	/** Processes Action for singular unit */
	private void processUnit(LocUnitContainer unit){
		Point loc = unit.getLocation();
		boolean ranged = unit.getUnitCont().getType().isRanged();
		if(ranged){
			List<LocUnitContainer> attackables = controller.getAttackableUnits(loc, controller.getPlayer());
			//Shoot Unit in range
			if(attackables.size()>0){
				LocUnitContainer toAttack = attackables.get(bestRangeAttackableUnit(attackables));
				controller.doCombat(loc, toAttack.getLocation());
			}
			//Move Somewhere
			else{
				controller.move(loc, controller.getMap().getMovementRange(loc).getRandLegalPoint());
			}
		}
		else{
			List<List<Tuple<Point>>> attackables = controller.getAttackableUnitsCloseRange(loc);
			if(attackables.size()>0){
				//Move and attack a unit in range
				List<Tuple<Point>> toAttack = attackables.get(bestCloseAttackableUnit(attackables));
				int dir = GameConstants.RANDOM.nextInt(toAttack.size());
				controller.moveAndDoCombat(loc, toAttack.get(dir).getA(), toAttack.get(dir).getB());
			}
			else{
				//Move somewhere
				controller.move(loc, controller.getMap().getMovementRange(loc).getRandLegalPoint());
			}
		}
	}
	
	/** Returns the index of the 'best' attackable unit */
	private int bestRangeAttackableUnit(List<LocUnitContainer> attackables){
		int bestChoice = GameConstants.RANDOM.nextInt(attackables.size());
		return bestChoice;
	}
	
	/** Returns the index of the 'best' attackable unit */
	private int bestCloseAttackableUnit(List<List<Tuple<Point>>> attackables){
		int bestChoice = GameConstants.RANDOM.nextInt(attackables.size());
		return bestChoice;
	}
	
}
