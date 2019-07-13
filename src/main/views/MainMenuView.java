package main.views;

import main.menus.ButtonUI;
import main.menus.BlankUI;
import rendering.Graphics;
import rendering.fonts.TextField;
import rendering.textures.SpriteData;

public class MainMenuView extends View {
    private TextField title;
    private BlankUI mainUI;
    private ButtonUI startButton;

    public MainMenuView(View parentView, int width, int height) {
        super(parentView, width, height);

        title = new TextField(Graphics.titleFont, width, 1);
        title.setBufferAlign(TextField.CENTER);
        title.setTextAlign(TextField.CENTER, TextField.TOP);

        mainUI = new BlankUI(null, 0, 0, width, height);
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
        Graphics.clear(0.8f, 0.8f, 0.8f, 1);

        Graphics.setColor(0, 0, 0, 1);
        title.drawText((int)(width*0.5), (int)(height*0.2));

        Graphics.setColor(1, 1, 1, 1);
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
