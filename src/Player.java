package com.codingame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {

    String message;
    Action action;
    int[][] medals;
    Map<String, Integer> registerPositions;
    List<String> trackState;

    public Player() {
        registerPositions = new HashMap<>();
        trackState = new ArrayList<>();
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

    @Override
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

    /**
     * Choisit une action pour le mini-jeu de course de haies.
     *
     * @param gpu  une chaîne représentant l'état de la piste avec les registres
     * @param reg0 à reg6  les positions des registres
     * @return l'action choisie (UP, RIGHT, WAIT)
     */

    public String chooseAction(String[] gpu, int reg0, int reg1, int reg2, int reg3, int reg4, int reg5, int reg6) {
        // Effacer la map et la liste pour ce tour
        registerPositions.clear();
        trackState.clear();

        // Remplir la map avec les positions actuelles des registres
        registerPositions.put("reg0", reg0); // position du player 1
        registerPositions.put("reg1", reg1); // position du player 2
        registerPositions.put("reg2", reg2); // position du player 3
        registerPositions.put("reg3", reg3); // Stun du player 1
        registerPositions.put("reg4", reg4); // Stun du player 2
        registerPositions.put("reg5", reg5); // Stun du player 3
        registerPositions.put("reg6", reg6); // Useless

        // Remplir la liste avec l'état actuel de la piste
        for (String state : gpu) {
            trackState.add(state);
        }

        System.out.println(GetTrackStateVisual());

        // Logique pour le mini-jeu de course de haies
        int currentPosition = registerPositions.get("reg0"); // Supposons que reg0 est la position actuelle à vérifier
        if (currentPosition < trackState.size() && trackState.get(currentPosition).charAt(currentPosition) == '#') {
            // Il y a une haie, sauter par-dessus
            return "UP";
        } else if (currentPosition < trackState.size() && trackState.get(currentPosition).equals("GAME_OVER")) {
            // Tour de réinitialisation
            return "WAIT";
        } else {
            // Avancer normalement
            return "RIGHT";
        }
    }

    /**
     * Retourne une représentation visuelle de l'état de la piste.
     *
     * @return une chaîne représentant visuellement l'état de la piste
     */
    public String getTrackStateVisual() {
        StringBuilder visual = new StringBuilder();
        for (String state : trackState) {
            visual.append(state).append("\n");
        }
        return visual.toString();
    }
}

