/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Game.Game;
import Players.MinimaxPlayer;
import Players.MonteCarloPlayer;
import Players.Player;

/**
 *
 * @author Leoni
 */
public class MonteCarloTest
{
    public static void main(String[] args)
    {
        final int[] stats = {0, 0, 0};

        for (int i = 0; i < 10; i++)
        {
            System.out.println("Game number " + (i + 1));

            Player one = new MonteCarloPlayer(1.0f, 300000, 15000, false);
            //Player one = new MinimaxPlayer();
            Player two = new MonteCarloPlayer(1.0f, 200000, 10000, false);

            Game tempGame;
            if (i % 2 == 0)
                tempGame = new Game(8, one, two);
            else
                tempGame = new Game(8, two, one);

            final Game game = tempGame;

            final int turnNumber = i;
            game.addEndGameListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                        int player;
                        if (turnNumber % 2 == 0)
                                player = game.getGameWinner();
                        else
                                player = (game.getGameWinner() == 1 ? 2 : 1);

                        stats[player]++;
                        //System.out.println("Player " + player + " won!\n\n");
                }
            });
            game.run();
        }
        System.out.println("White: " + stats[1]);
        System.out.println("Black: " + stats[2]);
    }
}
