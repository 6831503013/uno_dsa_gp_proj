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

        Card drawnCard = deck.drawCard();

        player.addCard(drawnCard);

        if (GameRules.isValidMove(drawnCard, topCard)) {
            System.out.println(player.getName() + " drew: " + drawnCard + " - playable!");
            return drawnCard;
        } else {
            System.out.println(player.getName() + " drew: " + drawnCard + " - not playable, drawing again...");
            return drawUntilPlayable(player, deck, topCard);
        }
    }
}
