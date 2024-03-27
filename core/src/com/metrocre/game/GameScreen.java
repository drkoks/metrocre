package com.metrocre.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture img;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer b2ddr;
    private Player player;
    private Map map;
    private Stage stage;
    private Joystick moveJoystick;
    private Joystick attackJoystick;

    public GameScreen() {
        batch = new SpriteBatch();
        img = new Texture("img1.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);
        world = new World(new Vector2(0, 0), false);
        b2ddr = new Box2DDebugRenderer();
        player = new Player(0, 4, world, img);
        map = new Map(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, world);
        stage = new Stage(new StretchViewport(16, 9));
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6, 6, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10, 0, 6, 6, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();

        map.draw(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        player.setVelocity(moveDirection.scl(5));

        Vector2 attackDirection = attackJoystick.getDirection();
        player.shoot(attackDirection);

        world.step(1f / 60, 6, 2);

        b2ddr.render(world, camera.combined);

        stage.act(1f / 60);
        stage.draw();
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
        batch.dispose();
        img.dispose();
        stage.dispose();
    }
}
