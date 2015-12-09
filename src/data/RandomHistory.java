package data;

import iointerface.SaveLoad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represent a random history of a game.
 * @author sivanantham
 *
 */
public class RandomHistory implements SaveLoad{
	
	/**
	 * A list of integer of 0 or 1.
	 */
	private final ArrayList<Integer> values;
	
	/**
	 * Position in the generator
	 */
	private int index;
	
	/**
	 * Byte value of the history.
	 */
	private byte[] randomHistory;

	/**
	 * 
	 * Constructor
	 */
	public RandomHistory() {
		this.values = new ArrayList<>();
		this.index = 0;
	}
	
	/**
	 * Get the next element of the list.
	 * @return : the next element of the list
	 */
	public int getNext(){
		this.index ++;
		if(this.values.size() != index)
			return this.values.get(index-1);
		else
			return -1;
	}
	/**
	 * Reset the index of the explore.
	 */
	public void reset(){
		this.index = 0;
	}
	
	@Override
	public boolean load(String path) {
		FileInputStream file;
		try {
			file = new FileInputStream(new File(path));
			this.randomHistory = new byte[file.available()];
			file.read(this.randomHistory);
			for(int j = 0; j<this.randomHistory.length;j++){
				byte mask = 1;
				for(int i = 0; i<8; i++){
					int toAdd = 2;
					if((mask & this.randomHistory[j])!=0 && this.values.size()>2)
						toAdd = 4;
					this.values.add(toAdd);
					mask = (byte)(mask<<1);
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
	/**
	 * Initialize the random history.
	 */
	public void init(){
		Random rand= new Random(System.currentTimeMillis());
		this.randomHistory = new byte[rand.nextInt(100)];
		for(int i = 0;i<this.randomHistory.length;i++){
			byte b = 1;
			int decalage = rand.nextInt(9);
			
			for(int j = 0; j<decalage; j++){
				b = (byte) (b<<1);
			}
			
			byte mask = 1;
			for(int j = 0; j<8;j++){
				int toAdd = 2;
				if((mask & b)!=0 && this.values.size()>2)
					toAdd = 4;
				this.values.add(toAdd);
			}
			this.randomHistory[i] = b;
		}
	}

	@Override
	public void save(String path) {
		FileOutputStream file;
		try {
			file =new FileOutputStream(new File(path));
			file.write(randomHistory);
			file.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 
	}
}
