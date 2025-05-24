package ru.carrace;

import static ru.carrace.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class SunButton {
    String text;
    BitmapFont font;
    float x, y;
    float width, height;

    public SunButton(String text, BitmapFont font, float x, float y) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public SunButton(String text, BitmapFont font, float y) {
        this.text = text;
        this.font = font;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        x = SCR_WIDTH/2 - width/2;
    }

    public void setFont(BitmapFont font){
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public void setText(String text){
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
    }

    boolean hit(float tx, float ty){
        boolean hit = tx >= x && tx <= x + width && ty <= y && ty >= y - height;
        if (hit) {
            Gdx.app.log("Button", "Hit " + text + " at (" + tx + "," + ty + ")");
            Gdx.app.log("Button", "Button bounds: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
        }
        return hit;
    }

    boolean hit(Vector3 t){
        return hit(t.x, t.y);
    }
}
