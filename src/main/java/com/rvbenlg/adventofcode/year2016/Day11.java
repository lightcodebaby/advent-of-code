package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private List<String> checkedStatuses = new ArrayList<>();
    private List<Status> statuses = new ArrayList<>();
    private List<String> elements = new ArrayList<>();

    public void solve() throws IOException {
        part1();
    }

    private void part1() throws IOException {
        input = Utilities.readInput("year2016/day11.txt");
        int result = useElevator();
        System.out.println("Part 1 solution: " + result);
    }

    private int useElevator() {
        Status initStatus = getInitStatus();
        fillElements(initStatus);
        statuses.add(getInitStatus());
        int steps = 0;
        while (statuses.stream().noneMatch(status -> status.floors.stream().anyMatch(floor -> floor.microchips.size() == 5 && floor.generators.size() == 5 && floor.number == 3))) {
            List<Status> toCheck = statuses.stream().filter(status -> !checkedStatuses.contains(status.toHash())).collect(Collectors.toList());
            for (Status status : toCheck) {
                checkOnlyGenerators(status);
                checkOnlyMicrochips(status);
                checkGeneratorsAndMicrochips(status);
                checkedStatuses.add(status.toHash());
            }
            steps++;
        }
        return steps;
    }

    private void checkGeneratorsAndMicrochips(Status currentStatus) {
        for (String generator : currentStatus.floors.get(currentStatus.elevator).generators) {
            List<String> generatorsToBring = new ArrayList<>();
            generatorsToBring.add(generator);
            for (String microchip : currentStatus.floors.get(currentStatus.elevator).microchips) {
                List<String> microchipsToBring = new ArrayList<>();
                microchipsToBring.add(microchip);
                if (canGoUp(currentStatus, generatorsToBring, microchipsToBring)) {
                    Status nextStatus = goUp(currentStatus, generatorsToBring, microchipsToBring);
                    if (!previousStatus(nextStatus)) {
                        statuses.add(nextStatus);
                    }
                }
            }
        }
    }

    private void checkOnlyMicrochips(Status currentStatus) {
        List<String> microchips = currentStatus.floors.get(currentStatus.elevator).microchips;
        for (int i = 0; i < microchips.size(); i++) {
            String microchip = microchips.get(i);
            for (int j = i + 1; j < microchips.size(); j++) {
                String microchip2 = microchips.get(j);
                List<String> microchipsToBring = new ArrayList<>();
                microchipsToBring.add(microchip);
                microchipsToBring.add(microchip2);
                if (canGoUp(currentStatus, new ArrayList<>(), microchipsToBring)) {
                    Status nextStatus = goUp(currentStatus, new ArrayList<>(), microchipsToBring);
                    if (!previousStatus(nextStatus)) {
                        statuses.add(nextStatus);
                    }
                }
            }
            if (canGoUp(currentStatus, new ArrayList<>(), Collections.singletonList(microchip))) {
                Status nextStatus = goUp(currentStatus, new ArrayList<>(), Collections.singletonList(microchip));
                if (!previousStatus(nextStatus)) {
                    statuses.add(nextStatus);
                }
            }
            if (canGoDown(currentStatus, new ArrayList<>(), Collections.singletonList(microchip))) {
                Status nextStatus = goDown(currentStatus, new ArrayList<>(), Collections.singletonList(microchip));
                if (!previousStatus(nextStatus)) {
                    statuses.add(nextStatus);
                }
            }
        }
    }

    private void checkOnlyGenerators(Status currentStatus) {
        List<String> generators = currentStatus.floors.get(currentStatus.elevator).generators;
        for (int i = 0; i < generators.size(); i++) {
            String generator = generators.get(i);
            for (int j = i + 1; j < generators.size(); j++) {
                String generator2 = generators.get(j);
                List<String> generatorsToBring = new ArrayList<>();
                generatorsToBring.add(generator);
                generatorsToBring.add(generator2);
                if (canGoUp(currentStatus, generatorsToBring, new ArrayList<>())) {
                    Status nextStatus = goUp(currentStatus, generatorsToBring, new ArrayList<>());
                    if (!previousStatus(nextStatus)) {
                        statuses.add(nextStatus);
                    }
                }
            }
            if (canGoUp(currentStatus, Collections.singletonList(generator), new ArrayList<>())) {
                Status nextStatus = goUp(currentStatus, Collections.singletonList(generator), new ArrayList<>());
                if (!previousStatus(nextStatus)) {
                    statuses.add(nextStatus);
                }
            }
            if (canGoDown(currentStatus, Collections.singletonList(generator), new ArrayList<>())) {
                Status nextStatus = goDown(currentStatus, Collections.singletonList(generator), new ArrayList<>());
                if (!previousStatus(nextStatus)) {
                    statuses.add(nextStatus);
                }
            }
        }
    }

    private void optimizePairs(Status nextStatus) {
        for(Floor floor : nextStatus.floors) {
            for(String microchip : floor.microchips) {
                if(floor.generators.contains(microchip)) {
                    Status duplicateStatus = duplicateStatus(nextStatus);
                    int generatorIndex = duplicateStatus.floors.get(floor.number).generators.indexOf(microchip);
                    int microchipIndex = duplicateStatus.floors.get(floor.number).microchips.indexOf(microchip);
                    for(String element : elements) {
                        duplicateStatus.floors.get(floor.number).generators.set(generatorIndex, element);
                        duplicateStatus.floors.get(floor.number).microchips.set(floor.microchips.indexOf(microchip), element);
                    }
                }
            }
        }
    }

    private boolean previousStatus(Status nextStatus) {
        boolean previousStatus = false;
        if (statuses.stream().anyMatch(status -> status.toHash().equals(nextStatus.toHash()))) {
            previousStatus = true;
        }
        return previousStatus;
    }

    private boolean canGoUp(Status status, List<String> generators, List<String> microchips) {
        boolean result = true;
        if (status.elevator != 3) {
            Floor origin = status.floors.get(status.elevator);
            Floor destination = status.floors.get(status.elevator + 1);
            if (!isValidOrigin(origin, generators, microchips) || !isValidDestination(destination, generators, microchips)) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean canGoDown(Status status, List<String> generators, List<String> microchips) {
        boolean result = true;
        if (status.elevator != 0) {
            Floor origin = status.floors.get(status.elevator);
            Floor destination = status.floors.get(status.elevator - 1);
            if (!isValidOrigin(origin, generators, microchips) || !isValidDestination(destination, generators, microchips)) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean isValidOrigin(Floor origin, List<String> generators, List<String> microchips) {
        boolean result = true;
        List<String> remainingGenerators = origin.generators.stream().filter(generator -> !generators.contains(generator)).collect(Collectors.toList());
        List<String> remainingMicrochips = origin.microchips.stream().filter(microchip -> !microchips.contains(microchip)).collect(Collectors.toList());
        if (!remainingGenerators.isEmpty() && !remainingMicrochips.isEmpty() && remainingMicrochips.stream().anyMatch(microchip -> !remainingGenerators.contains(microchip))) {
            result = false;
        }
        return result;
    }

    private boolean isValidDestination(Floor destination, List<String> generators, List<String> microchips) {
        boolean result = true;
        for (String generator : generators) {
            if (destination.microchips.stream().anyMatch(microchip -> !microchip.equals(generator) && !destination.generators.contains(microchip))) {
                result = false;
            }
            if (microchips.stream().anyMatch(microchip -> !generators.contains(microchip) && !destination.generators.contains(microchip))) {
                result = false;
            }
        }
        for (String microchip : microchips) {
            if (destination.generators.stream().anyMatch(generator -> !generator.equals(microchip) && !destination.generators.contains(microchip) && !generators.contains(microchip))) {
                result = false;
            }
        }
        return result;
    }

    private Status goUp(Status status, List<String> generators, List<String> microchips) {
        Status nextStatus = duplicateStatus(status);
        nextStatus.elevator += 1;
        moveElements(status, nextStatus, generators, microchips);
        return nextStatus;
    }

    private Status goDown(Status status, List<String> generators, List<String> microchips) {
        Status nextStatus = duplicateStatus(status);
        nextStatus.elevator -= 1;
        moveElements(status, nextStatus, generators, microchips);
        return nextStatus;
    }

    private void moveElements(Status oldStatus, Status newStatus, List<String> generatorsToMove, List<String> microchipsToMove) {
        newStatus.floors.get(oldStatus.elevator).generators.removeAll(generatorsToMove);
        newStatus.floors.get(oldStatus.elevator).microchips.removeAll(microchipsToMove);
        newStatus.floors.get(newStatus.elevator).generators.addAll(generatorsToMove);
        newStatus.floors.get(newStatus.elevator).microchips.addAll(microchipsToMove);
    }

    private Status duplicateStatus(Status oldStatus) {
        Status newStatus = new Status(oldStatus.elevator, new ArrayList<>());
        for (Floor floor : oldStatus.floors) {
            Floor newFloor = new Floor(floor.number);
            newFloor.generators.addAll(floor.generators);
            newFloor.microchips.addAll(floor.microchips);
            newStatus.floors.add(newFloor);
        }
        return newStatus;
    }

    private Floor parseLine(String line, int number) {
        Floor floor = new Floor(number);
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].contains("generator")) {
                floor.generators.add(words[i - 1]);
            } else if (words[i].contains("microchip")) {
                floor.microchips.add(words[i - 1].replaceAll("-compatible", ""));
            }
        }
        return floor;
    }

    private Status getInitStatus() {
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            floors.add(parseLine(line, i));
        }
        return new Status(0, floors);
    }

    private void fillElements(Status status) {
        for (Floor floor : status.floors) {
            for (String generator : floor.generators) {
                if (!generator.isEmpty() && !elements.contains(generator)) {
                    elements.add(generator);
                }
            }
            for (String microchip : floor.microchips) {
                if (!microchip.isEmpty() && !elements.contains(microchip)) {
                    elements.add(microchip);
                }
            }
        }
    }

    private class Status {

        private int elevator;
        private List<Floor> floors;

        private Status(int elevator, List<Floor> floors) {
            this.elevator = elevator;
            this.floors = floors;
        }

        private String toHash() {
            StringBuilder result = new StringBuilder();
            result.append(elevator);
            for (Floor floor : floors) {
                for (String element : elements) {
                    result.append(floor.generators.contains(element) ? 1 : 0);
                    result.append(floor.microchips.contains(element) ? 1 : 0);
                }
            }
            return result.toString();
        }
    }

    private class Floor {

        private int number;
        private List<String> generators;
        private List<String> microchips;

        private Floor(int number) {
            this.number = number;
            this.generators = new ArrayList<>();
            this.microchips = new ArrayList<>();
        }


    }

}
