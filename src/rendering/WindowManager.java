package rendering;

import main.util.SettingType;
import main.util.Settings;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class WindowManager {
    private static double lastFrameTime;
    private static double delta;
    private static int frameCount;
    private static double lastFpsUpdateTime;
    private static int fps;

    public static long window;

    public static void createWindow() {
        int windowWidth = Settings.get(SettingType.RESOLUTION_WIDTH);
        int windowHeight = Settings.get(SettingType.RESOLUTION_HEIGHT);

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_SAMPLES, 4);

        // Create the window
        window = glfwCreateWindow(windowWidth, windowHeight, "GAME GAME", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        /*glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });*/

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        // Enable v-sync
        glfwSwapInterval(1);

        // Center the window
        centerWindow();

        // Make the window visible
        glfwShowWindow(window);



        lastFrameTime = getTime();
        updateFps();
    }

    public static void updateWindow() {
        glfwSwapBuffers(window); // swap the color buffers
        // Poll for window events. The key callback above will only be invoked during this call.


        updateFps();
    }

    private static void updateFps() {
        double currentFrameTime = glfwGetTime();
        delta = (currentFrameTime - lastFrameTime);
        if(delta > 1) { delta = 0; }
        frameCount++;
        if(currentFrameTime - lastFpsUpdateTime > 1) {
            fps = frameCount;
            frameCount = 0;
            lastFpsUpdateTime = currentFrameTime;
        }
        lastFrameTime = currentFrameTime;
        //delta = 1.0 / 60;
    }

    public static void destroyWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void centerWindow() {
        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }

    public static double getDelta() {
        return delta;
    }

    public static int getFps() {
        return fps;
    }

    public static double getTime() {
        return lastFrameTime;
    }
}
