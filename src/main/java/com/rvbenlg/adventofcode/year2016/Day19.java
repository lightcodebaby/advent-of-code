package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day19 {

    private List<Integer> elves = new ArrayList<>();
    private boolean[] hasPresents;
    private int totalElves;

    public void solve() throws IOException {
//        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day19.txt");
        totalElves = Integer.parseInt(lines.get(0));
        resetVariables();
        fillPresents(totalElves);
        int result = stealPresents() + 1;
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day19.txt");
        totalElves = Integer.parseInt(lines.get(0));
        resetVariables();
        fillPresents(totalElves);
        int result = happyIdea2();
        System.out.println("Part 1 solution: " + result);
    }

    private int happyIdea2() {
        boolean found = false;
        int base = 3;
        while(!found) {
            int auxBase = base * 3;
            if(auxBase <= elves.size()) {
                base = auxBase;
            } else {
                found = true;
            }
        }
        int result = elves.size() - base;
        return result;
    }

    private int happyIdea() {
        int result = 1;
        int lastMatch = 9;
        for (int i = 10; i < elves.size(); i++) {
            if (result == i) {
                lastMatch = i;
                result = 1;
            } else {
                if (result < lastMatch) {
                    result++;
                } else {
                    result += 2;
                }
            }
        }
        return result;
    }

    private int stealPresents() {
        boolean done = false;
        boolean stealing = false;
        int index = 0;
        int howManyElvesHavePresents = 0;
        int lastElveWithPresents = 0;
        while (!done) {
            if (hasPresents[index]) {
                if (stealing) {
                    hasPresents[index] = false;
                    stealing = false;
                } else {
                    howManyElvesHavePresents++;
                    lastElveWithPresents = index;
                    stealing = true;
                }
            }
            index++;
            if (index == hasPresents.length) {
                index = 0;
                done = howManyElvesHavePresents == 1;
                howManyElvesHavePresents = 0;
            }
        }
        return lastElveWithPresents;
    }

    private int stealPresentsInCircle() {
        LinkedList<Integer> firstHalf = new LinkedList<>();
        LinkedList<Integer> secondHalf = new LinkedList<>();
        for (int i = 1; i <= elves.size() / 2; i++) {
            firstHalf.add(i);
        }
        for (int i = (elves.size() / 2) + 1; i <= elves.size(); i++) {
            secondHalf.add(i);
        }
        while (firstHalf.size() != 1) {
            int aux = firstHalf.removeFirst();
            if (firstHalf.size() >= secondHalf.size()) {
                firstHalf.removeLast();
            } else {
                secondHalf.removeFirst();
            }
            secondHalf.addLast(aux);
            firstHalf.addLast(secondHalf.removeFirst());
        }
        return firstHalf.get(0);
    }

    private void fillPresents(int elves) {
        hasPresents = new boolean[elves];
        for (int i = 0; i < elves; i++) {
            this.elves.add(i + 1);
            hasPresents[i] = true;
        }
    }

    private void resetVariables() {
        hasPresents = new boolean[totalElves];
        elves = new ArrayList<>();
    }


}
