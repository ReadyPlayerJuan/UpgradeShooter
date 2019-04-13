package main.views;

import main.menus.ButtonUI;
import main.menus.BlankUI;
import main.menus.MenuFrameUI;
import org.lwjgl.opengl.GL11;
import rendering.Graphics;
import rendering.fonts.TextField;
import rendering.textures.SpriteData;

public class PauseMenuView extends View {
    private final int frameCornerSize = 30;
    private final int borderMargin = 40;
    private final int numSections = 3;
    private final int sectionHeight = 120;
    private final int sectionSeparation = 12;

    private final int bodyWidth, bodyHeight;
    private final int sectionWidth;

    private BlankUI mainUI;
    private ButtonUI equipmentSection;
    private ButtonUI inventorySection;
    private ButtonUI upgradesSection;
    private MenuFrameUI bodyFrame;

    public PauseMenuView(View parentView, int width, int height) {
        super(parentView, width, height);

        bodyWidth = width - borderMargin*2;
        bodyHeight = height - borderMargin*2 - sectionHeight;
        sectionWidth = bodyWidth - (numSections+1) * sectionSeparation;

        mainUI = new BlankUI(null, 0, 0, width, height);

        int i = 0;
        equipmentSection = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + (i+1)*sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click equipment section");
            }
        }; i++;

        inventorySection = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + (i+1)*sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click inventory section");
            }
        }; i++;

        upgradesSection = new ButtonUI(mainUI, SpriteData.BUTTON_9S, borderMargin + (i+1)*sectionSeparation, borderMargin, sectionWidth, sectionHeight, frameCornerSize) {
            @Override
            public void buttonAction() {
                System.out.println("click upgrades section");
            }
        };

        bodyFrame = new MenuFrameUI(mainUI, SpriteData.BUTTON_9S, borderMargin, borderMargin + sectionHeight, bodyWidth, bodyHeight, frameCornerSize);
    }

    @Override
    public void updateSelf(double delta) {
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

        } else {
            sendViewAction(action); //pass action up the chain
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
}
