package utils;

import java.util.ArrayList;
import java.util.List;
import models.Card;

public class DisplayHandler {

    public static void displayPlayerTurn(String playerName) {
        System.out.println("It's " + playerName + "'s turn!");
    }

    public static void declareWinner(String playerName) {
        System.out.println("Congratulations " + playerName + "! You win!");
    }

    public static void displayCard(int index, Card card) {
        System.out.println(index + ": " + card.getColor() + " " + card.getValue());
    }

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[96m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    // Get ANSI color from card color
    private static String getColorCode(String color) {
        return switch (color.toUpperCase()) {
            case "RED" ->
                RED;
            case "BLUE" ->
                BLUE;
            case "GREEN" ->
                GREEN;
            case "YELLOW" ->
                YELLOW;
            default ->
                RESET;
        };
    }

    // Render SINGLE card
    public static List<String> renderCard(Card card) {
        List<String> lines = new ArrayList<>();
        String colorCode = getColorCode(card.getColor());
        String value = card.getValue();

        int totalWidth = 9; // The inner width of the card

        // 1. Calculate padding for centering
        int totalPadding = totalWidth - value.length();
        int rightPadding = totalPadding / 2;
        int leftPadding = totalPadding - rightPadding;

        // 2. Build the strings
        String border = colorCode + "." + "-".repeat(totalWidth) + "." + RESET;
        String emptyLine = colorCode + "|" + " ".repeat(totalWidth) + "|" + RESET;

        // Create the centered text line
        String centeredValue = " ".repeat(leftPadding) + value + " ".repeat(rightPadding);
        String contentLine = colorCode + "|" + centeredValue + "|" + RESET;

        // 3. Assemble the card
        lines.add(border);
        lines.add(emptyLine);
        lines.add(contentLine);
        lines.add(emptyLine);
        lines.add(border);

        return lines;
    }

    public static void renderTopCard(Card card) {
        System.out.println("Top Card:");
        List<String> cardLines = renderCard(card);
        for (String line : cardLines) {
            System.out.println(line);
        }

    }

    // Display ALL cards (side by side)
    public static void displayHand(List<Card> hand) {
        if (hand == null || hand.isEmpty()) {
            System.out.println("No cards.");
            return;
        }

        // System.out.println("Your hand:");
        List<List<String>> renderedCards = new ArrayList<>();

        for (Card card : hand) {
            renderedCards.add(renderCard(card));
        }

        int height = renderedCards.get(0).size();

        for (int i = 0; i < height; i++) {
            for (List<String> cardLines : renderedCards) {
                System.out.print(cardLines.get(i) + "  ");
            }
            System.out.println();
        }

        // Display card indices (1-based)
        for (int i = 0; i < hand.size(); i++) {
            String index = String.valueOf(i + 1);
            int cardWidth = 11; // width of card with borders
            int totalPadding = cardWidth - index.length();
            int leftPadding = totalPadding / 2;
            int rightPadding = totalPadding - leftPadding;
            String indexLine = " ".repeat(leftPadding) + index + " ".repeat(rightPadding);
            System.out.print(indexLine + "  ");
        }
        System.out.println();
    }

    public static void displayWinner(String playerName) {
        System.out.println();
        System.out.println("Congratulations " + playerName + "! You win!");
    }

    // Display functions for wild cards and special cards can be added here as
    // needed
    public static void displaySkipCard() {
        String RED = "\033[91m"; // bright red
        String RESET = "\033[0m";

        String skipArt = """
                  ███████╗██╗  ██╗██╗██████╗
                  ██╔════╝██║ ██╔╝██║██╔══██╗
                  ███████╗█████╔╝ ██║██████╔╝
                  ╚════██║██╔═██╗ ██║██╔═══╝
                  ███████║██║  ██╗██║██║
                  ╚══════╝╚═╝  ╚═╝╚═╝╚═╝

                        O
                       /

                     TURN LOST
                     You got SKIPPED!
                """;

        String[] lines = skipArt.split("\n");

        System.out.println();
        for (String line : lines) {
            System.out.println(RED + line + RESET);
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void displayReverseCard() {
        String BLUE = "\033[94m"; // bright blue
        String RESET = "\033[0m";

        String reverseArt = """
                  ██████╗ ███████╗██╗   ██╗███████╗██████╗ ███████╗███████╗
                  ██╔══██╗██╔════╝██║   ██║██╔════╝██╔══██╗██╔════╝██╔════╝
                  ██████╔╝█████╗  ██║   ██║█████╗  ██████╔╝███████╗█████╗
                  ██╔══██╗██╔══╝  ╚██╗ ██╔╝██╔══╝  ██╔══██╗╚════██║██╔══╝
                  ██║  ██║███████╗ ╚████╔╝ ███████╗██║  ██║███████║███████╗
                  ╚═╝  ╚═╝╚══════╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝

                        <<<<<<   >>>>>>
                         DIRECTION FLIP
                """;

        String[] lines = reverseArt.split("\\n");

        System.out.println();
        for (String line : lines) {
            System.out.println(BLUE + line + RESET);
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void displayDraw2Card() {
        String YELLOW = "\033[93m"; // bright yellow
        String RESET = "\033[0m";

        String draw2Art = """
                  ██████╗ ██████╗  █████╗ ██╗    ██╗  ██████╗
                  ██╔══██╗██╔══██╗██╔══██╗██║    ██║ ██╔═══██╗
                  ██║  ██║██████╔╝███████║██║ █╗ ██║     ██╔╝
                  ██║  ██║██╔══██╗██╔══██║██║███╗██║  ██╔╝
                  ██████╔╝██║  ██║██║  ██║╚███╔███╔╝ ███████╗
                  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚══╝╚══╝  ╚══════╝

                         ▌▌   +2   ▌▌
                         DRAW  TWO
                """;

        String[] lines = draw2Art.split("\\n");

        System.out.println();
        for (String line : lines) {
            System.out.println(YELLOW + line + RESET);
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void displayWildCard() {
        String CYAN = "\033[96m";
        String RED = "\033[91m";
        String YELLOW = "\033[93m";
        String BLUE = "\033[34m";
        String GREEN = "\033[92m";
        String RESET = "\033[0m";

        String wildArt = """
                  ██╗    ██╗██╗██╗     ██████╗
                  ██║    ██║██║██║     ██╔══██╗
                  ██║ █╗ ██║██║██║     ██║  ██║
                  ██║███╗██║██║██║     ██║  ██║
                  ╚███╔███╔╝██║███████╗██████╔╝
                   ╚══╝╚══╝ ╚═╝╚══════╝╚═════╝

                       [R]  [Y]  [B]  [G]
                         CHOOSE A COLOR
                """;

        String[] lines = wildArt.split("\\n");

        System.out.println();
        for (String line : lines) {

            // Apply cyan to entire line first
            line = CYAN + line + RESET;

            // Then override specific parts with their own colors
            if (line.contains("[R]")) {
                line = line.replace("[R]", RED + "[R]" + CYAN)
                        .replace("[Y]", YELLOW + "[Y]" + CYAN)
                        .replace("[B]", BLUE + "[B]" + CYAN)
                        .replace("[G]", GREEN + "[G]" + CYAN);
            }

            System.out.println(line);

            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void displayWild4Card() {
        String PURPLE = "\033[95m";
        String RED = "\033[91m";
        String YELLOW = "\033[93m";
        String BLUE = "\033[94m";
        String GREEN = "\033[92m";
        String RESET = "\033[0m";

        String wild4Art = """
                  ██╗    ██╗██╗██╗     ██████╗
                  ██║    ██║██║██║     ██╔══██╗
                  ██║ █╗ ██║██║██║     ██║  ██║
                  ██║███╗██║██║██║     ██║  ██║
                  ╚███╔███╔╝██║███████╗██████╔╝
                   ╚══╝╚══╝ ╚═╝╚══════╝╚═════╝

                       [R]  [Y]  [B]  [G]

                            ██╗  ██╗
                            ██║  ██║
                            ███████║
                                 ██║
                                 ██║

                        >>>  +4  <<<
                       DRAW   FOUR
                """;

        String[] lines = wild4Art.split("\\n");

        System.out.println();
        for (String line : lines) {

            // Step 1: apply red base
            line = RED + line + RESET;

            // Step 2: multicolor for [R][Y][B][G]
            if (line.contains("[R]")) {
                line = line.replace("[R]", RED + "[R]" + PURPLE)
                        .replace("[Y]", YELLOW + "[Y]" + PURPLE)
                        .replace("[B]", BLUE + "[B]" + PURPLE)
                        .replace("[G]", GREEN + "[G]" + PURPLE);
            }

            // Step 3: highlight +4 and FOUR in red
            if (line.contains("+4")) {
                line = line.replace("+4", RED + "+4" + RED);
            }

            if (line.contains("FOUR")) {
                line = line.replace("FOUR", RED + "FOUR" + PURPLE);
            }

            System.out.println(line + RESET);

            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
