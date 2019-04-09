package main.views;

import main.input.InputManager;
import rendering.FrameBuffer;

import java.util.LinkedList;

public abstract class View {
    public int draw_priority = 0;
    protected int width, height;
    protected boolean shouldBeDestroyed = false;
    protected boolean shouldSortSubViews = false;
    protected boolean isFocused = false;

    protected FrameBuffer mainFrameBuffer;
    protected LinkedList<View> subViews;

    public View(int width, int height) {
        this.width = width;
        this.height = height;
        mainFrameBuffer = new FrameBuffer(width, height);
        subViews = new LinkedList<View>();
    }

    public void update(double delta) {
        updateSubViews(delta);
        updateSelf(delta);
    }

    public void draw() {
        drawSubViews();
        drawSelf();
    }

    public void setFocused(boolean focused) {
        if(focused) {
            isFocused = true;
            InputManager.setFocusedView(this);
        } else {
            isFocused = false;
        }
    }

    public void addSubView(View v) {
        subViews.add(v);
        shouldSortSubViews = true;
    }

    private void updateSubViews(double delta) {
        if(shouldSortSubViews) { sortSubViews(); }
        for(int i = 0; i < subViews.size(); i++) {
            View v = subViews.get(i);
            v.update(delta);
            if(v.shouldBeDestroyed) {
                subViews.remove(i).cleanUp();
                i--;
            }
        }
    }

    private void drawSubViews() {
        if(shouldSortSubViews) { sortSubViews(); }
        for(View v: subViews) {
            v.draw();
        }
    }

    public abstract void updateSelf(double delta);
    public abstract void drawSelf();

    /*public void drawMainView(double x, double y, double width, double height) {
        //mainFrameBuffer.draw(x, y, scale);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, mainFrameBuffer.getTexture());

        glBegin(GL_QUADS);
        glColor4f(1, 1, 1, 1);
        glTexCoord2f(0, 0); glVertex2d(x - width*0.5, y - height*0.5);
        glTexCoord2f(1, 0); glVertex2d(x + width*0.5, y - height*0.5);
        glTexCoord2f(1, 1); glVertex2d(x + width*0.5, y + height*0.5);
        glTexCoord2f(0, 1); glVertex2d(x - width*0.5, y + height*0.5);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }*/

    /*public void drawMainView(int x, int y) {

    }*/

    public FrameBuffer getMainFrameBuffer() {
        return mainFrameBuffer;
    }

    private void sortSubViews() {
        for(int i = 0; i < subViews.size()-1; i++) {
            for(int j = 0; j < i; j++) {
                if(subViews.get(j).draw_priority < subViews.get(j+1).draw_priority) {
                    subViews.add(j, subViews.remove(j+1));
                }
            }
        }
    }

    public boolean shouldBeDestroyed() {
        return shouldBeDestroyed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void cleanUp() {
        for(View v: subViews) {
            v.cleanUp();
        }
    }
}
