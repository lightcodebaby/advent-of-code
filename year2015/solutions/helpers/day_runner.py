import importlib

from solutions.helpers import file_utils


class DayRunner:
    """Runs the solutions for a given day."""

    def __init__(self, day: int):
        self.day = str(day).zfill(2)
        self.input = file_utils.read_file_lines(day=self.day)

    def run(self):
        """Runs the solutions for a given day."""
        module_name = f"solutions.day{self.day}"
        module = importlib.import_module(module_name)
        part1_result = module.Part1(input=self.input).solve()
        # part2_result = module.Part2(input=self.input).solve()
        print(f"Part 1: {part1_result}")
        # print(f"Part 2: {part2_result}")
