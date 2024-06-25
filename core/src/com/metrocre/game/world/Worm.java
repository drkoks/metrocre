package com.metrocre.game.world;

import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.metrocre.game.MyGame;
import com.metrocre.game.network.Network;
import com.metrocre.game.towers.Tower;

import java.util.ArrayList;
import java.util.List;

public class Worm extends Entity {
    private float size;
    private int numSegments;
    private int type;
    private int health;
    private List<Body> segmentList = new ArrayList<>();
    private float timeFromLastUpdate = 0;
    private float speed = 0;
    private Vector2 targetPos;
    private float damage;

    public Worm(float x, float y, WorldManager worldManager, int type) {
        super(worldManager, new Texture(type == 1 ? "smallWormSegment.png" : "bigWormSegment.png"));
        this.type = type;
        if (type == 1) {
            this.size = SCALE / 3f;
            this.numSegments = 10;
            this.health = 2;
            this.damage = 10;
        } else {
            this.size = 2 * SCALE;
            this.numSegments = 20;
            this.health = 100;
            this.damage = 30;
        }
        this.width = this.size;
        this.height = this.size;
        body = worldManager.createCircleBody(x, y, size / 2, true, false, this);
        segmentList.add(body);
        for (int nSegment = 1; nSegment < numSegments; nSegment++) {
            segmentList.add(worldManager.createCircleBody(x, y, size / 2, true, false, this));
        }
    }

    @Override
    public void update(float deltaTime) {
        timeFromLastUpdate += deltaTime;
        if (type == 1 && timeFromLastUpdate >= 3) {
            int height = worldManager.getServer().getRandom().nextInt((int) worldManager.getMap().getHeight());
            targetPos = new Vector2(0, height);
            speed = 3 * SCALE;
            timeFromLastUpdate = 0;
        } else if (type == 2 && timeFromLastUpdate >= 4) {
            List<Player> players = worldManager.getPlayers();
            if (!players.isEmpty()) {
                int playerIndex = worldManager.getServer().getRandom().nextInt(players.size());
                targetPos = players.get(playerIndex).getPosition().cpy();
                speed = 7 * SCALE;
            }
            timeFromLastUpdate = 0;
        }
        if (speed > 0) {
            Vector2 dir = targetPos.cpy().sub(body.getPosition()).limit(speed).scl(deltaTime);
            body.setTransform(body.getPosition().x + dir.x, body.getPosition().y + dir.y, body.getAngle());
            speed = max(0, speed - 1 * deltaTime * SCALE);
        }
        updateSegments();
    }

    @Override
    public void draw(SpriteBatch batch) {
        updateSegments();
        for (Body body : segmentList) {
            batch.draw(texture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);
        }
    }

    private void updateSegments() {
        for (int i = 1; i < numSegments; i++) {
            Body prevSegment = segmentList.get(i - 1);
            Body curSegment = segmentList.get(i);
            Vector2 delta = curSegment.getPosition().cpy().sub(prevSegment.getPosition());
            if (delta.len() > this.size * 0.5f) {
                delta = delta.limit(this.size * 0.75f);
                curSegment.setTransform(prevSegment.getPosition().x + delta.x, prevSegment.getPosition().y + delta.y, curSegment.getAngle());
            }
        }
    }

    public List<Body> getSegmentList() {
        return segmentList;
    }

    public void takeDamage(float damage, int senderId) {
        if (health == 0) {
            return;
        }
        Entity entity = worldManager.getEntity(senderId);
        float totalDamage = damage;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            totalDamage = damage + 0.2f * damage * (player.getPlayersProfile().getAttack() - 1);
        }
        health = (int) max(0f, health - totalDamage);
        if (health == 0) {
            if (worldManager.getServer() != null) {
                destroy();
            }
        }
        if (worldManager.getServer() != null) {
            Network.TakeDamage takeDamage = new Network.TakeDamage();
            takeDamage.senderId = senderId;
            takeDamage.receiverId = id;
            takeDamage.damage = damage;
            worldManager.getServer().packToSend(takeDamage);
        }
    }

    public float getDamage() {
        return damage;
    }

    public int getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }
}
