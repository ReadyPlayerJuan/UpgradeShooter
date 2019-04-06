package main.game.entities;

import main.game.boards.Camera;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.enums.Team;
import main.input.ControlMapping;
import main.input.InputManager;

import java.util.LinkedList;

import static java.lang.Math.*;

public class Player extends Entity {
    private final double maxHealth = 1;
    private double health = maxHealth;
    private double radius = 20;
    private double accel = 100;
    private double maxSpeed = 80;

    private BodyHitbox hitbox;

    public Player() {
        super(Team.PLAYER);

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
    }

    @Override
    public void updatePost(double delta) {
        x += xVel * delta;
        y += yVel * delta;
        System.out.println(x + " " + y);
    }

    @Override
    public void draw(Camera camera) {

    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }
}
