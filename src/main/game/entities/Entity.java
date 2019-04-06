package main.game.entities;

import main.game.boards.Camera;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.DamagerHitbox;
import main.game.enums.Team;

public abstract class Entity {
    protected Team team;
    protected double x, y;
    protected double xVel, yVel;
    protected BodyHitbox[] bodyHitboxes = new BodyHitbox[0];
    protected DamagerHitbox[] damagerHitboxes = new DamagerHitbox[0];

    protected double terrainCollisionRadius = -1;

    public Entity(Team team) {
        this.team = team;
        EntityManager.current.addEntity(this);
    }

    public abstract void updatePre(double delta);
    public abstract void updatePost(double delta);
    public abstract void draw(Camera camera);

    public abstract boolean isAlive();

    public BodyHitbox[] getBodyHitboxes() {
        return bodyHitboxes;
    }

    public DamagerHitbox[] getDamagerHitboxes() {
        return damagerHitboxes;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXVel() {
        return xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public double getNextX(double delta) {
        return x + delta * xVel;
    }

    public double getNextY(double delta) {
        return y + delta * yVel;
    }

    public double getTerrainCollisionRadius() {
        return terrainCollisionRadius;
    }

    public Team getTeam() {
        return team;
    }
}
