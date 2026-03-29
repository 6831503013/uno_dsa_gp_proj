package controllers;

import models.Card;
import models.Deck;
import models.Player;

public class GameRules {

    public static boolean isValidMove(Card playedCard, Card topCard, String currentColor) {

        // Checks the color, value, and if it's a wild card to determine if the move is
        // valid
        return playedCard.getColor().equals(currentColor)
                || playedCard.getValue().equals(topCard.getValue())
                || playedCard.getColor().equals("Wild")
                || playedCard.getValue().equals("WildDraw4");
    }

    // Applies the effects of special cards such as Skip, Reverse, Draw2, Wild, and
    // WildDraw4
    public static void applySpecialCard(Card card, GameController game, Deck deck) {

        switch (card.getValue()) {
            case "Skip" -> game.skipNextPlayer();

            case "Reverse" -> game.reverseDirection();

            case "Draw2" -> {
                Player next = game.getNextPlayer();
                // Draw 2 cards and then skip that player's turn
                next.addCard(deck.drawCard());
                next.addCard(deck.drawCard());
                game.skipNextPlayer();
            }

            case "Wild" -> game.changeColor();

            case "WildDraw4" -> {
                Player nextPlayer = game.getNextPlayer();
                for (int i = 0; i < 4; i++) {
                    nextPlayer.addCard(deck.drawCard());
                }
                // Order matters: pick color, then skip the victim
                game.skipNextPlayer();
                game.changeColor();
            }
        }
    }
}
