package ru.carrace;

import static ru.carrace.Main.*;
import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenAbout implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private Main main;

    Texture imgBackGround;

    SunButton btnBack;
    private String text = "Это простая 2D игра\n" +
        "про машинки.\n" +
        "В игре присутствует \n" +
        "управление стрелочками\n" +
        "и с помощью \n" +
        "акселерометра.\n" +
        "главная задача - это\n" +
        "продержаться\n" +
        "как можно больше\n" +
        "времени на дороге.\n" +
        "Удачи!";

    public ScreenAbout(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70white;

        imgBackGround = new Texture("scr3.png");

        btnBack = new SunButton("Back", font, 150);
    }


    @Override
    public void show() {
        Gdx.graphics.setForegroundFPS(10);
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, "ABOUT", 0, 1500, SCR_WIDTH, Align.center, false);
        font.draw(batch, text, 0, 1200, SCR_WIDTH, Align.center, false);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
