import cipher.TextCipherStation;
import exception.UnknownTagException;
import tags.CensusArchive;
import tags.PageTagCounter;
import tags.TagCensus;
import text.DenseLineFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

/**
 * A command-word console menu: type a command name
 * (maxline, encrypt, decrypt, tags, search, help, quit).
 */
public class App {

    private static final String TARGET_PAGE = "https://www.bbc.com";

    private static final TextCipherStation cipherStation = new TextCipherStation();
    private static final PageTagCounter tagCounter = new PageTagCounter();
    private static final CensusArchive archive = new CensusArchive();

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        printBanner();

        var running = true;
        while (running) {
            System.out.print("\n> ");
            var command = scanner.nextLine().trim().toLowerCase();

            running = switch (command) {
                case "maxline" -> {
                    findDensestLine(scanner);
                    yield true;
                }
                case "encrypt" -> {
                    encrypt(scanner);
                    yield true;
                }
                case "decrypt" -> {
                    decrypt(scanner);
                    yield true;
                }
                case "tags" -> {
                    analyzeTags(scanner);
                    yield true;
                }
                case "search" -> {
                    searchCensus(scanner);
                    yield true;
                }
                case "help" -> {
                    printBanner();
                    yield true;
                }
                case "quit", "exit" -> {
                    System.out.println("Goodbye!");
                    yield false;
                }
                default -> {
                    System.out.println("Unknown command. Type 'help' to see available commands.");
                    yield true;
                }
            };
        }
        scanner.close();
    }

    private static void printBanner() {
        System.out.print("""
                =========================================
                 FILE HANDLING LAB
                =========================================
                Available commands:
                  maxline  - find the line with the most words in a file
                  encrypt  - encrypt a file with a shift-cipher key
                  decrypt  - decrypt a file with a shift-cipher key
                  tags     - fetch %s and tally its HTML tags
                  search   - load a saved tag census and search it by tag name
                  help     - show this list again
                  quit     - exit the program
                """.formatted(TARGET_PAGE));
    }

    // -----------------------------------------------------------
    // maxline
    // -----------------------------------------------------------
    private static void findDensestLine(Scanner scanner) {
        Path file = promptExistingFile(scanner, "Path of the file to scan: ");
        try {
            String result = DenseLineFinder.locateDensestLine(file);
            if (result == null) {
                System.out.println("The file is empty.");
            } else {
                System.out.println("Densest line (" + DenseLineFinder.wordsIn(result) + " words): " + result);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------
    // encrypt
    // -----------------------------------------------------------
    private static void encrypt(Scanner scanner) {
        Path input = promptExistingFile(scanner, "Plain-text file to encrypt: ");
        Path output = promptOutputFile(scanner, "Save the encrypted file to: ");
        char key = promptKeyChar(scanner);
        try {
            cipherStation.lock(input, output, key);
            System.out.println("Encrypted file written to " + output);
        } catch (IOException e) {
            System.out.println("Error during encryption: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------
    // decrypt
    // -----------------------------------------------------------
    private static void decrypt(Scanner scanner) {
        Path input = promptExistingFile(scanner, "Encrypted file to decrypt: ");
        Path output = promptOutputFile(scanner, "Save the decrypted file to: ");
        char key = promptKeyChar(scanner);
        try {
            cipherStation.unlock(input, output, key);
            System.out.println("Decrypted file written to " + output);
        } catch (IOException e) {
            System.out.println("Error during decryption: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------
    // tags (+ optional save via object streams)
    // -----------------------------------------------------------
    private static void analyzeTags(Scanner scanner) {
        String html;
        try {
            System.out.println("Fetching " + TARGET_PAGE + " ...");
            html = tagCounter.download(TARGET_PAGE);
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching the page: " + e.getMessage());
            return;
        }

        Map<String, Integer> tally = tagCounter.tally(html);
        if (tally.isEmpty()) {
            System.out.println("No tags found on the page.");
            return;
        }

        System.out.println("\n-- a) Sorted by tag name (lexicographical) --");
        for (var entry : tagCounter.byNameAscending(tally)) {
            System.out.printf("  %-15s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n-- b) Sorted by frequency (ascending) --");
        for (var entry : tagCounter.byFrequencyAscending(tally)) {
            System.out.printf("  %-15s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.print("\nSave this census to a file? (y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            Path destination = promptOutputFile(scanner, "Save the census to: ");
            try {
                var census = new TagCensus(TARGET_PAGE, tally);
                archive.store(census, destination);
                System.out.println("Census saved to " + destination);
            } catch (IOException e) {
                System.out.println("Error saving census: " + e.getMessage());
            }
        }
    }

    // -----------------------------------------------------------
    // search
    // -----------------------------------------------------------
    private static void searchCensus(Scanner scanner) {
        Path source = promptExistingFile(scanner, "Path of a saved census file: ");

        TagCensus census;
        try {
            census = archive.retrieve(source);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading census: " + e.getMessage());
            return;
        }

        System.out.println("Loaded: " + census);
        System.out.println("Enter a tag name to search (blank to return to the command prompt).");

        while (true) {
            System.out.print("Tag name: ");
            var tagName = scanner.nextLine().trim().toLowerCase();
            if (tagName.isEmpty()) {
                return;
            }
            try {
                int frequency = lookUp(census, tagName);
                System.out.println("  <" + tagName + "> occurs " + frequency + " time(s).");
            } catch (UnknownTagException e) {
                System.out.println("  " + e.getMessage());
            }
        }
    }

    private static int lookUp(TagCensus census, String tagName) throws UnknownTagException {
        Integer count = census.getTally().get(tagName);
        if (count == null) {
            throw new UnknownTagException("Tag \"" + tagName + "\" was not found in this census.");
        }
        return count;
    }

    // -----------------------------------------------------------
    // Validated input helpers
    // -----------------------------------------------------------
    private static Path promptExistingFile(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            var input = scanner.nextLine().trim();
            try {
                Path path = Paths.get(input);
                if (!Files.exists(path)) {
                    throw new IOException("no such file: " + input + "\n");
                }
                if (!Files.isRegularFile(path)) {
                    throw new IOException(input + " is not a regular file.");
                }
                return path;
            } catch (InvalidPathException | IOException e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
            }
        }
    }

    private static Path promptOutputFile(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            var input = scanner.nextLine().trim();
            try {
                return Paths.get(input);
            } catch (InvalidPathException e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
            }
        }
    }

    private static char promptKeyChar(Scanner scanner) {
        while (true) {
            System.out.print("Single-character encryption key: ");
            var input = scanner.nextLine();
            try {
                if (input.length() != 1) {
                    throw new IllegalArgumentException("Key must be exactly one character.");
                }
                return input.charAt(0);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
