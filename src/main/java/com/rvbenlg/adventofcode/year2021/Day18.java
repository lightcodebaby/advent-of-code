package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    private List<Pair> pairs = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day18.txt");
        parsePairs(input);
        System.out.println();
    }

    private void parsePairs(List<String> input) {
        for(String pairDescription : input) {
            pairs.add(parsePair(pairDescription));
        }
    }

    private Pair parsePair(String pairDescription) {
        if(pairDescription.startsWith("[[") || pairDescription.startsWith(",[")) {
            String firstP
        }
        if(pairDescription.contains(",")) {
            String[] parts = pairDescription.split(",");
            String firstPairDescription = parts[0].substring(1);
            String secondPairDescription = parts[1].substring(0, parts[1].length() - 1);
            return new Pair(parsePair(firstPairDescription), parsePair(secondPairDescription));
        } else {
            return new Pair(Integer.parseInt(pairDescription));
        }

    }

    private class Pair {
        boolean isRegular;
        Pair firstPair;
        Pair secondPair;
        int value;

        private Pair (Pair firstPair, Pair secondPair) {
            this.isRegular = false;
            this.firstPair = firstPair;
            this.secondPair = secondPair;
            this.value = Integer.MAX_VALUE;
        }

        private Pair(int value) {
            this.isRegular = true;
            this.value = value;
        }
    }

}
