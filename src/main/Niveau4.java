package main;

import solveur.SolveurLvl4;

public class Niveau4 {


	public static void main(String[] args) {
		SolveurLvl4 solveur = new SolveurLvl4();
		while(!solveur.getPlateau().gameOver()){
			solveur.play();
		}
	}
}
