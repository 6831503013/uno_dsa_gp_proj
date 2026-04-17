package controllers;

import exceptions.EmptyNameException;
import exceptions.InvalidNameException;
import java.util.*;
import models.*;
import utils.DisplayHandler;

public class GameController {

    private Deck deck;
    private List<Player> players;
    private GamePile<Card> discardPile;

    private int currentPlayerIndex;
    private boolean isClockWise = true;
    private String currentColor;
    private final int CardsPerPlayer = 7;
    private Scanner scanner = new Scanner(System.in);
    // private boolean gameOver = false;

    public GameController() {
        deck = new Deck();
        players = new ArrayList<>();
        // custome generic class (Parametric Polymorphism)
        discardPile = new GamePile<>();
        currentPlayerIndex = 0;

    }

    public void startGame() {
        setupPlayers();
        dealCards();
        initializeDiscardPile();
        Player winner = gameLoop();
        declareWinner(winner);
    }

    public String getValidateName(Scanner sc) throws EmptyNameException, InvalidNameException {
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();

        // Check for empty name
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyNameException("Name cannot be empty. Please enter a valid name.");
        }

        // Check for invalid characters (only letters and spaces allowed)
        if (!name.matches("[a-zA-Z ]+")) {
            throw new InvalidNameException(
                    "Name contains invalid characters. Please enter a valid name with words(Aa-Zz).");
        }

        return name;
    }

    private void setupPlayers() {
        // Ask the user for names and validate them
        Scanner sc = new Scanner(System.in);
        String playerName = "";
        while (true) {
            try {
                playerName = getValidateName(sc);
                break; // Exit loop if name is valid
            } catch (EmptyNameException | InvalidNameException e) {
                System.out.println("\033[91m" + e.getMessage() + "\033[0m");// Print error message in red
            }
        }

        // Just for the sake of the program
        String[] names = { playerName, "CPU 1", "CPU 2" };
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
         * Shuffling before distribution is critical to ensure game randomness
         * and fairness
         */
        // deck.shuffleDeck(); no need to shuffle here since the Deck constructor
        // already shuffles

        // We use a nested loop to implement "Round-Robin" dealing.
        // The outer loop represents the "round" (1st card, 2nd card, etc.)
        for (int i = 0; i < CardsPerPlayer; i++) {

            /**
             * The inner loop iterates through the player list to give each
             * player one card per round
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

        while (firstCard != null && (firstCard.getValue().equals("WildDraw4") || firstCard.getValue().equals("Wild"))) {
            deck.addCardToBottom(firstCard);
            firstCard = safeDraw();
        }

        if (firstCard != null) {
            discardPile.push(firstCard);
            currentColor = firstCard.getColor();

            // Optional: trigger special effect if the first card isn't a normal number
            // handleSpecialCard(firstCard);
        }
    }

    // private void initializeDiscardPile() {
    // Card firstCard = safeDraw();
    // while (firstCard != null && firstCard.getValue().equals("WildDraw4")) {
    // deck.addCard(firstCard);
    // deck.shuffleDeck();
    // firstCard = deck.drawCard();
    // }
    // if (firstCard != null) {
    // discardPile.push(firstCard); // Only push once
    // currentColor = firstCard.getColor();
    // // Optional: Trigger special effect if the first card isn't a normal number
    // handleSpecialCard(firstCard);
    // }
    // }
    // private void gameLoop() {
    // Player currentPlayer = players.get(currentPlayerIndex);
    // while (!GameRules.checkWinner(currentPlayer)) {
    // Card topCard = discardPile.peek();
    // Card chosenCard = currentPlayer.playTurn(topCard);
    // if (chosenCard != null && GameRules.isValidMove(chosenCard, topCard,
    // currentColor)) {
    // System.out.println(currentPlayer.getName() + " played: " + chosenCard);
    // discardPile.push(chosenCard);
    // handleSpecialCard(chosenCard);
    // } else if (chosenCard != null) {
    // // Safety: If the player tried to play an illegal card,
    // // give it back to them and make them draw as a penalty.
    // currentPlayer.addCard(chosenCard);
    // currentPlayer.addCard(safeDraw());
    // } else {
    // System.out.println(currentPlayer.getName() + " had to draw.");
    // Card drawn = safeDraw();
    // if (drawn != null) {
    // currentPlayer.addCard(drawn);
    // }
    // }
    // nextTurn();
    // }
    // }
    private Player gameLoop() {
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            Card topCard = discardPile.peek();

            // show top card and player hand
            DisplayHandler.renderTopCard(topCard);
            DisplayHandler.displayHand(currentPlayer.getHand());
            // Handle special card's effects before the player can move
            if (isSpecialCard(topCard)) {
                GameRules.applySpecialCard(topCard, this, deck);
            }

            // Ask the player for their move until they make a valid one
            while (true) {
                Move move = currentPlayer.playTurn(scanner);
                if (move.getType() == Move.Type.DRAW) {
                    // Player chose to draw
                    System.out.println(currentPlayer.getName() + " chose to draw.");
                    Card drawnCard = safeDraw();
                    if (drawnCard != null) {
                        currentPlayer.addCard(drawnCard);
                    }
                    break;
                } else {
                    // Player chose to play a card
                    Card card = move.getCard();
                    if (GameRules.isValidMove(card, topCard)) {
                        // Valid move, play the card
                        System.out.println(currentPlayer.getName() + " played: " + card);
                        currentPlayer.getHand().remove(card);
                        discardPile.push(card);

                        // Check for winner after every move
                        if (GameRules.checkWinner(currentPlayer)) {
                            // return winner and end game loop function
                            return currentPlayer;
                        }
                        break;
                    } else {
                        System.out.println("Invalid move!");
                    }
                }
            }
            nextTurn();
        }
    }

    private boolean isSpecialCard(Card card) {
        String value = card.getValue();
        return value.equals("Skip") || value.equals("Reverse") || value.equals("Draw2") || value.equals("Wild")
                || value.equals("WildDraw4");
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
        // We add players.size() before the module to handle negative results from
        // counter-clockwise
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        return players.get(nextIndex);
    }

    public void changeColor() {
        currentColor = players.get(currentPlayerIndex).chooseColor();
    }

    // private void handleSpecialCard(Card card) {
    // // If it's a normal colored card, update the game's currentColor tracker
    // if (!card.getColor().equals("Wild")) {
    // this.currentColor = card.getColor();
    // }
    // // Delegate special card effects to the rules engine
    // GameRules.applySpecialCard(card, this, deck);
    // // String value = card.getValue();
    // // switch (value) {
    // // case "Reverse" -> {
    // // if (players.size() == 2) {
    // // nextTurn();
    // // } else {
    // // isClockWise = !isClockWise;
    // // }
    // // }
    // // case "Skip" -> nextTurn();
    // // case "Draw2" -> {
    // // nextTurn();
    // // Player victim = players.get(currentPlayerIndex);
    // // for (int i = 0; i < 2; i++) {
    // // victim.addCard(deck.drawCard());
    // // }
    // // }
    // // case "Wild" -> {
    // // currentColor = players.get(currentPlayerIndex).chooseColor();
    // // }
    // // case "WildDraw4" -> {
    // // nextTurn();
    // // currentColor = players.get(currentPlayerIndex).chooseColor();
    // // Player victim = players.get(currentPlayerIndex);
    // // for (int i = 0; i < 4; i++)
    // // victim.addCard(safeDraw());
    // // }
    // // }
    // }
    private void nextTurn() {
        // TODO
        int direction = isClockWise ? 1 : -1;
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    // private boolean checkWinner() {
    // // TODO
    // for (Player p : players) {
    // if (p.getHand().isEmpty()) {
    // System.out.println(p.getName() + " has won the game!");
    // return true;
    // }
    // }
    // return false;
    // }
    // Add this helper to your GameController to handle empty decks safely
    // private Card safeDraw() {
    // Card drawn = deck.drawCard();
    // if (drawn == null) {
    // // Safety: If deck is empty, move discard pile (minus top card) back to deck
    // Card top = discardPile.pop();
    // // Assuming your teammate provides a way to add cards back
    // // For now, we'll just log it so you know where it failed
    // System.out.println("Deck empty! Reshuffling needed.");
    // discardPile.push(top);
    // return null; // Or trigger a reshuffle method if it exists
    // }
    // return drawn;
    // }
    private Card safeDraw() {
        Card drawn = deck.drawCard();

        if (drawn == null) {
            if (discardPile.size() <= 1) {
                System.out.println("No cards left to reshuffle!");
                return null;
            }

            System.out.println("Deck empty! Reshuffling discard pile...");

            // Use the generic class to handle the extraction
            List<Card> oldCards = discardPile.clearExceptTop();

            Collections.shuffle(oldCards);
            for (Card c : oldCards) {
                deck.addCardToBottom(c);
            }

            return deck.drawCard();
        }
        return drawn;
    }
}
