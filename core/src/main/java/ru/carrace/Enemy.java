package ru.carrace;

import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;

public class Enemy extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;

    List<Integer> numbers = List.of(120, 330, 570, 780);

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        x = numbers.get(MathUtils.random(0, 3));
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = 8;
        vx = 0;
        setupByType();
    }

    public void move(float gameSpeed) {
        // Ограничиваем максимальную скорость движения
        float maxSpeed = 15f;
        float currentSpeed = vy * gameSpeed;
        if (currentSpeed > maxSpeed) {
            currentSpeed = maxSpeed;
        }
        y -= currentSpeed;
        
        // Обновляем фазу анимации с учетом скорости
        if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            phase = (phase + 1) % nPhases;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public boolean outOfScreen(){
        return y<-height/2;
    }

    private void setupByType(){
        switch (type){
            case 0:
                vy = MathUtils.random(5f, 6f);
                hp = 1;
                price = 2;
                break;
            case 1:
                vy = MathUtils.random(5f, 8f);
                hp = 1;
                price = 5;
                break;
            case 2:
                vy = MathUtils.random(7f, 8f);
                hp = 1;
                price = 3;
                break;
            case 3:
                vy = MathUtils.random(8f, 11f);
                hp = 1;
                price = 2;
                break;
        }
    }
}
