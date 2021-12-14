package com.rvbenlg.adventofcode.year2015;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day06 {

    /*
    --- Day 6: Probably a Fire Hazard ---
    Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

    Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

    Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.

    To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

    For example:

    turn on 0,0 through 999,999 would turn on (or leave on) every light.
    toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
    turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
    After following the instructions, how many lights are lit?

    --- Part Two ---
    You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

    The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

    The phrase turn on actually means that you should increase the brightness of those lights by 1.

    The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

    The phrase toggle actually means that you should increase the brightness of those lights by 2.

    What is the total brightness of all lights combined after following Santa's instructions?

    For example:

    turn on 0,0 through 0,0 would increase the total brightness by 1.
    toggle 0,0 through 999,999 would increase the total brightness by 2000000.
     */

    private boolean[][] lights = new boolean[1000][1000];
    private int[][] brightness = new int[1000][1000];

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2015/day06.txt");
        input.replaceAll(instruction -> instruction.replaceAll("turn ", ""));
        followInstructions(input);
        int result = howManyLightsAreLit();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2015/day06.txt");
        input.replaceAll(instruction -> instruction.replaceAll("turn ", ""));
        followInstructions2(input);
        int result = getTotalBrightness();
        System.out.println("Part 2 solution: " + result);
    }

    private int getTotalBrightness() {
        int result = 0;
        for (int i = 0; i < brightness.length; i++) {
            for (int j = 0; j < brightness[i].length; j++) {
                result += brightness[i][j];
            }
        }
        return result;
    }

    private void followInstructions2(List<String> input) {
        for (String instruction : input) {
            String[] words = instruction.split(" ");
            String[] from = words[1].split(",");
            String[] to = words[3].split(",");
            int fromX = Integer.parseInt(from[0]);
            int fromY = Integer.parseInt(from[1]);
            int toX = Integer.parseInt(to[0]);
            int toY = Integer.parseInt(to[1]);
            switch (words[0]) {
                case "on":
                    turnOn2(fromX, fromY, toX, toY);
                    break;
                case "off":
                    turnOff2(fromX, fromY, toX, toY);
                    break;
                case "toggle":
                    toggle2(fromX, fromY, toX, toY);
                    break;
            }
        }
    }

    private void turnOn2(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                brightness[i][j] = brightness[i][j] + 1;
            }
        }
    }

    private void turnOff2(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                if (brightness[i][j] > 0) {
                    brightness[i][j] = brightness[i][j] - 1;
                }
            }
        }
    }

    private void toggle2(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                brightness[i][j] = brightness[i][j] + 2;
            }
        }
    }

    private int howManyLightsAreLit() {
        int result = 0;
        for (int i = 0; i < lights.length; i++) {
            for (int j = 0; j < lights[i].length; j++) {
                if (lights[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    private void followInstructions(List<String> input) {
        for (String instruction : input) {
            String[] words = instruction.split(" ");
            String[] from = words[1].split(",");
            String[] to = words[3].split(",");
            int fromX = Integer.parseInt(from[0]);
            int fromY = Integer.parseInt(from[1]);
            int toX = Integer.parseInt(to[0]);
            int toY = Integer.parseInt(to[1]);
            switch (words[0]) {
                case "on":
                    turnOn(fromX, fromY, toX, toY);
                    break;
                case "off":
                    turnOff(fromX, fromY, toX, toY);
                    break;
                case "toggle":
                    toggle(fromX, fromY, toX, toY);
                    break;
            }
        }
    }

    private void turnOn(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                lights[i][j] = true;
            }
        }
    }

    private void turnOff(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                lights[i][j] = false;
            }
        }
    }

    private void toggle(int fromX, int fromY, int toX, int toY) {
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                lights[i][j] = !lights[i][j];
            }
        }
    }

    private void resetVariables() {
        lights = new boolean[1000][1000];
        brightness = new int[1000][1000];
    }

}
