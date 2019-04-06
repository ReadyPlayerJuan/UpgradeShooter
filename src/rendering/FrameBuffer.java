package rendering;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import main.SettingType;
import main.Settings;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rendering.shaders.ShaderProgram;

import java.nio.ByteBuffer;

public class FrameBuffer {
    private int fboID;
    private int textureID;
    private int width, height;

    private boolean automaticOrtho = true;

    private float[] quadPositions = {-1, -1, 1, -1, -1, 1, 1, 1};
    private float[] quadTextureCoords = {0, 1, 1, 1, 0, 0, 1, 0};
    private RawModel quad;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;

        /*for(int i = 0; i < 8; i++) {
            if(i % 2 == 0)
                quadPositions[i] *= width/2;
            else
                quadPositions[i] *= height/2;
        }*/
        quad = Loader.loadToVAO(quadPositions, quadTextureCoords);


        //texture
        glEnable(GL_TEXTURE_2D);
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);


        //frame buffer
        fboID = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER, fboID);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureID, 0);
        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER);
        if (result != GL_FRAMEBUFFER_COMPLETE) {
            glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
            glDeleteFramebuffers(fboID);

            System.exit(0);//throw new Exception("" + result);
        }
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    public void draw(double x, double y, double w, double h) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, getTexture());

        glBegin(GL_QUADS);
        glColor4f(1, 1, 1, 1);
        glTexCoord2f(0, 0); glVertex2d(x - w*0.5, y - h*0.5);
        glTexCoord2f(1, 0); glVertex2d(x + w*0.5, y - h*0.5);
        glTexCoord2f(1, 1); glVertex2d(x + w*0.5, y + h*0.5);
        glTexCoord2f(0, 1); glVertex2d(x - w*0.5, y + h*0.5);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    public void draw(double x, double y) {
        draw(x, y, width, height);
    }

    public void bindFrameBuffer() {
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        if(automaticOrtho)
            glOrtho(0, width, 0, height, 0, 1);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboID);
    }

    public void unbindFrameBuffer() {
        glViewport(0, 0, Settings.get(SettingType.RESOLUTION_WIDTH),
                Settings.get(SettingType.RESOLUTION_HEIGHT));
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //if(automaticOrtho)
            //glOrtho(-1, 1, 1, -1, 0, 1);
            //glOrtho(0, Settings.get(SettingType.RESOLUTION_WIDTH),
              //      Settings.get(SettingType.RESOLUTION_HEIGHT), 0, 0, 1);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void cleanUp() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glDeleteFramebuffersEXT(fboID);
        glDeleteTextures(textureID);
    }

    public void setAutomaticOrtho(boolean automaticOrtho) {
        this.automaticOrtho = automaticOrtho;
    }

    public int getID() {
        return fboID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTexture() {
        return textureID;
    }
}