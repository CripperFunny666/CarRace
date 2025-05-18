package ru.carrace;

public class Player {
    public String name;
    public int score;
    public int coins;

    public Player() {
        name = "Noname";
        score = 0;
        coins = 0;
    }

    public void clear() {
        name = "Noname";
        score = 0;
        coins = 0;
    }

    public void clone(Player p) {
        name = p.name;
        score = p.score;
        coins = p.coins;
    }
}
