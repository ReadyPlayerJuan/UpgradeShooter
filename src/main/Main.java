package main;

import main.input.InputManager;
import main.util.EfficiencyMetricType;
import main.util.EfficiencyMetrics;
import main.util.SettingType;
import main.util.Settings;
import main.views.MainView;
import rendering.Graphics;
import rendering.WindowManager;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import rendering.textures.TextureManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {
    public void run() {
        Settings.loadSettings();
        WindowManager.createWindow();
        initGL();

        Graphics.initShaders();
        Graphics.initFonts();
        EfficiencyMetrics.init();
        TextureManager.loadAllTextures();

        InputManager.init(WindowManager.window);

        MainView mainView = new MainView(Settings.get(SettingType.RESOLUTION_WIDTH), Settings.get(SettingType.RESOLUTION_HEIGHT));

        while (!glfwWindowShouldClose(WindowManager.window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            WindowManager.pollEvents();

            EfficiencyMetrics.frameStart();
            InputManager.update(WindowManager.getDelta());

            EfficiencyMetrics.startTimer(EfficiencyMetricType.ENTIRE_FRAME);
            EfficiencyMetrics.startTimer(EfficiencyMetricType.UPDATE_ALL);
            mainView.update(WindowManager.getDelta());
            EfficiencyMetrics.stopTimer(EfficiencyMetricType.UPDATE_ALL);
            EfficiencyMetrics.startTimer(EfficiencyMetricType.DRAW_ALL);
            mainView.draw();
            EfficiencyMetrics.stopTimer(EfficiencyMetricType.DRAW_ALL);
            EfficiencyMetrics.stopTimer(EfficiencyMetricType.ENTIRE_FRAME);


            glOrtho(-1, 1, -1, 1, 0, 1);
            glColor4f(1, 1, 1, 1);
            mainView.getMainFrameBuffer().draw(0, 0, 2, 2);

            WindowManager.updateWindow();
            EfficiencyMetrics.frameEnd();
        }

        WindowManager.destroyWindow();
        TextureManager.cleanUp();
        Graphics.cleanUp();
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