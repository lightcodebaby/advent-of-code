package com.rvbenlg.adventofcode.year2015;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Day04 {

    /*
    --- Day 4: The Ideal Stocking Stuffer ---
    Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

    To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

    For example:

    If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
    If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef...

    --- Part Two ---
    Now find one that starts with six zeroes.
     */

    public void solve() throws IOException, NoSuchAlgorithmException {
        part1();
        part2();
    }

    private void part1() throws IOException, NoSuchAlgorithmException {
        List<String> input = Utilities.readInput("year2015/day04.txt");
        int result = encryptUntilStartsWithFiveZeroes(input.get(0));
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException, NoSuchAlgorithmException {
        List<String> input = Utilities.readInput("year2015/day04.txt");
        int result = encryptUntilStartsWithSixZeroes(input.get(0));
        System.out.println("Part 2 solution: " + result);
    }

    private int encryptUntilStartsWithFiveZeroes(String toEncrypt) throws NoSuchAlgorithmException {
        int result = 0;
        while(!Utilities.md5(toEncrypt + result).startsWith("00000")) {
            result++;
        }
        return result;
    }

    private int encryptUntilStartsWithSixZeroes(String toEncrypt) throws NoSuchAlgorithmException {
        int result = 0;
        while(!Utilities.md5(toEncrypt + result).startsWith("000000")) {
            result++;
        }
        return result;
    }

}
