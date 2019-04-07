package main.game.boards;

import main.views.GameView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rendering.shaders.wall_shader.WallShader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class Board {
    private static final WallShader wallShader = new WallShader();
    //private static final double CELL_SIZE = 100.0;

    protected GameView parentView;

    protected Wall[] walls;
    protected FloatBuffer vertexBuffer;
    protected IntBuffer indexBuffer;
    protected int vertexVbo, vao;

    public Board(GameView parentVew) {
        this.parentView = parentVew;
    }

    public void render(Camera camera) {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);

        wallShader.start();
        wallShader.setCameraAndViewSize(camera);
        wallShader.setColor(0, 0, 0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer);
        wallShader.stop();

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
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
}
