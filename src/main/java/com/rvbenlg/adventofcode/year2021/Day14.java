package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 {

    /*
    --- Day 14: Extended Polymerization ---
    The incredible pressures at this depth are starting to put a strain on your submarine. The submarine has polymerization equipment that would produce suitable materials to reinforce the submarine, and the nearby volcanically-active caves should even have the necessary input elements in sufficient quantities.

    The submarine manual contains instructions for finding the optimal polymer formula; specifically, it offers a polymer template and a list of pair insertion rules (your puzzle input). You just need to work out what polymer would result after repeating the pair insertion process a few times.

    For example:

    NNCB

    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
    The first line is the polymer template - this is the starting point of the process.

    The following section defines the pair insertion rules. A rule like AB -> C means that when elements A and B are immediately adjacent, element C should be inserted between them. These insertions all happen simultaneously.

    So, starting with the polymer template NNCB, the first step simultaneously considers all three pairs:

    The first pair (NN) matches the rule NN -> C, so element C is inserted between the first N and the second N.
    The second pair (NC) matches the rule NC -> B, so element B is inserted between the N and the C.
    The third pair (CB) matches the rule CB -> H, so element H is inserted between the C and the B.
    Note that these pairs overlap: the second element of one pair is the first element of the next pair. Also, because all pairs are considered simultaneously, inserted elements are not considered to be part of a pair until the next step.

    After the first step of this process, the polymer becomes NCNBCHB.

    Here are the results of a few steps using the above rules:

    Template:     NNCB
    After step 1: NCNBCHB
    After step 2: NBCCNBBBCBHCB
    After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
    After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
    This polymer grows quickly. After step 5, it has length 97; After step 10, it has length 3073. After step 10, B occurs 1749 times, C occurs 298 times, H occurs 161 times, and N occurs 865 times; taking the quantity of the most common element (B, 1749) and subtracting the quantity of the least common element (H, 161) produces 1749 - 161 = 1588.

    Apply 10 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?

     --- Part Two ---
    The resulting polymer isn't nearly strong enough to reinforce the submarine. You'll need to run more steps of the pair insertion process; a total of 40 steps should do it.

    In the above example, the most common element is B (occurring 2192039569602 times) and the least common element is H (occurring 3849876073 times); subtracting these produces 2188189693529.

    Apply 40 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?
     */

    private Rule[] rules = new Rule[0];
    private Map<Character, Long> elementsAndTimes = new HashMap<>();
    private Map<Pair, Long> pairsAndTimes = new HashMap<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day14.txt");
        fillRules(input.subList(2, input.size()));
        fillPairsAndTimes(input.get(0));
        applySteps(10);
        fillElementsAndTimes();
        long mostCommonTimes = howManyTimeMostCommonOccurs();
        long leastCommonTimes = howManyTimeLeastCommonOccurs();
        long result = ((mostCommonTimes - leastCommonTimes) / 2) + 1;
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day14.txt");
        fillRules(input.subList(2, input.size()));
        fillPairsAndTimes(input.get(0));
        applySteps(40);
        fillElementsAndTimes();
        long mostCommonTimes = howManyTimeMostCommonOccurs();
        long leastCommonTimes = howManyTimeLeastCommonOccurs();
        long result = ((mostCommonTimes - leastCommonTimes) / 2) + 1;
        System.out.println("Part 2 solution: " + result);
    }

    private long howManyTimeMostCommonOccurs() {
        return elementsAndTimes.values().stream().max(Long::compare).get();
    }

    private long howManyTimeLeastCommonOccurs() {
        return elementsAndTimes.values().stream().min(Long::compare).get();
    }

    private void applySteps(int steps) {
        for (int i = 0; i < steps; i++) {
            processPolymerTemplate();
        }
    }

    private void processPolymerTemplate() {
        Set<Pair> pairs = new HashSet<>(pairsAndTimes.keySet().stream().filter(pair -> pairsAndTimes.get(pair) != 0).collect(Collectors.toSet()));
        HashMap<Pair, Long> toInsert = new HashMap<>();
        for (Pair pair : pairs) {
            char firstElement = pair.firstElement;
            char secondElement = pair.secondElement;
            char elementToInsert = whatToInsert(firstElement, secondElement);
            if (elementToInsert != '#') {
                toInsert.put(new Pair(firstElement, elementToInsert), pairsAndTimes.get(pair));
                toInsert.put(new Pair(elementToInsert, secondElement), pairsAndTimes.get(pair));
                pairsAndTimes.put(pair, 0L);
            }

        }
        updatePairsAndtimes(toInsert);
    }

    private void updatePairsAndtimes(HashMap<Pair, Long> generatedPairs) {
        for (Pair pair : generatedPairs.keySet()) {
            Optional<Pair> optionalPair = pairsAndTimes.keySet().stream().filter(pair1 -> pair1.firstElement == pair.firstElement && pair1.secondElement == pair.secondElement).findFirst();
            if (optionalPair.isPresent()) {
                pairsAndTimes.put(optionalPair.get(), pairsAndTimes.get(optionalPair.get()) + generatedPairs.get(pair));
            } else {
                pairsAndTimes.put(pair, generatedPairs.get(pair));
            }
        }
    }

    private char whatToInsert(char firstElement, char secondElement) {
        boolean done = false;
        char result = '#';
        for (int i = 0; i < rules.length && !done; i++) {
            Rule rule = rules[i];
            if (rule.firstElement == firstElement && rule.secondElement == secondElement) {
                result = rule.elementToInsert;
            }
        }
        return result;
    }

    private void fillRules(List<String> input) {
        rules = new Rule[input.size()];
        for (int i = 0; i < input.size(); i++) {
            String ruleDescription = input.get(i);
            String[] parts = ruleDescription.split(" -> ");
            rules[i] = new Rule(parts[0].charAt(0), parts[0].charAt(1), parts[1].charAt(0));
        }
    }

    private void fillElementsAndTimes() {
        for (Pair pair : pairsAndTimes.keySet()) {
            char firstElement = pair.firstElement;
            char secondElement = pair.secondElement;
            if (elementsAndTimes.containsKey(firstElement)) {
                elementsAndTimes.put(firstElement, elementsAndTimes.get(firstElement) + pairsAndTimes.get(pair));
            } else {
                elementsAndTimes.put(firstElement, pairsAndTimes.get(pair));
            }
            if (elementsAndTimes.containsKey(secondElement)) {
                elementsAndTimes.put(secondElement, elementsAndTimes.get(secondElement) + pairsAndTimes.get(pair));
            } else {
                elementsAndTimes.put(secondElement, pairsAndTimes.get(pair));
            }
        }
    }

    private void fillPairsAndTimes(String polymer) {
        for (int i = 0; i < polymer.length() - 1; i++) {
            char firstElement = polymer.charAt(i);
            char secondElement = polymer.charAt(i + 1);
            Optional<Pair> optionalPair = pairsAndTimes.keySet().stream().filter(pair -> pair.firstElement == firstElement && pair.secondElement == secondElement).findFirst();
            if (optionalPair.isPresent()) {
                pairsAndTimes.put(optionalPair.get(), pairsAndTimes.get(optionalPair.get()) + 1);
            } else {
                pairsAndTimes.put(new Pair(firstElement, secondElement), 1L);
            }
        }
    }

    private void resetVariables() {
        rules = new Rule[0];
        elementsAndTimes = new HashMap<>();
        pairsAndTimes = new HashMap<>();
    }

    private class Pair {

        char firstElement;
        char secondElement;

        private Pair(char firstElement, char secondElement) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
        }

    }

    private class Rule {

        char firstElement;
        char secondElement;
        char elementToInsert;

        private Rule(char firstElement, char secondElement, char elementToInsert) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
            this.elementToInsert = elementToInsert;
        }

    }

}
