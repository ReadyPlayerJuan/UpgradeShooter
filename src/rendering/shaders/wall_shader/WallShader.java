package rendering.shaders.wall_shader;

import main.game.boards.Camera;
import rendering.shaders.ShaderProgram;

public class WallShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/rendering/shaders/wall_shader/wallVertex.txt";
    private static final String FRAGMENT_FILE = "src/rendering/shaders/wall_shader/wallFragment.txt";

    private int location_color;
    private int location_view_size;
    private int location_camera_position;

    public WallShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_view_size = super.getUniformLocation("view_size");
        location_camera_position = super.getUniformLocation("camera_position");
    }

    public void setColor(float r, float g, float b) {
        super.loadVector(location_color, r, g, b);
    }

    public void setCameraAndViewSize(Camera camera) {
        setViewSize(camera.getViewWidth() / camera.getZoom(), camera.getViewHeight() / camera.getZoom());
        setCamera(camera);
    }

    public void setViewSize(float w, float h) {
        super.loadVector(location_view_size, w, h);
    }

    public void setCamera(Camera camera) {
        super.loadVector(location_camera_position,
                (float)(camera.getCenterX() - (camera.getViewWidth() / camera.getZoom() / 2)),
                (float)(camera.getCenterY() - (camera.getViewHeight() / camera.getZoom() / 2)));
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
