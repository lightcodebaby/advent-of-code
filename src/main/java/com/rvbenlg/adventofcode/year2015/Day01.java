package com.rvbenlg.adventofcode.year2015;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day01 {

    /*
    --- Day 1: Not Quite Lisp ---
    Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars, and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.

    Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

    Here's an easy puzzle to warm you up.

    Santa is trying to deliver presents in a large apartment building, but he can't find the right floor - the directions he got are a little confusing. He starts on the ground floor (floor 0) and then follows the instructions one character at a time.

    An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ), means he should go down one floor.

    The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.

    For example:
    (()) and ()() both result in floor 0.
    ((( and (()(()( both result in floor 3.
    ))((((( also results in floor 3.
    ()) and ))( both result in floor -1 (the first basement level).
    ))) and )())()) both result in floor -3.
    To what floor do the instructions take Santa?

    --- Part Two ---
    Now, given the same instructions, find the position of the first character that causes him to enter the basement (floor -1). The first character in the instructions has position 1, the second character has position 2, and so on.

    For example:

    ) causes him to enter the basement at character position 1.
    ()()) causes him to enter the basement at character position 5.
    What is the position of the character that causes Santa to first enter the basement?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2015/day01.txt");
        long floor = 0;
        for (String line : lines) {
            floor = floor + line.chars().filter(value -> value == '(').count() - line.chars().filter(value -> value == ')').count();
        }
        System.out.println("Part 1 solution: " + floor);
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2015/day01.txt");
        long floor = 0;
        long position = 0;
        for (String line : lines) {
            for (int i = 0; i < line.length() && floor != -1; i++) {
                char instruction = line.charAt(i);
                if (instruction == '(') {
                    floor++;
                } else {
                    floor--;
                }
                position++;
            }
        }
        System.out.println("Part 2 solution: " + position);
    }

}
