import java.util.*;

class Player {
    private static Map<Integer, GameInfo> gamesInfo = new HashMap<>();
    private static boolean isStunned = false;
    private static int playerIdx;
    private static int nbGames;

    static class GameInfo {
        String gpu;
        List<Integer> obstacles = new ArrayList<>();
        List<Integer> futureObstacles = new ArrayList<>();
        int myPosition;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        playerIdx = in.nextInt();
        nbGames = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine(); // Consume newline character
        }

        // game loop
        while (true) {
            isStunned = false; // Reset stun status for the new game

            // Read score information for each player
            String[] scoreInfo = new String[3];
            for (int i = 0; i < 3; i++) {
                scoreInfo[i] = in.nextLine();
            }

            // Read game data for each mini-game
            for (int i = 0; i < nbGames; i++) {
                String gpu = in.next();
                int[] registers = new int[7]; // 7 registers (reg0 to reg6)
                for (int j = 0; j < 7; j++) {
                    registers[j] = in.nextInt();
                }
                if (in.hasNextLine()) {
                    in.nextLine(); // Consume newline character
                }

                // Store game information
                GameInfo gameInfo = new GameInfo();
                gameInfo.gpu = gpu;
                gameInfo.myPosition = registers[0];
                gamesInfo.put(i, gameInfo);
            }

            // Update current game info
            GameInfo currentGame = gamesInfo.get(playerIdx);

            // Pre-calculate obstacles for the current player's GPU
            calculateObstacles(currentGame);

            // Pre-calculate future obstacles
            updateFutureObstacles(currentGame);

            // Choose the action for the current game state
            String direction = chooseDirection(currentGame);

            // Output the chosen direction
            System.out.println(direction);
        }
    }

    private static void calculateObstacles(GameInfo gameInfo) {
        gameInfo.obstacles.clear();
        int trackLength = gameInfo.gpu.length();
        for (int i = 0; i < trackLength; i++) {
            if (gameInfo.gpu.charAt(i) == '#') {
                gameInfo.obstacles.add(i);
            }
        }
    }

    private static void updateFutureObstacles(GameInfo gameInfo) {
        gameInfo.futureObstacles.clear();
        int trackLength = gameInfo.gpu.length();
        int myPosition = gameInfo.myPosition;

        // Add current obstacles
        for (int obstacle : gameInfo.obstacles) {
            if (obstacle > myPosition) {
                gameInfo.futureObstacles.add(obstacle);
            }
        }
        // Analyze the track for new obstacles that you can see ahead
        for (int i = myPosition; i < trackLength; i++) {
            if (gameInfo.gpu.charAt(i) == '#') {
                gameInfo.futureObstacles.add(i);
            }
        }
    }

    private static String chooseDirection(GameInfo gameInfo) {
        // If stunned, do nothing
        if (isStunned) {
            return "LEFT";
        }

        // Check the track length
        int trackLength = gameInfo.gpu.length();

        // Check up to myPosition + 3 for potential obstacles
        for (int i = gameInfo.myPosition + 1; i <= gameInfo.myPosition + 3 && i < trackLength; i++) {
            if (gameInfo.obstacles.contains(i)) {
                // If there's an obstacle, decide the best move
                if (i + 1 < trackLength && gameInfo.gpu.charAt(i + 1) == '#') {
                    return "RIGHT";
                } else if (i + 2 < trackLength && gameInfo.gpu.charAt(i + 2) == '#') {
                    return "DOWN";
                } else {
                    return "UP";
                }
            }
        }

        // If we can move up to myPosition + 3 without obstacles, move RIGHT
        if (gameInfo.myPosition + 3 < trackLength && gameInfo.gpu.charAt(gameInfo.myPosition + 3) != '#') {
            return "RIGHT";
        }

        // Default action: move one space to the left (LEFT)
        return "LEFT";
    }
}