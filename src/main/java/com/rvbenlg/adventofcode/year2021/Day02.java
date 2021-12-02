package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day02 {

    private int horizontal = 0;
    private int depth = 0;
    private int aim = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input  = Utilities.readInput("year2021/day02.txt");
        int result = move(input);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input  = Utilities.readInput("year2021/day02.txt");
        int result = move2(input);
        System.out.println("Part 2 solution: " + result);
    }

    private int move(List<String> input) {
        for(String movement : input) {
            String[] parts = movement.split(" ");
            String howToMove = parts[0];
            String howLongToMove = parts[1];
            switch (howToMove) {
                case "forward":
                    moveForward(howLongToMove);
                    break;
                case "down":
                    moveDown(howLongToMove);
                    break;
                case "up":
                    moveUp(howLongToMove);
                    break;
            }
        }
        return horizontal * depth;
    }

    private void moveForward(String howLongToMove) {
        horizontal += Integer.parseInt(howLongToMove);
    }

    private void moveDown(String howLongToMove) {
        depth += Integer.parseInt(howLongToMove);
    }

    private void moveUp(String howLongToMove) {
        depth -= Integer.parseInt(howLongToMove);
    }

    private int move2(List<String> input) {
        for(String movement : input) {
            String[] parts = movement.split(" ");
            String howToMove = parts[0];
            String howLongToMove = parts[1];
            switch (howToMove) {
                case "forward":
                    moveForward2(howLongToMove);
                    break;
                case "down":
                    moveDown2(howLongToMove);
                    break;
                case "up":
                    moveUp2(howLongToMove);
                    break;
            }
        }
        return horizontal * depth;
    }

    private void moveForward2(String howLongToMove) {
        horizontal += Integer.parseInt(howLongToMove);
        depth += Integer.parseInt(howLongToMove) * aim;
    }

    private void moveDown2(String howLongToMove) {
        aim += Integer.parseInt(howLongToMove);
    }

    private void moveUp2(String howLongToMove) {
        aim -= Integer.parseInt(howLongToMove);
    }

    private void resetVariables() {
        horizontal = 0;
        depth = 0;
        aim = 0;
    }

}
