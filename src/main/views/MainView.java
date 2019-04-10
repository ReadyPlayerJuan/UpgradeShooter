package main.views;

import main.util.EfficiencyMetricType;
import main.util.EfficiencyMetrics;
import rendering.FrameBuffer;
import rendering.WindowManager;
import rendering.fonts.TextField;

import static org.lwjgl.opengl.GL11.*;

public class MainView extends View {
    public View gameView;

    private TextField debugTextField;
    private TextField testTextField;
    private FrameBuffer layerFrameBuffer;

    public MainView(int width, int height) {
        super(width, height);

        debugTextField = new TextField(WindowManager.debugFont, 500, EfficiencyMetrics.processData.size()*4 + 1);
        debugTextField.setBufferAlign(TextField.TOP_LEFT);

        layerFrameBuffer = new FrameBuffer(width, height);

        gameView = new GameView(width, height);
        addSubView(gameView);

        gameView.setFocused(true); //gameview recieves input
    }

    public void updateSelf(double delta) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FPS " + WindowManager.getFps() + "\n");
        for(int i = 0; i < EfficiencyMetrics.processData.size(); i++) {
            stringBuilder.append(EfficiencyMetrics.processData.get(i));
        }

        debugTextField.setText(stringBuilder.toString());
    }

    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        //WindowManager.setRenderTarget(mainFrameBuffer);
        glClearColor(0f, 0f, 1f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glColor4f(1, 1, 1, 1);
        gameView.getMainFrameBuffer().draw(width/2, height/2);

        glColor3f(1f, 0f, 1f);
        debugTextField.drawText(0, height);

        mainFrameBuffer.unbindFrameBuffer();
    }

    public void cleanUp() {
        super.cleanUp();
    }
}
