package com.ritika.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class CardGame {
    private List<String> drawPile;
    private List<String> discardPile;
    private List<String>[] playersCard;

    private int currentPlayer;
    private boolean reverse;
    private boolean skipNextPlayer;
    private boolean gameOver;
    
    public CardGame(int players) {
        drawPile = new ArrayList<>(); 
        discardPile = new ArrayList<>();
        playersCard = new ArrayList[players];

        for (int i = 0; i < players; i++) {
            playersCard[i] = new ArrayList<>();
        }
        currentPlayer = 0;
        reverse = false;
        skipNextPlayer = false;
        gameOver = false;
    }

    public void startGame() {
       initializeDeck();
       shuffleDeck();
        
        while(!gameOver) {
            play();
            checkWinCondition();
            changePlayer();

        }
    }

    public void initializeDeck() {
        String[] suits = {
            "Heart",
            "Spade",
            "Diamond",
            "Club"
        };
        String[] ranks = {
            "Ace",
            "King",
            "Queen",
            "10",
            "9",
            "8",
            "7",
            "6",
            "5",
            "4",
            "3",
            "2",
            "1",
            "Jack"
        };
        
        for(String suit: suits) {
            for(String rank: ranks) {
                drawPile.add(rank + " of " + suit);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(drawPile);
    }

    public void play() {
        System.out.println("Player " + (currentPlayer + 1) + "'s turn.");
        System.out.println("Top card: " + discardPile.get(discardPile.size() - 1));
        System.out.println("Your hand: " + playersCard[currentPlayer]);

        if (skipNextPlayer) {
            skipNextPlayer = false;
            System.out.println("Skipping next player's turn.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean validPlay = false;

        while (!validPlay) {
            System.out.print("Choose a card to play or type 'draw' to draw a card: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("draw")) {
                String drawnCard = drawCard();
                if (drawnCard != null) {
                    System.out.println("You drew: " + drawnCard);
                    playersCard[currentPlayer].add(drawnCard);
                    validPlay = true;
                }
            } else if (playersCard[currentPlayer].contains(input)) {
                String playedCard = playersCard[currentPlayer].remove(playersCard[currentPlayer].indexOf(input));
                if (isValidPlay(playedCard)) {
                    System.out.println("You played: " + playedCard);
                    discardPile.add(playedCard);
                    validPlay = true;
                    applySpecialAction(playedCard);
                } else {
                    System.out.println("Invalid play. Try again.");
                }
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    private boolean isValidPlay(String playedCard) {
        String topCard = discardPile.get(discardPile.size() - 1);
        String[] playedCardComponents = playedCard.split(" ");
        String[] topCardComponents = topCard.split(" ");

        return playedCardComponents[0].equalsIgnoreCase(topCardComponents[0]) ||
                playedCardComponents[2].equalsIgnoreCase(topCardComponents[2]);
    }

    public void applySpecialAction(String playedCard) {
        String[] playedCardComponents = playedCard.split(" ");
        String rank = playedCardComponents[0];

        switch (rank.toLowerCase()) {
            case "ace":
                skipNextPlayer = true;
                break;
            case "king":
                reverse = !reverse;
                break;
            case "queen":
                drawCards(2);
                break;
            case "jack":
                drawCards(4);
                break;
            default:
                break;
        }
    }

    private void drawCards(int numCards) {
        for (int i = 0; i < numCards; i++) {
            String drawnCard = drawCard();
            if (drawnCard != null) {
                System.out.println("Next player drew: " + drawnCard);
                playersCard[(currentPlayer + 1) % playersCard.length].add(drawnCard);
            }
        }
    }

    private String drawCard() {
        if (drawPile.isEmpty()) {
            gameOver = true;
            return null;
        }
        return drawPile.remove(drawPile.size() - 1);
    }

    private void checkWinCondition() {
        if (playersCard[currentPlayer].isEmpty()) {
            gameOver = true;
            System.out.println("Player " + (currentPlayer + 1) + " wins!");
        } else if (drawPile.isEmpty()) {
            gameOver = true;
            System.out.println("It's a draw!");
        }
    }

    private void changePlayer() {
        if (reverse) {
            currentPlayer = (currentPlayer - 1 + playersCard.length) % playersCard.length;
        } else {
            currentPlayer = (currentPlayer + 1) % playersCard.length;
        }
    }
    
    public int getDrawPileSize() {
        return drawPile.size();
    }

    public int getDiscardPileSize() {
        return drawPile.size();
    }

    public List<String> getDrawPile() {
        return drawPile;
    }

    public int getPlayerHandSize(int ind) {
        return playersCard[ind].size();
    }
    
}
