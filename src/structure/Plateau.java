package structure;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import data.History;
import data.Round;

import structure.NewCase;
/**
 * This class represent the board of the game.
 * @author sivanantham
 *
 */
public class Plateau {

	/**
	 * The matrix of the board.
	 */
	private final int[][] plateau;
	/**
	 * A random generator
	 */
	private final Random rand;
	/**
	 * The number of past round.
	 */
	private int nbTour;
	/**
	 * The last movement done on the board
	 */
	private char lastMove;
	/**
	 * Has the board moved after the last movement?
	 */
	private boolean hasMoved;
	/**
	 * The last case added in the board
	 */
	private NewCase lastNewCase;
	/**
	 * The history of the game.
	 */
	private final History history;
	/**
	 * The size of the board.
	 */
	private final int size;
	/**
	 * Constructor.
	 * @param size : size of the board.
	 */
	public Plateau(int size){
		if(size == 0)
			throw new IllegalArgumentException();
		this.nbTour = 0;
		this.rand = new Random(System.currentTimeMillis());
		this.plateau = new int [size][size];
		this.hasMoved = true;
		this.size = size;
		this.lastNewCase = NewCase.emptyNewCase();
		this.history = new History();
		this.lastMove = '\0';
	}
	/**
	 * Constructor for the clone.
	 * @param plateau
	 * @param rand
	 * @param nbTour
	 * @param hasMoved
	 * @param lastNewCase
	 * @param size
	 * @param history
	 * @param move
	 */
	private Plateau(int[][] plateau, Random rand, int nbTour, boolean hasMoved, NewCase lastNewCase, int size, History history, char move){
		Objects.requireNonNull(plateau);
		Objects.requireNonNull(rand);
		Objects.requireNonNull(lastNewCase);
		Objects.requireNonNull(history);
		if(plateau.length != size || plateau[0].length != size || nbTour < 0)
			throw new IllegalArgumentException();
		this.plateau = plateau;
		this.rand = rand;
		this.nbTour = nbTour;
		this.hasMoved = hasMoved;
		this.lastNewCase  = lastNewCase;
		this.size = size;
		this.history = history;
		this.lastMove = move;
	}
	/**
	 * Get the size of the board
	 * @return : this.size
	 */
	public int getSize(){
		return size;
	}

	@Override
	public Plateau clone() {
		return new Plateau(this.getPlateau(), this.rand, this.nbTour, this.hasMoved, this.lastNewCase.clone(), this.size, this.history.clone(),this.lastMove);
	}
	
	/**
	 * Get the matrix of the board.
	 * @return
	 */
	public int[][] getPlateau(){
		int [][] res = new int[this.plateau.length][this.plateau[0].length];
		for(int i = 0; i<this.plateau.length ; i++)
			for(int j = 0; j<this.plateau[i].length; j++)
				res[i][j] = this.plateau[i][j];
		return res;
	}
	
	/**
	 * Check and fusion of the cases if it can be possible
	 * @param i : row who is checked
	 * @param j : column who is checked
	 * @param incres : 1 if it's the normal way, -1 if it's the reverse
	 * @param vertical : is it a vertical fusion?
	 * @return : has the board changed after?
	 */
	private boolean lvlUp(int i, int j, int incres, boolean vertical){
		boolean moved = false;
		if(this.plateau[i][j] != 0){

			if(vertical && i+incres >= 0 && i+incres < this.plateau.length){
				if(this.plateau[i][j] == this.plateau[i+incres][j]){
					this.plateau[i][j] += this.plateau[i][j];
					this.plateau[i+incres][j] = 0;
					moved = true;
				}
			}
			if(!vertical && j+incres >= 0 
					&& j+incres < this.plateau[i].length 
					&& this.plateau[i][j] == this.plateau[i][j+incres]){
				this.plateau[i][j] += this.plateau[i][j];
				this.plateau[i][j+incres] = 0;
				moved = true;

			}
		}

		return moved;
	}
	/**
	 * Exchanges two cases of the board.
	 * @param ai : row of the case a
	 * @param aj : column of the case a
	 * @param bi : row of the case b
	 * @param bj :  column of the case b
	 */
	private void swap(int ai, int aj, int bi, int bj){
		int temp = this.plateau[ai][aj];
		this.plateau[ai][aj] = this.plateau[bi][bj];
		this.plateau[bi][bj] = temp;
	}
	
	/**
	 * Push to the end a void case of an array of the board if the current position isn't void and if there is at least a void case before.
	 * @param indexOfFirstVoid : index of the first void case
	 * @param i : row of the current position
	 * @param j : column of the current position
	 * @param incres : 1 if it's the normal way, -1 if it's the reverse
	 * @param vertical : is it a vertical fusion?
	 * @return : the index of the first void after the push.
	 */
	private int deleteVoid(int indexOfFirstVoid, int i, int j,final int incres,boolean vertical){
		if(this.plateau[i][j] == 0 && indexOfFirstVoid == -1)
			if(vertical)
				indexOfFirstVoid = i;
			else
				indexOfFirstVoid = j;

		if(indexOfFirstVoid != -1 && this.plateau[i][j] != 0){
			if(vertical)
				swap(i,j,indexOfFirstVoid,j);
			else
				swap(i,j,i,indexOfFirstVoid);

			indexOfFirstVoid += incres;
		}
		return indexOfFirstVoid;
	}
	/**
	 * Push to the and all the void cases of the board
	 * @param start : this.plateau.length-1 if we start at the end(reverse), 0(normal) else.
	 * @param incres : 1 if it's the normal way, -1 if it's the reverse
	 * @param vertical : is it a vertical fusion?
	 * @return : has the board changed after?
	 */
	private boolean deleteAllVoid(final int start, final int incres, boolean vertical){
		int indexOfFirstVoid = -1;
		int firstElemVoid =  indexOfFirstVoid;
		boolean moved = false;

		for(int i = 0; i<this.plateau.length; i++){
			for(int j = start; j>=0 && j<this.plateau[i].length; j+= incres){
				if(vertical)
					indexOfFirstVoid = deleteVoid(indexOfFirstVoid,j,i,incres,true);
				else	
					indexOfFirstVoid = deleteVoid(indexOfFirstVoid, i, j, incres,false);


				if(firstElemVoid == -1)
					firstElemVoid = indexOfFirstVoid;
			}
			moved = (firstElemVoid != indexOfFirstVoid ) || moved;

			indexOfFirstVoid = -1;
			firstElemVoid = indexOfFirstVoid;
		}
		return moved;
	}
	/**
	 * Moves the board to a direction
	 * @param start : this.plateau.length-1 if we start at the end(reverse), 0(normal) else.
	 * @param incres : 1 if it's the normal way, -1 if it's the reverse
	 * @param vertical : is it a vertical fusion?
	 * @return : has the board changed after?
	 */
	private boolean deplace(final int start, final int incres,final boolean vertical){
		boolean moved = this.deleteAllVoid(start, incres,vertical);	
		for(int i = 0; i<this.plateau.length; i++){
			for(int j = start; j>=0 && j<this.plateau[i].length; j+= incres){
				if(vertical)
					moved = this.lvlUp(j,i,incres,vertical) || moved;
				else
					moved = this.lvlUp(i,j,incres,vertical) || moved;
			}
		}
		moved = this.deleteAllVoid(start, incres, vertical) || moved;

		return moved;
	}
	
	/**
	 * Move the board to the up side.
	 */
	public void deplaceUp(){
		this.hasMoved = this.deplace(0, 1, true);
		this.lastMove = 'N';
	}
	/**
	 * Move the board to the left side.
	 */
	public void deplaceLeft(){
		this.hasMoved = this.deplace(0, 1, false);
		this.lastMove = 'W';
	}
	/**
	 * Move the board to the down side.
	 */
	public void deplaceDown(){
		this.hasMoved = this.deplace(this.plateau[0].length-1, -1, true);
		this.lastMove = 'S';
	}
	
	/**
	 * Move the board to the right side.
	 */
	public void deplaceRight(){
		this.hasMoved = this.deplace(this.plateau[0].length-1, -1, false);
		this.lastMove = 'E';
	}

	/**
	 * Has the game over?
	 * @return : true if the game is over, false else
	 */
	public boolean gameOver(){
		for(int i = 0; i<this.plateau.length; i++){
			for(int j = 0; j<this.plateau[i].length; j++){

				if(this.plateau[i][j] == 0)
					return false;

				if(j<this.plateau[i].length-1 && (this.plateau[i][j] == this.plateau[i][j+1])){
					return false;
				}

				if(i<this.plateau.length-1 && (this.plateau[i][j] == this.plateau[i+1][j])){
					return false;
				}

			}	
		}
		return true;
	}
	
	/**
	 * Has the board moved?
	 * @return : true if the board has moved, false else
	 */
	public boolean moved(){
		return hasMoved;
	}
	
	/**
	 * Set a new case on the board if a specified value.
	 * @param value : value of the new case (2 or 4)
	 */
	public void newCase(int value){
		if(!this.hasMoved)
			throw new IllegalStateException();

		LinkedList<Integer[]> voidCases = getVoidCases();

		if(voidCases.size() != 0){
			Integer[] randomCase = voidCases.get(rand.nextInt(voidCases.size()));
			this.newCase(value, randomCase[1], randomCase[0]);
		}
	}
	
	/**
	 * Get a list of void cases of the board
	 * @return : a list of void cases of the board
	 */
	public LinkedList<Integer[]> getVoidCases(){
		LinkedList<Integer[]> voidCases = new LinkedList<>();
		for(int i =0; i<this.plateau.length ; i++){
			for(int j = 0; j<this.plateau[i].length; j++){
				if(this.plateau[i][j] == 0){
					Integer[] temp = new Integer[2];
					temp[0] = i;
					temp[1] = j;
					voidCases.add(temp);
				}
			}
		}
		return voidCases;
	}
	
	/**
	 * Get the value of a case of the board.
	 * @param row : row of the value to get
	 * @param column : column of the value to get
	 * @return
	 */
	public int getValue(int row, int column) {
		return this.plateau[row][column];
	}
	
	/**
	 * Get the number of round of the game
	 * @return: this.nbTour
	 */
	public int getNbTour(){
		return this.nbTour;
	}

	/**
	 * Set a new case on the board.
	 */
	public void newCase(){
		if(rand.nextInt(100)<90 || this.nbTour<2 )
			newCase(2);
		else
			newCase(4);
	}
	
	/**
	 * Set a new case on the board with a specified position and value.
	 * @param value : value of the new case to set (2 or 4)
	 * @param column : column of the new case to set
	 * @param row : row of the new case to set
	 */
	public void newCase(int value, int column, int row){
		if(!this.hasMoved || row < 0 || row >=this.plateau.length || column <0 || column >=this.plateau[0].length || this.plateau[row][column] != 0){
			throw new IllegalArgumentException();
		}
		this.lastNewCase = new NewCase(row, column, value);
		this.history.addRound(new Round(this.lastNewCase.clone(),this.lastMove));
		this.plateau[row][column] = value;
		this.nbTour++;
	}
	
	/**
	 * Calculate an heuristic score of the board (for minimax).
	 * @return : the score of the board
	 */
	public int heuristic(){
		int voidCases = 0;
		for(int i = 0;  i<this.plateau.length;i++){
			for(int j = 0; j<this.plateau[i].length; j++){
				if(this.plateau[i][j] == 0)
					voidCases++;
			}
		}
		return voidCases*this.score();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Plateau){
			Plateau p = (Plateau)obj;
			for(int i = 0; i<this.plateau.length;i++){
				for(int j = 0; j<this.plateau[i].length;j++){
					if (p.getValue(i, j)!=this.plateau[i][j])
						return false;
				}
			}
		}
		else
			return false;

		return true;
	}
	
	/**
	 * Set the board with an extern matrix.
	 * Used fo test only.
	 * @param p : the extern matrix to set.
	 */
	public void setPlateau(int[][] p) {
		for(int i = 0; i<this.plateau.length;i++){
			for(int j = 0; j<this.plateau[i].length;j++){
				this.plateau[i][j] = p[i][j];
			}
		}
	}
	
	/**
	 * Has the board accessed to max.
	 * @param max : value to access for win.
	 * @return : true if max is in the board, false else.
	 */
	public boolean won(int max) {
		for(int i = 0; i<this.plateau.length;i++){
			for(int j = 0; j<this.plateau[i].length;j++){
				if(this.plateau[i][j] >= max)
					return true;
			}
		}
		return false;
	}
	/**
	 * Get the history of this board
	 * @return : this.history
	 */
	public History getHistory() {
		return this.history.clone();
	}

	/**
	 * Set a new case on the board with a NewCase.
	 * @param n : new case to add.
	 */
	public void newCase(NewCase n) {
		this.newCase(n.getValue(),n.getColumn(),n.getRow());
	}
	
	/**
	 * Calculate the score of the board
	 * @return : the score of the board
	 */
	public int score(){
		int res = 0;
		for(int i = 0; i<this.plateau.length;i++){
			for(int j = 0;j<this.plateau[i].length;j++){
				res += this.plateau[i][j];
			}
		}
		return res;
	}


}
