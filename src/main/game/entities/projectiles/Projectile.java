package main.game.entities.projectiles;

import main.game.Team;
import main.game.entities.Entity;
import main.game.weapons.Weapon;

public abstract class Projectile extends Entity {
    protected Entity source;
    protected Weapon weaponSource;
    //inherited: protected Team team;
    //inherited: protected double x, y;
    //inherited: protected double xVel, yVel;
    //inherited: protected double nextX, nextY;
    //inherited: protected BodyHitbox[] bodyHitboxes = new BodyHitbox[0];
    //inherited: protected DamagerHitbox[] damagerHitboxes = new DamagerHitbox[0];
    //inherited: protected double terrainCollisionRadius = -1;

    public Projectile(Entity source, Weapon weaponSource) {
        super(source.getTeam());
        this.source = source;
        this.weaponSource = weaponSource;
    }

    //public abstract void
}
