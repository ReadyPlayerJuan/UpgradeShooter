package main.views;

import main.menus.ButtonUI;
import main.menus.FrameUI;
import main.menus.UI;
import org.lwjgl.opengl.GL11;
import rendering.Graphics;
import rendering.fonts.TextField;
import rendering.textures.SpriteData;

public class MainMenuView extends View {
    private TextField title;
    private FrameUI mainUI;
    private ButtonUI startButton;

    public MainMenuView(View parentView, int width, int height) {
        super(parentView, width, height);

        title = new TextField(Graphics.titleFont, width, 1);
        title.setTextAlign(TextField.CENTER, TextField.TOP);

        mainUI = new FrameUI(null, 0, 0, width, height);
        startButton = new ButtonUI(mainUI, SpriteData.BUTTON_9S, 200, height/2, 300, 80, 30) {
            @Override
            public void buttonAction() {
                sendViewAction("main menu - start game");
            }
        };
    }

    @Override
    public void updateSelf(double delta) {
        title.setText("TITLE GOES HERE");
        mainUI.update(delta);
    }

    @Override
    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        GL11.glClearColor(0.8f, 0.8f, 0.8f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glColor3f(0, 0, 0);
        title.drawText((int)(width*0.5), (int)(height*0.8));

        GL11.glColor3f(1, 1, 1);
        mainUI.draw();

        mainFrameBuffer.unbindFrameBuffer();
    }

    @Override
    public void processViewAction(String action) {
        sendViewAction(action); //pass action up the chain
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
}
