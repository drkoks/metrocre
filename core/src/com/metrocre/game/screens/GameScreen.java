package com.metrocre.game.screens;

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
import com.metrocre.game.Enemy;
import com.metrocre.game.Entity;
import com.metrocre.game.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.Messages;
import com.metrocre.game.MyGame;
import com.metrocre.game.Player;
import com.metrocre.game.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture enemyTexture;
    private OrthographicCamera camera;
    private WorldManager worldManager;
    private Box2DDebugRenderer b2ddr;
    private Player player;
    private Map map;
    private Stage stage;
    private Joystick moveJoystick;
    private Joystick attackJoystick;
    private Enemy[] enemies;
    private List<Entity> entities = new ArrayList<>();

    public GameScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        playerTexture = new Texture("img1.png");
        enemyTexture = new Texture("enemy.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);
        worldManager = new WorldManager(new World(new Vector2(0, 0), false));
        b2ddr = new Box2DDebugRenderer();
        player = new Player(0, 4, worldManager, playerTexture);
        map = new Map(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, worldManager);
        stage = new Stage(new StretchViewport(16, 9));
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6, 6, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10, 0, 6, 6, 0.2f, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
        enemies = new Enemy[3];
        enemies[0] = new Enemy(5, 5, worldManager, enemyTexture);
        enemies[1] = new Enemy(7, 5, worldManager, enemyTexture);
        enemies[2] = new Enemy(5, 7, worldManager, enemyTexture);
        entities.add(player);
        for (Enemy enemy : enemies) {
            entities.add(enemy);
            worldManager.getMessageDispatcher().addListener(enemy, Messages.HIT);
        }
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
        for (Entity entity : entities) {
            entity.draw(batch);
            entity.update(delta);
        }
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        player.setVelocity(moveDirection.scl(5));

        Vector2 attackDirection = attackJoystick.getDirection();
        player.shoot(attackDirection);

        worldManager.getWorld().step(delta, 6, 2);

        b2ddr.render(worldManager.getWorld(), camera.combined);

        stage.act(delta);
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
        playerTexture.dispose();
        enemyTexture.dispose();
        stage.dispose();
    }
}
