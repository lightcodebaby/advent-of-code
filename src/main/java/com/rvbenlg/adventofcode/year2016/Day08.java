package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day08 {

    /*
    --- Day 8: Two-Factor Authentication ---
    You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.

    To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.

    Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.

    The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three somewhat peculiar operations:

    rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
    rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
    rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
    For example, here is a simple sequence on a smaller screen:

    rect 3x2 creates a small rectangle in the top-left corner:

    ###....
    ###....
    .......
    rotate column x=1 by 1 rotates the second column down by one pixel:

    #.#....
    ###....
    .#.....
    rotate row y=0 by 4 rotates the top row right by four pixels:

    ....#.#
    ###....
    .#.....
    rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:

    .#..#.#
    #.#....
    .#.....
    As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.

    There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?

    --- Part Two ---
    You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter is 5 pixels wide and 6 tall.

    After you swipe your card, what code is the screen trying to display?
     */

    private final int rows = 6;
    private final int columns = 50;
    private boolean screen[][] = new boolean[rows][columns];

    public void solve() throws IOException {
        part1();
        part2();
    }


    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day08.txt");
        resetScreen();
        for(String line : lines) {
            followInstruction(line);
        }
        int litPixels = howManyPixelsAreLit();
        System.out.println("Part 1 solution: " + litPixels);
    }

    private void part2() {
        System.out.println("Part 2 solution: ");
        printScreen();
    }

    private int howManyPixelsAreLit() {
        int count = 0;
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++) {
                if(screen[row][column]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void followInstruction(String line) {
        if(isCreateRect(line)) {
            int wide = getRectangleWide(line);
            int tall = getRectangleTall(line);
            createRectangle(wide, tall);;
        } else if(isRotateColumn(line)) {
            int column = getColumnToRotate(line);
            int by = howManyToRotateColumn(line);
            rotateColumn(column, by);
        } else if(isRotateRow(line)) {
            int row = getRowToRotate(line);
            int by = howManyToRotateRow(line);
            rotateRow(row, by);
        }
    }

    private void createRectangle(int wide, int tall) {
        for(int row = 0; row < tall; row++) {
            for(int column = 0; column < wide; column++) {
                screen[row][column] = true;
            }
        }
    }

    private void rotateColumn(int column, int by) {
        boolean[][] auxScreen = getAuxScreen2();
        for(int row = 0; row < rows; row++) {
            screen[row][column] = auxScreen[Math.abs(rows + row - by) % rows][column];
        }
    }

    private void rotateRow(int row, int by) {
        boolean[][] auxScreen = getAuxScreen2();
        for(int column = 0; column < columns; column++) {
            screen[row][column] = auxScreen[row][Math.abs(columns + column - by) % columns];
        }
    }

    private boolean isCreateRect(String instruction) {
        return instruction.startsWith("rect ");
    }

    private boolean isRotateRow(String instruction){
        return instruction.startsWith("rotate row y=");
    }

    private boolean isRotateColumn(String instruction){
        return instruction.startsWith("rotate column x=");
    }

    private int getRectangleWide(String instruction) {
        instruction = instruction.replaceAll("rect ", "");
        return Integer.parseInt(instruction.split("x")[0]);
    }

    private int getRectangleTall(String instruction) {
        instruction = instruction.replaceAll("rect ", "");
        return Integer.parseInt(instruction.split("x")[1]);
    }

    private int getRowToRotate(String instruction) {
        instruction = instruction.replaceAll("rotate row y=", "");
        return Integer.parseInt(instruction.split(" ")[0]);
    }

    private int howManyToRotateRow(String instruction) {
        instruction = instruction.replaceAll("rotate row y=", "");
        return Integer.parseInt(instruction.split(" ")[2]);
    }

    private int getColumnToRotate(String instruction) {
        instruction = instruction.replaceAll("rotate column x=", "");
        return Integer.parseInt(instruction.split(" ")[0]);
    }

    private int howManyToRotateColumn(String instruction) {
        instruction = instruction.replaceAll("rotate column x=", "");
        return Integer.parseInt(instruction.split(" ")[2]);
    }

    private void resetScreen() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                screen[i][j] = false;
            }
        }
    }

    private boolean[][] getAuxScreen2() {
        boolean[][] auxScreen = new boolean[rows][columns];
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                auxScreen[row][column] = screen[row][column];
            }
        }
        return auxScreen;
    }

    private void printScreen() {
        System.out.println("------------------PRINTING SCREEN------------------");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(screen[i][j] ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println("------------------SCREEN PRINTED------------------");
    }

}
