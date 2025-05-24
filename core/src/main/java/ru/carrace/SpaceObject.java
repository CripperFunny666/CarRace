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
        float dx = Math.abs(x - o.x);
        float dy = Math.abs(y - o.y);
        float maxStep = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        
        float collisionWidth = width/2 + o.width/2 + maxStep;
        float collisionHeight = height/2 + o.height/2 + maxStep;
        
        return dx < collisionWidth && dy < collisionHeight;
    }
}
