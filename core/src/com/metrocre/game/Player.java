package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    public static final float SIZE = 1f;

    private Body body;
    private Texture img;
    private World world;

    public Player(float x, float y, World world, Texture img) {
        body = createBody(x, y, world);
        this.world = world;
        this.img = img;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(img, body.getPosition().x - SIZE / 2, body.getPosition().y - SIZE / 2, SIZE, SIZE);
    }

    public Body getBody() {
        return body;
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void shoot(Vector2 direction) {
        if (direction.len() == 0) {
            return;
        }
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        bd.bullet = true;
        bd.position.set(body.getPosition().add(direction.scl(0.75f)));
        Body bullet = world.createBody(bd);
        bullet.setLinearVelocity(direction.scl(10));

        CircleShape shape = new CircleShape();
        shape.setRadius(0.125f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;
        //fd.isSensor = true;

        Fixture fixture = bullet.createFixture(fd);

        shape.dispose();
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
