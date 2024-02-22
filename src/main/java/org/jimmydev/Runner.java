package org.jimmydev;

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        Gamble gambler;


        while (true) {
            System.out.print("Please enter toys for ruffle number: ");
            var input = scanner.nextLine();

            if (input.matches("\\d+")) {
                gambler = new Gamble(Integer.parseInt(input));
                gambler.run();
                return;
            }

            System.out.println("Incorrect input type!");
        }

    }
}
