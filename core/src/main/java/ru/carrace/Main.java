package ru.carrace;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 900;
    public static final float SCR_HEIGHT = 1600;
    public static final int KEYBOARD = 0, ACCELEROMETER = 1;
    public static final boolean LEFT = false, RIGHT = true;
    public static int controls = KEYBOARD;
    public static boolean isSoundOn = true;
    public static float fadeAlpha = 0f;
    public static boolean isFading = false;
    public static float fadeSpeed = 2f;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font70white;
    public BitmapFont font70gray;
    public BitmapFont font50white;
    public Music music;
    public Texture fadeTexture;

    Player player;
    ScreenMenu screenMenu;
    ScreenGame screenGame;
    ScreenSettings screenSettings;
    ScreenLeaderBoard screenLeaderBoard;
    ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        font70white = new BitmapFont(Gdx.files.internal("fnt/gu.fnt"));
        font70gray = new BitmapFont(Gdx.files.internal("fnt/gugray.fnt"));
        font50white = new BitmapFont(Gdx.files.internal("fnt/gu50white.fnt"));
        fadeTexture = new Texture("white.png");

        music = Gdx.audio.newMusic(Gdx.files.internal("main.mp3"));
        music.setLooping(true);
        if (isSoundOn) {
            music.play();
        }

        player = new Player();
        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenLeaderBoard = new ScreenLeaderBoard(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenMenu);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font70white.dispose();
        font70gray.dispose();
        music.dispose();
        fadeTexture.dispose();
    }

    public void fadeToScreen(Screen screen) {
        isFading = true;
        fadeAlpha = 0f;
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Gdx.app.postRunnable(() -> {
                    setScreen(screen);
                    if (isSoundOn) {
                        music.play();
                    } else {
                        music.stop();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
