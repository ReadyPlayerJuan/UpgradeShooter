package main.game.entities.enemies;

import main.game.Team;
import main.game.entities.Entity;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import rendering.WindowManager;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

public class Dummy extends Entity {
    private Sprite sprite;
    private BodyHitbox hitbox;

    private final double maxHealth = 3;
    private double health = maxHealth;
    private double radius = 20;

    public Dummy(double x, double y) {
        super(Team.ENEMY, x, y);

        terrainCollisionRadius = radius;

        sprite = new Sprite(SpriteData.PLAYER, x, y, radius);

        hitbox = new BodyHitbox(this, team, radius) {
            @Override
            public void takeDamage(double damage, double knockback, double knockbackAngle) {
                health -= damage;
                System.out.println("health: " + health);
                //xVel += knockback * Math.cos(knockbackAngle);
                //yVel += knockback * Math.sin(knockbackAngle);
            }
        };

        bodyHitboxes = new BodyHitbox[] {hitbox};
        registerEntityAndHitboxes();
    }

    @Override
    public void updatePre(double delta) {
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

        hitbox.setPosition(x, y);
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
        slotMinX = (int)Math.floor((x - radius) / slotSize);
        slotMaxX = (int)Math.floor((x + radius) / slotSize);
        slotMinY = (int)Math.floor((y - radius) / slotSize);
        slotMaxY = (int)Math.floor((y + radius) / slotSize);
    }
}
