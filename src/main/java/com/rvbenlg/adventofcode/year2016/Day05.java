package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Day05 {

    /*
    --- Day 5: How About a Nice Game of Chess? ---
    You are faced with a security door designed by Easter Bunny engineers that seem to have acquired most of their security knowledge by watching hacking movies.

    The eight-character password for the door is generated one character at a time by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).

    A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes. If it does, the sixth character in the hash is the next character of the password.

    For example, if the Door ID is abc:

    The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing abc3231929; the sixth character of the hash, and thus the first character of the password, is 1.
    5017308 produces the next interesting hash, which starts with 000008f82..., so the second character of the password is 8.
    The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
    In this example, after continuing this search a total of eight times, the password is 18f47a30.

    Given the actual Door ID, what is the password?

    --- Part Two ---
    As the door slides open, you are presented with a second door that uses a slightly more inspired security mechanism. Clearly unimpressed by the last version (in what movie is the password decrypted in order?!), the Easter Bunny engineers have worked out a better solution.

    Instead of simply filling in the password from left to right, the hash now also indicates the position within the password to fill. You still look for hashes that begin with five zeroes; however, now, the sixth character represents the position (0-7), and the seventh character is the character to put in that position.

    A hash result of 000001f means that f is the second character in the password. Use only the first result for each position, and ignore invalid positions.

    For example, if the Door ID is abc:

    The first interesting hash is from abc3231929, which produces 0000015...; so, 5 goes in position 1: _5______.
    In the previous method, 5017308 produced an interesting hash; however, it is ignored, because it specifies an invalid position (8).
    The second interesting hash is at index 5357525, which produces 000004e...; so, e goes in position 4: _5__e___.
    You almost choke on your popcorn as the final character falls into place, producing the password 05ace8e3.

    Given the actual Door ID and this new method, what is the password? Be extra proud of your solution if it uses a cinematic "decrypting" animation.
     */

    public void solve() throws IOException, NoSuchAlgorithmException {
        part1();
        part2();
    }

    private void part1() throws IOException, NoSuchAlgorithmException {
        List<String> lines = Utilities.readInput("year2016/day05.txt");
        String result = "";
        for (String line : lines) {
            result = getPassword(line);
        }
        System.out.println("Part 1 solution: " + result);
    }

    private String getPassword(String input) throws NoSuchAlgorithmException {
        int index = 0;
        StringBuilder password = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("MD5");
        while (password.length() < 8) {
            String auxInput = input + index;
            md.update(auxInput.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter.printHexBinary(digest);
            if (hash.startsWith("00000")) {
                password.append(hash.charAt(5));
            }
            index++;
        }
        return password.toString().toLowerCase();
    }

    private void part2() throws NoSuchAlgorithmException, IOException {
        List<String> lines = Utilities.readInput("year2016/day05.txt");
        String result = "";
        for (String line : lines) {
            result = getSecondPassword(line);
        }
        System.out.println("Part 2 solution: " + result);
    }

    private String getSecondPassword(String input) throws NoSuchAlgorithmException {
        String result = "________";
        List<Integer> positions = new ArrayList<>();
        int index = 0;
        MessageDigest md = MessageDigest.getInstance("MD5");
        while (positions.size() < 8) {
            String auxInput = input + index;
            md.update(auxInput.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter.printHexBinary(digest);
            if (isValidHash(hash)) {
                String strPosition = hash.substring(5, 6);
                if (isValidPosition(strPosition, positions)) {
                    int position = Integer.parseInt(strPosition);
                    result = result.substring(0, position) + hash.charAt(6) + result.substring(position + 1);
                    positions.add(position);
                }
            }
            index++;
        }
        return result.toLowerCase();
    }

    private boolean isValidHash(String hash) {
        return hash.startsWith("00000");
    }

    private boolean isValidPosition(String strPosition, List<Integer> positions) {
        return Utilities.isNumber(strPosition) && !positions.contains(Integer.parseInt(strPosition)) && Integer.parseInt(strPosition) < 8;
    }

}
