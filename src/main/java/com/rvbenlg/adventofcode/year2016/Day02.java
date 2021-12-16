package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day02 {

    /*
    --- Day 2: Bathroom Security ---
    You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.

    "In order to improve security," the document you find says, "bathroom codes will no longer be written down. Instead, please memorize and follow the procedure below to access the bathrooms."

    The document goes on to explain that each button to be pressed can be found by starting on the previous button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.

    You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:

    1 2 3
    4 5 6
    7 8 9
    Suppose your instructions are:

    ULL
    RRDDD
    LURDL
    UUUUD
    You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
    Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
    Continuing from "9", you move left, up, right, down, and left, ending with 8.
    Finally, you move up four times (stopping at "2"), then down once, ending with 5.
    So, in this example, the bathroom code is 1985.

    Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?

    --- Part Two ---
    You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to your bladder's dismay, the keypad is not at all like you imagined it. Instead, you are confronted with the result of hundreds of man-hours of bathroom-keypad-design meetings:

        1
      2 3 4
    5 6 7 8 9
      A B C
        D
    You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is very different:

    You start at "5" and don't move at all (up and left are both edges), ending at 5.
    Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
    Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
    Finally, after five more moves, you end at 3.
    So, given the actual keypad layout, the code would be 5DB3.

    Using the same instructions in your puzzle input, what is the correct bathroom code?
     */

    private final int[][] keypad1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    private final String[][] keypad2 = {{"", "", "1", "", ""}, {"", "2", "3", "4", ""}, {"5", "6", "7", "8", "9"}, {"", "A", "B", "C", ""}, {"", "", "D", "", ""}};
    private int row;
    private int column;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day02.txt");
        resetVariables(1);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : input) {
            stringBuilder.append(followInstructions(line));
        }
        System.out.println("Part 1 solution: " + stringBuilder);
    }

    private String followInstructions(String line) {
        for (char instruction : line.toCharArray()) {
            if (instruction == 'U') {
                moveUp();
            }
            if (instruction == 'D') {
                moveDown();
            }
            if (instruction == 'L') {
                moveLeft();
            }
            if (instruction == 'R') {
                moveRight();
            }
        }
        return String.valueOf(keypad1[row][column]);
    }

    private void moveUp() {
        if (row > 0) {
            row--;
        }
    }

    private void moveDown() {
        if (row < 2) {
            row++;
        }
    }

    private void moveLeft() {
        if (column > 0) {
            column--;
        }
    }

    private void moveRight() {
        if (column < 2) {
            column++;
        }
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day02.txt");
        resetVariables(2);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : input) {
            stringBuilder.append(followInstructions2(line));
        }
        System.out.println("Part 2 solution: " + stringBuilder);
    }

    private String followInstructions2(String line) {
        for (char instruction : line.toCharArray()) {
            if (instruction == 'U') {
                moveUp2();
            }
            if (instruction == 'D') {
                moveDown2();
            }
            if (instruction == 'L') {
                moveLeft2();
            }
            if (instruction == 'R') {
                moveRight2();
            }
        }
        return String.valueOf(keypad2[row][column]);
    }

    private void moveUp2() {
        if (row > 0 && !keypad2[row - 1][column].isEmpty()) {
            row--;
        }
    }

    private void moveDown2() {
        if (row < 4 && !keypad2[row + 1][column].isEmpty()) {
            row++;
        }
    }

    private void moveLeft2() {
        if (column > 0 && !keypad2[row][column - 1].isEmpty()) {
            column--;
        }
    }

    private void moveRight2() {
        if (column < 4 && !keypad2[row][column + 1].isEmpty()) {
            column++;
        }
    }

    private void resetVariables(int part) {
        if (part == 1) {
            row = 1;
            column = 1;
        } else {
            row = 2;
            column = 0;
        }
    }

}
