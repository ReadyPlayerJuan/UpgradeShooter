package rendering.textures;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class NineSliceSprite extends Sprite {
    private float cornerSize = 0;

    public NineSliceSprite(SpriteData spriteData) {
        super(spriteData);
    }

    public Sprite setCornerSize(float cornerSize) {
        this.cornerSize = cornerSize;
        return this;
    }

    public Sprite setCornerScale(float cornerScale) {
        this.cornerSize = (texture.getWidth() / 3) * cornerScale;
        return this;
    }

    public float getCornerSize() {
        return cornerSize;
    }

    public float getCornerScale() {
        return cornerSize / (texture.getWidth() / 3);
    }

    @Override
    public void draw() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        if(texture.hasTransparency()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        glColor3f(1, 1, 1);
        glBegin(GL_QUADS);
        for(float x = 0; x < 3; x++) {
            for(float y = 0; y < 3; y++) {
                float minX, minY, maxX, maxY;
                if(x < 2)
                    minX = -width/2 + (x)*cornerSize;
                else
                    minX = width/2 + (x-3)*cornerSize;
                if(x+1 < 2)
                    maxX = -width/2 + (x+1)*cornerSize;
                else
                    maxX = width/2 + (x-2)*cornerSize;
                if(y < 2)
                    minY = -height/2 + (y)*cornerSize;
                else
                    minY = height/2 + (y-3)*cornerSize;
                if(y+1 < 2)
                    maxY = -height/2 + (y+1)*cornerSize;
                else
                    maxY = height/2 + (y-2)*cornerSize;

                drawTexCoord((x+1)/3, y/3); drawVertex(maxX, minY);
                drawTexCoord(x/3, y/3); drawVertex(minX, minY);
                drawTexCoord(x/3, (y+1)/3); drawVertex(minX, maxY);
                drawTexCoord((x+1)/3, (y+1)/3); drawVertex(maxX, maxY);
            }
        }
        glEnd();

        if(texture.hasTransparency()) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }
}
