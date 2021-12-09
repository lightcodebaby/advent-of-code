package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day24 {

    /*
    --- Day 24: Air Duct Spelunking ---
    You've finally met your match; the doors that provide access to the roof are locked tight, and all of the controls and related electronics are inaccessible. You simply can't reach them.

    The robot that cleans the air ducts, however, can.

    It's not a very fast little robot, but you reconfigure it to be able to interface with some of the exposed wires that have been routed through the HVAC system. If you can direct it to each of those locations, you should be able to bypass the security controls.

    You extract the duct layout for this area from some blueprints you acquired and create a map with the relevant locations marked (your puzzle input). 0 is your current location, from which the cleaning robot embarks; the other numbers are (in no particular order) the locations the robot needs to visit at least once each. Walls are marked as #, and open passages are marked as .. Numbers behave like open passages.

    For example, suppose you have a map like the following:

    ###########
    #0.1.....2#
    #.#######.#
    #4.......3#
    ###########
    To reach all of the points of interest as quickly as possible, you would have the robot take the following path:

    0 to 4 (2 steps)
    4 to 1 (4 steps; it can't move diagonally)
    1 to 2 (6 steps)
    2 to 3 (2 steps)
    Since the robot isn't very fast, you need to find it the shortest route. This path is the fewest steps (in the above example, a total of 14) required to start at 0 and then visit every other location at least once.

    Given your actual map, and starting from location 0, what is the fewest number of steps required to visit every non-0 number marked on the map at least once?

    --- Part Two ---
    Of course, if you leave the cleaning robot somewhere weird, someone is bound to notice.

    What is the fewest number of steps required to start at 0, visit every non-0 number marked on the map at least once, and then return to 0?
     */

    private List<String> layout = new ArrayList<>();
    private char[][] matrix = new char[0][0];
    private boolean[][] permissions = new boolean[0][0];
    private Coordinate[][] coordinates = new Coordinate[0][0];
    private List<Location> locations = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        layout = Utilities.readInput("year2016/day24.txt");
        fillMatrix();
        simplifyMatrix();
        fillPermissions();
        fillCoordinates();
        fillLocations();
        int result = move();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        layout = Utilities.readInput("year2016/day24.txt");
        fillMatrix();
        simplifyMatrix();
        fillPermissions();
        fillCoordinates();
        fillLocations();
        int result = goAndComeBack();
        System.out.println("Part 2 solution: " + result);
    }

    private int goAndComeBack() {
        Coordinate origin = locations.stream().filter(location -> location.value == 0).findFirst().get().coordinate;
        List<Path> paths = new ArrayList<>();
        paths.add(getFirstPath());
        int result = 0;
        while(paths.stream().noneMatch(path -> path.visitedLocations.size() == locations.size() && path.coordinates.get(path.coordinates.size() - 1).equals(origin))) {
            List<Path> newPaths = paths.stream().filter(path -> !path.checked).collect(Collectors.toList());
            for(Path path : newPaths) {
                Coordinate currentCoordinate = path.coordinates.get(path.coordinates.size() - 1);
                visitingNewLocation(path, currentCoordinate);
                paths.addAll(getNextPaths(path, currentCoordinate, paths));
                path.checked = true;
            }
            paths.removeIf(path -> path.checked || paths.stream().anyMatch(path1 -> path1.visitedLocations.size() > path.visitedLocations.size() + 1));
            result++;
        }
        return result;
    }

    private int move() {
        List<Path> paths = new ArrayList<>();
        paths.add(getFirstPath());
        int result = 0;
        while(paths.stream().noneMatch(path -> path.visitedLocations.size() == locations.size())) {
            List<Path> newPaths = paths.stream().filter(path -> !path.checked).collect(Collectors.toList());
            for(Path path : newPaths) {
                Coordinate currentCoordinate = path.coordinates.get(path.coordinates.size() - 1);
                visitingNewLocation(path, currentCoordinate);
                paths.addAll(getNextPaths(path, currentCoordinate, paths));
                path.checked = true;
            }
            paths.removeIf(path -> path.checked || paths.stream().anyMatch(path1 -> path1.visitedLocations.size() > path.visitedLocations.size() + 1));
            result++;
        }
        return result - 1;
    }

    private List<Path> getNextPaths(Path path, Coordinate currentCoordinate, List<Path> paths) {
        List<Path> result = new ArrayList<>();
        List<Path> equivalentPaths = getPathsWithSameLocations(path, paths);
        if(permissions[currentCoordinate.row - 1][currentCoordinate.column] && equivalentPaths.stream().noneMatch(path1 -> path1.coordinates.contains(coordinates[currentCoordinate.row - 1][currentCoordinate.column]))) {
            paths.add(new Path(path, coordinates[currentCoordinate.row - 1][currentCoordinate.column], path.steps + "U"));
        }
        if(permissions[currentCoordinate.row + 1][currentCoordinate.column] && equivalentPaths.stream().noneMatch(path1 -> path1.coordinates.contains(coordinates[currentCoordinate.row + 1][currentCoordinate.column]))) {
            paths.add(new Path(path, coordinates[currentCoordinate.row + 1][currentCoordinate.column], path.steps + "D"));
        }
        if(permissions[currentCoordinate.row][currentCoordinate.column - 1] && equivalentPaths.stream().noneMatch(path1 -> path1.coordinates.contains(coordinates[currentCoordinate.row][currentCoordinate.column - 1]))) {
            paths.add(new Path(path, coordinates[currentCoordinate.row][currentCoordinate.column - 1], path.steps + "L"));
        }
        if(permissions[currentCoordinate.row][currentCoordinate.column + 1] && equivalentPaths.stream().noneMatch(path1 -> path1.coordinates.contains(coordinates[currentCoordinate.row][currentCoordinate.column + 1]))) {
            paths.add(new Path(path, coordinates[currentCoordinate.row][currentCoordinate.column + 1], path.steps + "R"));
        }
        return result;
    }

    private List<Path> getPathsWithSameLocations(Path path, List<Path> paths) {
        return paths.stream().filter(path1 -> path1.visitedLocations.equals(path.visitedLocations)).collect(Collectors.toList());
    }

    private void visitingNewLocation(Path path, Coordinate currentCoordinate) {
        if(isLocation(currentCoordinate)) {
            Location location = getLocation(currentCoordinate);
            if(!path.visitedLocations.contains(location.value)) {
                path.visitedLocations.add(location.value);
                path.coordinates.removeIf(coordinate -> !coordinate.equals(currentCoordinate));
                path.checked = false;
            }
        }
    }

    private boolean isLocation(Coordinate coordinate) {
        Optional<Location> optionalLocation = locations.stream().filter(location -> location.coordinate.equals(coordinate)).findFirst();
        return optionalLocation.isPresent();
    }

    private Location getLocation(Coordinate coordinate) {
        return locations.stream().filter(location -> location.coordinate.equals(coordinate)).findFirst().get();
    }

    private Path getFirstPath() {
        Path result = new Path();
        Location originLocation = locations.stream().filter(location -> location.value == 0).findFirst().get();
        Coordinate originCoordinate = originLocation.coordinate;
        result.visitedLocations.add(originLocation.value);
        result.coordinates.add(originCoordinate);
        return result;
    }

    private void fillLocations() {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                String value = String.valueOf(matrix[i][j]);
                if(Utilities.isNumber(value)) {
                    locations.add(new Location(coordinates[i][j], Integer.parseInt(value)));
                }
            }
        }
    }

    private void fillCoordinates() {
        coordinates = new Coordinate[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            coordinates[i] = new Coordinate[matrix[i].length];
            for(int j = 0; j < matrix[i].length; j++) {
                coordinates[i][j] = new Coordinate(i, j);
            }
        }
    }

    private void fillPermissions() {
        permissions = new boolean[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            permissions[i] = new boolean[matrix[i].length];
            for(int j = 0; j < matrix[i].length; j++) {
                permissions[i][j] = matrix[i][j] != '#';
            }
        }
    }

    private void fillMatrix() {
        matrix = new char[layout.size()][layout.get(0).length()];
        for (int i = 0; i < layout.size(); i++) {
            for (int j = 0; j < layout.get(i).length(); j++) {
                matrix[i][j] = layout.get(i).charAt(j);
            }
        }
    }

    private void simplifyMatrix() {
        boolean simplified = false;
        for (int i = 0; i < matrix.length && !simplified; i++) {
            for (int j = 0; j < matrix[i].length && !simplified; j++) {
                if (matrix[i][j] == '.') {
                    if(wallsAround(i, j) >= 3) {
                        matrix[i][j] = '#';
                        simplified = true;
                    }
                }
            }
        }
        if(simplified) {
            simplifyMatrix();
        }
    }

    private int wallsAround(int row, int column) {
        int walls = 0;
        if (matrix[row + 1][column] == '#') {
            walls++;
        }
        if (matrix[row - 1][column] == '#') {
            walls++;
        }
        if (matrix[row][column + 1] == '#') {
            walls++;
        }
        if (matrix[row][column - 1] == '#') {
            walls++;
        }
        return walls;
    }

    private void resetVariables() {
        layout = new ArrayList<>();
        matrix = new char[0][0];
        permissions = new boolean[0][0];
        coordinates = new Coordinate[0][0];
        locations = new ArrayList<>();
    }

    private class Path {

        private List<Coordinate> coordinates;
        private List<Integer> visitedLocations;
        private boolean checked;
        private String steps;

        private Path() {
            this.coordinates = new ArrayList<>();
            this.visitedLocations = new ArrayList<>();
            this.checked = false;
            this.steps = "";
        }

        private Path(Path path, Coordinate coordinate, String steps) {
            this.coordinates = new ArrayList<>(path.coordinates);
            this.coordinates.add(coordinate);
            this.visitedLocations = new ArrayList<>(path.visitedLocations);
            this.checked = false;
            this.steps = steps;
        }

    }

    private class Coordinate {

        private int row;
        private int column;

        private Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    private class Location {

        private int value;
        private Coordinate coordinate;

        private Location(Coordinate coordinate, int value) {
            this.coordinate = coordinate;
            this.value = value;
        }

    }

}
