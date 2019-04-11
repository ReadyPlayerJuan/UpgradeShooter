package main.views;

import main.game.boards.Board;
import main.game.boards.BoardPreset1;
import main.game.boards.Camera;
import main.game.entities.EntityManager;
import main.game.entities.Player;
import main.game.entities.enemies.Dummy;
import main.util.EfficiencyMetricType;
import main.util.EfficiencyMetrics;
import org.lwjgl.opengl.GL11;

public class GameView extends View {
    private EntityManager entityManager;
    private Board board;
    private Camera camera;

    public GameView(View parentView, int width, int height) {
        super(parentView, width, height);

        entityManager = new EntityManager();
        board = new BoardPreset1(this);
        camera = new Camera(board, width, height);

        Player p = new Player();
        p.setCamera(camera);
        camera.follow(p, 16);

        new Dummy(100, 100);
        new Dummy(100, 150);
        new Dummy(100, 200);
        new Dummy(100, 250);
        new Dummy(100, 300);
    }

    @Override
    public void updateSelf(double delta) {
        board.update(delta);

        EfficiencyMetrics.startTimer(EfficiencyMetricType.UPDATE_ENTITIES);
        entityManager.updateEntities(delta, board);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.UPDATE_ENTITIES);

        camera.update(delta);
    }

    @Override
    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        GL11.glClearColor(1, 1, 1, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        EfficiencyMetrics.startTimer(EfficiencyMetricType.DRAW_WALLS);
        board.render(camera);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.DRAW_WALLS);

        EfficiencyMetrics.startTimer(EfficiencyMetricType.DRAW_ENTITIES);
        entityManager.render(camera);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.DRAW_ENTITIES);

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
