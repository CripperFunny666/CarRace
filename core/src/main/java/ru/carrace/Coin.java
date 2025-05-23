package ru.carrace;

import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends SpaceObject {
    private static final float COIN_SIZE = 80;
    private static final float COIN_SPEED = 5;
    private static final int COIN_VALUE = 1;
    private static final float COIN_HITBOX_WIDTH = 90;
    private static final float COIN_HITBOX_HEIGHT = 90;

    public static TextureRegion imgCoin;

    public Coin() {
        width = height = COIN_SIZE;
        x = (float) (Math.random() * (SCR_WIDTH - 2*width) + width);
        y = SCR_HEIGHT+height;
        vy = COIN_SPEED;
        vx = 0;
    }

    public int getValue() {
        return COIN_VALUE;
    }

    public boolean outOfScreen() {
        return y < -height;
    }

    public void move(float gameSpeed) {
        y -= vy * gameSpeed;
    }

    @Override
    public boolean overlap(SpaceObject o) {
        return Math.abs(x - o.x) < COIN_HITBOX_WIDTH && Math.abs(y - o.y) < COIN_HITBOX_HEIGHT;
    }
}
