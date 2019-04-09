package rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import rendering.shaders.ShaderProgram;
import rendering.shaders.entity_shader.EntityShader;
import rendering.shaders.wall_shader.WallShader;

public class Graphics {
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
    }
}
