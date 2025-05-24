package ru.carrace;

import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

public class SpaceObject {
    public float x, y;
    public float width, height;
    public float vx, vy;
    public int type;
    private float lastX, lastY;
    private float deltaX, deltaY;

    public SpaceObject(float x, float y) {
        this.x = x;
        this.y = y;
        lastX = x;
        lastY = y;
    }

    public SpaceObject() {
    }

    public void move(){
        lastX = x;
        lastY = y;
        x += vx;
        y += vy;
        deltaX = x - lastX;
        deltaY = y - lastY;
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }

    public boolean overlap(SpaceObject o){
    return  Math.abs(x-o.x)< 230/2 && Math.abs(y - o.y) < 350/2;
    }
}
