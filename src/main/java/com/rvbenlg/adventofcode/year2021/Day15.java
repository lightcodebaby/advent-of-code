package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private Risk[][] risks = new Risk[0][0];
    private Risk destination = new Risk(0, 0, 0);

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day15.txt");
        fillRisks(input);
        fillDestination();
        List<Path> paths = getAllPaths();
        int result = getLowestTotalRisk(paths);
        System.out.println("Part 1 solution: " + result);
    }

    private int getLowestTotalRisk(List<Path> paths) {
        int lowestRisk = Integer.MAX_VALUE;
        for (Path path : paths) {
            int pathRisk = path.totalRisk;
            if (pathRisk < lowestRisk) {
                lowestRisk = pathRisk;
            }
        }
        return lowestRisk;
    }

    private List<Path> getAllPaths() {
        List<Path> paths = new ArrayList<>();
        paths.add(getFirstPath());
        List<Path> finishedPaths = new ArrayList<>();
        List<Path> unfinishedPaths = new ArrayList<>(paths);
        while (!unfinishedPaths.isEmpty()) {
            for (Path path : unfinishedPaths) {
                paths.addAll(getNewPaths(path, paths));
            }
            paths.removeIf(unfinishedPaths::contains);
            finishedPaths.addAll(paths.stream().filter(path1 -> !finishedPaths.contains(path1) && path1.pathRisks.get(path1.pathRisks.size() - 1).equals(destination)).collect(Collectors.toList()));
            unfinishedPaths = paths.stream().filter(path1 -> !finishedPaths.contains(path1) && finishedPaths.stream().noneMatch(finishedPath -> finishedPath.totalRisk <= path1.totalRisk)).collect(Collectors.toList());
        }
        return paths;
    }

    private List<Path> getNewPaths(Path path, List<Path> paths) {
        List<Path> newPaths = new ArrayList<>();
        Risk currentRisk = path.pathRisks.get(path.pathRisks.size() - 1);
        if (canGoUp(path, paths)) {
            List<Risk> newPathRisks = new ArrayList<>(path.pathRisks);
            newPathRisks.add(risks[currentRisk.row - 1][currentRisk.column]);
            newPaths.add(new Path(newPathRisks, path.totalRisk + risks[currentRisk.row - 1][currentRisk.column].risk));
        }
        if (canGoDown(path, paths)) {
            List<Risk> newPathRisks = new ArrayList<>(path.pathRisks);
            newPathRisks.add(risks[currentRisk.row + 1][currentRisk.column]);
            newPaths.add(new Path(newPathRisks, path.totalRisk + risks[currentRisk.row + 1][currentRisk.column].risk));
        }
        if (canGoLeft(path, paths)) {
            List<Risk> newPathRisks = new ArrayList<>(path.pathRisks);
            newPathRisks.add(risks[currentRisk.row][currentRisk.column - 1]);
            newPaths.add(new Path(newPathRisks, path.totalRisk + risks[currentRisk.row][currentRisk.column - 1].risk));
        }
        if (canGoRight(path, paths)) {
            List<Risk> newPathRisks = new ArrayList<>(path.pathRisks);
            newPathRisks.add(risks[currentRisk.row][currentRisk.column + 1]);
            newPaths.add(new Path(newPathRisks, path.totalRisk + risks[currentRisk.row][currentRisk.column + 1].risk));
        }
        return newPaths;
    }

    private boolean canGoUp(Path path, List<Path> paths) {
        Risk currentRisk = path.pathRisks.get(path.pathRisks.size() - 1);
        if (currentRisk.row > 0) {
            Risk nextRisk = risks[currentRisk.row - 1][currentRisk.column];
            return !path.pathRisks.contains(nextRisk) && paths.stream().noneMatch(path1 -> path1.pathRisks.contains(nextRisk) && path1.totalRisk <= path.totalRisk + nextRisk.risk);
        } else {
            return false;
        }
    }

    private boolean canGoDown(Path path, List<Path> paths) {
        Risk currentRisk = path.pathRisks.get(path.pathRisks.size() - 1);
        if (currentRisk.row < risks.length - 1) {
            Risk nextRisk = risks[currentRisk.row + 1][currentRisk.column];
            return !path.pathRisks.contains(nextRisk) && paths.stream().noneMatch(path1 -> path1.pathRisks.contains(nextRisk) && path1.totalRisk <= path.totalRisk + nextRisk.risk);
        } else {
            return false;
        }
    }

    private boolean canGoLeft(Path path, List<Path> paths) {
        Risk currentRisk = path.pathRisks.get(path.pathRisks.size() - 1);
        if (currentRisk.column > 0) {
            Risk nextRisk = risks[currentRisk.row][currentRisk.column - 1];
            return !path.pathRisks.contains(nextRisk) && paths.stream().noneMatch(path1 -> path1.pathRisks.contains(nextRisk) && path1.totalRisk <= path.totalRisk + nextRisk.risk);
        } else {
            return false;
        }
    }

    private boolean canGoRight(Path path, List<Path> paths) {
        Risk currentRisk = path.pathRisks.get(path.pathRisks.size() - 1);
        if (currentRisk.column < risks[currentRisk.row].length - 1) {
            Risk nextRisk = risks[currentRisk.row][currentRisk.column + 1];
            return !path.pathRisks.contains(nextRisk) && paths.stream().noneMatch(path1 -> path1.pathRisks.contains(nextRisk) && path1.totalRisk <= path.totalRisk + nextRisk.risk);
        } else {
            return false;
        }
    }

    private Path getFirstPath() {
        List<Risk> pathRisks = new ArrayList<>();
        Risk firstRisk = risks[0][0];
        pathRisks.add(firstRisk);
        Path path = new Path(pathRisks, firstRisk.risk);
        return path;
    }

    private void fillRisks(List<String> input) {
        risks = new Risk[input.size()][];
        for (int i = 0; i < input.get(0).length(); i++) {
            String line = input.get(i);
            risks[i] = new Risk[line.length()];
            for (int j = 0; j < line.length(); j++) {
                risks[i][j] = new Risk(i, j, Integer.parseInt(String.valueOf(line.charAt(j))));
            }
        }
        risks[0][0].risk = 0;
    }

    private void fillDestination() {
        destination = risks[risks.length - 1][risks[risks.length - 1].length - 1];
    }

    private void resetVariables() {
        risks = new Risk[0][0];
    }

    private class Path {
        List<Risk> pathRisks;
        int totalRisk;

        private Path(List<Risk> pathRisks, int totalRisk) {
            this.pathRisks = pathRisks;
            this.totalRisk = totalRisk;
        }
    }

    private class Risk {
        int row;
        int column;
        int risk;

        private Risk(int row, int column, int risk) {
            this.row = row;
            this.column = column;
            this.risk = risk;
        }
    }


}
