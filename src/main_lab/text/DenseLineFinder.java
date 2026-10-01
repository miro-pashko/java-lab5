package text;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Finds the line holding the most whitespace-separated words in a text file.
 */
public final class DenseLineFinder {

    private DenseLineFinder() {
        // utility class
    }

    /** @return the first line with the highest word count, or null if the file has no lines */
    public static String locateDensestLine(Path file) throws IOException {
        String densest = null;
        int topCount = -1;

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                int wordCount = wordsIn(line);
                if (wordCount > topCount) {
                    topCount = wordCount;
                    densest = line;
                }
            }
        }
        return densest;
    }

    public static int wordsIn(String line) {
        var trimmed = line.strip();
        return trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
    }
}
