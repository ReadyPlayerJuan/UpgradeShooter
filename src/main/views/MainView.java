package main.views;

import main.util.EfficiencyMetricType;
import main.util.EfficiencyMetrics;
import rendering.FrameBuffer;
import rendering.WindowManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class MainView extends View {
    public View gameView;

    private FrameBuffer layerFrameBuffer;

    public MainView(int width, int height) {
        super(width, height);

        layerFrameBuffer = new FrameBuffer(width, height);

        gameView = new GameView(width, height);
        addSubView(gameView);

        gameView.setFocused(true); //gameview recieves input
    }

    public void updateSelf(double delta) {

    }

    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        //WindowManager.setRenderTarget(mainFrameBuffer);
        glClearColor(0f, 0f, 1f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gameView.getMainFrameBuffer().draw(width/2, height/2);

        glColor3f(1f, 0f, 1f);
        WindowManager.debugFont.drawText("FPS " + WindowManager.getFps(), 0, height-WindowManager.debugFontSize);
        for(int i = 0; i < EfficiencyMetrics.processData.size(); i++) {
            String data = EfficiencyMetrics.processData.get(i);
            WindowManager.debugFont.drawText(data, 0, height-WindowManager.debugFontSize*(i*4 + 2));
        }

        mainFrameBuffer.unbindFrameBuffer();
    }

    public void cleanUp() {
        super.cleanUp();
    }
}
