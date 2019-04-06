package main;

import main.input.InputManager;
import main.views.MainView;
import rendering.Loader;
import rendering.WindowManager;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {
    public void run() {
        Settings.loadSettings();
        WindowManager.createWindow();
        initGL();

        InputManager.init(WindowManager.window);
        WindowManager.init();

        MainView mainView = new MainView(Settings.get(SettingType.RESOLUTION_WIDTH), Settings.get(SettingType.RESOLUTION_HEIGHT));

        while (!glfwWindowShouldClose(WindowManager.window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            InputManager.update(WindowManager.getDelta());

            mainView.update(WindowManager.getDelta());
            mainView.draw();

            glOrtho(-1, 1, -1, 1, 0, 1);
            mainView.getMainFrameBuffer().draw(0, 0, 2, 2);

            WindowManager.updateWindow();
        }

        WindowManager.destroyWindow();
        WindowManager.cleanUp();
        Loader.cleanUp();
        mainView.cleanUp();
    }

    private void initGL() {
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glViewport(0, 0,
                Settings.get(SettingType.RESOLUTION_WIDTH),
                Settings.get(SettingType.RESOLUTION_HEIGHT));
        GL11.glOrtho(0,
                Settings.get(SettingType.RESOLUTION_WIDTH),
                Settings.get(SettingType.RESOLUTION_HEIGHT), 0, 0, 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();


    }

    public static void main(String[] argv) {
        new Main().run();
    }
}