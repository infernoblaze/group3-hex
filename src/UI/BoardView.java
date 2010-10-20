package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

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
	private JButton newGameButton;

	public BoardView()
	{
		hexView = new HexComponent();
		
		statusLabel = new JLabel("", JLabel.CENTER);
		Dimension preferedSize = new Dimension(200, 30);
		statusLabel.setHorizontalTextPosition(JLabel.CENTER);
		statusLabel.setPreferredSize(preferedSize);
		
		swapSidesButton = new JButton("Swap sides");
		swapSidesButton.setVisible(false);
		swapSidesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (activePlayer != null &&
						activePlayer.getClass().getName() == "Players.HumanPlayer")
				{
					if (((HumanPlayer)activePlayer).swapSides())
						activePlayer = null;
				}
			}
		});
		
		newGameButton = new JButton("New Game!");
		newGameButton.setVisible(false);
		final BoardView tempBoardView = this;
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game game = new Game();
				game.setBoardView(tempBoardView);
				new Thread(game).start();
				
				newGameButton.setVisible(false);
			}
		});
		
		this.add(hexView);
		this.add(statusLabel);
		this.add(swapSidesButton);
		this.add(newGameButton);
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		layout.setHgap(1000);
		this.setLayout(layout);
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	private GeneralPath[][] hexagons;
	
	public void setBoard(Board board)
	{
		this.board = board;
		
		int boardDimensions = board.getDimensions();
		this.hexView.setPreferredSize(new Dimension(
				(boardDimensions * 2 - 1) * 35 + 11 + 24,
				boardDimensions * 40 + 20 + 24));
		
		hexagons = new GeneralPath[boardDimensions][boardDimensions];
		for (int i = 0; i < boardDimensions; i++)
			for (int j = 0; j < boardDimensions; j++)
			{
				double startX = i * 35.0 + j * 35.0 + 10.0;
				double startY = (boardDimensions - i) * 20.0 + j * 20.0 + 12.0;
				
				GeneralPath hex = new GeneralPath();
				hex.moveTo(startX + 10.0, startY + 00.0);
				hex.lineTo(startX + 35.0, startY + 00.0);
				hex.lineTo(startX + 45.0, startY + 20.0);
				hex.lineTo(startX + 35.0, startY + 40.0);
				hex.lineTo(startX + 10.0, startY + 40.0);
				hex.lineTo(startX + 00.0, startY + 20.0);
				hex.closePath();
				hexagons[i][j] = hex;
			}

		this.repaint();
	}
	
	public void waitingForMove(Player aPlayer)
	{
		this.activePlayer = aPlayer;
		
		if (activePlayer.getPlayerId() == Game.PLAYER_ONE)
			if (activePlayer.getClass().getName() == "Players.HumanPlayer")
				setStatusText("It is White's move.");
			else
				setStatusText("Waiting for White's move.");
		
		else if (activePlayer.getPlayerId() == Game.PLAYER_TWO)
			if (activePlayer.getClass().getName() == "Players.HumanPlayer")
				setStatusText("It is Black's move.");
			else
				setStatusText("Waiting for Black's move.");
	}
	
	public void canSwapSides(boolean state)
	{
		if (state && activePlayer.getClass().getName() == "Players.HumanPlayer")
		{
			swapSidesButton.setVisible(true);
			this.repaint();
			
			if (activePlayer.getPlayerId() == Game.PLAYER_ONE)
				setStatusText("It is White's move. Maybe swap?");			
			else if (activePlayer.getPlayerId() == Game.PLAYER_TWO)
				setStatusText("It is Black's move. Maybe swap?");			
		}
		else
			swapSidesButton.setVisible(false);
	}
	
	public void gameHasEnded(int winner) {
		newGameButton.setVisible(true);
		this.repaint();
		
		if (winner == 1) {
			setStatusText("White wins, hurrah!");
		} else if (winner == 2) {
			setStatusText("Black wins, hurrah!");
		}

	}

	public void setStatusText(String text)
	{
		statusLabel.setText(text);
	}
	
	/**
	 * Custom drawing class for the board of the game.
	 */
	private class HexComponent extends JComponent implements MouseListener, MouseMotionListener
	{
		private static final long serialVersionUID = -778343204163666984L;
		
		/**
		 * Sets up the right size of the board view
		 */
		public HexComponent()
		{
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
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

			drawBoardBorder(g2);
			
			for (int i = 0; i < boardDimensions; i++)
				for (int j = 0; j < boardDimensions; j++)
				{
					g2.setColor(new Color(0, 0, 0));

					double startX = i * 35.0 + j * 35.0 + 12.0;
					double startY = (boardDimensions - i) * 20.0 + j * 20.0 + 10.0;
					
					GeneralPath hex = new GeneralPath();
					if (i == boardDimensions - 1)
					{
						hex.moveTo(startX + 45.0, startY + 20.0);						
					}
					else
					{
						if (j == 0)
							hex.moveTo(startX + 35.0, startY + 00.0);							
						else
						{
							hex.moveTo(startX + 10.0, startY + 00.0);
							hex.lineTo(startX + 35.0, startY + 00.0);							
						}
						hex.lineTo(startX + 45.0, startY + 20.0);
					}
					if (j != boardDimensions - 1)
						hex.lineTo(startX + 35.0, startY + 40.0);
					
//					hex.lineTo(startX + 10.0, startY + 40.0);
//					hex.lineTo(startX + 00.0, startY + 20.0);
//					hex.closePath();
					
					g2.draw(hex);
					
					int field = board.getField(i, j);
					if (field != 0) {
						if (field == 1)
							g2.setColor(Color.white);
						else
							g2.setColor(Color.black);
						
						Ellipse2D piece = new Ellipse2D.Double(
								startX + 45.0/2 - 24.0/2,
								startY + 40.0/2 - 24.0/2,
								24.0, 24.0);
						
						g2.fill(piece);
						
						if (field == 1)
						{
							g2.setColor(Color.black);
							g2.draw(piece);
						}
					}
				}			
		}

		private void drawBoardBorder(Graphics2D g2)
		{
			int boardDimensions = board.getDimensions();

			GeneralPath border1 = new GeneralPath();
			
			double startX = boardDimensions * 35.0 + 2;
			double startY = 30.0;
			
			border1.moveTo(startX - 2, startY - 10.0);
			border1.lineTo(startX - 2, startY);
			border1.lineTo(startX + 10.0, startY);
			
			for (int j = 1; j < boardDimensions; j++) {
				border1.lineTo(startX - 15.0 + 35.0 * j, startY + 20.0 * j);					
				border1.lineTo(startX - 15.0 + 35.0 * j + 25.0, startY + 20.0 * j);
			}
			
			border1.lineTo(startX - 15.0 + 35.0 * boardDimensions, startY + 20.0 * boardDimensions);					
			border1.lineTo(startX - 15.0 + 35.0 * boardDimensions + 12, startY + 20.0 * boardDimensions);

			for (int j = boardDimensions - 1; j > 0; j--) {
				border1.lineTo(startX + 10.0 - 18.0 + 35.0 * j + 25.0, startY - 10.0 + 20.0 * j);
				border1.lineTo(startX + 10.0 - 18.0 + 35.0 * j, startY - 10.0 + 20.0 * j);					
			}

			border1.lineTo(startX + 10.0 - 18.0 + 25.0, startY - 10.0);
			border1.closePath();					

			g2.setColor(Color.white);
			g2.fill(border1);
			g2.setColor(Color.black);
			g2.draw(border1);

			////////
			
			GeneralPath border2 = new GeneralPath();
			
			startX = boardDimensions * 35.0 + 2;
			startY = 30.0 + boardDimensions * 40;
			
			border2.moveTo(startX - 2, startY + 10.0);
			border2.lineTo(startX - 2, startY);
			border2.lineTo(startX + 10.0, startY);
			
			for (int j = 1; j < boardDimensions; j++) {
				border2.lineTo(startX - 15.0 + 35.0 * j, startY - 20.0 * j);					
				border2.lineTo(startX - 15.0 + 35.0 * j + 25.0, startY - 20.0 * j);
			}
			
			border2.lineTo(startX - 15.0 + 35.0 * boardDimensions, startY - 20.0 * boardDimensions);					
			border2.lineTo(startX - 15.0 + 35.0 * boardDimensions + 12, startY - 20.0 * boardDimensions);

			for (int j = boardDimensions - 1; j > 0; j--) {
				border2.lineTo(startX + 10.0 - 18.0 + 35.0 * j + 25.0, startY + 10.0 - 20.0 * j);
				border2.lineTo(startX + 10.0 - 18.0 + 35.0 * j, startY + 10.0 - 20.0 * j);					
			}

			border2.lineTo(startX + 10.0 - 18.0 + 25.0, startY + 10.0);
			border2.closePath();					

			g2.setColor(Color.black);
			g2.fill(border2);
			g2.setColor(Color.black);
			g2.draw(border2);
			
			////////
			
			GeneralPath border3 = new GeneralPath();
			
			startX = boardDimensions * 35.0 - 3;
			startY = 30.0 + boardDimensions * 40;
			
			border3.moveTo(startX + 3, startY + 10.0);
			border3.lineTo(startX + 3, startY);
			border3.lineTo(startX - 10.0, startY);
			
			for (int j = 1; j < boardDimensions; j++) {
				border3.lineTo(startX + 15.0 - 35.0 * j, startY - 20.0 * j);					
				border3.lineTo(startX + 15.0 - 35.0 * j - 25.0, startY - 20.0 * j);
			}
			
			border3.lineTo(startX + 15.0 - 35.0 * boardDimensions, startY - 20.0 * boardDimensions);					
			border3.lineTo(startX + 15.0 - 35.0 * boardDimensions - 12, startY - 20.0 * boardDimensions);

			for (int j = boardDimensions - 1; j > 0; j--) {
				border3.lineTo(startX - 10.0 + 18.0 - 35.0 * j - 25.0, startY + 10.0 - 20.0 * j);
				border3.lineTo(startX - 10.0 + 18.0 - 35.0 * j, startY + 10.0 - 20.0 * j);					
			}

			border3.lineTo(startX - 10.0 + 18.0 - 25.0, startY + 10.0);
			border3.closePath();					

			g2.setColor(Color.white);
			g2.fill(border3);
			g2.setColor(Color.black);
			g2.draw(border3);
			
			////////
			
			GeneralPath border4 = new GeneralPath();
			
			startX = boardDimensions * 35.0 - 3;
			startY = 30.0;
			
			border4.moveTo(startX + 3, startY - 10.0);
			border4.lineTo(startX + 3, startY);
			border4.lineTo(startX - 10.0, startY);
			
			for (int j = 1; j < boardDimensions; j++) {
				border4.lineTo(startX + 15.0 - 35.0 * j, startY + 20.0 * j);					
				border4.lineTo(startX + 15.0 - 35.0 * j - 25.0, startY + 20.0 * j);
			}
			
			border4.lineTo(startX + 15.0 - 35.0 * boardDimensions, startY + 20.0 * boardDimensions);					
			border4.lineTo(startX + 15.0 - 35.0 * boardDimensions - 12, startY + 20.0 * boardDimensions);

			for (int j = boardDimensions - 1; j > 0; j--) {
				border4.lineTo(startX - 10.0 + 18.0 - 35.0 * j - 25.0, startY - 10.0 + 20.0 * j);
				border4.lineTo(startX - 10.0 + 18.0 - 35.0 * j, startY - 10.0 + 20.0 * j);					
			}

			border4.lineTo(startX - 10.0 + 18.0 - 25.0, startY - 10.0);
			border4.closePath();					

			g2.setColor(Color.black);
			g2.fill(border4);
			g2.setColor(Color.black);
			g2.draw(border4);

		}

		
		private int[] mouseDownAt;
		
		public void mousePressed(MouseEvent e)
		{
			Rectangle2D mousePoint = new Rectangle2D.Double(e.getX(), e.getY(), 1, 1); 

			int boardDimensions = board.getDimensions();
			for (int i = 0; i < boardDimensions; i++)
				for (int j = 0; j < boardDimensions; j++)
				{										
					if (hexagons[i][j].intersects(mousePoint))
					{
						mouseDownAt = new int[] { i, j };
						return;
					}
				}
		}
		
		/**
		 * Handles the mouse clicks
		 */
		public void mouseReleased(MouseEvent e)
		{
			Rectangle2D mousePoint = new Rectangle2D.Double(e.getX(), e.getY(), 1, 1); 
			int x = -1, y = -1;
			
			int boardDimensions = board.getDimensions();
			hexLoop: for (int i = 0; i < boardDimensions; i++)
				for (int j = 0; j < boardDimensions; j++)
				{										
					if (hexagons[i][j].intersects(mousePoint))
					{
						x = i;
						y = j;
						break hexLoop;
					}
				}
			
			// Make sure that we don't move into other hex
			if (x != mouseDownAt[0] || y != mouseDownAt[1])
				return;
			
			if (x >= 0 && x < boardDimensions &&
					y >= 0 && y < boardDimensions &&
					activePlayer != null &&
					activePlayer.getClass().getName() == "Players.HumanPlayer")
			{
				int[] move = {x, y};
				
				HumanPlayer human = (HumanPlayer)activePlayer;
				if (human.setNextMove(move))
					activePlayer = null;
			}
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			
		}
	}
}
