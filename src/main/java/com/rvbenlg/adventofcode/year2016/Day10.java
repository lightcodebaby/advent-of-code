package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day10 {

    /*
    --- Day 10: Balance Bots ---
    You come upon a factory in which many robots are zooming around handing small microchips to each other.

    Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.

    Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some logic to decide what to do with each chip. You access the local control computer and download the bots' instructions (your puzzle input).

    Some of the instructions specify that a specific-valued microchip should be given to a specific bot; the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.

    For example, consider the following instructions:

    value 5 goes to bot 2
    bot 2 gives low to bot 1 and high to bot 0
    value 3 goes to bot 1
    bot 1 gives low to output 1 and high to bot 0
    bot 0 gives low to output 2 and high to output 0
    value 2 goes to bot 2
    Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
    Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
    Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
    Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
    In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing value-5 microchips with value-2 microchips.

    Based on your instructions, what is the number of the bot that is responsible for comparing value-61 microchips with value-17 microchips?

    --- Part Two ---
    What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?
     */

    private HashMap<Integer, List<Integer>> bots = new HashMap<>();
    private HashMap<Integer, Integer> outputs = new HashMap<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    public void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day10.txt");
        List<String> valueInstructions = input.stream().filter(this::isValueInstruction).collect(Collectors.toList());
        List<String> botInstructions = input.stream().filter(this::isBotInstruction).collect(Collectors.toList());
        int result = 0;
        for (String instruction : valueInstructions) {
            followInstruction(instruction);
        }
        int count = 0;
        while (!botInstructions.isEmpty()) {
            if (count >= botInstructions.size()) {
                count = 0;
            }
            if (followInstruction(botInstructions.get(count))) {
                botInstructions.remove(count);
                Optional<Integer> optionalBot = bots.keySet().stream().filter(key -> bots.get(key).contains(61) && bots.get(key).contains(17)).findFirst();
                if (optionalBot.isPresent()) {
                    result = optionalBot.get();
                }
            } else {
                count++;
            }
        }
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() {
        int result = outputs.get(0) * outputs.get(1) * outputs.get(2);
        System.out.println("Part 2 solution: " + result);
    }

    private boolean followInstruction(String line) {
        boolean result = false;
        if (isValueInstruction(line)) {
            result = followValueInstruction(line);
        } else if (isBotInstruction(line)) {
            result = followBotInstruction(line);
        }
        return result;
    }

    private boolean followValueInstruction(String instruction) {
        int value = getValueFromInstruction(instruction);
        int bot = getToFromInstruction(instruction);
        addValueToBot(bot, value);
        return true;
    }

    private boolean followBotInstruction(String instruction) {
        boolean result = false;
        int bot = getBotFromInstruction(instruction);
        if (bots.containsKey(bot) && bots.get(bot).size() == 2) {
            int low = getLowFromInstruction(instruction);
            int high = getHighFromInstruction(instruction);
            if (givesToBot(instruction, low)) {
                giveLowToBot(bot, low);
            } else if (givesToOutput(instruction, low)) {
                giveLowToOutput(bot, low);
            }
            if (givesToBot(instruction, high)) {
                giveHighToBot(bot, high);
            } else if (givesToOutput(instruction, high)) {
                giveHighToOutput(bot, high);
            }
            result = true;
        }
        return result;
    }

    private void giveHighToOutput(int bot, int high) {
        int value = bots.get(bot).stream().filter(integer -> bots.get(bot).stream().noneMatch(integer1 -> integer1 > integer)).findFirst().get();
        outputs.put(high, value);
    }

    private void giveLowToOutput(int bot, int low) {
        int value = bots.get(bot).stream().filter(integer -> bots.get(bot).stream().noneMatch(integer1 -> integer1 < integer)).findFirst().get();
        outputs.put(low, value);
    }

    private void giveHighToBot(int bot, int high) {
        int value = bots.get(bot).stream().filter(integer -> bots.get(bot).stream().noneMatch(integer1 -> integer1 > integer)).findFirst().get();
        if (bots.containsKey(high)) {
            if (bots.get(high).size() < 2) {
                bots.get(high).add(value);
                bots.get(bot).removeIf(integer -> integer == value);
            }
        } else {
            bots.put(high, new ArrayList<>());
            bots.get(high).add(value);
            bots.get(bot).removeIf(integer -> integer == value);
        }

    }

    private void giveLowToBot(int bot, int low) {
        int value = bots.get(bot).stream().filter(integer -> bots.get(bot).stream().noneMatch(integer1 -> integer1 < integer)).findFirst().get();
        if (bots.containsKey(low)) {
            if (bots.get(low).size() < 2) {
                bots.get(low).add(value);
                bots.get(bot).removeIf(integer -> integer == value);
            }
        } else {
            bots.put(low, new ArrayList<>());
            bots.get(low).add(value);
            bots.get(bot).removeIf(integer -> integer == value);
        }
    }

    private void addValueToBot(int bot, int value) {
        if (bots.containsKey(bot)) {
            bots.get(bot).add(value);
        } else {
            bots.put(bot, new ArrayList<>());
            bots.get(bot).add(value);
        }
    }

    private boolean isValueInstruction(String instruction) {
        return instruction.startsWith("value ");
    }

    private boolean isBotInstruction(String instruction) {
        return instruction.startsWith("bot ");
    }

    private int getValueFromInstruction(String instruction) {
        return Integer.parseInt(instruction.split(" ")[1]);
    }

    private int getToFromInstruction(String instruction) {
        return Integer.parseInt(instruction.split(" ")[5]);
    }

    private int getBotFromInstruction(String instruction) {
        return Integer.parseInt(instruction.split(" ")[1]);
    }

    private int getLowFromInstruction(String instruction) {
        return Integer.parseInt(instruction.split(" ")[6]);
    }

    private int getHighFromInstruction(String instruction) {
        return Integer.parseInt(instruction.split(" ")[11]);
    }

    private boolean givesToOutput(String instruction, int to) {
        return instruction.contains("to output " + to);
    }

    private boolean givesToBot(String instruction, int to) {
        return instruction.contains("to bot " + to);
    }

}
