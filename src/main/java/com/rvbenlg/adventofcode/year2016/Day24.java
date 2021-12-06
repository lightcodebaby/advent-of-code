package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day24 {

    private List<String> layout = new ArrayList<>();
    private char[][] matrix = new char[0][0];
    private boolean[][] permissions = new boolean[0][0];
    private Coordinate[][] coordinates = new Coordinate[0][0];
    private List<Location> locations = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        layout = Utilities.readInput("year2016/day24.txt");
        fillMatrix();
        simplifyMatrix();
        fillPermissions();
        fillCoordinates();
        fillLocations();
        int result = move();
        System.out.println("Part 1 solution: " + result);
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
            paths.add(new Path(path, coordinates[currentCoordinate.row][currentCoordinate.column + 1], path.steps + "L"));
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
