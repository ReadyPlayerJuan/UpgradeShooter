package rendering.textures;

import org.lwjgl.opengl.GL30;

public class SpriteTexture {
    private int textureID;
    private int numRows;
    private boolean hasTransparency;

    public SpriteTexture(int textureID, int numRows, boolean hasTransparency) {
        this.textureID = textureID;
        this.numRows = numRows;
        this.hasTransparency = hasTransparency;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumRows() {
        return numRows;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void destroy() {
        GL30.glDeleteTextures(textureID);
    }
}
