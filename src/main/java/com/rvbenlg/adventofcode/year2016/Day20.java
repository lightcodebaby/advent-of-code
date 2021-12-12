package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day20 {

    /*
    --- Day 20: Firewall Rules ---
    You'd like to set up a small hidden computer here so you can use it to get back into the network later. However, the corporate firewall only allows communication with certain external IP addresses.

    You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly maintained, and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal notation, they are written as plain 32-bit integers, which can have any value from 0 through 4294967295, inclusive.

    For example, suppose only the values 0 through 9 were valid, and that you retrieved the following blacklist:

    5-8
    0-2
    4-7
    The blacklist specifies ranges of IPs (inclusive of both the start and end value) that are not allowed. Then, the only IPs that this firewall allows are 3 and 9, since those are the only numbers not in any range.

    Given the list of blocked IPs you retrieved from the firewall (your puzzle input), what is the lowest-valued IP that is not blocked?

    --- Part Two ---
    How many IPs are allowed by the blacklist?
     */

    private List<BlackListRange> blackListRanges = new ArrayList<>();

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day20.txt");
        for (String line : input) {
            createBlackListRange(line);
        }
        long result = findLowestIP();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day20.txt");
        for (String line : input) {
            createBlackListRange(line);
        }
        int result = howManyIpsAreAllowed();
        System.out.println("Part 2 solution: " + result);
    }

    private int howManyIpsAreAllowed() {
        int result = 0;
        for (long l = 0L; l < 4294967295L; l++) {
            long ipToCheck = l;
            Optional<BlackListRange> optionalBlackListRange = blackListRanges.stream().filter(blr -> blr.min <= ipToCheck && blr.max >= ipToCheck).findAny();
            if (optionalBlackListRange.isPresent()) {
                l = optionalBlackListRange.get().max;
            } else {
                result++;
            }
        }
        return result;
    }

    private long findLowestIP() {
        long result = 0L;
        boolean found = false;
        while (!found) {
            long finalResult = result;
            Optional<BlackListRange> optionalBlackListRange = blackListRanges.stream().filter(blr -> blr.min <= finalResult && blr.max >= finalResult).findAny();
            if (optionalBlackListRange.isPresent()) {
                result = optionalBlackListRange.get().max + 1;
            } else {
                found = true;
            }
        }
        return result;
    }

    private void createBlackListRange(String line) {
        long min = Long.parseLong(line.split("-")[0]);
        long max = Long.parseLong(line.split("-")[1]);
        blackListRanges.add(new BlackListRange(min, max));
    }

    private void resetVariables() {
        blackListRanges = new ArrayList<>();
    }

    private class BlackListRange {

        long min;
        long max;

        private BlackListRange(long min, long max) {
            this.min = min;
            this.max = max;
        }
    }

}
