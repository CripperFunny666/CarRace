package ru.carrace;

import static ru.carrace.Main.*;
import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

public class Space extends SpaceObject{
    public Space(float x, float y){
        super(x, y);
        width = SCR_WIDTH;
        height = SCR_HEIGHT+6;
        vy = -3;
    }

    @Override
    public void move() {
        super.move();
        outOfScreen();
    }

    private void outOfScreen(){
        if(y<-SCR_HEIGHT) y = SCR_HEIGHT;
    }

    public void move(float gameSpeed) {
        y -= 5 * gameSpeed;
        if(y <= -SCR_HEIGHT) y = SCR_HEIGHT;
    }
}
