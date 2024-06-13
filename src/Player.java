class Player {

    static java.util.List<Integer> obstacles = new java.util.ArrayList<>();
    static boolean isStunned = false;

    public static void main(String args[]) {
        java.util.Scanner in = new java.util.Scanner(System.in);

        int playerIdx = in.nextInt();
        int nbGames = in.nextInt();
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

            // Pre-calculate obstacles
            calculateObstacles(gpu[playerIdx]);

            // Choose the action for the current game state
            String direction = chooseDirection(playerIdx, gpu[playerIdx], registers[playerIdx][0], registers[playerIdx][3], 0);

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

    private static String chooseDirection(int playerIdx, String gpu, int myPosition, int myStunCount, int gameIdx) {
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