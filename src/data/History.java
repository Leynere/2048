package data;

import iointerface.SaveLoad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

import structure.NewCase;
/**
 * This class represent an history of a game.
 * @author sivanantham
 *
 */
public class History implements SaveLoad{
	/**
	 * A list of round.
	 */
	private final LinkedList<Round> history;
	/**
	 * Constructor
	 * 
	 */
	public History() {
		this.history = new LinkedList<>();
	}
	
	
	/**
	 * 
	 * Add  an element in the history
	 * @param round : rounde to add in history
	 */
	public void addRound(Round round){
		Objects.requireNonNull(round);
		this.history.add(round);
	}
	/**
	 * 
	 * Get the list of history
	 * @return a new list
	 */
	public LinkedList<Round> getHistory(){
		return new LinkedList<Round>(history);
	}
	
	
	@Override
	public boolean load(String path) {
		FileInputStream file;
		char buffer;
		int value; 
		int column; 
		int row;
		try {
			file  = new FileInputStream(new File(path));
			
			while(file.available()>0){
				buffer = (char) file.read();
				if(buffer == '#'){
					while((buffer = (char) file.read())!='\n');
				}
				else{
					
					if(buffer == '2' || buffer == '4'){
						value = buffer-48;
						column = file.read()-49;
						row = file.read()-49;
						this.addRound(new Round(new NewCase(row,column,value)));
					}
					else{
						value = file.read()-48;
						column = file.read()-49;
						row = file.read()-49;
						this.addRound(new Round(new NewCase(row,column,value),buffer));
					}
					file.read();
				}

			}
			file.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;

	}
	@Override
	public History clone(){
		History res = new History();
		for(Round r : this.history){
			res.addRound(r.clone());
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Round tour : this.history)
			builder.append(tour.toString());
		return builder.toString();
	}
	
	
	@Override
	public void save(String path) {
		FileOutputStream file;
		try {
			file =new FileOutputStream(new File(path));

			for(Round round : this.history){
				round.save(file);
			}

			file.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 
	}
}
