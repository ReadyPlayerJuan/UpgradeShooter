package main.menus.pause_menu;

import main.game.weapons.Weapon;
import main.menus.MenuFrameUI;
import main.menus.UI;
import rendering.Graphics;
import rendering.fonts.TextField;
import rendering.fonts.TrueTypeFont;
import rendering.textures.SpriteData;

public class WeaponInfoBoxUI extends MenuFrameUI {
    private final int borderMargin = 10;

    private Weapon weapon;
    private TrueTypeFont titleFont;
    private TrueTypeFont infoFont;

    private TextField weaponName;
    private TextField weaponInfoLeft, weaponInfoRight;

    public WeaponInfoBoxUI(UI parent, SpriteData spriteData, Weapon weapon, TrueTypeFont titleFont, TrueTypeFont infoFont, float relativeX, float relativeY, int width, int height, int cornerSize) {
        super(parent, spriteData, relativeX, relativeY, width, height, cornerSize);
        this.weapon = weapon;
        this.titleFont = titleFont;
        this.infoFont = infoFont;

        weaponName = new TextField(titleFont, width-borderMargin*2, 1);
        weaponName.setTextAlign(TextField.CENTER, TextField.TOP);

        weaponInfoLeft = new TextField(infoFont, width/2-borderMargin, (height-borderMargin*2 - titleFont.getFontHeight()) / infoFont.getFontHeight());
        weaponInfoLeft.setTextAlign(TextField.RIGHT, TextField.TOP);

        weaponInfoRight = new TextField(infoFont, width/2-borderMargin, (height-borderMargin*2 - titleFont.getFontHeight()) / infoFont.getFontHeight());
        weaponInfoRight.setTextAlign(TextField.LEFT, TextField.TOP);
    }

    @Override
    public void updateSelf(double delta) {
        if(weapon != null) {
            weaponName.setText(weapon.getName());
            weaponInfoLeft.setText(weapon.getStatNamesString());
            weaponInfoRight.setText(weapon.getStatValuesString());
        }
    }

    @Override
    public void drawSelf() {
        super.drawSelf(); //draw frame sprite

        if(weapon != null) {
            Graphics.setColor(0, 0, 0, 1);
            weaponName.drawText((int)worldX+borderMargin, (int)worldY+borderMargin);
            weaponInfoLeft.drawText((int)worldX+borderMargin, (int)worldY+borderMargin + titleFont.getFontHeight());
            weaponInfoRight.drawText((int)worldX+0 + width / 2, (int)worldY+borderMargin + titleFont.getFontHeight());
        }
    }
}
