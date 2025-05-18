package ru.carrace;

import static ru.carrace.Main.*;
import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

public class Shot extends SpaceObject{

    public Shot(float x, float y) {
        super(x, y);
        width = 50;
        height = 150;
        vy = 20f;
    }

    public boolean outOfScreen(){
        return y<-height/2 || y>SCR_HEIGHT+height/2 || x<-width/2 || x>SCR_WIDTH+width/2;
    }
}
