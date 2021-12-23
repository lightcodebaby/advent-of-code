package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class Day21 {

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

    private void part2() {
        BigInteger universes = new BigInteger("1");
        for(int i = 0; i < 41; i++) {
            universes = universes.multiply(new BigInteger("3"));
        }
        BigInteger tercio = universes.divide(new BigInteger("3"));
        BigInteger dosTercios = tercio.multiply(new BigInteger("2"));
        System.out.println();
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
        while(firstPlayer.score < 1000 && secondPlayer.score < 1000) {
            if(playFirstPlayer) {
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

    private void updateScore(Player player) {
        if(player.position != 0) {
            player.score += player.position;
        } else {
            player.score += 10;
        }
    }

    private void parsePlayers(List<String> input) {
        players = new Player[input.size()];
        for(int i  = 0; i < input.size(); i++) {
            String playerDescription = input.get(i);
            String strPosition = playerDescription.substring(playerDescription.indexOf(":") + 2);
            players[i] = new Player(Integer.parseInt(strPosition));
        }
    }

    private class Player {
        int position;
        int score;

        private Player(int position) {
            this.position = position;
            this.score = 0;
        }
    }

}
