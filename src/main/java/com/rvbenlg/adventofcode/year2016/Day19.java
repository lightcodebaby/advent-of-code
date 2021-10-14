package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day19 {

    private List<Boolean> hasPresents = new ArrayList<>();
    private boolean[] hasPresents2;

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day19.txt");
        int elves = Integer.parseInt(lines.get(0));
        fillPresents(elves);
        int result = 0;

        result = stealPresents2() + 1;

        System.out.println("Part 1 solution: " + result);
    }

    private int stealPresents2() {
        boolean done = false;
        boolean stealing = false;
        int index = 0;
        int howManyElvesHavePresents = 0;
        int lastElveWithPresents = 0;
        while (!done) {
            if(hasPresents2[index]) {
                if(stealing) {
                    hasPresents2[index] = false;
                    stealing = false;
                } else {
                    howManyElvesHavePresents++;
                    lastElveWithPresents = index;
                    stealing = true;
                }
            }
            index++;
            if (index == hasPresents2.length) {
                index = 0;
                done = howManyElvesHavePresents == 1;
                howManyElvesHavePresents = 0;
            }
        }
        return lastElveWithPresents;
    }

    private void stealPresents() {
        for (int i = 0; i < hasPresents.size(); i++) {
            if (hasPresents.get(i)) {
                int indexToSteal = presentsToSteal(i);
                hasPresents.set(indexToSteal, false);
            }
        }
    }

    private int presentsToSteal(int currentIndex) {
        boolean found = false;
        int result = 0;
        for (int i = 1; i < hasPresents.size() && !found; i++) {
            if (hasPresents.get((currentIndex + i) % hasPresents.size())) {
                result = (currentIndex + i) % hasPresents.size();
                found = true;
            }
        }
        return result;
    }

    private void fillPresents(int elves) {
        hasPresents2 = new boolean[elves];
        for (int i = 0; i < elves; i++) {
            hasPresents.add(true);
            hasPresents2[i] = true;
        }
    }


}
