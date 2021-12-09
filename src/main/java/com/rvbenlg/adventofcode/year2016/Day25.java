package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day25 {

    /*
    --- Day 25: Clock Signal ---
    You open the door and find yourself on the roof. The city sprawls away from you for miles and miles.

    There's not much time now - it's already Christmas, but you're nowhere near the North Pole, much too far to deliver these stars to the sleigh in time.

    However, maybe the huge antenna up here can offer a solution. After all, the sleigh doesn't need the stars, exactly; it needs the timing data they provide, and you happen to have a massive signal generator right here.

    You connect the stars you have to your prototype computer, connect that to the antenna, and begin the transmission.

    Nothing happens.

    You call the service number printed on the side of the antenna and quickly explain the situation. "I'm not sure what kind of equipment you have connected over there," he says, "but you need a clock signal." You try to explain that this is a signal for a clock.

    "No, no, a clock signal - timing information so the antenna computer knows how to read the data you're sending it. An endless, alternating pattern of 0, 1, 0, 1, 0, 1, 0, 1, 0, 1...." He trails off.

    You ask if the antenna can handle a clock signal at the frequency you would need to use for the data from the stars. "There's no way it can! The only antenna we've installed capable of that is on top of a top-secret Easter Bunny installation, and you're definitely not-" You hang up the phone.

    You've extracted the antenna's clock signal generation assembunny code (your puzzle input); it looks mostly compatible with code you worked on just recently.

    This antenna code, being a signal generator, uses one extra instruction:

    out x transmits x (either an integer or the value of a register) as the next value for the clock signal.
    The code takes a value (via register a) that describes the signal to generate, but you're not sure how it's used. You'll have to find the input to produce the right signal through experimentation.

    What is the lowest positive integer that can be used to initialize register a and cause the code to output a clock signal of 0, 1, 0, 1... repeating forever?

    Your puzzle answer was 182.

    --- Part Two ---
    The antenna is ready. Now, all you need is the fifty stars required to generate the signal for the sleigh, but you don't have enough.

    You look toward the sky in desperation... suddenly noticing that a lone star has been installed at the top of the antenna! Only 49 more to go.
     */

    private HashMap<Character, Integer> values = new HashMap<>();
    private List<String> instructions = new ArrayList<>();
    private int firstValue = 0;
    private int lastPrinted = -1;

    public void solve() throws IOException, InterruptedException {
        part1();
    }

    private void part1() throws IOException, InterruptedException {
        resetVariables();
        instructions = Utilities.readInput("year2016/day25.txt");
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
        instructions = new ArrayList<>();
    }

    private void refreshFirstValue() {
        firstValue++;
        lastPrinted = -1;
    }

}
