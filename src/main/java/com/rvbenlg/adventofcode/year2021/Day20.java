package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.List;

public class Day20 {

    /*
    --- Day 20: Trench Map ---
    With the scanners fully deployed, you turn their attention to mapping the floor of the ocean trench.

    When you get back the image from the scanners, it seems to just be random noise. Perhaps you can combine an image enhancement algorithm and the input image (your puzzle input) to clean it up a little.

    For example:

    ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
    #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
    .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
    .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
    .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
    ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
    ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

    #..#.
    #....
    ##..#
    ..#..
    ..###
    The first section is the image enhancement algorithm. It is normally given on a single line, but it has been wrapped to multiple lines in this example for legibility. The second section is the input image, a two-dimensional grid of light pixels (#) and dark pixels (.).

    The image enhancement algorithm describes how to enhance an image by simultaneously converting all pixels in the input image into an output image. Each pixel of the output image is determined by looking at a 3x3 square of pixels centered on the corresponding input image pixel. So, to determine the value of the pixel at (5,10) in the output image, nine pixels from the input image need to be considered: (4,9), (4,10), (4,11), (5,9), (5,10), (5,11), (6,9), (6,10), and (6,11). These nine input pixels are combined into a single binary number that is used as an index in the image enhancement algorithm string.

    For example, to determine the output pixel that corresponds to the very middle pixel of the input image, the nine pixels marked by [...] would need to be considered:

    # . . # .
    #[. . .].
    #[# . .]#
    .[. # .].
    . . # # #
    Starting from the top-left and reading across each row, these pixels are ..., then #.., then .#.; combining these forms ...#...#.. By turning dark pixels (.) into 0 and light pixels (#) into 1, the binary number 000100010 can be formed, which is 34 in decimal.

    The image enhancement algorithm string is exactly 512 characters long, enough to match every possible 9-bit binary number. The first few characters of the string (numbered starting from zero) are as follows:

    0         10        20        30  34    40        50        60        70
    |         |         |         |   |     |         |         |         |
    ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
    In the middle of this first group of characters, the character at index 34 can be found: #. So, the output pixel in the center of the output image should be #, a light pixel.

    This process can then be repeated to calculate every pixel of the output image.

    Through advances in imaging technology, the images being operated on here are infinite in size. Every pixel of the infinite output image needs to be calculated exactly based on the relevant pixels of the input image. The small input image you have is only a small region of the actual infinite input image; the rest of the input image consists of dark pixels (.). For the purposes of the example, to save on space, only a portion of the infinite-sized input and output images will be shown.

    The starting input image, therefore, looks something like this, with more dark pixels (.) extending forever in every direction not shown here:

    ...............
    ...............
    ...............
    ...............
    ...............
    .....#..#......
    .....#.........
    .....##..#.....
    .......#.......
    .......###.....
    ...............
    ...............
    ...............
    ...............
    ...............
    By applying the image enhancement algorithm to every pixel simultaneously, the following output image can be obtained:

    ...............
    ...............
    ...............
    ...............
    .....##.##.....
    ....#..#.#.....
    ....##.#..#....
    ....####..#....
    .....#..##.....
    ......##..#....
    .......#.#.....
    ...............
    ...............
    ...............
    ...............
    Through further advances in imaging technology, the above output image can also be used as an input image! This allows it to be enhanced a second time:

    ...............
    ...............
    ...............
    ..........#....
    ....#..#.#.....
    ...#.#...###...
    ...#...##.#....
    ...#.....#.#...
    ....#.#####....
    .....#.#####...
    ......##.##....
    .......###.....
    ...............
    ...............
    ...............
    Truly incredible - now the small details are really starting to come through. After enhancing the original input image twice, 35 pixels are lit.

    Start with the original input image and apply the image enhancement algorithm twice, being careful to account for the infinite size of the images. How many pixels are lit in the resulting image?

     --- Part Two ---
    You still can't quite make out the details in the image. Maybe you just didn't enhance it enough.

    If you enhance the starting input image in the above example a total of 50 times, 3351 pixels are lit in the final output image.

    Start again with the original input image and apply the image enhancement algorithm 50 times. How many pixels are lit in the resulting image?
     */

    private boolean[][] pixels = new boolean[0][0];
    private boolean[] algorithm = new boolean[0];
    private int iterations = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day20.txt");
        fillPixels(input.subList(2, input.size()));
        fillAlgorithm(input.get(0));
        iterations = 2;
        applyAlgorithmNTimes();
        int result = howManyPixelsAreLit();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day20.txt");
        fillPixels(input.subList(2, input.size()));
        fillAlgorithm(input.get(0));
        iterations = 50;
        applyAlgorithmNTimes();
        int result = howManyPixelsAreLit();
        System.out.println("Part 2 solution: " + result);
    }

    private int howManyPixelsAreLit() {
        int rows = pixels.length;
        int columns = pixels[0].length;
        int result = 0;
        for (int i = iterations; i < rows - iterations; i++) {
            for (int j = iterations; j < columns - iterations; j++) {
                if (pixels[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    private void applyAlgorithmNTimes() {
        pixels = increaseSize(pixels);
        for (int i = 0; i < iterations; i++) {
            applyAlgorithm();
        }
    }

    private void applyAlgorithm() {
        boolean[][] newImage = cloneImage(pixels);
        for (int i = 1; i < pixels.length - 1; i++) {
            for (int j = 1; j < pixels[0].length - 1; j++) {
                newImage[i][j] = getNewPixel(i, j);
            }
        }
        pixels = newImage;
    }

    private boolean getNewPixel(int row, int column) {
        String binaryNumber = getBinaryNumberCenteredOn(row, column);
        int position = Integer.parseInt(binaryNumber, 2);
        return algorithm[position];
    }

    private String getBinaryNumberCenteredOn(int row, int column) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                resultBuilder.append(pixels[i][j] ? 1 : 0);
            }
        }
        return resultBuilder.toString();
    }

    private void fillAlgorithm(String input) {
        algorithm = new boolean[input.length()];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '#') {
                algorithm[i] = true;
            }
        }
    }

    private void fillPixels(List<String> input) {
        int totalRows = input.size();
        int totalColumns = input.get(0).length();
        pixels = new boolean[totalRows][totalColumns];
        for (int i = 0; i < input.size(); i++) {
            String row = input.get(i);
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c == '#') {
                    pixels[i][j] = true;
                }
            }
        }
    }

    private boolean[][] increaseSize(boolean[][] matrix) {
        boolean[][] newMatrix = cloneImage(matrix);
        newMatrix = addFirstRow(newMatrix);
        newMatrix = addLastRow(newMatrix);
        newMatrix = addFirstColumn(newMatrix);
        newMatrix = addLastColumn(newMatrix);
        return newMatrix;
    }

    private boolean[][] cloneImage(boolean[][] original) {
        int rows = original.length;
        int columns = original[0].length;
        boolean[][] result = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = original[i][j];
            }
        }
        return result;
    }

    private void printImage(boolean[][] image) {
        printHeaderAndFooter();
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                System.out.print(image[i][j] ? "#" : ".");
            }
            if (i < image.length - 1) {
                System.out.println();
            }
        }
        printHeaderAndFooter();
    }

    private void printHeaderAndFooter() {
        int columns = pixels[0].length;
        System.out.println();
        for (int i = 0; i < columns; i++) {
            System.out.print("-");
        }
        System.out.println();
    }


    private boolean[][] addFirstRow(boolean[][] matrix) {
        int increase = 2 * iterations;
        int rows = matrix.length + increase;
        int columns = matrix[0].length;
        boolean[][] result = new boolean[rows][columns];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < columns; j++) {
                result[i + increase][j] = matrix[i][j];
            }
        }
        return result;
    }


    private boolean[][] addLastRow(boolean[][] matrix) {
        int increase = 2 * iterations;
        int rows = matrix.length + increase;
        int columns = matrix[0].length;
        boolean[][] result = new boolean[rows][columns];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }


    private boolean[][] addFirstColumn(boolean[][] matrix) {
        int increase = 2 * iterations;
        int rows = matrix.length;
        int columns = matrix[0].length + increase;
        boolean[][] result = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j + increase] = matrix[i][j];
            }
        }
        return result;
    }


    private boolean[][] addLastColumn(boolean[][] matrix) {
        int increase = 2 * iterations;
        int rows = matrix.length;
        int columns = matrix[0].length + increase;
        boolean[][] result = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

}
