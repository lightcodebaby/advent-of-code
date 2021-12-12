package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {

    /*
    --- Day 17: Two Steps Forward ---
    You're trying to access a secure vault protected by a 4x4 grid of small rooms connected by doors. You start in the top-left room (marked S), and you can access the vault (marked V) once you reach the bottom-right room:

    #########
    #S| | | #
    #-#-#-#-#
    # | | | #
    #-#-#-#-#
    # | | | #
    #-#-#-#-#
    # | | |
    ####### V
    Fixed walls are marked with #, and doors are marked with - or |.

    The doors in your current room are either open or closed (and locked) based on the hexadecimal MD5 hash of a passcode (your puzzle input) followed by a sequence of uppercase characters representing the path you have taken so far (U for up, D for down, L for left, and R for right).

    Only the first four characters of the hash are used; they represent, respectively, the doors up, down, left, and right from your current position. Any b, c, d, e, or f means that the corresponding door is open; any other character (any number or a) means that the corresponding door is closed and locked.

    To access the vault, all you need to do is reach the bottom-right room; reaching this room opens the vault and all doors in the maze.

    For example, suppose the passcode is hijkl. Initially, you have taken no steps, and so your path is empty: you simply find the MD5 hash of hijkl alone. The first four characters of this hash are ced9, which indicate that up is open (c), down is open (e), left is open (d), and right is closed and locked (9). Because you start in the top-left corner, there are no "up" or "left" doors to be open, so your only choice is down.

    Next, having gone only one step (down, or D), you find the hash of hijklD. This produces f2bc, which indicates that you can go back up, left (but that's a wall), or right. Going right means hashing hijklDR to get 5745 - all doors closed and locked. However, going up instead is worthwhile: even though it returns you to the room you started in, your path would then be DU, opening a different set of doors.

    After going DU (and then hashing hijklDU to get 528e), only the right door is open; after going DUR, all doors lock. (Fortunately, your actual passcode is not hijkl).

    Passcodes actually used by Easter Bunny Vault Security do allow access to the vault if you know the right path. For example:

    If your passcode were ihgpwlah, the shortest path would be DDRRRD.
    With kglvqrro, the shortest path would be DDUDRLRRUDRD.
    With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.
    Given your vault's passcode, what is the shortest path (the actual path, not just the length) to reach the vault?
     */

    private int destX;
    private int destY;
    List<Path> paths;

    public void solve() throws IOException, NoSuchAlgorithmException {
        part1();
        part2();
    }

    private void part1() throws IOException, NoSuchAlgorithmException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day17.txt");
        String passcode = input.get(0);
        paths.add(new Path(0, 0, passcode, getPathHash(passcode)));
        String result = move();
        System.out.println("Part 1 solution: " + result.substring(passcode.length()));
    }

    private void part2() throws NoSuchAlgorithmException, IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day17.txt");
        String passcode = input.get(0);
        paths.add(new Path(0, 0, passcode, getPathHash(passcode)));
        String result = findLongestPath();
        System.out.println("Part 2 solution: " + (result.length() - passcode.length()));
    }

    private String move() throws NoSuchAlgorithmException {
        while (paths.stream().noneMatch(path -> path.posX == destX && path.posY == destY)) {
            List<Path> newPaths = paths.stream().filter(path -> !path.checked).collect(Collectors.toList());
            for (Path path : newPaths) {
                if (canGoUp(path)) {
                    paths.add(new Path(path.posX, path.posY - 1, path.path + "U", getPathHash(path.path + "U")));
                }
                if (canGoDown(path)) {
                    paths.add(new Path(path.posX, path.posY + 1, path.path + "D", getPathHash(path.path + "D")));
                }
                if (canGoLeft(path)) {
                    paths.add(new Path(path.posX - 1, path.posY, path.path + "L", getPathHash(path.path + "L")));
                }
                if (canGoRight(path)) {
                    paths.add(new Path(path.posX + 1, path.posY, path.path + "R", getPathHash(path.path + "R")));
                }
                path.checked = true;
            }
        }
        String result = paths.stream().filter(path -> path.posX == destX && path.posY == destY).findFirst().get().path;
        return result;
    }

    private String findLongestPath() {
        int length = 0;
        List<Path> newPaths = paths.stream().filter(path -> !path.checked && (path.posX != destX || path.posY != destY) && !isLocked(path)).collect(Collectors.toList());
        while (!newPaths.isEmpty()) {
            for (Path path : newPaths) {
                if (path.posX != destX || path.posY != destY) {
                    if (canGoUp(path)) {
                        paths.add(new Path(path.posX, path.posY - 1, path.path + "U", getPathHash(path.path + "U")));
                    }
                    if (canGoDown(path)) {
                        paths.add(new Path(path.posX, path.posY + 1, path.path + "D", getPathHash(path.path + "D")));
                    }
                    if (canGoLeft(path)) {
                        paths.add(new Path(path.posX - 1, path.posY, path.path + "L", getPathHash(path.path + "L")));
                    }
                    if (canGoRight(path)) {
                        paths.add(new Path(path.posX + 1, path.posY, path.path + "R", getPathHash(path.path + "R")));
                    }
                }
                path.checked = true;
            }
            clearPaths();
            length++;
            newPaths = paths.stream().filter(path -> !path.checked && (path.posX != destX || path.posY != destY) && !isLocked(path)).collect(Collectors.toList());
        }
        List<Path> destPaths = paths.stream().filter(path -> path.posX == destX && path.posY == destY).collect(Collectors.toList());
        String result = destPaths.stream().filter(path -> destPaths.stream().map(Path::getPath).noneMatch(destPath -> destPath.length() > path.getPath().length())).findFirst().get().path;
        return result;
    }

    private void clearPaths() {
        paths.removeIf(path -> path.checked && (path.posX != destX || path.posY != destY));
        paths.removeIf(path -> isLocked(path) && (path.posX != destX || path.posY != destY));
    }

    private boolean canGoUp(Path path) {
        return path.posY > 0 && path.hash.charAt(0) >= 'b' && path.hash.charAt(0) <= 'f';
    }

    private boolean canGoDown(Path path) {
        return path.posY < 3 && path.hash.charAt(1) >= 'b' && path.hash.charAt(1) <= 'f';
    }

    private boolean canGoLeft(Path path) {
        return path.posX > 0 && path.hash.charAt(2) >= 'b' && path.hash.charAt(2) <= 'f';
    }

    private boolean canGoRight(Path path) {
        return path.posX < 3 && path.hash.charAt(3) >= 'b' && path.hash.charAt(3) <= 'f';
    }

    private boolean isLocked(Path path) {
        return !canGoUp(path) && !canGoDown(path) && !canGoLeft(path) && !canGoRight(path);
    }

    private String getPathHash(String path) {
        try {
            return Utilities.md5(path).substring(0, 4);
        } catch (Exception e) {
            return "";
        }

    }

    private void resetVariables() {
        destX = 3;
        destY = 3;
        paths = new ArrayList<>();
    }

    private class Path {

        int posX;
        int posY;
        String path;
        String hash;
        boolean checked;

        private Path(int posX, int posY, String path, String hash) {
            this.posX = posX;
            this.posY = posY;
            this.path = path;
            this.hash = hash;
            this.checked = false;
        }

        private String getPath() {
            return this.path;
        }

    }

}
