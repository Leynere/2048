package solveur;

/*import java.util.HashMap;*/

import game.Game;

import structure.Plateau;
/**
 * This class represent the solveur lvl 4.
 * @author sivanantham
 *
 */
public class SolveurLvl4 {

	/*Map who saves the score of a board.
	private HashMap <Plateau,Integer> scores;
	Map who saves the deep of the score of a board.
	private HashMap <Plateau,Integer> depth;*/
	/**
	 * Move to do.
	 */
	private int res;
	/**
	 * The game to solv.
	 */
	private final Game game;
	
	
	/**
	 * Constructor.
	 */
	public SolveurLvl4() {
		this.game = new Game(4);
		this.game.addOption();
		/*this.depth = new HashMap<>();
		this.scores = new HashMap<>();*/
	}
	/**
	 * Get a board of the game
	 * @return : the board of the game
	 */
	public Plateau getPlateau() {
		return this.game.getPlateau();
	}
	/**
	 * Start the game.
	 */
	public void play() {
		this.res = -1;
		this.minimax(this.game.getPlateau(),Integer.MIN_VALUE,Integer.MAX_VALUE,8,true);
		/*System.out.println(this.res);*/
		this.game.playSolvLvl4(this.res);
	}	
	/**
	 * Algorithme minimax
	 * @param plateau : the board at a moment.
	 * @param alpha : max value.
	 * @param beta : min value
	 * @param depth : depth to explore.
	 * @param player : AI or human?
	 * @return : score of the board.
	 */
	public int minimax(Plateau plateau,int alpha, int beta, int depth, boolean player){
		if(depth == 0 || plateau.gameOver() || plateau.won(2048)){
			if(plateau.gameOver())
				return Integer.MIN_VALUE;
			if(plateau.won(2048))
				return Integer.MAX_VALUE;
			return plateau.heuristic();
		}

		if(player){
			int[] res = new int[4];
			int i;
			int value = Integer.MIN_VALUE;

			for(i = 0; i<4; i++){
				res[i] = Integer.MIN_VALUE;
			}

			for(i = 0; i<4; i++){
				Plateau tmpPlateau = plateau.clone();
				value = -1;
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

					/*if(this.scores.containsKey(tmpPlateau) && this.depth.get(tmpPlateau)>=depth)
						value = max(value,this.scores.get(tmpPlateau));
					else{*/
					value = max(value,minimax(tmpPlateau,alpha,beta,depth -1, false));
					/*this.scores.put(tmpPlateau, value);
						this.depth.put(tmpPlateau, depth);

					}*/
					if(value>=beta){
						this.res = max(res); 
						return value;
					}
					alpha = max(alpha,value);
					res[i] = alpha;
				}
			}
			this.res = max(res); 
			return value;
		}
		else{
			int value = Integer.MAX_VALUE;
			for(int i = 0; i< plateau.getSize(); i++){
				for(int j = 0; j<plateau.getSize(); j++){
					for(int k = 1; k< 3; k++)
						if(plateau.getValue(i, j) == 0){
							Plateau tmpPlateau = plateau.clone();
							tmpPlateau.newCase(2*k, j, i);

							/*if(this.scores.containsKey(tmpPlateau) && this.depth.get(tmpPlateau)>=depth)
								value = min(value,this.scores.get(tmpPlateau));
							else{*/
							value = min(value,minimax(tmpPlateau,alpha,beta, depth -1, true));
							/*this.scores.put(tmpPlateau, value);
								this.depth.put(tmpPlateau, depth);
							}*/

							if(alpha>=value){
								return value;
							}
							beta = min(beta,value);


						}
				}

			}
			return value;
		}
	}
	int max(int[] a){
		int res = 0;
		for(int i = 0; i<a.length;i++){
			if(a[res]<a[i])
				res = i;
		}
		/*System.out.println(a[res]);
		System.out.println();*/
		return res;
	}
	int max(int a, int b){
		if(a>=b)
			return a;
		return b;
	}

	int min(int a, int b){
		if(a<=b)
			return a;
		return b;
	}

}
