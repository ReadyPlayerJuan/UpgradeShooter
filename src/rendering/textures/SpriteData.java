package rendering.textures;

public enum SpriteData {
    PLAYER ("test_sprite_1",    2, 2, true),
    BUTTON ("button",           2, 2, false),
    BUTTON_9S ("button_9s",     2, 2, false);


    private String fileName;
    private int numRows, numCols;
    private boolean hasTransparency;
    private SpriteTexture texture = null;

    SpriteData(String fileName, int numRows, int numCols, boolean hasTransparency) {
        this.fileName = fileName;
        this.numRows = numRows;
        this.numCols = numCols;
        this.hasTransparency = hasTransparency;
    }

    public void load() {
        texture = TextureManager.loadTexture(fileName, numRows, numCols, hasTransparency);
    }

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
}
