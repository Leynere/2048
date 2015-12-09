package game;

import gaphique.Fenetre;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import data.History;
import data.RandomHistory;
import data.Round;
import structure.NewCase;

import structure.Plateau;

/**
 * This class represent the game 2048.
 * @author sivanantham
 *
 */
public class Game{

	/**
	 * The graphique interface of the game.
	 */
	private final Fenetre fenetre;

	/**
	 * The game data.
	 */
	private final Plateau plateau;

	/**
	 * Has the game start?
	 */
	private  boolean started;

	/**
	 * A random value to add, if there isn't random value, it's initialized on -1
	 */
	private RandomHistory randHistory;


	/**
	 * Constructor.
	 * @param size : size of the board.
	 */
	public Game(int size) {
		if(size == 0)
			throw new IllegalArgumentException();
		this.plateau = new Plateau(size);
		this.fenetre = new Fenetre(size);
		this.started = false;
	}


	/**
	 * Says if the game has started.
	 * @return : true id started false else.
	 */
	public boolean isStarted(){
		return this.started;
	}


	/**
	 * Initialize the game when there isn't options.
	 * 
	 */
	public void addOption(){
		if(this.started == true)
			throw new IllegalStateException();
		this.plateau.newCase();
		this.plateau.newCase();
		this.fenetre.update(plateau.getPlateau());
	}




	/**
	 * Initialize the game with option(s).
	 * @param option : formated option.
	 * @param argPath : path of the target file.
	 */
	public void addOption(String option, final String argPath){
		switch(option){
		case "-s":
			this.fenetre.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					plateau.getHistory().save(argPath);
				}
			});
			break;
		case "-n":
			/*I don't know what to do here*/
			/* Get Random value ?*/
			this.randHistory = new RandomHistory();
			this.randomLoad();
			this.randHistory.save(argPath);
			/*Do a game with random value? */
			/*if(this.started)
				throw new IllegalArgumentException();
			this.historyLoad(argPath);*/
			this.started = true;
			break;
		case "-a":
			this.randomLoad(argPath);
			this.started = true;
			break;

		case "-r":
			if(this.started)
				throw new IllegalArgumentException();
			this.historyLoad(argPath);
			this.started = true;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Load a history.
	 * @param argPath : target of the history file.
	 */
	private void historyLoad(String argPath) {
		History loadedHistory = new History();
		if(!loadedHistory.load(argPath))
			this.exit();
		this.historyLoad(loadedHistory);
	}

	/**
	 * Load a history.
	 * @param loadedHistory : the history to load.
	 */
	public void historyLoad(History loadedHistory){
		for(Round round : loadedHistory.getHistory()){

			switch(round.getMove()){
			case 'N':
				plateau.deplaceUp();
				break;
			case 'S':
				plateau.deplaceDown();
				break;
			case 'E':
				plateau.deplaceRight();
				break;
			case 'W':
				plateau.deplaceLeft();
				break;
			}

			NewCase newCase = round.getNewCase();
			try{
				plateau.newCase(newCase.getValue(), newCase.getColumn(), newCase.getRow());
			}catch(IllegalArgumentException e){
				this.exit();
			}catch(IllegalStateException e){
				this.exit();
			}

			this.update();
			printOver();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
			}
		}
	}

	/**
	 * 
	 * Load a random history.
	 * @param argPath : target of the random history file.
	 */
	private void randomLoad(String argPath) {
		this.randHistory = new RandomHistory();
		if(!this.randHistory.load(argPath))
			this.exit();
		if(!this.started)
			this.addOption();
	}

	/**
	 * Exit the software when an error is occurred.
	 */
	private void exit() {
		this.fenetre.message("Code erreur 12");
		System.exit(0);
	}


	/**
	 * 
	 * Load a random history.
	 */
	private void randomLoad() {
		this.randHistory = new RandomHistory();
		this.randHistory.init();
		if(!this.started)
			this.addOption();
	}


	/**
	 * 
	 * Add a key listener on this.fenetre.
	 */
	public void addKeyListener(){
		this.fenetre.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode())
				{	
				case KeyEvent.VK_RIGHT:
					plateau.deplaceRight();
					break;
				case KeyEvent.VK_LEFT:	
					plateau.deplaceLeft();
					break;
				case KeyEvent.VK_UP:
					plateau.deplaceUp();
					break;
				case KeyEvent.VK_DOWN:
					plateau.deplaceDown();
					break;
				}

				if(plateau.moved()){
					int value;
					if(randHistory != null && (value = randHistory.getNext())!= -1)
						plateau.newCase(value);
					else
						plateau.newCase();

					update();
				}

				printOver();
			}


		});
	}


	/**
	 * 
	 * Print a message when the game is finished.
	 */
	private void printOver(){
		if(this.plateau.gameOver()){
			this.printScore();
		}
	}
	
	public void printScore(){
		this.fenetre.message("Score : "+this.plateau.score());
	}

	/**
	 * Update the graphique interface items and the history after a move.
	 * @param dir : direction of the move
	 */
	private void update(){
		this.fenetre.update(plateau.getPlateau());
	}

	/**
	 * Get a state of the board of the game.
	 * @return : a clone of the board.
	 */
	public Plateau getPlateau(){
		return this.plateau.clone();
	}

	/**
	 * 
	 * Update the board after a move.
	 * @param move : direction of the movement.
	 * @return : return a formated char of the move.
	 */
	public void play(int move){

		switch(move){
		case 0:
			plateau.deplaceUp();
			return;
		case 1:
			plateau.deplaceDown();
			return;
		case 2:
			plateau.deplaceRight();
			return;
		case 3:
			plateau.deplaceLeft();
			return;
		default:
			System.out.println(move);
			throw new IllegalArgumentException();
		}
	}

	/**
	 * The steps for play the minimax algorithm.
	 * @param move : move to do.
	 */
	public void playSolvLvl4(int move){
		play(move);

		plateau.newCase();
		this.update();
		printOver();
	}

	/**
	 * Initialize the game options for the solveur of level 3.
	 * @param n1 : first case to add.
	 * @param n2 : second case to add.
	 */
	public void addOptionSolv3(NewCase n1, NewCase n2){
		this.plateau.newCase(n1);
		this.plateau.newCase(n2);
		this.fenetre.update(this.plateau.getPlateau());
	}

	/**
	 * Set up an extern board for the game.
	 * Used for test only.
	 * @param p : board to set.
	 */
	public void setPlateau(int[][] p ){
		this.plateau.setPlateau(p);
		this.fenetre.update(p);
	}

	/**
	 * The steps for the solveur of lvl 3
	 * @param move : move to do.
	 * @param n : case to add.
	 */
	public void playSolvLvl3(int move, NewCase n) {
		play(move);
		plateau.newCase(n);
		this.update();
		printOver();

	}
}


