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

                int stun = 0;
                int positionPlayer = 0;
                if (playerIdx == 0) {
                    stun = reg3;
                    positionPlayer = reg0;
                } else if (playerIdx == 1) {
                    stun = reg4;
                    positionPlayer = reg1;
                } else {
                    stun = reg5;
                    positionPlayer = reg2;
                }
                // Example condition to select active games based on playerIdx
                if (!gpu.equals("GAME_OVER") || stun == 0) {
                    if (positionPlayer <= gpu.length()) {
                        activeGames.add(gpu.substring(positionPlayer));
                    } else {
                        activeGames.add(gpu);
                    }
                } else {
                    activeGames.add(gpu);
                }
            }

            int scoreRight = 0;
            int scoreDown = 0;
            int scoreLeft = 0;
            int scoreUp = 0;
            int scoreMax = 0;


            // Perform actions with the active games
            for (String gpu : activeGames) {
                int retourHurdle = gpu.indexOf("#");
                if (retourHurdle != -1) {
                    if (retourHurdle > 3) {
                        scoreRight += 1;
                    }
                    if (retourHurdle > 2) {
                        scoreDown += 1;
                    }
                    if (retourHurdle > 1) {
                        scoreLeft += 1;
                    }
                } else {
                    scoreRight += 1;
                    scoreDown += 1;
                    scoreLeft += 1;
                }
                System.err.println("Active game: " + gpu);
                // Actions pour chaque jeu actif
            }
            for (String gpu : activeGames) {
                int retourHurdle = gpu.indexOf("#", 2);
                if (retourHurdle != -1) {
                    if (retourHurdle > 1) {
                        scoreUp += 1;
                    }
                } else {
                    scoreUp += 1;

                }
                System.err.println("Active game: " + gpu);
                // Actions pour chaque jeu actif
            }

            for (String gpu : activeGames) {
                int retourHurdle = gpu.lastIndexOf("#");
                if (retourHurdle != -1) {
                    if (retourHurdle == 0) {
                        scoreRight += 1;
                    }
                } else {
                    scoreRight += 1;
                }
            }

            scoreMax = Math.max(scoreRight, Math.max(scoreDown, Math.max(scoreLeft, scoreUp)));

            if (scoreMax == scoreRight) {
                System.out.println("RIGHT");
            } else if (scoreMax == scoreUp) {
                System.out.println("UP");
            } else if (scoreMax == scoreDown) {
                System.out.println("DOWN");
            } else {
                System.out.println("LEFT");
            }

        }
    }

}