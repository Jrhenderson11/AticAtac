package com.aticatac;

public class Main {

    private static boolean running;

    public static void main(String[] args) {

        running = true;

        while (running) {

            handleinupt();
            updatestate();
            render();

        }
    }
}
