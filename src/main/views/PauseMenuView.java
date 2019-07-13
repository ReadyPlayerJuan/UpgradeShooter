package main.views;

import main.menus.ButtonUI;
import main.menus.BlankUI;
import main.menus.MenuFrameUI;
import main.menus.pause_menu.EquipmentSectionUI;
import rendering.Graphics;
import rendering.textures.SpriteData;

public class PauseMenuView extends View {
    private final int frameCornerSize = 30;
    private final int borderMargin = 25;
    private final int numSections = 3;
    private final int sectionHeight = 100;
    private final int sectionSeparation = 15;

    private final int bodyWidth, bodyHeight;
    private final int sectionWidth;

    private BlankUI mainUI;
    private ButtonUI equipmentSectionButton;
    private ButtonUI inventorySectionButton;
    private ButtonUI upgradesSectionButton;
    private MenuFrameUI bodyFrame;
    private EquipmentSectionUI equipmentSection;

    public PauseMenuView(View parentView, int width, int height) {
        super(parentView, width, height);

        bodyWidth = width - borderMargin*2;
        bodyHeight = height - borderMargin*2 - sectionHeight;
        sectionWidth = (bodyWidth - (numSections+1) * sectionSeparation) / numSections;

        mainUI = new BlankUI(null, 0, 0, width, height);

        int i = 0;
        equipmentSectionButton = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + i*(sectionSeparation+sectionWidth)+sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click equipment section");
            }
        }; i++;

        inventorySectionButton = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + i*(sectionSeparation+sectionWidth)+sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click inventory section");
            }
        }; i++;

        upgradesSectionButton = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + i*(sectionSeparation+sectionWidth)+sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click upgrades section");
            }
        };

        bodyFrame = new MenuFrameUI(mainUI, SpriteData.BUTTON_9S, borderMargin, borderMargin + sectionHeight, bodyWidth, bodyHeight, frameCornerSize);

        equipmentSection = new EquipmentSectionUI(bodyFrame, 0, 0, bodyWidth, bodyHeight);

        updateSelf(0);
    }

    @Override
    public void updateSelf(double delta) {
        //mainUI.setRelativePosition((float)InputManager.mouseX, (float)InputManager.mouseY);
        mainUI.update(delta);
    }

    @Override
    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        Graphics.clear(0, 0, 0, 0);

        mainUI.draw();

        mainFrameBuffer.unbindFrameBuffer();
    }

    @Override
    public void processViewAction(String action) {
        if(action.equals("reset pause menu")) {
            updateSelf(0);
        } else {
            sendViewAction(action); //pass action up the chain
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
}
