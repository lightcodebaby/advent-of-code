package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day20 {

    private int maxValue = Integer.MAX_VALUE;
    private List<BlackListRange> blackListRanges = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day20.txt");
        for (String line : lines) {
            createBlackListRange(line);
        }
        long result = findLowestIP();
        System.out.println("Part 1 solution: " + result);
    }

    private long findLowestIP() {
        long result = 0L;
        boolean found = false;
        while(!found) {
            long finalResult = result;
            Optional<BlackListRange> optionalBlackListRange = blackListRanges.stream().filter(blr -> blr.min <= finalResult && blr.max >= finalResult).findAny();
            if(optionalBlackListRange.isPresent()) {
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

    private class BlackListRange {
        private long min;
        private long max;

        private BlackListRange(long min, long max) {
            this.min = min;
            this.max = max;
        }
    }

}
