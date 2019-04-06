package main.game.entities.hitboxes;

import main.game.entities.Entity;
import main.game.enums.HitboxType;
import main.game.enums.Team;

public abstract class Hitbox {
    protected Entity owner;
    protected Team team;
    protected HitboxType type;

    protected double x, y;
    protected double radius;

    public Hitbox(Entity owner, Team team, double x, double y, double radius) {
        this.owner = owner;
        this.team = team;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Hitbox(Entity owner, Team team, double radius) {
        this(owner, team, 0, 0, radius);
    }

    public boolean overlapping(Hitbox other) {
        return Math.sqrt((other.getX() - x)*(other.getX() - x) + (other.getY() - y)*(other.getY() - y)) < radius + other.getRadius();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public Team getTeam() {
        return team;
    }

    public HitboxType getType() {
        return type;
    }
}
