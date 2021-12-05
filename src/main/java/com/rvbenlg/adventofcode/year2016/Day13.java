package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 {

    /*
    --- Day 13: A Maze of Twisty Little Cubicles ---
    You arrive at the first floor of this new building to discover a much less welcoming environment than the shiny atrium of the last one. Instead, you are in a maze of twisty little cubicles, all alike.

    Every location in this area is addressed by a pair of non-negative integers (x,y). Each such coordinate is either a wall or an open space. You can't move diagonally. The cube maze starts at 0,0 and seems to extend infinitely toward positive x and y; negative values are invalid, as they represent a location outside the building. You are in a small waiting area at 1,1.

    While it seems chaotic, a nearby morale-boosting poster explains, the layout is actually quite logical. You can determine whether a given x,y coordinate will be a wall or an open space using a simple system:

    Find x*x + 3*x + 2*x*y + y + y*y.
    Add the office designer's favorite number (your puzzle input).
    Find the binary representation of that sum; count the number of bits that are 1.
    If the number of bits that are 1 is even, it's an open space.
    If the number of bits that are 1 is odd, it's a wall.
    For example, if the office designer's favorite number were 10, drawing walls as # and open spaces as ., the corner of the building containing 0,0 would look like this:

      0123456789
    0 .#.####.##
    1 ..#..#...#
    2 #....##...
    3 ###.#.###.
    4 .##..#..#.
    5 ..##....#.
    6 #...##.###
    Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:

      0123456789
    0 .#.####.##
    1 .O#..#...#
    2 #OOO.##...
    3 ###O#.###.
    4 .##OO#OO#.
    5 ..##OOO.#.
    6 #...##.###
    Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).

    What is the fewest number of steps required for you to reach 31,39?

    --- Part Two ---
    How many locations (distinct x,y coordinates, including your starting location) can you reach in at most 50 steps?
     */

    private int destinationX = 31;
    private int destinationY = 39;
    Set<Position> positions = new HashSet<>();
    private int officerDesignerFavoriteNumber = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day13.txt");
        initializePositions();
        int result = 0;
        for (String line : input) {
            result = move(line);
        }
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day13.txt");
        initializePositions();
        int result = 0;
        for (String line : input) {
            result = move50Times(line);
        }
        System.out.println("Part 2 solution: " + result);

    }

    private int move(String line) {
        officerDesignerFavoriteNumber = Integer.parseInt(line);
        int steps = 0;
        while (positions.stream().noneMatch(position -> position.x == destinationX && position.y == destinationY)) {
            List<Position> toExplore = positions.stream().filter(position -> position.toExplore).collect(Collectors.toList());
            steps++;
            for (Position position : toExplore) {
                position.toExplore = false;
                if (canGoUp(position)) {
                    positions.add(new Position(position.x, position.y - 1, true));
                }
                if (canGoDown(position)) {
                    positions.add(new Position(position.x, position.y + 1, true));
                }
                if (canGoLeft(position)) {
                    positions.add(new Position(position.x - 1, position.y, true));
                }
                if (canGoRight(position)) {
                    positions.add(new Position(position.x + 1, position.y, true));
                }
            }
        }
        return steps;
    }

    private int move50Times(String line) {
        officerDesignerFavoriteNumber = Integer.parseInt(line);
        int steps = 0;
        while (steps < 50) {
            List<Position> toExplore = positions.stream().filter(position -> position.toExplore).collect(Collectors.toList());
            steps++;
            for (Position position : toExplore) {
                position.toExplore = false;
                if (canGoUp(position)) {
                    positions.add(new Position(position.x, position.y - 1, true));
                }
                if (canGoDown(position)) {
                    positions.add(new Position(position.x, position.y + 1, true));
                }
                if (canGoLeft(position)) {
                    positions.add(new Position(position.x - 1, position.y, true));
                }
                if (canGoRight(position)) {
                    positions.add(new Position(position.x + 1, position.y, true));
                }
            }
        }
        return positions.size();
    }


    private void initializePositions() {
        positions = new HashSet<>();
        positions.add(new Position(1, 1, true));
    }


    private boolean canGoUp(Position position) {
        boolean result = false;
        if (position.y - 1 >= 0 && positions.stream().noneMatch(pos -> position.x == pos.x && pos.y == position.y - 1)) {
            result = isOpen(toBinary(formula(position.x, position.y - 1) + officerDesignerFavoriteNumber));
        }
        return result;
    }

    private boolean canGoDown(Position position) {
        boolean result = false;
        if (position.y + 1 >= 0 && positions.stream().noneMatch(pos -> position.x == pos.x && pos.y == position.y + 1)) {
            result = isOpen(toBinary(formula(position.x, position.y + 1) + officerDesignerFavoriteNumber));
        }
        return result;
    }


    private boolean canGoLeft(Position position) {
        boolean result = false;
        if (position.x - 1 >= 0 && positions.stream().noneMatch(pos -> position.x - 1 == pos.x && pos.y == position.y)) {
            result = isOpen(toBinary(formula(position.x - 1, position.y) + officerDesignerFavoriteNumber));
        }
        return result;
    }

    private boolean canGoRight(Position position) {
        boolean result = false;
        if (position.x + 1 >= 0 && positions.stream().noneMatch(pos -> position.x + 1 == pos.x && pos.y == position.y)) {
            result = isOpen(toBinary(formula(position.x + 1, position.y) + officerDesignerFavoriteNumber));
        }
        return result;
    }

    private int formula(int x, int y) {
        return (x * x) + (3 * x) + (2 * x * y) + y + (y * y);
    }

    private String toBinary(int number) {
        return Integer.toBinaryString(number);
    }

    private boolean isOpen(String binary) {
        return binary.chars().filter(c -> c == '1').count() % 2 == 0;
    }


    private class Position {
        private int x;
        private int y;
        private boolean toExplore;

        private Position(int x, int y, boolean toExplore) {
            this.x = x;
            this.y = y;
            this.toExplore = toExplore;
        }
    }

}
