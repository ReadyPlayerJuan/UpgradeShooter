package rendering.fonts;

/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static rendering.fonts.GLFWUtil.glfwInvoke;

public class FontTester3 {
    private int fontHeight = 24;

    private TrueTypeFont truetype;

    private long window;
    private int ww = 800;
    private int wh = 600;

    public static void main(String[] args) {
        new FontTester3().run("STB Truetype Demo");
    }

    protected void run(String title) {
        try {
            init(title);
            truetype = new TrueTypeFont(fontHeight);

            loop();
        } finally {
            try {
                destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void init(String title) {
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        long monitor = glfwGetPrimaryMonitor();

        this.window = glfwCreateWindow(ww, wh, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center window
        GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(monitor));

        glfwSetWindowPos(
                window,
                (vidmode.width() - ww) / 2,
                (vidmode.height() - wh) / 2
        );

        // Create context
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glfwSwapInterval(1);
        glfwShowWindow(window);

        glfwInvoke(window, this::windowSizeChanged, this::framebufferSizeChanged);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f); // BG color
            glClear(GL_COLOR_BUFFER_BIT);

            glColor3f(255f / 255f, 0f / 255f, 0f / 255f); // Text color
            truetype.drawText("WASSUP BOYYY", 4.0f, fontHeight * 0.5f + 4.0f);

            glfwSwapBuffers(window);
        }

        truetype.cleanUp();
    }

    private void windowSizeChanged(long window, int width, int height) {
        this.ww = width;
        this.wh = height;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
    }

    private void framebufferSizeChanged(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }

    private void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

}