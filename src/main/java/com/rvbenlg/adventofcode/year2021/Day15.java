package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Coordinate[][] coordinates = new Coordinate[0][0];
    private int[][] largerOriginal = new int[0][0];
    private int[][] largerModified = new int[0][0];


    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day15.txt");
        fillMatrixes(input);
        fillCoordinates(original);
        modifyMatrix(1);
        System.out.println("Part 1 solution: " + modified[modified.length - 1][modified[modified.length - 1].length - 1]);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day15.txt");
        fillMatrixes(input);
        fillCoordinates(largerOriginal);
        modifyMatrix(2);
        System.out.println("Part 2 solution: " + largerModified[largerModified.length - 1][largerModified[largerModified.length - 1].length - 1]);
    }

    private void modifyMatrix(int part) {
        Coordinate origin = new Coordinate(0, 0);
        Set<Coordinate> modified = modifyAdjacents(origin, part);
        do {
            Set<Coordinate> newModified = new HashSet<>();
            for (Coordinate coordinate : modified) {
                newModified.addAll(modifyAdjacents(coordinate, part));
            }
            modified = newModified;
        } while (!modified.isEmpty());
    }

    private Set<Coordinate> modifyAdjacents(Coordinate coordinate, int part) {
        Set<Coordinate> modifiedAdjacents = new HashSet<>();
        if (canModifyTop(coordinate, part)) {
            modifyTop(coordinate, part);
            modifiedAdjacents.add(getTopCoordinate(coordinate));
        }
        if (canModifyDown(coordinate, part)) {
            modifyDown(coordinate, part);
            modifiedAdjacents.add(getDownCoordinate(coordinate));
        }
        if (canModifyLeft(coordinate, part)) {
            modifyLeft(coordinate, part);
            modifiedAdjacents.add(getLeftCoordinate(coordinate));
        }
        if (canModifyRight(coordinate, part)) {
            modifyRight(coordinate, part);
            modifiedAdjacents.add(getRightCoordinate(coordinate));
        }
        return modifiedAdjacents;
    }

    private Coordinate getTopCoordinate(Coordinate coordinate) {
        return coordinates[coordinate.row - 1][coordinate.column];
    }

    private Coordinate getDownCoordinate(Coordinate coordinate) {
        return coordinates[coordinate.row + 1][coordinate.column];
    }

    private Coordinate getLeftCoordinate(Coordinate coordinate) {
        return coordinates[coordinate.row][coordinate.column - 1];
    }

    private Coordinate getRightCoordinate(Coordinate coordinate) {
        return coordinates[coordinate.row][coordinate.column + 1];
    }

    private boolean canModifyTop(Coordinate coordinate, int part) {
        if (part == 1) {
            return canModifyTop(coordinate, original, modified);
        } else {
            return canModifyTop(coordinate, largerOriginal, largerModified);
        }
    }

    private boolean canModifyTop(Coordinate coordinate, int[][] original, int[][] modified) {
        return coordinate.row > 0
                && (original[coordinate.row - 1][coordinate.column] == modified[coordinate.row - 1][coordinate.column] ||
                modified[coordinate.row - 1][coordinate.column] > modified[coordinate.row][coordinate.column] + original[coordinate.row - 1][coordinate.column]);
    }

    private void modifyTop(Coordinate coordinate, int part) {
        if (part == 1) {
            modified[coordinate.row - 1][coordinate.column] = original[coordinate.row - 1][coordinate.column] + modified[coordinate.row][coordinate.column];
        } else {
            largerModified[coordinate.row - 1][coordinate.column] = largerOriginal[coordinate.row - 1][coordinate.column] + largerModified[coordinate.row][coordinate.column];
        }
    }

    private boolean canModifyDown(Coordinate coordinate, int part) {
        if (part == 1) {
            return canModifyDown(coordinate, original, modified);
        } else {
            return canModifyDown(coordinate, largerOriginal, largerModified);
        }

    }

    private boolean canModifyDown(Coordinate coordinate, int[][] original, int[][] modified) {
        return coordinate.row < original.length - 1
                && (original[coordinate.row + 1][coordinate.column] == modified[coordinate.row + 1][coordinate.column] ||
                modified[coordinate.row + 1][coordinate.column] > original[coordinate.row + 1][coordinate.column] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyDown(Coordinate coordinate, int part) {
        if (part == 1) {
            modified[coordinate.row + 1][coordinate.column] = original[coordinate.row + 1][coordinate.column] + modified[coordinate.row][coordinate.column];
        } else {
            largerModified[coordinate.row + 1][coordinate.column] = largerOriginal[coordinate.row + 1][coordinate.column] + largerModified[coordinate.row][coordinate.column];
        }

    }

    private boolean canModifyLeft(Coordinate coordinate, int part) {
        if (part == 1) {
            return canModifyLeft(coordinate, original, modified);
        } else {
            return canModifyLeft(coordinate, largerOriginal, largerModified);
        }

    }

    private boolean canModifyLeft(Coordinate coordinate, int[][] original, int[][] modified) {
        return coordinate.column > 0
                && (original[coordinate.row][coordinate.column - 1] == modified[coordinate.row][coordinate.column - 1] ||
                modified[coordinate.row][coordinate.column - 1] > original[coordinate.row][coordinate.column - 1] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyLeft(Coordinate coordinate, int part) {
        if (part == 1) {
            modified[coordinate.row][coordinate.column - 1] = original[coordinate.row][coordinate.column - 1] + modified[coordinate.row][coordinate.column];
        } else {
            largerModified[coordinate.row][coordinate.column - 1] = largerOriginal[coordinate.row][coordinate.column - 1] + largerModified[coordinate.row][coordinate.column];
        }
    }

    private boolean canModifyRight(Coordinate coordinate, int part) {
        if (part == 1) {
            return canModifyRight(coordinate, original, modified);
        } else {
            return canModifyRight(coordinate, largerOriginal, largerModified);
        }
    }

    private boolean canModifyRight(Coordinate coordinate, int[][] original, int[][] modified) {
        return coordinate.column < original[0].length - 1
                && (original[coordinate.row][coordinate.column + 1] == modified[coordinate.row][coordinate.column + 1] ||
                modified[coordinate.row][coordinate.column + 1] > original[coordinate.row][coordinate.column + 1] + modified[coordinate.row][coordinate.column]);
    }

    private void modifyRight(Coordinate coordinate, int part) {
        if (part == 1) {
            modified[coordinate.row][coordinate.column + 1] = original[coordinate.row][coordinate.column + 1] + modified[coordinate.row][coordinate.column];
        } else {
            largerModified[coordinate.row][coordinate.column + 1] = largerOriginal[coordinate.row][coordinate.column + 1] + largerModified[coordinate.row][coordinate.column];
        }

    }

    private void fillMatrixes(List<String> input) {
        fillSmallMatrixes(input);
        fillLargeMatrixes();
        original[0][0] = 0;
        modified[0][0] = 0;
        largerOriginal[0][0] = 0;
        largerModified[0][0] = 0;
    }

    private void fillSmallMatrixes(List<String> input) {
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
            }
        }
    }

    private void fillLargeMatrixes() {
        fillLargeMatrixesDimensions();
        fillOriginalMatrix();
        fillExtraRows();
        fillExtraColumns();
    }

    private void fillLargeMatrixesDimensions() {
        largerOriginal = new int[original.length * 5][];
        largerModified = new int[original.length * 5][];
        for (int i = 0; i < largerOriginal.length; i++) {
            largerOriginal[i] = new int[original[0].length * 5];
            largerModified[i] = new int[modified[0].length * 5];
        }
    }

    private void fillOriginalMatrix() {
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                largerOriginal[i][j] = original[i][j];
                largerModified[i][j] = modified[i][j];
            }
        }
    }

    private void fillExtraRows() {
        for (int i = original.length; i < largerOriginal.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                int previous = largerOriginal[i - original.length][j];
                int current = previous == 9 ? 1 : previous + 1;
                largerOriginal[i][j] = current;
                largerModified[i][j] = current;
            }
        }
    }

    private void fillExtraColumns() {
        for (int i = 0; i < largerOriginal.length; i++) {
            for (int j = original[0].length; j < largerOriginal[0].length; j++) {
                int previous = largerOriginal[i][j - original[0].length];
                int current = previous == 9 ? 1 : previous + 1;
                largerOriginal[i][j] = current;
                largerModified[i][j] = current;
            }
        }
    }

    private void fillCoordinates(int[][] matrix) {
        coordinates = new Coordinate[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            coordinates[i] = new Coordinate[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                coordinates[i][j] = new Coordinate(i, j);
            }
        }
    }


    private class Coordinate {
        int row;
        int column;

        private Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Coordinate) obj).row == this.row
                    && ((Coordinate) obj).column == this.column;
        }
    }


}
