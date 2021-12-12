package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day05 {

    /*
    --- Day 5: Hydrothermal Venture ---
    You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.

    They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review. For example:

    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
    Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends. In other words:

    An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
    An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
    For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.

    So, the horizontal and vertical lines from the above list would produce the following diagram:

    .......1..
    ..1....1..
    ..1....1..
    .......1..
    .112111211
    ..........
    ..........
    ..........
    ..........
    222111....
    In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.

    To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.

    Consider only horizontal and vertical lines. At how many points do at least two lines overlap?

    --- Part Two ---
    Unfortunately, considering only horizontal and vertical lines doesn't give you the full picture; you need to also consider diagonal lines.

    Because of the limits of the hydrothermal vent mapping system, the lines in your list will only ever be horizontal, vertical, or a diagonal line at exactly 45 degrees. In other words:

    An entry like 1,1 -> 3,3 covers points 1,1, 2,2, and 3,3.
    An entry like 9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
    Considering all lines from the above example would now produce the following diagram:

    1.1....11.
    .111...2..
    ..2.1.111.
    ...1.2.2..
    .112313211
    ...1.2....
    ..1...1...
    .1.....1..
    1.......1.
    222111....
    You still need to determine the number of points where at least two lines overlap. In the above example, this is still anywhere in the diagram with a 2 or larger - now a total of 12 points.

    Consider all of the lines. At how many points do at least two lines overlap?
     */

    private List<Line> lines = new ArrayList<>();
    private int[][] matrix;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day05.txt");
        lines = parseHorizontalAndVerticalLines(input);
        int result = getTwoLinesOverlappedPoints(lines);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day05.txt");
        lines = parseAllLines(input);
        int result = getTwoLinesOverlappedPoints(lines);
        System.out.println("Part 2 solution: " + result);
    }

    private int getTwoLinesOverlappedPoints(List<Line> lines) {
        int result = 0;
        matrix = getMatrix(lines);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] >= 2) {
                    result++;
                }
            }
        }
        return result;
    }

    private int[][] getMatrix(List<Line> lines) {
        int maxX = getMaxX(lines);
        int maxY = getMaxY(lines);
        int[][] result = new int[Math.max(maxX, maxY) + 1][Math.max(maxX, maxY) + 1];
        for (Line line : lines) {
            int posX = line.initX;
            int posY = line.initY;
            while (posX != line.endX || posY != line.endY) {
                result[posY][posX]++;
                if (posX != line.endX) {
                    posX += posX < line.endX ? 1 : -1;
                }
                if (posY != line.endY) {
                    posY += posY < line.endY ? 1 : -1;
                }
            }
            result[posY][posX]++;
        }
        return result;
    }

    private int getMaxX(List<Line> lines) {
        int result = 0;
        for (Line line : lines) {
            if (line.initX > result) {
                result = line.initX;
            }
            if (line.endX > result) {
                result = line.endX;
            }
        }
        return result;
    }

    private int getMaxY(List<Line> lines) {
        int result = 0;
        for (Line line : lines) {
            if (line.initY > result) {
                result = line.initY;
            }
            if (line.endY > result) {
                result = line.endY;
            }
        }
        return result;
    }

    private List<Line> parseAllLines(List<String> input) {
        List<Line> lines = new ArrayList<>();
        for (String lineDescription : input) {
            lineDescription = lineDescription.replaceAll(" ", "");
            String[] parts = lineDescription.split("->");
            String[] firstPart = parts[0].split(",");
            String[] SecondPart = parts[1].split(",");
            int x1 = Integer.parseInt(firstPart[0]);
            int x2 = Integer.parseInt(SecondPart[0]);
            int y1 = Integer.parseInt(firstPart[1]);
            int y2 = Integer.parseInt(SecondPart[1]);
            lines.add(new Line(x1, y1, x2, y2));
        }
        return lines;
    }

    private List<Line> parseHorizontalAndVerticalLines(List<String> input) {
        List<Line> lines = new ArrayList<>();
        for (String lineDescription : input) {
            lineDescription = lineDescription.replaceAll(" ", "");
            String[] parts = lineDescription.split("->");
            String[] firstPart = parts[0].split(",");
            String[] SecondPart = parts[1].split(",");
            int x1 = Integer.parseInt(firstPart[0]);
            int x2 = Integer.parseInt(SecondPart[0]);
            int y1 = Integer.parseInt(firstPart[1]);
            int y2 = Integer.parseInt(SecondPart[1]);
            if (x1 == x2 || y1 == y2) {
                lines.add(new Line(x1, y1, x2, y2));
            }
        }
        return lines;
    }

    private class Line {

        int initX;
        int initY;
        int endX;
        int endY;

        private Line(int initX, int initY, int endX, int endY) {
            this.initX = initX;
            this.initY = initY;
            this.endX = endX;
            this.endY = endY;
        }
    }


}
