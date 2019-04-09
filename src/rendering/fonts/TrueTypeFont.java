package rendering.fonts;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class TrueTypeFont {
    private FontUtil fontUtil;
    private int fontTextureId;
    private float fontSize;

    public TrueTypeFont(String ttfFilename, float fontSize) throws Throwable {
        this.fontSize = fontSize;
        fontUtil = new FontUtil(ttfFilename, fontSize);
        generateTexture(fontUtil.getFontAsByteBuffer());
    }

    private void generateTexture(ByteBuffer bb) {
        this.fontTextureId = glGenTextures();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, this.fontTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                (int) fontUtil.getFontImageWidth(),
                (int) fontUtil.getFontImageHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, bb);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void drawFontTexture(int x, int y) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, this.fontTextureId);

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex3f(x, y, 0);

        glTexCoord2f(1, 0);
        glVertex3f(x + fontUtil.getFontImageWidth(), y, 0);

        glTexCoord2f(1, 1);
        glVertex3f(x + fontUtil.getFontImageWidth(), y + fontUtil.getFontImageHeight(), 0);

        glTexCoord2f(0, 1);
        glVertex3f(x, y + fontUtil.getFontImageHeight(), 0);

        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    public void drawText(String text, int xPosition, int yPosition) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, this.fontTextureId);
        glBegin(GL_QUADS);
        int xTmp = xPosition;
        int yTmp = yPosition;
        for (char c : text.toCharArray()) {
            if(c == ' ') {
                xTmp += fontUtil.getCharWidth(c);
                continue;
            } else if(c == '\n') {
                xTmp = xPosition;
                yTmp -= fontSize;
                continue;
            }

            float width = fontUtil.getCharWidth(c);
            float height = fontUtil.getCharHeight();
            float w = 1f / fontUtil.getFontImageWidth() * width;
            float h = 1f / fontUtil.getFontImageHeight() * height;
            float x = 1f / fontUtil.getFontImageWidth() * fontUtil.getCharX(c);
            float y = 1f / fontUtil.getFontImageHeight() * fontUtil.getCharY(c);

            glTexCoord2f(x, y);
            glVertex2f(xTmp, yTmp + height);

            glTexCoord2f(x + w, y);
            glVertex2f(xTmp + width, yTmp + height);

            glTexCoord2f(x + w, y + h);
            glVertex2f(xTmp + width, yTmp);

            glTexCoord2f(x, y + h);
            glVertex2f(xTmp, yTmp);

            xTmp += width;
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);
    }
}