package main.game.entities;

import main.game.PlayerData;
import main.game.boards.Camera;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.Team;
import main.game.weapons.Weapon;
import main.game.weapons.WeaponController;
import main.game.weapons.guns.Pistol;
import main.input.ControlMapping;
import main.input.InputManager;
import rendering.EntityRenderer;
import rendering.WindowManager;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

import java.util.LinkedList;

import static java.lang.Math.*;

public class Player extends Entity implements WeaponController {
    private Sprite sprite;
    private BodyHitbox hitbox;

    private final double maxHealth = 1;
    private double health = maxHealth;
    private double radius = 20;
    private double accel = 2000;
    private double maxSpeed = 300;

    private Camera camera;
    private int currentWeaponIndex = 0;
    private boolean firingWeapon = false, prevFiringWeapon = false;
    private double targetX = 0, targetY = 0;

    public Player() {
        super(Team.PLAYER);

        terrainCollisionRadius = radius;

        sprite = new Sprite(SpriteData.PLAYER).setPosition((float)x, (float)y).setSize((float)radius*2);

        hitbox = new BodyHitbox(this, team, radius) {
            @Override
            public void takeDamage(double damage, double knockback, double knockbackAngle) {
                health -= damage;
                xVel += knockback * Math.cos(knockbackAngle);
                yVel += knockback * Math.sin(knockbackAngle);
            }
        };

        bodyHitboxes = new BodyHitbox[] {hitbox};
        registerEntityAndHitboxes();
    }

    @Override
    public void updatePre(double delta) {
        LinkedList<Double[]> heldKeys = InputManager.getHeldActionKeys();

        int moveX = 0;
        int moveY = 0;
        prevFiringWeapon = firingWeapon;
        firingWeapon = false;
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

            if(keys[0] == ControlMapping.FIRE_WEAPON.keyCode)
                firingWeapon = true;
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
        sprite.setRotation(sprite.getRotation() + 0.01f);
        //sprite.setImageIndex((int)(WindowManager.getTime() * 1.0) % 4);
        sprite.setPosition((float)x, (float)y);

        hitbox.setPosition(x, y);


        targetX = InputManager.mouseX - camera.getViewWidth()/2 + camera.getCenterX() - x;
        targetY = -InputManager.mouseY + camera.getViewHeight()/2 + camera.getCenterY() - y;
        PlayerData.getEquippedWeapon(currentWeaponIndex).update(delta, this);
        /*if(Math.floor(WindowManager.getTime()*1 - delta) < Math.floor(WindowManager.getTime()*1)) {
            new TestProjectile(this, null, SpriteData.PLAYER, x, y, 1, 300, 0, 10, 0);
        }*/
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void registerSprites(EntityRenderer renderer) {
        renderer.registerSprite(sprite);
    }

    @Override
    public void eventTerrainCollision(double angle) {

    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void updateSlotPositions(double slotSize) {
        slotMinX = (int)floor((x - radius) / slotSize);
        slotMaxX = (int)floor((x + radius) / slotSize);
        slotMinY = (int)floor((y - radius) / slotSize);
        slotMaxY = (int)floor((y + radius) / slotSize);
    }

    @Override
    public double getTargetX() {
        return targetX;
    }

    @Override
    public double getTargetY() {
        return targetY;
    }

    @Override
    public boolean isFiringWeapon() {
        return firingWeapon;
    }

    @Override
    public boolean wasFiringWeapon() {
        return prevFiringWeapon;
    }
}
