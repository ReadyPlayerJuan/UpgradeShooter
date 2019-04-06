package main;

public enum SettingType {
    RESOLUTION_WIDTH    (0),
    RESOLUTION_HEIGHT   (1),
    IS_FULLSCREEN       (2);

    private int index;
    SettingType(int index) {
        this.index = index;
    }
    public int getIndex() { return index; }
}
