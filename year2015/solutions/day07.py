class Part1:
    """
    --- Day 7: Some Assembly Required ---
    This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

    Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

    The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.

    For example:

    123 -> x means that the signal 123 is provided to wire x.
    x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
    p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
    NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
    Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.

    For example, here is a simple circuit:

    123 -> x
    456 -> y
    x AND y -> d
    x OR y -> e
    x LSHIFT 2 -> f
    y RSHIFT 2 -> g
    NOT x -> h
    NOT y -> i
    After it is run, these are the signals on the wires:

    d: 72
    e: 507
    f: 492
    g: 114
    h: 65412
    i: 65079
    x: 123
    y: 456
    In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?
    """

    def __init__(self, input: list[str] = None) -> None:
        self.input = input

    def solve(self) -> int:
        """Solver for part 1 of the puzzle.

        Returns:
            int: Solution of part 1
        """
        return self.get_signal_from_wire_a(self.input)

    def get_signal_from_wire_a(self, input: list[str] = None) -> int:
        """Get the signal from wire a.

        Args:
            input (list[str], optional): List of instructions. Defaults to None.

        Returns:
            int: Signal from wire a
        """
        wires = self.initialize_wires(input)
        old_wires = wires.copy()
        while True:
            for instruction in input:
                wires = self.execute_instruction(instruction, wires)
            if wires == old_wires:
                break
            else:
                old_wires = wires.copy()
        return wires["a"]

    def initialize_wires(self, instructions: list[str]) -> dict:
        """Initialize the wires.

        Args:
            instructions (list[str]): List of instructions

        Returns:
            dict: Dictionary of wires
        """
        wires = {}
        for instruction in instructions:
            instruction = instruction.split(" ")
            if instruction[-2] == "->":
                wires[instruction[-1]] = 0
        return wires

    def execute_instruction(self, instruction: str, wires: dict) -> dict:
        """Execute the instruction.

        Args:
            instruction (str): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        instruction = instruction.split(" ")
        if len(instruction) == 3:
            wires = self.get_signal_from_wire(instruction, wires)
        elif len(instruction) == 4:
            wires = self.get_signal_from_not(instruction, wires)
        elif len(instruction) == 5:
            wires = self.get_signal_from_gate(instruction, wires)
        return wires

    def get_signal_from_wire(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from wire.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        wires[instruction[2]] = (
            wires[instruction[0]]
            if not instruction[0].isnumeric()
            else int(instruction[0])
        )
        return wires

    def get_signal_from_gate(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        if instruction[1] == "AND":
            wires = self.get_signal_from_and(instruction, wires)
        elif instruction[1] == "OR":
            wires = self.get_signal_from_or(instruction, wires)
        elif instruction[1] == "LSHIFT":
            wires = self.get_signal_from_lshift(instruction, wires)
        elif instruction[1] == "RSHIFT":
            wires = self.get_signal_from_rshift(instruction, wires)
        return wires

    def get_signal_from_and(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from AND gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] & values[1]
        return wires

    def get_signal_from_or(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from OR gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] | values[1]
        return wires

    def get_signal_from_lshift(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from LSHIFT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] << values[1]
        return wires

    def get_signal_from_rshift(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from RSHIFT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] >> values[1]
        return wires

    def get_signal_from_not(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from NOT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        wires[instruction[3]] = ~wires[instruction[1]]
        return wires

    def get_instruction_values(self, instruction: list[str], wires: dict) -> tuple:
        """Get the values from instruction.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            tuple: Tuple of values
        """
        return (
            wires[instruction[0]]
            if not instruction[0].isnumeric()
            else int(instruction[0]),
            wires[instruction[2]]
            if not instruction[2].isnumeric()
            else int(instruction[2]),
        )


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

    def get_a_signal(self, input: list[str] = None) -> int:
        """Get the signal from wire a.

        Args:
            input (list[str], optional): List of instructions. Defaults to None.

        Returns:
            int: Signal from wire a
        """
        self.override_wire_b(input)
        wires = self.initialize_wires(input)
        old_wires = wires.copy()
        while True:
            for instruction in input:
                wires = self.execute_instruction(instruction, wires)
            if wires == old_wires:
                break
            else:
                old_wires = wires.copy()
        return wires["a"]

    def override_wire_b(self, input: list[str]) -> list[str]:
        """Override wire b with the signal from wire a.

        Args:
            input (list[str]): List of instructions

        Returns:
            list[str]: List of instructions
        """
        new_b_value = Part1(input).solve()
        for instruction in input:
            if instruction.endswith("-> b"):
                input[input.index(instruction)] = f"{new_b_value} -> b"

    def initialize_wires(self, instructions: list[str]) -> dict:
        """Initialize the wires.

        Args:
            instructions (list[str]): List of instructions

        Returns:
            dict: Dictionary of wires
        """
        wires = {}
        for instruction in instructions:
            instruction = instruction.split(" ")
            if instruction[-2] == "->":
                wires[instruction[-1]] = 0
        return wires

    def execute_instruction(self, instruction: str, wires: dict) -> dict:
        """Execute the instruction.

        Args:
            instruction (str): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        instruction = instruction.split(" ")
        if len(instruction) == 3:
            wires = self.get_signal_from_wire(instruction, wires)
        elif len(instruction) == 4:
            wires = self.get_signal_from_not(instruction, wires)
        elif len(instruction) == 5:
            wires = self.get_signal_from_gate(instruction, wires)
        return wires

    def get_signal_from_wire(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from wire.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        wires[instruction[2]] = (
            wires[instruction[0]]
            if not instruction[0].isnumeric()
            else int(instruction[0])
        )
        return wires

    def get_signal_from_gate(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        if instruction[1] == "AND":
            wires = self.get_signal_from_and(instruction, wires)
        elif instruction[1] == "OR":
            wires = self.get_signal_from_or(instruction, wires)
        elif instruction[1] == "LSHIFT":
            wires = self.get_signal_from_lshift(instruction, wires)
        elif instruction[1] == "RSHIFT":
            wires = self.get_signal_from_rshift(instruction, wires)
        return wires

    def get_signal_from_and(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from AND gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] & values[1]
        return wires

    def get_signal_from_or(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from OR gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] | values[1]
        return wires

    def get_signal_from_lshift(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from LSHIFT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] << values[1]
        return wires

    def get_signal_from_rshift(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from RSHIFT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        values = self.get_instruction_values(instruction, wires)
        wires[instruction[4]] = values[0] >> values[1]
        return wires

    def get_signal_from_not(self, instruction: list[str], wires: dict) -> dict:
        """Get the signal from NOT gate.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        wires[instruction[3]] = ~wires[instruction[1]]
        return wires

    def get_instruction_values(self, instruction: list[str], wires: dict) -> dict:
        """Get the values from instruction.

        Args:
            instruction (list[str]): Instruction
            wires (dict): Dictionary of wires

        Returns:
            dict: Dictionary of wires
        """
        return (
            wires[instruction[0]]
            if not instruction[0].isnumeric()
            else int(instruction[0]),
            wires[instruction[2]]
            if not instruction[2].isnumeric()
            else int(instruction[2]),
        )
