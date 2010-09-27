package Game;

import java.awt.Dimension;

import javax.swing.JFrame;

import UI.*;

/**
 * The main class with the main method, yay!
 * @author Martins
 */
public class Main {
	
	public static void main(String[] args) {

		BoardView boardView = new BoardView();

		Game game = new Game();
		game.setBoardView(boardView);
		new Thread(game).start();
		
		JFrame frame = new JFrame("Hexly");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(800, 500));
		frame.add(boardView);
		frame.setVisible(true);
		
	}
	
}
