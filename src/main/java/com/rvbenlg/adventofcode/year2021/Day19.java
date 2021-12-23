package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day19 {


    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day19.txt");
        System.out.println();
    }

    private void parseScanners(List<String> input) {

    }

    private List<Coordinate> rotate(Coordinate coordinate) {
        List<Coordinate> result = new ArrayList<>();
        result.add(coordinate);
        result.add(new Coordinate(coordinate.x, coordinate.z, -coordinate.y));
        result.add(new Coordinate(coordinate.x, -coordinate.y, -coordinate.z));
        result.add(new Coordinate(coordinate.x, -coordinate.z, coordinate.y));

        result.add(new Coordinate(-coordinate.x, coordinate.y, -coordinate.z));
        result.add(new Coordinate(-coordinate.x, -coordinate.z, -coordinate.y));
        result.add(new Coordinate(-coordinate.x, -coordinate.y, coordinate.z));
        result.add(new Coordinate(-coordinate.x, coordinate.z, coordinate.y));

        result.add(new Coordinate(coordinate.y, coordinate.z, coordinate.x));
        result.add(new Coordinate(coordinate.y, coordinate.x, -coordinate.z));
        result.add(new Coordinate(coordinate.y, -coordinate.z, -coordinate.x));
        result.add(new Coordinate(coordinate.y, -coordinate.x, coordinate.z));

        result.add(new Coordinate(-coordinate.y, coordinate.z, -coordinate.x));
        result.add(new Coordinate(-coordinate.y, -coordinate.x, -coordinate.z));
        result.add(new Coordinate(-coordinate.y, -coordinate.z, -coordinate.x));
        result.add(new Coordinate(-coordinate.y, coordinate.x, coordinate.z));

        result.add(new Coordinate(coordinate.z, coordinate.x, coordinate.y));
        result.add(new Coordinate(coordinate.z, coordinate.y, -coordinate.x));
        result.add(new Coordinate(coordinate.z, -coordinate.x, -coordinate.y));
        result.add(new Coordinate(coordinate.z, -coordinate.y, coordinate.x));

        result.add(new Coordinate(-coordinate.z, coordinate.x, -coordinate.y));
        result.add(new Coordinate(-coordinate.z, -coordinate.y, -coordinate.x));
        result.add(new Coordinate(-coordinate.z, -coordinate.x, coordinate.y));
        result.add(new Coordinate(-coordinate.z, coordinate.y, coordinate.x));
        return result;
    }

    private class Scanner {
        List<Space> spaces;

        private Scanner() {
            this.spaces = new ArrayList<>();
        }
    }

    private class Space {
        List<Coordinate> coordinates;

        private Space() {
            this.coordinates = new ArrayList<>();
        }
    }

    private class Coordinate {
        int x;
        int y;
        int z;

        private Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean equals(Coordinate position) {
            return this.x - position.x == this.y - position.y && this.x - position.x == this.z - position.z;
        }
    }

}
