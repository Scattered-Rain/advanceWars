package com.advance.thesis.ai.blueCanary;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.advance.thesis.Main;
import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.mapRenderer.RangeClusterRenderer;
import com.advance.thesis.util.range.RangeCluster;

/** blueCanary at the outlet by the light switch */
public class BlueCanary extends AbstractAI{
	
	/** The Neural core of the Canary */
	@Getter @Setter private Neurals neurals;
	
	/** Constructs New Instance of blueCanary */
	public BlueCanary(MapController controller) {
		super(controller);
		this.neurals = new Neurals();
	}
	
	/** Constructs New Instance of blueCanary */
	public BlueCanary(MapController controller, Neurals neural) {
		super(controller);
		this.neurals = neural;
	}
	
	/** Processes blueCanary */
	@Override protected void doProcess() {
		PreSweeper sweep = new PreSweeper(controller.getMap(), controller.getPlayer());
		List<RangeCluster> clusters = sweep.preSweep();
		neurals.process(controller, clusters);
	}
	
}
