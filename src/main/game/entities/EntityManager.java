package main.game.entities;

import main.game.boards.Board;
import main.game.boards.Camera;
import main.game.boards.Wall;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.DamagerHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import main.game.enums.Team;

import java.util.LinkedList;

import static java.lang.Math.*;

public class EntityManager {
    public static EntityManager current = null;

    private EntityRenderer entityRenderer;

    private LinkedList<Entity> playerEntities, enemyEntities;//, neutralEntities;
    private LinkedList<BodyHitbox> playerBodyHitboxes, enemyBodyHitboxes;
    private LinkedList<DamagerHitbox> playerDamagerHitboxes, enemyDamagerHitboxes;

    public EntityManager() {
        setCurrent();

        entityRenderer = new EntityRenderer();

        playerEntities = new LinkedList<>();
        enemyEntities = new LinkedList<>();
        //neutralEntities = new LinkedList<>();

        playerBodyHitboxes = new LinkedList<>();
        enemyBodyHitboxes = new LinkedList<>();
        playerDamagerHitboxes = new LinkedList<>();
        enemyDamagerHitboxes = new LinkedList<>();
    }

    public void updateEntities(double delta, Board board) {
        setCurrent();

        for(Entity e: playerEntities) {
            e.updatePre(delta);
        }
        for(Entity e: enemyEntities) {
            e.updatePre(delta);
        }

        collideEntities();
        collideTerrain(board);

        for(Entity e: playerEntities) {
            e.updatePost(delta);
        }
        for(Entity e: enemyEntities) {
            e.updatePost(delta);
        }
    }

    private void collideEntities() {

    }

    private void collideTerrain(Board board) {
        for(Wall wall: board.getWalls()) {
            for(Entity entity: playerEntities) {
                wall.collide(entity);
            }
        }
    }

    public void render(Camera camera) {
        entityRenderer.render(camera, playerEntities, enemyEntities);
    }

    public void addEntity(Entity e) {
        switch(e.getTeam()) {
            case PLAYER:
                playerEntities.add(e); break;
            case ENEMY:
                enemyEntities.add(e); break;
        }

        for(BodyHitbox h: e.getBodyHitboxes()) {
            if(h.getTeam() == Team.PLAYER) {
                playerBodyHitboxes.add(h);
            } else if(h.getTeam() == Team.ENEMY) {
                enemyBodyHitboxes.add(h);
            }
        }
        for(DamagerHitbox h: e.getDamagerHitboxes()) {
            if(h.getTeam() == Team.PLAYER) {
                playerDamagerHitboxes.add(h);
            } else if(h.getTeam() == Team.ENEMY) {
                enemyDamagerHitboxes.add(h);
            }
        }
    }

    public void setCurrent() {
        EntityManager.current = this;
    }
}
