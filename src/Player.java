import javax.swing.plaf.SplitPaneUI;
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

            List<Hurdle> hurdle = new ArrayList<>();
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
                hurdle.add(new Hurdle(gpu, reg0, reg1, reg2, reg3, reg4, reg5, reg6, playerIdx));
            }


            int scoreRight = 0;
            int scoreDown = 0;
            int scoreLeft = 0;
            int scoreUp = 0;
            int scoreMax = 0;


            // Perform actions with the active games
            for (Hurdle scorehurdle : hurdle) {
                ScoreAction scores = scorehurdle.compute();
                scoreRight += scores.scoreRight;
                scoreDown += scores.scoreDown;
                scoreLeft += scores.scoreLeft;
                scoreUp += scores.scoreUp;
            }


            scoreMax = Math.max(scoreRight, Math.max(scoreLeft, Math.max(scoreDown, scoreUp)));
            System.err.println("scoreUp : " + scoreUp + " scoreLeft : " + scoreLeft + " scoreDown : " + scoreDown + " scoreRight : " + scoreRight);

            // exemple :    0 1 2 3 4 5 6 7 8 9 10
            //              . . # . . . # . # .
            //              . . . # . . # . . #
            //              . . # . # . # . # .
            //              . . . # . # . # . .


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

class Hurdle {
    public String activeGames;
    public static int firstHurdle;
    public static int retourHurdle;

    public Hurdle(String gpu, int reg0, int reg1, int reg2, int reg3, int reg4, int reg5, int reg6, int playerIdx) {

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
        System.err.println("Player ID : " + playerIdx);

        // Example condition to select active games based on playerIdx
        if (!gpu.equals("GAME_OVER") && stun == 0) {
            if (positionPlayer <= gpu.length()) {
                activeGames = gpu.substring(positionPlayer);
            } else {
                activeGames = "";
            }
        }
    }

    public ScoreAction compute() {
        int scoreRight = 0;
        int scoreDown = 0;
        int scoreLeft = 0;
        int scoreUp = 0;
        int scoreMax = 0;


        // Perform actions with the active games
        if (activeGames != null && !activeGames.isEmpty()) {
            firstHurdle = activeGames.indexOf("#", 1);

            if (firstHurdle != -1) {
                if (firstHurdle > 3) {
                    scoreRight += 1;
                }
                if (firstHurdle > 1) {
                    scoreLeft += 1;
                }
                if (firstHurdle > 2) {
                    scoreDown += 1;
                }

            } else {
                scoreRight += 1;
                scoreDown += 1;
                scoreLeft += 1;
            }


            retourHurdle = activeGames.indexOf("#", 2);
            System.err.println("gpu " + activeGames + "retourHurdle " + retourHurdle);
            if (retourHurdle != -1) {

                if (retourHurdle > 2) {
                    scoreUp += 1;
                }


            } else {
                scoreUp += 1;
            }
            System.err.println("Active game: " + activeGames);
        }
        ScoreAction scoreAction = new ScoreAction();
        scoreAction.scoreRight = scoreRight;
        scoreAction.scoreDown = scoreDown;
        scoreAction.scoreLeft = scoreLeft;
        scoreAction.scoreUp = scoreUp;
        return scoreAction;
    }
}

class ScoreAction {
    public int scoreLeft = 0;
    public int scoreRight = 0;
    public int scoreDown = 0;
    public int scoreUp = 0;
}

enum Action {
    UP, DOWN, LEFT, RIGHT
}