package main.game.boards;

import main.game.entities.Entity;

public class Camera {
    private Board board;

    private float zoom;
    private double centerX, centerY;
    private int viewWidth, viewHeight;

    private double followSpeed;
    private Entity following;

    public Camera(Board board, int viewWidth, int viewHeight) {
        this.board = board;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        centerX = 0.0;
        centerY = 0.0;
        zoom = 1.0f;
    }

    public void setPosition(double x, double y) {
        centerX = x;
        centerY = y;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void update(double delta) {
        //delta = 99;//1.0 / 60;
        if(following != null) {
            double targetX = following.getX();
            double targetY = following.getY();
            centerX += Math.signum(targetX - centerX) * Math.min(1, delta * followSpeed) * Math.abs(targetX - centerX);
            centerY += Math.signum(targetY - centerY) * Math.min(1, delta * followSpeed) * Math.abs(targetY - centerY);
        }
    }

    public void follow(Entity entity, double followSpeed) {
        this.following = entity;
        this.followSpeed = followSpeed;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
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
