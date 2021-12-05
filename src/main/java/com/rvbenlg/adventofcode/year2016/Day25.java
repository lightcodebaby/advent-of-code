package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day25 {

    private HashMap<Character, Integer> values = new HashMap<>();
    private List<String> instructions = new ArrayList<>();
    int firstValue = 0;
    int lastPrinted = -1;

    public void solve() throws IOException, InterruptedException {
        part1();
    }

    private void part1() throws IOException, InterruptedException {
        instructions = Utilities.readInput("year2016/day25.txt");
        resetVariables();
        instructions.set(0, "cpy " + firstValue + " a");
        int currentInstruction = 0;
        while (currentInstruction < instructions.size()) {
            currentInstruction += followInstruction(currentInstruction);
        }
    }


    private int followInstruction(int currentInstruction) throws InterruptedException, IOException {
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
        } else if (isTransmitInstruction(instruction)) {
            int whatToPrint = values.get(whatToTransmit(instruction));
            if (whatToPrint == lastPrinted) {
                refreshFirstValue();
                System.out.println("Refreshing first value. New value: " + firstValue);
                part1();
            } else {
                lastPrinted = whatToPrint;
            }

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

    private boolean isTransmitInstruction(String instruction) {
        return instruction.startsWith("out ");
    }

    private char whatToTransmit(String instruction) {
        return instruction.split(" ")[1].charAt(0);
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

    private void resetVariables() {
        values = new HashMap<>();
    }

    private void refreshFirstValue() {
        firstValue++;
        lastPrinted = -1;
    }

}
