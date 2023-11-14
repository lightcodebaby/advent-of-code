def read_file_lines(day: str) -> list[str]:
    filename = f"./solutions/inputs/day{day}.txt"
    with open(filename) as f:
        return f.read().splitlines()


def read_file(day: str) -> str:
    filename = f"./solutions/inputs/day{day}.txt"
    with open(filename) as f:
        return f.read()
