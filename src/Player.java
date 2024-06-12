import java.util.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int playerIdx = in.nextInt();
        int nbGames = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine(); // Consume newline character
        }

        // game loop
        while (true) {
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

            // Choose the action for the current game state
            String direction = chooseDirection(playerIdx, gpu[0], registers[playerIdx][0], registers[playerIdx][3]);

            // Output the chosen direction
            System.out.println(direction);
        }
    }

    private static String chooseDirection(int playerIdx, String gpu, int myPosition, int myStunCount) {
        // If stunned, do nothing
        if (myStunCount > 0) {
            return "LEFT";
        }

        // Check the track length
        int trackLength = gpu.length();

        // Check the next cells for obstacles
        if (myPosition < trackLength - 1) {
            char nextCell = gpu.charAt(myPosition + 1);
            if (nextCell == '#') {
                // If next cell is a hurdle, jump over it (UP)
                if (myPosition + 2 < trackLength) {
                    return "UP";
                }
            } else if (myPosition + 2 < trackLength && gpu.charAt(myPosition + 2) == '#') {
                // If the cell after next is a hurdle, move two spaces to the right (RIGHT)
                return "RIGHT";
            } else if (myPosition + 3 < trackLength && gpu.charAt(myPosition + 3) == '#') {
                // If the cell two spaces after next is a hurdle, move two spaces down (DOWN)
                return "DOWN";
            }
        }

        // Default action: move one space to the left (LEFT)
        return "LEFT";
    }
}

enum Action {
    UP, DOWN, LEFT, RIGHT
}