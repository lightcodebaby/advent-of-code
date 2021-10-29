package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11 {

    /*
    --- Day 11: Radioisotope Thermoelectric Generators ---
    You come upon a column of four floors that have been entirely sealed off from the rest of the building except for a small dedicated lobby. There are some radiation warnings and a big sign which reads "Radioisotope Testing Facility".

    According to the project status board, this facility is currently being used to experiment with Radioisotope Thermoelectric Generators (RTGs, or simply "generators") that are designed to be paired with specially-constructed microchips. Basically, an RTG is a highly radioactive rock that generates electricity through heat.

    The experimental RTGs have poor radiation containment, so they're dangerously radioactive. The chips are prototypes and don't have normal radiation shielding, but they do have the ability to generate an electromagnetic radiation shield when powered. Unfortunately, they can only be powered by their corresponding RTG. An RTG powering a microchip is still dangerous to other microchips.

    In other words, if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG, the chip will be fried. Therefore, it is assumed that you will follow procedure and keep chips connected to their corresponding RTG when they're in the same room, and away from other RTGs otherwise.

    These microchips sound very interesting and useful to your current activities, and you'd like to try to retrieve them. The fourth floor of the facility has an assembling machine which can make a self-contained, shielded computer for you to take with you - that is, if you can bring it all of the RTGs and microchips.

    Within the radiation-shielded part of the facility (in which it's safe to have these pre-assembly RTGs), there is an elevator that can move between the four floors. Its capacity rating means it can carry at most yourself and two RTGs or microchips in any combination. (They're rigged to some heavy diagnostic equipment - the assembling machine will detach it for you.) As a security measure, the elevator will only function if it contains at least one RTG or microchip. The elevator always stops on each floor to recharge, and this takes long enough that the items within it and the items on that floor can irradiate each other. (You can prevent this if a Microchip and its Generator end up on the same floor in this way, as they can be connected while the elevator is recharging.)

    You make some notes of the locations of each component of interest (your puzzle input). Before you don a hazmat suit and start moving things around, you'd like to have an idea of what you need to do.

    When you enter the containment area, you and the elevator will start on the first floor.

    For example, suppose the isolated area has the following arrangement:

    The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
    The second floor contains a hydrogen generator.
    The third floor contains a lithium generator.
    The fourth floor contains nothing relevant.
    As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium, M for Microchip, and G for Generator), the initial state looks like this:

    F4 .  .  .  .  .
    F3 .  .  .  LG .
    F2 .  HG .  .  .
    F1 E  .  HM .  LM
    Then, to get everything up to the assembling machine on the fourth floor, the following steps could be taken:

    Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it can get power from the Hydrogen Generator:

    F4 .  .  .  .  .
    F3 .  .  .  LG .
    F2 E  HG HM .  .
    F1 .  .  .  .  LM
    Bring both Hydrogen-related items to the third floor, which is safe because the Hydrogen-compatible microchip is getting power from its generator:

    F4 .  .  .  .  .
    F3 E  HG HM LG .
    F2 .  .  .  .  .
    F1 .  .  .  .  LM
    Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible Microchip back down with you so you can still use the elevator:

    F4 .  .  .  .  .
    F3 .  HG .  LG .
    F2 E  .  HM .  .
    F1 .  .  .  .  LM
    At the first floor, grab the Lithium-compatible Microchip, which is safe because Microchips don't affect each other:

    F4 .  .  .  .  .
    F3 .  HG .  LG .
    F2 .  .  .  .  .
    F1 E  .  HM .  LM
    Bring both Microchips up one floor, where there is nothing to fry them:

    F4 .  .  .  .  .
    F3 .  HG .  LG .
    F2 E  .  HM .  LM
    F1 .  .  .  .  .
    Bring both Microchips up again to floor three, where they can be temporarily connected to their corresponding generators while the elevator recharges, preventing either of them from being fried:

    F4 .  .  .  .  .
    F3 E  HG HM LG LM
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    Bring both Microchips to the fourth floor:

    F4 E  .  HM .  LM
    F3 .  HG .  LG .
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    Leave the Lithium-compatible microchip on the fourth floor, but bring the Hydrogen-compatible one so you can still use the elevator; this is safe because although the Lithium Generator is on the destination floor, you can connect Hydrogen-compatible microchip to the Hydrogen Generator there:

    F4 .  .  .  .  LM
    F3 E  HG HM LG .
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    Bring both Generators up to the fourth floor, which is safe because you can connect the Lithium-compatible Microchip to the Lithium Generator upon arrival:

    F4 E  HG .  LG LM
    F3 .  .  HM .  .
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    Bring the Lithium Microchip with you to the third floor so you can use the elevator:

    F4 .  HG .  LG .
    F3 E  .  HM .  LM
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    Bring both Microchips to the fourth floor:

    F4 E  HG HM LG LM
    F3 .  .  .  .  .
    F2 .  .  .  .  .
    F1 .  .  .  .  .
    In this arrangement, it takes 11 steps to collect all of the objects at the fourth floor for assembly. (Each elevator stop counts as one step, even if nothing is added to or removed from it.)

    In your situation, what is the minimum number of steps required to bring all of the objects to the fourth floor?
     */

    private List<String> input = new ArrayList<>();
    private Set<String> checkedStatuses = new HashSet<>();
    private List<Status> statuses = new ArrayList<>();
    private int numberOfElements = 10;
    private String[] elements = {"polonium", "thulium", "promethium", "ruthenium", "cobalt"};
    private int minFloor = 0;

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        input = Utilities.readInput("year2016/day11.txt");
        useElevator();
    }

    private void useElevator() {
        Status initStatus = getInitStatus();
        statuses.add(initStatus);
        boolean ended = false;
        int steps = 0;
        while (!ended) {
            List<Status> unchecked = statuses.stream().filter(status -> !status.checked).collect(Collectors.toList());
            for (Status status : unchecked) {
                if (!status.checked) {
                    ended = checkIfEnded(status);
                    checkMinFloor();
                    move(status);
                    checkedStatuses.add(status.toHash());
                    status.checked = true;
                }
            }
            steps++;
        }
    }

    private void checkMinFloor() {
        if (statuses.stream().anyMatch(status -> status.toHash().substring(1).startsWith("0000000000"))) {
            minFloor = 1;
        }
        if (statuses.stream().anyMatch(status -> status.toHash().substring(1).startsWith("00000000000000000000"))) {
            minFloor = 2;
        }
    }

    private boolean checkIfEnded(Status status) {
        return status.toHash().equals("40000000000000000000000000000001111111111");
    }

    private void move(Status status) {
        Floor floor = status.floors[status.elevator];
        int[] indexes = new int[2];
        for (int i = 0; i < floor.elements.length; i++) {
            boolean movedUp = false;
            boolean movedDown = false;
            if (floor.elements[i] != 0) {
                indexes[0] = i;
                indexes[1] = -1;
                movedDown = moveDown(status, indexes);
                for (int j = i + 1; j < floor.elements.length; j++) {
                    if (floor.elements[j] != 0) {
                        indexes[1] = j;
                        movedUp = moveUp(status, indexes);
                        if (!movedDown) {
                            moveDown(status, indexes);
                        }
                    }
                }
                if (!movedUp) {
                    moveUp(status, indexes);
                }
            }
        }
    }

    private void removeElements(Status status, int[] indexes) {
        for (int index : indexes) {
            if (index != -1) {
                status.floors[status.elevator].elements[index] = 0;
            }
        }
    }

    private boolean moveUp(Status status, int[] indexes) {
        boolean result = false;
        if (status.elevator < 3) {
            Status auxStatus = duplicate(status);
            moveElementsUp(auxStatus, indexes);
            auxStatus.elevator++;
            result = !checkedStatuses.contains(auxStatus.toHash()) && isValid(auxStatus);
            if (result) {
                statuses.add(auxStatus);
            }
        }
        return result;
    }

    private boolean moveDown(Status status, int[] indexes) {
        boolean result = false;
        if (status.elevator > 0) {
            Status auxStatus = duplicate(status);
            moveElementsDown(auxStatus, indexes);
            auxStatus.elevator--;
            result = !checkedStatuses.contains(auxStatus.toHash()) && isValid(auxStatus);
            if (result) {
                statuses.add(auxStatus);
            }
        }
        return result;
    }

    private void moveElementsUp(Status status, int[] indexes) {
        removeElements(status, indexes);
        for (int index : indexes) {
            if (index != -1) {
                status.floors[status.elevator + 1].elements[index] = 1;
            }
        }
    }

    private void moveElementsDown(Status status, int[] indexes) {
        removeElements(status, indexes);
        for (int index : indexes) {
            if (index != -1) {
                status.floors[status.elevator - 1].elements[index] = 1;
            }
        }
    }

    private boolean areEquivalent(Status status1, Status status2) {
        List<Status> equivalent = equivalents(status1);
        return equivalent.stream().map(Status::toHash).collect(Collectors.toList()).contains(status2.toHash());
    }

    private Status duplicate(Status status) {
        int elevator = status.elevator;
        Floor[] floors = new Floor[status.floors.length];
        for (int i = 0; i < floors.length; i++) {
            Floor floor = status.floors[i];
            Floor auxFloor = new Floor(floor.number);
            int[] auxElements = new int[floor.elements.length];
            for (int j = 0; j < floor.elements.length; j++) {
                auxElements[j] = floor.elements[j];
            }
            auxFloor.elements = auxElements;
            floors[i] = auxFloor;
        }
        return new Status(elevator, floors);
    }

    private List<Status> equivalents(Status status) {
        List<Status> statuses = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i += 2) {
            for (int j = i + 2; j < numberOfElements; j += 2) {
                Status auxStatus = duplicate(status);
                Floor[] floors = status.floors;
                for (Floor floor : floors) {
                    int auxValue = floor.elements[i];
                    int auxValue2 = floor.elements[i + 1];
                    floor.elements[i] = floor.elements[j];
                    floor.elements[i + 1] = floor.elements[j + 1];
                    floor.elements[j] = auxValue;
                    floor.elements[j + 1] = auxValue2;
                }
                statuses.add(auxStatus);
            }
        }
        return statuses;
    }

    private boolean isValid(Status status) {
        boolean result = true;
        if(statuses.stream().noneMatch(status1 -> areEquivalent(status1, status))) {
            for (int j = 0; j < status.floors.length; j++) {
                Floor floor = status.floors[j];
                int[] elements = floor.elements;
                boolean generators = false;
                boolean notProtectedMicrochips = false;
                for (int i = 0; i < elements.length; i++) {
                    if (i % 2 == 0 && elements[i] == 1) {
                        generators = true;
                    } else if (i % 2 != 0 && elements[i] == 1 && elements[i - 1] == 0) {
                        notProtectedMicrochips = true;
                    }
                }
                if (generators && notProtectedMicrochips) {
                    result = false;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    private Status getInitStatus() {
        Floor[] floors = new Floor[4];
        for (int i = 0; i < floors.length; i++) {
            floors[i] = parseLine(input.get(i), i);
        }
        return new Status(0, floors);
    }

    private Floor parseLine(String line, int number) {
        Floor floor = new Floor(number);
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].contains("generator")) {
                floor.elements[2 * Arrays.asList(elements).indexOf(words[i - 1])] = 1;
            } else if (words[i].contains("microchip")) {
                floor.elements[(2 * Arrays.asList(elements).indexOf(words[i - 1].replaceAll("-compatible", ""))) + 1] = 1;
            }
        }
        return floor;
    }

    private class Status {

        int elevator;
        Floor[] floors;
        boolean checked;

        private Status(int elevator, Floor[] floors) {
            this.elevator = elevator;
            this.floors = floors;
            this.checked = false;
        }

        private String toHash() {
            StringBuilder result = new StringBuilder();
            result.append(elevator);
            for (Floor floor : floors) {
                for (int element : floor.elements) {
                    result.append(element);
                }
            }
            return result.toString();
        }
    }

    private class Floor {
        int number;
        int[] elements;

        private Floor(int number) {
            this.number = number;
            this.elements = new int[10];
        }
    }


}
