package main.game.entities;

import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import main.game.enums.Team;
import main.input.ControlMapping;
import main.input.InputManager;
import rendering.WindowManager;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

import java.util.LinkedList;

import static java.lang.Math.*;

public class Player extends Entity {
    private Sprite sprite;
    private final double maxHealth = 1;
    private double health = maxHealth;
    private double radius = 20;
    private double accel = 2000;
    private double maxSpeed = 300;

    private BodyHitbox hitbox;

    public Player() {
        super(Team.PLAYER);

        terrainCollisionRadius = radius;

        sprite = new Sprite(SpriteData.PLAYER, 0, 0, radius);

        hitbox = new BodyHitbox(this, team, radius) {
            @Override
            public void takeDamage(double damage, double knockback, double knockbackAngle) {
                health -= damage;
                xVel += knockback * Math.cos(knockbackAngle);
                yVel += knockback * Math.sin(knockbackAngle);
            }
        };

        bodyHitboxes = new BodyHitbox[] {hitbox};
    }

    @Override
    public void updatePre(double delta) {
        LinkedList<Double[]> heldKeys = InputManager.getHeldActionKeys();

        int moveX = 0;
        int moveY = 0;
        for(Double[] keys: heldKeys) {
            //System.out.println(keys[0] + " " + keys[1]);
            if(keys[0] == ControlMapping.MOVE_LEFT.keyCode)
                moveX--;
            if(keys[0] == ControlMapping.MOVE_RIGHT.keyCode)
                moveX++;
            if(keys[0] == ControlMapping.MOVE_DOWN.keyCode)
                moveY--;
            if(keys[0] == ControlMapping.MOVE_UP.keyCode)
                moveY++;
        }

        if(moveX == 0) {
            xVel = signum(xVel) * max(0, abs(xVel) - accel * delta);
        } else {
            xVel += moveX * accel * delta;
            xVel = signum(xVel) * min(maxSpeed, abs(xVel));
        }
        if(moveY == 0) {
            yVel = signum(yVel) * max(0, abs(yVel) - accel * delta);
        } else {
            yVel += moveY * accel * delta;
            yVel = signum(yVel) * min(maxSpeed, abs(yVel));
        }

        nextX = x + xVel * delta;
        nextY = y + yVel * delta;
    }

    @Override
    public void updatePost(double delta) {
        x = nextX;
        y = nextY;
        sprite.setRotation(sprite.getRotation() + 0.01);
        sprite.setImageIndex((int)(WindowManager.getTime() * 1.0) % 4);
        sprite.setPosition(x, y);
    }

    @Override
    public void registerSprites(EntityRenderer renderer) {
        renderer.registerSprite(sprite);
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }
}
