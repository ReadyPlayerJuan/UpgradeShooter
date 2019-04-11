package main.views;

import main.util.EfficiencyMetrics;
import rendering.Graphics;
import rendering.WindowManager;
import rendering.fonts.TextField;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;

import static org.lwjgl.opengl.GL11.*;

public class MainView extends View {
    private View gameView;
    private View mainMenuView;

    private TextField debugTextField;

    public MainView(int width, int height) {
        super(null, width, height);
        isFocused = true;

        debugTextField = new TextField(Graphics.debugFont, 500, EfficiencyMetrics.processData.size()*4 + 1);
        debugTextField.setBufferAlign(TextField.TOP_LEFT);

        gameView = new GameView(this, width, height);
        addSubView(gameView);
        mainMenuView = new MainMenuView(this, width, height);
        addSubView(mainMenuView);

        setFocusedSubView(mainMenuView);
    }

    @Override
    public void updateSelf(double delta) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FPS ").append(WindowManager.getFps()).append("\n");
        for(int i = 0; i < EfficiencyMetrics.processData.size(); i++) {
            stringBuilder.append(EfficiencyMetrics.processData.get(i));
        }

        debugTextField.setText(stringBuilder.toString());
    }

    @Override
    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        glClearColor(0f, 0f, 1f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glColor4f(1, 1, 1, 1);
        if(mainMenuView.isFocused())
            mainMenuView.getMainFrameBuffer().draw(width/2, height/2);
        else if(gameView.isFocused())
            gameView.getMainFrameBuffer().draw(width/2, height/2);

        glColor3f(1f, 0f, 1f);
        debugTextField.drawText(0, height);

        mainFrameBuffer.unbindFrameBuffer();
    }

    @Override
    public void processViewAction(String action) {
        if(action.equals("main menu - start game")) {
            setFocusedSubView(gameView);
        } else {
            System.out.println("UNKNOWN VIEW ACTION: " + action);
            System.exit(1);
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
}
