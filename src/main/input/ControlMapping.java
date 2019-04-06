package main.input;

public enum ControlMapping {
    MOVE_LEFT (65),
    MOVE_RIGHT (68),
    MOVE_UP (87),
    MOVE_DOWN (83);

    public int keyCode;
    ControlMapping(int key) {
        keyCode = key;
    }
}
