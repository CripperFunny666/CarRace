package ru.carrace;

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

public class ScreenLeaderBoard implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    private BitmapFont font50;
    private BitmapFont font50new;
    private Main main;

    Texture imgBackGround;

    SunButton btnClear;
    SunButton btnBack;

    Player[] players;

    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font70 = main.font70white;
        font50 = main.font50white;
        font50new = main.font50new;
        players = main.screenGame.players;

        imgBackGround = new Texture("scr2.png");

        btnClear = new SunButton("Clear", font70, 400);
        btnBack = new SunButton("Back", font70, 150);
    }

    @Override
    public void show() {
        Gdx.graphics.setForegroundFPS(10); // Устанавливаем лимит в 10 FPS
        players = main.screenGame.players;
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (btnClear.hit(touch)){
                main.screenGame.clearTableOfRecords();
                main.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font70.draw(batch, "LEADERBOARD", 0, 1500, SCR_WIDTH, Align.center, false);
        font50new.draw(batch, "score", 450, 1200, 200, Align.right, false);
        font50new.draw(batch, "name", 133, 1200, 200, Align.right, false);
        font50new.draw(batch, "coins", 640, 1200, 200, Align.right, false);

        for (int i = 0; i < players.length; i++) {
            font50new.draw(batch, i + 1 + "", 100, 1100 - i * 70);
            font50new.draw(batch, players[i].name, 200, 1100 - i * 70);
            font50new.draw(batch, players[i].score + "", 450, 1100 - i * 70, 200, Align.right, false);
            font50new.draw(batch, players[i].coins + "", 600, 1100 - i * 70, 200, Align.right, false);
        }

        btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
