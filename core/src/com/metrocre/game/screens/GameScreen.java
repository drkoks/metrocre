package com.metrocre.game.screens;


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
import com.metrocre.game.towers.GunTower;
import com.metrocre.game.world.Enemy;
import com.metrocre.game.controller.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.MyGame;
import com.metrocre.game.Player;
import com.metrocre.game.wepons.Railgun;
import com.metrocre.game.world.Train;
import com.metrocre.game.world.WorldManager;

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
        player = new Player(6, 1, worldManager, playerTexture, game.playersProfile);
        //player.setWeapon(new Railgun(player, worldManager.getProjectileManager(), new Texture("railgun.png")));
        player.setWeapon(new Pistol(player, worldManager.getProjectileManager(), new Texture("pistol.png"), 0.6F, 0.4F));

        map = new Map(worldManager);
        stage = new Stage(new StretchViewport(16, 9));
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6, 6, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10, 0, 6, 6, 0.5f, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
        worldManager.addEntity(new Enemy(12, 6, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(new Enemy(14, 6, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(new Enemy(14, 7, 3, 100, worldManager, enemyTexture));
        worldManager.addEntity(player);
        worldManager.addEntity(new GunTower(6.5f, 5.9f, 5, 10, worldManager, player, new Texture("guntower.png")));

        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        train = new Train(1, 0, worldManager, new Texture("data/empty.png"), 3, map.getHeight());
        worldManager.addEntity(train);
        nextLevelButton = new TextButton("", skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(4, 4);
        nextLevelButton.setPosition(stage.getWidth()-nextLevelButton.getWidth(), 0);
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


    private float getCameraX(){
        return min(max(player.getX(), 8), map.getWidth() - 8);
    }
    private float getCameraY(){
        return min(max(player.getY(), 4.5F), map.getHeight() - 4.5F);
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
        playerTexture.dispose();
        enemyTexture.dispose();
        worldManager.dispose();
        stage.dispose();
    }
}
