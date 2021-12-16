package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class Day16 {

    private int position = 0;
    private int totalVersion = 0;

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day16.txt");
        int result = decode(input.get(0));
        System.out.println("Part 1 solution: " + totalVersion);
    }

    private int decode(String hex) {
        String binary = getBinaryRepresentation(hex);
        int version = getVersion(binary.substring(position, position + 3));
        int typeID = getTypeID(binary.substring(position + 3, position + 6));
        totalVersion += version;
        position += 6;
        if (typeID == 4) {
            String literalValue = decodeLiteralValue(binary.substring(position));
//            int value = Integer.parseInt(literalValue, 2);
            position += literalValue.length() + (literalValue.length() / 4);
        } else {
            int lengthTypeID = Integer.parseInt(String.valueOf(binary.charAt(position)));
            position++;
            if (lengthTypeID == 0) {
                int totalSubPacketLength = getSubPacketsTotalLength(binary.substring(position, position + 15));
                position += 15;
                int auxPosition = position;
                while(position != auxPosition + totalSubPacketLength) {
                    decode(hex);
                }
            } else {
                int subPacketsNumber = fromBinaryToDecimal(binary.substring(position, position + 11));
                position += 11;
                for(int i = 0; i < subPacketsNumber; i++) {
                    decode(hex);
                }
            }
        }
        return 0;
    }

    private String decodeLiteralValue(String toDecode) {
        StringBuilder resultBuilder = new StringBuilder();
        boolean decoded = false;
        for (int i = 0; i < toDecode.length() && !decoded; i += 5) {
            resultBuilder.append(toDecode, i + 1, i + 5);
            decoded = toDecode.charAt(i) == '0';
        }
        return resultBuilder.toString();
    }

    private int fromBinaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

    private int getSubPacketsTotalLength(String binary) {
        return Integer.parseInt(binary, 2);
    }

    private int getTypeID(String binary) {
        return Integer.parseInt(binary, 2);
    }

    private int getVersion(String binary) {
        return Integer.parseInt(binary, 2);
    }

    private String getBinaryRepresentation(String hex) {
        StringBuilder resultBuilder = new StringBuilder();
        for (char c : hex.toCharArray()) {
            resultBuilder.append(getBinaryRepresentation(c));
        }
        return resultBuilder.toString();
    }

    private String getBinaryRepresentation(char c) {
        String result = "";
        switch (c) {
            case '0':
                result = "0000";
                break;
            case '1':
                result = "0001";
                break;
            case '2':
                result = "0010";
                break;
            case '3':
                result = "0011";
                break;
            case '4':
                result = "0100";
                break;
            case '5':
                result = "0101";
                break;
            case '6':
                result = "0110";
                break;
            case '7':
                result = "0111";
                break;
            case '8':
                result = "1000";
                break;
            case '9':
                result = "1001";
                break;
            case 'A':
                result = "1010";
                break;
            case 'B':
                result = "1011";
                break;
            case 'C':
                result = "1100";
                break;
            case 'D':
                result = "1101";
                break;
            case 'E':
                result = "1110";
                break;
            case 'F':
                result = "1111";
                break;
        }
        return result;
    }

}
