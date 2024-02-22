package org.jimmydev;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.*;

public final class Gamble {
    private final int toysForRuffleNumber;
    private final int TOYS_DATA_LENGTH = 3;
    private final List<Toy> toysBox = new ArrayList<>();
    private final Queue<Toy> toysQueue = new PriorityQueue<>(Comparator.comparingInt(Toy::getWeight));
    private final File result = new File("result.txt");

    public Gamble(int toysForRuffleNumber) {
        this.toysForRuffleNumber = toysForRuffleNumber;
    }


    public void run() {
        System.out.println("""
                1. Enter a toy data.
                2. Start gamble;
                """);


        var scanner = new Scanner(System.in);


        // Gambling cycle
        while (true) {
            System.out.print("Choose option: ");
            var input = scanner.nextLine();

            if(!input.matches("[1,2]")) {
                System.out.println("Incorrect option!");
                continue;
            }

            switch (Integer.parseInt(input)) {
                case 1 -> {

                    System.out.print("Enter a toy data: ");
                    input = scanner.nextLine();
                    while (!put(input)) {
                        System.out.print("Enter a toy data: ");
                        input = scanner.nextLine();
                    }

                }
                case 2 -> {
                    System.out.println("Gamble starting...");
                    fillToysQueue();
                    while (!toysQueue.isEmpty()) {
                        System.out.println(toysQueue.peek());
                        writeResult();
                        get();
                    }
                    return;
                }
            }
        }

    }

    private boolean put(String input) {
        int toyId;
        String toyName;
        int toyWeight;

        String[] parts = input.split(" ");

        //Data size validation
        if (!validation(parts)) {
            System.out.println("Please enter correct data.");
            return false;
        }

        //Adding a toy
        toyId = Integer.parseInt(parts[0]);
        toyWeight = Integer.parseInt(parts[1]);
        toyName = parts[2];

        toysBox.add(new Toy(toyId, toyName, toyWeight));

        return true;

    }

    private boolean validation(String[] parts) {
        if (parts.length != TOYS_DATA_LENGTH) {
            System.err.println("Incorrect data size!");
            return false;
        }

        //Id validation
        if (!parts[0].matches("\\d+")) {
            System.err.println("Incorrect index type");
            return false;
        }

        if (toysBox.stream().anyMatch(toy -> Integer.parseInt(parts[0]) == (toy.getId()))) {
            System.err.println("Toy with current index already exists!");
            return false;
        }

        //Toy name validation
        if(toysBox.stream().anyMatch(toy -> toy.getName().equals(parts[2]))) {
            System.err.println("Toy with current name already added!");
        }

        return true;
    }

    private void get() {
        if (!toysQueue.isEmpty()) {
            toysQueue.poll();
        }
    }

    private void fillToysQueue() {
        int totalWeight = toysBox.stream().mapToInt(Toy::getWeight).sum();
        toysBox.forEach(toy -> {
            double percentage = (double) toy.getWeight() / totalWeight;
            int toyNumber = (int) (toysForRuffleNumber * percentage);
            for (int i = 0; i < toyNumber; i++) {
                toysQueue.add(toy);
            }
        });
    }

    private void writeResult() {
        try (Writer writer = new FileWriter(result, true)) {
            writer.write(Date.from(Instant.ofEpochSecond(System.currentTimeMillis()))
                    + Objects.requireNonNull(toysQueue.peek()).toString()
                    + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
