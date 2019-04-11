package rendering.textures;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class TextureManager {
    private static final LinkedList<String> textureNames = new LinkedList<>();
    private static final HashMap<Integer, SpriteTexture> textures = new HashMap<>();

    private static final String FILE_PREFIX = "res/";
    private static final String FILE_POSTFIX = ".png";

    public static void loadAllTextures() {
        SpriteData.loadAll();
    }

    public static SpriteTexture loadTexture(String fileName, int numRows, boolean hasTransparency) {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        try {
            InputStream in = new FileInputStream(FILE_PREFIX + fileName + FILE_POSTFIX);
            PNGDecoder decoder = new PNGDecoder(in);

            System.out.println("width="+decoder.getWidth());
            System.out.println("height="+decoder.getHeight());

            ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
            decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            buf.flip();

            in.close();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);

        } catch(Exception e) {
            System.out.println("COULD NOT LOAD TEXTURE: " + e.getMessage());
            glDeleteTextures(texID);
            return null;
        }

        glBindTexture(GL_TEXTURE_2D, 0);

        SpriteTexture spriteTexture = new SpriteTexture(texID, numRows, hasTransparency);
        textures.put(textureNames.size(), spriteTexture);
        textureNames.add(fileName);
        return spriteTexture;
    }

    public static void cleanUp() {
        for(Integer key: textures.keySet()) {
            textures.get(key).destroy();
        }
        textures.clear();
        textureNames.clear();
    }
}
