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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.metrocre.game.network.Network;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.world.HUD;
import com.metrocre.game.controller.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.MyGame;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Queue;

public class GameScreen implements Screen {
    private MyGame game;
    private Music backgroundMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private Joystick moveJoystick;
    private Joystick attackJoystick;
    private TextButton nextLevelButton;
    private Player player = null;
    private Map map = null;
    private WorldManager worldManager;
    private HUD hud;
    private Skin skin;

    public void addTexture() {
        worldManager.addTexture(new Texture("avatar.png"), "player");
        worldManager.addTexture(new Texture("enemy.png"), "enemy1");
        worldManager.addTexture(new Texture("guntower.png"), "gunTower");
    }

    public GameScreen(MyGame game) {
        this.game = game;
        worldManager = new WorldManager(new World(new Vector2(0, 0), false), null);
        addTexture();
        skin = new Skin(Gdx.files.internal("lib.json"));
        batch = new SpriteBatch();
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/GameScreenTheme.mp3"));
//        backgroundMusic.setLooping(true);
//        backgroundMusic.play();
//        backgroundMusic.setVolume(game.getVolume());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16*SCALE, 9*SCALE);
        stage = new Stage(new StretchViewport(16*SCALE, 9*SCALE));
        hud = new HUD(player, stage, skin);
        moveJoystick = new Joystick(new Texture("joystick.png"), 0, 0, 6*SCALE, 6*SCALE, 0, true);
        stage.addActor(moveJoystick);
        attackJoystick = new Joystick(new Texture("joystick.png"), 10*SCALE, 0, 6*SCALE, 6*SCALE, 0.5f, false);
        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);
        nextLevelButton = new TextButton("", skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(4*SCALE, 4*SCALE);
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


    private float getCameraX() {
        float x = 0;
        if (player != null) {
            x = player.getX();
        }
        float mapWidth = 0;
        if (map != null) {
            mapWidth = map.getWidth();
        }
        return min(max(x, 8*SCALE), mapWidth - 8*SCALE);
    }

    private float getCameraY() {
        float y = 0;
        if (player != null) {
            y = player.getY();
        }
        float mapHeight = 0;
        if (map != null) {
            mapHeight = map.getHeight();
        }
        return min(max(y, 4.5F*SCALE), mapHeight - 4.5F*SCALE);
    }

    public void processRemoteEvent(Object o) {
        if (o instanceof WorldEvents.AddEntity) {
            WorldEvents.AddEntity event = (WorldEvents.AddEntity) o;
            worldManager.getMessageDispatcher().dispatchMessage(event.ID, event);
        } else if (o instanceof Network.SendMapSeed) {
            Network.SendMapSeed event = (Network.SendMapSeed) o;
            map = new Map(worldManager);
        } else if (o instanceof Network.UpdateEntityPosition) {
            Network.UpdateEntityPosition event = (Network.UpdateEntityPosition) o;
//            System.out.println(event.x + " " + event.y);
            Body body = worldManager.getEntity(event.entityId).getBody();
            body.setTransform(event.x, event.y, body.getAngle());
        } else if (o instanceof Network.SetLocalPlayer) {
            Network.SetLocalPlayer setLocalPlayer = (Network.SetLocalPlayer) o;
            player = (Player) worldManager.getEntity(setLocalPlayer.id);
        } else if (o instanceof WorldEvents.EquipWeapon) {
            WorldEvents.EquipWeapon equipWeapon = (WorldEvents.EquipWeapon) o;
            worldManager.getMessageDispatcher().dispatchMessage(equipWeapon.ID, equipWeapon);
        } else if (o instanceof Network.DestroyEntity) {
            Network.DestroyEntity destroyEntity = (Network.DestroyEntity) o;
            worldManager.getEntity(destroyEntity.id).destroy();
        }
    }

    @Override
    public void render(float delta) {
        Queue<Object> events = game.getClient().getRemoteEvents();
        for (Object o : events) {
            processRemoteEvent(o);
        }

        worldManager.processDestroyed();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(1, 1, 1, 1);

        camera.position.set(getCameraX(), getCameraY(), 0);
        camera.update();

        if (map != null) {
            map.draw(camera);
        }

        //nextLevelButton.setVisible(train.isPlayerOnTrain(player) && isAbleToFinishLevel());

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (worldManager != null) {
            worldManager.drawWorld(batch);
        }
        hud.update();
        batch.end();

        Vector2 moveDirection = moveJoystick.getDelta();
        Network.PlayerMove playerMove = new Network.PlayerMove();
        playerMove.direction = moveDirection;
        game.getClient().packToSend(playerMove);
//        System.out.println("PlayerMove sended");
        //player.move(moveDirection);

        Vector2 attackDirection = attackJoystick.getDirection();
        Network.PlayerAttack playerAttack = new Network.PlayerAttack();
        playerAttack.direction = attackDirection;
        game.getClient().packToSend(playerAttack);
        //player.shoot(attackDirection);

//        worldManager.update(delta);
//        worldManager.getWorld().step(delta, 6, 2);

        //b2ddr.render(worldManager.getWorld(), camera.combined);

        stage.act(delta);
        stage.draw();

        game.getClient().sendAll();
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
}
