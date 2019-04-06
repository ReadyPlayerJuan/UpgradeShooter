package main.game.boards;

public class Camera {
    private Board board;

    private float zoom;
    private float centerX, centerY;
    private int viewWidth, viewHeight;

    public Camera(Board board, int viewWidth, int viewHeight) {
        this.board = board;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        centerX = 0.0f;
        centerY = 0.0f;
        zoom = 1.0f;
    }

    public void update(double delta) {

    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getZoom() {
        return zoom;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }
}
