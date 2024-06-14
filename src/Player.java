import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int playerIdx = in.nextInt();
        int nbGames = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        // game loop
        while (true) {
            for (int i = 0; i < 3; i++) {
                String scoreInfo = in.nextLine();
            }

            List<String> activeGames = new ArrayList<>();

            for (int i = 0; i < nbGames; i++) {
                String gpu = in.next();
                int reg0 = in.nextInt();
                int reg1 = in.nextInt();
                int reg2 = in.nextInt();
                int reg3 = in.nextInt();
                int reg4 = in.nextInt();
                int reg5 = in.nextInt();
                int reg6 = in.nextInt();
                in.nextLine();

                // Example condition to select active games based on playerIdx
                if (reg0 == playerIdx || reg1 == playerIdx) {
                    activeGames.add(gpu);
                }
            }


            // Perform actions with the active games
            for (String gpu : activeGames) {
                System.err.println("Active game: " + gpu);
                // Actions pour chaque jeu actif
            }

            System.out.println("LEFT");
        }
    }
}