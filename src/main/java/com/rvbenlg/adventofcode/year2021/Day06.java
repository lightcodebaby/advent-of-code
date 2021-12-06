package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day06 {

    /*
    --- Day 6: Lanternfish ---
    The sea floor is getting steeper. Maybe the sleigh keys got carried this way?

    A massive school of glowing lanternfish swims past. They must spawn quickly to reach such large numbers - maybe exponentially quickly? You should model their growth rate to be sure.

    Although you know nothing about this specific species of lanternfish, you make some guesses about their attributes. Surely, each lanternfish creates a new lanternfish once every 7 days.

    However, this process isn't necessarily synchronized between every lanternfish - one lanternfish might have 2 days left until it creates another lanternfish, while another might have 4. So, you can model each fish as a single number that represents the number of days until it creates a new lanternfish.

    Furthermore, you reason, a new lanternfish would surely need slightly longer before it's capable of producing more lanternfish: two more days for its first cycle.

    So, suppose you have a lanternfish with an internal timer value of 3:

    After one day, its internal timer would become 2.
    After another day, its internal timer would become 1.
    After another day, its internal timer would become 0.
    After another day, its internal timer would reset to 6, and it would create a new lanternfish with an internal timer of 8.
    After another day, the first lanternfish would have an internal timer of 5, and the second lanternfish would have an internal timer of 7.
    A lanternfish that creates a new fish resets its timer to 6, not 7 (because 0 is included as a valid timer value). The new lanternfish starts with an internal timer of 8 and does not start counting down until the next day.

    Realizing what you're trying to do, the submarine automatically produces a list of the ages of several hundred nearby lanternfish (your puzzle input). For example, suppose you were given the following list:

    3,4,3,1,2
    This list means that the first fish has an internal timer of 3, the second fish has an internal timer of 4, and so on until the fifth fish, which has an internal timer of 2. Simulating these fish over several days would proceed as follows:

    Initial state: 3,4,3,1,2
    After  1 day:  2,3,2,0,1
    After  2 days: 1,2,1,6,0,8
    After  3 days: 0,1,0,5,6,7,8
    After  4 days: 6,0,6,4,5,6,7,8,8
    After  5 days: 5,6,5,3,4,5,6,7,7,8
    After  6 days: 4,5,4,2,3,4,5,6,6,7
    After  7 days: 3,4,3,1,2,3,4,5,5,6
    After  8 days: 2,3,2,0,1,2,3,4,4,5
    After  9 days: 1,2,1,6,0,1,2,3,3,4,8
    After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
    After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
    After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
    After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
    After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
    After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
    After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
    After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
    After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
    Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other number decreases by 1 if it was present at the start of the day.

    In this example, after 18 days, there are a total of 26 fish. After 80 days, there would be a total of 5934.

    Find a way to simulate lanternfish. How many lanternfish would there be after 80 days?

    --- Part Two ---
    Suppose the lanternfish live forever and have unlimited food and space. Would they take over the entire ocean?

    After 256 days in the example above, there would be a total of 26984457539 lanternfish!

    How many lanternfish would there be after 256 days?
     */

    private int days1 = 80;
    private int days2 = 256;
    private HashMap<Integer, Long> timersMap = new HashMap<>();
    private List<Integer> timersList = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day06.txt");
        fillTimersList(input.get(0));
        int result = howManyFishesP1();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day06.txt");
        fillTimersMap(input.get(0));
        long result = howManyFishesP2();
        System.out.println("Part 2 solution: " + result);
    }

    private long howManyFishesP2() {
        long result = 0L;
        for (int i = 1; i <= days2; i++) {
            for (int j = 0; j <= 8; j++) {
                if (timersMap.containsKey(j)) {
                    timersMap.put(j - 1, timersMap.get(j));
                    timersMap.remove(j);
                }
            }
            if (timersMap.containsKey(-1)) {
                timersMap.put(8, timersMap.get(-1));
                timersMap.put(6, timersMap.containsKey(6) ? timersMap.get(6) + timersMap.get(-1) : timersMap.get(-1));
                timersMap.remove(-1);
            }
        }
        for(int key : timersMap.keySet()) {
            result += timersMap.get(key);
        }
        return result;
    }

    private int howManyFishesP1() {
        List<Integer> resultList = new ArrayList<>(timersList);
        for (int i = 1; i <= days1; i++) {
            List<Integer> auxTimers = new ArrayList<>(resultList);
            for (int j = 0; j < auxTimers.size(); j++) {
                if (auxTimers.get(j) == 0) {
                    resultList.add(8);
                    resultList.set(j, 6);
                } else {
                    resultList.set(j, resultList.get(j) - 1);
                }
            }
        }
        return resultList.size();
    }

    private void fillTimersMap(String timersDescription) {
        String[] parts = timersDescription.split(",");
        for (String part : parts) {
            int value = Integer.parseInt(part);
            if (timersMap.containsKey(value)) {
                timersMap.put(value, timersMap.get(value) + 1);
            } else {
                timersMap.put(value, 1L);
            }
        }
    }

    private void fillTimersList(String timersDescription) {
        String[] parts = timersDescription.split(",");
        for (String part : parts) {
            timersList.add(Integer.parseInt(part));
        }
    }

    private void resetVariables() {
        timersList = new ArrayList<>();
    }
}
