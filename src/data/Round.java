package data;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import structure.NewCase;
/**
 * This class represent a round of the game.
 * @author sivanantham
 *
 */
public class Round {
	/**
	 * The new case of the round.
	 */
	private NewCase newCase;
	
	/**
	 * the move who has been done in the round.
	 */
	private char move;
	/**
	 * 
	 * Constructor of a moved round
	 * @param newCase : a new case added in this round
	 * @param move : the move who has been done in this round.
	 */
	public Round(NewCase newCase, char move) {
		Objects.requireNonNull(newCase);
		this.newCase = newCase;
		this.move = move;
	}
	
	/**
	 * 
	 * Constructor of a round
	 * @param newCase : a new case added in this round
	 */
	public Round(NewCase newCase) {
		Objects.requireNonNull(newCase);
		this.newCase = newCase;
		this.move = '\0';
	}
	
	
	/**
	 * 
	 * Formated save of a round in a file.
	 * @param file : target file.
	 */
	public void save(FileOutputStream file) {
		try{
			if(this.move != '\0')
				file.write(this.move);
			this.newCase.save(file);
			file.write('\n');
		}catch(IOException e){
			
		}
	}
	
	/**
	 * 
	 * get the case added in this round
	 * @return : the case added in this round
	 */
	public NewCase getNewCase() {
		return this.newCase.clone();
	}
	
	/**
	 * 
	 * get the move who has been done in this round
	 * @return : the movement of this round
	 */
	public char getMove(){
		return this.move;
	}
	
	@Override
	public Round clone(){
		return new Round(this.newCase.clone(),this.move);
	}
	@Override
	public String toString() {
		return newCase.toString()+"; dir : "+this.move+"\n";
	}
	
}
