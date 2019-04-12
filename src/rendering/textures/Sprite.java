package rendering.textures;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Sprite {
    private SpriteTexture texture;
    private float x, y;
    private float rotation;
    private float width, height;
    private int imageIndex;

    public Sprite(SpriteData spriteData, float x, float y, float width, float height, float rotation, int imageIndex) {
        this.texture = spriteData.getTexture();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.imageIndex = imageIndex;
    }

    public Sprite(SpriteData spriteData, float x, float y, float width, float height) {
        this.texture = spriteData.getTexture();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = 0;
        this.imageIndex = 0;
    }

    public Sprite(SpriteData spriteData, float x, float y) {
        this.texture = spriteData.getTexture();
        this.x = x;
        this.y = y;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.rotation = 0;
        this.imageIndex = 0;
    }

    public Sprite(SpriteData spriteData) {
        this.texture = spriteData.getTexture();
        this.x = 0;
        this.y = 0;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.rotation = 0;
        this.imageIndex = 0;
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

    public float getScaleX() {
        return width / texture.getWidth();
    }

    public float getScaleY() {
        return height / texture.getHeight();
    }

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    public void setScale(float scaleX, float scaleY) {
        this.width = scaleX * texture.getWidth();
        this.height = scaleY * texture.getHeight();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setSize(float size) {
        setSize(size, size);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
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
        int numCols = texture.getNumCols();
        float imageOffX = imageIndex % numCols;
        float imageOffY = imageIndex / numCols;
        glTexCoord2f((offX * 0.5f + 0.5f + imageOffX) / numCols, (offY * -0.5f + 0.5f + imageOffY) / numRows);
        //System.out.println(((offX * 0.5f + 0.5f + imageOffX) / numCols) + " " + ((offY * -0.5f + 0.5f + imageOffY) / numRows));

        float offW = width * 0.5f;
        float offH = height * 0.5f;
        float offPosX = (offX * offW * (float)Math.cos(rotation) - offY * offH * (float)Math.sin(rotation));
        float offPosY = (offX * offW * (float)Math.sin(rotation) + offY * offH * (float)Math.cos(rotation));
        glVertex2d(x + offPosX, y + offPosY);
    }
}
