package controllers;

import models.Card;
import models.Deck;
import models.Player;
import utils.DisplayHandler;

public class GameRules {

  public static boolean isValidMove(Card playedCard, Card topCard) {

      String playedColor = playedCard.getColor();
      String playedValue = playedCard.getValue();
      String topColor = topCard.getColor();
      String topValue = topCard.getValue();
        // Checks the color, value, and if it's a wild card to determine if the move is valid
        if (topValue.equals("Wild") || topValue.equals("WildDraw4")) {
            // If the top card is a wild card, only the color matters
            return playedColor.equals(topColor) || playedValue.equals("Wild")
                    || playedValue.equals("WildDraw4");
        }
        return playedColor.equals(topColor)
                || playedValue.equals(topValue)
                || playedValue.equals("Wild")
                || playedValue.equals("WildDraw4");
    }

    // Applies the effects of special cards such as Skip, Reverse, Draw2, Wild, and WildDraw4
    public static void applySpecialCard(Card card, GameController game, Deck deck) {

        switch (card.getValue()) {
            case "Skip" -> {
                DisplayHandler.displaySkipCard();
                game.skipNextPlayer();
            }

            case "Reverse" -> {
                DisplayHandler.displayReverseCard();
                game.reverseDirection();
            }

            case "Draw2" -> {
                DisplayHandler.displayDraw2Card();
                Player nextPlayer = game.getNextPlayer();
                // Draw 2 cards and then skip that player's turn
                nextPlayer.addCard(deck.drawCard());
                nextPlayer.addCard(deck.drawCard());
                game.skipNextPlayer();
            }

            case "Wild" -> {
                DisplayHandler.displayWildCard();
                game.changeColor();
            }

            case "WildDraw4" -> {
                DisplayHandler.displayWild4Card();
                game.changeColor();
                Player nextPlayer = game.getNextPlayer();
                System.out.println(nextPlayer.getName() + " must draw 4 cards and skip their turn.");
                for (int i = 0; i < 4; i++) {
                    nextPlayer.addCard(deck.drawCard());
                }
                // Order matters: pick color, then skip the victim
                game.skipNextPlayer();
            }
        }
    }

    public static boolean checkWinner(Player player) {
        return player.getHand().isEmpty();
    }
}
