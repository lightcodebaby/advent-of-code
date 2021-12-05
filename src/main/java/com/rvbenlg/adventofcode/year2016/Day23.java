package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day23 {

    /*
    --- Day 23: Safe Cracking ---
    This is one of the top floors of the nicest tower in EBHQ. The Easter Bunny's private office is here, complete with a safe hidden behind a painting, and who wouldn't hide a star in a safe behind a painting?

    The safe has a digital screen and keypad for code entry. A sticky note attached to the safe has a password hint on it: "eggs". The painting is of a large rabbit coloring some eggs. You see 7.

    When you go to type the code, though, nothing appears on the display; instead, the keypad comes apart in your hands, apparently having been smashed. Behind it is some kind of socket - one that matches a connector in your prototype computer! You pull apart the smashed keypad and extract the logic circuit, plug it into your computer, and plug your computer into the safe.

    Now, you just need to figure out what output the keypad would have sent to the safe. You extract the assembunny code from the logic chip (your puzzle input).
    The code looks like it uses almost the same architecture and instruction set that the monorail computer used! You should be able to use the same assembunny interpreter for this as you did there, but with one new instruction:

    tgl x toggles the instruction x away (pointing at instructions like jnz does: positive means forward; negative means backward):

    For one-argument instructions, inc becomes dec, and all other one-argument instructions become inc.
    For two-argument instructions, jnz becomes cpy, and all other two-instructions become jnz.
    The arguments of a toggled instruction are not affected.
    If an attempt is made to toggle an instruction outside the program, nothing happens.
    If toggling produces an invalid instruction (like cpy 1 2) and an attempt is later made to execute that instruction, skip it instead.
    If tgl toggles itself (for example, if a is 0, tgl a would target itself and become inc a), the resulting instruction is not executed until the next time it is reached.
    For example, given this program:

    cpy 2 a
    tgl a
    tgl a
    tgl a
    cpy 1 a
    dec a
    dec a
    cpy 2 a initializes register a to 2.
    The first tgl a toggles an instruction a (2) away from it, which changes the third tgl a into inc a.
    The second tgl a also modifies an instruction 2 away from it, which changes the cpy 1 a into jnz 1 a.
    The fourth line, which is now inc a, increments a to 3.
    Finally, the fifth line, which is now jnz 1 a, jumps a (3) instructions ahead, skipping the dec a instructions.
    In this example, the final value in register a is 3.

    The rest of the electronics seem to place the keypad entry (the number of eggs, 7) in register a, run the code, and then send the value left in register a to the safe.

    What value should be sent to the safe?

    --- Part Two ---
    The safe doesn't open, but it does make several angry noises to express its frustration.

    You're quite sure your logic is working correctly, so the only other thing is... you check the painting again. As it turns out, colored eggs are still eggs. Now you count 12.

    As you run the program with this new input, the prototype computer begins to overheat. You wonder what's taking so long, and whether the lack of any instruction more powerful than "add one" has anything to do with it. Don't bunnies usually multiply?

    Anyway, what value should actually be sent to the safe?
     */


    private HashMap<Character, Integer> values = new HashMap<>();
    private List<String> instructions = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        instructions.add("cpy 7 a");
        instructions.addAll(Utilities.readInput("year2016/day23.txt"));
        int currentInstruction = 0;
        while (currentInstruction < instructions.size()) {
            currentInstruction += followInstruction(currentInstruction);
        }
        System.out.println("Part 1 solution: " + values.get('a'));
    }

    private void part2() throws IOException {
        System.out.println("Please, wait...");
        resetVariables();
        instructions.add("cpy 12 a");
        instructions.addAll(Utilities.readInput("year2016/day23.txt"));
        int currentInstruction = 0;
        while (currentInstruction < instructions.size()) {
            currentInstruction += followInstruction(currentInstruction);
        }
        System.out.println("Part 1 solution: " + values.get('a'));
    }

    private int followInstruction(int currentInstruction) {
        int result = 1;
        String instruction = instructions.get(currentInstruction);
        if (isCopyInstruction(instruction)) {
            followCopyInstruction(instruction);
        } else if (isIncreaseInstruction(instruction)) {
            followIncreaseInstruction(instruction);
        } else if (isDecreaseInstruction(instruction)) {
            followDecreaseInstruction(instruction);
        } else if (isJumpInstruction(instruction)) {
            if (checkBeforeJumping(instruction)) {
                result = howManyToJump(instruction);
            }
        } else if (isToggleInstruction(instruction)) {
            followToggleInstruction(instruction, currentInstruction);
        }
        return result;
    }

    private void followCopyInstruction(String instruction) {
        char whereToCopy = getWhereToCopy(instruction);
        if (!Utilities.isNumber(String.valueOf(whereToCopy))) {
            int whatToCopy = getValueToCopy(instruction);
            values.put(whereToCopy, whatToCopy);
        }
    }

    private void followIncreaseInstruction(String instruction) {
        char whatToIncrease = whatToIncrease(instruction);
        values.put(whatToIncrease, values.get(whatToIncrease) + 1);
    }

    private void followDecreaseInstruction(String instruction) {
        char whatToIncrease = whatToDecrease(instruction);
        values.put(whatToIncrease, values.get(whatToIncrease) - 1);
    }

    private void followToggleInstruction(String instruction, int currentInstruction) {
        int howFarToToggle = howFarToToggle(instruction);
        if (currentInstruction + howFarToToggle < instructions.size()) {
            String instructionToToggle = instructions.get(currentInstruction + howFarToToggle);
            if (isIncreaseInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("inc ", "dec "));
            } else if (isDecreaseInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("dec ", "inc "));
            } else if (isToggleInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("tgl ", "inc "));
            } else if (isCopyInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("cpy ", "jnz "));
            } else if (isJumpInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("jnz ", "cpy "));
            }
        }
    }

    private boolean isCopyInstruction(String instruction) {
        return instruction.startsWith("cpy ");
    }

    private int getValueToCopy(String instruction) {
        int result = 0;
        if (Utilities.isNumber(instruction.split(" ")[1])) {
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
        if (Utilities.isNumber(instruction.split(" ")[1])) {
            if (Integer.parseInt(instruction.split(" ")[1]) != 0) {
                jump = true;
            }
        } else {
            char c = instruction.split(" ")[1].charAt(0);
            jump = values.containsKey(c) && values.get(c) != 0;
        }
        return jump;
    }

    private int howManyToJump(String instruction) {
        int result = 0;
        if (Utilities.isNumber(instruction.split(" ")[2])) {
            result = Integer.parseInt(instruction.split(" ")[2]);
        } else {
            result = values.get(instruction.split(" ")[2].charAt(0));
        }
        return result;
    }

    private boolean isToggleInstruction(String instruction) {
        return instruction.startsWith("tgl ");
    }

    private int howFarToToggle(String instruction) {
        return values.get(instruction.split(" ")[1].charAt(0));
    }

    private void resetVariables() {
        instructions = new ArrayList<>();
        values = new HashMap<>();
    }

}
