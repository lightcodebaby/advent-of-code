package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day09 {

    /*
    --- Day 9: Smoke Basin ---
    These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke into the caves that slowly settles like rain.

    If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).

    Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
    Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.

    Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)

    In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower adjacent location, and so are not low points.

    The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2, 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.

    Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?

    --- Part Two ---
    Next, you need to find the largest basins so you know what areas are most important to avoid.

    A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.

    The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.

    The top-left basin, size 3:

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
    The top-right basin, size 9:

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
    The middle basin, size 14:

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
    The bottom-right basin, size 9:

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
    Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.

    What do you get if you multiply together the sizes of the three largest basins?
     */

    private int[][] matrix = new int[0][0];
    private List<Coordinate> coordinates = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day09.txt");
        fillMatrix(input);
        int result = sumRisksOfLowPoints();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day09.txt");
        fillMatrix(input);
        fillCoordinates();
        identifyLowPoints();
        calculateBasinsSizes();
        int result = multiplyThreeLargestBasinsSizes();
        System.out.println("Part 2 solution: " + result);
    }

    private int multiplyThreeLargestBasinsSizes() {
        List<Coordinate> lowPoints = coordinates.stream().filter(coordinate -> coordinate.lowPoint).collect(Collectors.toList());
        Coordinate first = lowPoints.get(0);
        Coordinate second = lowPoints.get(1);
        Coordinate third = lowPoints.get(2);
        for (Coordinate lowPoint : lowPoints) {
            if (lowPoint.basinSize > first.basinSize
                    && (lowPoint.row != second.row || lowPoint.column != second.column)
                    && (lowPoint.row != third.row || lowPoint.column != third.column)) {
                first = lowPoint;
            }
            if (lowPoint.basinSize > second.basinSize
                    && (lowPoint.row != first.row || lowPoint.column != first.column)
                    && (lowPoint.row != third.row || lowPoint.column != third.column)) {
                second = lowPoint;
            }
            if (lowPoint.basinSize > third.basinSize
                    && (lowPoint.row != first.row || lowPoint.column != first.column)
                    && (lowPoint.row != second.row || lowPoint.column != second.column)) {
                third = lowPoint;
            }
        }
        return first.basinSize * second.basinSize * third.basinSize;
    }

    private void calculateBasinsSizes() {
        List<Coordinate> lowPoints = coordinates.stream().filter(coordinate -> coordinate.lowPoint).collect(Collectors.toList());
        for (Coordinate lowPoint : lowPoints) {
            Set<Coordinate> basins = getBasins(lowPoint);
            lowPoint.basinSize = basins.size() + 1;
        }
    }

    private Set<Coordinate> getBasins(Coordinate coordinate) {
        Set<Coordinate> basins = getAdjacents(coordinate).stream().filter(basin -> basin.value > coordinate.value && basin.value != 9).collect(Collectors.toSet());
        if (!basins.isEmpty()) {
            Set<Coordinate> newBasins = new HashSet<>();
            basins.stream().filter(coordinate1 -> !coordinate1.checked).forEach(coordinate1 -> {
                newBasins.addAll(getBasins(coordinate1));
                coordinate1.checked = true;
            });
            basins.addAll(newBasins);
        }
        return basins;
    }

    private Set<Coordinate> getAdjacents(Coordinate coordinate) {
        Set<Coordinate> adjacents = new HashSet<>();
        Optional<Coordinate> optionalTop = coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row - 1 && coordinate1.column == coordinate.column).findFirst();
        Optional<Coordinate> optionalBottom = coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row + 1 && coordinate1.column == coordinate.column).findFirst();
        Optional<Coordinate> optionalLeft = coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row && coordinate1.column == coordinate.column - 1).findFirst();
        Optional<Coordinate> optionalRight = coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row && coordinate1.column == coordinate.column + 1).findFirst();
        optionalTop.ifPresent(adjacents::add);
        optionalBottom.ifPresent(adjacents::add);
        optionalLeft.ifPresent(adjacents::add);
        optionalRight.ifPresent(adjacents::add);
        return adjacents;
    }

    private void identifyLowPoints() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (isLowPoint(i, j)) {
                    int row = i;
                    int column = j;
                    coordinates.stream().filter(coordinate -> coordinate.row == row && coordinate.column == column).findFirst().get().lowPoint = true;
                }
            }
        }
    }

    private int sumRisksOfLowPoints() {
        int result = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (isLowPoint(i, j)) {
                    result += (matrix[i][j] + 1);
                }
            }
        }
        return result;
    }

    private boolean isLowPoint(int row, int column) {
        boolean result = true;
        int number = matrix[row][column];
        if ((row > 0 && matrix[row - 1][column] <= number) ||
                (row < matrix.length - 1 && matrix[row + 1][column] <= number) ||
                (column > 0 && matrix[row][column - 1] <= number) ||
                (column < matrix[row].length - 1 && matrix[row][column + 1] <= number)) {
            result = false;
        }
        return result;
    }

    private void fillMatrix(List<String> input) {
        matrix = new int[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            String row = input.get(i);
            matrix[i] = new int[row.length()];
            for (int j = 0; j < row.length(); j++) {
                matrix[i][j] = Integer.parseInt(row.substring(j, j + 1));

            }
        }
    }

    private void fillCoordinates() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                coordinates.add(new Coordinate(i, j, matrix[i][j]));
            }
        }
    }

    private void resetVariables() {
        matrix = new int[0][0];
        coordinates = new ArrayList<>();
    }

    private class Coordinate {

        int row;
        int column;
        int value;
        boolean checked;
        boolean lowPoint;
        int basinSize;

        private Coordinate(int row, int column, int value) {
            this.row = row;
            this.column = column;
            this.value = value;
            this.checked = false;
            this.lowPoint = false;
            this.basinSize = 0;
        }

    }

}
