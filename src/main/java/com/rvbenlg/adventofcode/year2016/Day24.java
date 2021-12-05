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
    private List<Location> locations = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        layout = Utilities.readInput("year2016/day24.txt");
        fillMatrix();
        int steps = move();
        System.out.println("Part 1 solution: " + steps);

    }

    private int move() {
        int result = 0;
        List<Path> paths = new ArrayList<>();
        paths.add(getFirstPath());
        while(paths.stream().noneMatch(path -> path.visitedLocations.size() == locations.size())) {
            List<Path> newPaths = paths.stream().filter(path -> !path.checked).collect(Collectors.toList());
            List<Path> haveVisitedNewLocation = new ArrayList<>();
            for(Path path : newPaths) {
                Coordinate currentCoordinate = path.coordinates.get(path.coordinates.size() - 1);
                if(checkIfNewLocation(path)) {
                    haveVisitedNewLocation.add(path);
                } else {
                    if(canGoUp(currentCoordinate, getPathsWithSameVisitedLocations(path, paths))) {
                        paths.add(new Path(path, new Coordinate(currentCoordinate.x, currentCoordinate.y - 1)));
                    }
                    if(canGoDown(currentCoordinate, getPathsWithSameVisitedLocations(path, paths))) {
                        paths.add(new Path(path, new Coordinate(currentCoordinate.x, currentCoordinate.y + 1)));
                    }
                    if(canGoLeft(currentCoordinate, getPathsWithSameVisitedLocations(path, paths))) {
                        paths.add(new Path(path, new Coordinate(currentCoordinate.x - 1, currentCoordinate.y)));
                    }
                    if(canGoRight(currentCoordinate, getPathsWithSameVisitedLocations(path, paths))) {
                        paths.add(new Path(path, new Coordinate(currentCoordinate.x + 1, currentCoordinate.y)));
                    }
                    path.checked = true;
                }
            }
            if(haveVisitedNewLocation.size() > 0) {
                updatePaths(paths, haveVisitedNewLocation);
            } else {
                paths.removeIf(path -> path.checked);
                result++;
            }
        }
        return result;
    }

    private void updatePaths(List<Path> paths, List<Path> haveVisitedNewLocation) {
        paths.clear();
        for(Path path : haveVisitedNewLocation) {
            List<Coordinate> allCurrentCoordinates = paths.stream().map(path1 -> path1.coordinates.get(path1.coordinates.size() - 1)).collect(Collectors.toList());
            Coordinate pathCurrentCoordinate = path.coordinates.get(path.coordinates.size() - 1);
            if(allCurrentCoordinates.stream().noneMatch(coordinate -> coordinate.x == pathCurrentCoordinate.x && coordinate.y == pathCurrentCoordinate.y)) {
                paths.add(path);
            }
        }
    }

    private boolean checkIfNewLocation(Path path) {
        boolean result = false;
        Coordinate currentCoordinate = path.coordinates.get(path.coordinates.size() - 1);
        Optional<Location> optionalLocation = locations.stream().filter(location -> location.coordinate.x == currentCoordinate.x && location.coordinate.y == currentCoordinate.y && path.visitedLocations.stream().noneMatch(location1 -> location.value == location1.value)).findFirst();
        if(optionalLocation.isPresent()) {
            path.visitedLocations.add(optionalLocation.get());
            path.coordinates = new ArrayList<>();
            path.coordinates.add(currentCoordinate);
            result = true;
        }
        return result;
    }

    private Path getFirstPath() {
        Location origin = locations.stream().filter(location -> location.value == 0).findFirst().get();
        Path firstPath = new Path();
        firstPath.coordinates.add(new Coordinate(origin.coordinate.x, origin.coordinate.y));
        firstPath.visitedLocations.add(origin);
        return firstPath;
    }


    private boolean canGoUp(Coordinate coordinate, List<Path> paths) {
        boolean result = false;
        List<Coordinate> coordinatesToCheck = new ArrayList<>();
        paths.forEach(path -> coordinatesToCheck.addAll(path.coordinates));
        if (coordinate.y > 0 && coordinatesToCheck.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y - 1 && coordinate1.x == coordinate.x)) {
            result = matrix[coordinate.y - 1][coordinate.x] != '#';
        }
        return result;
    }

    private boolean canGoDown(Coordinate coordinate, List<Path> paths) {
        boolean result = false;
        List<Coordinate> coordinatesToCheck = new ArrayList<>();
        paths.forEach(path -> coordinatesToCheck.addAll(path.coordinates));
        if (coordinate.y < layout.size() - 1 && coordinatesToCheck.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y + 1 && coordinate1.x == coordinate.x)) {
            result = matrix[coordinate.y + 1][coordinate.x] != '#';
        }
        return result;
    }

    private boolean canGoLeft(Coordinate coordinate, List<Path> paths) {
        boolean result = false;
        List<Coordinate> coordinatesToCheck = new ArrayList<>();
        paths.forEach(path -> coordinatesToCheck.addAll(path.coordinates));
        if (coordinate.x > 0 && coordinatesToCheck.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y && coordinate1.x == coordinate.x - 1)) {
            result = matrix[coordinate.y][coordinate.x - 1] != '#';
        }
        return result;
    }

    private boolean canGoRight(Coordinate coordinate, List<Path> paths) {
        boolean result = false;
        List<Coordinate> coordinatesToCheck = new ArrayList<>();
        paths.forEach(path -> coordinatesToCheck.addAll(path.coordinates));
        if (coordinate.x < layout.get(0).length() && coordinatesToCheck.stream().noneMatch(coordinate1 -> coordinate1.y == coordinate.y && coordinate1.x == coordinate.x + 1)) {
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
                    locations.add(new Location(new Coordinate(j, i), Integer.parseInt(String.valueOf(matrix[i][j]))));
                }
            }
        }
    }

    private List<Path> getPathsWithSameVisitedLocations(Path path, List<Path> paths) {
        List<Integer> visitedLocations = path.visitedLocations.stream().map(location -> location.value).collect(Collectors.toList());
        List<Path> result = new ArrayList<>();
        result.add(path);
        for(Path auxPath : paths) {
            List<Integer> auxVisitedLocations = auxPath.visitedLocations.stream().map(location -> location.value).collect(Collectors.toList());
            if(visitedLocations.size() > 1 && visitedLocations.containsAll(auxVisitedLocations)) {
                result.add(auxPath);
            }
        }
        return result;
    }


    private void resetVariables() {
        layout = new ArrayList<>();
        matrix = new char[0][0];
    }

    private class Path {

        private List<Coordinate> coordinates;
        private List<Location> visitedLocations;
        private boolean checked;

        private Path() {
            this.coordinates = new ArrayList<>();
            this.visitedLocations = new ArrayList<>();
            this.checked = false;
        }

        private Path(Path path, Coordinate coordinate) {
            this.coordinates = new ArrayList<>(path.coordinates);
            this.coordinates.add(coordinate);
            this.visitedLocations = new ArrayList<>(path.visitedLocations);
            this.checked = false;
        }

        @Override
        public boolean equals(Object obj) {
            Path objPath = (Path) obj;
            Coordinate objCurrentCoordinate = objPath.coordinates.get(objPath.coordinates.size() - 1);
            Coordinate thisCurrentCoordinate = this.coordinates.get(this.coordinates.size() - 1);
            return objCurrentCoordinate.x == thisCurrentCoordinate.x && objCurrentCoordinate.y == thisCurrentCoordinate.y;
        }
    }

    private class Coordinate {

        private int x;
        private int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Location {

        private Coordinate coordinate;
        private int value;

        private Location(Coordinate coordinate, int value) {
            this.coordinate = coordinate;
            this.value = value;
        }
    }


}
