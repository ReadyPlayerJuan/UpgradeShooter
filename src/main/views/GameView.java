package main.views;

import main.game.boards.Board;
import main.game.boards.BoardPreset1;
import main.game.boards.Camera;
import main.game.entities.EntityManager;
import main.game.entities.Player;
import org.lwjgl.opengl.GL11;
import rendering.FrameBuffer;

public class GameView extends View {
    private EntityManager entityManager;
    private Board board;
    private Camera camera;

    private FrameBuffer layerFrameBuffer;

    public GameView(int width, int height) {
        super(width, height);

        layerFrameBuffer = new FrameBuffer(width, height);

        entityManager = new EntityManager();
        board = new BoardPreset1(this);
        camera = new Camera(board, width, height);

        new Player();
    }

    public void updateSelf(double delta) {
        board.update(delta);
        entityManager.updateEntities(delta, board);
        camera.update(delta);
    }

    public void drawSelf() {
        layerFrameBuffer.bindFrameBuffer();
        GL11.glClearColor(1, 1, 1, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        board.render(camera);
        entityManager.drawEntities(camera);

        layerFrameBuffer.unbindFrameBuffer();



        mainFrameBuffer.bindFrameBuffer();
        layerFrameBuffer.draw(width/2, height/2, width * camera.getZoom(), height * camera.getZoom());
        mainFrameBuffer.unbindFrameBuffer();
    }

    public void cleanUp() {
        super.cleanUp();
    }
}
