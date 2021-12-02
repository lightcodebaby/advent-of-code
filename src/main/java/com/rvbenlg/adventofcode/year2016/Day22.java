package com.rvbenlg.adventofcode.year2016;

import com.rvbenlg.adventofcode.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day22 {

    /*
    --- Day 22: Grid Computing ---
    You gain access to a massive storage cluster arranged in a grid; each storage node is only connected to the four nodes directly adjacent to it (three if the node is on an edge, two if it's in a corner).

    You can directly access data only on node /dev/grid/node-x0-y0, but you can perform some limited actions on the other nodes:

    You can get the disk usage of all nodes (via df). The result of doing this is in your puzzle input.
    You can instruct a node to move (not copy) all of its data to an adjacent node (if the destination node has enough space to receive the data). The sending node is left empty after this operation.
    Nodes are named by their position: the node named node-x10-y10 is adjacent to nodes node-x9-y10, node-x11-y10, node-x10-y9, and node-x10-y11.

    Before you begin, you need to understand the arrangement of data on these nodes. Even though you can only move data between directly connected nodes, you're going to need to rearrange a lot of the data to get access to the data you need. Therefore, you need to work out how you might be able to shift data around.

    To do this, you'd like to count the number of viable pairs of nodes. A viable pair is any two nodes (A,B), regardless of whether they are directly connected, such that:

    Node A is not empty (its Used is not zero).
    Nodes A and B are not the same node.
    The data on node A (its Used) would fit on node B (its Avail).
    How many viable pairs of nodes are there?

    --- Part Two ---
    Now that you have a better understanding of the grid, it's time to get to work.

    Your goal is to gain access to the data which begins in the node with y=0 and the highest x (that is, the node in the top-right corner).

    For example, suppose you have the following grid:

    Filesystem            Size  Used  Avail  Use%
    /dev/grid/node-x0-y0   10T    8T     2T   80%
    /dev/grid/node-x0-y1   11T    6T     5T   54%
    /dev/grid/node-x0-y2   32T   28T     4T   87%
    /dev/grid/node-x1-y0    9T    7T     2T   77%
    /dev/grid/node-x1-y1    8T    0T     8T    0%
    /dev/grid/node-x1-y2   11T    7T     4T   63%
    /dev/grid/node-x2-y0   10T    6T     4T   60%
    /dev/grid/node-x2-y1    9T    8T     1T   88%
    /dev/grid/node-x2-y2    9T    6T     3T   66%
    In this example, you have a storage grid 3 nodes wide and 3 nodes tall. The node you can access directly, node-x0-y0, is almost full. The node containing the data you want to access, node-x2-y0 (because it has y=0 and the highest x value), contains 6 terabytes of data - enough to fit on your node, if only you could make enough space to move it there.

    Fortunately, node-x1-y1 looks like it has enough free space to enable you to move some of this data around. In fact, it seems like all of the nodes have enough space to hold any node's data (except node-x0-y2, which is much larger, very full, and not moving any time soon). So, initially, the grid's capacities and connections look like this:

    ( 8T/10T) --  7T/ 9T -- [ 6T/10T]
        |           |           |
      6T/11T  --  0T/ 8T --   8T/ 9T
        |           |           |
     28T/32T  --  7T/11T --   6T/ 9T
    The node you can access directly is in parentheses; the data you want starts in the node marked by square brackets.

    In this example, most of the nodes are interchangable: they're full enough that no other node's data would fit, but small enough that their data could be moved around. Let's draw these nodes as .. The exceptions are the empty node, which we'll draw as _, and the very large, very full node, which we'll draw as #. Let's also draw the goal data as G. Then, it looks like this:

    (.) .  G
     .  _  .
     #  .  .
    The goal is to move the data in the top right, G, to the node in parentheses. To do this, we can issue some commands to the grid and rearrange the data:

    Move data from node-y0-x1 to node-y1-x1, leaving node node-y0-x1 empty:

    (.) _  G
     .  .  .
     #  .  .
    Move the goal data from node-y0-x2 to node-y0-x1:

    (.) G  _
     .  .  .
     #  .  .
    At this point, we're quite close. However, we have no deletion command, so we have to move some more data around. So, next, we move the data from node-y1-x2 to node-y0-x2:

    (.) G  .
     .  .  _
     #  .  .
    Move the data from node-y1-x1 to node-y1-x2:

    (.) G  .
     .  _  .
     #  .  .
    Move the data from node-y1-x0 to node-y1-x1:

    (.) G  .
     _  .  .
     #  .  .
    Next, we can free up space on our node by moving the data from node-y0-x0 to node-y1-x0:

    (_) G  .
     .  .  .
     #  .  .
    Finally, we can access the goal data by moving the it from node-y0-x1 to node-y0-x0:

    (G) _  .
     .  .  .
     #  .  .
    So, after 7 steps, we've accessed the data we want. Unfortunately, each of these moves takes time, and we need to be efficient:

    What is the fewest number of steps required to move your goal data to node-x0-y0?
     */

    private List<Node> nodes = new ArrayList<>();
    private List<Node> path = new ArrayList<>();
    private int steps = 0;

    public void solve() throws IOException {
        part1();
        part2();
    }

    private void part1() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day22.txt");
        for (int i = 2; i < input.size(); i++) {
            nodes.add(parseNode(input.get(i)));
        }
        int result = getViablePairs();
        System.out.println("Part 1 solution: " + result);
    }

    private void part2() throws IOException {
        resetVariables();
        List<String> input = Utilities.readInput("year2016/day22.txt");
        for (int i = 2; i < input.size(); i++) {
            nodes.add(parseNode(input.get(i)));
        }
        Node goal = nodes.stream().filter(node -> node.y == 0 && nodes.stream().noneMatch(node1 -> node1.x > node.x)).findFirst().get();
        Node emptyNode = nodes.stream().filter(node -> node.used == 0).findFirst().get();
        while (goal.x != 0 || goal.y != 0) {
            move(emptyNode, goal);
            emptyNode = goal;
            goal = updateGoal(goal);
            nodes.forEach(node -> node.checked = false);
            path = new ArrayList<>();
            steps++;
        }
        System.out.println("Part 2 solution: " + steps);
    }

    private Node updateGoal(Node goal) {
        Node auxGoal = goal;
        goal = nodes.stream().filter(node -> auxGoal.x - 1 == node.x && auxGoal.y == node.y).findFirst().get();
        return goal;
    }

    private void move(Node origin, Node goal) {
        path.add(origin);
        while (path.stream().noneMatch(node -> node.y == 0 && node.x == goal.x - 1)) {
            List<Node> toCheck = path.stream().filter(node -> !node.checked).collect(Collectors.toList());
            for (int i = 0; i < toCheck.size(); i++) {
                Node from = toCheck.get(i);
                if (canGoUp(from)) {
                    Node to = nodes.stream().filter(node -> node.x == from.x && node.y == from.y - 1).findFirst().get();
                    if (to.x != goal.x || to.y != goal.y) {
                        path.add(to);
                    }
                }
                if (canGoDown(from)) {
                    Node to = nodes.stream().filter(node -> node.x == from.x && node.y == from.y + 1).findFirst().get();
                    if (to.x != goal.x || to.y != goal.y) {
                        path.add(to);
                    }
                }
                if (canGoRight(from)) {
                    Node to = nodes.stream().filter(node -> node.x == from.x + 1 && node.y == from.y).findFirst().get();
                    if (to.x != goal.x || to.y != goal.y) {
                        path.add(to);
                    }
                }
                if (canGoLeft(from)) {
                    Node to = nodes.stream().filter(node -> node.x == from.x - 1 && node.y == from.y).findFirst().get();
                    if (to.x != goal.x || to.y != goal.y) {
                        path.add(to);
                    }
                }
                from.checked = true;
            }
            steps++;
        }
    }

    private boolean canGoUp(Node from) {
        boolean result = false;
        Optional<Node> optionalTo = nodes.stream().filter(node -> node.x == from.x && node.y == from.y - 1 && path.stream().noneMatch(node1 -> node1.x == node.x && node1.y == node.y)).findFirst();
        if (optionalTo.isPresent()) {
            Node to = optionalTo.get();
            if (to.size >= from.used) {
                result = true;
            }
        }
        return result;
    }

    private boolean canGoDown(Node from) {
        boolean result = false;
        Optional<Node> optionalTo = nodes.stream().filter(node -> node.x == from.x && node.y == from.y + 1 && path.stream().noneMatch(node1 -> node1.x == node.x && node1.y == node.y)).findFirst();
        if (optionalTo.isPresent()) {
            Node to = optionalTo.get();
            if (to.size >= from.used) {
                result = true;
            }
        }
        return result;
    }

    private boolean canGoRight(Node from) {
        boolean result = false;
        Optional<Node> optionalTo = nodes.stream().filter(node -> node.x == from.x + 1 && node.y == from.y && path.stream().noneMatch(node1 -> node1.x == node.x && node1.y == node.y)).findFirst();
        if (optionalTo.isPresent()) {
            Node to = optionalTo.get();
            if (to.size >= from.used) {
                result = true;
            }
        }
        return result;
    }

    private boolean canGoLeft(Node from) {
        boolean result = false;
        Optional<Node> optionalTo = nodes.stream().filter(node -> node.x == from.x - 1 && node.y == from.y && path.stream().noneMatch(node1 -> node1.x == node.x && node1.y == node.y)).findFirst();
        if (optionalTo.isPresent()) {
            Node to = optionalTo.get();
            if (to.size >= from.used) {
                result = true;
            }
        }
        return result;
    }

    private int getViablePairs() {
        int result = 0;
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (areViablePair(i, j)) {
                    result++;
                }
            }
        }
        return result;
    }

    private boolean areViablePair(int i, int j) {
        return (i != j && nodes.get(i).used != 0 && nodes.get(i).used <= nodes.get(j).avail);
    }

    private Node parseNode(String string) {
        while (string.contains("  ")) {
            string = string.replaceAll("  ", " ");
        }
        String[] parts = string.split(" ");
        String coordinates = parts[0].replaceAll("/dev/grid/node-", "");
        int x = Integer.parseInt(coordinates.split("-")[0].substring(1));
        int y = Integer.parseInt(coordinates.split("-")[1].substring(1));
        int size = Integer.parseInt(parts[1].replaceAll("T", ""));
        int used = Integer.parseInt(parts[2].replaceAll("T", ""));
        int avail = Integer.parseInt(parts[3].replaceAll("T", ""));
        return new Node(x, y, size, used, avail);
    }

    private void resetVariables() {
        nodes = new ArrayList<>();
    }

    private class Node {
        private int x;
        private int y;
        private int size;
        private int used;
        private int avail;
        private boolean checked;

        private Node(int x, int y, int size, int used, int avail) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.used = used;
            this.avail = avail;
            this.checked = false;
        }
    }

}
