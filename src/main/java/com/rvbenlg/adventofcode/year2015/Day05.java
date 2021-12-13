package com.rvbenlg.adventofcode.year2015;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day05 {

    /*
    --- Day 5: Doesn't He Have Intern-Elves For This? ---
    Santa needs help figuring out which strings in his text file are naughty or nice.

    A nice string is one with all of the following properties:

    It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
    It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
    It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
    For example:

    ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...), and none of the disallowed substrings.
    aaa is nice because it has at least three vowels and a double letter, even though the letters used by different rules overlap.
    jchzalrnumimnmhp is naughty because it has no double letter.
    haegwjzuvuyypxyu is naughty because it contains the string xy.
    dvszwmarrgswjxmb is naughty because it contains only one vowel.
    How many strings are nice?

    --- Part Two ---
    Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.

    Now, a nice string is one with all of the following properties:

    It contains a pair of any two letters that appears at least twice in the string without overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
    It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe), or even aaa.
    For example:

    qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one letter between them (zxz).
    xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though the letters used by each rule overlap.
    uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
    ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
    How many strings are nice under these new rules?
     */

    private char[] vowels = {'a', 'e', 'i', 'o', 'u'};
    private String[] forbidden = {"ab", "cd", "pq", "xy"};

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2015/day05.txt");
        int result = howManyStringsAreNice(input);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2015/day05.txt");
        int result = howManyStringsAreNice2(input);
        System.out.println("Part 1 solution: " + result);
    }

    private int howManyStringsAreNice2(List<String> input) {
        int result = 0;
        for (String string : input) {
            if (isNice2(string)) {
                result++;
            }
        }
        return result;
    }

    private boolean isNice2(String string) {
        return containsAPairOfLettersThatAppearsAtLeastTwice(string) && containsAtLeastOneLetterWhichRepeatsWithOneLetterBetweemThem(string);
    }

    private boolean containsAtLeastOneLetterWhichRepeatsWithOneLetterBetweemThem(String string) {
        boolean result = false;
        for(int i = 1; i < string.length() - 1 && !result; i++) {
            if(string.charAt(i - 1) == string.charAt(i + 1)) {
                result = true;
            }
        }
        return result;
    }

    private boolean containsAPairOfLettersThatAppearsAtLeastTwice(String string) {
        boolean result = false;
        for (int i = 0; i < string.length() - 1 && !result; i++) {
            for (int j = i + 1; j < string.length() && !result; j++) {
                String pair = string.substring(i, i + 1) + string.substring(j, j + 1);
                String aux = string.replaceFirst(pair, "**");
                if(aux.contains(pair)) {
                    result = true;
                }
            }
        }
        return result;
    }

    private int howManyStringsAreNice(List<String> input) {
        int result = 0;
        for (String string : input) {
            if (isNice(string)) {
                result++;
            }
        }
        return result;
    }

    private boolean isNice(String string) {
        return containsAtLeastThreeVowels(string) && containsAtLeastOneLetterAppearsTwiceInARow(string) && !containsForbiddenStrings(string);
    }

    private boolean containsAtLeastThreeVowels(String string) {
        int howManyVowels = 0;
        for (int i = 0; i < string.length() && howManyVowels < 3; i++) {
            char c = string.charAt(i);
            boolean isVowel = false;
            for (int j = 0; j < vowels.length && !isVowel; j++) {
                if (c == vowels[j]) {
                    isVowel = true;
                    howManyVowels++;
                }
            }
        }
        return howManyVowels >= 3;
    }

    private boolean containsAtLeastOneLetterAppearsTwiceInARow(String string) {
        char currentChar = string.charAt(0);
        boolean result = false;
        for (int i = 1; i < string.length() && !result; i++) {
            char c = string.charAt(i);
            if (c == currentChar) {
                result = true;
            }
            currentChar = c;
        }
        return result;
    }

    private boolean containsForbiddenStrings(String string) {
        boolean result = false;
        for (int i = 0; i < forbidden.length && !result; i++) {
            String forbiddenString = forbidden[i];
            if (string.contains(forbiddenString)) {
                result = true;
            }
        }
        return result;
    }

}
