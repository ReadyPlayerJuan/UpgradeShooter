package main.views;

import main.game.PlayerData;
import main.game.boards.Board;
import main.game.boards.BoardPreset1;
import main.game.boards.Camera;
import main.game.entities.EntityManager;
import main.game.entities.Player;
import main.game.entities.enemies.Dummy;
import main.game.weapons.guns.Pistol;
import main.input.ControlMapping;
import main.input.InputManager;
import main.util.EfficiencyMetricType;
import main.util.EfficiencyMetrics;
import rendering.Graphics;

public class GameView extends View {
    private EntityManager entityManager;
    private Board board;
    private Camera camera;

    private boolean paused = false;
    private PauseMenuView pauseMenuView;

    public GameView(View parentView, int width, int height) {
        super(parentView, width, height);

        PlayerData.setEquippedWeapon(0, new Pistol());

        pauseMenuView = new PauseMenuView(this, width, height);
        pauseMenuView.setFocused(false);
        addSubView(pauseMenuView);

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

        if(InputManager.keyPressed(ControlMapping.PAUSE_GAME.keyCode)) {
            if(paused) {
                paused = false;
                pauseMenuView.setFocused(false);
            } else {
                paused = true;
                pauseMenuView.setFocused(true);
                pauseMenuView.processViewAction("reset pause menu");
            }
        }
        EfficiencyMetrics.startTimer(EfficiencyMetricType.UPDATE_ENTITIES);
        if(!paused)
            entityManager.updateEntities(delta, board);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.UPDATE_ENTITIES);

        if(!paused)
            camera.update(delta);
    }

    @Override
    public void drawSelf() {
        mainFrameBuffer.bindFrameBuffer();
        Graphics.clear(1, 1, 1, 1);

        EfficiencyMetrics.startTimer(EfficiencyMetricType.DRAW_WALLS);
        board.render(camera);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.DRAW_WALLS);

        EfficiencyMetrics.startTimer(EfficiencyMetricType.DRAW_ENTITIES);
        entityManager.render(camera);
        EfficiencyMetrics.stopTimer(EfficiencyMetricType.DRAW_ENTITIES);

        if(paused) {
            Graphics.setColor(1, 1, 1, 1);
            Graphics.enableBlend();
            pauseMenuView.getMainFrameBuffer().draw(width/2, height/2);
            Graphics.disableBlend();
        }

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
