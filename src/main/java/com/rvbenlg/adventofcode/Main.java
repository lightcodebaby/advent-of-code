package com.rvbenlg.adventofcode;

import java.lang.reflect.Method;

public class Main {

    public static final int YEAR = 2021;
    public static final int DAY = 17;

    public static void main(String[] args) {
        if (args.length != 2) {
            solveProblem(YEAR, DAY);
        } else {
            solveProblem(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }

    }

    public static void solveProblem(int year, int day) {
        System.out.printf("Year: %d\nDay: %d\n", year, day);
        long startAt = System.currentTimeMillis();
        try {
            String className = String.format("com.rvbenlg.adventofcode.year%d.Day%02d", year, day);
            Method method = Class.forName(className).getDeclaredMethod("solve");
            method.invoke(Class.forName(className).getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            System.out.println("Error llamando a la clase o al método");
            e.printStackTrace();
        }
        long endAt = System.currentTimeMillis();
        System.out.println(String.format("Time taken: %d milis", endAt - startAt));
    }

}
