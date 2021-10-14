package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 {

    /*
    --- Day 9: Explosives in Cyberspace ---
    Wandering around a secure area, you come across a datalink port to a new part of the network. After briefly scanning it for interesting files, you find one file in particular that catches your attention. It's compressed with an experimental format, but fortunately, the documentation for the format is nearby.

    The format compresses a sequence of characters. Whitespace is ignored. To indicate that some sequence should be repeated, a marker is added to the file, like (10x2). To decompress this marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading the file after the repeated data. The marker itself is not included in the decompressed output.

    If parentheses or other characters appear within the data referenced by a marker, that's okay - treat it like normal data, not a marker, and then resume looking for markers after the decompressed section.

    For example:

    ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed length of 6.
    A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
    (3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
    A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
    (6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data section of another marker, it is not treated any differently from the A that comes after it. It has a decompressed length of 6.
    X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
    What is the decompressed length of the file (your puzzle input)? Don't count whitespace.

     --- Part Two ---
    Apparently, the file actually uses version two of the format.

    In version two, the only difference is that markers within decompressed data are decompressed. This, the documentation explains, provides much more substantial compression capabilities, allowing many-gigabyte files to be stored in only a few kilobytes.

    For example:

    (3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
    X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY, because the decompressed data from the (8x2) marker is then further decompressed, thus triggering the (3x3) marker twice for a total of six ABC sequences.
    (27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
    (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
    Unfortunately, the computer you brought probably doesn't have enough memory to actually decompress the file; you'll have to come up with another way to get its decompressed length.

    What is the decompressed length of the file using this improved format?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day09.txt");
        int result = 0;
        for (String line : lines) {
            result += decompress(line).length();
        }
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> lines = Utilities.readInput("year2016/day09.txt");
        long result = 0;
        for (String line : lines) {
            String data = line;
//            result = totalLength(data);
        }
        System.out.println("Part 2 solution: " + result);
    }


    private String decompress(String compressed) {
        StringBuilder decompressed = new StringBuilder();
        for (int i = 0; i < compressed.length(); i++) {
            if (!isMarker(compressed, i)) {
                decompressed.append(compressed.charAt(i));
            } else {
                String marker = getMarker(compressed, i);
                int howManyCharacters = howManyCharactersToRepeat(marker);
                decompressed.append(whatToAppend(compressed, i));
                i = whereMarkerEnds(compressed, i);
                i += howManyCharacters;
            }
        }
        return decompressed.toString();
    }

    private String joinMarkers(String compressed) {
        List<Integer> markersIndexes = new ArrayList<>();
        for(int i = 0; i < compressed.length(); i++) {
            if(isMarker(compressed, i)) {
                int markerEnd = whereMarkerEnds(compressed, i);

                i = whereMarkerEnds(compressed, i);

            }
        }
        while(compressed.contains(")(")) {
//            compressed.in
        }
        return "";
    }


    private String whatToAppend(String compressed, int position) {
        StringBuilder result = new StringBuilder();
        String marker = getMarker(compressed, position);
        String subsequent = subsequentToRepeat(compressed, position);
        int times = howManyTimesToRepeat(marker);
        for (int i = 0; i < times; i++) {
            result.append(subsequent);
        }
        return result.toString();
    }

    private int whereMarkerEnds(String compressed, int position) {
        int end = 0;
        if (isMarker(compressed, position)) {
            end = compressed.indexOf(")", position);
        }
        return end;
    }

    private boolean isMarker(String compressed, int position) {
        return compressed.charAt(position) == '(' && Utilities.isNumber(String.valueOf(compressed.charAt(position + 1)));
    }

    private String getMarker(String compressed, int position) {
        String marker = compressed.substring(position + 1);
        marker = marker.substring(0, marker.indexOf(")"));
        return marker;
    }

    private int howManyCharactersToRepeat(String marker) {
        return Integer.parseInt(marker.split("x")[0]);
    }

    private int howManyTimesToRepeat(String marker) {
        return Integer.parseInt(marker.split("x")[1]);
    }

    private String subsequentToRepeat(String compressed, int position) {
        String marker = getMarker(compressed, position);
        String subsequent = compressed.substring(position);
        subsequent = subsequent.substring(subsequent.indexOf(")") + 1);
        subsequent = subsequent.substring(0, howManyCharactersToRepeat(marker));
        return subsequent;
    }

    private boolean hasMarker(String compressed) {
        boolean found = false;
        while (!found && compressed.contains("(")) {
            compressed = compressed.substring(compressed.indexOf("(") + 1);
            if (Utilities.isNumber(String.valueOf(compressed.charAt(0)))) {
                found = true;
            }
        }
        return found;
    }

    private int firstMarker(String compressed) {
        boolean found = false;
        int markerPosition = compressed.indexOf("(");
        while (!found && compressed.substring(markerPosition + 1).contains("(")) {
            if (Utilities.isNumber(String.valueOf(compressed.charAt(markerPosition + 1)))) {
                found = true;
            } else {
                markerPosition = compressed.indexOf("(", markerPosition + 1);
            }
        }
        if (!found) {
            markerPosition = -1;
        }
        return markerPosition;
    }


}
