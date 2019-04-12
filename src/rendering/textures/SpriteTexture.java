package rendering.textures;

import org.lwjgl.opengl.GL30;

public class SpriteTexture {
    private int textureID;
    private int width, height;
    private int numRows, numCols;
    private boolean hasTransparency;

    public SpriteTexture(int textureID, int width, int height, int numRows, int numCols, boolean hasTransparency) {
        this.textureID = textureID;
        this.width = width;
        this.height = height;
        this.numRows = numRows;
        this.numCols = numCols;
        this.hasTransparency = hasTransparency;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void destroy() {
        GL30.glDeleteTextures(textureID);
    }
}
