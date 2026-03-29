package controllers;

import java.util.*;
import models.*;

public class GameController {

    private Deck deck;
    private List<Player> players;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private boolean isClockWise = true;
    private String currentColor;

    public GameController() {
        deck = new Deck();
        players = new ArrayList<>();
        discardPile = new Stack<>();
        currentPlayerIndex = 0;
    }

    public void startGame() {
        setupPlayers();
        dealCards();
        initializeDiscardPile();
        gameLoop();
    }

    private void setupPlayers() {
        // Just for the sake of the program
        String[] names = { "Human", "CPU 1", "CPU 2" };
        for (String n : names) {
            // adding players to the game via Player constructor
            players.add(new Player(n));
        }
    }

    /**
     * Distributes starting hands to all players using a round-robin approach.
     * This ensures fairness by following standard table dealing rules.
     */
    private void dealCards() {
        /**
         * Shuffling before distribution is critical to ensure game randomness and
         * fairness
         */
        deck.shuffleDeck();

        int cardsPerPlayer = 7;

        // We use a nested loop to implement "Round-Robin" dealing.
        // The outer loop represents the "round" (1st card, 2nd card, etc.)
        for (int i = 0; i < cardsPerPlayer; i++) {

            /**
             * The inner loop iterates through the player list to give each player one card
             * per round
             */
            for (Player player : players) {

                /*
                 * * We use safeDraw() instead of deck.drawCard() to protect against
                 * NullPointerExceptions if the deck size is smaller than (players * 7).
                 */
                Card drawnCard = safeDraw();

                if (drawnCard != null) {
                    player.addCard(drawnCard);
                }
            }
        }
    }

    private void initializeDiscardPile() {
        Card firstCard = safeDraw();
        while (firstCard != null && firstCard.getValue().equals("WildDraw4")) {
            deck.addCard(firstCard);
            deck.shuffleDeck();
            firstCard = deck.drawCard();
        }
        if (firstCard != null) {
            discardPile.push(firstCard); // Only push once
            currentColor = firstCard.getColor();

            // Optional: Trigger special effect if the first card isn't a normal number
            handleSpecialCard(firstCard);
        }
    }

    private void gameLoop() {
        while (!checkWinner()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            Card topCard = discardPile.peek();

            Card chosenCard = currentPlayer.playTurn(topCard);

            if (chosenCard != null && GameRules.isValidMove(chosenCard, topCard, currentColor)) {
                System.out.println(currentPlayer.getName() + " played: " + chosenCard);
                discardPile.push(chosenCard);
                handleSpecialCard(chosenCard);
            } else if (chosenCard != null) {
                // Safety: If the player tried to play an illegal card,
                // give it back to them and make them draw as a penalty.
                currentPlayer.addCard(chosenCard);
                currentPlayer.addCard(safeDraw());
            } else {
                System.out.println(currentPlayer.getName() + " had to draw.");
                Card drawn = safeDraw();
                if (drawn != null)
                    currentPlayer.addCard(drawn);
            }

            nextTurn();
        }
    }

    public void skipNextPlayer() {
        nextTurn();
        System.out.println("The next player is skipped.");
    }

    public void reverseDirection() {
        if (players.size() > 2) {
            isClockWise = !isClockWise;
        } else {
            nextTurn();
        }
    }

    public Player getNextPlayer() {
        int direction = isClockWise ? 1 : -1;
        // We add players.size() before the modulo to handle negative results from
        // counter-clockwise
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        return players.get(nextIndex);
    }

    public void changeColor() {
        currentColor = players.get(currentPlayerIndex).chooseColor();
    }

    private void handleSpecialCard(Card card) {
        // If it's a normal colored card, update the game's currentColor tracker
        if (!card.getColor().equals("Wild")) {
            this.currentColor = card.getColor();
        }

        // Delegate special card effects to the rules engine
        GameRules.applySpecialCard(card, this, deck);

        // String value = card.getValue();

        // switch (value) {
        // case "Reverse" -> {
        // if (players.size() == 2) {
        // nextTurn();
        // } else {
        // isClockWise = !isClockWise;
        // }
        // }
        // case "Skip" -> nextTurn();
        // case "Draw2" -> {
        // nextTurn();
        // Player victim = players.get(currentPlayerIndex);
        // for (int i = 0; i < 2; i++) {
        // victim.addCard(deck.drawCard());
        // }
        // }
        // case "Wild" -> {
        // currentColor = players.get(currentPlayerIndex).chooseColor();
        // }
        // case "WildDraw4" -> {
        // nextTurn();

        // currentColor = players.get(currentPlayerIndex).chooseColor();
        // Player victim = players.get(currentPlayerIndex);
        // for (int i = 0; i < 4; i++)
        // victim.addCard(safeDraw());
        // }
        // }
    }

    private void nextTurn() {
        // TODO
        int direction = isClockWise ? 1 : -1;
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    private boolean checkWinner() {
        // TODO
        for (Player p : players) {
            if (p.getHand().isEmpty()) {
                System.out.println(p.getName() + " has won the game!");
                return true;
            }
        }
        return false;
    }

    // Add this helper to your GameController to handle empty decks safely
    private Card safeDraw() {
        Card drawn = deck.drawCard();
        if (drawn == null) {
            // Safety: If deck is empty, move discard pile (minus top card) back to deck
            Card top = discardPile.pop();
            // Assuming your teammate provides a way to add cards back
            // For now, we'll just log it so you know where it failed
            System.out.println("Deck empty! Reshuffling needed.");
            discardPile.push(top);
            return null; // Or trigger a reshuffle method if it exists
        }
        return drawn;
    }
}
