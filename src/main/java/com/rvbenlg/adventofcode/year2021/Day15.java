package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day15 {

    /*
    --- Day 15: Chiton ---
    You've almost reached the exit of the cave, but the walls are getting closer together. Your submarine can barely still fit, though; the main problem is that the walls of the cave are covered in chitons, and it would be best not to bump any of them.

    The cavern is large, but has a very low ceiling, restricting your motion to two dimensions. The shape of the cavern resembles a square; a quick scan of chiton density produces a map of risk level throughout the cave (your puzzle input). For example:

    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
    You start in the top left position, your destination is the bottom right position, and you cannot move diagonally. The number at each position is its risk level; to determine the total risk of an entire path, add up the risk levels of each position you enter (that is, don't count the risk level of your starting position unless you enter it; leaving it adds no risk to your total).

    Your goal is to find a path with the lowest total risk. In this example, a path with the lowest total risk is highlighted here:

    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
    The total risk of this path is 40 (the starting position is never entered, so its risk is not counted).

    What is the lowest total risk of any path from the top left to the bottom right?
     */

    private int[][] original = new int[0][0];
    private int[][] modified = new int[0][0];
    private List<Coordinate> coordinates = new ArrayList<>();


    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day15.txt");
        fillMatrixes(input);
        modifyMatrix();
        System.out.println("Part 1 solution: " + modified[modified.length-1][modified[modified.length - 1].length - 1]);
    }

    private void modifyMatrix() {
        Coordinate origin = coordinates.stream().filter(coordinate -> coordinate.row == 0 && coordinate.column == 0).findFirst().get();
        List<Coordinate> modified = modifyAdjacents(origin);
        do {
            List<Coordinate> newModified = new ArrayList<>();
            for(Coordinate coordinate : modified) {
                newModified.addAll(modifyAdjacents(coordinate));
            }
            modified = newModified;
        } while(!modified.isEmpty());
    }

    private List<Coordinate> modifyAdjacents(Coordinate coordinate) {
        List<Coordinate> modifiedAdjacents = new ArrayList<>();
        if (canModifyTop(coordinate)) {
            modifyTop(coordinate);
            modifiedAdjacents.add(getTopCoordinate(coordinate));
        }
        if (canModifyDown(coordinate)) {
            modifyDown(coordinate);
            modifiedAdjacents.add(getDownCoordinate(coordinate));
        }
        if (canModifyLeft(coordinate)) {
            modifyLeft(coordinate);
            modifiedAdjacents.add(getLeftCoordinate(coordinate));
        }
        if (canModifyRight(coordinate)) {
            modifyRight(coordinate);
            modifiedAdjacents.add(getRightCoordinate(coordinate));
        }
        return modifiedAdjacents;
    }

    private Coordinate getTopCoordinate(Coordinate coordinate) {
        return coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row - 1 && coordinate1.column == coordinate.column).findFirst().get();
    }

    private Coordinate getDownCoordinate(Coordinate coordinate) {
        return coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row + 1 && coordinate1.column == coordinate.column).findFirst().get();
    }

    private Coordinate getLeftCoordinate(Coordinate coordinate) {
        return coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row && coordinate1.column == coordinate.column - 1).findFirst().get();
    }

    private Coordinate getRightCoordinate(Coordinate coordinate) {
        return coordinates.stream().filter(coordinate1 -> coordinate1.row == coordinate.row && coordinate1.column == coordinate.column + 1).findFirst().get();
    }

    private boolean canModifyTop(Coordinate coordinate) {
        return coordinate.row > 0
                && (original[coordinate.row - 1][coordinate.column] == modified[coordinate.row - 1][coordinate.column] ||
                modified[coordinate.row - 1][coordinate.column] > modified[coordinate.row][coordinate.column] + original[coordinate.row - 1][coordinate.column]);
    }

    private void modifyTop(Coordinate coordinate) {
        modified[coordinate.row - 1][coordinate.column] = original[coordinate.row - 1][coordinate.column] + modified[coordinate.row][coordinate.column];
    }

    private boolean canModifyDown(Coordinate coordinate) {
        return coordinate.row < original.length - 1
                && (original[coordinate.row + 1][coordinate.column] == modified[coordinate.row + 1][coordinate.column] ||
                modified[coordinate.row + 1][coordinate.column] > original[coordinate.row + 1][coordinate.column] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyDown(Coordinate coordinate) {
        modified[coordinate.row + 1][coordinate.column] = original[coordinate.row + 1][coordinate.column] + modified[coordinate.row][coordinate.column];
    }

    private boolean canModifyLeft(Coordinate coordinate) {
        return coordinate.column > 0
                && (original[coordinate.row][coordinate.column - 1] == modified[coordinate.row][coordinate.column - 1] ||
                modified[coordinate.row][coordinate.column - 1] > original[coordinate.row][coordinate.column - 1] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyLeft(Coordinate coordinate) {
        modified[coordinate.row][coordinate.column - 1] = original[coordinate.row][coordinate.column - 1] + modified[coordinate.row][coordinate.column];
    }

    private boolean canModifyRight(Coordinate coordinate) {
        return coordinate.column < original[0].length - 1
                && (original[coordinate.row][coordinate.column + 1] == modified[coordinate.row][coordinate.column + 1] ||
                modified[coordinate.row][coordinate.column + 1] > original[coordinate.row][coordinate.column + 1] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyRight(Coordinate coordinate) {
        modified[coordinate.row][coordinate.column + 1] = original[coordinate.row][coordinate.column + 1] + modified[coordinate.row][coordinate.column];
    }

    private void fillMatrixes(List<String> input) {
        original = new int[input.size()][];
        modified = new int[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            original[i] = new int[line.length()];
            modified[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                int value = Integer.parseInt(String.valueOf(line.charAt(j)));
                original[i][j] = value;
                modified[i][j] = value;
                coordinates.add(new Coordinate(i, j));
            }
        }
        original[0][0] = 0;
        modified[0][0] = 0;
    }


    private class Coordinate {
        int row;
        int column;

        private Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }


}
