package main.game.entities;

import main.game.boards.Camera;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import rendering.Graphics;
import rendering.textures.Sprite;
import rendering.textures.SpriteTexture;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

public class EntityRenderer {
    private final int MAX_SPRITES = 100000;
    private final int SPRITE_DATA_LENGTH = 5; //number of floats per sprite

    private final FloatBuffer spriteDataBuffer = BufferUtils.createFloatBuffer(MAX_SPRITES * SPRITE_DATA_LENGTH);
    private float[] spriteData;
    private int spriteDataPointer = 0;
    private int vao, vbo;

    //private LinkedList<Sprite> testSprites;

    private HashMap<SpriteTexture, LinkedList<Sprite>> sprites = new HashMap<>();

    public EntityRenderer() {
        vao = Graphics.createVao();
        GL30.glBindVertexArray(vao);

        vbo = Graphics.createVbo();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, MAX_SPRITES * SPRITE_DATA_LENGTH * 4, GL15.GL_STREAM_DRAW);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, SPRITE_DATA_LENGTH * 4, 0 * 4);
        GL20.glVertexAttribPointer(1, 1, GL11.GL_FLOAT, false, SPRITE_DATA_LENGTH * 4, 2 * 4);
        GL20.glVertexAttribPointer(2, 1, GL11.GL_FLOAT, false, SPRITE_DATA_LENGTH * 4, 3 * 4);
        GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, SPRITE_DATA_LENGTH * 4, 4 * 4);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);


        /*testSprites = new LinkedList<>();
        int numTotal = (int)(MAX_SPRITES * 0.95);
        int screenWidth = Settings.get(SettingType.RESOLUTION_WIDTH);
        int screenHeight = Settings.get(SettingType.RESOLUTION_HEIGHT);
        float ratio = (float)screenWidth / screenHeight;
        int numX = (int)Math.sqrt(numTotal * ratio);
        int numY = (int)(numX / ratio);
        System.out.println(numX + " " + numY + " " + numX*numY);
        for(double x = -screenWidth/2; x < screenWidth/2; x += (double)screenWidth / numX) {
            for(double y = -screenHeight/2; y < screenHeight/2; y += (double)screenHeight / numY) {
                testSprites.add(new Sprite(SpriteData.PLAYER, x, y, 0, 20, (int)(Math.random() * 4)));
            }
        }*/
    }

    public void registerSprite(Sprite sprite) {
        SpriteTexture texture = sprite.getTexture();
        LinkedList<Sprite> spriteList = sprites.get(texture);
        if(spriteList != null) {
            spriteList.add(sprite);
        } else {
            spriteList = new LinkedList<Sprite>();
            spriteList.add(sprite);
            sprites.put(texture, spriteList);
        }
    }

    public void render(Camera camera, LinkedList<Entity> playerEntities, LinkedList<Entity> enemyEntities) {
        sprites.clear();
        for (Entity e : playerEntities) {
            e.registerSprites(this);
        }
        for (Entity e : enemyEntities) {
            e.registerSprites(this);
        }
        /*for (Sprite s : testSprites) {
            registerSprite(s);
        }*/

        Graphics.entityShader.start();
        Graphics.entityShader.setCameraAndViewSize(camera);

        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);

        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for(SpriteTexture texture: sprites.keySet()) {
            LinkedList<Sprite> spriteList = sprites.get(texture);

            loadSpriteData(spriteList);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            Graphics.entityShader.setNumTextureRows(texture.getNumRows());

            GL11.glDrawArrays(GL11.GL_POINTS, 0, spriteList.size());
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);

        Graphics.entityShader.stop();
    }

    private void loadSpriteData(LinkedList<Sprite> spriteList) {
        spriteData = new float[spriteList.size() * SPRITE_DATA_LENGTH];
        spriteDataPointer = 0;

        for(Sprite s: spriteList) {
            spriteData[spriteDataPointer++] = (float)s.getX();//x
            spriteData[spriteDataPointer++] = (float)s.getY();//y
            spriteData[spriteDataPointer++] = (float)s.getScale();//scale
            spriteData[spriteDataPointer++] = (float)s.getRotation();//rotation
            spriteData[spriteDataPointer++] = (float)s.getImageIndex();//image index
        }

        spriteDataBuffer.clear();
        spriteDataBuffer.put(spriteData);
        spriteDataBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, spriteDataBuffer.capacity() * 4, GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, spriteDataBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
