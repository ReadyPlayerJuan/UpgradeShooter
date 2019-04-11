package main.input;

import main.views.View;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
    private static View focusedView = null;
    private static LinkedList<Double[]> pressedActionKeys, heldActionKeys, releasedActionKeys;
    public static double mouseX, mouseY;

    public static void init(long w) {
        pressedActionKeys = new LinkedList<Double[]>();
        heldActionKeys = new LinkedList<Double[]>();
        releasedActionKeys = new LinkedList<Double[]>();


        glfwSetKeyCallback(w, (window, key, scancode, action, mods) -> {
            processKey(key, action, mods);
        });
        glfwSetMouseButtonCallback(w, (window, button, action, mods) -> {
            processKey(button+1000, action, mods);
        });
        glfwSetCursorPosCallback(w, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });
    }

    private static void processKey(int key, int action, int mods) {
        if (action == GLFW_PRESS) {
            //add to pressed keys list
            pressedActionKeys.add(new Double[]{(double) key, 0.0});
            heldActionKeys.add(new Double[]{(double) key, 0.0});
        } else if (action == GLFW_RELEASE) {
            //remove from pressed keys list
            for (int i = 0; i < heldActionKeys.size(); i++) {
                if (heldActionKeys.get(i)[0] == key) {
                    releasedActionKeys.add(heldActionKeys.remove(i));
                    break;
                }
            }
        }
    }

    public static void update(double delta) {
        for(Double[] d: heldActionKeys) {
            d[1] += delta;
            //System.out.println(d[0] + " " + d[1]);
        }

        pressedActionKeys.clear();
        releasedActionKeys.clear();
        glfwPollEvents();
    }

    public static void setFocusedView(View v) {
        if(focusedView != null)
            focusedView.setFocused(false);
        focusedView = v;
        //new focused view sets its own focused flag, otherwise would create an infinite loop
    }

    public static LinkedList<Double[]> getPressedActionKeys() {
        return pressedActionKeys;
    }

    public static LinkedList<Double[]> getHeldActionKeys() {
        return heldActionKeys;
    }

    public static LinkedList<Double[]> getReleasedActionKeys() {
        return releasedActionKeys;
    }

    public static View getFocusedView() {
        return focusedView;
    }
}
