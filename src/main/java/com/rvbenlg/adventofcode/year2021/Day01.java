package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2021/day01.txt");
        List<Integer> numbers = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        int result = howManyMeasurementsAreLarger(numbers);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2021/day01.txt");
        List<Integer> numbers = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        int result = howManyMeasurementsAreLarger2(numbers);
        System.out.println("Part 2 solution: " + result);
    }

    private int howManyMeasurementsAreLarger(List<Integer> numbers) {
        int result = 0;
        int last = numbers.get(0);
        int current = 0;
        for (int i = 1; i < numbers.size(); i++) {
            current = numbers.get(i);
            if (current > last) {
                result++;
            }
            last = current;
        }
        return result;
    }

    private int howManyMeasurementsAreLarger2(List<Integer> numbers) {
        int result = 0;
        int last = numbers.get(0) + numbers.get(1) + numbers.get(2);
        int current = 0;
        for (int i = 1; i < numbers.size() - 2; i++) {
            current = numbers.get(i) + numbers.get(i + 1) + numbers.get(i + 2);
            if (current > last) {
                result++;
            }
            last = current;
        }
        return result;
    }

}
