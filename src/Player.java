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
            List<String> scoreInfoList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String scoreInfo = in.nextLine();
                System.err.println("scoreInfo : " + scoreInfo);
                scoreInfoList.add(scoreInfo);
            }

            int goldCountHurdle = 0;
            int goldCountBow = 0;
            int goldCountDiving = 0;

            for (String scoreInfo : scoreInfoList) {
                if (scoreInfo.length() > 10) {
                    if (scoreInfo.charAt(2) == '1') {
                        goldCountHurdle++;
                    }
                    if (scoreInfo.charAt(8) == '1') {
                        goldCountBow++;
                    }
                    if (scoreInfo.charAt(20) == '1') {
                        goldCountDiving++;
                    }
                    System.err.println("goldCountHurdle : " + goldCountHurdle + " goldCountBow : " + goldCountBow + " goldCountDiving : " + goldCountDiving);
                }
            }

            List<Game> games = new ArrayList<>();


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


                if (i == 0) {
                    games.add(new Hurdle(gpu, reg0, reg1, reg2, reg3, reg4, reg5, reg6, playerIdx));
                } else if (i == 3) {
                    games.add(new Diving(gpu, reg0, reg1, reg2, reg3, reg4, reg5, reg6, playerIdx));
                } else if (i == 1) {
                    games.add(new Bow(gpu, reg0, reg1, reg2, reg3, reg4, reg5, reg6, playerIdx));
                }

            }


            int scoreRight = 0;
            int scoreDown = 0;
            int scoreLeft = 0;
            int scoreUp = 0;
            int scoreMax = 0;


            // Perform actions with the active games
            for (Game game : games) {
                ScoreAction scores = game.compute();

                // Si le jeu précédent n'a pas obtenu une médaille d'or, appliquer le facteur x2
                if (game instanceof Hurdle && goldCountHurdle == 0 ||
                        game instanceof Bow && goldCountBow == 0 ||
                        game instanceof Diving && goldCountDiving == 0) {
                    scores.scoreRight *= 2;
                    scores.scoreDown *= 2;
                    scores.scoreLeft *= 2;
                    scores.scoreUp *= 2;
                }

                scoreRight += scores.scoreRight;
                scoreDown += scores.scoreDown;
                scoreLeft += scores.scoreLeft;
                scoreUp += scores.scoreUp;
            }


            scoreMax = Math.max(scoreRight, Math.max(scoreLeft, Math.max(scoreDown, scoreUp)));
            System.err.println("Resultat Up : " + scoreUp + " Left : " + scoreLeft + " Down : " + scoreDown + " Right : " + scoreRight);


            if (scoreMax == scoreRight) {
                System.out.println("RIGHT");
            } else if (scoreMax == scoreDown && scoreDown != scoreUp) {
                System.out.println("DOWN");
            } else if (scoreMax == scoreUp) {
                System.out.println("UP");
            } else {
                System.out.println("LEFT");
            }
            goldCountHurdle = 0;
            goldCountBow = 0;
            goldCountDiving = 0;
        }
    }

}

interface Game {
    ScoreAction compute();

}

class Bow implements Game {
    public String activeGames;
    public int positionX;
    public int positionY;
    public int windforce;
    public CursorPosition cursorPosition;

    public Bow(String gpu, int reg0, int reg1, int reg2, int reg3, int reg4, int reg5, int reg6, int playerIdx) {
        this.positionX = 0;
        this.positionY = 0;
        this.windforce = 0;

        this.cursorPosition = new CursorPosition(0, 0);


        if (playerIdx == 0) {
            this.positionX = reg0;
            this.positionY = reg1;
        } else if (playerIdx == 1) {
            this.positionX = reg2;
            this.positionY = reg3;
        } else {
            this.positionX = reg4;
            this.positionY = reg5;
        }

        if (!gpu.equals("GAME_OVER") && windforce == 0) {
            this.windforce = Character.getNumericValue(gpu.charAt(0));
            if (windforce <= gpu.length() && windforce >= 0) {
                activeGames = gpu.substring(windforce);
            } else {
                activeGames = "";
            }
            this.cursorPosition = new CursorPosition(positionX, positionY);
        }


    }


    @Override
    public ScoreAction compute() {
        int scoreRight = 0;
        int scoreDown = 0;
        int scoreLeft = 0;
        int scoreUp = 0;
        int scoreMax = 0;


        if (activeGames != null) {
            int absPositionX = Math.abs(positionX);
            int absPositionY = Math.abs(positionY);
            System.err.println("Position du curseur : (" + positionX + ", " + positionY + ")");
            System.err.println("Cible : (0, 0)");
            System.err.println("Force du vent : " + windforce);

            if (absPositionX > absPositionY) {
                if (positionX < 0) {
                    scoreRight += 1;
                } else if (positionX > 0) {
                    scoreLeft += 1;
                }
            } else if (absPositionX < absPositionY) {
                if (positionY > 0) {
                    scoreUp += 1;
                } else if (positionY < 0) {
                    scoreDown += 1;
                }
            } else {
                // Si absPositionX et absPositionY sont égaux, prioriser arbitrairement un mouvement
                if (positionX < 0) {
                    scoreRight += 1;
                } else if (positionX > 0) {
                    scoreLeft += 1;
                } else if (positionY > 0) {
                    scoreUp += 1;
                } else if (positionY < 0) {
                    scoreDown += 1;
                }
            }
            System.err.println("scoreUp : " + scoreUp + " scoreLeft : " + scoreLeft + " scoreDown : " + scoreDown + " scoreRight : " + scoreRight);
        }


        ScoreAction scoreAction = new ScoreAction();
        scoreAction.scoreRight = scoreRight;
        scoreAction.scoreDown = scoreDown;
        scoreAction.scoreLeft = scoreLeft;
        scoreAction.scoreUp = scoreUp;
        return scoreAction;
    }


    public static class CursorPosition {
        public int positionX;
        public int positionY;

        public CursorPosition(int PositionX, int PositionY) {
            this.positionX = Math.max(-20, Math.min(20, PositionX)); // voir pour l'intervalle [-20;20]
            this.positionY = Math.max(-20, Math.min(20, PositionY)); // voir pour l'intervalle [-20;20]
        }

    }

}

class Diving implements Game {
    public String activeGames;
    public static int moove;
    public int playerPosition;
    public int combo;


    public Diving(String gpu, int reg0, int reg1, int reg2, int reg3, int reg4, int reg5, int reg6, int playerIdx) {

        int combo = 0;
        int pointsPlayer = 0;


        if (playerIdx == 0) {
            this.combo = reg3;
            pointsPlayer = reg0;
        } else if (playerIdx == 1) {
            this.combo = reg4;
            pointsPlayer = reg1;
        } else {
            this.combo = reg5;
            pointsPlayer = reg2;
        }

        playerPosition = 0;

        if (!gpu.equals("GAME_OVER") && this.combo >= 0) {
            if (playerPosition < gpu.length() && pointsPlayer >= 0) {
                activeGames = gpu.substring(playerPosition);
            } else {
                activeGames = "";
            }

        }

    }

    @Override
    public ScoreAction compute() {
        int scoreRight = 0;
        int scoreDown = 0;
        int scoreLeft = 0;
        int scoreUp = 0;
        int scoreMax = 0;


        if (activeGames != null && playerPosition < activeGames.length()) {
            // Obtenir le prochain mouvement à la position actuelle du joueur
            char nextMoove = activeGames.charAt(0);
            moove = nextMoove;

            // Mettre à jour les scores en fonction du prochain mouvement
            if (nextMoove == 'U') {
                if (this.combo >= 1) {
                    scoreUp += 2;
                } else {
                    scoreUp += 1;
                }
            } else if (nextMoove == 'R') {
                if (this.combo >= 1) {
                    scoreRight += 2;
                } else {
                    scoreRight += 1;
                }
            } else if (nextMoove == 'L') {
                if (this.combo >= 1) {
                    scoreLeft += 2;
                } else {
                    scoreLeft += 1;
                }
            } else if (this.combo >= 1) {
                scoreDown += 2;
            } else {
                scoreDown += 1;
            }
            if (this.combo >= 1) {
                scoreUp *= 1;
                scoreLeft *= 1;
                scoreDown *= 1;
                scoreRight *= 1;
            }

            System.err.println("NextMoove: " + nextMoove);
            System.err.println("scoreUp : " + scoreUp + " scoreLeft : " + scoreLeft + " scoreDown : " + scoreDown + " scoreRight : " + scoreRight);


        }

        ScoreAction scoreAction = new ScoreAction();
        scoreAction.scoreRight = scoreRight;
        scoreAction.scoreDown = scoreDown;
        scoreAction.scoreLeft = scoreLeft;
        scoreAction.scoreUp = scoreUp;
        return scoreAction;

    }

}

class Hurdle implements Game {
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


        // Example condition to select active games based on playerIdx
        if (!gpu.equals("GAME_OVER") && stun == 0) {
            if (positionPlayer <= gpu.length() && positionPlayer >= 0) {
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
                if (firstHurdle > 4) {
                    scoreUp += 1;

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
            System.err.println("scoreUp : " + scoreUp + " scoreLeft : " + scoreLeft + " scoreDown : " + scoreDown + " scoreRight : " + scoreRight);
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