package com.metrocre.game.screens;


import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Float.max;
import static java.lang.Float.min;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
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
import com.metrocre.game.Map;
import com.metrocre.game.MyGame;
import com.metrocre.game.controller.Joystick;
import com.metrocre.game.towers.GunTower;
import com.metrocre.game.weapons.Pistol;
import com.metrocre.game.world.HUD;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.Train;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.world.enemies.Enemy1;
import com.metrocre.game.world.enemies.Enemy2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import states.GameState;

public class GameScreen implements Screen {
    private final Music backgroundMusic;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final WorldManager worldManager;
    private final Player player;
    private final Train train;
    private final Map map;
    private final Stage stage;
    private final Joystick moveJoystick;
    private final Joystick attackJoystick;
    private final TextButton nextLevelButton;
    private final TextButton shopButton;
    private final HUD hud;

    public GameScreen(MyGame game, GameState gameState) {
        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        batch = new SpriteBatch();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/GameScreenTheme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(game.getVolume());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16 * SCALE, 9 * SCALE);
        worldManager = new WorldManager(new World(new Vector2(0, 0), false));
        addTexture(worldManager);
        Box2DDebugRenderer b2ddr = new Box2DDebugRenderer();
        if (gameState == null) {
            player = new Player(6 * SCALE, SCALE, worldManager, game.playersProfile);
            //player.setWeapon(new Railgun(player, worldManager.getProjectileManager(), new Texture("railgun.png")));
            player.setWeapon(new Pistol(player, worldManager.getProjectileManager(), new Texture("pistol.png"),
                    0.6F * SCALE, 0.4F * SCALE, game.playersProfile.getWeaponLevel()));
        } else {
            player = new Player(gameState.getPlayerState(), worldManager, game.playersProfile);
        }
        worldManager.addEntity(player);
        map = new Map(worldManager); // #TODO load map
        stage = new Stage(new StretchViewport(16 * SCALE, 9 * SCALE));
        hud = new HUD(player, stage, skin);
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6 * SCALE, 6 * SCALE, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10 * SCALE, 0, 6 * SCALE, 6 * SCALE, 0.5f, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
        if (gameState == null) {
            worldManager.addEntity(new Enemy1(12 * SCALE, 6 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(14 * SCALE, 6 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(14 * SCALE, 7 * SCALE, worldManager));
        } else {
            for (Vector2 enemyPosition : gameState.getEnemyPositions()) {
                worldManager.addEntity(new Enemy1(enemyPosition.x, enemyPosition.y, worldManager)); // #TODO load enemies
            }
        }
        worldManager.addEntity(new GunTower(6.5f * SCALE, 5.9f * SCALE, 5, 10 * SCALE, worldManager, player, "gunTower"));
        train = new Train(SCALE, 0, worldManager, new Texture("data/empty.png"), 3 * SCALE, map.getHeight());
        worldManager.addEntity(train);
        nextLevelButton = new TextButton("", skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(4 * SCALE, 4 * SCALE);
        nextLevelButton.setPosition(stage.getWidth() - nextLevelButton.getWidth(), 0);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new TradeScreen(game));
            }
        });
        stage.addActor(nextLevelButton);

        shopButton = new TextButton("", skin, "market");
        shopButton.setSize(2 * SCALE, 2 * SCALE);
        shopButton.setPosition(stage.getWidth() - shopButton.getWidth(), stage.getHeight() - shopButton.getHeight());
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                saveGameState();
                game.setScreen(new ShopScreen(game));
            }
        });
        stage.addActor(shopButton);

    }

    public GameScreen(MyGame game) {
        this(game, null);
    }

    public static GameScreen loadGameState(MyGame game) {
        try {
            FileInputStream fileIn = new FileInputStream("gamestate.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameState gameState = (GameState) in.readObject();
            in.close();
            fileIn.close();
            return new GameScreen(game, gameState);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("GameState class not found");
            c.printStackTrace();
        }
        return new GameScreen(game);
    }

    public void addTexture(WorldManager worldManager) {
        worldManager.addTexture(new Texture("avatar.png"), "player");
        worldManager.addTexture(new Texture("enemies/enemy.png"), "enemy1");
        worldManager.addTexture(new Texture("enemies/enemy2.png"), "enemy2");
        worldManager.addTexture(new Texture("guntower.png"), "gunTower");
    }

    private void commonSetup() {

    }

    @Override
    public void show() {

    }

    private boolean isAbleToFinishLevel() {
        for (Enemy enemy : worldManager.getEnemies()) {
            if (!enemy.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    private float getCameraX() {
        return min(max(player.getX(), 8 * SCALE), map.getWidth() - 8 * SCALE);
    }

    private float getCameraY() {
        return min(max(player.getY(), 4.5F * SCALE), map.getHeight() - 4.5F * SCALE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(1, 1, 1, 1);

        camera.position.set(getCameraX(), getCameraY(), 0);
        camera.update();

        map.draw(camera);

        nextLevelButton.setVisible(train.isPlayerOnTrain(player) && isAbleToFinishLevel());

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldManager.drawWorld(batch);
        hud.update();
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        player.move(moveDirection);

        Vector2 attackDirection = attackJoystick.getDirection();
        player.shoot(attackDirection);

        worldManager.update(delta);
        worldManager.getWorld().step(delta, 6, 2);

        //b2ddr.render(worldManager.getWorld(), camera.combined);

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
        hud.dispose();
        worldManager.dispose();
        stage.dispose();
    }

    public void saveGameState() {
        try {
            FileOutputStream fileOut = new FileOutputStream("gamestate.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new GameState(player, worldManager));
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
