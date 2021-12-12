package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day04 {

    /*
    --- Day 4: Giant Squid ---
    You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight. What you can see, however, is a giant squid that has attached itself to the outside of your submarine.

    Maybe it wants to play bingo?

    Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears. (Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)

    The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time. It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input). For example:

    7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

    22 13 17 11  0
     8  2 23  4 24
    21  9 14 16  7
     6 10  3 18  5
     1 12 20 15 19

     3 15  0  2 22
     9 18 13 17  5
    19  8  7 25 23
    20 11 10 24  4
    14 21 16 12  6

    14 21 17 24  4
    10 16 15  9 19
    18  8 23 26 20
    22 11 13  6  5
     2  0 12  3  7
    After the first five numbers are drawn (7, 4, 9, 5, and 11), there are no winners, but the boards are marked as follows (shown here adjacent to each other to save space):

    22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
    21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
    After the next six numbers are drawn (17, 23, 2, 0, 14, and 21), there are still no winners:

    22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
    21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
    Finally, 24 is drawn:

    22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
    21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
    At this point, the third board wins because it has at least one complete row or column of marked numbers (in this case, the entire top row is marked: 14 21 17 24 4).

    The score of the winning board can now be calculated. Start by finding the sum of all unmarked numbers on that board; in this case, the sum is 188. Then, multiply that sum by the number that was just called when the board won, 24, to get the final score, 188 * 24 = 4512.

    To guarantee victory against the giant squid, figure out which board will win first. What will your final score be if you choose that board?

    --- Part Two ---
    On the other hand, it might be wise to try a different strategy: let the giant squid win.

    You aren't sure how many bingo boards a giant squid could play at once, so rather than waste time counting its arms, the safe thing to do is to figure out which board will win last and choose that one. That way, no matter which boards it picks, it will win for sure.

    In the above example, the second board is the last to win, which happens after 13 is eventually called and its middle column is completely marked. If you were to keep playing until this point, the second board would have a sum of unmarked numbers equal to 148 for a final score of 148 * 13 = 1924.

    Figure out which board will win last. Once it wins, what would its final score be?
     */

    private List<Integer> sequence = new ArrayList<>();
    private List<Board> boards = new ArrayList<>();
    private final int dimension = 5;
    private int lastNumber = 0;
    private List<Integer> winners = new ArrayList<>();


    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day04.txt");
        fillSequence(input);
        fillBoards(input);
        int winner = play();
        int score = calculateScore(winner);
        System.out.println("Part 1 solution: " + score);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2021/day04.txt");
        fillSequence(input);
        fillBoards(input);
        int loser = lose();
        int score = calculateScore(loser);
        System.out.println("Part 2 solution: " + score);
    }

    private int calculateScore(int winner) {
        Board board = boards.get(winner);
        int unMarked = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (!board.matrix[i][j].marked) {
                    unMarked += board.matrix[i][j].value;
                }
            }
        }
        return unMarked * lastNumber;
    }

    private int play() {
        int anyWinner = -1;
        for (int i = 0; i < sequence.size() && anyWinner == -1; i++) {
            int number = sequence.get(i);
            lastNumber = number;
            markNumber(number);
            anyWinner = checkIfAnyWinner();
        }
        return anyWinner;
    }

    private int lose() {
        for (int i = 0; i < sequence.size() && winners.size() != boards.size(); i++) {
            int number = sequence.get(i);
            lastNumber = number;
            markNumber(number);
            checkWinners();
        }
        return winners.get(winners.size() - 1);
    }

    private void checkWinners() {
        for (int i = 0; i < boards.size(); i++) {
            if (!winners.contains(i)) {
                Board board = boards.get(i);
                if (checkIfWinner(board)) {
                    winners.add(i);
                }
            }
        }
    }

    private int checkIfAnyWinner() {
        int result = -1;
        for (int i = 0; i < boards.size() && result == -1; i++) {
            Board board = boards.get(i);
            if (checkIfWinner(board)) {
                result = i;
            }
        }
        return result;
    }

    private boolean checkIfWinner(Board board) {
        boolean result = false;
        for (int i = 0; i < dimension && !result; i++) {
            boolean keepGoingRow = true;
            boolean keepGoingColumn = true;
            for (int j = 0; j < dimension && (keepGoingRow || keepGoingColumn); j++) {
                if (keepGoingRow) {
                    keepGoingRow = board.matrix[i][j].marked;
                }
                if (keepGoingColumn) {
                    keepGoingColumn = board.matrix[j][i].marked;
                }
            }
            result = (keepGoingRow || keepGoingColumn);
        }
        return result;
    }

    private void markNumber(int number) {
        for (Board board : boards) {
            boolean marked = false;
            for (int i = 0; i < board.matrix.length && !marked; i++) {
                for (int j = 0; j < board.matrix[i].length && !marked; j++) {
                    if (board.matrix[i][j].value == number) {
                        board.matrix[i][j].marked = true;
                        marked = true;
                    }
                }
            }
        }
    }

    private void fillSequence(List<String> input) {
        String[] numbers = input.get(0).split(",");
        for (String number : numbers) {
            sequence.add(Integer.parseInt(number));
        }
    }

    private void fillBoards(List<String> input) {
        Number[][] matrix = new Number[dimension][dimension];
        int row = 0;
        for (int i = 1; i < input.size(); i++) {
            String line = input.get(i).replace("  ", " ").trim();
            if (line.isEmpty()) {
                if (row != 0) {
                    boards.add(new Board(matrix));
                    matrix = new Number[dimension][dimension];
                    row = 0;
                }
            } else {
                String[] numbers = line.split(" ");
                for (int j = 0; j < numbers.length; j++) {
                    matrix[row][j] = new Number(Integer.parseInt(numbers[j]));
                }
                row++;
            }
        }
        boards.add(new Board(matrix));
    }

    private void resetVariables() {
        sequence = new ArrayList<>();
        boards = new ArrayList<>();
        winners = new ArrayList<>();
        lastNumber = 0;
    }

    private class Board {

        Number[][] matrix;

        private Board(Number[][] matrix) {
            this.matrix = matrix;
        }
    }

    private class Number {

        int value;
        boolean marked;

        private Number(int value) {
            this.value = value;
            marked = false;
        }
    }

}
