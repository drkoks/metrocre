package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy {
    public static final float SIZE = 1f;

    private Body body;
    private Texture img;
    private World world;

    public Enemy(float x, float y, World world, Texture img) {
        body = createBody(x, y, world);
        this.world = world;
        this.img = img;
    }

    private Body createBody(float x, float y, World world) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        body = world.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(SIZE / 2);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;

        Fixture fixture = body.createFixture(fd);

        shape.dispose();

        return body;
    }

}
