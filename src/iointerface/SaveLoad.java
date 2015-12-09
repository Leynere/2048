package iointerface;

/**
 * This interface define the functions to implement when the game is saved or load.
 * @author sivanantham
 *
 */
public interface SaveLoad {
	/**
	 * 
	 * Save a game in a file.
	 * @param path : path to the file.
	 */
	public void save(String path);
	
	/**
	 * 
	 * Load a game from a file.
	 * @param path : path to the file.
	 * @return : true if there is no problem, false else
	 */
	public boolean load(String path);
}
