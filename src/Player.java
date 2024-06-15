import java.util.*;
import java.io.*;
import java.math.*;

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
            int Runtotheend = 0;
            int gamesWithHurdleAtFourOrMore = 0;

            // Perform actions with the active games
            for (String gpu : activeGames) {
                int firstHurdle = gpu.indexOf("#");

                if (firstHurdle >= 4) {
                    gamesWithHurdleAtFourOrMore += 1;
                } else if (firstHurdle == -1) {
                    Runtotheend += 1;
                }

                if (firstHurdle != -1) {
                    if (firstHurdle >= 3) {
                        scoreRight += 1;
                    }
                    if (firstHurdle >= 2) {
                        scoreDown += 1;
                    }
                    if (firstHurdle >= 1) {
                        scoreLeft += 1;
                    }
                } else {
                    scoreRight += 1;
                    scoreDown += 1;
                    scoreLeft += 1;
                }
                System.err.println("Active game: " + gpu);
            }

            for (String gpu : activeGames) {
                int retourHurdle = gpu.indexOf("#", 2);
                if (retourHurdle != -1) {
                    if (retourHurdle >= 1) {
                        scoreUp += 1;
                    }
                } else {
                    scoreUp += 1;
                }
                System.err.println("Active game: " + gpu);
            }

            scoreMax = Math.max(scoreRight, Math.max(scoreLeft, Math.max(scoreDown, scoreUp)));

            // If in at least 2 out of the 4 races the first '#' is at position 4 or more, always choose RIGHT
            if (gamesWithHurdleAtFourOrMore >= 2) {
                System.out.println("UP");
            } else if (Runtotheend >= 2) {
                System.out.println("RIGHT");
            } else {
                if (scoreMax == scoreRight) {
                    System.out.println("RIGHT");
                } else if (scoreMax == scoreDown) {
                    System.out.println("DOWN");
                } else if (scoreMax == scoreUp) {
                    System.out.println("UP");
                } else {
                    System.out.println("LEFT");
                }
            }

        }
    }
}