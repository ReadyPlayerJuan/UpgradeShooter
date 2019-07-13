package main.menus.pause_menu;

import main.game.PlayerData;
import main.menus.BlankUI;
import main.menus.UI;
import rendering.Graphics;
import rendering.textures.SpriteData;

public class EquipmentSectionUI extends BlankUI {
    private final int frameCornerSize = 30;
    private final int borderMargin = 10;
    private final double weaponInfoWidthPct = 0.30;

    private final int weaponInfoWidth, weaponInfoHeight;

    private WeaponInfoBoxUI weaponInfoLeft, weaponInfoRight;

    public EquipmentSectionUI(UI parent, float relativeX, float relativeY, int width, int height) {
        super(parent, relativeX, relativeY, width, height);

        weaponInfoWidth = (int)((width - borderMargin*2) * weaponInfoWidthPct);
        weaponInfoHeight = height - borderMargin*2;

        weaponInfoLeft = new WeaponInfoBoxUI(this, SpriteData.BUTTON_9S, PlayerData.getEquippedWeapon(0),
                Graphics.mediumFont, Graphics.smallFont,
                borderMargin, borderMargin, weaponInfoWidth, weaponInfoHeight, frameCornerSize);
        weaponInfoRight = new WeaponInfoBoxUI(this, SpriteData.BUTTON_9S, PlayerData.getEquippedWeapon(1),
                Graphics.mediumFont, Graphics.smallFont,
                width - weaponInfoWidth - borderMargin, borderMargin, weaponInfoWidth, weaponInfoHeight, frameCornerSize);
    }

    @Override
    public void updateSelf(double delta) {

    }

    @Override
    public void drawSelf() {

    }
}
