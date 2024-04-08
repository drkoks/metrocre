package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Joystick extends Actor {
    public static final float JOYSTICK_RADIUS = 1;
    private final float sensitivity;
    private boolean isTouched = false;
    private float joystickX;
    private float joystickY;
    private float curX;
    private float curY;
    private final Texture img;
    private final boolean follow;
    private final float posX;
    private final float posY;

    public Joystick(Texture img, float posX, float posY, float width, float height, float sensitivity, boolean follow) {
        this.img = img;
        this.posX = posX;
        this.posY = posY;
        this.sensitivity = sensitivity;
        this.follow = follow;
        setBounds(posX, posY, width, height);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Joystick.this.touch(x, y);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Joystick.this.untouch();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Joystick.this.drag(x, y);
            }
        });
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
        Vector2 delta = new Vector2(curX - joystickX, curY - joystickY);
        if (delta.len() < sensitivity * JOYSTICK_RADIUS) {
            delta.x = 0;
            delta.y = 0;
        }
        return delta;
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
