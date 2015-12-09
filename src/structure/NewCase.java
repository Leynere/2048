package structure;

import java.io.FileOutputStream;
import java.io.IOException;
/**
 * This class represent a new case of the board.
 * @author sivanantham
 *
 */
public class NewCase {
	/**
	 * Value of the new case.
	 */
	private final int value;
	/**
	 * The row of the new case.
	 */
	private final int row;
	/**
	 * The column of the new case
	 */
	private final int column;
	
	/**
	 * Constructor.
	 * @param row
	 * @param column
	 * @param value
	 */
	public NewCase(int row, int column, int value) {
		if(value != 2 && value !=4 && value != -1){
			throw new IllegalArgumentException();
		}
		this.value = value;
		this.row = row;
		this.column = column;
	}
	/**
	 * Get the value of the new case.
	 * @return : this.value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Get the row of the new case.
	 * @return : this.row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Get the column of the new case.
	 * @return : this.column
	 */
	public int getColumn() {
		return column;
	}
	@Override
	public String toString() {
		return "row : "+row+"; column : "+column+"; value : "+value;
	}
	
	@Override
	public NewCase clone(){
		return new NewCase(this.row,this.column,this.value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NewCase){
			NewCase o = (NewCase) obj;
			return (o.getValue() == this.value && o.getColumn() == this.column && o.getRow() == this.row);
		}
		return false;
	}
	/**
	 * Write this new case in a stream.
	 * @param file
	 */
	public void save(FileOutputStream file) {
		try {
			file.write(this.value+48);
			file.write(this.column+49);
			file.write(this.row+49);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get an empty new case.
	 * @return a new case initialized in -1 -1 -1
	 */
	public static NewCase emptyNewCase() {
		return new NewCase(-1,-1,-1);
	}
	
}
