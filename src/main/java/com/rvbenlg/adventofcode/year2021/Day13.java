package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 {

    /*
    --- Day 13: Transparent Origami ---
    You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal imaging so you could tell ahead of time which caves are too hot to safely enter.

    Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:

    Congratulations on your purchase! To activate this infrared thermal imaging
    camera system, please enter the code found on page 1 of the manual.
    Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input). For example:

    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along y=7
    fold along x=5
    The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where # is a dot on the paper and . is an empty, unmarked position:

    ...#..#..#.
    ....#......
    ...........
    #..........
    ...#....#.#
    ...........
    ...........
    ...........
    ...........
    ...........
    .#....#.##.
    ....#......
    ......#...#
    #..........
    #.#........
    Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked here with -):

    ...#..#..#.
    ....#......
    ...........
    #..........
    ...#....#.#
    ...........
    ...........
    -----------
    ...........
    ...........
    .#....#.##.
    ....#......
    ......#...#
    #..........
    #.#........
    Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:

    #.##..#..#.
    #...#......
    ......#...#
    #...#......
    .#.#..#.###
    ...........
    ...........
    Now, only 17 dots are visible.

    Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.

    Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.

    The second fold instruction is fold along x=5, which indicates this line:

    #.##.|#..#.
    #...#|.....
    .....|#...#
    #...#|.....
    .#.#.|#.###
    .....|.....
    .....|.....
    Because this is a vertical line, fold left:

    #####
    #...#
    #...#
    #...#
    #####
    .....
    .....
    The instructions made a square!

    The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single dot.

    How many dots are visible after completing just the first fold instruction on your transparent paper?

    --- Part Two ---
    Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.

    What code do you use to activate the infrared thermal imaging camera system?
     */

    private boolean[][] markedPositions = new boolean[0][0];
    private int maxX = 0;
    private int maxY = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day13.txt");
        List<String> marksCoordinates = getMarksCoordinates(input);
        List<String> foldInstructions = getFoldInstructions(input);
        parseMaxValues(marksCoordinates);
        fillArray();
        markPositions(marksCoordinates);
        followFoldInstructions(Collections.singletonList(foldInstructions.get(0)));
        int result = howManyDotsAreVisible();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day13.txt");
        List<String> marksCoordinates = getMarksCoordinates(input);
        List<String> foldInstructions = getFoldInstructions(input);
        parseMaxValues(marksCoordinates);
        fillArray();
        markPositions(marksCoordinates);
        followFoldInstructions(foldInstructions);
        System.out.println("Part 2 solution: " + solutionToPrint());
    }

    private String solutionToPrint() {
        StringBuilder solutionBuilder = new StringBuilder();
        solutionBuilder.append("\n");
        for(int i = 0; i < maxY; i++) {
            for(int j = 0; j < maxX; j++) {
                if(markedPositions[i][j]) {
                    solutionBuilder.append("#");
                } else {
                    solutionBuilder.append(".");
                }
            }
            if(i < maxY - 1) {
                solutionBuilder.append("\n");
            }
        }
        return solutionBuilder.toString();
    }

    private int howManyDotsAreVisible() {
        int result = 0;
        for(int i = 0; i < markedPositions.length; i++) {
            for(int j = 0; j < markedPositions[i].length; j++) {
                if(markedPositions[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    private void followFoldInstructions(List<String> foldInstructions) {
        for (String instruction : foldInstructions) {
            instruction = instruction.replaceAll("fold along ", "");
            int value = Integer.parseInt(instruction.substring(instruction.indexOf("=") + 1));
            if (instruction.startsWith("x=")) {
                foldAlongX(value);
            } else {
                foldAlongY(value);
            }
        }
    }

    private void foldAlongX(int column) {
        maxX = column;
        for (int i = 1; i <= markedPositions[0].length / 2; i++) {
            for (int j = 0; j < markedPositions.length; j++) {
                if (markedPositions[j][column + i]) {
                    markedPositions[j][column - i] = true;
                    markedPositions[j][column + i] = false;
                }
            }
        }
    }

    private void foldAlongY(int row) {
        maxY = row;
        for (int i = 1; i <= markedPositions.length / 2; i++) {
            for (int j = 0; j < markedPositions[row + i].length; j++) {
                if (markedPositions[row + i][j]) {
                    markedPositions[row - i][j] = true;
                    markedPositions[row + i][j] = false;
                }
            }
        }
    }

    private void markPositions(List<String> marksCoordinates) {
        for (String markCoordinates : marksCoordinates) {
            String[] values = markCoordinates.split(",");
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            markedPositions[y][x] = true;
        }
    }

    private void fillArray() {
        markedPositions = new boolean[maxY + 1][maxX + 1];
        for (int i = 0; i < markedPositions.length; i++) {
            for (int j = 0; j < markedPositions[i].length; j++) {
                markedPositions[i][j] = false;
            }
        }
    }

    private void parseMaxValues(List<String> foldInstructions) {
        for (String pair : foldInstructions) {
            String[] values = pair.split(",");
            int currentX = Integer.parseInt(values[0]);
            int currentY = Integer.parseInt(values[1]);
            if (currentX > maxX) {
                maxX = currentX;
            }
            if (currentY > maxY) {
                maxY = currentY;
            }
        }
    }

    private List<String> getFoldInstructions(List<String> input) {
        List<String> result = new ArrayList<>();
        boolean add = false;
        for (int i = 0; i < input.size(); i++) {
            if (!input.get(i).isEmpty() && add) {
                result.add(input.get(i));
            } else if (input.get(i).isEmpty()) {
                add = true;
            }
        }
        return result;
    }

    private List<String> getMarksCoordinates(List<String> input) {
        List<String> result = new ArrayList<>();
        boolean done = false;
        for (int i = 0; i < input.size() && !done; i++) {
            if (!input.get(i).isEmpty()) {
                result.add(input.get(i));
            } else {
                done = true;
            }
        }
        return result;
    }

    private void resetVariables() {
        markedPositions = new boolean[0][0];
        maxX = 0;
        maxY = 0;
    }


}
