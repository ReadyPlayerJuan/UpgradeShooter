package main.menus;

import main.input.InputManager;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

public abstract class ButtonUI extends UI {
    private Sprite sprite;

    private int hoverWidth, hoverHeight;
    private boolean hovering;
    private boolean clicked;

    public ButtonUI(UI parent, SpriteData spriteData, float relativeX, float relativeY, int size, int hoverWidth, int hoverHeight) {
        super(parent, relativeX, relativeY, size, size);

        this.hoverWidth = hoverWidth;
        this.hoverHeight = hoverHeight;

        sprite = new Sprite(spriteData);
        sprite.setScale(size/2);
    }

    public abstract void buttonAction();

    @Override
    public void updateSelf(double delta) {
        hovering = false;
        if(InputManager.mouseX > worldX-hoverWidth/2 && InputManager.mouseX < worldX+hoverWidth/2 &&
                InputManager.mouseY > worldY-hoverHeight/2 && InputManager.mouseY < worldY+hoverHeight/2) {
            hovering = true;

            if(InputManager.keyHeld(InputManager.LEFT_CLICK)) {
                clicked = true;
            } else {
                if(clicked) {
                    //just released click on button
                    buttonAction();
                }
                clicked = false;
            }
        } else {
            clicked = false;
        }

        sprite.setPosition(worldX, worldY);
        if(clicked)
            sprite.setImageIndex(2);
        else if(hovering)
            sprite.setImageIndex(1);
        else
            sprite.setImageIndex(0);
    }

    @Override
    public void drawSelf() {
        sprite.draw();
    }
}
