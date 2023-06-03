package com.ritika;

import org.junit.Before;
import org.junit.Test;

import com.ritika.Entity.CardGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CardGameTest {
    private CardGame game;

    @Before
    public void setUp() {
        int numPlayers = 4;
        game = new CardGame(numPlayers);
    }

    @Test
    public void testInitializeDeck() {
        game.initializeDeck();
        assertEquals(52, game.getDrawPileSize());
    }

    @Test
    public void testShuffleDeck() {
        List<String> originalPile = new ArrayList<>(game.getDrawPile());
        game.shuffleDeck();
        assertNotEquals(originalPile, game.getDrawPile());
    }

    @Test
    public void testDealInitialCards() {
        game.initializeDeck();
        game.shuffleDeck();
        game.dealInitialCards();
        assertEquals(5, game.getPlayerHandSize(0));
        assertEquals(5, game.getPlayerHandSize(1));
        assertEquals(5, game.getPlayerHandSize(2));
        assertEquals(5, game.getPlayerHandSize(3));
        assertEquals(1, game.getDiscardPileSize());
    }

    @Test
    public void testDrawCard() {
        game.initializeDeck();
        int initialDrawPileSize = game.getDrawPileSize();
        String card = game.drawCard();
        assertNotNull(card);
        assertEquals(initialDrawPileSize - 1, game.getDrawPileSize());
    }

    @Test
    public void testPlayTurnValidPlay() {
        game.initializeDeck();
        game.shuffleDeck();
        game.dealInitialCards();

        // Set up a valid play
        game.setTopCard("Ace of Spades");
        game.setCurrentPlayerHand(0, new ArrayList<>(List.of("Ace of Hearts")));

        // Play the turn
        game.play();

        // Verify the card was played and the top card has been updated
        assertEquals(0, game.getPlayerHandSize(0));
        assertEquals("Ace of Hearts", game.getTopCard());
    }

    @Test
    public void testPlayTurnInvalidPlay() {
        game.initializeDeck();
        game.shuffleDeck();
        game.dealInitialCards();

        // Set up an invalid play
        game.setTopCard("Ace of Spades");
        game.setCurrentPlayerHand(0, new ArrayList<>(List.of("King of Hearts")));

        // Play the turn
        game.playTurn();

        // Verify the card was not played and the top card remains unchanged
        assertEquals(1, game.getPlayerHandSize(0));
        assertEquals("Ace of Spades", game.getTopCard());
    }

    @Test
    public void testApplySpecialActionSkipNextPlayer() {
        game.applySpecialAction("Ace of Spades");
        assertTrue(game.isSkipNextPlayer());
    }

    @Test
    public void testApplySpecialActionReverse() {
        assertFalse(game.isReverse());
        game.applySpecialAction("King of Hearts");
        assertTrue(game.isReverse());
    }

    @Test
    public void testApplySpecialActionDrawCards() {
        game.initializeDeck();
        int initialDrawPileSize = game.getDrawPileSize();
        game.applySpecialAction("Queen of Diamonds");
        assertEquals(initialDrawPileSize - 2, game.getDrawPileSize());
    }

    @Test
    public void testCheckWinCondition() {
        game.setCurrentPlayerHand(0, new ArrayList<>());
        game.checkWinCondition();
        assertTrue(game.isGameOver());
    }
}
