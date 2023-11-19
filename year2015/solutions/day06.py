class Part1:
    """
    --- Day 6: Probably a Fire Hazard ---
    Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

    Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

    Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.

    To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

    For example:

    turn on 0,0 through 999,999 would turn on (or leave on) every light.
    toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
    turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
    After following the instructions, how many lights are lit?
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.how_many_lit_lights(self.input)

    def how_many_lit_lights(self, input: list[str]) -> int:
        """Count how many lights are lit.

        Args:
            input (list[str]): List of instructions

        Returns:
            int: Number of lit lights
        """
        lights = self.initialize_lights()
        for instruction in input:
            lights = self.execute_instruction(lights, instruction)
        return self.count_lit_lights(lights)

    def initialize_lights(self) -> list[list[bool]]:
        """Initialize lights.

        Returns:
            list[list[bool]]: Lights
        """
        lights = []
        for _ in range(1000):
            lights.append([False] * 1000)
        return lights

    def execute_instruction(
        self, lights: list[list[bool]], instruction: str
    ) -> list[list[bool]]:
        """Execute an instruction.

        Args:
            lights (list[list[bool]]): Lights
            instruction (str): Instruction

        Returns:
            list[list[bool]]: Lights
        """
        instruction = instruction.split(" ")
        if instruction[0] == "toggle":
            return self.toggle_lights(lights, instruction)
        elif instruction[0] == "turn":
            return self.turn_lights(lights, instruction)
        else:
            raise ValueError(f"Unknown instruction: {instruction}")

    def toggle_lights(
        self, lights: list[list[bool]], instruction: list[str]
    ) -> list[list[bool]]:
        """Toggle lights.

        Args:
            lights (list[list[bool]]): Lights
            instruction (list[str]): Instruction

        Returns:
            list[list[bool]]: Lights
        """
        start = instruction[1].split(",")
        end = instruction[3].split(",")
        for i in range(int(start[0]), int(end[0]) + 1):
            for j in range(int(start[1]), int(end[1]) + 1):
                lights[i][j] = not lights[i][j]
        return lights

    def turn_lights(
        self, lights: list[list[bool]], instruction: list[str]
    ) -> list[list[bool]]:
        """Turn lights on or off.

        Args:
            lights (list[list[bool]]): Lights
            instruction (list[str]): Instruction

        Returns:
            list[list[bool]]: Lights
        """
        start = instruction[2].split(",")
        end = instruction[4].split(",")
        for i in range(int(start[0]), int(end[0]) + 1):
            for j in range(int(start[1]), int(end[1]) + 1):
                if instruction[1] == "on":
                    lights[i][j] = True
                elif instruction[1] == "off":
                    lights[i][j] = False
                else:
                    raise ValueError(f"Unknown instruction: {instruction}")
        return lights

    def count_lit_lights(self, lights: list[list[bool]]) -> int:
        """Count how many lights are lit.

        Args:
            lights (list[list[bool]]): Lights

        Returns:
            int: Number of lit lights
        """
        lit_lights = 0
        for i in range(len(lights)):
            for j in range(len(lights[i])):
                if lights[i][j]:
                    lit_lights += 1
        return lit_lights


class Part2:
    """
    --- Part Two ---
    You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

    The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

    The phrase turn on actually means that you should increase the brightness of those lights by 1.

    The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

    The phrase toggle actually means that you should increase the brightness of those lights by 2.

    What is the total brightness of all lights combined after following Santa's instructions?

    For example:

    turn on 0,0 through 0,0 would increase the total brightness by 1.
    toggle 0,0 through 999,999 would increase the total brightness by 2000000.
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 2 of the puzzle.

        Returns:
            int: Solution of part 2
        """
        return self.calculate_total_brightness(self.input)

    def calculate_total_brightness(self, input: list[str]) -> int:
        """Calculate total brightness.

        Args:
            input (list[str]): List of instructions

        Returns:
            int: Total brightness
        """
        lights = self.initialize_lights()
        for instruction in input:
            lights = self.execute_instruction(lights, instruction)
        return self.calculate_total_brightness_of_lights(lights)

    def initialize_lights(self) -> list[list[int]]:
        """Initialize lights.

        Returns:
            list[list[int]]: Lights
        """
        lights = []
        for _ in range(1000):
            lights.append([0] * 1000)
        return lights

    def execute_instruction(
        self, lights: list[list[int]], instruction: str
    ) -> list[list[int]]:
        """Execute an instruction.

        Args:
            lights (list[list[int]]): Lights
            instruction (str): Instruction

        Returns:
            list[list[int]]: Lights
        """
        instruction = instruction.split(" ")
        if instruction[0] == "toggle":
            return self.toggle_lights(lights, instruction)
        elif instruction[0] == "turn":
            return self.turn_lights(lights, instruction)
        else:
            raise ValueError(f"Unknown instruction: {instruction}")

    def toggle_lights(
        self, lights: list[list[int]], instruction: str
    ) -> list[list[int]]:
        """Toggle lights.

        Args:
            lights (list[list[int]]): Lights
            instruction (str): Instruction

        Returns:
            list[list[int]]: Lights
        """
        start = instruction[1].split(",")
        end = instruction[3].split(",")
        for i in range(int(start[0]), int(end[0]) + 1):
            for j in range(int(start[1]), int(end[1]) + 1):
                lights[i][j] += 2
        return lights

    def turn_lights(self, lights: list[list[int]], instruction: str) -> list[list[int]]:
        """Turn lights on or off.

        Args:
            lights (list[list[int]]): Lights
            instruction (str): Instruction

        Returns:
            list[list[int]]: Lights
        """
        start = instruction[2].split(",")
        end = instruction[4].split(",")
        for i in range(int(start[0]), int(end[0]) + 1):
            for j in range(int(start[1]), int(end[1]) + 1):
                if instruction[1] == "on":
                    lights[i][j] += 1
                elif instruction[1] == "off":
                    if lights[i][j] > 0:
                        lights[i][j] -= 1
                else:
                    raise ValueError(f"Unknown instruction: {instruction}")
        return lights

    def calculate_total_brightness_of_lights(self, lights: list[list[int]]) -> int:
        """Calculate total brightness of lights.

        Args:
            lights (list[list[int]]): Lights

        Returns:
            int: Total brightness of lights
        """
        total_brightness = 0
        for i in range(len(lights)):
            for j in range(len(lights[i])):
                total_brightness += lights[i][j]
        return total_brightness
