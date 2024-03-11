package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Joystick extends Actor {
    public static final float JOYSTICK_RADIUS = 1;

    private boolean isTouched = false;
    private float joystickX;
    private float joystickY;
    private float curX;
    private float curY;
    private Texture img;
    private boolean follow;
    private float posX;
    private float posY;
    private float width;
    private float height;

    public Joystick(Texture img, float posX, float posY, float width, float height, boolean follow) {
        this.img = img;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.follow = follow;
        setBounds(posX, posY, width, height);
        addListener(new JoystickInputListener(this));
    }

    public void touch(float x, float y) {
        isTouched = true;
        joystickX = x;
        joystickY = y;
        curX = x;
        curY = y;
    }

    public void untouch() {
        isTouched = false;
    }

    public void drag(float x, float y) {
        curX = x;
        curY = y;
        float dx = curX - joystickX;
        float dy = curY - joystickY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (follow && length > JOYSTICK_RADIUS) {
            float k = (length - JOYSTICK_RADIUS) / length;
            joystickX += dx * k;
            joystickY += dy * k;
        }
    }

    public Vector2 getDelta() {
        if (!isTouched) {
            return new Vector2();
        }
        float dx = curX - joystickX;
        float dy = curY - joystickY;
        return new Vector2(dx, dy);
    }

    public Vector2 getDirection() {
        Vector2 delta = getDelta();
        if (delta.len() == 0) {
            return new Vector2();
        }
        return delta.scl(1f / delta.len());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isTouched) {
            batch.draw(img,
                    posX + joystickX - JOYSTICK_RADIUS,
                    posY + joystickY - JOYSTICK_RADIUS,
                    JOYSTICK_RADIUS * 2,
                    JOYSTICK_RADIUS * 2);
        }
    }
}
