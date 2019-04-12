package main.menus;

import main.input.InputManager;
import rendering.textures.NineSliceSprite;
import rendering.textures.SpriteData;

public abstract class ButtonUI extends UI {
    private NineSliceSprite sprite;

    private int hoverWidth, hoverHeight;
    private boolean hovering, clicked;

    public ButtonUI(UI parent, SpriteData spriteData, float relativeX, float relativeY, int width, int height, int cornerSize) {
        super(parent, relativeX, relativeY, 0, 0);

        this.width = width;
        this.height = height;
        this.hoverWidth = width;
        this.hoverHeight = height;

        sprite = new NineSliceSprite(spriteData);
        sprite.setSize(width, height);
        sprite.setCornerSize(cornerSize);
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
