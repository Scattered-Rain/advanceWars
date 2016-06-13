package com.advance.thesis.ai;

import com.advance.thesis.game.logic.MapController;

/** The Generic class that all AIs are extended from */
public abstract class AbstractAI{
	
	/** The map controller through which the AI interacts with the map */
	protected MapController controller;
	
	/** Constructs new AI */
	public AbstractAI(MapController controller){
		this.controller = controller;
	}
	
	/** Method called to initialize the processing of the AI */
	public final void process(){
		controller.refreshUnits();
		doProcess();
	}
	
	/** Encodes the actual processing of the AI */
	protected abstract void doProcess();
	
}
