package models;

import data.GameConstants;
import java.util.*;

public class Deck {

    // Stores the full set of UNO cards(reference template)
    private List<Card> cards;

    // Represents the actual playable draw pile during the game
    private Queue<Card> drawPile;

    public Deck() {
        cards = new ArrayList<>();
        createDeck();
        resetDrawPile();
    }

    /**
     * Creates a standard UNO deck: - 1 zero card per color - 2 of each number
     * (1–9) per color - 2 of each action card (Skip, Reverse, Draw2) per color
     * - 4 Wild and 4 Wild Draw 4 cards
     */
    public void createDeck() {
        cards.clear(); // prevents duplicate cards if method is called again

        for (String color : GameConstants.COLORS) {
            // Add colored number cards
            for (String value : GameConstants.NUMBER_VALUES) {
                cards.add(new Card(color, value));
                if (!value.equals("0")) {
                    cards.add(new Card(color, value)); // Two of each number except 0
                }
            }

            // Add special cards(Skip, Reverse, Draw2)
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

    private void resetDrawPile() {// Shuffles the cards and initializes the draw pile
        List<Card> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        drawPile = new LinkedList<>(shuffledCards);
    }

    public void shuffleDeck() {// Shuffles the current draw pile
        List<Card> temp = new ArrayList<>(drawPile);
        Collections.shuffle(temp);
        drawPile = new LinkedList<>(temp);
    }

    public Card drawCard() {// Draws a card from the top of the draw pile
        if (drawPile.isEmpty()) {
            System.out.println("Deck is empty!");
            return null;
        }
        return drawPile.poll();// removes and returns the top card
    }

    public void addCardToBottom(Card card) {
        if (card != null) {
            drawPile.offer(card);// Add to bottom of the draw pile
        }
    }

    public void reloadFromDiscard(List<Card> discardedCards) {// Reloads the draw pile using cards from the discard
        // pile.
        if (discardedCards == null || discardedCards.isEmpty()) {
            return;
        }

        Collections.shuffle(discardedCards);// Shuffle before adding
        for (Card c : discardedCards) {
            drawPile.offer(c);// Add to bottom of the draw pile
        }
    }

    public void printTopCards(int n) {// Debug method: prints top n cards without modifying the deck,useful for
        // testing shuffle behavior
        int count = 0;
        for (Card c : drawPile) {
            System.out.println(c);
            count++;
            if (count >= n) {
                break;
            }
        }
    }

    public boolean isEmpty() {// Checks if the draw pile is empty
        return drawPile.isEmpty();
    }

    public int size() {// Returns the number of cards left in the draw pile
        return drawPile.size();
    }

    public static void main(String[] args) {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        System.out.println("Deck 1:");
        for (int i = 0; i < 20; i++) {
            System.out.println(deck1.drawCard());
        }

        System.out.println("\nDeck 2:");
        for (int i = 0; i < 20; i++) {
            System.out.println(deck2.drawCard());
        }
    }
}
