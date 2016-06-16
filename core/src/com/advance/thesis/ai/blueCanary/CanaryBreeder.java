package com.advance.thesis.ai.blueCanary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import com.advance.thesis.ai.AbstractAI;
import com.advance.thesis.ai.basicAi.AgressiveRandomAI;
import com.advance.thesis.game.GameConstants;
import com.advance.thesis.game.enums.MapTiled;
import com.advance.thesis.game.enums.Player;
import com.advance.thesis.game.logic.Map;
import com.advance.thesis.game.logic.MapController;
import com.advance.thesis.game.logic.TiledMapFactory;
import com.advance.thesis.util.Point;

public class CanaryBreeder {
	
	/** Turns until time out is declared */
	private static final int TIME_OUT = 100;
	
	/** How often a match is to be played */
	private static final int REPETITIONS = 5;
	
	/** Size of the population */
	private static final int POPULATION_SIZE = 16;
	/** Size of the Elite */
	private static final int ELITE = 4;
	/** Index of where random canaries are added into the population */
	private static final int SPAWN = 16-2;
	
	/** The AI against which each AI is tested */
	private static final AbstractAI CHALLENGER = new  AgressiveRandomAI(null);
	
	/** Map that can be used to gain a glance at the breeding */
	@Getter private Map window;
	
	/** The map that all games are executed on */
	private Map trainingGrounds;
	
	/** The current Generation */
	private int currentGen;
	/** The next available id */
	private int nextFreeId;
	
	/** Holds the scores of how often the canaries vs challenger have won [canary, challenger, draw] */
	private int[] cScore;
	
	/** The entire population of Canaries */
	private BirdHouse[] population;
	
	/** Constructs new Canary Breeder */
	public CanaryBreeder(Map trainingMap){
		this.window = new Map(1, 1);
		this.cScore = new int[]{0, 0, 0};
		this.nextFreeId = 0;
		this.currentGen = 0;
		this.trainingGrounds = trainingMap;
		this.population = new BirdHouse[POPULATION_SIZE];
		spawnFrom(0);
	}
	
	/** Processes the generational loop */
	public void process(){
		BirdHouse topChicken = population[0];
		float topScore = Float.NEGATIVE_INFINITY;
		while(true){
			genStep();
			System.out.println("Gen: "+currentGen+" | Canary: "+cScore[0]+" | Challenger: "+cScore[1]+" | Draw: "+cScore[2]+" | Ratio:  "+((float)cScore[0])/((float)cScore[1])+" | Top Elite: "+population[0].getScore()+" | Top Score: "+topScore);
			if(topScore<population[0].getScore()){
				topChicken = population[0];
				topScore = topChicken.getScore();
				System.out.println();
				System.out.println("New Top Chicken!");
				System.out.println(topChicken.getCanary().getNeurals());
				System.out.println();
			}
		}
	}
	
	/** Processes an entire generation */
	public void genStep(){
		//Let's Game!
		for(int c=0; c<population.length; c++){
			BirdHouse bird = population[c];
			if(!bird.hasScore()){
				float score = 0;
				for(int c2=0; c2<REPETITIONS; c2++){
					score += scoreCanary(bird, testChallenger(bird));
				}
				score = score/REPETITIONS;
				bird.setScore(score);
			}
		}
		//let's Breed!
		sortPopulation();
		breedFrom(ELITE);
		spawnFrom(SPAWN);
		currentGen++;
	}
	
	/** Evaluates the performance of the AI in the given map and sets score accordingly */
	private float scoreCanary(BirdHouse bird, Map map){
		int score = 0;
		if(map.calcWinner().equals(Player.P0)){
			score = 1000;
		}
		else if(map.calcWinner().equals(Player.P1)){
			score = 500;
		}
		int ownUnits = 0;
		for(Point unit : map.getAllUnitsOfPlayer(Player.P0)){
			ownUnits += map.getUnitContainer(unit).getHp();
		}
		int foeUnits = 0;
		for(Point unit : map.getAllUnitsOfPlayer(Player.P1)){
			foeUnits += map.getUnitContainer(unit).getHp();
		}
		score += ownUnits - foeUnits;
		return score;
	}
	
	/** Tests the given individual against the challenger AI */
	private Map testChallenger(BirdHouse bird){
		Map map = game(bird.getCanary(), CHALLENGER);
		Player winner = map.calcWinner();
		if(winner.equals(Player.NONE)){
			this.cScore[2]++;
		}
		else{
			this.cScore[winner.getId()]++;
		}
		return map;
	}
	
	/** Executes a single game between two AIs */
	private Map game(AbstractAI p0, AbstractAI p1){
		Map map = trainingGrounds.clone();
		this.window = map;
		AbstractAI[] ais = new AbstractAI[]{p0, p1};
		for(int c=0; c<ais.length; c++){
			ais[c].setController(new MapController(map, Player.getPlayer(c)));
		}
		for(int turns=0; turns<TIME_OUT; turns++){
			for(int c=0; c<ais.length; c++){
				ais[c].process();
				if(!map.calcWinner().equals(Player.NONE)){
					return map;
				}
			}
		}
		return map;
	}
	
	/** Fills the population from the given index on with children of the upper part */
	private void breedFrom(int index){
		for(int c=index; c<population.length; c++){
			int mother = GameConstants.RANDOM.nextInt(index);
			int father = GameConstants.RANDOM.nextInt(index);
			Neurals neural = NeuralBreeder.breed(population[mother].getCanary().getNeurals(), population[father].getCanary().getNeurals());
			BirdHouse bird = new BirdHouse(new BlueCanary(null, neural), Float.NEGATIVE_INFINITY, currentGen, nextFreeId++);
			population[c] = bird;
		}
	}
	
	/** Adds completely new canaries to the population, starting at index */
	private void spawnFrom(int index){
		for(int c=index; c<population.length; c++){
			Neurals neural = new Neurals();
			BirdHouse bird = new BirdHouse(new BlueCanary(null, neural), Float.NEGATIVE_INFINITY, currentGen, nextFreeId++);
			population[c] = bird;
		}
	}
	
	/** Sorts Population according to score */
	private void sortPopulation(){
		for(int c=0; c<population.length; c++){
			for(int c2=c+1; c2<population.length; c2++){
				if(population[c].getScore()<population[c2].getScore()){
					BirdHouse temp = population[c];
					population[c] = population[c2];
					population[c2] = temp;
				}
			}
		}
	}
	
	/** Inner class holding an individual */
	@AllArgsConstructor @Data private class BirdHouse{
		private BlueCanary canary;
		private float score;
		private int birthGen;
		private int id;
		/** Returns wether this bird has already scored */
		public boolean hasScore(){
			return score>-1000;
		}
	}
	
}
