package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {

    List<String> layout = new ArrayList<>();
    List<Coordinate> coordinates = new ArrayList<>();
    char[][] matrix = new char[0][0];
    private List<List<Integer>> distances = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        layout = Utilities.readInput("year2016/day24.txt");
        fillMatrix();
        move();
        for (int i = 0; i < distances.size(); i++) {
            List<Integer> alreadyUsed = new ArrayList<>();
            alreadyUsed.add(i);
            int closer = i;
            for (int j = 0; j < 7; j++) {
                List<Integer> auxDistance = new ArrayList<>(distances.get(closer));
                auxDistance.removeIf(integer -> alreadyUsed.contains(auxDistance.indexOf(integer)));
                closer = distances.get(closer).indexOf(auxDistance.stream().filter(integer -> auxDistance.stream().noneMatch(integer1 -> integer1 < integer)).findFirst().get());
                alreadyUsed.add(closer);
            }
        }

    }

    private void move() {
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate origin = coordinates.get(i);
            List<Integer> distance = new ArrayList<>();
            for (int j = 0; j < coordinates.size(); j++) {
                if(i != j) {
                    Coordinate destination = coordinates.get(j);
                    distance.add(distance(origin, destination));
                }
            }
            distances.add(distance);
        }

    }


    private int distance(Coordinate origin, Coordinate destination) {
        origin.checked = false;
        destination.checked = false;
        int result = 0;
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(origin);
        while (coordinates.stream().noneMatch(coordinate -> coordinate.x == destination.x && coordinate.y == destination.y)) {
            List<Coordinate> unchecked = coordinates.stream().filter(coordinate -> !coordinate.checked).collect(Collectors.toList());
            for (Coordinate coordinate : unchecked) {
                if (canGoUp(coordinate, coordinates)) {
                    coordinates.add(new Coordinate(coordinate.x, coordinate.y - 1));
                }
                if (canGoDown(coordinate, coordinates)) {
                    coordinates.add(new Coordinate(coordinate.x, coordinate.y + 1));
                }
                if (canGoLeft(coordinate, coordinates)) {
                    coordinates.add(new Coordinate(coordinate.x - 1, coordinate.y));
                }
                if (canGoRight(coordinate, coordinates)) {
                    coordinates.add(new Coordinate(coordinate.x + 1, coordinate.y));
                }
                coordinate.checked = true;
            }
            result++;
        }
        return result;
    }

    private boolean canGoUp(Coordinate coordinate, List<Coordinate> coordinates) {
        boolean result = false;
        if (coordinate.y > 0 && coordinates.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y - 1 && coordinate1.x == coordinate.x)) {
            result = matrix[coordinate.y - 1][coordinate.x] != '#';
        }
        return result;
    }

    private boolean canGoDown(Coordinate coordinate, List<Coordinate> coordinates) {
        boolean result = false;
        if (coordinate.y < layout.size() - 1 && coordinates.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y + 1 && coordinate1.x == coordinate.x)) {
            result = matrix[coordinate.y + 1][coordinate.x] != '#';
        }
        return result;
    }

    private boolean canGoLeft(Coordinate coordinate, List<Coordinate> coordinates) {
        boolean result = false;
        if (coordinate.x > 0 && coordinates.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y && coordinate1.x == coordinate.x - 1)) {
            result = matrix[coordinate.y][coordinate.x - 1] != '#';
        }
        return result;
    }

    private boolean canGoRight(Coordinate coordinate, List<Coordinate> coordinates) {
        boolean result = false;
        if (coordinate.x < layout.get(0).length() && coordinates.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y && coordinate1.x == coordinate.x + 1)) {
            result = matrix[coordinate.y][coordinate.x + 1] != '#';
        }
        return result;
    }

    private void fillMatrix() {
        matrix = new char[layout.size()][layout.get(0).length()];
        for (int i = 0; i < layout.size(); i++) {
            String row = layout.get(i);
            for (int j = 0; j < row.length(); j++) {
                matrix[i][j] = row.charAt(j);
                if (Utilities.isNumber(String.valueOf(matrix[i][j]))) {
                    coordinates.add(new Coordinate(j, i));
                }
            }
        }
    }


    private void resetVariables() {
        layout = new ArrayList<>();
        matrix = new char[0][0];
    }

    private class Coordinate {

        private int x;
        private int y;
        private boolean checked;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


}
