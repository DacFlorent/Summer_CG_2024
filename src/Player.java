import java.util.*;
import java.util.stream.Collectors;

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

            // listes de stockage GPUs et Reg positions
            List<String> gpus = new ArrayList<>();
            List<int[]> registers = new ArrayList<>();

            for (int i = 0; i < nbGames; i++) {
                String gpu = in.next();
                int reg0 = in.nextInt(); // position player 1
                int reg1 = in.nextInt(); // position player 2
                int reg2 = in.nextInt(); // position player 3
                int reg3 = in.nextInt(); // stun player 1
                int reg4 = in.nextInt(); // stun player 2
                int reg5 = in.nextInt(); // stun player 3
                int reg6 = in.nextInt(); // useless

                gpus.add(gpu);
                registers.add(new int[]{reg0, reg1, reg2, reg3, reg4, reg5, reg6});

                // debug System.err.println("message")
                System.err.println("GPUs : " + gpus);
                System.err.println("Registers : " + registers);
            }
            in.nextLine();

            // Boucle de calcul par itération
            for (int i = 0; i < nbGames; i++) {
                String gpu = gpus.get(i);
                int[] reg = registers.get(i);
                List<Action> actionsPossibles = calculatePossibleACtions(gpu, reg);

                System.err.println("Game " + i + "GPU = " + gpu + ", Actions possibles = " + actionsPossibles);
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("LEFT");
        }
    }

    // Methode de calcul par itération
    public static List<Action> calculatePossibleACtions(String gpu, int[] reg) {
        List<Action> actions = new ArrayList<>();

        int positionPlayer1 = reg[0];
        int positionPlayer2 = reg[1];
        int positionPlayer3 = reg[2];
        int stunPlayer1 = reg[3];
        int stunPlayer2 = reg[4];
        int stunPlayer3 = reg[5];

        // distance du prochain #
        int distanceProchainObstacle = positionPlayer1;

        if (distanceProchainObstacle > 0) {
            actions.add(Action.UP);
        } else {
            actions.add(Action.DOWN);
            actions.add(Action.LEFT);
            actions.add(Action.RIGHT);
        }

        return actions;

    }

    private static String chooseDirection(String[] gpus, int[][] registers) {
        // Determine the games to consider (the 3 games with the least obstacles)
        List<Integer> gamesToConsider = getGamesToConsider(gpus);

        // Check each game's state to decide the action
        for (int i : gamesToConsider) {
            String gpu = gpus[i];
            int position = registers[i][0];
            int stunCount = registers[i][3];

            // If the game is stunned, skip it
            if (stunCount > 0) {
                continue;
            }

            // Check if there's an obstacle in the next cell
            if (position < gpu.length() - 1 && gpu.charAt(position + 1) == '#') {
                // Try to jump over the obstacle
                registers[i][0]++; // Move position forward

                // Check if we can do a double jump (obstacle two cells ahead)
                if (position < gpu.length() - 2 && gpu.charAt(position + 2) == '#') {
                    registers[i][0]++; // Move position forward again
                    return "UP";
                } else {
                    return "UP";
                }
            }

            // Check if there's an obstacle two cells ahead for double jump
            if (position < gpu.length() - 2 && gpu.charAt(position + 2) == '#') {
                registers[i][0] += 2; // Move position forward
                return "UP";
            }
        }

        // Default action: move one space to the right
        return "RIGHT";
    }

    private static List<Integer> getGamesToConsider(String[] gpus) {
        // Find the games with the least obstacles
        List<Integer> gamesToConsider = new ArrayList<>();
        int[] obstacleCounts = new int[gpus.length];

        // Calculate obstacle counts for each game
        for (int i = 0; i < gpus.length; i++) {
            String gpu = gpus[i];
            int obstacleCount = 0;

            // Count obstacles in the game
            for (int j = 0; j < gpu.length(); j++) {
                if (gpu.charAt(j) == '#') {
                    obstacleCount++;
                }
            }

            obstacleCounts[i] = obstacleCount;
        }

        // Find indices of games with the least obstacles
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE, min3 = Integer.MAX_VALUE;
        int idx1 = -1, idx2 = -1, idx3 = -1;

        for (int i = 0; i < obstacleCounts.length; i++) {
            if (obstacleCounts[i] < min1) {
                min3 = min2;
                idx3 = idx2;
                min2 = min1;
                idx2 = idx1;
                min1 = obstacleCounts[i];
                idx1 = i;
            } else if (obstacleCounts[i] < min2) {
                min3 = min2;
                idx3 = idx2;
                min2 = obstacleCounts[i];
                idx2 = i;
            } else if (obstacleCounts[i] < min3) {
                min3 = obstacleCounts[i];
                idx3 = i;
            }
        }

        // Add the indices of the 3 games to consider
        gamesToConsider.add(idx1);
        gamesToConsider.add(idx2);
        gamesToConsider.add(idx3);

        return gamesToConsider;
    }

    String message;
    Action action;
    int[][] medals;

    public Player() {
    }

    public void init(int gameCount) {
        medals = new int[gameCount][3];
    }

    public int getPoints() {
        int p = 1;
        for (int i = 0; i < medals.length; ++i) {
            p *= (3 * medals[i][0] + medals[i][1]);
        }
        return p;
    }

    public int getExpectedOutputLines() {
        return 1;
    }

    public void reset() {
        this.message = null;
        this.action = null;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public void setAction(Action button) {
        this.action = button;
    }

    public Action getAction() {
        return action;
    }

    public int[] getMedalsTotal() {
        int[] total = new int[3];
        for (int i = 0; i < medals.length; ++i) {
            int golds = medals[i][0];
            int silvers = medals[i][1];
            int bronzes = medals[i][2];
            total[0] += golds;
            total[1] += silvers;
            total[2] += bronzes;
        }
        return total;
    }

    public String getScoreText() {
        List<String> minigameScores = new ArrayList<>(medals.length);
        for (int i = 0; i < medals.length; ++i) {
            int golds = medals[i][0];
            int silvers = medals[i][1];
            if (golds == 0 && silvers > 1) {
                minigameScores.add(String.format("%d🥈", silvers));
            } else if (golds > 0 && silvers == 0) {
                minigameScores.add(String.format("%d🥇", golds));
            } else if (golds == 0 && silvers == 0) {
                minigameScores.add("0");
            } else {
                minigameScores.add(String.format("%d🥇+%d🥈", golds, silvers));
            }
        }
        return minigameScores.stream().collect(Collectors.joining(" * ")) + " = " + getPoints();
    }
}

enum Action {
    UP, DOWN, LEFT, RIGHT
}
