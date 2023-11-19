class Part1:
    """
    --- Day 3: Perfectly Spherical Houses in a Vacuum ---
    Santa is delivering presents to an infinite two-dimensional grid of houses.

    He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

    However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

    For example:

    > delivers presents to 2 houses: one at the starting location, and one to the east.
    ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
    ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.how_many_houses(self.input[0])

    def how_many_houses(self, input: list[str]) -> int:
        """Calculate how many houses receive at least one present.

        Args:
            input (str): Directions

        Returns:
            int: Houses that receive at least one present
        """
        houses = set()
        x, y = 0, 0
        houses.add((x, y))
        for direction in input:
            if direction == "^":
                y += 1
            elif direction == "v":
                y -= 1
            elif direction == ">":
                x += 1
            elif direction == "<":
                x -= 1
            houses.add((x, y))
        return len(houses)


class Part2:
    """
    --- Part Two ---
    The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

    Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

    This year, how many houses receive at least one present?

    For example:

    ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
    ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
    ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 2 of the puzzle.

        Returns:
            int: Solution of part 2
        """
        return self.how_many_houses(self.input[0])

    def how_many_houses(self, input: str) -> int:
        """Calculate how many houses receive at least one present.

        Args:
            input (str): Directions

        Returns:
            int: Houses that receive at least one present
        """
        houses = set()
        santa_x, santa_y = 0, 0
        robo_x, robo_y = 0, 0
        houses.add((santa_x, santa_y))
        houses.add((robo_x, robo_y))
        for index, direction in enumerate(input):
            if index % 2 == 0:
                if direction == "^":
                    santa_y += 1
                elif direction == "v":
                    santa_y -= 1
                elif direction == ">":
                    santa_x += 1
                elif direction == "<":
                    santa_x -= 1
                houses.add((santa_x, santa_y))
            else:
                if direction == "^":
                    robo_y += 1
                elif direction == "v":
                    robo_y -= 1
                elif direction == ">":
                    robo_x += 1
                elif direction == "<":
                    robo_x -= 1
                houses.add((robo_x, robo_y))
        return len(houses)    
