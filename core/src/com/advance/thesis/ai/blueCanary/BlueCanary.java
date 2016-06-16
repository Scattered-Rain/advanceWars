package com.advance.thesis.ai.blueCanary;

import java.util.List;

import com.advance.thesis.Main;
import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.mapRenderer.RangeClusterRenderer;
import com.advance.thesis.util.range.RangeCluster;

/** blueCanary at the outlet by the light switch */
public class BlueCanary extends AbstractAI{
	
	/** Constructs New Instance of blueCanary */
	public BlueCanary(MapController controller) {
		super(controller);
	}
	
	/** Processes blueCanary */
	@Override protected void doProcess() {
		PreSweeper sweep = new PreSweeper(controller.getMap(), controller.getPlayer());
		List<RangeCluster> clusters = sweep.preSweep();
		//Main.setRenderer(new RangeClusterRenderer(map, sweep.get(0)));
	}
	
}
