package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Day01 {

    /*
    --- Day 1: No Time for a Taxicab ---
    Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.

    Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

    You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.

    The Document indicates that you should start at the given coordinates (where you just landed) and face North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.

    There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination. Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?

    For example:

    Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
    R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
    R5, L5, R5, R3 leaves you 12 blocks away.
    How many blocks away is Easter Bunny HQ?

    --- Part Two ---
    Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.

    For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.

    How many blocks away is the first location you visit twice?
     */

    private int lookingAt = 0;
    private int north = 0;
    private int east = 0;
    private Hashtable<Integer, List<Integer>> locations = new Hashtable<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day01.txt");
        for (String line : input) {
            String[] instructions = line.replace(" ", "").split(",");
            for (String instruction : instructions) {
                followInstruction(instruction);
            }
            System.out.println("Part 1 solution: " + (Math.abs(east) + Math.abs(north)));
        }
    }

    private void resetVariables() {
        lookingAt = 0;
        north = 0;
        east = 0;
        locations = new Hashtable<>();
    }

    private void followInstruction(String instruction) {
        whereToLook(instruction);
        int steps = howManySteps(instruction);
        walk(steps);
    }

    private void whereToLook(String instruction) {
        if (instruction.charAt(0) == 'R') {
            lookingAt = (lookingAt + 1) % 4;
        } else {
            lookingAt = (lookingAt + 3) % 4;
        }
    }

    private void walk(int steps) {
        if (lookingAt == 0) {
            north += steps;
        }
        if (lookingAt == 1) {
            east += steps;
        }
        if (lookingAt == 2) {
            north -= steps;
        }
        if (lookingAt == 3) {
            east -= steps;
        }
    }

    private int howManySteps(String instruction) {
        return Integer.parseInt(instruction.substring(1));
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day01.txt");
        for (String line : input) {
            String[] instructions = line.replace(" ", "").split(",");
            boolean alreadyVisited = false;
            for (int i = 0; i < instructions.length && !alreadyVisited; i++) {
                alreadyVisited = visitLocation2(instructions[i]);
            }
            System.out.println("Part 2 solution: " + (Math.abs(east) + Math.abs(north)));
        }
    }

    private boolean visitLocation2(String instruction) {
        whereToLook(instruction);
        int steps = howManySteps(instruction);
        return walkStepByStep(steps);
    }

    private boolean walkStepByStep(int steps) {
        boolean alreadyVisited = false;
        for (int i = 0; i < steps && !alreadyVisited; i++) {
            if (lookingAt == 0) {
                walkNorth();
            }
            if (lookingAt == 1) {
                walkEast();
            }
            if (lookingAt == 2) {
                walkSouth();
            }
            if (lookingAt == 3) {
                walkWest();
            }
            alreadyVisited = checkIfAlreadyVisited();
            visitLocation();
        }
        return alreadyVisited;
    }

    private void visitLocation() {
        if (!locations.containsKey(north)) {
            locations.put(north, new ArrayList<>());
        }
        locations.get(north).add(east);
    }

    private boolean checkIfAlreadyVisited() {
        return locations.containsKey(north) && locations.get(north).contains(east);
    }

    private void walkNorth() {
        north++;
    }

    private void walkEast() {
        east++;
    }

    private void walkSouth() {
        north--;
    }

    private void walkWest() {
        east--;
    }
}
