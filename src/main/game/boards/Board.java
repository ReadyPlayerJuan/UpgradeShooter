package main.game.boards;

import main.views.GameView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rendering.Graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class Board {
    public static final double CELL_SIZE = 100.0;

    protected CellSlotter<Wall> slottedWalls;

    protected GameView parentView;

    protected Wall[] walls;
    protected FloatBuffer vertexBuffer;
    protected IntBuffer indexBuffer;
    protected int vertexVbo, vao;

    public Board(GameView parentVew) {
        this.parentView = parentVew;
        slottedWalls = new CellSlotter<>();
    }

    public void render(Camera camera) {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);

        Graphics.wallShader.start();
        Graphics.wallShader.setCameraAndViewSize(camera);
        Graphics.wallShader.setColor(0, 0, 0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer);
        Graphics.wallShader.stop();

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    protected void slotWalls() {
        for(Wall wall: walls) {
            wall.updateSlotPositions(Board.CELL_SIZE);
        }

        slottedWalls.clear();
        slottedWalls.addAll(walls);
    }

    public void update(double delta) {
        /*for(Projectile p: projectiles) {
            p.update(delta);
        }

        for(hitboxes h: hitboxes) {
            if(h.canCollide()) {
                for(Projectile p: projectiles) {
                    if(p.isColliding(h)) {
                        h.collide(p);
                        p.collide(h);
                    }
                }
            }
        }

        for(int i = 0; i < projectiles.size(); i++) {
            if(projectiles.get(i).shouldBeDestroyed()) {
                projectiles.remove(i);
                i--;
            }
        }*/
    }

    public Wall[] getWalls() {
        return walls;
    }

    public CellSlotter<Wall> getSlottedWalls() {
        return slottedWalls;
    }
}
