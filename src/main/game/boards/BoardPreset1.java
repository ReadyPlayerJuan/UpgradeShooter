package main.game.boards;

import main.views.GameView;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class BoardPreset1 extends Board {
    public BoardPreset1(GameView parentView) {
        super(parentView);

        walls = new Wall[] {
            new Wall(0, 350, 500, 250),
            new Wall(500, 250, 400, -350),
            new Wall(400, -350, 100, -150),
            new Wall(100, -150, -100, -400),
            new Wall(-100, -400, -500, 0),
            new Wall(-500, 0, 0, 350),
        };

        slotWalls();



        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        vertexVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, walls.length * 8 * 4, GL15.GL_STREAM_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);


        vertexBuffer = BufferUtils.createFloatBuffer(walls.length * 12);
        indexBuffer = BufferUtils.createIntBuffer(walls.length * 6);

        float[] vertices = new float[walls.length * 12];
        int[] indices = new int[walls.length * 6];
        int vertexBufferIndex = 0;
        int indexBufferIndex = 0;
        for(int i = 0; i < walls.length; i++) {
            Wall wall = walls[i];

            vertices[vertexBufferIndex++] = (float)wall.getX1();
            vertices[vertexBufferIndex++] = (float)wall.getY1();
            vertices[vertexBufferIndex++] = (float)0.0;
            vertices[vertexBufferIndex++] = (float)wall.getX1();
            vertices[vertexBufferIndex++] = (float)wall.getY1()+10;
            vertices[vertexBufferIndex++] = (float)0.0;
            vertices[vertexBufferIndex++] = (float)wall.getX2();
            vertices[vertexBufferIndex++] = (float)wall.getY2();
            vertices[vertexBufferIndex++] = (float)0.0;
            vertices[vertexBufferIndex++] = (float)wall.getX2();
            vertices[vertexBufferIndex++] = (float)wall.getY2()+10;
            vertices[vertexBufferIndex++] = (float)0.0;

            indices[indexBufferIndex++] = i*4;
            indices[indexBufferIndex++] = i*4 + 1;
            indices[indexBufferIndex++] = i*4 + 2;
            indices[indexBufferIndex++] = i*4 + 2;
            indices[indexBufferIndex++] = i*4 + 1;
            indices[indexBufferIndex++] = i*4 + 3;
        }
        vertexBuffer.put(vertices);
        vertexBuffer.flip();

        indexBuffer.put(indices);
        indexBuffer.flip();


        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
