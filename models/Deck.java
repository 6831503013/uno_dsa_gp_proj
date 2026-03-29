package models;

import data.GameConstants;
import java.util.*;

public class Deck {

    private List<Card> cards;
    private Queue<Card> drawPile;

    public Deck() {
        cards = new ArrayList<>();
        createDeck();
        // shuffleDeck();
        // drawPile = new LinkedList<>(cards);
    }

    private void createDeck() {
        // TODO: Create UNO cards
        for (String color : GameConstants.COLORS) {
            for (String value : GameConstants.NUMBER_VALUES) {
                cards.add(new Card(color, value));
                if (!value.equals("0")) {
                    cards.add(new Card(color, value)); // Two of each number except 0
                }
            }

            // Add special cards
            for (String value : GameConstants.SPECIAL_VALUES) {
                cards.add(new Card(color, value));
                cards.add(new Card(color, value)); // Two of each special card
            }
        }

        // Add wild cards
        for (String value : GameConstants.WILD_VALUES) {
            cards.add(new Card("Wild", value));
            cards.add(new Card("Wild", value));
            cards.add(new Card("Wild", value));
            cards.add(new Card("Wild", value)); // Wild cards have no color,4 of each wild card
        }
    }

    public void shuffleDeck() {
        // TODO: Shuffle cards
    }

    public Card drawCard() {
        // TODO: Return top card
        return null;
    }

    public void addCard(Card firstCard) {
        // TODO: If WildDraw4 is the first card flipped, it must be put back to the
        // deck: in this case to the bottom of the pile.
    }

    public boolean isEmpty() {
        return drawPile.isEmpty();
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        for (Card card : deck.cards) {
            System.out.println(card);
        }
        System.out.println("Total cards in deck: " + deck.cards.size());
    }
}
