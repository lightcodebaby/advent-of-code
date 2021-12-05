package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day07 {

    /*
    --- Day 7: Internet Protocol Version 7 ---
    While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).

    An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.

    For example:

    abba[mnop]qrst supports TLS (abba outside square brackets).
    abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
    aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
    ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
    How many IPs in your puzzle input support TLS?

    --- Part Two ---
    You would also like to know which IPs support SSL (super-secret listening).

    An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the same character twice with a different character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.

    For example:

    aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
    xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
    aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
    zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
    How many IPs in your puzzle input support SSL?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day07.txt");
        int result = 0;
        for (String line : input) {
            if (supportTLS(line)) {
                result++;
            }
        }
        System.out.println("Part 1 solution: " + result);
    }

    private boolean supportTLS(String line) {
        boolean supportTLS = false;
        List<String> insideBrackets = insideBrackets(line);
        List<String> outsideBrackets = outsideBrackets(line);
        if (insideBrackets.stream().noneMatch(this::hasABBA) && outsideBrackets.stream().anyMatch(this::hasABBA)) {
            supportTLS = true;
        }
        return supportTLS;
    }

    private List<String> insideBrackets(String line) {
        List<String> insideBrackets = new ArrayList<>();
        while (line.contains("[")) {
            int indexFrom = line.indexOf("[");
            int indexTo = line.indexOf("]");
            String bracket = line.substring(indexFrom + 1, indexTo);
            insideBrackets.add(bracket);
            line = line.substring(0, indexFrom) + line.substring(indexTo + 1);
        }
        return insideBrackets;
    }

    private List<String> outsideBrackets(String line) {
        List<String> outsideBrackets = new ArrayList<>();
        while (line.contains("[")) {
            int indexFrom = line.indexOf("[");
            int indexTo = line.indexOf("]");
            outsideBrackets.add(line.substring(0, indexFrom));
            line = line.substring(indexTo + 1);
        }
        outsideBrackets.add(line);
        return outsideBrackets;
    }

    private boolean hasABBA(String string) {
        boolean hasABBA = false;
        for (int i = 0; i < string.length() - 3 && !hasABBA; i++) {
            String firstHalf = string.substring(i, i + 2);
            String secondHalf = string.substring(i + 2, i + 4);
            if (!firstHalf.equals(secondHalf) && firstHalf.equals(new StringBuilder(secondHalf).reverse().toString())) {
                hasABBA = true;
            }
        }
        return hasABBA;
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day07.txt");
        int result = 0;
        for (String line : input) {
            if (supportSSL(line)) {
                result++;
            }
        }
        System.out.println("Part 2 solution: " + result);
    }

    private boolean supportSSL(String line) {
        List<String> insideBrackets = insideBrackets(line);
        List<String> outsideBrackets = outsideBrackets(line);
        List<String> abas = getAllAbas(outsideBrackets);
        List<String> babs = getBabsForAbas(abas);
        return babs.stream().anyMatch(bab -> insideBrackets.stream().anyMatch(string -> string.contains(bab)));
    }

    private List<String> getAllAbas(List<String> outsideBrackets) {
        List<String> abas = new ArrayList<>();
        for (String string : outsideBrackets) {
            abas.addAll(getAbas(string));
        }
        return abas;
    }

    private List<String> getAbas(String string) {
        List<String> abas = new ArrayList<>();
        for (int i = 0; i < string.length() - 2; i++) {
            char firstChar = string.charAt(i);
            char secondChar = string.charAt(i + 1);
            char thirdChar = string.charAt(i + 2);
            if (firstChar != secondChar && firstChar == thirdChar) {
                abas.add(string.substring(i, i + 3));
            }
        }
        return abas;
    }

    private List<String> getBabsForAbas(List<String> abas) {
        List<String> babs = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (String aba : abas) {
            stringBuilder.append(aba.charAt(1));
            stringBuilder.append(aba.charAt(0));
            stringBuilder.append(aba.charAt(1));
            babs.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        return babs;
    }


}
