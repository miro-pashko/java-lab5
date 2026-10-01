package view;

import model.Shape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Everything the user sees or types goes through this class. The
 * controller never calls System.out/Scanner directly - it only talks to
 * the view.
 */
public class ShapeView {

    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        System.out.println("=========================================");
        System.out.println(" SHAPE DATASET PROCESSOR (MVC demo)");
        System.out.println("=========================================");
        System.out.println(" 1 - Display the dataset");
        System.out.println(" 2 - Total area of all shapes");
        System.out.println(" 3 - Total area of shapes of a specified type");
        System.out.println(" 4 - Sort dataset by increasing area");
        System.out.println(" 5 - Sort dataset by color");
        System.out.println(" 6 - Save dataset to file");
        System.out.println(" 7 - Load dataset from file");
        System.out.println(" 0 - Exit");
        System.out.print("Your choice: ");
    }

    public String readMenuChoice() {
        return scanner.nextLine().trim();
    }

    public void displayShapes(Shape[] shapes) {
        System.out.println("\n--- Dataset (" + shapes.length + " shapes) ---");
        for (int i = 0; i < shapes.length; i++) {
            System.out.printf("%2d. %s%n", i + 1, shapes[i]);
        }
        System.out.println();
    }

    public void displayTotalArea(double total) {
        System.out.printf("Total area of all shapes: %.2f%n%n", total);
    }

    public void displayTotalAreaByType(String typeName, double total, int matchCount) {
        System.out.printf("Total area of %d %s shape(s): %.2f%n%n", matchCount, typeName, total);
    }

    /** Returns 1 = Rectangle, 2 = Triangle, 3 = Circle. */
    public int promptShapeTypeChoice() {
        while (true) {
            System.out.print("Select shape type - 1: Rectangle, 2: Triangle, 3: Circle: ");
            String input = scanner.nextLine().trim();
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return Integer.parseInt(input);
            }
            System.out.println("  Error: enter 1, 2 or 3.");
        }
    }

    public void displaySortResult(String criterion) {
        System.out.println("Dataset sorted by " + criterion + ":");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }

    /** Prompts for a path to write to. No existence check - the file may not exist yet. */
    public Path promptOutputFilePath(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Paths.get(input);
            } catch (InvalidPathException e) {
                System.out.println("  Error: " + e.getMessage() + " Please try again.");
            }
        }
    }

    /** Prompts for a path to an existing, readable file; re-prompts until one is given. */
    public Path promptExistingFilePath(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                Path path = Paths.get(input);
                if (!Files.exists(path)) {
                    throw new IOException("no such file: " + input);
                }
                if (!Files.isRegularFile(path)) {
                    throw new IOException(input + " is not a regular file.");
                }
                return path;
            } catch (InvalidPathException | IOException e) {
                System.out.println("  Error: " + e.getMessage() + " Please try again.");
            }
        }
    }
}
