package main.menus;

import main.input.InputManager;
import rendering.textures.NineSliceSprite;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

public class MenuFrameUI extends UI {
    protected Sprite sprite;

    public MenuFrameUI(UI parent, SpriteData spriteData, float relativeX, float relativeY, int width, int height, int cornerSize) {
        super(parent, relativeX, relativeY, 0, 0);

        this.width = width;
        this.height = height;

        sprite = new NineSliceSprite(spriteData).setCornerSize(cornerSize).setSize(width, height);
    }

    @Override
    public void updateSelf(double delta) {

    }

    @Override
    public void drawSelf() {
        sprite.setPosition(worldX + width/2, worldY + height/2);
        sprite.draw();
    }
}
