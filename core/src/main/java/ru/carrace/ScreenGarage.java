package ru.carrace;

import static ru.carrace.Main.SCR_HEIGHT;
import static ru.carrace.Main.SCR_WIDTH;
import static ru.carrace.ScreenGame.totalCoins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenGarage implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font, fontSmall;
    private Main main;
    private Preferences prefs;

    Texture imgBackGround;
    //  Texture imgCarsAtlas;
    //  TextureRegion[] imgCars = new TextureRegion[4]; // Массив для хранения изображений машин

    SunButton btnBack;
    //  SunButton[] btnBuyCar = new SunButton[4]; // Кнопки для покупки машин
    private String text = "You have " + totalCoins + " coins";
    //   private int[] carPrices = {0, 1000, 2500, 5000}; // Цены на машины
    //   private boolean[] carsOwned = {true, false, false, false}; // Статус владения машинами

    public ScreenGarage(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70white;
        fontSmall = main.font50new;
        prefs = Gdx.app.getPreferences("CarRacePrefs");

        imgBackGround = new Texture("scr3.png");
        // imgCarsAtlas = new Texture("ships_atlas.png");

        // Загружаем изображения машин
        //  for (int i = 0; i < imgCars.length; i++) {
        //     imgCars[i] = new TextureRegion(imgCarsAtlas, i * 400, 0, 400, 400);
        //  }

        btnBack = new SunButton("Back", font, 150);

        // Создаем кнопки для покупки машин
        // for (int i = 0; i < btnBuyCar.length; i++) {
        //     btnBuyCar[i] = new SunButton("Buy", fontSmall, SCR_WIDTH/2 - 100, 800 - i * 200);
        // }

        // Загружаем статус купленных машин
        //   loadCarsStatus();
    }

    //  private void loadCarsStatus() {
    //      for (int i = 0; i < carsOwned.length; i++) {
    //          carsOwned[i] = prefs.getBoolean("car" + i + "Owned", i == 0); // Первая машина доступна по умолчанию
    //     }
    //  }

    // private void saveCarsStatus() {
    //     for (int i = 0; i < carsOwned.length; i++) {
    //         prefs.putBoolean("car" + i + "Owned", carsOwned[i]);
    //     }
    //     prefs.flush();
    // }

    @Override
    public void show() {
        Gdx.graphics.setForegroundFPS(60);
        text = "You have " + totalCoins + " coins"; // Обновляем текст при показе экрана
    }

    @Override
    public void render(float delta) {
        // касания
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (btnBack.hit(touch.x, touch.y)) {
                //     saveCarsStatus(); // Сохраняем статус перед выходом
                main.setScreen(main.screenMenu);
            }

            // Проверяем нажатия на кнопки покупки машин
            // for (int i = 0; i < btnBuyCar.length; i++) {
            //     if (!carsOwned[i] && btnBuyCar[i].hit(touch.x, touch.y)) {
            //         if (totalCoins >= carPrices[i]) {
            //             totalCoins -= carPrices[i];
            //             carsOwned[i] = true;
            //             text = "You have " + totalCoins + " coins";
            //             saveCarsStatus(); // Сохраняем после покупки
            //         }
            //      }
            //  }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        // Заголовок
        font.draw(batch, "GARAGE", 0, 1500, SCR_WIDTH, Align.center, false);
        font.draw(batch, text, 0, 1400, SCR_WIDTH, Align.center, false);

        // Отрисовка машин и информации о них
        // for (int i = 0; i < imgCars.length; i++) {
        // Рисуем машину
        //    batch.draw(imgCars[i], SCR_WIDTH/2 - 200, 1000 - i * 200, 400, 400);

        // Информация о машине
        //    String carInfo = "Car " + (i + 1);
        //    if (carsOwned[i]) {
        //       carInfo += " (Owned)";
        //   } else {
        //        carInfo += " - " + carPrices[i] + " coins";
        //    }
        //   fontSmall.draw(batch, carInfo, SCR_WIDTH/2 - 200, 950 - i * 200);

        // Кнопка покупки
        //    if (!carsOwned[i]) {
        //        btnBuyCar[i].font.draw(batch, btnBuyCar[i].text, btnBuyCar[i].x, btnBuyCar[i].y);
        //    }
        //}

        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }



     @Override
    public void pause() {
    //     saveCarsStatus();
      }

      @Override
     public void resume() {
     }

      @Override
     public void hide() {
        //saveCarsStatus();
     }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        //    imgCarsAtlas.dispose();
    }
}
