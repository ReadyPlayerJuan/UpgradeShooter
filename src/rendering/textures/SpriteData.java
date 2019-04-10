package rendering.textures;

public enum SpriteData {
    PLAYER ("test_sprite_1", 2, true);


    private String fileName;
    private int numRows;
    private boolean hasTransparency;
    private SpriteTexture texture = null;

    SpriteData(String fileName, int numRows, boolean hasTransparency) {
        this.fileName = fileName;
        this.numRows = numRows;
        this.hasTransparency = hasTransparency;
    }

    public void load() {
        texture = TextureManager.loadTexture(fileName, numRows, hasTransparency);
    }

    /*public void cleanUp() {
        if(texture != null) {
            texture.destroy();
            texture = null;
        }
    }*/

    public String getFileName() {
        return fileName;
    }

    public int getNumRows() {
        return numRows;
    }

    public SpriteTexture getTexture() {
        if(texture == null) {
            System.out.println("Attempting to access texture before loading: " + fileName + ". Loading now.");
            load();
        }
        return texture;
    }

    public static void loadAll() {
        for(SpriteData s: SpriteData.values()) {
            s.load();
        }
    }

    /*public static void cleanUpAll() {
        for(SpriteData s: SpriteData.values()) {
            s.cleanUp();
        }
    }*/
}
