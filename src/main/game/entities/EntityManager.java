package main.game.entities;

import main.game.boards.*;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.DamagerHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import main.game.Team;

import java.util.LinkedList;

public class EntityManager {
    public static EntityManager current = null;

    private EntityRenderer entityRenderer;

    private LinkedList<Entity> newEntities;

    private LinkedList<Entity> playerEntities, enemyEntities;//, neutralEntities;
    //private LinkedList<BodyHitbox> playerBodyHitboxes, enemyBodyHitboxes;
    //private LinkedList<DamagerHitbox> playerDamagerHitboxes, enemyDamagerHitboxes;

    private CellSlotter<Entity> slottedPlayerEntities, slottedEnemyEntities;
    private CellSlotter<BodyHitbox> slottedPlayerBodyHitboxes, slottedEnemyBodyHitboxes;
    private CellSlotter<DamagerHitbox> slottedPlayerDamagerHitboxes, slottedEnemyDamagerHitboxes;

    private CellEventManager<Wall, Entity> terrainCollisionEventManager;
    private CellEventManager<BodyHitbox, DamagerHitbox> hitboxCollisionEventManager;

    public EntityManager() {
        setCurrent();

        entityRenderer = new EntityRenderer();

        newEntities = new LinkedList<>();

        playerEntities = new LinkedList<>();
        enemyEntities = new LinkedList<>();

        slottedPlayerEntities = new CellSlotter<>();
        slottedEnemyEntities = new CellSlotter<>();
        //neutralEntities = new LinkedList<>();

        //playerBodyHitboxes = new LinkedList<>();
        //enemyBodyHitboxes = new LinkedList<>();
        //playerDamagerHitboxes = new LinkedList<>();
        //enemyDamagerHitboxes = new LinkedList<>();

        slottedPlayerBodyHitboxes = new CellSlotter<>();
        slottedEnemyBodyHitboxes = new CellSlotter<>();
        slottedPlayerDamagerHitboxes = new CellSlotter<>();
        slottedEnemyDamagerHitboxes = new CellSlotter<>();


        terrainCollisionEventManager = new CellEventManager<>() {
            @Override
            public void event(Wall item1, Entity item2) {
                if(item1.collide(item2)) {
                    item2.eventTerrainCollision(0);
                }
            }
        };
        hitboxCollisionEventManager = new CellEventManager<>() {
            @Override
            public void event(BodyHitbox item1, DamagerHitbox item2) {
                //System.out.println("checking collision between " + item1 + " and " + item2);
                if(item1.getOwner().isAlive() && item2.getOwner().isAlive() && item2.overlapping(item1)) {
                    item2.damage(item1);
                    //System.out.println("collide");
                }
            }
        };
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

        for(int i = 0; i < playerEntities.size(); i++) {
            Entity e = playerEntities.get(i);
            e.updatePost(delta);
            if(!e.isAlive()) {
                playerEntities.remove(i--);
            }
        }
        for(int i = 0; i < enemyEntities.size(); i++) {
            Entity e = enemyEntities.get(i);
            e.updatePost(delta);
            if(!e.isAlive()) {
                enemyEntities.remove(i--);
            }
        }

        addNewEntities();
    }

    private void collideEntities() {

    }

    private void collideTerrain(Board board) {
        slottedPlayerEntities.clear();
        slottedPlayerEntities.addAndUpdateAll(playerEntities, Board.CELL_SIZE);
        slottedEnemyEntities.clear();
        slottedEnemyEntities.addAndUpdateAll(enemyEntities, Board.CELL_SIZE);

        terrainCollisionEventManager.callEvents(board.getSlottedWalls(), slottedPlayerEntities);
        terrainCollisionEventManager.callEvents(board.getSlottedWalls(), slottedEnemyEntities);

        slottedPlayerBodyHitboxes.clear();
        slottedEnemyBodyHitboxes.clear();
        slottedPlayerDamagerHitboxes.clear();
        slottedEnemyDamagerHitboxes.clear();
        //slottedPlayerBodyHitboxes.addAndUpdateAll(playerBodyHitboxes, Board.CELL_SIZE);
        //slottedEnemyBodyHitboxes.addAndUpdateAll(enemyBodyHitboxes, Board.CELL_SIZE);
        //slottedPlayerDamagerHitboxes.addAndUpdateAll(playerDamagerHitboxes, Board.CELL_SIZE);
        //slottedEnemyDamagerHitboxes.addAndUpdateAll(enemyDamagerHitboxes, Board.CELL_SIZE);

        for(Entity e: playerEntities) {
            slottedPlayerBodyHitboxes.addAndUpdateAll(e.getBodyHitboxes(), Board.CELL_SIZE);
            slottedPlayerDamagerHitboxes.addAndUpdateAll(e.getDamagerHitboxes(), Board.CELL_SIZE);
        }
        for(Entity e: enemyEntities) {
            slottedEnemyBodyHitboxes.addAndUpdateAll(e.getBodyHitboxes(), Board.CELL_SIZE);
            slottedEnemyDamagerHitboxes.addAndUpdateAll(e.getDamagerHitboxes(), Board.CELL_SIZE);
        }

        //hitboxCollisionEventManager.callEvents(slottedPlayerBodyHitboxes, slottedEnemyDamagerHitboxes);
        hitboxCollisionEventManager.callEvents(slottedEnemyBodyHitboxes, slottedPlayerDamagerHitboxes);

        /*for(Wall wall: board.getWalls()) {
            for(Entity entity: playerEntities) {
                wall.collide(entity);
            }
        }*/
    }

    public void render(Camera camera) {
        entityRenderer.render(camera, playerEntities, enemyEntities);
    }

    public void addEntity(Entity e) {
        newEntities.add(e);
    }

    private void addNewEntities() {
        while(newEntities.size() > 0) {
            Entity e = newEntities.remove(0);

            switch (e.getTeam()) {
                case PLAYER:
                    playerEntities.add(e);
                    break;
                case ENEMY:
                    enemyEntities.add(e);
                    break;
            }

            /*for(BodyHitbox h : e.getBodyHitboxes()) {
                if (h.getTeam() == Team.PLAYER) {
                    playerBodyHitboxes.add(h);
                } else if (h.getTeam() == Team.ENEMY) {
                    enemyBodyHitboxes.add(h);
                }
            }
            for(DamagerHitbox h : e.getDamagerHitboxes()) {
                if (h.getTeam() == Team.PLAYER) {
                    playerDamagerHitboxes.add(h);
                } else if (h.getTeam() == Team.ENEMY) {
                    enemyDamagerHitboxes.add(h);
                }
            }*/
        }
    }

    public void setCurrent() {
        EntityManager.current = this;
    }
}
