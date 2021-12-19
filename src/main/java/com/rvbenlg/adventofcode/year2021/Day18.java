package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    private List<Pair> pairs = new ArrayList<>();
    private int position = 0;

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day18.txt");
        parsePairs(input);
        System.out.println();
    }

    private void parsePairs(List<String> input) {
        for (String pairDescription : input) {
            pairs.add(parsePair(pairDescription));
            position = 0;
        }
        for(Pair pair : pairs) {
            boolean explode = shouldExplode(pair, 0);
            System.out.println();
        }
    }

    private Pair parsePair(String pairDescription) {
        Pair firstPair;
        Pair secondPair;
        if (pairDescription.substring(position).startsWith("[[")) {
            position++;
            firstPair = parsePair(pairDescription);
        } else {
            position++;
            firstPair = new Pair(Integer.parseInt(String.valueOf(pairDescription.charAt(position))), true, false);
            position++;
        }
        if (pairDescription.substring(position).startsWith(",[")) {
            position++;
            secondPair = parsePair(pairDescription);
        } else {
            position++;
            secondPair = new Pair(Integer.parseInt(String.valueOf(pairDescription.charAt(position))), false, true);
            position++;
        }
        position++;
        return new Pair(firstPair, secondPair);
    }

    private Pair shouldExplode(Pair pair, int subPair) {
        Pair result = null;
        if (subPair >= 4) {
            result = pair;
        }
        if (pair.firstPair != null && !pair.firstPair.isRegular) {
            int auxSubPair = subPair + 1;
            result = shouldExplode(pair.firstPair, auxSubPair);
        }
        if (pair.secondPair != null && !pair.secondPair.isRegular) {
            int auxSubPair = subPair + 1;
            result = shouldExplode(pair.secondPair, auxSubPair);
        }
        return result;
    }


    private Pair addTwoPairs(Pair firstPair, Pair secondPair) {
        return new Pair(firstPair, secondPair);
    }

    private class Pair {
        boolean isRegular;
        Pair firstPair;
        Pair secondPair;
        boolean isLeft;
        boolean isRight;
        int value;

        private Pair(Pair firstPair, Pair secondPair) {
            this.isRegular = false;
            this.firstPair = firstPair;
            this.secondPair = secondPair;
            this.value = Integer.MAX_VALUE;
        }

        private Pair(int value, boolean isLeft, boolean isRight) {
            this.isRegular = true;
            this.value = value;
            this.isLeft = isLeft;
            this.isRight = isRight;
        }
    }

}
