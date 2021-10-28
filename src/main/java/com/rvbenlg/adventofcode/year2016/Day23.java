package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day23 {

    private HashMap<Character, Integer> values = new HashMap<>();
    private List<String> instructions = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        instructions = Utilities.readInput("year2016/day23.txt");
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
        if(!Utilities.isNumber(String.valueOf(whereToCopy))) {
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
        if(currentInstruction + howFarToToggle < instructions.size()) {
            String instructionToToggle = instructions.get(currentInstruction + howFarToToggle);
            if (isIncreaseInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("inc ", "dec "));
            } else if (isDecreaseInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("dec ", "inc "));
            } else if (isToggleInstruction(instructionToToggle)) {
                instructions.set(currentInstruction + howFarToToggle, instructionToToggle.replaceAll("tgl ", "inc "));
            } else if(isCopyInstruction(instructionToToggle)) {
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
        if(Utilities.isNumber(instruction.split(" ")[2])) {
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

}