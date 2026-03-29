package models;

import java.util.*;

public class Deck {

    private List<Card> cards;
    private Queue<Card> drawPile;

    public Deck() {
        cards = new ArrayList<>();
        createDeck();
        shuffleDeck();
        drawPile = new LinkedList<>(cards);
    }

    private void createDeck() {
        // TODO: Create UNO cards
    }

    private void shuffleDeck() {
        Random rand = new Random();

        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1); 

            
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
    }
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            System.out.println("Deck is empty!");
            return null;
        }
        return drawPile.poll();
    }

    public boolean isEmpty() {
        return drawPile.isEmpty();
    }
}
