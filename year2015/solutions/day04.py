import hashlib


class Part1:
    """
    --- Day 4: The Ideal Stocking Stuffer ---
    Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

    To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

    For example:

    If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
    If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.find_lowest_positive_integer(self.input[0])

    def find_lowest_positive_integer(self, input: list[str]) -> int:
        """Find the lowest positive integer that produces a hash with 5 leading zeroes.

        Args:
            input (str): Secret key

        Returns:
            int: Lowest positive integer that produces a hash with 5 leading zeroes
        """
        number = 1
        while True:
            hash = hashlib.md5(f"{input}{number}".encode("utf-8")).hexdigest()
            if hash.startswith("00000"):
                return number
            number += 1


class Part2:
    """
    --- Part Two ---
    Now find one that starts with six zeroes.
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 2 of the puzzle.

        Returns:
            int: Solution of part 2
        """
        return self.find_lowest_positive_integer(self.input[0])

    def find_lowest_positive_integer(self, input: str) -> int:
        """Find the lowest positive integer that produces a hash with 6 leading zeroes.

        Args:
            input (str): Secret key

        Returns:
            int: Lowest positive integer that produces a hash with 6 leading zeroes
        """
        number = 1
        while True:
            hash = hashlib.md5(f"{input}{number}".encode("utf-8")).hexdigest()
            if hash.startswith("000000"):
                return number
            number += 1