package com.rvbenlg.adventofcode.year2021;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

    /*
    --- Day 10: Syntax Scoring ---
    You ask the submarine to determine the best route out of the deep-sea cave, but it only replies:

    Syntax error in navigation subsystem on line: all of them
    All of them?! The damage is worse than you thought. You bring up a copy of the navigation subsystem (your puzzle input).

    The navigation subsystem syntax is made of several lines containing chunks. There are one or more chunks on each line, and chunks contain zero or more other chunks. Adjacent chunks are not separated by any delimiter; if one chunk stops, the next chunk (if any) can immediately start. Every chunk must open and close with one of four legal pairs of matching characters:

    If a chunk opens with (, it must close with ).
    If a chunk opens with [, it must close with ].
    If a chunk opens with {, it must close with }.
    If a chunk opens with <, it must close with >.
    So, () is a legal chunk that contains no other chunks, as is []. More complex but valid chunks include ([]), {()()()}, <([{}])>, [<>({}){}[([])<>]], and even (((((((((()))))))))).

    Some lines are incomplete, but others are corrupted. Find and discard the corrupted lines first.

    A corrupted line is one where a chunk closes with the wrong character - that is, where the characters it opens and closes with do not form one of the four legal pairs listed above.

    Examples of corrupted chunks include (], {()()()>, (((()))}, and <([]){()}[{}]). Such a chunk can appear anywhere within a line, and its presence causes the whole line to be considered corrupted.

    For example, consider the following navigation subsystem:

    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
    Some of the lines aren't corrupted, just incomplete; you can ignore these lines for now. The remaining five lines are corrupted:

    {([(<{}[<>[]}>{[]{[(<()> - Expected ], but found } instead.
    [[<[([]))<([[{}[[()]]] - Expected ], but found ) instead.
    [{[{({}]{}}([{[{{{}}([] - Expected ), but found ] instead.
    [<(<(<(<{}))><([]([]() - Expected >, but found ) instead.
    <{([([[(<>()){}]>(<<{{ - Expected ], but found > instead.
    Stop at the first incorrect closing character on each corrupted line.

    Did you know that syntax checkers actually have contests to see who can get the high score for syntax errors in a file? It's true! To calculate the syntax error score for a line, take the first illegal character on the line and look it up in the following table:

    ): 3 points.
    ]: 57 points.
    }: 1197 points.
    >: 25137 points.
    In the above example, an illegal ) was found twice (2*3 = 6 points), an illegal ] was found once (57 points), an illegal } was found once (1197 points), and an illegal > was found once (25137 points). So, the total syntax error score for this file is 6+57+1197+25137 = 26397 points!

    Find the first illegal character in each corrupted line of the navigation subsystem. What is the total syntax error score for those errors?

    --- Part Two ---
    Now, discard the corrupted lines. The remaining lines are incomplete.

    Incomplete lines don't have any incorrect characters - instead, they're missing some closing characters at the end of the line. To repair the navigation subsystem, you just need to figure out the sequence of closing characters that complete all open chunks in the line.

    You can only use closing characters (), ], }, or >), and you must add them in the correct order so that only legal pairs are formed and all chunks end up closed.

    In the example above, there are five incomplete lines:

    [({(<(())[]>[[{[]{<()<>> - Complete by adding }}]])})].
    [(()[<>])]({[<{<<[]>>( - Complete by adding )}>]}).
    (((({<>}<{<{<>}{[]{[]{} - Complete by adding }}>}>)))).
    {<[[]]>}<{[{[{[]{()[[[] - Complete by adding ]]}}]}]}>.
    <{([{{}}[<[[[<>{}]]]>[]] - Complete by adding ])}>.
    Did you know that autocomplete tools also have contests? It's true! The score is determined by considering the completion string character-by-character. Start with a total score of 0. Then, for each character, multiply the total score by 5 and then increase the total score by the point value given for the character in the following table:

    ): 1 point.
    ]: 2 points.
    }: 3 points.
    >: 4 points.
    So, the last completion string above - ])}> - would be scored as follows:

    Start with a total score of 0.
    Multiply the total score by 5 to get 0, then add the value of ] (2) to get a new total score of 2.
    Multiply the total score by 5 to get 10, then add the value of ) (1) to get a new total score of 11.
    Multiply the total score by 5 to get 55, then add the value of } (3) to get a new total score of 58.
    Multiply the total score by 5 to get 290, then add the value of > (4) to get a new total score of 294.
    The five lines' completion strings have total scores as follows:

    }}]])})] - 288957 total points.
    )}>]}) - 5566 total points.
    }}>}>)))) - 1480781 total points.
    ]]}}]}]}> - 995444 total points.
    ])}> - 294 total points.
    Autocomplete tools are an odd bunch: the winner is found by sorting all of the scores and then taking the middle score. (There will always be an odd number of scores to consider.) In this example, the middle score is 288957 because there are the same number of scores smaller and larger than it.

    Find the completion string for each incomplete line, score the completion strings, and sort the scores. What is the middle score?
     */

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        List<String> input = Utilities.readInput("year2021/day10.txt");
        int result = calculateSyntaxErrorScore(input);
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        List<String> input = Utilities.readInput("year2021/day10.txt");
        List<Long> autoCompleteScores = getAutoCompleteScores(input);
        long result = getMiddleScore(autoCompleteScores);
        System.out.println("Part 2 solution: " + result);
    }

    private long getMiddleScore(List<Long> scores) {
        return scores.get(scores.size() / 2);
    }

    private List<Long> getAutoCompleteScores(List<String> input) {
        List<Long> result = new ArrayList<>();
        for (String line : input) {
            if (getFirstIllegalCharacter(line) == '#') {
                List<Character> sequenceToClose = getSequenceToClose(line);
                result.add(calculateAutoCompleteScore(sequenceToClose));
            }
        }
        result = result.stream().sorted().collect(Collectors.toList());
        return result;
    }

    private long calculateAutoCompleteScore(List<Character> sequenceToClose) {
        long result = 0L;
        for(int i = sequenceToClose.size() - 1; i >= 0; i--) {
            result *= 5;
            result += getAutoCompleteScore(sequenceToClose.get(i));
        }
        return result;
    }

    private int getAutoCompleteScore(char toClose) {
        int result = 0;
        if (toClose == '(') {
            result = 1;
        } else if (toClose == '[') {
            result = 2;
        } else if (toClose == '{') {
            result = 3;
        } else if (toClose == '<') {
            result = 4;
        }
        return result;
    }

    private List<Character> getSequenceToClose(String line) {
        List<Character> openingChars = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                openingChars.add(c);
            } else {
                openingChars.remove(openingChars.size() - 1);
            }
        }
        return openingChars;
    }

    private int calculateSyntaxErrorScore(List<String> input) {
        int result = 0;
        for (String line : input) {
            char firstIllegarCharacter = getFirstIllegalCharacter(line);
            result += getSyntaxErrorScore(firstIllegarCharacter);
        }
        return result;
    }

    private int getSyntaxErrorScore(char firstIllegalCharacter) {
        int result = 0;
        if (firstIllegalCharacter == ')') {
            result = 3;
        } else if (firstIllegalCharacter == ']') {
            result = 57;
        } else if (firstIllegalCharacter == '}') {
            result = 1197;
        } else if (firstIllegalCharacter == '>') {
            result = 25137;
        }
        return result;
    }

    private char getFirstIllegalCharacter(String line) {
        List<Character> openingChars = new ArrayList<>();
        char result = '#';
        for (int i = 0; i < line.length() && result == '#'; i++) {
            char c = line.charAt(i);
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                openingChars.add(c);
            } else {
                if (openingChars.isEmpty()) {
                    result = c;
                } else {
                    char lastOpeningChar = openingChars.get(openingChars.size() - 1);
                    if ((lastOpeningChar == '(' && c != ')')
                            || (lastOpeningChar == '[' && c != ']')
                            || (lastOpeningChar == '{' && c != '}')
                            || (lastOpeningChar == '<' && c != '>')) {
                        result = c;
                    } else {
                        openingChars.remove(openingChars.size() - 1);
                    }
                }
            }
        }
        return result;
    }

}
