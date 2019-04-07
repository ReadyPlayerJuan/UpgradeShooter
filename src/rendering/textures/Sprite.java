package rendering.textures;

public class Sprite {
    private SpriteTexture texture;
    private double x, y;
    private double rotation, scale;
    private int imageIndex;

    public Sprite(SpriteData spriteData, double x, double y, double rotation, double scale, int imageIndex) {
        this.texture = spriteData.getTexture();
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scale = scale;
        this.imageIndex = imageIndex;
    }

    public Sprite(SpriteData spriteData, double x, double y, double scale) {
        this(spriteData, x, y, 0, scale, 0);
    }

    public Sprite(SpriteData spriteData) {
        this(spriteData, 0, 0, 0, 1, 0);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public SpriteTexture getTexture() {
        return texture;
    }
}
