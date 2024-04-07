package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.metrocre.game.Enemy;
import com.metrocre.game.Entity;
import com.metrocre.game.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.Messages;
import com.metrocre.game.MyGame;
import com.metrocre.game.Pistol;
import com.metrocre.game.Player;
import com.metrocre.game.ProjectileManager;
import com.metrocre.game.Railgun;
import com.metrocre.game.Train;
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
    private ProjectileManager projectileManager;
    private Box2DDebugRenderer b2ddr;
    private Player player;
    private Train train;
    private Map map;
    private Stage stage;
    private Joystick moveJoystick;
    private Joystick attackJoystick;
    private Enemy[] enemies;
    private List<Entity> entities = new ArrayList<>();
    private TextButton nextLevelButton;

    public GameScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        playerTexture = new Texture("avatar.png");
        enemyTexture = new Texture("enemy.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);
        worldManager = new WorldManager(new World(new Vector2(0, 0), false));
        b2ddr = new Box2DDebugRenderer();
        player = new Player(0, 4, worldManager, playerTexture, game.playersProfile);
        projectileManager = new ProjectileManager(worldManager);
        //player.setWeapon(new Railgun(player, projectileManager, new Texture("railgun.png")));
        player.setWeapon(new Pistol(player, projectileManager, new Texture("railgun.png")));

        map = new Map(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, worldManager);
        stage = new Stage(new StretchViewport(16, 9));
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6, 6, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10, 0, 6, 6, 0.5f, false);
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

        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        train = new Train(2, 8, worldManager, new Texture("train.png"), 5, 1);
        entities.add(train);
        nextLevelButton = new TextButton("",skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(5, 3);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TradeScreen(game));
            }
        });
        stage.addActor(nextLevelButton);

    }

    @Override
    public void show() {

    }
    private boolean isAbleToFinishLevel(){
//        for (Enemy enemy : enemies) {
//            if (enemy.getBody() != null) {
//                return false;
//            }
//        }
        return true;
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();

        map.draw(camera);

        nextLevelButton.setVisible(train.isPlayerOnTrain(player) && isAbleToFinishLevel());

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entities) {
            entity.draw(batch);
            entity.update(delta);
        }
        projectileManager.draw(batch);
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        player.setVelocity(moveDirection.scl(5));

        Vector2 attackDirection = attackJoystick.getDirection();
        player.shoot(attackDirection);

        projectileManager.update(delta);
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
        projectileManager.dispose();
        worldManager.dispose();
        stage.dispose();
    }
}
