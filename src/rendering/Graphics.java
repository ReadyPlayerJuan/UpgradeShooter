package rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import rendering.fonts.TrueTypeFont;
import rendering.shaders.ShaderProgram;
import rendering.shaders.entity_shader.EntityShader;
import rendering.shaders.wall_shader.WallShader;

public class Graphics {
    public static TrueTypeFont debugFont; private static final int debugFontSize = 20;
    public static TrueTypeFont titleFont; private static final int titleFontSize = 48;
    public static TrueTypeFont smallFont; private static final int smallFontSize = 26;
    public static TrueTypeFont mediumFont; private static final int mediumFontSize = 40;

    public static WallShader wallShader;
    public static EntityShader entityShader;

    private static ArrayList<Integer> vaos = new ArrayList<>();
    private static ArrayList<Integer> vbos = new ArrayList<>();
    private static ArrayList<ShaderProgram> shaders = new ArrayList<>();
    private static ArrayList<FrameBuffer> frameBuffers = new ArrayList<>();

    public static void initShaders() {
        wallShader = new WallShader();
        entityShader = new EntityShader();
    }

    public static void initFonts() {
        try {
            debugFont = new TrueTypeFont(debugFontSize);
            titleFont = new TrueTypeFont(titleFontSize);
            smallFont = new TrueTypeFont(smallFontSize);
            mediumFont = new TrueTypeFont(mediumFontSize);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    public static int createVao() {
        int vao = GL30.glGenVertexArrays();
        vaos.add(vao);
        return vao;
    }

    public static int createVbo() {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        return vbo;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer storeDataInFloatBuffer(FloatBuffer buffer, float[] data) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(IntBuffer buffer, int[] data) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static void addShader(ShaderProgram shader) {
        shaders.add(shader);
    }

    public static void addFrameBuffer(FrameBuffer frameBuffer) {
        frameBuffers.add(frameBuffer);
    }

    public static void setColor(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    public static void clear(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public static void enableBlend() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableBlend() {
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void cleanUp() {
        for(int vao: vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for(int vbo: vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for(ShaderProgram shader: shaders) {
            shader.cleanUp();
        }

        for(FrameBuffer frameBuffer: frameBuffers) {
            frameBuffer.cleanUp();
        }

        debugFont.cleanUp();
        titleFont.cleanUp();
    }
}
