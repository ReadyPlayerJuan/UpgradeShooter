package rendering.textures;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Sprite {
    private SpriteTexture texture;
    private float x, y;
    private float rotation, scale;
    private int imageIndex;

    public Sprite(SpriteData spriteData, float x, float y, float rotation, float scale, int imageIndex) {
        this.texture = spriteData.getTexture();
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scale = scale;
        this.imageIndex = imageIndex;
    }

    public Sprite(SpriteData spriteData, float x, float y, float scale) {
        this(spriteData, x, y, 0, scale, 0);
    }

    public Sprite(SpriteData spriteData) {
        this(spriteData, 0, 0, 0, 1, 0);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public SpriteTexture getTexture() {
        return texture;
    }

    public void draw() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        if(texture.hasTransparency()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        glColor3f(1, 1, 1);
        glBegin(GL_QUADS);
        vertex(1, -1);
        vertex(-1, -1);
        vertex(-1, 1);
        vertex(1, 1);
        glEnd();

        if(texture.hasTransparency()) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    private void vertex(int offX, int offY) {
        int numRows = texture.getNumRows();
        float imageOffX = imageIndex % numRows;
        float imageOffY = imageIndex / numRows;
        glTexCoord2f((offX * 0.5f + 0.5f + imageOffX) / numRows, (offY * -0.5f + 0.5f + imageOffY) / numRows);

        float offPosX = scale * (offX * (float)Math.cos(rotation) - offY * (float)Math.sin(rotation));
        float offPosY = scale * (offX * (float)Math.sin(rotation) + offY * (float)Math.cos(rotation));
        glVertex2d(x + offPosX, y + offPosY);
    }
}
