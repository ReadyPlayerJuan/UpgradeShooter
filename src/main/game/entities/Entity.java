package main.game.entities;

import main.game.boards.Slottable;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.DamagerHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import main.game.Team;

public abstract class Entity extends Slottable {
    protected Team team;
    protected double x, y;
    protected double xVel, yVel;
    protected double nextX, nextY;
    protected BodyHitbox[] bodyHitboxes = new BodyHitbox[0];
    protected DamagerHitbox[] damagerHitboxes = new DamagerHitbox[0];
    protected double terrainCollisionRadius = -1;

    public Entity(Team team) {
        this.team = team;
    }

    public Entity(Team team, double x, double y) {
        this.team = team;
        this.x = x;
        this.y = y;
    }

    protected void registerEntityAndHitboxes() {
        EntityManager.current.addEntity(this);
    }

    public abstract void updatePre(double delta);
    public abstract void updatePost(double delta);
    public abstract void registerSprites(EntityRenderer renderer);

    public abstract void eventTerrainCollision(double angle);

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

    public void setVelocity(double xVel, double yVel) {
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public double getNextX() {
        return nextX;
    }

    public double getNextY() {
        return nextY;
    }

    public void setNextPosition(double nextX, double nextY) {
        this.nextX = nextX;
        this.nextY = nextY;
    }

    public double getTerrainCollisionRadius() {
        return terrainCollisionRadius;
    }

    public Team getTeam() {
        return team;
    }

    public String toString() {
        return team.toString() + " " + this.hashCode();
    }
}
