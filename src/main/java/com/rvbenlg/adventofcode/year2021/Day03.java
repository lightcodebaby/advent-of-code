package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

    private String mostCommon = "";
    private String leastCommon = "";

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day03.txt");
        findMostAndLeastCommon(input);
        int result = Integer.parseInt(mostCommon, 2) * Integer.parseInt(leastCommon, 2);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day03.txt");
        String oxygenGeneratorRating = getOxygenGeneratorRating(input);
        String co2ScrubberRating = getCO2ScrubberRating(input);
        int result = Integer.parseInt(oxygenGeneratorRating, 2) * Integer.parseInt(co2ScrubberRating, 2);
        System.out.println("Part 2 solution: " + result);
    }

    private String getOxygenGeneratorRating(List<String> input) {
        int numbersLength = input.get(0).length();
        List<String> matches = new ArrayList<>(input);
        for(int i = 0; i < numbersLength && matches.size() > 1; i++) {
            int position = i;
            char toMatch = mostCommon(matches, position);
            matches.removeIf(s -> s.charAt(position) != toMatch);
        }
        return matches.get(0);
    }

    private String getCO2ScrubberRating(List<String> input)  {
        int numbersLength = input.get(0).length();
        List<String> matches = new ArrayList<>(input);
        for(int i = 0; i < numbersLength && matches.size() > 1; i++) {
            int position = i;
            char toMatch = leastCommon(matches, position);
            matches.removeIf(s -> s.charAt(position) != toMatch);
        }
        return matches.get(0);
    }

    private char mostCommon(List<String> input, int position) {
        int zeros = 0;
        int ones = 0;
        char result;
        for(String number : input) {
            if(number.charAt(position) == '0') {
                zeros++;
            } else {
                ones++;
            }
        }
        if(zeros > ones) {
            result = '0';
        } else {
            result = '1';
        }
        return result;
    }

    private char leastCommon(List<String> input, int position) {
        int zeros = 0;
        int ones = 0;
        char result;
        for(String number : input) {
            if(number.charAt(position) == '0') {
                zeros++;
            } else {
                ones++;
            }
        }
        if(zeros <= ones) {
            result = '0';
        } else {
            result = '1';
        }
        return result;
    }

    private void findMostAndLeastCommon(List<String> input) {
        StringBuilder mostCommonBuilder = new StringBuilder();
        StringBuilder leastCommonBuilder = new StringBuilder();
        int numbersLength = input.get(0).length();
        for(int i = 0; i < numbersLength; i++) {
            int zeros = 0;
            int ones = 0;
            for(String number : input) {
                if(number.charAt(i) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            if(zeros > ones) {
                mostCommonBuilder.append(0);
                leastCommonBuilder.append(1);
            } else {
                mostCommonBuilder.append(1);
                leastCommonBuilder.append(0);
            }
        }
        mostCommon = mostCommonBuilder.toString();
        leastCommon = leastCommonBuilder.toString();
    }

}
