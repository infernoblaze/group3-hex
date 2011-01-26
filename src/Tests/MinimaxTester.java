/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Tests;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Game.Game;
import Players.*;

/**
 *
 * @author Leoni
 */
public class MinimaxTester
{
    public static void main(String[] args)
    {
        final int[] stats = {0, 0, 0};

        for (int i = 0; i < 10; i++)
        {
            System.out.println("Game number " + (i + 1));

            //Player one = new MonteCarloPlayer();
            Player one = new NegaMaxPlayer(2);
            Player two = new MonteCarloPlayer();

            Game tempGame;
            if (i % 2 == 0)
                tempGame = new Game(5, one, two);
            else
                tempGame = new Game(5, two, one);

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
