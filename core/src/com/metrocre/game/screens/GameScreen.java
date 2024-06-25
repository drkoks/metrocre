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
import com.metrocre.game.weapons.Rail;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.EntityType;
import com.metrocre.game.world.HUD;
import com.metrocre.game.controller.Joystick;
import com.metrocre.game.Map;
import com.metrocre.game.MyGame;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.Train;
import com.metrocre.game.world.WorldManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.metrocre.game.world.Worm;
import com.metrocre.game.world.enemies.Enemy;

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
    private TextButton shopButton;
    private Train train;
    private boolean isAbleToFinishLevel = false;

    public void addTexture() {
        worldManager.addTexture(new Texture("dog.png"), "player");
        worldManager.addTexture(new Texture("enemies/enemy.png"), "enemy1");
        worldManager.addTexture(new Texture("enemies/enemy2.png"), "enemy2");
        worldManager.addTexture(new Texture("guntower.png"), "gunTower");
        worldManager.addTexture(new Texture("healTower.png"), "healTower");
    }

    public GameScreen(MyGame game) {
        this.game = game;
        worldManager = new WorldManager(new World(new Vector2(0, 0), true), null);
        addTexture();
        skin = new Skin(Gdx.files.internal("lib.json"));
        batch = new SpriteBatch();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/GameScreenTheme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(0/*game.getVolume()*/);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16 * SCALE, 9 * SCALE);
        stage = new Stage(new StretchViewport(16 * SCALE, 9 * SCALE));

        hud = new HUD(player, stage, skin);

        moveJoystick = new Joystick(new Texture("joystick.png"),
                0, 0, 6 * SCALE, 6 * SCALE, 0, true);

        stage.addActor(moveJoystick);

        attackJoystick = new Joystick(new Texture("joystick.png"),
                10 * SCALE, 0, 6 * SCALE, 6 * SCALE, 0.5f, false);

        stage.addActor(attackJoystick);
        Gdx.input.setInputProcessor(stage);

        nextLevelButton = new TextButton("", skin, "next");
        nextLevelButton.setVisible(false);
        nextLevelButton.setSize(4 * SCALE, 4 * SCALE);
        nextLevelButton.setPosition(stage.getWidth() - nextLevelButton.getWidth(), 0);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getClient().packToSend(new Network.CompleteLevel());
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
                game.setShopScreen(new ShopScreen(game, stage));
            }
        });
        stage.addActor(shopButton);

    }

//    public static GameScreen loadGameState(MyGame game) {
//        try {
//            FileInputStream fileIn = new FileInputStream("gamestate.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            GameState gameState = (GameState) in.readObject();
//            in.close();
//            fileIn.close();
//            return new GameScreen(game, gameState);
//        } catch (IOException i) {
//            i.printStackTrace();
//        } catch (ClassNotFoundException c) {
//            System.out.println("GameState class not found");
//            c.printStackTrace();
//        }
//        return new GameScreen(game);
//    }

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
        return min(max(x, 8 * SCALE), mapWidth - 8 * SCALE);
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
        return min(max(y, 4.5F * SCALE), mapHeight - 4.5F * SCALE);
    }

    public void processRemoteEvent(Object o) {
        if (o instanceof WorldEvents.AddEntity) {
            WorldEvents.AddEntity addEntity = (WorldEvents.AddEntity) o;
            worldManager.getMessageDispatcher().dispatchMessage(addEntity.ID, addEntity);
            if (addEntity.type == EntityType.Train) {
                train = (Train) worldManager.getLastAddedEntity();
            }
        } else if (o instanceof Network.SendMapSeed) {
            Network.SendMapSeed sendMapSeed = (Network.SendMapSeed) o;
            map = new Map(worldManager, sendMapSeed.seed, true);
            worldManager.setMap(map);
        } else if (o instanceof Network.UpdateEntityPosition) {
            Network.UpdateEntityPosition updateEntityPosition = (Network.UpdateEntityPosition) o;
            Entity entity = worldManager.getEntity(updateEntityPosition.entityId);
            if (entity != null) {
                Body body = entity.getBody();
                body.setTransform(updateEntityPosition.x, updateEntityPosition.y, body.getAngle());
            }
        } else if (o instanceof Network.SetLocalPlayer) {
            Network.SetLocalPlayer setLocalPlayer = (Network.SetLocalPlayer) o;
            player = (Player) worldManager.getEntity(setLocalPlayer.id);
            if (player != null) {
                game.localPlayerProfile = player.getPlayersProfile();
                hud = new HUD(player, stage, skin);
            }
        } else if (o instanceof WorldEvents.EquipWeapon) {
            WorldEvents.EquipWeapon equipWeapon = (WorldEvents.EquipWeapon) o;
            worldManager.getMessageDispatcher().dispatchMessage(equipWeapon.ID, equipWeapon);
        } else if (o instanceof Network.DestroyEntity) {
            Network.DestroyEntity destroyEntity = (Network.DestroyEntity) o;
            Entity entity = worldManager.getEntity(destroyEntity.id);
            if (entity != null) {
                entity.destroy();
            }
        } else if (o instanceof Network.EndGame) {
            Network.EndGame endGame = (Network.EndGame) o;
            backgroundMusic.stop();
            game.setScreen(new GameOverScreen(game, endGame.isVictory));
        } else if (o instanceof Network.Buy) {
            Network.Buy buy = (Network.Buy) o;
            Player player = (Player) worldManager.getEntity(buy.playerId);
            if (player != null) {
                player.getPlayersProfile().buyItem(buy.upgrades, worldManager, null, -1);
            }
        } else if (o instanceof Network.CompleteLevel) {
            backgroundMusic.stop();
            game.setScreen(new TradeScreen(game));
        } else if (o instanceof Network.AbleToFinish) {
            isAbleToFinishLevel = true;
        } else if (o instanceof Network.TakeDamage) {
            Network.TakeDamage takeDamage = (Network.TakeDamage) o;
            Entity receiver = worldManager.getEntity(takeDamage.receiverId);
            if (receiver != null) {
                if (receiver instanceof Enemy) {
                    Enemy enemy = (Enemy) receiver;
                    enemy.takeDamage(takeDamage.damage, takeDamage.senderId);
                } else if (receiver instanceof Player) {
                    Player player = (Player) receiver;
                    player.takeDamage(takeDamage.damage, takeDamage.senderId);
                } else if (receiver instanceof Train) {
                    Train train = (Train) receiver;
                    train.takeDamage(takeDamage.damage);
                } else if (receiver instanceof Worm) {
                    Worm worm = (Worm) receiver;
                    worm.takeDamage(takeDamage.damage, takeDamage.senderId);
                }
            }
        } else if (o instanceof Network.Heal) {
            Network.Heal heal = (Network.Heal) o;
            Player player = (Player) worldManager.getEntity(heal.playerId);
            if (player != null) {
                player.heal(heal.value);
            }
        } else if (o instanceof Rail) {
            Rail rail = (Rail) o;
            worldManager.getProjectileManager().addRail(rail);
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
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        camera.position.set(getCameraX(), getCameraY(), 0);
        camera.update();

        if (map != null) {
            map.draw(camera);
        }

        nextLevelButton.setVisible(train != null && train.isPlayerOnTrain(player) && isAbleToFinishLevel);

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

        worldManager.getProjectileManager().update(delta);

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

//    public void saveGameState() {
//        try {
//            FileOutputStream fileOut = new FileOutputStream("gamestate.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(new GameState(player, worldManager, map));
//            out.close();
//            fileOut.close();
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
//    }
}
