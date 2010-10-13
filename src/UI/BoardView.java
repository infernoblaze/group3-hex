package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.Board;
import Game.Game;
import Players.HumanPlayer;
import Players.Player;

/**
 * Deals with all the user interface stuff. 
 */
public class BoardView extends JPanel
{

	private static final long serialVersionUID = -6451102456367716785L;
	
	private Game game;
	private Board board;
	private Player activePlayer;

	private HexComponent hexView;
	private JLabel statusLabel;
	private JButton swapSidesButton; 
	
	public BoardView()
	{
		this.hexView = new HexComponent();
		this.statusLabel = new JLabel("WELKOM!");
		this.swapSidesButton = new JButton("Swap sides");
		this.swapSidesButton.setVisible(false);
		
		this.add(hexView);
//		this.add(statusLabel);
//		this.add(swapSidesButton);
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		layout.setHgap(1000);
		this.setLayout(layout);
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public void setBoard(Board board)
	{
		this.board = board;
		
		int boardDimensions = board.getDimensions();
		this.hexView.setPreferredSize(new Dimension(
				boardDimensions * 41 + boardDimensions * 21,
				boardDimensions * 36 + 10));
		this.repaint();
	}
	
	public void waitingForMove(Player aPlayer) {
		this.activePlayer = aPlayer;
	}

	/**
	 * Custom drawing class for the board of the game.
	 */
	private class HexComponent extends JComponent implements MouseListener
	{
		private static final long serialVersionUID = -778343204163666984L;
		
		/**
		 * Sets up the right size of the board view
		 */
		public HexComponent()
		{
			this.addMouseListener(this);
		}
		
		/**
		 * Does the drawing
		 */
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			
			// We can't draw anything if there is no board 
			if (board == null)
				return;
			
			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON );
			
			
			int boardDimensions = board.getDimensions();
			for (int i = 0; i < boardDimensions; i++)
				for (int j = 0; j < boardDimensions; j++)
				{
					g2.setColor(new Color(0.8f, 0.6f, 0.1f, 0.8f));

					double startX = i * 41.0 + j * 21.0;
					double startY = j * 36.0;
					
					GeneralPath hex = new GeneralPath();
					hex.moveTo(startX + 00.0, startY + 10.0);
					hex.lineTo(startX + 00.0, startY + 35.0);
					hex.lineTo(startX + 20.0, startY + 45.0);
					hex.lineTo(startX + 40.0, startY + 35.0);
					hex.lineTo(startX + 40.0, startY + 10.0);
					hex.lineTo(startX + 20.0, startY + 0.0);
					hex.closePath();
					
					g2.fill(hex);
					
					int field = board.getField(i, j);
					if (field != 0) {
						if (field == 1)
							g2.setColor(Color.white);
						else
							g2.setColor(Color.black);
						
						g2.fill(new Ellipse2D.Double(
								startX + 13.0, startY + 15.0,
								15.0, 15.0));
					}
				}
		}

		private int[] mouseDownAt;
		
		public void mousePressed(MouseEvent e)
		{
			// Lets get the big blocks that don't "overlay"
			int y1 = (e.getY() / 36);
			int y2 = (e.getY() - 11 * (y1 + 1)) / 25;
			
			if (y1 != y2)
				return;
			// TODO: make the overlapping parts work too.
			
			int x = (e.getX() - y1 * 21) / 41;

			mouseDownAt = new int[2];
			mouseDownAt[0] = x;
			mouseDownAt[1] = y1;
		}
		
		/**
		 * Handles the mouse clicks
		 */
		public void mouseReleased(MouseEvent e)
		{
			// Lets get the big blocks that don't "overlay"
			int y1 = (e.getY() / 36);
			int y2 = (e.getY() - 11 * (y1 + 1)) / 25;
			
			if (y1 != y2)
				return;
			// TODO: make the overlapping parts work too.
			
			int x = (e.getX() - y1 * 21) / 41;
			
			// Make sure that we don't move into other hex
			if (x != mouseDownAt[0] || y1 != mouseDownAt[1])
				return;
			
			int boardDimensions = board.getDimensions();
			if (x >= 0 && x < boardDimensions &&
					y1 >= 0 && y1 < boardDimensions &&
					activePlayer != null &&
					activePlayer.getClass().getName() == "Players.HumanPlayer")
			{
				int[] move = {x, y1};
				
				HumanPlayer human = (HumanPlayer)activePlayer;
				if (human.setNextMove(move))
					activePlayer = null;
			}
		}
		
		public void mouseClicked(MouseEvent e) {
                board.printBoard();
                }
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
	}
	
}
