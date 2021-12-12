package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

    /*
    --- Day 12: Passage Pathing ---
    With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is to find all of them.

    Fortunately, the sensors are still mostly working, and so you build a rough map of the remaining caves (your puzzle input). For example:

    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
    This is a list of how all of the caves are connected. You start in the cave named start, and your destination is the cave named end. An entry like b-d means that cave b is connected to cave d - that is, you can move between them.

    So, the above cave system looks roughly like this:

        start
        /   \
    c--A-----b--d
        \   /
         end
    Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in lowercase, like b). It would be a waste of time to visit any small cave more than once, but big caves are large enough that it might be worth visiting them multiple times. So, all paths you find should visit small caves at most once, and can visit big caves any number of times.

    Given these rules, there are 10 paths through this example cave system:

    start,A,b,A,c,A,end
    start,A,b,A,end
    start,A,b,end
    start,A,c,A,b,A,end
    start,A,c,A,b,end
    start,A,c,A,end
    start,A,end
    start,b,A,c,A,end
    start,b,A,end
    start,b,end
    (Each line in the above list corresponds to a single path; the caves visited by that path are listed in the order they are visited and separated by commas.)

    Note that in this cave system, cave d is never visited by any path: to do so, cave b would need to be visited twice (once on the way to cave d and a second time when returning from cave d), and since cave b is small, this is not allowed.

    Here is a slightly larger example:

    dc-end
    HN-start
    start-kj
    dc-start
    dc-HN
    LN-dc
    HN-end
    kj-sa
    kj-HN
    kj-dc
    The 19 paths through it are as follows:

    start,HN,dc,HN,end
    start,HN,dc,HN,kj,HN,end
    start,HN,dc,end
    start,HN,dc,kj,HN,end
    start,HN,end
    start,HN,kj,HN,dc,HN,end
    start,HN,kj,HN,dc,end
    start,HN,kj,HN,end
    start,HN,kj,dc,HN,end
    start,HN,kj,dc,end
    start,dc,HN,end
    start,dc,HN,kj,HN,end
    start,dc,end
    start,dc,kj,HN,end
    start,kj,HN,dc,HN,end
    start,kj,HN,dc,end
    start,kj,HN,end
    start,kj,dc,HN,end
    start,kj,dc,end
    Finally, this even larger example has 226 paths through it:

    fs-end
    he-DX
    fs-he
    start-DX
    pj-DX
    end-zg
    zg-sl
    zg-pj
    pj-he
    RW-he
    fs-DX
    pj-RW
    zg-RW
    start-pj
    he-WI
    zg-he
    pj-fs
    start-RW
    How many paths through this cave system are there that visit small caves at most once?

    --- Part Two ---
    After reviewing the available paths, you realize you might have time to visit a single small cave twice. Specifically, big caves can be visited any number of times, a single small cave can be visited at most twice, and the remaining small caves can be visited at most once. However, the caves named start and end can only be visited exactly once each: once you leave the start cave, you may not return to it, and once you reach the end cave, the path must end immediately.

    Now, the 36 possible paths through the first example above are:

    start,A,b,A,b,A,c,A,end
    start,A,b,A,b,A,end
    start,A,b,A,b,end
    start,A,b,A,c,A,b,A,end
    start,A,b,A,c,A,b,end
    start,A,b,A,c,A,c,A,end
    start,A,b,A,c,A,end
    start,A,b,A,end
    start,A,b,d,b,A,c,A,end
    start,A,b,d,b,A,end
    start,A,b,d,b,end
    start,A,b,end
    start,A,c,A,b,A,b,A,end
    start,A,c,A,b,A,b,end
    start,A,c,A,b,A,c,A,end
    start,A,c,A,b,A,end
    start,A,c,A,b,d,b,A,end
    start,A,c,A,b,d,b,end
    start,A,c,A,b,end
    start,A,c,A,c,A,b,A,end
    start,A,c,A,c,A,b,end
    start,A,c,A,c,A,end
    start,A,c,A,end
    start,A,end
    start,b,A,b,A,c,A,end
    start,b,A,b,A,end
    start,b,A,b,end
    start,b,A,c,A,b,A,end
    start,b,A,c,A,b,end
    start,b,A,c,A,c,A,end
    start,b,A,c,A,end
    start,b,A,end
    start,b,d,b,A,c,A,end
    start,b,d,b,A,end
    start,b,d,b,end
    start,b,end
    The slightly larger example above now has 103 paths through it, and the even larger example now has 3509 paths through it.

    Given these new rules, how many paths through this cave system are there?
     */

    private List<Coordinate> coordinates = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day12.txt");
        fillCoordinates(input);
        int result = move(input);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day12.txt");
        fillCoordinates(input);
        int result = move2(input);
        System.out.println("Part 1 solution: " + result);
    }

    private int move2(List<String> input) {
        int result = 0;
        List<List<Coordinate>> paths = new ArrayList<>();
        paths.add(getFirstPath());
        while (!paths.isEmpty()) {
            List<List<Coordinate>> newPaths = new ArrayList<>();
            for (List<Coordinate> path : paths) {
                List<Coordinate> connections = connections(path.get(path.size() - 1), input);
                for (Coordinate connection : connections) {
                    if (!connection.isLowercase || !path.contains(connection) || (!connection.identifier.equals("start") && !connection.identifier.equals("end") && !hasVisitedTwiceASmallCave(path))) {
                        List<Coordinate> newPath = new ArrayList<>(path);
                        newPath.add(connection);
                        newPaths.add(newPath);
                    }
                }
            }
            paths = newPaths.stream().filter(coordinates1 -> !coordinates1.get(coordinates1.size() - 1).identifier.equals("end")).collect(Collectors.toList());
            result += newPaths.size() - paths.size();
        }
        return result;
    }

    private boolean hasVisitedTwiceASmallCave(List<Coordinate> path) {
        boolean result = false;
        for(int i = 0; i < path.size() && !result; i++) {
            Coordinate coordinate = path.get(i);
            if(coordinate.isLowercase && path.stream().filter(coordinate1 -> coordinate1.identifier.equals(coordinate.identifier)).count() > 1) {
                result = true;
            }
        }
        return result;
    }

    private int move(List<String> input) {
        List<List<Coordinate>> paths = new ArrayList<>();
        paths.add(getFirstPath());
        List<List<Coordinate>> unfinishedPaths = paths.stream().filter(coordinates1 -> !coordinates1.get(coordinates1.size() - 1).identifier.equals("end")).collect(Collectors.toList());
        while (!unfinishedPaths.isEmpty()) {
            for (List<Coordinate> path : unfinishedPaths) {
                List<Coordinate> connections = connections(path.get(path.size() - 1), input);
                for (Coordinate connection : connections) {
                    if (!connection.isLowercase || !path.contains(connection)) {
                        List<Coordinate> newPath = new ArrayList<>(path);
                        newPath.add(connection);
                        paths.add(newPath);
                    }
                }
            }
            paths.removeIf(unfinishedPaths::contains);
            unfinishedPaths = paths.stream().filter(coordinates1 -> !coordinates1.get(coordinates1.size() - 1).identifier.equals("end")).collect(Collectors.toList());
        }
        return paths.size();
    }

    private List<Coordinate> connections(Coordinate coordinate, List<String> input) {
        List<Coordinate> result = new ArrayList<>();
        for (String pair : input) {
            if (pair.contains(coordinate.identifier + "-") || pair.contains("-" + coordinate.identifier)) {
                String connection = pair.replaceAll("-", "").replaceAll(coordinate.identifier, "");
                result.add(coordinates.stream().filter(coordinate1 -> coordinate1.identifier.equals(connection)).findFirst().get());
            }
        }
        return result;
    }

    private List<Coordinate> getFirstPath() {
        List<Coordinate> result = new ArrayList<>();
        result.add(coordinates.stream().filter(coordinate -> coordinate.identifier.equals("start")).findFirst().get());
        return result;
    }

    private void fillCoordinates(List<String> input) {
        for (String pair : input) {
            for (String identifier : pair.split("-")) {
                if (coordinates.stream().noneMatch(coordinate -> coordinate.identifier.equals(identifier))) {
                    coordinates.add(new Coordinate(identifier));
                }
            }
        }
    }

    private void resetVariables() {
        coordinates = new ArrayList<>();
    }

    private class Coordinate {

        String identifier;
        boolean isLowercase;

        private Coordinate(String identifier) {
            this.identifier = identifier;
            this.isLowercase = identifier.toLowerCase().equals(identifier);
        }

    }

}
