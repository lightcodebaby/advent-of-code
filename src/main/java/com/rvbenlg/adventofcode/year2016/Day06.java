package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {

    /*
    --- Day 6: Signals and Noise ---
    Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol in situations like this is to switch to a simple repetition code to get the message through.

    In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.

    All you need to do is figure out which character is most frequent for each position. For example, suppose you had recorded the following messages:

    eedadn
    drvtee
    eandsr
    raavrd
    atevrs
    tsrnev
    sdttsa
    rasrtv
    nssdts
    ntnada
    svetve
    tesnvt
    vntsnd
    vrdear
    dvrsen
    enarar
    The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining these characters returns the error-corrected message, easter.

    Given the recording in your puzzle input, what is the error-corrected version of the message being sent?

    --- Part Two ---
    Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.

    In this modified code, the sender instead transmits what looks like random data, but for each character, the character they actually want to send is slightly less likely than the others. Even after signal-jamming noise, you can look at the letter distributions in each column and choose the least common letter to reconstruct the original message.

    In the above example, the least common character in the first column is a; in the second, d, and so on. Repeating this process for the remaining characters produces the original message, advent.

    Given the recording in your puzzle input and this new decoding methodology, what is the original message that Santa is trying to send?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day06.txt");
        String result = "";
        int lineLength = lines.get(0).length();
        for(int i = 0; i < lineLength; i++) {
            List<Character> characters = getColumnChars(lines, i);
            result = result + mostCommonCharacterInColumn(characters);
        }
        System.out.println("Part 1 solution: " + result);
    }

    private char mostCommonCharacterInColumn(List<Character> characters) {
        Hashtable<Character, Integer> howManyTimes = howManyTimesCharacterInColumn(characters);
        int times = howManyTimes.values().stream().filter(value -> howManyTimes.values().stream().noneMatch(value2 -> value < value2)).findFirst().get();
        return howManyTimes.keySet().stream().filter(character -> howManyTimes.get(character) == times).findFirst().get();
    }

    private Hashtable<Character, Integer> howManyTimesCharacterInColumn(List<Character> characters) {
        Hashtable<Character, Integer> howManyTimes = new Hashtable<>();
        for(char character : characters) {
            if(howManyTimes.containsKey(character)) {
                int times = howManyTimes.get(character) + 1;
                howManyTimes.put(character, times);
            } else {
                howManyTimes.put(character, 1);
            }
        }
        return howManyTimes;
    }

    private List<Character> getColumnChars(List<String> lines, int column) {
        return lines.stream().map(line -> line.charAt(column)).collect(Collectors.toList());
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day06.txt");
        String result = "";
        int lineLength = lines.get(0).length();
        for(int i = 0; i < lineLength; i++) {
            List<Character> characters = getColumnChars(lines, i);
            result = result + leastCommonCharacterInColumn(characters);
        }
        System.out.println("Part 2 solution: " + result);
    }

    private char leastCommonCharacterInColumn(List<Character> characters) {
        Hashtable<Character, Integer> howManyTimes = howManyTimesCharacterInColumn(characters);
        int times = howManyTimes.values().stream().filter(value -> howManyTimes.values().stream().noneMatch(value2 -> value > value2)).findFirst().get();
        return howManyTimes.keySet().stream().filter(character -> howManyTimes.get(character) == times).findFirst().get();
    }

}
