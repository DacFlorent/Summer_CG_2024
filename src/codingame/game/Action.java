package com.codingame.game;

public class Action {
    // Définissez votre classe Action ici
    private String type;

    public Action(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}