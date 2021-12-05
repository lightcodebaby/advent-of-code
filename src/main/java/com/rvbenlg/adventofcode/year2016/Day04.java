package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class Day04 {

    /*
    --- Day 4: Security Through Obscurity ---
    Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.

    Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.

    A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:

    aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
    a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
    not-a-real-room-404[oarel] is a real room.
    totally-real-room-200[decoy] is not.
    Of the real rooms from the list above, the sum of their sector IDs is 1514.

    What is the sum of the sector IDs of the real rooms?

    --- Part Two ---
    With all the decoy data out of the way, it's time to decrypt this list and get moving.

    The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.

    To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.

    For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.

    What is the sector ID of the room where North Pole objects are stored?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2016/day04.txt");
        int result = 0;
        for (String line : input) {
            int sectorId = Integer.parseInt(getSectorId(line));
            String checksum = getChecksum(line);
            String onlyLetters = getLetters(line);
            String sortedLetters = sortAlphabetically(onlyLetters);
            String mostCommonLetters = mostCommonLetters(sortedLetters);
            if (isValidRoom(checksum, mostCommonLetters)) {
                result += sectorId;
            }
        }
        System.out.println("Part 1 solution: " + result);
    }

    private String getChecksum(String code) {
        String insideBrackets = code.split("\\[")[1].replace("]", "");
        return insideBrackets;
    }

    private String getSectorId(String code) {
        String[] splittedCode = code.split("-");
        String sectorId = splittedCode[splittedCode.length - 1].split("\\[")[0];
        return sectorId;
    }

    private String getLetters(String code) {
        int lastDashIndex = code.lastIndexOf("-");
        String onlyCode = code.substring(0, lastDashIndex);
        return onlyCode.replace("-", "");
    }

    private String sortAlphabetically(String code) {
        char[] letters = code.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    private Hashtable<Character, Integer> howManyTimes(String code) {
        Hashtable<Character, Integer> result = new Hashtable<>();
        for (char character : code.toCharArray()) {
            if (result.containsKey(character)) {
                int times = result.get(character);
                times++;
                result.put(character, times);
            } else {
                result.put(character, 1);
            }
        }
        return result;
    }

    private String mostCommonLetters(String characters) {
        Hashtable<Character, Integer> howManyTimes = howManyTimes(characters);
        StringBuilder mostCommonLetters = new StringBuilder();
        while (mostCommonLetters.length() < 5) {
            int maxTimes = howManyTimes.values().stream().filter(value -> howManyTimes.values().stream().noneMatch(value2 -> value < value2)).findFirst().get();
            for (char character : characters.toCharArray()) {
                if (howManyTimes.containsKey(character) && howManyTimes.get(character) == maxTimes) {
                    if (mostCommonLetters.length() < 5) {
                        mostCommonLetters.append(character);
                        howManyTimes.remove(character);
                    }
                }
            }
        }
        return mostCommonLetters.toString();
    }

    private boolean isValidRoom(String checksum, String mostCommonLetters) {
        return checksum.equals(mostCommonLetters);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2016/day04.txt");
        int result = 0;
        for (String line : input) {
            String encryptedRoom = getEncryptedRoomName(line);
            String decryptedRoom = "";
            int sectorId = Integer.parseInt(getSectorId(line));
            String checksum = getChecksum(line);
            String onlyLetters = getLetters(line);
            String sortedLetters = sortAlphabetically(onlyLetters);
            String mostCommonLetters = mostCommonLetters(sortedLetters);
            if (isValidRoom(checksum, mostCommonLetters)) {
                decryptedRoom = decryptRoomName(encryptedRoom, sectorId);
                if (decryptedRoom.equals("northpole object storage")) {
                    result = sectorId;
                }
            }
        }
        System.out.println("Part 2 solution: " + result);
    }

    private String getEncryptedRoomName(String code) {
        int lastDashIndex = code.lastIndexOf("-");
        String onlyCode = code.substring(0, lastDashIndex);
        return onlyCode.replace("-", " ");
    }

    private String decryptRoomName(String code, int sectorId) {
        List<String> alphabet = Arrays.asList(Utilities.ALPHABET);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            String encryptedLetter = code.substring(i, i + 1);
            if (encryptedLetter.equals(" ")) {
                result.append(" ");
            } else {
                String decryptedLetter = alphabet.get((alphabet.indexOf(encryptedLetter) + sectorId) % alphabet.size());
                result.append(decryptedLetter);
            }
        }
        return result.toString();
    }

}
