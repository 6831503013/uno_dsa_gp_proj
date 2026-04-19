package controllers;

import exceptions.EmptyNameException;
import exceptions.InvalidNameException;
import java.util.*;
import models.*;
import utils.*;

public class GameController {

    private Deck deck;
    private List<Player> players;
    private GamePile<Card> discardPile;

    private int currentPlayerIndex;
    private boolean isClockWise = true;
    private String currentColor;
    private final int CardsPerPlayer = 7;
    private Scanner scanner = new Scanner(System.in);

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
        DisplayHandler.declareWinner(winner.getName());
    }

    private void setupPlayers() {
        // Ask the user for names and validate them
        Scanner sc = new Scanner(System.in);
        String playerName = "";
        while (true) {
            try {
                playerName = InputHandler.getValidateName(sc);
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
        }
    }

    private Player gameLoop() {
        while (true) {
            // Clear the console after a successful move for better readability
            System.out.print("\033[H\033[2J");
            System.out.flush();
            Player currentPlayer = players.get(currentPlayerIndex);
            Card topCard = discardPile.peek();
            DisplayHandler.displayPlayerTurn(currentPlayer.getName());

            if (isSpecialCard(topCard)) {
                topCard.setColor(currentColor);
            }

            // show top card and player hand
            DisplayHandler.renderTopCard(topCard);

            System.out.println(currentPlayer.getName() + "'s hand:");

            DisplayHandler.displayHand(currentPlayer.getHand());

            // Ask the player for their move until they make a valid one
            while (true) {
                Move move = currentPlayer.playTurn(scanner);

                if (move.getType() == Move.Type.DRAW) {
                    System.out.println(currentPlayer.getName() + " chose to draw.");

                    Card drawnCard = RecursionHelper.drawUntilPlayable(currentPlayer, deck,
                            topCard);

                    if (drawnCard != null) {
                        // clear the screen after drawing for better readability
                        System.out.print("\033[H\033[2J");
                        System.out.flush();

                        // show top card and player hand again after drawing
                        DisplayHandler.renderTopCard(topCard);
                        System.out.println(currentPlayer.getName() + "'s hand:");
                        DisplayHandler.displayHand(currentPlayer.getHand());

                        // Ask the player to choose whether to play or draw again
                        Move playMove = currentPlayer.playTurn(scanner);

                        // Player chose to play the card
                        if (playMove.getType() == Move.Type.PLAY) {
                            Card chosenCard = playMove.getCard();

                            // Check if the played card is valid
                            if (GameRules.isValidMove(chosenCard, topCard)) {
                                currentPlayer.getHand().remove(chosenCard);
                                System.out.println(currentPlayer.getName() + " played: " + chosenCard);
                                currentColor = chosenCard.getColor();
                                discardPile.push(chosenCard);

                                // Apply special effect RIGHT AFTER the card is played
                                if (isSpecialCard(chosenCard)) {
                                    GameRules.applySpecialCard(chosenCard, this, deck);
                                }
                            } else {
                                System.out.println("That card is not playable.");
                            }
                        } else {
                            DisplayHandler.typewrite(currentPlayer.getName() + " cannot choose to draw again.", 90);
                        }
                    }
                    break;
                } else {
                    // Player chose to play a card
                    Card card = move.getCard();
                    if (GameRules.isValidMove(card, topCard)) {
                        // Valid move, play the card
                        System.out.println(currentPlayer.getName() + " played: " + card);
                        currentPlayer.getHand().remove(card);
                        currentColor = card.getColor();
                        discardPile.push(card);

                        // Apply special effect RIGHT AFTER the card is played
                        if (isSpecialCard(card)) {
                            GameRules.applySpecialCard(card, this, deck);
                        }

                        // Check for winner after every move
                        if (GameRules.checkWinner(currentPlayer)) {
                            // return winner and end game loop function
                            return currentPlayer;
                        }
                        // Clear the console after a successful move for better readability
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
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
        DisplayHandler.typewrite("The next player is skipped.", 90);
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

    private void nextTurn() {
        int direction = isClockWise ? 1 : -1;
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

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
