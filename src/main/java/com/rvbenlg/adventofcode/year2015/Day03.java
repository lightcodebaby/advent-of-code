package com.rvbenlg.adventofcode.year2015;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day03 {

    /*
    --- Day 3: Perfectly Spherical Houses in a Vacuum ---
    Santa is delivering presents to an infinite two-dimensional grid of houses.

    He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

    However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

    For example:

    > delivers presents to 2 houses: one at the starting location, and one to the east.
    ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
    ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.

    --- Part Two ---
    The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

    Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

    This year, how many houses receive at least one present?

    For example:

    ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
    ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
    ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
     */

    private HashMap<Integer, List<Integer>> locations = new HashMap<>();
    private int santaNorth = 0;
    private int santaEast = 0;
    private int robotNorth = 0;
    private int robotEast = 0;
    private boolean santaMoving = true;
    int origen = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2015/day03.txt");
        resetVariables();
        int result = 0;
        for (String line : lines) {
            followInstructions(line);
            result = howManyHouses();
        }
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2015/day03.txt");
        resetVariables();
        int result = 0;
        for (String line : lines) {
            followInstructions2(line);
            result = howManyHouses();
        }
        System.out.println("Part 2 solution: " + result);
    }

    private void followInstructions(String instructions) {
        givePresentAtStartingLocation();
        for (char instruction : instructions.toCharArray()) {
            if (instruction == '^') {
                santaNorth++;
                deliverSanta();
            } else if (instruction == 'v') {
                santaNorth--;
                deliverSanta();
            } else if (instruction == '>') {
                santaEast++;
                deliverSanta();
            } else if (instruction == '<') {
                santaEast--;
                deliverSanta();
            }
        }
    }

    private void followInstructions2(String instructions) {
        givePresentAtStartingLocation();
        for (char instruction : instructions.toCharArray()) {
            if (instruction == '^') {
                goNorth();
            } else if (instruction == 'v') {
                goSouth();
            } else if (instruction == '>') {
                goEast();
            } else if (instruction == '<') {
                goWest();
            }
            santaMoving = !santaMoving;
        }
    }

    private void goNorth() {
        if (santaMoving) {
            santaNorth++;
            deliverSanta();
        } else {
            robotNorth++;
            deliverRobot();
        }
    }

    private void goSouth() {
        if (santaMoving) {
            santaNorth--;
            deliverSanta();
        } else {
            robotNorth--;
            deliverRobot();
        }
    }

    private void goEast() {
        if (santaMoving) {
            santaEast++;
            deliverSanta();
        } else {
            robotEast++;
            deliverRobot();
        }
    }

    private void goWest() {
        if (santaMoving) {
            santaEast--;
            deliverSanta();
        } else {
            robotEast--;
            deliverRobot();
        }
    }

    private void deliverSanta() {
        if (locations.containsKey(santaNorth)) {
            if (!locations.get(santaNorth).contains(santaEast)) {
                locations.get(santaNorth).add(santaEast);
            }
        } else {
            locations.put(santaNorth, new ArrayList<>());
            locations.get(santaNorth).add(santaEast);
        }
    }

    private void deliverRobot() {
        if (locations.containsKey(robotNorth)) {
            if (!locations.get(robotNorth).contains(robotEast)) {
                locations.get(robotNorth).add(robotEast);
            }
        } else {
            locations.put(robotNorth, new ArrayList<>());
            locations.get(robotNorth).add(robotEast);
        }
    }

    private void givePresentAtStartingLocation() {
        List<Integer> origenEast = new ArrayList<>();
        origenEast.add(origen);
        locations.put(origen, origenEast);
    }

    private int howManyHouses() {
        int result = 0;
        for (int key : locations.keySet()) {
            result += locations.get(key).stream().distinct().count();
        }
        return result;
    }

    private void resetVariables() {
        locations = new HashMap<>();
        santaNorth = 0;
        santaEast = 0;
        origen = 0;
        robotNorth = 0;
        robotEast = 0;
        santaMoving = true;
    }

}
