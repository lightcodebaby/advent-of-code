package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day21 {

    /*
    --- Day 21: Dirac Dice ---
    There's not much to do as you slowly descend to the bottom of the ocean. The submarine computer challenges you to a nice game of Dirac Dice.

    This game consists of a single die, two pawns, and a game board with a circular track containing ten spaces marked 1 through 10 clockwise. Each player's starting space is chosen randomly (your puzzle input). Player 1 goes first.

    Players take turns moving. On each player's turn, the player rolls the die three times and adds up the results. Then, the player moves their pawn that many times forward around the track (that is, moving clockwise on spaces in order of increasing value, wrapping back around to 1 after 10). So, if a player is on space 7 and they roll 2, 2, and 1, they would move forward 5 times, to spaces 8, 9, 10, 1, and finally stopping on 2.

    After each player moves, they increase their score by the value of the space their pawn stopped on. Players' scores start at 0. So, if the first player starts on space 7 and rolls a total of 5, they would stop on space 2 and add 2 to their score (for a total score of 2). The game immediately ends as a win for any player whose score reaches at least 1000.

    Since the first game is a practice game, the submarine opens a compartment labeled deterministic dice and a 100-sided die falls out. This die always rolls 1 first, then 2, then 3, and so on up to 100, after which it starts over at 1 again. Play using this die.

    For example, given these starting positions:

    Player 1 starting position: 4
    Player 2 starting position: 8
    This is how the game would go:

    Player 1 rolls 1+2+3 and moves to space 10 for a total score of 10.
    Player 2 rolls 4+5+6 and moves to space 3 for a total score of 3.
    Player 1 rolls 7+8+9 and moves to space 4 for a total score of 14.
    Player 2 rolls 10+11+12 and moves to space 6 for a total score of 9.
    Player 1 rolls 13+14+15 and moves to space 6 for a total score of 20.
    Player 2 rolls 16+17+18 and moves to space 7 for a total score of 16.
    Player 1 rolls 19+20+21 and moves to space 6 for a total score of 26.
    Player 2 rolls 22+23+24 and moves to space 6 for a total score of 22.
    ...after many turns...

    Player 2 rolls 82+83+84 and moves to space 6 for a total score of 742.
    Player 1 rolls 85+86+87 and moves to space 4 for a total score of 990.
    Player 2 rolls 88+89+90 and moves to space 3 for a total score of 745.
    Player 1 rolls 91+92+93 and moves to space 10 for a final score, 1000.
    Since player 1 has at least 1000 points, player 1 wins and the game ends. At this point, the losing player had 745 points and the die had been rolled a total of 993 times; 745 * 993 = 739785.

    Play a practice game using the deterministic 100-sided die. The moment either player wins, what do you get if you multiply the score of the losing player by the number of times the die was rolled during the game?

     --- Part Two ---
    Now that you're warmed up, it's time to play the real game.

    A second compartment opens, this time labeled Dirac dice. Out of it falls a single three-sided die.

    As you experiment with the die, you feel a little strange. An informational brochure in the compartment explains that this is a quantum die: when you roll it, the universe splits into multiple copies, one copy for each possible outcome of the die. In this case, rolling the die always splits the universe into three copies: one where the outcome of the roll was 1, one where it was 2, and one where it was 3.

    The game is played the same as before, although to prevent things from getting too far out of hand, the game now ends when either player's score reaches at least 21.

    Using the same starting positions as in the example above, player 1 wins in 444356092776315 universes, while player 2 merely wins in 341960390180808 universes.

    Using your given starting positions, determine every possible outcome. Find the player that wins in more universes; in how many universes does that player win?
     */

    private Player[] players = new Player[0];
    private int multiply = 2;


    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day21.txt");
        parsePlayers(input);
        play();
        int result = calculateResult();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day21.txt");
        parsePlayers(input);
        long result = playInTheMultiverse();
        System.out.println("Part 2 solution: " + result);
    }

    private int calculateResult() {
        int rolls = multiply - 2;
        Player loser = players[0].score >= 1000 ? players[1] : players[0];
        return rolls * loser.score;
    }

    private void play() {
        Player firstPlayer = players[0];
        Player secondPlayer = players[1];
        boolean playFirstPlayer = true;
        while (firstPlayer.score < 1000 && secondPlayer.score < 1000) {
            if (playFirstPlayer) {
                firstPlayer.position = (firstPlayer.position + (multiply * 3)) % 10;
                updateScore(firstPlayer);
            } else {
                secondPlayer.position = (secondPlayer.position + (multiply * 3)) % 10;
                updateScore(secondPlayer);
            }
            multiply += 3;
            playFirstPlayer = !playFirstPlayer;
        }
    }

    private long playInTheMultiverse() {
        List<Universe> multiverse = getMultiverse();
        boolean playFirstPlayer = true;
        long firstPlayerWins = 0L;
        long secondPlayerWins = 0L;
        while (!multiverse.isEmpty()) {
            List<Universe> newUniverses = new ArrayList<>();
            for (Universe universe : multiverse) {
                newUniverses.addAll(getNewUniverses(universe, playFirstPlayer));
            }
            List<Universe>[] splittedMultiverse = splitUniverses(newUniverses);
            List<Universe> finishedUniverses = splittedMultiverse[0];
            List<Universe> unfinishedUniverses = splittedMultiverse[1];
            multiverse = unfinishedUniverses;
            for(Universe finishedUniverse : finishedUniverses) {
                if(playFirstPlayer) {
                    firstPlayerWins += finishedUniverse.repeated;
                } else {
                    secondPlayerWins += finishedUniverse.repeated;
                }
            }
            playFirstPlayer = !playFirstPlayer;
        }
        return Math.max(firstPlayerWins, secondPlayerWins);
    }

    private List<Universe>[] splitUniverses(List<Universe> multiverse) {
        List<Universe> finished = new ArrayList<>();
        List<Universe> unfinished = new ArrayList<>();
        for(Universe universe : multiverse) {
            if(universe.firstPlayer.score >= 21 || universe.secondPlayer.score >= 21) {
                finished.add(universe);
            } else {
                unfinished.add(universe);
            }
        }
        List<Universe>[] result = new List[2];
        result[0] = finished;
        result[1] = unfinished;
        return result;
    }

    private List<Universe> getNewUniverses(Universe universe, boolean playFirstPlayer) {
        List<Universe> newUniverses = new ArrayList<>();
        for (int i = 3; i <= 9; i++) {
            newUniverses.add(getNewUniverse(i, playFirstPlayer, universe));
        }
        return newUniverses;
    }

    private Universe getNewUniverse(int rollsSum, boolean playFirstPlayer, Universe universe) {
        Universe result = null;
        Player newFirstPlayer;
        Player newSecondPlayer;
        if (playFirstPlayer) {
            newFirstPlayer = updatePlayer(universe.firstPlayer, rollsSum);
            newSecondPlayer = universe.secondPlayer;
        } else {
            newFirstPlayer = universe.firstPlayer;
            newSecondPlayer = updatePlayer(universe.secondPlayer, rollsSum);
        }
        switch (rollsSum) {
            case 3:
            case 9:
                result = new Universe(newFirstPlayer, newSecondPlayer, universe.repeated);
                break;
            case 4:
            case 8:
                result = new Universe(newFirstPlayer, newSecondPlayer, universe.repeated * 3);
                break;
            case 5:
            case 7:
                result = new Universe(newFirstPlayer, newSecondPlayer, universe.repeated * 6);
                break;
            case 6:
                result = new Universe(newFirstPlayer, newSecondPlayer, universe.repeated * 7);
                break;
        }
        return result;
    }

    private Player updatePlayer(Player oldPlayer, int rollsSum) {
        Player newPlayer = new Player((oldPlayer.position + rollsSum) % 10, oldPlayer.score);
        updateScore(newPlayer);
        return newPlayer;
    }

    private List<Universe> getMultiverse() {
        Player firstPlayer = players[0];
        Player secondPlayer = players[1];
        Universe firstUniverse = new Universe(firstPlayer, secondPlayer, 1L);
        List<Universe> multiverse = new ArrayList<>();
        multiverse.add(firstUniverse);
        return multiverse;
    }

    private void updateScore(Player player) {
        if (player.position != 0) {
            player.score += player.position;
        } else {
            player.score += 10;
        }
    }

    private void parsePlayers(List<String> input) {
        players = new Player[input.size()];
        for (int i = 0; i < input.size(); i++) {
            String playerDescription = input.get(i);
            String strPosition = playerDescription.substring(playerDescription.indexOf(":") + 2);
            players[i] = new Player(Integer.parseInt(strPosition), 0);
        }
    }

    private class Universe {
        Player firstPlayer;
        Player secondPlayer;
        long repeated;

        private Universe(Player firstPlayer, Player secondPlayer, long repeated) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
            this.repeated = repeated;
        }
    }

    private class Player {
        int position;
        int score;

        private Player(int position, int score) {
            this.position = position;
            this.score = score;
        }

    }

}
