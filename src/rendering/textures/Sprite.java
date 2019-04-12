package rendering.textures;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Sprite {
    protected SpriteTexture texture;
    protected float x, y;
    protected float rotation;
    protected float width, height;
    protected int imageIndex;
    protected float[] tint;

    public Sprite(SpriteData spriteData) {
        this.texture = spriteData.getTexture();
        this.x = 0;
        this.y = 0;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.rotation = 0;
        this.imageIndex = 0;
        tint = new float[] {1, 1, 1};
    }

    public Sprite setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Sprite setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public Sprite setScale(float scale) {
        setScale(scale, scale);
        return this;
    }

    public Sprite setScale(float scaleX, float scaleY) {
        this.width = scaleX * texture.getWidth();
        this.height = scaleY * texture.getHeight();
        return this;
    }

    public Sprite setSize(float size) {
        setSize(size, size);
        return this;
    }

    public Sprite setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Sprite setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
        return this;
    }

    public Sprite setTint(float r, float g, float b) {
        tint[0] = r;
        tint[1] = g;
        tint[2] = b;
        return this;
    }

    public SpriteTexture getTexture() {
        return texture;
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

    public float getScaleX() {
        return width / texture.getWidth();
    }

    public float getScaleY() {
        return height / texture.getHeight();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public float[] getTint() {
        return tint;
    }

    public void draw() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        if(texture.hasTransparency()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        glColor3f(tint[0], tint[1], tint[2]);
        glBegin(GL_QUADS);
        drawTexCoord(1, 0); drawVertex(width/2, -height/2);
        drawTexCoord(0, 0); drawVertex(-width/2, -height/2);
        drawTexCoord(0, 1); drawVertex(-width/2, height/2);
        drawTexCoord(1, 1); drawVertex(width/2, height/2);
        glEnd();

        if(texture.hasTransparency()) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    protected void drawTexCoord(float offX, float offY) {
        int numRows = texture.getNumRows();
        int numCols = texture.getNumCols();
        float imageOffX = imageIndex % numCols;
        float imageOffY = imageIndex / numCols;
        glTexCoord2f((offX + imageOffX) / numCols, (offY + imageOffY) / numRows);
    }

    protected void drawVertex(float offW, float offH) {
        float offPosX = (offW * (float)Math.cos(rotation) - offH * (float)Math.sin(rotation));
        float offPosY = (offW * (float)Math.sin(rotation) + offH * (float)Math.cos(rotation));
        glVertex2d(x + offPosX, y + offPosY);
    }
}
