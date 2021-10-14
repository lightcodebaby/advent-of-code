package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Day12 {

    /*
    --- Day 12: Leonardo's Monorail ---
    You finally reach the top floor of this building: a garden with a slanted glass ceiling. Looks like there are no more stars to be had.

    While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the files you extracted from the servers downstairs.

    According to these documents, Easter Bunny HQ isn't just this building - it's a collection of buildings in the nearby area. They're all connected by a local monorail, and there's another building not far from here! Unfortunately, being night, the monorail is currently not operating.

    You remotely connect to the monorail control systems and discover that the boot sequence expects a password. The password-checking logic (your puzzle input) is easy to extract, but the code it uses is strange: it's assembunny code designed for the new computer you just assembled. You'll have to execute the code and get the password.

    The assembunny code you've extracted operates on four registers (a, b, c, and d) that start at 0 and can hold any integer. However, it seems to make use of only a few instructions:

    cpy x y copies x (either an integer or the value of a register) into register y.
    inc x increases the value of register x by one.
    dec x decreases the value of register x by one.
    jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
    The jnz instruction moves relative to itself: an offset of -1 would continue at the previous instruction, while an offset of 2 would skip over the next instruction.

    For example:

    cpy 41 a
    inc a
    inc a
    dec a
    jnz a 2
    dec a
    The above code would set register a to 41, increase its value by 2, decrease its value by 1, and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42. When you move past the last instruction, the program halts.

    After executing the assembunny code in your puzzle input, what value is left in register a?
     */

    private HashMap<Character, Integer> values = new HashMap<>();

    public void solve() throws IOException {
//        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day12.txt");
        int currentInstruction = 0;
        while (currentInstruction < lines.size()) {
            String instruction = lines.get(currentInstruction);
            currentInstruction += followInstruction(instruction);
        }
        System.out.println("Part 1 solution: " + values.get('a'));
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day12.txt");
        int currentInstruction = 0;
        String newInstruction = "cpy 1 c";
        followInstruction(newInstruction);
        while (currentInstruction < lines.size()) {
            String instruction = lines.get(currentInstruction);
            currentInstruction += followInstruction(instruction);
        }
        System.out.println("Part 2 solution: " + values.get('a'));
    }

    private int followInstruction(String instruction) {
        int result = 1;
        if (isCopyInstruction(instruction)) {
            followCopyInstruction(instruction);
        } else if (isIncreaseInstruction(instruction)) {
            followIncreaseInstruction(instruction);
        } else if (isDecreaseInstruction(instruction)) {
            followDecreaseInstruction(instruction);
        } else if (isJumpInstruction(instruction)) {
            if(checkBeforeJumping(instruction)) {
                result = howManyToJump(instruction);
            }
        }
        return result;
    }

    private void followCopyInstruction(String instruction) {
        char whereToCopy = getWhereToCopy(instruction);
        int whatToCopy = getValueToCopy(instruction);
        values.put(whereToCopy, whatToCopy);
    }

    private void followIncreaseInstruction(String instruction) {
        char whatToIncrease = whatToIncrease(instruction);
        values.put(whatToIncrease, values.get(whatToIncrease) + 1);
    }

    private void followDecreaseInstruction(String instruction) {
        char whatToIncrease = whatToDecrease(instruction);
        values.put(whatToIncrease, values.get(whatToIncrease) - 1);
    }

    private boolean isCopyInstruction(String instruction) {
        return instruction.startsWith("cpy ");
    }

    private int getValueToCopy(String instruction) {
        int result = 0;
        if(Utilities.isNumber(instruction.split(" ")[1])) {
            result = Integer.parseInt(instruction.split(" ")[1]);
        } else {
            result = values.get(instruction.split(" ")[1].charAt(0));
        }
        return result;
    }

    private char getWhereToCopy(String instruction) {
        return instruction.split(" ")[2].charAt(0);
    }

    private boolean isIncreaseInstruction(String instruction) {
        return instruction.startsWith("inc ");
    }

    private char whatToIncrease(String instruction) {
        return instruction.split(" ")[1].charAt(0);
    }

    private boolean isDecreaseInstruction(String instruction) {
        return instruction.startsWith("dec ");
    }

    private char whatToDecrease(String instruction) {
        return instruction.split(" ")[1].charAt(0);
    }

    private boolean isJumpInstruction(String instruction) {
        return instruction.startsWith("jnz ");
    }

    private boolean checkBeforeJumping(String instruction) {
        boolean jump = false;
        if(Utilities.isNumber(instruction.split(" ")[1])) {
            if(Integer.parseInt(instruction.split(" ")[1]) != 0) {
                jump = true;
            }
        } else {
            char c = instruction.split(" ")[1].charAt(0);
            jump = values.containsKey(c) && values.get(c) != 0;
        }
        return jump;
    }

    private int howManyToJump(String instruction) {
        return Integer.parseInt(instruction.split(" ")[2]);
    }

}
