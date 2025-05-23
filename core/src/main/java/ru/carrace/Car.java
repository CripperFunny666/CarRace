package ru.carrace;
import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;
import com.badlogic.gdx.utils.TimeUtils;
public class Car extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;

    public Car(float x, float y) {
        super(x, y);
        width = height = 200;
        type = 4;
    }

    @Override
    public void move() {
        super.move();
        changePhase();
        outOfScreen();
    }

    private void outOfScreen(){
        if(x<width/2){
            vx = 0;
            x = width/2;
        }
        if(x>SCR_WIDTH-width/2){
            vx = 0;
            x = SCR_WIDTH-width/2;
        }
        if(y<height/2){
            vy = 0;
            y = height/2;
        }
        if(y>SCR_HEIGHT-height/2){
            vy = 0;
            y = SCR_HEIGHT-height/2;
        }
    }

    private void changePhase(){
        if(TimeUtils.millis()>timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }
}
