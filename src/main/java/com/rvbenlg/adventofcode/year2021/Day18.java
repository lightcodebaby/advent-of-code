package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {


    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day18.txt");
//        String sumResult = calculateFinalSum(input);
        String sumResult = input.get(0);
        int magnitude = calculateMagnitude(sumResult);
        System.out.println();
    }

    private int calculateMagnitude(String sumResult) {
        String regex = "\\[[0-9]+,[0-9]+\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sumResult);
        while(matcher.find()) {
            String toReduce = sumResult.substring(matcher.start(), matcher.end());
            String[] numbers = toReduce.split(",");
            int firstNumber = Integer.parseInt(numbers[0].substring(1));
            int secondNumber = Integer.parseInt(numbers[1].substring(0, numbers[1].length() - 1));
            int result = (3*firstNumber) + (2*secondNumber);
            sumResult = sumResult.substring(0, matcher.start()) + result + sumResult.substring(matcher.end());
            matcher = pattern.matcher(sumResult);
        }
        return 0;
    }

    private String calculateFinalSum(List<String> input) {
        String currentPair = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            String sum = addPairs(currentPair, input.get(i));
            currentPair = reduce(sum);
        }
        return currentPair;
    }

    private String reduce(String pairDescription) {
        String oldPairDescription;
        String reduced = pairDescription;
        do {
            oldPairDescription = reduced;
            reduced = reduceExploding(reduced);
            reduced = reduceSplitting(reduced);
        } while (!reduced.equals(oldPairDescription));
        return reduced;
    }

    private String reduceSplitting(String pairDescription) {
        int position = 0;
        int digits = 0;
        boolean reduced = false;
        do {
            char c = pairDescription.charAt(position);
            if (c != '[' && c != ',' && c != ']') {
                digits++;
            } else {
                digits = 0;
            }
            if (digits >= 2) {
                pairDescription = split(pairDescription, position);
                reduced = true;
            }
            position++;
        } while (!reduced && position < pairDescription.length());
        return pairDescription;
    }

    private String split(String pairDescription, int position) {
        String left = pairDescription.substring(0, position - 1);
        String toReplace = pairDescription.substring(position - 1, position + 1);
        String right = pairDescription.substring(position + 1);
        int value = Integer.parseInt(toReplace);
        int firstValue = value / 2;
        int secondValue = value % 2 == 0 ? value / 2 : (value / 2) + 1;
        return left + createPair(firstValue, secondValue) + right;
    }

    private String reduceExploding(String pairDescription) {
        int position = 0;
        int openPairs = 0;
        boolean reduced = false;
        do {
            char c = pairDescription.charAt(position);
            if (c == '[') {
                openPairs++;
            } else if (c == ']') {
                openPairs--;
            }
            if (openPairs > 4) {
                pairDescription = explode(pairDescription, position);
                reduced = true;
            }
            position++;
        } while (!reduced && position < pairDescription.length());
        return pairDescription;
    }

    private String explode(String pairDescription, int position) {
        int ends = pairDescription.indexOf("]", position);
        int[] pair = getPairToExplode(pairDescription, position);
        String left = modifyLeftPart(pairDescription.substring(0, position), pair[0]);
        String right = modifyRightPart(pairDescription.substring(ends + 1), pair[1]);
        return left + "0" + right;
    }

    private String modifyLeftPart(String pairDescription, int value) {
        StringBuilder result = new StringBuilder();
        boolean alreadyModified = false;
        for (int i = pairDescription.length() - 1; i >= 0; i--) {
            char c = pairDescription.charAt(i);
            if (alreadyModified || c == '[' || c == ']' || c == ',') {
                result.append(c);
            } else {
                int oldValue = Integer.parseInt(String.valueOf(c));
                int newValue = oldValue + value;
                result.append(new StringBuilder(String.valueOf(newValue)).reverse());
                alreadyModified = true;
            }
        }
        return result.reverse().toString();
    }

    private String modifyRightPart(String pairDescription, int value) {
        StringBuilder result = new StringBuilder();
        boolean alreadyModified = false;
        for (int i = 0; i < pairDescription.length(); i++) {
            char c = pairDescription.charAt(i);
            if (alreadyModified || c == '[' || c == ']' || c == ',') {
                result.append(c);
            } else {
                int oldValue = Integer.parseInt(String.valueOf(c));
                int newValue = oldValue + value;
                result.append(newValue);
                alreadyModified = true;
            }
        }
        return result.toString();
    }

    private int[] getPairToExplode(String pairDescription, int position) {
        int[] result = new int[2];
        boolean found = false;
        for (int i = position; !found; i++) {
            char c = pairDescription.charAt(i);
            if (c != '[' && c != ',' && c != ']') {
                result[0] = Integer.parseInt(pairDescription.substring(i, pairDescription.indexOf(",", i)));
                result[1] = Integer.parseInt(pairDescription.substring(pairDescription.indexOf(",", i) + 1, pairDescription.indexOf("]", i)));
                found = true;
            }
        }
        return result;
    }

    private String addPairs(String firstPair, String secondPair) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append(firstPair);
        result.append(",");
        result.append(secondPair);
        result.append("]");
        return result.toString();
    }

    private String createPair(int firstValue, int secondValue) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append(firstValue);
        result.append(",");
        result.append(secondValue);
        result.append("]");
        return result.toString();
    }


}
