package com.advance.thesis.ai.blueCanary;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
	private static final int TIME_OUT = 25;
	
	/** How often a match is to be played */
	private static final int REPETITIONS = 5;
	
	/** Size of the population */
	private static final int POPULATION_SIZE = 25;
	/** Size of the Elite */
	private static final int ELITE = 8;
	/** Index of where random canaries are added into the population */
	private static final int SPAWN = POPULATION_SIZE-4;
	
	/** The AI against which each AI is tested */
	private static AbstractAI CHALLENGER = new  AgressiveRandomAI(null);
	
	/** Map that can be used to gain a glance at the breeding */
	@Getter private Map window;
	
	/** Contains an entire game that was played */
	@Getter private List<Map> game;
	
	/** Tracker for syso purposes */
	private Tracker tracker;
	
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
		this.game = new ArrayList<Map>();
		this.game.add(window);
		this.cScore = new int[]{0, 0, 0};
		this.nextFreeId = 0;
		this.currentGen = 0;
		this.trainingGrounds = trainingMap;
		this.population = new BirdHouse[POPULATION_SIZE];
		spawnFrom(0, false);
		this.tracker = new Tracker(population[0], Float.NEGATIVE_INFINITY);
	}
	
	/** Processes the generational loop */
	public void process(){
		while(true){
			gameStep();
			track();
			genStep();
//			if(currentGen<40){
//				this.CHALLENGER = population[0].getCanary();
//			}
//			else if(currentGen==40){
//				System.out.println("\n\nCanary Peace Declared\n\n");
//				this.CHALLENGER = new  AgressiveRandomAI(null);
//			}
		}
	}
	
	/** Does nice printouts */
	private void track(){
		System.out.println("Gen: "+currentGen+" | Canary: "+cScore[0]+" | Foes: "+cScore[1]+" | Draw: "+cScore[2]+" | Ratio:  "+((float)cScore[0])/((float)cScore[1])+" | GenWins: "+calcGenWins()+"/"+(POPULATION_SIZE-ELITE)*REPETITIONS+" | BestTime: "+getBestTime()+" | TopGen: "+bestCurrentGenner().getScore()+" | TopPop: "+population[0].getScore()+" | TopScore: "+tracker.getTopScore());
		if(tracker.getTopScore()<population[0].getScore()){
			tracker.setTopChicken(population[0]);
			tracker.setTopScore(tracker.getTopChicken().getScore());
			System.out.println();
			System.out.println("New Top Chicken!");
			System.out.println(tracker.getTopChicken().getCanary().getNeurals());
			System.out.println();
		}
	}
	
	/** Returns the win/loss ratio of the current generation */
	private int calcGenWins(){
		int played = 0;
		int wins = 0;
		for(int c=0; c<population.length; c++){
			if(population[c].hasPlayed){
				wins += population[c].hasWon;
				played++;
			}
		}
		return wins;
	}
	
	/** Returns best amount of turns needed to complete in generation */
	private float getBestTime(){
		float best = Float.POSITIVE_INFINITY;
		for(int c=0; c<population.length; c++){
			if(population[c].hasPlayed){
				if(population[c].getAvgTime()<best){
					best = population[c].getAvgTime();
				}
			}
		}
		return best;
	}
	
	/** evaluates an entire generation */
	private void gameStep(){
		final float SCORE_DEVALUING = 0.05f;
		for(int c=0; c<population.length; c++){
			BirdHouse bird = population[c];
			bird.setHasPlayed(false);
			if(!bird.hasScore()){
				float score = 0;
				float turns = 0;
				for(int c2=0; c2<REPETITIONS; c2++){
					Map test = testChallenger(bird);
					score += scoreCanary(bird, test);
					turns += test.getTurn();
				}
				score = score/REPETITIONS;
				turns = turns/REPETITIONS;
				bird.setScore(score);
				bird.setAvgTime(turns);
			}
			else{
				bird.setScore(bird.getScore()-bird.getScore()*SCORE_DEVALUING);
			}
		}
		currentGen++;
		sortPopulation();
	}
	
	/** Processes an entire generation that has been evaluated */
	private void genStep(){
		breedFrom(ELITE);
		spawnFrom(SPAWN, true);
	}
	
	/** Evaluates the performance of the AI in the given map and sets score accordingly */
	private float scoreCanary(BirdHouse bird, Map map){
		int score = 0;
		if(map.calcWinner().equals(Player.P0)){
			score = 5000;
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
		score -= foeUnits;
		//score += ownUnits;
		score -= map.getTurn();
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
			if(winner.getId()==0){
				bird.setHasWon(bird.getHasWon()+1);
				this.cScore[0]++;
			}
			else if(winner.getId()==1){
				this.cScore[1]++;
			}
		}
		bird.setHasPlayed(true);
		return map;
	}
	
	/** Executes a single game between two AIs */
	private Map game(AbstractAI p0, AbstractAI p1){
		List<Map> game = new ArrayList<Map>();
		Map map = trainingGrounds.clone();
		this.window = map;
		AbstractAI[] ais = new AbstractAI[]{p0, p1};
		for(int c=0; c<ais.length; c++){
			ais[c].setController(new MapController(map, Player.getPlayer(c)));
		}
		for(int turns=0; turns<TIME_OUT; turns++){
			for(int c=0; c<ais.length; c++){
				game.add(map.clone());
				if(!map.calcWinner().equals(Player.NONE)){
					map.setTurn(turns);
					return map;
				}
				ais[c].process();
			}
		}
		map.setTurn(TIME_OUT);
		this.game = game;
		return map;
	}
	
	/** Fills the population from the given index on with children of the upper part */
	private void breedFrom(int index){
		for(int c=index; c<population.length; c++){
			int mother = GameConstants.RANDOM.nextInt(index);
			int father = GameConstants.RANDOM.nextInt(index);
			Neurals neural = Crossover.breed(population[mother].getCanary().getNeurals(), population[father].getCanary().getNeurals());
			BirdHouse bird = new BirdHouse(new BlueCanary(null, neural), Float.NEGATIVE_INFINITY, currentGen, nextFreeId++);
			population[c] = bird;
		}
	}
	
	/** Adds completely new canaries to the population, starting at index */
	private void spawnFrom(int index, boolean breed){
		for(int c=index; c<population.length; c++){
			Neurals neural = new Neurals();
			if(breed){
				neural = Crossover.breed(population[GameConstants.RANDOM.nextInt(ELITE)].getCanary().getNeurals(), Crossover.breed(population[GameConstants.RANDOM.nextInt(ELITE)].getCanary().getNeurals(),neural));
			}
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
	
	/** Returns the bird with the best score who is from the latest generation */
	public BirdHouse bestCurrentGenner(){
		for(int c=0; c<population.length; c++){
			if(population[c].getBirthGen()==currentGen-1){
				return population[c];
			}
		}
		return null;
	}
	
	/** Inner class holding an individual */
	@RequiredArgsConstructor @Data private class BirdHouse{
		@NonNull private BlueCanary canary;
		@NonNull private float score;
		@NonNull private int birthGen;
		@NonNull private int id;
		private int hasWon = 0;
		private boolean hasPlayed = false;
		private float avgTime = Float.NEGATIVE_INFINITY;
		/** Returns wether this bird has already scored */
		public boolean hasScore(){
			return score>=1000;
		}
	}
	
	/** Keeps track of values used for syso tracking */
	@Data @AllArgsConstructor private class Tracker{
		private BirdHouse topChicken;
		private float topScore;
	}
	
}
