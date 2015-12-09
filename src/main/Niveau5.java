package main;

import game.Game;

public class Niveau5 {

	public static void main(String[] args){
		Game game = new Game(5);
		for(int i = 0; i<args.length; i+=2 )
			try{
				game.addOption(args[i],args[i+1]);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Arguments invalide.");
			}catch (IllegalArgumentException e) {
				System.out.println("Arguments invalide.");
			}
		if(!game.isStarted())
			game.addOption();
		game.addKeyListener();
	}
}
