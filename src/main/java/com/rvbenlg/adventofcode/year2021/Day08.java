package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08 {

    private int[][] inputs = new int[0][];
    private int[][] outputs = new int[0][];

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day08.txt");
        parseData(input);
        int result = howManyTimeDoDigits1478Appear();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day08.txt");
        int result = decode(input);
        System.out.println("Part 1 solution: " + result);
    }

    private int decode(List<String> input) {
        int result = 0;
        for (int i = 0; i < input.size(); i++) {
            int auxResult = 0;
            String data = input.get(i);
            Eight eight = new Eight(data);
            String[] outputs = data.split("\\|")[1].trim().split(" ");
            for(int j = 0; j < outputs.length; j++) {
                String output = outputs[j];
                auxResult += (number(output, eight) * Math.pow(10, outputs.length - j - 1));
            }
            result += auxResult;
        }
        return result;
    }

    private int number(String output, Eight eight) {
        int result = -1;
        if (output.length() == 7) {
            result = 8;
        } else if (output.length() == 4) {
            result = 4;
        } else if (output.length() == 3) {
            result = 7;
        } else if (output.length() == 2) {
            result = 1;
        } else if (output.length() == 6) {
            if (!output.contains(String.valueOf(eight.middle))) {
                result = 0;
            } else if (!output.contains(String.valueOf(eight.topRight))) {
                result = 6;
            } else {
                result = 9;
            }
        } else if (output.length() == 5) {
            if (!output.contains(String.valueOf(eight.bottomRight))) {
                result = 2;
            } else if (!output.contains(String.valueOf(eight.topRight))) {
                result = 5;
            } else {
                result = 3;
            }
        }
        return result;
    }

    private int howManyTimeDoDigits1478Appear() {
        int result = 0;
        for (int[] output : outputs) {
            for (int value : output) {
                if (value == 2 || value == 3 || value == 4 || value == 7) {
                    result++;
                }
            }
        }
        return result;
    }

    private void parseData(List<String> input) {
        inputs = new int[input.size()][];
        outputs = new int[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            String data = input.get(i);
            String[] parts = data.split("\\|");
            String[] strInputs = parts[0].trim().split(" ");
            String[] strOutputs = parts[1].trim().split(" ");
            inputs[i] = new int[strInputs.length];
            outputs[i] = new int[strOutputs.length];
            for (int j = 0; j < strInputs.length; j++) {
                inputs[i][j] = strInputs[j].length();
            }
            for (int j = 0; j < strOutputs.length; j++) {
                outputs[i][j] = strOutputs[j].length();
            }
        }
    }

    private class Eight {

        String data;
        String[] inputs;
        private String one;
        private String four;
        private String seven;
        private String eight;
        List<String> zeroSixNine;
        private char top;
        private char topLeft;
        private char topRight;
        private char middle;
        private char bottomLeft;
        private char bottomRight;
        private char bottom;

        private Eight(String data) {
            this.data = data;
            inputs = data.split("\\|")[0].trim().split(" ");
            one = Stream.of(inputs).filter(s -> s.length() == 2).findFirst().get();
            four = Stream.of(inputs).filter(s -> s.length() == 4).findFirst().get();
            seven = Stream.of(inputs).filter(s -> s.length() == 3).findFirst().get();
            eight = Stream.of(inputs).filter(s -> s.length() == 7).findFirst().get();
            zeroSixNine = Stream.of(inputs).filter(s -> s.length() == 6).collect(Collectors.toList());
            findTop();
            findTopRight();
            findBottomRight();
            findMiddle();
            findTopLeft();
            findBottom();
            findBottomLeft();
        }

        private void findTop() {
            char result = ' ';
            for (char c: seven.toCharArray()) {
                if (!one.contains(String.valueOf(c))) {
                    result = c;
                }
            }
            top = result;
        }

        private void findTopRight() {
            char result = ' ';
            for (String number : zeroSixNine) {
                for (char c : one.toCharArray()) {
                    if (!number.contains(String.valueOf(c))) {
                        result = c;
                    }
                }
            }
            topRight = result;
        }

        private void findBottomRight() {
            char result = ' ';
            for (char c : one.toCharArray()) {
                if (c != topRight) {
                    result = c;
                }
            }
            bottomRight = result;
        }
        private void findMiddle() {
            char result = ' ';
            for (char c : four.toCharArray()) {
                for (String number : zeroSixNine) {
                    if (!one.contains(String.valueOf(c)) && !number.contains(String.valueOf(c))) {
                        result = c;
                    }
                }
            }
            middle = result;
        }

        private void findTopLeft() {
            char result = ' ';
            for (char c : four.toCharArray()) {
                if (c != topRight && c != bottomRight && c != middle) {
                    result = c;
                }
            }
            topLeft = result;
        }

        private void findBottom() {
            char result = ' ';
            String nine = Stream.of(inputs).filter(s -> s.length() == 6).filter(s -> s.contains(String.valueOf(middle))).filter(s -> s.contains(String.valueOf(topRight))).findFirst().get();
            for (char c : nine.toCharArray()) {
                if (c != top && c != topLeft && c != topRight && c != middle && c != bottomRight) {
                    result = c;
                }
            }
            bottom = result;
        }

        private void findBottomLeft() {
            char result = ' ';
            for (char c : eight.toCharArray()) {
                if (c != top && c != topLeft && c != topRight && c != middle && c != bottomRight && c != bottom) {
                    result = c;
                }
            }
            bottomLeft = result;
        }

    }

}
