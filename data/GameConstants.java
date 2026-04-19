package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameConstants {

    private static final String BASE_FOLDER = "resources";

    private static final Path COLORS_FILE = Paths.get(BASE_FOLDER, "colors.txt");
    private static final Path NUMBER_VALUES_FILE = Paths.get(BASE_FOLDER, "number_values.txt");
    private static final Path SPECIAL_VALUES_FILE = Paths.get(BASE_FOLDER, "special_values.txt");
    private static final Path WILD_VALUES_FILE = Paths.get(BASE_FOLDER, "wild_values.txt");

    public static String[] COLORS;
    public static String[] NUMBER_VALUES;
    public static String[] SPECIAL_VALUES;
    public static String[] WILD_VALUES;

    // Static block runs automatically when the class is first loaded
    static {
        try {
            initializeFiles();
            loadConstants();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize game constants.", e);
        }
    }

    /**
     * Creates the resources folder and constant files if they do not exist.
     * Writes default values into a file only the first time it is created.
     */
    private static void initializeFiles() throws IOException {
        Files.createDirectories(Paths.get(BASE_FOLDER));

        createFileIfMissing(COLORS_FILE, List.of("Red", "Blue", "Green", "Yellow"));
        createFileIfMissing(NUMBER_VALUES_FILE, List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        createFileIfMissing(SPECIAL_VALUES_FILE, List.of("Skip", "Reverse", "Draw2"));
        createFileIfMissing(WILD_VALUES_FILE, List.of("Wild", "WildDraw4"));
    }

    /**
     * Creates a file and writes default values only if the file does not already
     * exist.
     */
    private static void createFileIfMissing(Path filePath, List<String> defaultValues) throws IOException {
        if (!Files.exists(filePath)) {
            Files.write(filePath, defaultValues);
        }
    }

    /**
     * Loads all values from the files into the class variables.
     */
    private static void loadConstants() throws IOException {
        COLORS = readFileToArray(COLORS_FILE);
        NUMBER_VALUES = readFileToArray(NUMBER_VALUES_FILE);
        SPECIAL_VALUES = readFileToArray(SPECIAL_VALUES_FILE);
        WILD_VALUES = readFileToArray(WILD_VALUES_FILE);
    }

    /**
     * Reads all non-empty lines from a file and returns them as a String array.
     */
    private static String[] readFileToArray(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<String> values = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }

        return values.toArray(new String[0]);
    }
}