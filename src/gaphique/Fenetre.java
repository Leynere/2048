package gaphique;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * This class define the graphique window of the game.
 * @author sivanantham
 *
 */
public class Fenetre {
	/**
	 * The main frame of the game.
	 */
	private final JFrame fenetre;
	/**
	 * The graphique board of the game.
	 */
	private final Case[][] cases;
	
	/**
	 * Constructor.
	 * @param size : size of the board.
	 */
	public Fenetre(int size){
		if(size == 0)
			throw new IllegalArgumentException();
		this.fenetre = new JFrame();
		this.fenetre.setTitle("SIVANANTHAM 2048");
		this.fenetre.setSize(1024, 768);
		this.fenetre.setLocation(0, 0);
		this.fenetre.setLocationRelativeTo(null);
		this.fenetre.setResizable(false);
		this.fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension gridDimension = new Dimension();
		gridDimension.setSize(125*size,125*size);

		final GridLayout gl = new GridLayout(size,size);
		gl.setHgap(10);
		gl.setVgap(10);

		JPanel grid = new JPanel();
		grid.setBackground(Color.black);
		grid.setLayout(gl);
		grid.setPreferredSize(gridDimension);

		JPanel background = new JPanel();
		background.setBackground(Color.white);

		background.add(grid);

		this.fenetre.setContentPane(background);
		this.cases = new Case[size][size];
		for(int i =0; i< this.cases.length; i++){
			for(int j = 0; j<this.cases[i].length; j++ ){
				this.cases[i][j] = new Case();
				grid.add(this.cases[i][j].getPanneau());
			}
		}
		this.fenetre.setVisible(true);	
	}	
	
	/**
	 * Add a key listener to the main frame.
	 * @param listener : key listener to add.
	 */
	public void addKeyListener(KeyListener listener){
		this.fenetre.addKeyListener(listener);
	}
	
	/**
	 * Add a window listener to the main frame.
	 * @param listener : window listener to add.
	 */
	public void addWindowListener(WindowListener listener) {
		this.fenetre.addWindowListener(listener);
	}
	
	/**
	 * Update the graphique board.
	 * @param plateau : the data board.
	 */
	public void update(final int[][] plateau){
		for(int i = 0; i<this.cases.length; i++){
			for(int j = 0; j<this.cases[i].length; j++){
				this.cases[i][j].setLvl(plateau[i][j]);
			}
		}
	}
	/**
	 * Pop a message.
	 * @param string : the message to pop.
	 */
	public void message(String string) {
		JOptionPane.showMessageDialog(this.fenetre.getContentPane(), string);
	}

}
