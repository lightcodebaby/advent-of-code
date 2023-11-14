import importlib

from solutions.helpers import file_utils

DAY = "02"

module_name = f"solutions.day{DAY}"
module = importlib.import_module(module_name)

part1_result = module.Part1(input=file_utils.read_file_lines(day=DAY)).solve()
part2_result = module.Part2(input=file_utils.read_file_lines(day=DAY)).solve()

print(f"Part 1: {part1_result}")
print(f"Part 2: {part2_result}")