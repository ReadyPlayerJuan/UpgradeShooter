package rendering.shaders.entity_shader;

import main.game.boards.Camera;
import rendering.shaders.ShaderProgram;
import rendering.textures.Sprite;
import rendering.textures.SpriteTexture;

public class EntityShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/rendering/shaders/entity_shader/entityVertex.txt";
    private static final String GEOMETRY_FILE = "src/rendering/shaders/entity_shader/entityGeometry.txt";
    private static final String FRAGMENT_FILE = "src/rendering/shaders/entity_shader/entityFragment.txt";

    private int location_camera_position;
    private int location_view_size;
    private int location_texture_div;
    private int location_texture;

    public EntityShader() {
        super(VERTEX_FILE, GEOMETRY_FILE, FRAGMENT_FILE);
        start();
        connectTextureUnits();
        stop();
    }

    @Override
    protected void getAllUniformLocations() {
        location_camera_position = super.getUniformLocation("cameraPosition");
        location_view_size = super.getUniformLocation("viewSize");
        location_texture_div = super.getUniformLocation("textureDiv");
        location_texture = super.getUniformLocation("texture");
    }

    public void setCameraAndViewSize(Camera camera) {
        setViewSize(camera.getViewWidth() / camera.getZoom(), camera.getViewHeight() / camera.getZoom());
        setCamera(camera);
    }

    public void setViewSize(float w, float h) {
        super.loadVector(location_view_size, w, h);
    }

    public void setCamera(Camera camera) {
        super.loadVector(location_camera_position, (float)camera.getCenterX(), (float)camera.getCenterY());
    }

    public void setTextureDivisions(int numRows, int numCols) {
        super.loadVector(location_texture_div, numRows, numCols);
    }

    protected void connectTextureUnits() {
        super.loadInt(location_texture, 0);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "worldPosition");
        super.bindAttribute(1, "size");
        super.bindAttribute(2, "rotation");
        super.bindAttribute(3, "imageIndex");
    }
}
