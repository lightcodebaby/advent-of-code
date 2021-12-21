package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day19 {

    private List<Scanner> scanners = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day19.txt");
        parseScanners(input);
        int result = howManyBeaconsAreThere();
        System.out.println();
    }

    private int howManyBeaconsAreThere() {
        int result = 0;
        boolean aa = areSamePosition(scanners.get(0).positions.get(9), scanners.get(1).positions.get(0));
        return result;
    }

    private boolean areSamePosition(Position firstPosition, Position secondPosition) {
        boolean result = false;
        List<Position> firstPositionRotations = rotate(firstPosition);
        List<Position> secondPositionRotations = rotate(secondPosition);
        for (int i = 0; i < firstPositionRotations.size() && !result; i++) {
            Position firstPositionRotation = firstPositionRotations.get(i);
            for (int j = 0; j < secondPositionRotations.size() && !result; j++) {
                Position secondPositionRotation = secondPositionRotations.get(i);
                if (firstPositionRotation.equals(secondPositionRotation)) {
                    result = true;
                }
            }
        }
        return result;
    }

    private List<Position> rotate(Position position) {
        List<Position> result = new ArrayList<>();
        result.add(position);
        result.add(new Position(position.x, position.z, position.y));
        result.add(new Position(position.y, position.x, position.z));
        result.add(new Position(position.z, position.x, position.y));
        result.add(new Position(position.y, position.z, position.x));
        result.add(new Position(position.z, position.y, position.x));
        new ArrayList<>(result).forEach(position1 -> result.addAll(mirror(position1)));
        return result;
    }

    private List<Position> mirror(Position position) {
        List<Position> result = new ArrayList<>();
        result.add(new Position(position.x * -1, position.y, position.z));
        result.add(new Position(position.x, position.y * -1, position.z));
        result.add(new Position(position.x, position.y, position.z * -1));
        return result;
    }

    private void parseScanners(List<String> input) {
        Scanner scanner = new Scanner();
        for (String line : input) {
            if (line.contains("scanner")) {
                scanner = new Scanner();
            } else if (line.isEmpty()) {
                scanners.add(scanner);
            } else {
                String[] coordinates = line.split(",");
                scanner.positions.add(new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2])));
            }
        }
    }

    private class Scanner {
        List<Position> positions;

        private Scanner() {
            this.positions = new ArrayList<>();
        }
    }

    private class Position {
        int x;
        int y;
        int z;

        private Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean equals(Position position) {
            return this.x - position.x == this.y - position.y && this.x - position.x == this.z - position.z;
        }
    }

}
