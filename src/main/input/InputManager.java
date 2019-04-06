package main.input;

import main.views.View;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
    private static View focusedView = null;
    private static LinkedList<Double[]> pressedActionKeys, heldActionKeys, releasedActionKeys;

    public static void init(long w) {
        pressedActionKeys = new LinkedList<Double[]>();
        heldActionKeys = new LinkedList<Double[]>();
        releasedActionKeys = new LinkedList<Double[]>();


        glfwSetKeyCallback(w, (window, key, scancode, action, mods) -> {
            if(isActionKey(key)) {
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
        });
    }

    private static boolean isActionKey(int key) {
        return true;
    }

    public static void update(double delta) {
        for(Double[] d: heldActionKeys) {
            d[1] += delta;
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
