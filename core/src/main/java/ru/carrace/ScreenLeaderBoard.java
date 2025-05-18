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
    private Main main;

    Texture imgBackGround;

    SunButton btnSwitchGlobal;
    SunButton btnClear;
    SunButton btnBack;

    Player[] players;
    private boolean showGlobalRecords;

    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font70 = main.font70white;
        font50 = main.font50white;
        players = main.screenGame.players;

        imgBackGround = new Texture("space2.png");

        btnSwitchGlobal = new SunButton("Local", font70, 1350);
        btnClear = new SunButton("Clear", font70, 350);
        btnBack = new SunButton("Back", font70, 150);
    }

    @Override
    public void show() {
        Gdx.graphics.setForegroundFPS(10); // Устанавливаем лимит в 10 FPS
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnSwitchGlobal.hit(touch)){
                showGlobalRecords = !showGlobalRecords;
                if(showGlobalRecords){
                    btnSwitchGlobal.setText("Global");
                    main.screenGame.loadFromInternetDB();
                } else {
                    btnSwitchGlobal.setText("Local");
                }
            }
            if (btnClear.hit(touch) && !showGlobalRecords){
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
        btnSwitchGlobal.font.draw(batch, btnSwitchGlobal.text, btnSwitchGlobal.x, btnSwitchGlobal.y);
        font50.draw(batch, "score", 500, 1200, 200, Align.right, false);
        font50.draw(batch, "coins", 650, 1200, 200, Align.right, false);
        if(showGlobalRecords){
            for (int i = 0; i < Math.min(main.screenGame.getDb().size(), players.length); i++) {
                font50.draw(batch, i + 1 + "", 100, 1100 - i * 70);
                font50.draw(batch, main.screenGame.getDb().get(i).name, 200, 1100 - i * 70);
                font50.draw(batch, main.screenGame.getDb().get(i).score + "", 500, 1100 - i * 70, 200, Align.right, false);
                font50.draw(batch, main.screenGame.getDb().get(i).coins + "", 650, 1100 - i * 70, 200, Align.right, false);
            }
        } else {
            for (int i = 0; i < players.length; i++) {
                font50.draw(batch, i + 1 + "", 100, 1100 - i * 70);
                font50.draw(batch, players[i].name, 200, 1100 - i * 70);
                font50.draw(batch, players[i].score + "", 500, 1100 - i * 70, 200, Align.right, false);
                font50.draw(batch, players[i].coins + "", 650, 1100 - i * 70, 200, Align.right, false);
            }
        }
        if(!showGlobalRecords) btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
