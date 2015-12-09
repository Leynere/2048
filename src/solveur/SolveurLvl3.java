package solveur;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import data.History;

import game.Game;
import structure.NewCase;
import structure.Plateau;
/**
 * This class represent the solveur lvl 3.
 * @author sivanantham
 *
 */
public class SolveurLvl3 {
	/**
	 * A board of the game.
	 */
	private final Plateau plateau;
	/**
	 * The random generator.
	 */
	private final Random rand;
	/**
	 * A map who saves the random value of a round.
	 */
	private final HashMap <Integer,Integer> randValue;
	/**
	 * A map who saves the random ncv of a round.
	 */
	private final HashMap <Integer,Integer> randNcv;
	/**
	 * The path to reach map with this game.
	 */
	private History res;
	/**
	 * Time of the start of the solveur.
	 */
	private long time;

	/**
	 * Constructor
	 * @param max : max value to access.
	 */
	public SolveurLvl3() {
		this.plateau = new Plateau(4);
		this.rand = new Random(14071789);
		this.randValue = new HashMap<>();
		this.randNcv = new HashMap<>();
		init();
		init();
	}
	/**
	 * Initialize the first 2 cases.
	 */
	public void init(){
		NewCase temp = getNewCase(2, rand.nextInt(10000), this.plateau);
		this.plateau.newCase(temp.getValue(),temp.getColumn(),temp.getRow());
	}
	/**
	 * Start the solveur.
	 */
	public void play(){
		this.time= System.currentTimeMillis();
		this.solv(this.plateau.clone());
		Game game = new Game(4);
		game.historyLoad(this.res);
		game.printScore();
	}
	/**
	 * 
	 * Algorithm who decide which path to take.
	 * @param plateau : the board at a moment of the game.
	 * @return : true if the goal is reached, false else.
	 */
	private boolean solv(Plateau plateau) {

		if(System.currentTimeMillis()-this.time>=3600000){
			return true;
		}

		if(plateau.won(32768)){
			this.res = plateau.getHistory();
			return true;
		}

		if(plateau.gameOver())
			return false;

		int randValue;
		int randNcv;

		if(this.randValue.containsKey(plateau.getNbTour())){
			randValue = this.randValue.get(plateau.getNbTour());
		}
		else{
			randValue = rand.nextInt(100);
			this.randValue.put(plateau.getNbTour(), randValue);
			this.res = plateau.getHistory();
		}

		if(this.randNcv.containsKey(plateau.getNbTour())){
			randNcv = this.randNcv.get(plateau.getNbTour());
		}
		else{
			randNcv = rand.nextInt(10000);
			this.randNcv.put(plateau.getNbTour(), randNcv);
		}

		int value;
		if(randValue>10)
			value = 2;
		else
			value = 4;

		int voidCase = randNcv;

		for(int i = 0; i< 4; i++){
			Plateau tmpPlateau = plateau.clone();
			switch(i){
			case 0:
				tmpPlateau.deplaceUp();
				break;
			case 1:
				tmpPlateau.deplaceDown();
				break;
			case 2:
				tmpPlateau.deplaceRight();
				break;
			case 3:
				tmpPlateau.deplaceLeft();
				break;
			}

			if(tmpPlateau.moved()){
				NewCase n = getNewCase(value,voidCase, tmpPlateau);

				tmpPlateau.newCase(n.getValue(), n.getColumn(), n.getRow());
				try{
					if(this.solv(tmpPlateau)){
						return true;
					}
				}catch(StackOverflowError e){
					return true;
				}catch(OutOfMemoryError e){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get a new case to add on the board.
	 * @param value : value of the case to add.
	 * @param ncv : a random value under 0 and 9999
	 * @param p : a board at a moment of the game.
	 * @return : a new case.
	 */
	private NewCase getNewCase(int value, int ncv, Plateau p){
		LinkedList<Integer[]>voidCases = p.getVoidCases();
		if(voidCases.size()!=0){
			Integer[] pos = voidCases.get(ncv%voidCases.size());
			return new NewCase(pos[0], pos[1], value);
		}
		throw new IllegalStateException();
	}

}
