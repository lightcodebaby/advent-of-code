class Part1:
    """
    --- Day 5: Doesn't He Have Intern-Elves For This? ---
    Santa needs help figuring out which strings in his text file are naughty or nice.

    A nice string is one with all of the following properties:

    It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
    It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
    It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
    For example:

    ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...), and none of the disallowed substrings.
    aaa is nice because it has at least three vowels and a double letter, even though the letters used by different rules overlap.
    jchzalrnumimnmhp is naughty because it has no double letter.
    haegwjzuvuyypxyu is naughty because it contains the string xy.
    dvszwmarrgswjxmb is naughty because it contains only one vowel.
    How many strings are nice?
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.how_many_nice_strings(self.input)

    def how_many_nice_strings(self, input: list[str]) -> int:
        """Count how many strings are nice.

        Args:
            input (list[str]): List of strings

        Returns:
            int: Number of nice strings
        """
        nice_strings = 0
        for string in input:
            if self.is_nice_string(string):
                nice_strings += 1
        return nice_strings

    def is_nice_string(self, string: str) -> bool:
        """Check if a string is nice.

        Args:
            string (str): String to check

        Returns:
            bool: True if string is nice, False otherwise
        """
        return (
            self.has_at_least_three_vowels(string)
            and self.has_at_least_one_letter_twice_in_a_row(string)
            and self.does_not_contain_disallowed_substrings(string)
        )

    def has_at_least_three_vowels(self, string: str) -> bool:
        """Check if a string has at least three vowels.

        Args:
            string (str): String to check

        Returns:
            bool: True if string has at least three vowels, False otherwise
        """
        vowels = ["a", "e", "i", "o", "u"]
        vowel_count = 0
        for vowel in vowels:
            vowel_count += string.count(vowel)
        return vowel_count >= 3

    def has_at_least_one_letter_twice_in_a_row(self, string: str) -> bool:
        """Check if a string has at least one letter that appears twice in a row.

        Args:
            string (str): String to check

        Returns:
            bool: True if string has at least one letter that appears twice in a row, False otherwise
        """
        for i in range(len(string) - 1):
            if string[i] == string[i + 1]:
                return True
        return False

    def does_not_contain_disallowed_substrings(self, string: str) -> bool:
        """Check if a string does not contain the strings ab, cd, pq, or xy.

        Args:
            string (str): String to check

        Returns:
            bool: True if string does not contain the strings ab, cd, pq, or xy, False otherwise
        """
        disallowed_substrings = ["ab", "cd", "pq", "xy"]
        for substring in disallowed_substrings:
            if substring in string:
                return False
        return True


class Part2:
    """
    --- Part Two ---
    Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.

    Now, a nice string is one with all of the following properties:

    It contains a pair of any two letters that appears at least twice in the string without overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
    It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe), or even aaa.
    For example:

    qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one letter between them (zxz).
    xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though the letters used by each rule overlap.
    uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
    ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
    How many strings are nice under these new rules?
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 2 of the puzzle.

        Returns:
            int: Solution of part 2
        """
        return self.how_many_nice_strings(self.input)

    def how_many_nice_strings(self, input: str) -> int:
        """Count how many strings are nice.

        Args:
            input (str): List of strings

        Returns:
            int: Number of nice strings
        """
        nice_strings = 0
        for string in input:
            if self.is_nice_string(string):
                nice_strings += 1
        return nice_strings
    
    def is_nice_string(self, string: str) -> bool:
        """Check if a string is nice.

        Args:
            string (str): String to check

        Returns:
            bool: True if string is nice, False otherwise
        """
        return (
            self.has_pair_of_any_two_letters_that_appears_twice(string)
            and self.has_at_least_one_letter_which_repeats_with_exactly_one_letter_between(string)
        )
        
    def has_pair_of_any_two_letters_that_appears_twice(self, string: str) -> bool:
        """Check if a string has a pair of any two letters that appears at least twice in the string without overlapping.

        Args:
            string (str): String to check

        Returns:
            bool: True if string has a pair of any two letters that appears at least twice in the string without overlapping, False otherwise
        """
        for i in range(len(string) - 1):
            pair = string[i:i+2]
            if string.count(pair) >= 2:
                return True
        return False
    
    def has_at_least_one_letter_which_repeats_with_exactly_one_letter_between(self, string: str) -> bool:
        """Check if a string has at least one letter which repeats with exactly one letter between them.

        Args:
            string (str): String to check

        Returns:
            bool: True if string has at least one letter which repeats with exactly one letter between them, False otherwise
        """
        for i in range(len(string) - 2):
            if string[i] == string[i+2]:
                return True
        return False
