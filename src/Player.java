import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Player {
    private static List<Integer> obstacles = new ArrayList<>();
    private static List<Integer> futureObstacles = new ArrayList<>();
    private static boolean isStunned = false;
    private static int playerIdx;
    private static int nbGames;

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
            String[] gpu = new String[nbGames];
            int[][] registers = new int[nbGames][7]; // 7 registers (reg0 to reg6)
            for (int i = 0; i < nbGames; i++) {
                gpu[i] = in.next();
                for (int j = 0; j < 7; j++) {
                    registers[i][j] = in.nextInt();
                }
                if (in.hasNextLine()) {
                    in.nextLine(); // Consume newline character
                }
            }

            // Pre-calculate obstacles for the current player's GPU
            calculateObstacles(gpu[playerIdx]);

            // Pre-calculate futurObstacles
            updateFutureObstacles(gpu[playerIdx], registers[playerIdx][0]);

            // Choose the action for the current game state
            String direction = chooseDirection(gpu[playerIdx], registers[playerIdx][0], registers[playerIdx][3], 0);

            // Output the chosen direction
            System.out.println(direction);
        }
    }

    private static void calculateObstacles(String gpu) {
        obstacles.clear();
        int trackLength = gpu.length();
        for (int i = 0; i < trackLength; i++) {
            if (gpu.charAt(i) == '#') {
                obstacles.add(i);
            }
        }
    }

    private static void updateFutureObstacles(String gpu, int myPosition) {
        futureObstacles.clear();
        int trackLength = gpu.length();

        // Add current obstacles
        for (int obstacle : obstacles) {
            if (obstacle > myPosition) {
                futureObstacles.add(obstacle);
            }
        }
        // Analyze the track for new obstacles that you can see ahead
        for (int i = myPosition; i < trackLength; i++) {
            if (gpu.charAt(i) == '#') {
                futureObstacles.add(i);
            }
        }
    }

    private static String chooseDirection(String gpu, int myPosition, int myStunCount, int gameIdx) {
        // If stunned, do nothing
        if (isStunned && gameIdx == playerIdx) {
            return "LEFT";
        }

        // Check the track length
        int trackLength = gpu.length();

        // Check if the player is stunned in this game
        if (gameIdx == playerIdx && myStunCount > 0) {
            isStunned = true;
            return "LEFT";
        }

        // Check up to myPosition + 3 for potential obstacles
        for (int i = myPosition + 1; i <= myPosition + 3 && i < trackLength; i++) {
            if (obstacles.contains(i)) {
                // If there's an obstacle, decide the best move
                if (i + 1 < trackLength && gpu.charAt(i + 1) == '#') {
                    return "RIGHT";
                } else if (i + 2 < trackLength && gpu.charAt(i + 2) == '#') {
                    return "DOWN";
                } else {
                    return "UP";
                }
            }
        }

        // If we can move up to myPosition + 3 without obstacles, move RIGHT
        if (myPosition + 3 < trackLength && gpu.charAt(myPosition + 3) != '#') {
            return "RIGHT";
        }

        // Default action: move one space to the left (LEFT)
        return "LEFT";
    }
}