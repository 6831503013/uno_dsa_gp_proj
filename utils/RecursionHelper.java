package utils;

import controllers.GameRules;
import models.Card;
import models.Deck;
import models.Player;

public class RecursionHelper {

    public static Card drawUntilPlayable(Player player, Deck deck, Card topCard) {
        if (deck.isEmpty()) {
            return null;
        }

        // Draw a card and add it to the player's hand
        Card drawnCard = deck.drawCard();
        player.addCard(drawnCard);

        // Check if the drawn card is playable
        boolean playable = GameRules.isValidMove(drawnCard, topCard);
        System.out.println();

        // Inform the player about the drawn card and whether it's playable
        if (playable) {
            System.out.println(player.getName() + " drew: " + drawnCard + " - playable!");
        } else {
            System.out.println(player.getName() + " drew: " + drawnCard + " - not playable.");
        }

        // ask the player if they want to draw again or play the card
        String choice = InputHandler.getString("Do you want to draw again or play a card? (d/p): ").trim();

        // If the player chooses to draw again, recursively call this method
        if (choice.equalsIgnoreCase("d") || choice.isEmpty()) {
            return drawUntilPlayable(player, deck, topCard);
        }

        return drawnCard;
    }
}
