package main;

import game.Game;

public class Niveau1 {
	public static void main(String[] args) {

		Game game = new Game(4);
		game.addOption();
		game.addKeyListener();
	}
}
