class Part1:
    """
    --- Day 8: Matchsticks ---
    Space on the sleigh is limited this year, and so Santa will be bringing his list as a digital copy. He needs to know how much space it will take up when stored.

    It is common in many programming languages to provide a way to escape special characters in strings. For example, C, JavaScript, Perl, Python, and even PHP handle special characters in very similar ways.

    However, it is important to realize the difference between the number of characters in the code representation of the string literal and the number of characters in the in-memory string itself.

    For example:

    "" is 2 characters of code (the two double quotes), but the string contains zero characters.
    "abc" is 5 characters of code, but 3 characters in the string data.
    "aaa|"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single, escaped quote character, for a total of 7 characters in the string data.
    "|x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using hexadecimal notation.
    Santa's list is a file that contains many double-quoted string literals, one on each line. The only escape sequences used are || (which represents a single backslash), |" (which represents a lone double-quote character), and |x plus two hexadecimal characters (which represents a single character with that ASCII code).

    Disregarding the whitespace in the file, what is the number of characters of code for string literals minus the number of characters in memory for the values of the strings in total for the entire file?

    For example, given the four strings above, the total number of characters of string code (2 + 5 + 10 + 6 = 23) minus the total number of characters in memory for string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
    
    Note: All backslashes occurrences have been replaced with |
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.get_result(self.input)

    def get_result(self, input: list[str] = None) -> int:
        characters_of_code = 0
        characters_of_memory = 0
        for string in input:
            characters_of_code += len(string)
            characters_of_memory += self.get_characters_of_memory(string)
        return characters_of_code - characters_of_memory
    
    def get_characters_of_memory(self, string: str) -> int:
        characters_of_memory = 0
        string = string[1:-1]
        index = 0
        while index < len(string):
            character = string[index]
            if character == "\\":
                if string[index+1] in ["\\", "\""]:
                    index += 1
                elif string[index+1] == "x":
                    index += 3
            index += 1
            characters_of_memory += 1
        return characters_of_memory



class Part2:
    """
    --- Part Two ---
    Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a). What new signal is ultimately provided to wire a?
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 2 of the puzzle.

        Returns:
            int: Solution of part 2
        """
        return self.get_a_signal(self.input)
