package main.menus;

import java.util.Collection;
import java.util.LinkedList;

public abstract class UI {
    protected UI parent;
    protected LinkedList<UI> children = new LinkedList<>();

    public int priority = 0;
    protected float relativeX, relativeY;
    protected float worldX, worldY;
    protected int width, height;

    protected boolean shouldSortChildren = false;
    protected boolean shouldBeDestroyed = false;

    protected boolean visible = true;

    public UI(UI parent, float relativeX, float relativeY, int width, int height) {
        this.parent = parent;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
        worldX = 0;
        worldY = 0;

        if(parent != null) {
            parent.addChild(this);
        }
    }

    public void update(double delta) {
        if(visible) {
            updateSelf(delta);

            if (parent != null) {
                worldX = relativeX + parent.getWorldX();
                worldY = relativeY + parent.getWorldY();
            } else {
                worldX = relativeX;
                worldY = relativeY;
            }

            updateChildren(delta);
        }
    }

    public void draw() {
        if(visible) {
            drawSelf();
            drawChildren();
        }
    }

    public void addChild(UI ui) {
        children.add(ui);
        shouldSortChildren = true;
    }

    public void addChildren(UI... uis) {
        for(UI ui: uis)
            children.add(ui);
        shouldSortChildren = true;
    }

    public void addChildren(Collection<UI> uis) {
        children.addAll(uis);
        shouldSortChildren = true;
    }

    private void updateChildren(double delta) {
        if(shouldSortChildren) { sortChildren(); }
        for(int i = 0; i < children.size(); i++) {
            UI ui = children.get(i);
            ui.update(delta);
            if(ui.shouldBeDestroyed) {
                children.remove(i).cleanUp();
                i--;
            }
        }
    }

    private void drawChildren() {
        if(shouldSortChildren) { sortChildren(); }
        for(UI ui: children) {
            ui.draw();
        }
    }

    public abstract void updateSelf(double delta);
    public abstract void drawSelf();

    private void sortChildren() {
        for(int i = 0; i < children.size()-1; i++) {
            for(int j = 0; j < i; j++) {
                if(children.get(j).priority < children.get(j+1).priority) {
                    children.add(j, children.remove(j+1));
                }
            }
        }
    }

    public void setRelativePosition(float x, float y) {
        relativeX = x;
        relativeY = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean shouldBeDestroyed() {
        return shouldBeDestroyed;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void cleanUp() {
        for(UI ui: children) {
            ui.cleanUp();
        }
    }
}
