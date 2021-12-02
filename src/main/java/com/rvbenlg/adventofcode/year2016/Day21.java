package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day21 {

    /*
    --- Day 21: Scrambled Letters and Hash ---
    The computer system you're breaking into uses a weird scrambling function to store its passwords. It shouldn't be much trouble to create your own scrambled password so you can add it to the system; you just have to implement the scrambler.

    The scrambling function is a series of operations (the exact list is provided in your puzzle input). Starting with the password to be scrambled, apply each operation in succession to the string. The individual operations behave as follows:

    swap position X with position Y means that the letters at indexes X and Y (counting from 0) should be swapped.
    swap letter X with letter Y means that the letters X and Y should be swapped (regardless of where they appear in the string).
    rotate left/right X steps means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
    rotate based on position of letter X means that the whole string should be rotated to the right based on the index of letter X (counting from 0) as determined before this instruction does any rotations. Once the index is determined, rotate the string to the right one time, plus a number of times equal to that index, plus one additional time if the index was at least 4.
    reverse positions X through Y means that the span of letters at indexes X through Y (including the letters at X and Y) should be reversed in order.
    move position X to position Y means that the letter which is at index X should be removed from the string, then inserted such that it ends up at index Y.
    For example, suppose you start with abcde and perform the following operations:

    swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
    swap letter d with letter b swaps the positions of d and b: edcba.
    reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
    rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
    move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
    move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
    rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
    rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
    After these steps, the resulting scrambled password is decab.

    Now, you just need to generate a new scrambled password and you can access the system. Given the list of scrambling operations in your puzzle input, what is the result of scrambling abcdefgh?

    --- Part Two ---
    You scrambled the password correctly, but you discover that you can't actually modify the password file on the system. You'll need to un-scramble one of the existing passwords by reversing the scrambling process.

    What is the un-scrambled version of the scrambled password fbgdceah?
     */

    private final String[] instructions = {"swap positions", "swap letter", "reverse positions", "rotate left", "rotate right", "move position", "rotate based on position of letter"};
    private String toScramble = "abcdefgh";
    private String toUnscramble = "fbgdceah";
    private StringBuilder scrambleBuilder = new StringBuilder();


    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day21.txt");
        resetVariables(1);
        for (String instruction : input) {
            scramble(instruction);
        }
        System.out.println("Part 1 solution: " + scrambleBuilder.toString());
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day21.txt");
        resetVariables(2);
        for (int i = input.size() - 1; i >= 0; i--) {
            String instruction = input.get(i);
            unscramble(instruction);
        }
        System.out.println("Part 2 solution: " + scrambleBuilder.toString());
    }

    private void unscramble(String instruction) {
        int instructionType = identifyInstructionType(instruction);
        switch (instructionType) {
            case 0:
                swapPositions(instruction);
                break;
            case 1:
                swapLetters(instruction);
                break;
            case 2:
                reversePositions(instruction);
                break;
            case 3:
                unrotateLeft(instruction);
                break;
            case 4:
                unrotateRight(instruction);
                break;
            case 5:
                unmovePositions(instruction);
                break;
            case 6:
                unrotateBasedOnPosition(instruction);
                break;
        }
    }

    private void scramble(String instruction) {
        int instructionType = identifyInstructionType(instruction);
        switch (instructionType) {
            case 0:
                swapPositions(instruction);
                break;
            case 1:
                swapLetters(instruction);
                break;
            case 2:
                reversePositions(instruction);
                break;
            case 3:
                rotateLeft(instruction);
                break;
            case 4:
                rotateRight(instruction);
                break;
            case 5:
                movePositions(instruction);
                break;
            case 6:
                rotateBasedOnPosition(instruction);
                break;
        }
    }

    private void rotateBasedOnPosition(String instruction) {
        String[] elements = instruction.split(" ");
        String letter = elements[6];
        int steps = scrambleBuilder.indexOf(letter);
        steps += steps >= 4 ? 2 : 1;
        String auxInstruction = "rotate right " + steps + " steps";
        rotateRight(auxInstruction);
    }

    private void unrotateBasedOnPosition(String instruction) {
        String[] elements = instruction.split(" ");
        String letter = elements[6];
        String auxInstruction = "rotate right 1 steps";
        int steps = 0;
        int hipotheticalSteps = 0;
        do {
            rotateLeft(auxInstruction);
            steps++;
            int index = scrambleBuilder.indexOf(letter);
            hipotheticalSteps = index + (index >= 4 ? 2 : 1);
        } while(steps != hipotheticalSteps);
    }

    private void movePositions(String instruction) {
        String[] elements = instruction.split(" ");
        int positionA = Integer.parseInt(elements[2]);
        int positionB = Integer.parseInt(elements[5]);
        char toMove = scrambleBuilder.charAt(positionA);
        scrambleBuilder.replace(positionA, positionA+1, "");
        scrambleBuilder.insert(positionB, toMove);
    }

    private void unmovePositions(String instruction) {
        String[] elements = instruction.split(" ");
        int positionA = Integer.parseInt(elements[2]);
        int positionB = Integer.parseInt(elements[5]);
        char toMove = scrambleBuilder.charAt(positionB);
        scrambleBuilder.replace(positionB, positionB+1, "");
        scrambleBuilder.insert(positionA, toMove);
    }

    private void rotateLeft(String instruction) {
        String[] elements = instruction.split(" ");
        int steps = Integer.parseInt(elements[2]);
        for (int i = 0; i < steps; i++) {
            StringBuilder aux = new StringBuilder();
            for (int j = 0; j < scrambleBuilder.length(); j++) {
                if(j == scrambleBuilder.length() - 1) {
                    aux.append(scrambleBuilder.charAt(0));
                } else {
                    aux.append(scrambleBuilder.charAt(j + 1));
                }
            }
            scrambleBuilder = new StringBuilder(aux);
        }
    }

    private void unrotateLeft(String instruction) {
        String[] elements = instruction.split(" ");
        int steps = Integer.parseInt(elements[2]);
        String auxInstruction = "rotate right " + steps + " steps";
        rotateRight(auxInstruction);
    }

    private void rotateRight(String instruction) {
        String[] elements = instruction.split(" ");
        int steps = Integer.parseInt(elements[2]);
        for (int i = 0; i < steps; i++) {
            StringBuilder aux = new StringBuilder();
            for (int j = 0; j < scrambleBuilder.length(); j++) {
                if(j == 0) {
                    aux.append(scrambleBuilder.charAt(scrambleBuilder.length() - 1));
                } else {
                    aux.append(scrambleBuilder.charAt(j - 1));
                }
            }
            scrambleBuilder = new StringBuilder(aux);
        }
    }

    private void unrotateRight(String instruction) {
        String[] elements = instruction.split(" ");
        int steps = Integer.parseInt(elements[2]);
        String auxInstruction = "rotate left " + steps + " steps";
        rotateLeft(auxInstruction);
    }

    private void reversePositions(String instruction) {
        String[] elements = instruction.split(" ");
        int positionA = Integer.parseInt(elements[2]);
        int positionB = Integer.parseInt(elements[4]);
        scrambleBuilder.replace(positionA, positionB + 1, new StringBuilder(scrambleBuilder.substring(positionA, positionB + 1)).reverse().toString());
    }

    private void swapLetters(String instruction) {
        String[] elements = instruction.split(" ");
        char letterA = elements[2].charAt(0);
        char letterB = elements[5].charAt(0);
        String aux = scrambleBuilder.toString();
        for (int i = 0; i < aux.length(); i++) {
            if (aux.charAt(i) == letterA) {
                aux = aux.substring(0, i) + letterB + aux.substring(i + 1);
            } else if (aux.charAt(i) == letterB) {
                aux = aux.substring(0, i) + letterA + aux.substring(i + 1);
            }
        }
        scrambleBuilder = new StringBuilder(aux);
    }


    private void swapPositions(String instruction) {
        String[] elements = instruction.split(" ");
        int positionA = Integer.parseInt(elements[2]);
        int positionB = Integer.parseInt(elements[5]);
        char a = scrambleBuilder.charAt(positionA);
        char b = scrambleBuilder.charAt(positionB);
        scrambleBuilder.setCharAt(positionA, b);
        scrambleBuilder.setCharAt(positionB, a);
    }

    private int identifyInstructionType(String instruction) {
        int result = 0;
        for (int i = 0; i < instructions.length; i++) {
            if (instruction.contains(instructions[i])) {
                result = i;
            }
        }
        return result;
    }

    private void resetVariables(int part) {
        scrambleBuilder = new StringBuilder(part == 1 ? toScramble : toUnscramble);
    }

}
