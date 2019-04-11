package main.views;

import main.input.InputManager;
import rendering.FrameBuffer;

import java.util.LinkedList;

public abstract class View {
    public int priority = 0;
    protected int width, height;
    protected boolean isFocused = false;
    protected boolean updateIfUnfocused = false;
    protected boolean drawIfUnfocused = false;

    protected boolean shouldBeDestroyed = false;
    protected boolean shouldSortSubViews = false;

    protected FrameBuffer mainFrameBuffer;
    protected View parentView;
    protected LinkedList<View> subViews = new LinkedList<>();

    public View(View parentView, int width, int height) {
        this.parentView = parentView;
        this.width = width;
        this.height = height;
        mainFrameBuffer = new FrameBuffer(width, height);
    }

    public void update(double delta) {
        updateSubViews(delta);
        if(isFocused || updateIfUnfocused)
            updateSelf(delta);
    }

    public void draw() {
        drawSubViews();
        if(isFocused || drawIfUnfocused)
            drawSelf();
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

    protected void setFocusedSubView(View view) {
        for(View v: subViews) {
            if(v.equals(view))
                v.setFocused(true);
            else
                v.setFocused(false);
        }
    }

    public abstract void updateSelf(double delta);
    public abstract void drawSelf();

    public abstract void processViewAction(String action);
    protected void sendViewAction(String action) {
        if(parentView != null)
            parentView.processViewAction(action);
    }

    public FrameBuffer getMainFrameBuffer() {
        return mainFrameBuffer;
    }

    private void sortSubViews() {
        for(int i = 0; i < subViews.size()-1; i++) {
            for(int j = 0; j < i; j++) {
                if(subViews.get(j).priority < subViews.get(j+1).priority) {
                    subViews.add(j, subViews.remove(j+1));
                }
            }
        }
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public void setUpdateIfUnfocused(boolean updateIfUnfocused) {
        this.updateIfUnfocused = updateIfUnfocused;
    }

    public void setDrawIfUnfocused(boolean drawIfUnfocused) {
        this.drawIfUnfocused = drawIfUnfocused;
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

    public boolean isFocused() {
        return isFocused;
    }

    public void cleanUp() {
        for(View v: subViews) {
            v.cleanUp();
        }
    }
}
