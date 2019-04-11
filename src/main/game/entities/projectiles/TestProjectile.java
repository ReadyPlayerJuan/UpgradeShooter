package main.game.entities.projectiles;

import main.game.entities.Entity;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.DamagerHitbox;
import main.game.weapons.WeaponController;
import rendering.EntityRenderer;
import main.game.weapons.Weapon;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

import static java.lang.Math.floor;

public class TestProjectile extends Projectile {
    private Sprite sprite;
    private DamagerHitbox hitbox;

    private boolean alive = true;

    private double damage;
    private double speed;
    private double angle;
    private double radius;
    private double knockback;

    //inherited: protected Entity source;
    //inherited: protected Weapon weaponSource;
    //inherited: protected Team team;
    //inherited: protected double x, y;
    //inherited: protected double xVel, yVel;
    //inherited: protected double nextX, nextY;
    //inherited: protected BodyHitbox[] bodyHitboxes = new BodyHitbox[0];
    //inherited: protected DamagerHitbox[] damagerHitboxes = new DamagerHitbox[0];
    //inherited: protected double terrainCollisionRadius = -1;

    public TestProjectile(WeaponController source, Weapon weaponSource, SpriteData spriteData, double x, double y, double damage, double speed, double angle, double radius, double knockback) {
        super(source, weaponSource);

        sprite = new Sprite(spriteData, x, y, radius);
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speed = speed;
        this.angle = angle;
        this.radius = radius;
        this.knockback = knockback;
        this.terrainCollisionRadius = radius;

        hitbox = new DamagerHitbox(this, team, radius) {
            @Override
            public void damage(BodyHitbox other) {
                alive = false;
                other.takeDamage(damage, knockback, 0);
            }
        };
        damagerHitboxes = new DamagerHitbox[] {hitbox};
        registerEntityAndHitboxes();
    }

    @Override
    public void updatePre(double delta) {
        xVel = speed * Math.cos(angle);
        yVel = speed * Math.sin(angle);

        nextX = x + xVel * delta;
        nextY = y + yVel * delta;
    }

    @Override
    public void updatePost(double delta) {
        x = nextX;
        y = nextY;

        sprite.setPosition(x, y);

        hitbox.setPosition(x, y);
    }

    @Override
    public void registerSprites(EntityRenderer renderer) {
        renderer.registerSprite(sprite);
    }

    @Override
    public void eventTerrainCollision(double angle) {
        alive = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void updateSlotPositions(double slotSize) {
        slotMinX = (int)floor((x - radius) / slotSize);
        slotMaxX = (int)floor((x + radius) / slotSize);
        slotMinY = (int)floor((y - radius) / slotSize);
        slotMaxY = (int)floor((y + radius) / slotSize);
    }
}
