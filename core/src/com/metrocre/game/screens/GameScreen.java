package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.metrocre.game.controller.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.MyGame;
import com.metrocre.game.Player;
import com.metrocre.game.wepons.Railgun;
import com.metrocre.game.Train;
import com.metrocre.game.WorldManager;

public class GameScreen implements Screen {
    private final MyGame game;
    private final Music backgroundMusic;
    private final SpriteBatch batch;
    private final Texture playerTexture;
    private final Texture enemyTexture;
    private final OrthographicCamera camera;
    private final WorldManager worldManager;
    private final Box2DDebugRenderer b2ddr;
    private final Player player;
    private final Train train;
    private final Map map;
    private final Stage stage;
    private final Joystick moveJoystick;
    private final Joystick attackJoystick;
    private final TextButton nextLevelButton;

    public GameScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/GameScreenTheme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(game.getVolume());
        playerTexture = new Texture("avatar.png");
        enemyTexture = new Texture("enemy.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);
        worldManager = new WorldManager(new World(new Vector2(0, 0), false));
        b2ddr = new Box2DDebugRenderer();
        player = new Player(0, 4, worldManager, playerTexture, game.playersProfile);
        player.setWeapon(new Railgun(player, worldManager.getProjectileManager(), new Texture("railgun.png")));
        //player.setWeapon(new Pistol(player, worldManager.getProjectileManager(), new Texture("railgun.png")));

        map = new Map(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, worldManager);
        stage = new Stage(new StretchViewport(16, 9));
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6, 6, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10, 0, 6, 6, 0.5f, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
        worldManager.addEntity(new Enemy(5, 5, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(new Enemy(7, 5, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(new Enemy(5, 7, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(player);

        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        train = new Train(2, 8, worldManager, new Texture("train.png"), 5, 1);
        worldManager.addEntity(train);
        nextLevelButton = new TextButton("", skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(5, 3);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new TradeScreen(game));
            }
        });
        stage.addActor(nextLevelButton);

    }

    @Override
    public void show() {

    }

    private boolean isAbleToFinishLevel() {
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
        worldManager.drawWorld(batch);
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        player.move(moveDirection);

        Vector2 attackDirection = attackJoystick.getDirection();
        player.shoot(attackDirection);

        worldManager.update(delta);
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
        worldManager.dispose();
        stage.dispose();
    }
}
