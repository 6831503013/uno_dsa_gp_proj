package utils;

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

        if (drawnCard.getColor().equals(topCard.getColor())
                || drawnCard.getValue().equals(topCard.getValue())
                || drawnCard.getColor().equals("Wild")) {
            return drawnCard;
        } else {
            return drawUntilPlayable(player, deck, topCard);
        }

    }
}
