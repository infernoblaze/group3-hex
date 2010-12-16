/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Game.Game;
import Players.RandomPlayer;
import Players.Player;
import Players.RandomSimulationPlayer;
/**
 *
 * @author Leoni
 */
public class MonteRandomTest
{
    public static void main(String[] args)
    {
        final int[] stats = {0, 0, 0};

        for (int i = 0; i < 100; i++)
        {
            System.out.println("Game number " + (i + 1));

            Player two = new RandomPlayer();
            Player one = new RandomSimulationPlayer(new int[] { 1, 1, 10, 1 });

            Game tempGame;
            if (i % 2 == 0)
                tempGame = new Game(6, one, two);
            else
                tempGame = new Game(6, two, one);

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
