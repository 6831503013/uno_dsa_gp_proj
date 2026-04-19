package utils;

import java.util.ArrayList;
import java.util.List;
import models.Card;

public class DisplayHandler {

    public static void displayPlayerTurn(String playerName) {
        typewrite("It's " + playerName + "'s turn!", 90);
        System.out.println();
        // System.out.println("It's " + playerName + "'s turn!");
    }

    public static void declareWinner(String playerName) {
        String GOLD = "\033[93m";
        String RED = "\033[31m";
        String WHITE = "\033[97m";
        String RESET = "\033[0m";

        String unoArt = """
                ██╗   ██╗███╗   ██╗ ██████╗ ██╗
                ██║   ██║████╗  ██║██╔═══██╗██║
                ██║   ██║██╔██╗ ██║██║   ██║██║
                ██║   ██║██║╚██╗██║██║   ██║╚═╝
                ╚██████╔╝██║ ╚████║╚██████╔╝██╗
                 ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚═╝""";

        // 1. Calculate the center point based on the logo width (approx 44 chars)
        int logoWidth = 44;

        // 2. The Slam: UNO!
        System.out.println(RED);
        for (String line : unoArt.split("\n")) {
            System.out.println(line);
            pause(80);
        }
        pause(400);

        // 3. The Firework Burst (Centered)
        String[] burst = {
                "       .       ",
                "    .  |  .    ",
                "  '  - O -  '  ",
                "    '  |  '    ",
                "       '       "
        };

        System.out.println("\n");
        for (String line : burst) {
            int padding = (logoWidth - line.length()) / 2;
            System.out.println(GOLD + " ".repeat(Math.max(0, padding)) + line);
            pause(100);
        }

        // 4. The Reveal: Winner Box (Centered)
        String winnerBox = " ★ " + playerName.toUpperCase() + " WINS THE GAME! ★ ";
        int boxPadding = (logoWidth - winnerBox.length()) / 2;
        String leftPad = " ".repeat(Math.max(0, boxPadding));

        System.out.print("\n" + WHITE);
        System.out.println(leftPad + "═".repeat(winnerBox.length()));
        System.out.print(leftPad);
        typewrite(winnerBox, 60);
        System.out.println("\n" + leftPad + "═".repeat(winnerBox.length()));

        // 5. Glory Message (Centered)
        String glory = "* GLORY TO THE CHAMPION *";
        int gloryPadding = (logoWidth - glory.length()) / 2;
        String gloryPad = " ".repeat(Math.max(0, gloryPadding));

        for (int i = 0; i < 4; i++) {
            // \r goes to start of line, then we apply calculated padding
            System.out.print("\r" + GOLD + gloryPad + glory);
            pause(400);
            // Subtle flicker effect by clearing and re-printing
            System.out.print("\r" + " ".repeat(logoWidth));
            pause(200);
            System.out.print("\r" + GOLD + gloryPad + glory);
        }

        System.out.println("\n" + RESET);
    }

    private static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void typewrite(String text, int speed) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            pause(speed);
        }
        System.out.println(); // Add a newline at the end
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
        int cardsPerRow = 8; // Change this number to fit your terminal width
        int totalCards = hand.size();

        for (int rowStart = 0; rowStart < totalCards; rowStart += cardsPerRow) {
            int rowEnd = Math.min(rowStart + cardsPerRow, totalCards);
            List<Card> currentRow = hand.subList(rowStart, rowEnd);

            // 1. Render only the cards for this specific row
            List<List<String>> renderedCards = new ArrayList<>();
            for (Card card : currentRow) {
                renderedCards.add(renderCard(card));
            }

            int height = renderedCards.get(0).size();
            String cardGap = "  ";

            // 2. Print the visual cards for this row
            for (int i = 0; i < height; i++) {
                for (List<String> cardLines : renderedCards) {
                    System.out.print(cardLines.get(i) + cardGap);
                }
                System.out.println();
            }

            // 3. Print the indices for this row (Corrected for 2+ digits)
            int cardVisualWidth = 11;
            for (int i = rowStart; i < rowEnd; i++) {
                String index = String.valueOf(i + 1);
                int totalPadding = cardVisualWidth - index.length();
                int leftPadding = totalPadding / 2;
                int rightPadding = totalPadding - leftPadding;

                String indexLine = " ".repeat(leftPadding) + index + " ".repeat(rightPadding);
                System.out.print(indexLine + cardGap);
            }

            // Add extra spacing between rows of cards
            System.out.println("\n");
        }
    }

    public static void displayWinner(String playerName) {

    }

    // Display functions for wild cards and special cards can be added here as
    // needed
    // 1. Updated Skip Card with Player Name
    public static void displaySkipCard(String playerName) {
        String RED = "\033[91m";
        String RESET = "\033[0m";

        String skipArtTemplate = """
                      ███████╗██╗  ██╗██╗██████╗
                      ██╔════╝██║ ██╔╝██║██╔══██╗
                      ███████╗█████╔╝ ██║██████╔╝
                      ╚════██║██╔═██╗ ██║██╔═══╝
                      ███████║██║  ██╗██║██║
                      ╚══════╝╚═╝  ╚═╝╚═╝╚═╝

                            O
                           /

                      TURN LOST
                %s got SKIPPED!
                """;

        String skipArt = skipArtTemplate.formatted(playerName);
        String[] lines = skipArt.split("\n");

        System.out.println();
        for (String line : lines) {
            System.out.println(RED + line + RESET);
            pause(100);
        }
        pause(1000);
    }

    public static void displayReverseCard() {
        String BLUE = "\033[96m"; // bright blue
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
        pause(1000);
    }

    // 2. Updated Draw 2 with Player Name
    public static void displayDraw2Card(String playerName) {
        String YELLOW = "\033[93m";
        String RESET = "\033[0m";

        String draw2ArtTemplate = """
                  ██████╗ ██████╗  █████╗ ██╗    ██╗  ██████╗
                  ██╔══██╗██╔══██╗██╔══██╗██║     ██║ ██╔═══██╗
                  ██║  ██║██████╔╝███████║██║ █╗ ██║     ██╔╝
                  ██║  ██║██╔══██╗██╔══██║██║███╗██║  ██╔╝
                  ██████╔╝██║  ██║██║  ██║╚███╔███╔╝ ███████╗
                  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚══╝╚══╝  ╚══════╝

                          ▌▌   +2   ▌▌
                %s MUST DRAW TWO!
                """;

        String draw2Art = draw2ArtTemplate.formatted(playerName);
        String[] lines = draw2Art.split("\n");

        System.out.println();
        for (String line : lines) {
            System.out.println(YELLOW + line + RESET);
            pause(100);
        }
        pause(1000);
    }

    public static void displayWildCard() {
        String CYAN = "\033[96m";
        String RED = "\033[91m";
        String YELLOW = "\033[93m";
        String BLUE = "\033[96m";
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
        pause(1000);
    }

    // 3. Updated Wild Draw 4 with Player Name
    public static void displayWild4Card(String playerName) {
        String PURPLE = "\033[95m";
        String RED = "\033[91m";
        String YELLOW = "\033[93m";
        String BLUE = "\033[96m";
        String GREEN = "\033[92m";
        String RESET = "\033[0m";

        String wild4ArtTemplate = """
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

                 >>> %s DRAWS FOUR <<<
                """;

        String wild4Art = wild4ArtTemplate.formatted(playerName);
        String[] lines = wild4Art.split("\n");

        System.out.println();
        for (String line : lines) {
            line = RED + line + RESET;
            // (Keep your existing multicolor logic for [R][Y][B][G] here...)
            if (line.contains("[R]")) {
                line = line.replace("[R]", RED + "[R]" + PURPLE)
                        .replace("[Y]", YELLOW + "[Y]" + PURPLE)
                        .replace("[B]", BLUE + "[B]" + PURPLE)
                        .replace("[G]", GREEN + "[G]" + PURPLE);
            }
            System.out.println(line);
            pause(120);
        }
        pause(1000);
    }
}
