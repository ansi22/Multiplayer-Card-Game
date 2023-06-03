package com.ritika;

import java.util.Scanner;
import com.ritika.Entity.CardGame;;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter no. of players: ");

        int players = scanner.nextInt();

        CardGame game = new CardGame(players);
        game.startGame();

        scanner.close();
}
}
