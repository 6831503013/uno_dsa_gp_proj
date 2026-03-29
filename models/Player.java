package models;

import java.util.*;

public class Player {

    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        // TODO
    }

    public void removeCard(int index) {
        // TODO
    }

    public Card getCard(int index) {
        // TODO
        return null;
    }

    public int getHandSize() {
        return hand.size();
    }

    public void showHand() {
        // TODO: Display cards
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

    public Card playTurn(Card topCard) {
        for (int i = 0; i < hand.size(); i++) {
            Card candidate = hand.get(i);
            if (candidate.getColor().equals(topCard.getColor()) ||
                    candidate.getValue().equals(topCard.getValue()) ||
                    candidate.getColor().equals("Wild")) {

                return hand.remove(i); // Remove and return the card
            }
        }
        return null; // No playable card
    }

    public String chooseColor() {
        /**
         * "I need Player class to have a public String chooseColor() method that
         * returns either 'Red', 'Blue', 'Green', or 'Yellow'."
         */
        return "";
    }
}
