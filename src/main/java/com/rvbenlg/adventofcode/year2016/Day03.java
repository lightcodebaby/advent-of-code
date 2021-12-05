package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 {

    /*
    --- Day 3: Squares With Three Sides ---
    Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.

    Or are they?

    The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.

    In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.

    In your puzzle input, how many of the listed triangles are possible?

    --- Part Two ---
    Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.

    For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:

    101 301 501
    102 302 502
    103 303 503
    201 401 601
    202 402 602
    203 403 603
    In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day03.txt");
        int count = 0;
        for (String line : input) {
            if (isTriangle(line)) {
                count++;
            }
        }
        System.out.println("Part 1 solution: " + count);
    }


    private boolean isTriangle(String strDimensions) {
        List<Integer> dimensions = Arrays.stream(strDimensions.split(" "))
                .filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
        return dimensions.get(0) + dimensions.get(1) > dimensions.get(2)
                && dimensions.get(0) + dimensions.get(2) > dimensions.get(1)
                && dimensions.get(1) + dimensions.get(2) > dimensions.get(0);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day03.txt");
        int result = 0;
        for (int i = 0; i < input.size(); i += 3) {
            List<String> firstDimension = Arrays.stream(input.get(i).split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            List<String> secondDimension = Arrays.stream(input.get(i + 1).split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            List<String> thirdDimension = Arrays.stream(input.get(i + 2).split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            result += howManyTriangles(firstDimension, secondDimension, thirdDimension);
        }
        System.out.println("Part 2 solution: " + result);
    }

    private int howManyTriangles(List<String> firstDimension, List<String> secondDimension, List<String> thirdDimension) {
        int result = 0;
        List<String> triangles = new ArrayList<>();
        triangles.add(String.format("%s %s %s", firstDimension.get(0), secondDimension.get(0), thirdDimension.get(0)));
        triangles.add(String.format("%s %s %s", firstDimension.get(1), secondDimension.get(1), thirdDimension.get(1)));
        triangles.add(String.format("%s %s %s", firstDimension.get(2), secondDimension.get(2), thirdDimension.get(2)));
        for (String triangle : triangles) {
            if (isTriangle(triangle)) {
                result++;
            }
        }
        return result;
    }

}
