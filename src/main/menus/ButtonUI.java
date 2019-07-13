package main.menus;

import main.input.InputManager;
import rendering.textures.NineSliceSprite;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

public abstract class ButtonUI extends UI {
    private Sprite sprite;

    private int hoverWidth, hoverHeight;
    private boolean hovering, clicked;

    public ButtonUI(UI parent, SpriteData spriteData, float relativeX, float relativeY, int width, int height, int cornerSize) {
        super(parent, relativeX, relativeY, 0, 0);

        this.width = width;
        this.height = height;
        this.hoverWidth = width;
        this.hoverHeight = height;

        sprite = new NineSliceSprite(spriteData).setCornerSize(cornerSize).setSize(width, height);
    }

    public abstract void buttonAction();

    @Override
    public void updateSelf(double delta) {
        hovering = false;
        if(InputManager.mouseX > worldX+width/2-hoverWidth/2 && InputManager.mouseX < worldX+width/2+hoverWidth/2 &&
                InputManager.mouseY > worldY+height/2-hoverHeight/2 && InputManager.mouseY < worldY+height/2+hoverHeight/2) {
            hovering = true;

            if(InputManager.keyPressed(InputManager.LEFT_CLICK)) {
                clicked = true;
                buttonAction();
            } else if(InputManager.keyHeld(InputManager.LEFT_CLICK)) {
                clicked = true;
            } else {
                clicked = false;
            }
        } else {
            clicked = false;
        }

        if(clicked)
            sprite.setImageIndex(2);
        else if(hovering)
            sprite.setImageIndex(1);
        else
            sprite.setImageIndex(0);
    }

    @Override
    public void drawSelf() {
        sprite.setPosition(worldX + width/2, worldY + height/2);
        sprite.draw();
    }
}
