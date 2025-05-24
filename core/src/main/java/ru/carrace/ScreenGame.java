package ru.carrace;

import static ru.carrace.Main.ACCELEROMETER;
import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;
import static ru.carrace.Main.controls;
import static ru.carrace.Main.isSoundOn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font50, font70,font50new;
    private Main main;

    Texture imgBackGround;
    Texture imgShipsAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion[][] imgEnemy = new TextureRegion[4][12];
    TextureRegion[] imgShot = new TextureRegion[4];
    TextureRegion[][] imgFragment = new TextureRegion[5][16];

    Sound sndBlaster;
    Sound sndExplosion;
    Sound sndCoin;
    Sound sndCar;
    Music music;

    SunButton btnBack;
    SunButton btnRestart;

    Space[] space = new Space[2];
    Car car;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    List<Coin> coins = new ArrayList<>();
    Player[] players = new Player[10];


    private long timeLastSpawnEnemy, timeSpawnEnemyInterval = 1500;
    private long timeLastSpawnCoin, timeSpawnCoinInterval = 2000;
    private long timeLastShoot, timeShootInterval = 5000;
    private int nFragments = 36;
    private boolean gameOver;
    private long explosionStartTime; // Время начала взрыва
    private boolean explosionAnimationFinished; // Флаг завершения анимации взрыва
    private static final long EXPLOSION_DURATION = 2000; // Длительность анимации взрыва в миллисекундах

    // Переменные для ускорения
    private float gameSpeed = 1.0f;
    private float speedIncreaseRate = 0.0001f; // Скорость увеличения ускорения
    private float maxGameSpeed = 6.0f; // Максимальная скорость игры

    // Новые переменные для ScoreCounter
    private long lastScoreTime; // Время последнего добавления очка
    private int S = 0; // Текущий счёт (очки)


    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font70 = main.font70white;
        font50 = main.font50white;
        font50new = main.font50new;

        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndCoin = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
        sndCar = Gdx.audio.newSound(Gdx.files.internal("carsound.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
        music.setLooping(true);

        imgBackGround = new Texture("scrgame.png");
        imgShipsAtlas = new Texture("ships_atlas.png");
        imgShotsAtlas = new Texture("shots.png");
        Coin.imgCoin = new TextureRegion(new Texture("coin.png"));
        for (int i = 0; i < imgShip.length; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i < 7 ? i : 12 - i) * 400, 0, 400, 400);
        }
        for (int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgShipsAtlas, (i < 7 ? i : 12 - i) * 400, (j + 1) * 400, 400, 400);
            }
        }
        for (int i = 0; i < imgShot.length; i++) {
            imgShot[i] = new TextureRegion(imgShotsAtlas, i * 100, 0, 100, 350);
        }
        int k = (int) Math.sqrt(imgFragment[0].length);
        int size = 400 / k;
        for (int j = 0; j < imgFragment.length; j++) {
            for (int i = 0; i < imgFragment[j].length; i++) {
                if (j == imgFragment.length - 1)
                    imgFragment[j][i] = new TextureRegion(imgShip[0], i % k * size, i / k * size, size, size);
                else
                    imgFragment[j][i] = new TextureRegion(imgEnemy[j][0], i % k * size, i / k * size, size, size);
            }
        }

        btnBack = new SunButton("x", font70, SCR_WIDTH - 60, SCR_HEIGHT - 5);
        btnRestart = new SunButton("restart", font70, SCR_WIDTH/2 - 120, SCR_HEIGHT/2 - 450);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        loadTableOfRecords();

    }

    @Override
    public void show() {
        Gdx.graphics.setForegroundFPS(60);
        Gdx.input.setInputProcessor(new SunInputProcessor());
        gameStart();
        main.music.stop();
        if (isSoundOn) {
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        ScoreCounter(); // Обновляем счёт каждую секунду

        // Увеличиваем скорость игры со временем
        if (!gameOver) {
            gameSpeed = Math.min(gameSpeed + speedIncreaseRate, maxGameSpeed);
        }

        // Проверяем завершение анимации взрыва
        if (gameOver && !explosionAnimationFinished) {
            if (TimeUtils.millis() - explosionStartTime >= EXPLOSION_DURATION) {
                explosionAnimationFinished = true;
                if (main.player.score > players[players.length - 1].score) {
                    players[players.length - 1].clone(main.player);
                    sortTableOfRecords();
                    saveTableOfRecords();
                }
            }
        }

        // Касания
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (btnBack.hit(touch)) {
                if (isSoundOn) {
                    sndCar.stop();
                }
                main.setScreen(main.screenMenu);
            }
            if (gameOver && btnRestart.hit(touch) && explosionAnimationFinished) {
                gameStart();
            }
        }
        if (controls == ACCELEROMETER) {
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();

            // Нормализуем значения акселерометра
            car.vx = -accelX * 5; // Увеличиваем чувствительность
            car.vy = -accelY * 5;

            // Ограничиваем максимальную скорость
            car.vx = Math.max(-8, Math.min(8, car.vx));
            car.vy = Math.max(-4, Math.min(7, car.vy));
        }

        // События
        for (Space s : space) {
            s.move(gameSpeed); // Передаем текущую скорость игры
        }
        spawnEnemy();
        spawnCoin();
        if (!gameOver) {
            car.move();
        }
        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).move(gameSpeed); // Передаем текущую скорость игры
            if (enemies.get(i).outOfScreen()) {
                enemies.remove(i);
            }
            if (enemies.get(i).overlap(car)) {
                spawnFragments(enemies.get(i));
                enemies.remove(i);
                gameOver();
            }
        }
        for (int i = shots.size() - 1; i >= 0; i--) {
            shots.get(i).move();
            if (shots.get(i).outOfScreen()) {
                shots.remove(i);
                break;
            }
            for (int j = enemies.size() - 1; j >= 0; j--) {
                if (shots.get(i).overlap(enemies.get(j))) {
                    if (isSoundOn) sndExplosion.play();
                    shots.remove(i);
                    if (--enemies.get(j).hp == 0) {
                        spawnFragments(enemies.get(j));
                        if (!gameOver) {
                            main.player.score = enemies.get(j).price;
                        }
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }

        // Оптимизированная обработка фрагментов
        if (!fragments.isEmpty()) {
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                fragment.move();
                if (fragment.outOfScreen()) {
                    fragments.remove(i);
                }
            }
        }

        for (int i = coins.size() -1; i >= 0; i--) {
            coins.get(i).move(gameSpeed); // Передаем текущую скорость игры
            if (coins.get(i).outOfScreen()) {
                coins.remove(i);
                continue;
            }
            if (coins.get(i).overlap(car)) {
                if (isSoundOn) sndCoin.play();
                main.player.coins += coins.get(i).getValue();
                coins.remove(i);
            }
        }

        // Отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Space s : space) batch.draw(imgBackGround, s.x, s.y, s.width, s.height);

        // Оптимизированная отрисовка фрагментов
        if (!fragments.isEmpty()) {
            for (Fragment f : fragments) {
                batch.draw(imgFragment[f.type][f.num], f.scrX(), f.scrY(), f.width / 2, f.height / 2, f.width, f.height, 1, 1, f.rotation);
            }
        }
        for (Enemy e : enemies) {
            batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for (Shot s : shots) {
            batch.draw(imgShot[0], s.scrX(), s.scrY(), s.width, s.height);
        }
        batch.draw(imgShip[car.phase], car.scrX(), car.scrY(), car.width, car.height);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        font50new.draw(batch, "score:" + S, 15, 1590); // Выводим текущий счёт S
        font50new.draw(batch, "coins:" + main.player.coins, 15, 1540);
        font50new.draw(batch, "speed:" + String.format("%.1f", gameSpeed), 15, 1490); // Показываем текущую скорость
        if (gameOver) {
            if (explosionAnimationFinished) {
                font70.draw(batch, "GAME OVER", 0, 1300, SCR_WIDTH, Align.center, true);
                font50.draw(batch, "score", 450, 1200, 200, Align.right, false);
                font50.draw(batch, "name", 133, 1200, 200, Align.right, false);
                font50.draw(batch, "coins", 640, 1200, 200, Align.right, false);
                for (int i = 0; i < players.length; i++) {
                    font50.draw(batch, i + 1 + "", 100, 1100 - i * 70);
                    font50.draw(batch, players[i].name, 200, 1100 - i * 70);
                    font50.draw(batch, players[i].score + "", 450, 1100 - i * 70, 200, Align.right, false);
                    font50.draw(batch, players[i].coins + "", 600, 1100 - i * 70, 200, Align.right, false);
                }
                btnRestart.font.draw(batch, btnRestart.text, btnRestart.x, btnRestart.y);
            } else {
                font70.draw(batch, "You have been crash!", 0, 1400, SCR_WIDTH, Align.center, true);
            }
        }
        for (Coin c : coins) {
            batch.draw(Coin.imgCoin, c.scrX(), c.scrY(), c.width, c.height);
        }
        batch.end();
    }

    // Метод ScoreCounter (добавляет 1 очко каждую секунду)
    private void ScoreCounter() {
        if (!gameOver) { // Добавляем очки только во время игры
            long currentTime = TimeUtils.millis();
            if (currentTime - lastScoreTime >= 100) { // Прошла 1 секунда
                S++; // Увеличиваем счёт
                main.player.score = S;
                lastScoreTime = currentTime; // Обновляем время
            }
        }
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
        music.stop();
        if (isSoundOn) {
            main.music.play();
        } else {
            main.music.stop();
        }
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        imgShipsAtlas.dispose();
        imgShotsAtlas.dispose();
        sndBlaster.dispose();
        sndExplosion.dispose();
        sndCoin.dispose();
        sndCar.dispose();
        music.dispose();
    }

    private void spawnEnemy() {
        if (TimeUtils.millis() > timeLastSpawnEnemy + timeSpawnEnemyInterval) {
            enemies.add(new Enemy());
            timeLastSpawnEnemy = TimeUtils.millis();
        }
    }

    private void spawnFragments(SpaceObject o) {
        // Уменьшаем количество фрагментов при взрыве
        int fragmentsToSpawn = Math.min(nFragments, 9); // Ограничиваем максимальное количество фрагментов
        for (int i = 0; i < fragmentsToSpawn; i++) {
            fragments.add(new Fragment(o.x, o.y, o.type, imgFragment[0].length));
        }
    }

    private void spawnCoin() {
        if (TimeUtils.millis() > timeLastSpawnCoin + timeSpawnCoinInterval) {
            coins.add(new Coin());
            timeLastSpawnCoin = TimeUtils.millis();
        }
    }

    private void gameStart() {
        gameOver = false;
        explosionAnimationFinished = false;
        gameSpeed = 1.0f; // Сбрасываем скорость игры
        car = new Car(SCR_WIDTH / 2, 200);
        enemies.clear();
        fragments.clear();
        shots.clear();
        coins.clear();
        main.player.score = 0;
        main.player.coins = 0;
        S = 0;
        lastScoreTime = TimeUtils.millis();
        if (isSoundOn) {
            sndCar.loop();
        }
    }

    private void gameOver() {
        if (isSoundOn) {
            sndExplosion.play();
            sndCar.stop();
        }
        spawnFragments(car);
        car.x = -10000;
        gameOver = true;
        explosionStartTime = TimeUtils.millis();
        explosionAnimationFinished = false;
    }

    private void sortTableOfRecords() {
        for (int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length - 1; i++) {
                if (players[i].score < players[i + 1].score) {
                    Player tmp = players[i];
                    players[i] = players[i + 1];
                    players[i + 1] = tmp;
                }
            }
        }
    }

    public void saveTableOfRecords() {
        Preferences prefs = Gdx.app.getPreferences("SpaceWarRecords");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name" + i, players[i].name);
            prefs.putInteger("score" + i, players[i].score);
            prefs.putInteger("coins" + i, players[i].coins);
        }
        prefs.flush();
    }

    private void loadTableOfRecords() {
        Preferences prefs = Gdx.app.getPreferences("SpaceWarRecords");
        for (int i = 0; i < players.length; i++) {
            players[i].name = prefs.getString("name" + i, "Noname");
            players[i].score = prefs.getInteger("score" + i, 0);
            players[i].coins = prefs.getInteger("coins" + i, 0);
        }
    }

    public void clearTableOfRecords() {
        for (Player player : players) player.clear();
    }




    class SunInputProcessor implements InputProcessor {
        // Флаги для отслеживания состояния клавиш
        private boolean upPressed = false;
        private boolean downPressed = false;
        private boolean leftPressed = false;
        private boolean rightPressed = false;

        private void updateMovement() {
            // Вертикальное движение
            if (upPressed && !downPressed) {
                car.vy = 7;
            } else if (downPressed && !upPressed) {
                car.vy = -4;
            } else {
                car.vy = 0;
            }

            // Горизонтальное движение
            if (leftPressed && !rightPressed) {
                car.vx = -8;
            } else if (rightPressed && !leftPressed) {
                car.vx = 8;
            } else {
                car.vx = 0;
            }
        }

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.UP:
                    upPressed = true;
                    updateMovement();
                    return true;
                case Input.Keys.DOWN:
                    downPressed = true;
                    updateMovement();
                    return true;
                case Input.Keys.LEFT:
                    leftPressed = true;
                    updateMovement();
                    return true;
                case Input.Keys.RIGHT:
                    rightPressed = true;
                    updateMovement();
                    return true;
                case Input.Keys.SPACE:
                    if (TimeUtils.millis() > timeLastShoot + timeShootInterval) {
                        shots.add(new Shot(car.x, car.y + car.height));
                        timeLastShoot = TimeUtils.millis();
                        if (isSoundOn) sndBlaster.play();
                    }
                    return true;
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.UP:
                    upPressed = false;
                    updateMovement();
                    return true;
                case Input.Keys.DOWN:
                    downPressed = false;
                    updateMovement();
                    return true;
                case Input.Keys.LEFT:
                    leftPressed = false;
                    updateMovement();
                    return true;
                case Input.Keys.RIGHT:
                    rightPressed = false;
                    updateMovement();
                    return true;
            }
            return false;
        }

        // Остальные методы остаются без изменений
        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    public static void main(String[] args) {

    }
}
