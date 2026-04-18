package models;

import java.util.*;
import utils.DisplayHandler;

public class Player {

    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(int index) {
        hand.remove(index);
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public int getHandSize() {
        return hand.size();
    }

    public void showHand() {
        System.out.println(name + "'s hand:");
        DisplayHandler.displayHand(hand);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Move playTurn(Scanner scanner) {

        System.out.println("Choose a card index to play OR type 'd' to draw:");

        String input = scanner.nextLine();

        // player chooses to draw
        if (input.equalsIgnoreCase("d")) {
            return new Move(Move.Type.DRAW, null);
        }

        // player chooses a card
        try {
            // Convert 1-based index to 0-based
            int index = Integer.parseInt(input) - 1;

            if (index >= 0 && index < hand.size()) {
                Card chosenCard = hand.get(index);
                return new Move(Move.Type.PLAY, chosenCard);
            }
        } catch (NumberFormatException e) {
            // Invalid input, will be handled below
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine();
        }

        // invalid input (input error)
        System.out.println("Invalid input, try again.");
        return playTurn(scanner); // retry input only
    }

    public String chooseColor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(name + ", please choose a color (R, B, G, Y): ");
        String color = "";
        while (color.isEmpty()) {
            if (!scanner.hasNextLine()) {
                return "Red"; // Default color if no input
            }
            color = scanner.nextLine().trim();
            color = switch (color.toLowerCase()) {
                case "r" ->
                    "Red";
                case "b" ->
                    "Blue";
                case "g" ->
                    "Green";
                case "y" ->
                    "Yellow";
                default -> {
                    System.out.println("Invalid color. Please enter R, B, G, or Y:");
                    yield "";
                }
            };
        }
        // scanner.close();
        return color;
    }
}
