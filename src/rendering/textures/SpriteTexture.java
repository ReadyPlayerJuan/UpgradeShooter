package rendering.textures;

import org.lwjgl.opengl.GL30;

public class SpriteTexture {
    private int textureID;
    private int numRows;

    public SpriteTexture(int textureID, int numRows) {
        this.textureID = textureID;
        this.numRows = numRows;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumRows() {
        return numRows;
    }

    public void destroy() {
        GL30.glDeleteTextures(textureID);
    }
}
